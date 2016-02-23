package de.zabuza.soundEngine;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import de.zabuza.soundEngine.sample.SoundSample;

/**
 * SoundEngine. Provides playing .wav mono sounds and .midi or .mid. Will create
 * a own stereo interpretation of sounds with the x- and y-coords of the
 * {@link SoundSource}.
 * 
 * @author Zabuza
 * 
 */
public abstract class SoundEngine implements Runnable {

	/**
	 * Amount of all midi channels.
	 */
	private static final int MIDI_CHANNELS = 16;
	/**
	 * Number of the midi volume controller.
	 */
	private static final int MIDI_VOLUME_CONTROLLER = 7;
	/**
	 * Byte factor of midi data messages.
	 */
	private static final int MIDI_DATA_FACTOR = 127;

	/**
	 * Systems Midi Sequencer.
	 */
	private Sequencer sequencer;
	/**
	 * Receiver of systems Midi sequencer.
	 */
	private Receiver receiver;
	/**
	 * Current volume of sounds.
	 */
	private float soundVolume = 1.0f;
	/**
	 * Current volume of music.
	 */
	private float musicVolume = 1.0f;

	/**
	 * Updates the sounds and plays the next bytes. Should be triggered by a
	 * tick-system, like 24 ticks per seconds.
	 */
	public abstract void clientTick();

	/**
	 * Gets the current music volume.
	 * 
	 * @return Current music volume from 0.0 to 1.0
	 */
	public final float getMusicVolume() {
		return musicVolume;
	}

	/**
	 * Gets the current sound volume.
	 * 
	 * @return Current sound volume from 0.0 to 1.0
	 */
	public final float getSoundVolume() {
		return soundVolume;
	}

	/**
	 * Loads and returns a sample from an url.
	 * 
	 * @param resourceName
	 *            URL of the sample
	 * @return Loaded SoundSample from the url
	 */
	public abstract SoundSample loadSample(final String resourceName);

	/**
	 * Loads and returns a Midi Sequence from an InputStream. Will patch the
	 * loaded Sequence so it will not make problems.
	 * 
	 * @param sequenceStream
	 *            Sequences InputStream to load from
	 * @return Loaded and patched sequence
	 */
	public final Sequence loadSequence(final InputStream sequenceStream) {
		try {
			return patchSequence(MidiSystem.getSequence(sequenceStream));
		} catch (InvalidMidiDataException | IOException e) {
			e.printStackTrace();
			return null;
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
	public abstract void play(final SoundSample sample,
			final SoundSource soundSource, final float priority,
			final float thatRate);

	@Override
	public abstract void run();

	/**
	 * Sets the SoundListener of SoundEngines sounds.
	 * 
	 * @param soundListener
	 *            SoundListener of SoundEngines sounds
	 */
	public abstract void setListener(final SoundListener soundListener);

	/**
	 * Sets the current music volume.
	 * 
	 * @param volume
	 *            Music volume to set from 0.0 to 1.0
	 */
	public final void setMusicVolume(final float volume) {
		if (volume > 1.0f) {
			musicVolume = 1.0f;
		} else if (volume < 0f) {
			musicVolume = 0f;
		} else {
			musicVolume = volume;
		}

		ShortMessage volMessage = new ShortMessage();

		for (int i = 0; i < MIDI_CHANNELS; i++) {
			try {
				// Send a control change message with the new
				// value to all midi channels
				volMessage.setMessage(ShortMessage.CONTROL_CHANGE, i,
						MIDI_VOLUME_CONTROLLER,
						(int) (musicVolume * MIDI_DATA_FACTOR));
			} catch (InvalidMidiDataException e) {
				return;
			}
			receiver.send(volMessage, -1);
		}
	}

	/**
	 * Sets the current sound volume.
	 * 
	 * @param volume
	 *            Sound volume to set from 0.0 to 1.0
	 */
	public final void setSoundVolume(final float volume) {
		if (volume > 1.0f) {
			soundVolume = 1.0f;
		}
		if (volume < 0f) {
			soundVolume = 0f;
		} else {
			soundVolume = volume;
		}
	}

	/**
	 * Shuts the SoundEngine down.
	 */
	public abstract void shutDown();

	/**
	 * Plays a new Midi Sequence and loops it continuously.
	 * 
	 * @param sequence
	 *            Sequence to play
	 */
	public final void startMusic(final Sequence sequence) {
		stopMusic();
		if (sequencer != null) {
			try {
				sequencer.open();
				sequencer.getTransmitter().setReceiver(receiver);
				sequencer.setSequence((Sequence) null);
				sequencer.setSequence(sequence);
				sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
				sequencer.start();
			} catch (Exception e) {
				return;
			}
		}
	}

	/**
	 * Stops the current played Midi Sequence.
	 */
	public final void stopMusic() {
		if (sequencer != null) {
			try {
				sequencer.stop();
				sequencer.close();
			} catch (Exception e) {
				return;
			}
		}
	}

	/**
	 * Patchs a Midi Sequence and removes all data from it which would make
	 * problems. All messages which use the Controller 7 (CONTROL_CHANGE) will
	 * be removed. Should be used before loading a Sequence in the Sequencer. Is
	 * used by loadSequence(InputStream).
	 * 
	 * @param sequence
	 *            Sequence to patch
	 * @return Patched Sequence
	 */
	private Sequence patchSequence(final Sequence sequence) {
		for (Track track : sequence.getTracks()) {
			int i = 0;
			while (i < track.size()) {
				if (track.get(i).getMessage().getMessage()[1] == MIDI_VOLUME_CONTROLLER) {
					if (!track.remove(track.get(i))) {
						i++;
					}
				} else {
					i++;
				}
			}
		}
		return sequence;
	}

	/**
	 * Gets the receiver of the current used Midi sequencer.
	 * 
	 * @return Receiver of the current used Midi sequencer.
	 */
	protected final Receiver getReceiver() {
		return receiver;
	}

	/**
	 * Gets the current used Midi sequencer.
	 * 
	 * @return Current used Midi sequencer.
	 */
	protected final Sequencer getSequencer() {
		return sequencer;
	}

	/**
	 * Sets the receiver of the current used Midi sequencer.
	 * 
	 * @param thatReceiver
	 *            Receiver to set
	 */
	protected final void setReceiver(final Receiver thatReceiver) {
		this.receiver = thatReceiver;
	}

	/**
	 * Sets the current used Midi sequencer.
	 * 
	 * @param thatSequencer
	 *            Midi sequencer to set
	 */
	protected final void setSequencer(final Sequencer thatSequencer) {
		this.sequencer = thatSequencer;
	}

	/**
	 * Updates the sounds and plays the next bytes in SoundEngines own Thread
	 * while its running.
	 */
	protected abstract void tick();
}