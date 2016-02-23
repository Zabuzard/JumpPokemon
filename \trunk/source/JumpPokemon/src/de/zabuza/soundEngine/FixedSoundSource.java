package de.zabuza.soundEngine;

/**
 * FixedSoundSource Used if a sound should be played but client is no
 * SoundSource or does not provide x- and y-coords.
 * 
 * @author Zabuza
 * 
 */
public class FixedSoundSource implements SoundSource {
	/**
	 * X-coord of sounds source.
	 */
	private float x;
	/**
	 * Y-coord of sounds source.
	 */
	private float y;

	/**
	 * Creates a new FixedSoundSource with given coordinates.
	 * 
	 * @param thatX
	 *            X-coord of the {@link SoundSource}
	 * @param thatY
	 *            Y-coord of the {@link SoundSource}
	 */
	public FixedSoundSource(final float thatX, final float thatY) {
		this.x = thatX;
		this.y = thatY;
	}

	/**
	 * Creates a new FixedSoundSource with another SoundSource. Can be used if
	 * client wants to use another SoundSource than itself.
	 * 
	 * @param soundSource
	 *            SoundSource of the sound
	 */
	public FixedSoundSource(final SoundSource soundSource) {
		this.x = soundSource.getX();
		this.y = soundSource.getY();
	}

	@Override
	public final float getX() {
		return x;
	}

	@Override
	public final float getY() {
		return y;
	}
}