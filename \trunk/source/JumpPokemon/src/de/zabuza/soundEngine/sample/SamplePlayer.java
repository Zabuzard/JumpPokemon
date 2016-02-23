package de.zabuza.soundEngine.sample;

import de.zabuza.soundEngine.SoundProducer;

/**
 * SamplePlayer class. Plays SoundSamples.
 * 
 * @author Zabuza
 * 
 */
public class SamplePlayer implements SoundProducer {
	/**
	 * Sample to play.
	 */
	private SoundSample sample;
	/**
	 * Position in the sample.
	 */
	private float pos = 0;
	/**
	 * True if the sample is alive.
	 */
	private boolean alive = true;
	/**
	 * Rate if the sample.
	 */
	private float rate;

	/**
	 * Creates a new SamplePlayer with a SoundSample and a rate.
	 * 
	 * @param thatSample
	 *            SoundSample to play
	 * @param thatRate
	 *            Rate of the sound
	 */
	public SamplePlayer(final SoundSample thatSample, final float thatRate) {
		this.rate = thatRate;
		this.sample = thatSample;
	}

	@Override
	public final boolean isLive() {
		return alive;
	}

	@Override
	public final float read(final float[] buf, final int readRate) {
		float step = (sample.getRate() * rate) / readRate;

		for (int i = 0; i < buf.length; i++) {
			if (pos >= sample.getBuf().length) {
				buf[i] = 0;
				alive = false;
			} else {
				buf[i] = sample.getBuf()[(int) pos];
			}
			pos += step;
		}

		return 1;
	}

	@Override
	public final void skip(final int samplesToSkip, final int readRate) {
		float step = sample.getRate() / readRate;
		pos += step * samplesToSkip;

		if (pos >= sample.getBuf().length) {
			alive = false;
		}
	}
}