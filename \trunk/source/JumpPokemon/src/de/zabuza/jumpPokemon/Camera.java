package de.zabuza.jumpPokemon;

import de.zabuza.jumpPokemon.level.Level;
import de.zabuza.jumpPokemon.sprites.Sprite;

/**
 * Camera for LevelScenes. Follows a sprite by centering it in the cameras
 * watching area. Will use the bounds of the current {@link Level}.
 * 
 * @author Zabuza
 * 
 */
public class Camera {

	/**
	 * Left x-coord of cameras watching area.
	 */
	private int x;
	/**
	 * Bottom y-coord of cameras watching area.
	 */
	private int y;
	/**
	 * Width of cameras watching area.
	 */
	private int width;
	/**
	 * Height of cameras watching area.
	 */
	private int height;
	/**
	 * Level where camera is displayed, needed for the level bounds.
	 */
	private Level level;
	/**
	 * Sprite to center and follow by the camera.
	 */
	private Sprite sprite;

	/**
	 * Creates a camera which will follow a sprite and uses the bounds of the
	 * {@link Level}.
	 * 
	 * @param thatLevel
	 *            Level where the camera is used. Needs levelbounds.
	 * @param thatSprite
	 *            Sprite to follow and center in the cameras watching area
	 * @param thatWidth
	 *            Width of the camera
	 * @param thatHeight
	 *            Height of the camera
	 */
	public Camera(final Level thatLevel, final Sprite thatSprite,
			final int thatWidth, final int thatHeight) {
		this.level = thatLevel;
		this.sprite = thatSprite;
		width = thatWidth;
		height = thatHeight;
	}

	/**
	 * Updates the watching area of the camera to follow the sprite.
	 */
	public final void follow() {
		
//		setX((int) (sprite.getX() + (sprite.getWidth() / 2) - (width / 2)));
//		setY((int) (sprite.getY() + (sprite.getHeight() / 2) - (height / 2)));
		setX((int) sprite.getX() - (width / 2));
		setY((int) sprite.getY() - (height / 2));

	}

	/**
	 * Gets the height of cameras watching area.
	 * 
	 * @return Height of cameras watching area
	 */
	public final int getHeight() {
		return height;
	}

	/**
	 * Gets the width of cameras watching area.
	 * 
	 * @return Width of cameras watching area
	 */
	public final int getWidth() {
		return width;
	}

	/**
	 * Gets left x-coord of cameras watching area.
	 * 
	 * @return Left x-coord of cameras watching area
	 */
	public final int getX() {
		return this.x;
	}

	/**
	 * Gets bottom y-coord of cameras watching area.
	 * 
	 * @return Bottom y-coord of cameras watching area
	 */
	public final int getY() {
		return this.y;
	}

	/**
	 * Sets the left x-coord of cameras watching area. Will use the bounds of
	 * the {@link Level}.
	 * 
	 * @param thatX
	 *            Left x-coord of cameras watching area
	 */
	private void setX(final int thatX) {
		if (thatX < 0) {
			this.x = 0;
		} else if (thatX > (level.getWidth() * Commons.TILE_SIZE) - width) {
			this.x = (level.getWidth() * Commons.TILE_SIZE) - width;
		} else {
			this.x = thatX;
		}
	}

	/**
	 * Sets the bottom y-coord of cameras watching area. Will use the bounds of
	 * the {@link Level}.
	 * 
	 * @param thatY
	 *            Bottom y-coord of cameras watching area
	 */
	private void setY(final int thatY) {
		if (thatY < 0) {
			this.y = 0;
		} else if (thatY > (level.getHeight() * Commons.TILE_SIZE) - height) {
			this.y = (level.getHeight() * Commons.TILE_SIZE) - height;
		} else {
			this.y = thatY;
		}
	}
}