package de.zabuza.soundEngine.mixer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.zabuza.soundEngine.SoundListener;
import de.zabuza.soundEngine.SoundProducer;
import de.zabuza.soundEngine.SoundSource;
import de.zabuza.soundEngine.StereoSoundProducer;

/**
 * Organisates all sounds which should be played. Produces the stereo sound out
 * of mono sounds.
 * 
 * @author Zabuza
 * 
 */
public class ListenerMixer implements StereoSoundProducer {

	/**
	 * Mixers list of all sounds.
	 */
	private List<Sound> sounds = new ArrayList<Sound>();
	/**
	 * Sound buffer.
	 */
	private float[] buf = new float[0];
	/**
	 * Maximal sound channels of the mixer.
	 */
	private int maxChannels;
	/**
	 * Current SoundListener of the mixer.
	 */
	private SoundListener soundListener;

	/**
	 * Create a new ListenerMixer with a maximum amount of sound channels.
	 * 
	 * @param thatMaxChannels
	 *            Maximum amount of sound channels
	 */
	public ListenerMixer(final int thatMaxChannels) {
		this.maxChannels = thatMaxChannels;
	}

	/**
	 * Adds a new sound in its SoundProducer with its SoundSource, volume and
	 * priority.
	 * 
	 * @param producer
	 *            SoundProducer which will produce the sound
	 * @param soundSource
	 *            SoundSource of the producer
	 * @param volume
	 *            Volume of the producer
	 * @param priority
	 *            Priority of the producer
	 */
	public final void addSoundProducer(final SoundProducer producer,
			final SoundSource soundSource, final float volume,
			final float priority) {
		sounds.add(new Sound(producer, soundSource, volume, priority));
	}

	@Override
	public final float read(final float[] leftBuf, final float[] rightBuf,
			final int readRate) {
		if (buf.length != leftBuf.length) {
			buf = new float[leftBuf.length];
		}

		if (sounds.size() > maxChannels) {
			Collections.sort(sounds);
		}

		Arrays.fill(leftBuf, 0);
		Arrays.fill(rightBuf, 0);
		float maxAmplitude = 0;
		for (int i = 0; i < sounds.size(); i++) {
			Sound sound = sounds.get(i);
			if (i < maxChannels) {
				sound.read(buf, readRate);

				float rp = sound.getAmplitude();
				float lp = sound.getAmplitude();

				if (sound.getPan() >= 0) {
					rp *= (1 - sound.getPan());
				}
				if (sound.getPan() <= 0) {
					lp *= (1 + sound.getPan());
				}

				for (int j = 0; j < leftBuf.length; j++) {
					leftBuf[j] += buf[j] * lp;
					rightBuf[j] += buf[j] * rp;
					if (leftBuf[j] > maxAmplitude) {
						maxAmplitude = leftBuf[j];
					}
					if (rightBuf[j] > maxAmplitude) {
						maxAmplitude = rightBuf[j];
					}
				}
			} else {
				sound.skip(leftBuf.length, readRate);
			}
		}

		return maxAmplitude;
	}

	/**
	 * Sets the SoundListener of the ListenerMixer.
	 * 
	 * @param thatSoundListener
	 *            SoundListener which who will listen to the sounds
	 */
	public final void setSoundListener(final SoundListener thatSoundListener) {
		this.soundListener = thatSoundListener;
	}

	@Override
	public final void skip(final int samplesToSkip, final int readRate) {
		for (int i = 0; i < sounds.size(); i++) {
			Sound sound = sounds.get(i);
			sound.skip(samplesToSkip, readRate);
		}
	}

	/**
	 * Updates all sounds and removes the sounds which are not alive anymore.
	 */
	public final void update() {
		for (Iterator<Sound> it = sounds.iterator(); it.hasNext();) {
			Sound sound = it.next();
			sound.update(soundListener);
			if (!sound.isLive()) {
				it.remove();
			}
		}
	}
}