package de.zabuza.soundEngine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

import de.zabuza.soundEngine.mixer.ListenerMixer;
import de.zabuza.soundEngine.sample.SampleLoader;
import de.zabuza.soundEngine.sample.SamplePlayer;
import de.zabuza.soundEngine.sample.SoundSample;

/**
 * WorkingSoundEngine. Provides playing .wav mono sounds and .midi or .mid. Will
 * create a own stereo interpretation of sounds with the x- and y-coords of the
 * {@link SoundSource}.
 * 
 * @author Zabuza
 * 
 */
public class WorkingSoundEngine extends SoundEngine {

	/**
	 * Sounds rate.
	 */
	private static final int SOUND_RATE = 44100;
	/**
	 * Divider which converts the sound rate into the buffer size.
	 */
	private static final int BUF_SIZE_CONV_DIV = 100;
	/**
	 * Factor which increases the buffer size.
	 */
	private static final int BUF_SIZE_FACTOR = 4;
	/**
	 * Amount of sound channels, 1 for mono and 2 for stereo.
	 */
	private static final int SOUND_CHANNELS = 2;
	/**
	 * The number of bits in each sample.
	 */
	private static final int SAMPLE_BIT_SIZE = 16;
	/**
	 * Priority of SoundEngines thread.
	 */
	private static final int THREAD_PRIORITY = 10;
	/**
	 * Soundbuffers gain.
	 */
	private static final int GAIN = 32000;
	/**
	 * Sound buffers offset from gain to its bounds.
	 */
	private static final int SOUND_BUF_OFFSET = 767;
	/**
	 * Bounds of the left and right sound buffer.
	 */
	private static final int SOUND_BUF_BOUND = GAIN + SOUND_BUF_OFFSET;

	/**
	 * Silent sound sample, used if an error occured due the creation of a sound
	 * sample.
	 */
	private SoundSample silentSample;
	/**
	 * Sounds rate.
	 */
	private int rate = SOUND_RATE;
	/**
	 * Mixers sound dataline, will read all bytes from there.
	 */
	private SourceDataLine sdl;
	/**
	 * ListenerMixer which contains and creates all sounds.
	 */
	private ListenerMixer listenerMixer;
	/**
	 * Size of the buffer (10 milis).
	 */
	private int bufferSize = rate / BUF_SIZE_CONV_DIV;
	/**
	 * Buffer of the sound.
	 */
	private ByteBuffer soundBuffer = ByteBuffer.allocate(bufferSize
			* BUF_SIZE_FACTOR);
	/**
	 * Left and right buffer of the sound.
	 */
	private float[] leftBuf, rightBuf;
	/**
	 * True if the SoundEngine is alive and not shutted down.
	 */
	private boolean alive = true;

	// private float amplitude = 1;
	// private float targetAmplitude = 1;

	/**
	 * Creates a new WorkingSoundEngine with given maximal channels for sounds.
	 * 
	 * @param maxChannels
	 *            Maximal amount of channels for sounds
	 * @throws LineUnavailableException
	 *             If line can not be opened. This situation arises most
	 *             commonly when a requested line is already in use by another
	 *             application.
	 */
	public WorkingSoundEngine(final int maxChannels)
			throws LineUnavailableException {
		silentSample = new SoundSample(new float[] { 0 }, rate);
		Mixer mixer = AudioSystem.getMixer(null);

		sdl = (SourceDataLine) mixer
				.getLine(new Line.Info(SourceDataLine.class));
		sdl.open(new AudioFormat(rate, SAMPLE_BIT_SIZE, SOUND_CHANNELS, true,
				false), bufferSize * 2 * 2 * 2 * 2 * 2);
		soundBuffer.order(ByteOrder.LITTLE_ENDIAN);
		sdl.start();

		/*
		 * try { FloatControl volumeControl = (FloatControl) sdl.getControl(
		 * FloatControl.Type.MASTER_GAIN);
		 * volumeControl.setValue(volumeControl.getMaximum()); } catch
		 * (IllegalArgumentException e) {
		 * System.out.println("Failed to set the sound volume"); }
		 */

		listenerMixer = new ListenerMixer(maxChannels);

		leftBuf = new float[bufferSize];
		rightBuf = new float[bufferSize];

		// Create Midi sequencer and wire it to the receiver
		// to provide Midi-messages to it
		try {
			setSequencer(MidiSystem.getSequencer(false));
			setReceiver(MidiSystem.getReceiver());
			getSequencer().open();
			getSequencer().getTransmitter().setReceiver(getReceiver());
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}

		Thread thread = new Thread(this);
		thread.setDaemon(true);
		thread.setPriority(THREAD_PRIORITY);
		thread.start();
	}

	/**
	 * Protected constructor with no implementation.
	 */
	protected WorkingSoundEngine() {
	}

	/**
	 * Updates the sounds and plays the next bytes. Should be triggered by a
	 * tick-system, like 24 ticks per seconds.
	 */
	@Override
	public final void clientTick() {
		synchronized (listenerMixer) {
			listenerMixer.update();
		}
	}

	/**
	 * Loads and returns a sample from an url.
	 * 
	 * @param resourceName
	 *            URL of the sample
	 * @return Loaded SoundSample from the url
	 */
	@Override
	public final SoundSample loadSample(final String resourceName) {
		try {
			return SampleLoader.loadSample(resourceName);
		} catch (Exception e) {
			System.out.println("Failed to load sample " + resourceName
					+ ". Using silent sample");
			e.printStackTrace();
			return silentSample;
		}
	}

	/**
	 * Plays a SoundSample from a SoundSource with a rate and priority.
	 * 
	 * @param sample
	 *            SoundSample to play
	 * @param soundSource
	 *            SoundSource of the sample
	 * @param priority
	 *            Priority of the sample
	 * @param thatRate
	 *            Rate of the sample
	 */
	@Override
	public final void play(final SoundSample sample,
			final SoundSource soundSource, final float priority,
			final float thatRate) {
		synchronized (listenerMixer) {
			listenerMixer.addSoundProducer(new SamplePlayer(sample, thatRate),
					soundSource, getSoundVolume(), priority);
		}
	}

	@Override
	public final void run() {
		while (alive) {
			tick();
		}
	}

	/**
	 * Sets the SoundListener of SoundEngines sounds.
	 * 
	 * @param soundListener
	 *            SoundListener of SoundEngines sounds
	 */
	@Override
	public final void setListener(final SoundListener soundListener) {
		listenerMixer.setSoundListener(soundListener);
	}

	/**
	 * Shuts the SoundEngine down.
	 */
	@Override
	public final void shutDown() {
		alive = false;
	}

	/**
	 * Updates the sounds and plays the next bytes in SoundEngines own Thread
	 * while its running.
	 */
	@Override
	protected final void tick() {
		soundBuffer.clear();

		// targetAmplitude = (targetAmplitude - 1) * 0.9f + 1;
		// targetAmplitude = (targetAmplitude - 1) * 0.9f + 1;
		synchronized (listenerMixer) {
			@SuppressWarnings("unused")
			float maxAmplitude = listenerMixer.read(leftBuf, rightBuf, rate);
			// if (maxAmplitude > targetAmplitude) {
			// targetAmplitude = maxAmplitude;
			// }
		}

		soundBuffer.clear();
		float gain = GAIN;
		for (int i = 0; i < bufferSize; i++) {
			// amplitude += (targetAmplitude - amplitude) / rate;
			// amplitude = 1;
			// float gain = 30000;
			int l = (int) (leftBuf[i] * gain);
			int r = (int) (rightBuf[i] * gain);
			if (l > SOUND_BUF_BOUND) {
				l = SOUND_BUF_BOUND;
			}
			if (r > SOUND_BUF_BOUND) {
				r = SOUND_BUF_BOUND;
			}
			if (l < -SOUND_BUF_BOUND) {
				l = -SOUND_BUF_BOUND;
			}
			if (r < -SOUND_BUF_BOUND) {
				r = -SOUND_BUF_BOUND;
			}
			soundBuffer.putShort((short) l);
			soundBuffer.putShort((short) r);
		}

		sdl.write(soundBuffer.array(), 0, bufferSize * 2 * 2);
	}
}