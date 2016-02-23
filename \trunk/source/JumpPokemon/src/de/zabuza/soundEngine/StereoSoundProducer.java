package de.zabuza.soundEngine;

/**
 * Interface for StereoSoundProducers. Used to produce stereo sounds out of mono
 * sounds.
 * 
 * @author Zabuza
 * 
 */
public interface StereoSoundProducer {

	/**
	 * Reads the next sounds in the left and right stereo buffers with a reading
	 * rate.
	 * 
	 * @param leftBuf
	 *            Left sound buffer of the stereo sound
	 * @param rightBuf
	 *            Right sound buffer of the stereo sound
	 * @param readRate
	 *            Reading rate
	 * @return Maximal amplitude
	 */
	float read(float[] leftBuf, float[] rightBuf, int readRate);

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