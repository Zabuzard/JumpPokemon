package de.zabuza.soundEngine;

/**
 * Interface for SoundSources. Used to create a own stereo interpretation of
 * mono sounds.
 * 
 * @author Zabuza
 * 
 */
public interface SoundSource {
	/**
	 * Gets the x-coordinate of the SoundSource.
	 * 
	 * @return X-coordinate of the SoundSource
	 */
	float getX();

	/**
	 * Gets the y-coordinate of the SoundSource.
	 * 
	 * @return Y-coordinate of the SoundSource
	 */
	float getY();
}