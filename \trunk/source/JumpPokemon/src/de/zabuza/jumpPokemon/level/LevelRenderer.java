package de.zabuza.jumpPokemon.level;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Transparency;

import de.zabuza.jumpPokemon.Art;
import de.zabuza.jumpPokemon.Camera;
import de.zabuza.jumpPokemon.Commons;

/**
 * LevelRenderer class. Renders a level. Has camera support.
 * 
 * @author Zabuza
 * 
 */
public class LevelRenderer {

	/**
	 * Hexadezimal for FF.
	 */
	private static final int HEX_FF = 0xFF;
	/**
	 * Height of the tile data rects.
	 */
	private static final int RECT_HEIGHT = 4;

	/**
	 * X-coord of the camera.
	 */
	private int camX;
	/**
	 * Y-coord of the camera.
	 */
	private int camY;
	/**
	 * Image in which all rendering is done.
	 */
	private Image image;
	/**
	 * Graphics object of the rendering image.
	 */
	private Graphics2D g;
	/**
	 * Transparent color.
	 */
	private static final Color TRANSPARENT = new Color(0, 0, 0, 0);
	/**
	 * Level to render.
	 */
	private Level level;

	/**
	 * True if tiles data should be rendered, e.g. for debbuging purpose.
	 */
	private boolean renderBehaviors = false;

	/**
	 * Width of the camera.
	 */
	private int width;
	/**
	 * Height of the camera.
	 */
	private int height;

	/**
	 * Creates a new LevelRenderer which renders a custom level with custom
	 * width and height.
	 * 
	 * @param thatLevel
	 *            Level to render
	 * @param graphicsConfiguration
	 *            Current used Configuration
	 * @param thatWidth
	 *            Width of the camera
	 * @param thatHeight
	 *            Height of the camera
	 */
	public LevelRenderer(final Level thatLevel,
			final GraphicsConfiguration graphicsConfiguration,
			final int thatWidth, final int thatHeight) {
		this.width = thatWidth;
		this.height = thatHeight;

		this.level = thatLevel;
		image = graphicsConfiguration.createCompatibleImage(thatWidth,
				thatHeight, Transparency.BITMASK);
		g = (Graphics2D) image.getGraphics();
		g.setComposite(AlphaComposite.Src);

		updateArea(0, 0, thatWidth, thatHeight);
	}

	/**
	 * Renders the level on a {@link Graphics} object.
	 * 
	 * @param thatG
	 *            Graphics object to draw on
	 * @param alpha
	 *            1 if all ticks for the second are done, 0 if none is done, 0.5
	 *            if half etc.
	 */
	public final void render(final Graphics thatG, final float alpha) {
		thatG.drawImage(image, 0, 0, null);

		for (int x = camX / Commons.TILE_SIZE; x <= (camX + width)
				/ Commons.TILE_SIZE; x++) {
			for (int y = camY / Commons.TILE_SIZE; y <= (camY + height)
					/ Commons.TILE_SIZE; y++) {
				byte b = level.getBlock(x, y);

				if (renderBehaviors) {
					if (((Level.getTileBehaviors(b & HEX_FF)) & Level.BIT_BLOCK_UPPER) > 0) {
						thatG.setColor(Color.RED);
						thatG.fillRect((x * Commons.TILE_SIZE) - camX,
								(y * Commons.TILE_SIZE) - camY,
								Commons.TILE_SIZE, RECT_HEIGHT);
					}
					if (((Level.getTileBehaviors(b & HEX_FF)) & Level.BIT_BLOCK_ALL) > 0) {
						thatG.setColor(Color.RED);
						thatG.fillRect((x * Commons.TILE_SIZE) - camX,
								(y * Commons.TILE_SIZE) - camY,
								Commons.TILE_SIZE, RECT_HEIGHT);
						thatG.fillRect((x * Commons.TILE_SIZE) - camX,
								(y * Commons.TILE_SIZE) - camY
										+ Commons.TILE_SIZE - RECT_HEIGHT,
								Commons.TILE_SIZE, RECT_HEIGHT);
						thatG.fillRect((x * Commons.TILE_SIZE) - camX,
								(y * Commons.TILE_SIZE) - camY, RECT_HEIGHT,
								Commons.TILE_SIZE);
						thatG.fillRect((x * Commons.TILE_SIZE) - camX
								+ Commons.TILE_SIZE - RECT_HEIGHT,
								(y * Commons.TILE_SIZE) - camY, RECT_HEIGHT,
								Commons.TILE_SIZE);
					}
					if (((Level.getTileBehaviors(b & HEX_FF)) & Level.BIT_BLOCK_LOWER) > 0) {
						thatG.setColor(Color.RED);
						thatG.fillRect((x * Commons.TILE_SIZE) - camX,
								(y * Commons.TILE_SIZE) - camY
										+ Commons.TILE_SIZE - RECT_HEIGHT,
								Commons.TILE_SIZE, RECT_HEIGHT);
					}
					if (((Level.getTileBehaviors(b & HEX_FF)) & Level.BIT_SPECIAL) > 0) {
						thatG.setColor(Color.PINK);
						thatG.fillRect((x * Commons.TILE_SIZE) - camX
								+ RECT_HEIGHT + RECT_HEIGHT + 2,
								(y * Commons.TILE_SIZE) - camY + RECT_HEIGHT
										+ (RECT_HEIGHT * 2), RECT_HEIGHT * 2,
								RECT_HEIGHT * 2);
					}
					if (((Level.getTileBehaviors(b & HEX_FF)) & Level.BIT_BUMPABLE) > 0) {
						thatG.setColor(Color.BLUE);
						thatG.fillRect((x * Commons.TILE_SIZE) - camX
								+ RECT_HEIGHT, (y * Commons.TILE_SIZE) - camY
								+ RECT_HEIGHT, RECT_HEIGHT * 2, RECT_HEIGHT * 2);
					}
					if (((Level.getTileBehaviors(b & HEX_FF)) & Level.BIT_BREAKABLE) > 0) {
						thatG.setColor(Color.GREEN);
						thatG.fillRect((x * Commons.TILE_SIZE) - camX
								+ RECT_HEIGHT + (RECT_HEIGHT * 2),
								(y * Commons.TILE_SIZE) - camY + RECT_HEIGHT,
								RECT_HEIGHT * 2, RECT_HEIGHT * 2);
					}
					if (((Level.getTileBehaviors(b & HEX_FF)) & Level.BIT_PICKUPABLE) > 0) {
						thatG.setColor(Color.YELLOW);
						thatG.fillRect((x * Commons.TILE_SIZE) - camX
								+ RECT_HEIGHT, (y * Commons.TILE_SIZE) - camY
								+ RECT_HEIGHT + (RECT_HEIGHT * 2),
								RECT_HEIGHT * 2, RECT_HEIGHT * 2);
					}
					if (((Level.getTileBehaviors(b & HEX_FF)) & Level.BIT_ANIMATED) > 0) {
						return;
					}
				}

			}
		}
	}

	/**
	 * Repaints the component.
	 * 
	 * @param x0
	 *            Left x-coord of the area as tiles
	 * @param y0
	 *            Bottom y-coord of the area as tiles
	 * @param w
	 *            Width of the area as tiles
	 * @param h
	 *            Height of the area as tiles
	 */
	public final void repaint(final int x0, final int y0, final int w,
			final int h) {
		updateArea(x0 * Commons.TILE_SIZE - camX,
				y0 * Commons.TILE_SIZE - camY, w * Commons.TILE_SIZE, h
						* Commons.TILE_SIZE);
	}

	/**
	 * Sets the current used camera, does not follow automaticly.
	 * 
	 * @param cam
	 *            Cameras watching area to render
	 */
	public final void setCam(final Camera cam) {

		int xCamD = this.camX - cam.getX();
		int yCamD = this.camY - cam.getY();

		this.camX = cam.getX();
		this.camY = cam.getY();

		g.setComposite(AlphaComposite.Src);
		g.copyArea(0, 0, width, height, xCamD, yCamD);

		if (xCamD < 0) {
			if (xCamD < -width) {
				xCamD = -width;
			}
			updateArea(width + xCamD, 0, -xCamD, height);
		} else if (xCamD > 0) {
			if (xCamD > width) {
				xCamD = width;
			}
			updateArea(0, 0, xCamD, height);
		}

		if (yCamD < 0) {
			if (yCamD < -width) {
				yCamD = -width;
			}
			updateArea(0, height + yCamD, width, -yCamD);
		} else if (yCamD > 0) {
			if (yCamD > width) {
				yCamD = width;
			}
			updateArea(0, 0, width, yCamD);
		}
	}

	/**
	 * Sets the level to render.
	 * 
	 * @param thatLevel
	 *            Level to set
	 */
	public final void setLevel(final Level thatLevel) {
		this.level = thatLevel;
		updateArea(0, 0, width, height);
	}

	/**
	 * Sets the rendering behaviors. If true tile data will be rendered, e.g.
	 * for debbugin purpose.
	 * 
	 * @param thatRenderBehaviors
	 *            True if tile data should be rendered
	 */
	public final void setRenderBehaviors(final boolean thatRenderBehaviors) {
		this.renderBehaviors = thatRenderBehaviors;
	}

	/**
	 * Updates the current displayed area by drawing into the rendering image.
	 * 
	 * @param x0
	 *            Left x-coord of the area
	 * @param y0
	 *            Bottom y-coord of the area
	 * @param w
	 *            Width of the area
	 * @param h
	 *            Height of the area
	 */
	private void updateArea(final int x0, final int y0, final int w, final int h) {
		g.setBackground(TRANSPARENT);
		g.clearRect(x0, y0, w, h);
		int xTileStart = (x0 + camX) / Commons.TILE_SIZE;
		int yTileStart = (y0 + camY) / Commons.TILE_SIZE;
		int xTileEnd = (x0 + camX + w) / Commons.TILE_SIZE;
		int yTileEnd = (y0 + camY + h) / Commons.TILE_SIZE;
		for (int x = xTileStart; x <= xTileEnd; x++) {
			for (int y = yTileStart; y <= yTileEnd; y++) {
				int b = level.getBlock(x, y) & HEX_FF;
				if (((Level.getTileBehaviors(b)) & Level.BIT_ANIMATED) == 0) {
					g.drawImage(Art.level[b / Art.level.length][b
							% Art.level[0].length], (x * Commons.TILE_SIZE)
							- camX, (y * Commons.TILE_SIZE) - camY, null);
				}
			}
		}
	}
}