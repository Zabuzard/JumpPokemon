package de.zabuza.soundEngine;

/**
 * Interface for SoundProducers.
 * 
 * @author Zabuza
 * 
 */
public interface SoundProducer {

	/**
	 * Returns if the sample is still alive.
	 * 
	 * @return True if sample is alive
	 */
	boolean isLive();

	/**
	 * Reads the next sounds in the buffer with a reading rate.
	 * 
	 * @param buf
	 *            Sound buffer of the sound
	 * @param readRate
	 *            Reading rate
	 * @return Maximal amplitude
	 */
	float read(float[] buf, int readRate);

	/**
	 * Skips samples with a reading rate.
	 * 
	 * @param samplesToSkip
	 *            Samples to skip
	 * @param readRate
	 *            Reading rate
	 */
	void skip(int samplesToSkip, int readRate);
}