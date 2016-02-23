package de.zabuza.jumpPokemon.scenes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import de.zabuza.jumpPokemon.Art;
import de.zabuza.jumpPokemon.Commons;
import de.zabuza.jumpPokemon.JumpPkmnComponent;

/**
 * TitleScene of the game.
 * 
 * @author Zabuza
 * 
 */
public class TitleScene extends Scene {

	/**
	 * Y-coord of the title font image.
	 */
	private static final int TITLE_FONT_Y = 100;
	/**
	 * Index of the black row in the font.
	 */
	private static final int FONT_BLACK_ROW = 0;
	/**
	 * Index of the white row in the font.
	 */
	private static final int FONT_WHITE_ROW = 7;
	/**
	 * Blinking intervall of the starting text in ticks.
	 */
	private static final int TEXT_BLINK_INTERVALL = 16;
	/**
	 * Y offset of the starting text from mid of the screen.
	 */
	private static final int TEXT_Y_OFFSET = 100;
	/**
	 * Time when scrolling starts in seconds.
	 */
	private static final int SCROLL_START = 3;

	/**
	 * Component which created the Scene.
	 */
	private JumpPkmnComponent component;
	/**
	 * X-coord of the screen mid.
	 */
	private int midX = Commons.WIDTH / 2;
	/**
	 * y-coord of the screen mid.
	 */
	private int midY = Commons.HEIGHT / 2;
	/**
	 * Background image for titlescreen.
	 */
	private Image pic = Art.titleScreen;
	/**
	 * Tick when level is started, needed because start sound needs some time.
	 */
	private int startTick;
	/**
	 * Top pixel to display.
	 */
	private int camY = pic.getHeight(null) - Commons.HEIGHT;
	/**
	 * Current tick of the scene.
	 */
	private int tick;
	/**
	 * True if the cam scrolls.
	 */
	private boolean scrolling = false;
	/**
	 * True if client wants to start the game.
	 */
	private boolean start = false;
	/**
	 * True if the starting key was pressed.
	 */
	private boolean keyWasDown = true;

	/**
	 * Creates a new TitleScene.
	 * 
	 * @param thatComponent
	 *            Component which created the scene.
	 */
	public TitleScene(final JumpPkmnComponent thatComponent) {
		this.component = thatComponent;
	}

	@Override
	public final float getX() {
		return 0;
	}

	@Override
	public final float getY() {
		return 0;
	}

	@Override
	public final void init() {
		getSound().startMusic(Art.songs[0]);
	}

	@Override
	public final void render(final Graphics g, final float alpha) {
		// First color all black
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Commons.WIDTH, Commons.HEIGHT);

		// Draw background image centered, use camY to display
		// the max visible part, beginning at left bottom
		g.drawImage(pic, midX - (pic.getWidth(null) / 2), 0,
				midX + (pic.getWidth(null) / 2), Commons.HEIGHT, 0, camY,
				pic.getWidth(null), camY + Commons.HEIGHT, null);

		// Draw titleFont centered
		g.drawImage(Art.titleFont, midX - (Art.titleFont.getWidth(null) / 2),
				TITLE_FONT_Y, null);

		// Create blinking text centered
		if ((tick / TEXT_BLINK_INTERVALL) % 2 == 0 && scrolling) {
			String msg = "Press Space";
			component.drawString(g, msg,
					midX - msg.length() * (Art.font[0][0].getWidth(null) / 2)
							+ 1, midY + TEXT_Y_OFFSET + 1, FONT_BLACK_ROW);
			component.drawString(g, msg,
					midX - msg.length() * (Art.font[0][0].getWidth(null) / 2),
					midY + TEXT_Y_OFFSET, FONT_WHITE_ROW);
		}
	}

	@Override
	public final void tick() {
		tick++;
		// Start level if scrolling started and startTick is reached
		if (start) {
			if (tick >= startTick && start) {
				component.startLevel();
			}
		} else {
			// Start only if screen scrolls already
			if (!keyWasDown && getKeys()[Commons.KEY_JUMP] && scrolling) {
				getSound().play(Art.samples[Art.SAMPLE_GET_COIN], this, 1, 1);
				start = true;
				// Sound should be played trough once in startTick
				startTick = tick + Commons.TICKS_PER_SECOND;
			}
			if (getKeys()[Commons.KEY_JUMP]) {
				keyWasDown = false;
			}
			// Beginn to scroll after 3 seconds
			if (tick / Commons.TICKS_PER_SECOND >= SCROLL_START && !scrolling) {
				scrolling = true;
			}
			// Scroll displayed part by decreasing camY
			if (scrolling && camY > 2) {
				camY -= 2;
			}
		}
	}
}
