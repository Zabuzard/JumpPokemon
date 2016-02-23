package de.zabuza.soundEngine.sample;

/**
 * SoundSample class. Representates a .wav mono sound sample
 * 
 * @author Zabuza
 * 
 */
public class SoundSample {
	/**
	 * Buffer of the sample.
	 */
	private final float[] buf;
	/**
	 * Rate of the sample.
	 */
	private final float rate;

	/**
	 * Creates a new SoundSample with a sound buffer and a rate.
	 * 
	 * @param thatBuf
	 *            Sound buffer of the sample
	 * @param thatRate
	 *            Rate of the sample
	 */
	public SoundSample(final float[] thatBuf, final float thatRate) {
		this.buf = thatBuf;
		this.rate = thatRate;
	}

	/**
	 * Gets the samples buffer.
	 * 
	 * @return Samples buffer
	 */
	protected final float[] getBuf() {
		return buf;
	}

	/**
	 * Gets the samples rate.
	 * 
	 * @return Samples rate
	 */
	protected final float getRate() {
		return rate;
	}
}