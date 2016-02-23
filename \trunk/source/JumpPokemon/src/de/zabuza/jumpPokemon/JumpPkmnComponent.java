package de.zabuza.jumpPokemon;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.sound.sampled.LineUnavailableException;

import de.zabuza.jumpPokemon.menu.Menu;
import de.zabuza.jumpPokemon.scenes.LevelScene;
import de.zabuza.jumpPokemon.scenes.Scene;
import de.zabuza.jumpPokemon.scenes.TitleScene;
import de.zabuza.physicEngine.PhysicEngine;
import de.zabuza.soundEngine.FakeSoundEngine;
import de.zabuza.soundEngine.SoundEngine;
import de.zabuza.soundEngine.WorkingSoundEngine;

/**
 * Main component of the game.
 * 
 * @author Zabuza
 * 
 */
public class JumpPkmnComponent extends Canvas implements Runnable, KeyListener,
		FocusListener {

	/**
	 * Versions UID.
	 */
	private static final long serialVersionUID = 4421312591970156136L;
	/**
	 * Maximal sound channels.
	 */
	private static final int MAX_CHANNELS = 64;
	/**
	 * Character offset to the font Image[].
	 */
	private static final int CHAR_OFFSET = 32;
	/**
	 * Converts seconds into nanoseconds if multiplied with.
	 */
	private static final double SECOND_TO_NANO = 1000000000.0;
	/**
	 * Returns 90% if multiplied with.
	 */
	private static final float NINETY_PERCENT = 0.9f;
	/**
	 * Returns 10% if multiplied with.
	 */
	private static final float TEN_PERCENT = 0.1f;
	/**
	 * Index of the black row in the font.
	 */
	private static final int FONT_BLACK_ROW = 0;
	/**
	 * Index of the white row in the font.
	 */
	private static final int FONT_WHITE_ROW = 7;
	/**
	 * X- and y-coordinates where the FPS will be displayed.
	 */
	private static final int FPS_XY = 5;
	/**
	 * Game loop delay which is used in Thread.sleep(milis).
	 */
	private static final int GAME_LOOP_DELAY = 5;

	/**
	 * True if the game is running.
	 */
	private boolean running = false;
	/**
	 * True if the game is paused.
	 */
	private boolean paused = false;
	/**
	 * GraphicsConfiguration of the system.
	 */
	private GraphicsConfiguration gc;
	/**
	 * Current used Scene.
	 */
	private Scene scene;
	/**
	 * Current used SoundEngine.
	 */
	private SoundEngine sound;
	/**
	 * Current used PhysicEngine.
	 */
	private PhysicEngine physic;
	/**
	 * Current used Settings.
	 */
	private Settings settings;
	// XXX Remove SuppressWarnings("unused"), evtl. it will be
	// used in future but now the alert icon annoys
	/**
	 * True when game frame is focused by client.
	 */
	@SuppressWarnings("unused")
	private boolean focused = true;
	/**
	 * True when Menu is open.
	 */
	private boolean inMenu;
	/**
	 * Current used Menu.
	 */
	private Menu menu;
	/**
	 * True if can enter Menu.
	 */
	private boolean canMenu = true;

	/**
	 * Inits the Game component. Use method start() to start the component.
	 * 
	 * @param width
	 *            Width of the component
	 * @param height
	 *            Height of the component
	 */
	public JumpPkmnComponent(final int width, final int height) {
		setEnabled(true);

		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);

		physic = new PhysicEngine();

		try {
			sound = new WorkingSoundEngine(MAX_CHANNELS);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			sound = new FakeSoundEngine();
		}

		setFocusable(true);
	}

	/**
	 * Closes the menu.
	 */
	public final void closeMenu() {
		settings.saveSettings(this);
		menu.setVisible(false);
		paused = false;
	}

	/**
	 * Uses a font ressource (see {@link Art}) to draw a String in single
	 * images.
	 * 
	 * @param g
	 *            Graphics to draw with
	 * @param text
	 *            String which should be drawed
	 * @param x
	 *            X-Coord to draw the string
	 * @param y
	 *            Y-Coord to draw the string
	 * @param c
	 *            Row-Index of the ressource array (see {@link Art})
	 */
	public final void drawString(final Graphics g, final String text,
			final int x, final int y, final int c) {
		char[] ch = text.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			g.drawImage(Art.font[c][ch[i] - CHAR_OFFSET], x + i
					* Art.font[c][0].getWidth(null), y, null);
		}
	}

	@Override
	public final void focusGained(final FocusEvent e) {
		focused = true;
	}

	@Override
	public final void focusLost(final FocusEvent e) {
		focused = false;
		// Reset all keys if focus lost
		for (int i = 0; i < Scene.getKeys().length; i++) {
			Scene.getKeys()[i] = false;
		}
	}

	/**
	 * Gets the current used SoundEngine.
	 * 
	 * @return Current used SoundEngine
	 */
	public final SoundEngine getSoundEngine() {
		return sound;
	}

	@Override
	public final void keyPressed(final KeyEvent e) {
		toggleKey(e.getKeyCode(), true);
	}

	@Override
	public final void keyReleased(final KeyEvent e) {
		toggleKey(e.getKeyCode(), false);
	}

	@Override
	public void keyTyped(final KeyEvent e) {
	}

	/**
	 * Opens the menu.
	 */
	public final void openMenu() {
		menu.setVisible(true);
		paused = true;
	}

	@Override
	public void paint(final Graphics g) {
	}

	/**
	 * Renders the component on a {@link Graphics} object.
	 * 
	 * @param g
	 *            Graphics object to draw on
	 * @param alpha
	 *            1 if all ticks for second are done, 0 if none is done, 0.5 if
	 *            half etc.
	 */
	public final void render(final Graphics g, final float alpha) {
		scene.render(g, alpha);
		if (menu.isVisible()) {
			menu.render(g, alpha);
		}
	}

	/**
	 * Game loop, invoked by the start method.
	 */
	@Override
	public final void run() {
		createBufferStrategy(2);
		BufferStrategy bs = getBufferStrategy();
		Graphics g = bs.getDrawGraphics();
		gc = getGraphicsConfiguration();
		// Init all ressources
		Art.init(gc, sound);
		settings = new Settings();
		settings.loadSettings(this);
		menu = new Menu(this);

		int lastTick = -1;
		int renderedFrames = 0;
		int fps = 0;

		// Current time
		double time = System.nanoTime() / SECOND_TO_NANO;
		double now = time;
		double averagePassedTime = 0;

		// Start listening to events
		addKeyListener(this);
		addFocusListener(this);

		// Indicates if the processor timing is correct
		boolean naiveTiming = true;

		toTitle();

		// Game Loop
		while (running) {
			double lastTime = time;
			time = System.nanoTime() / SECOND_TO_NANO;
			double passedTime = time - lastTime;

			// Time running backwards, this sometimes happens on dual core amds
			if (passedTime < 0) {
				naiveTiming = false;
			}
			averagePassedTime = averagePassedTime * NINETY_PERCENT + passedTime
					* TEN_PERCENT;
			if (naiveTiming) {
				now = time;
			} else {
				// Use the average time if calculating current time isn't safe
				now += averagePassedTime;
			}

			int tick = (int) (now * Commons.TICKS_PER_SECOND);
			// Starting condition in the first run
			if (lastTick == -1) {
				lastTick = tick;
			}
			// Triggered everytime the tick goes one up
			while (lastTick < tick) {

				tick();
				lastTick++;

				// Triggered everytime the tick reaches the ticks per second
				if (lastTick % Commons.TICKS_PER_SECOND == 0) {
					// Calculate current fps
					fps = renderedFrames;
					renderedFrames = 0;
				}
			}

			sound.clientTick();

			float alpha = (float) (now * Commons.TICKS_PER_SECOND - tick);

			// First color all white
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, Commons.WIDTH, Commons.HEIGHT);

			render(g, alpha);

			// Display current fps
			g.setColor(Color.BLACK);
			drawString(g, "FPS: " + fps, FPS_XY, FPS_XY, FONT_WHITE_ROW);
			drawString(g, "FPS: " + fps, FPS_XY - 1, FPS_XY - 1, FONT_BLACK_ROW);

			bs.show();

			renderedFrames++;

			// Game loop delay
			try {
				Thread.sleep(GAME_LOOP_DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		sound.stopMusic();
	}

	/**
	 * Starts the Game Thread.
	 */
	public final void start() {
		if (!running) {
			running = true;
			new Thread(this, "Game Thread").start();
		}
	}

	/**
	 * Creates a Level.
	 */
	public final void startLevel() {
		scene = new LevelScene(this, gc, physic);
		scene.setSound(sound);
		physic.setScene(scene);
		scene.init();
	}

	/**
	 * Stops the Game Thread.
	 */
	public final void stop() {
		sound.stopMusic();
		running = false;
	}

	/**
	 * Logic of the component, will trigger {@link Scene}s tick.
	 */
	public final void tick() {
		if (Scene.getKeys()[Commons.KEY_MENU] && !paused && !inMenu && canMenu) {
			canMenu = false;
			openMenu();
		}
		if (!Scene.getKeys()[Commons.KEY_MENU] && !paused && !inMenu
				&& !canMenu) {
			canMenu = true;
		}

		if (!paused) {
			scene.tick();
		}

		if (menu.isVisible()) {
			menu.tick();
		}
	}

	/**
	 * Creates the Titlescreen.
	 */
	public final void toTitle() {
		scene = new TitleScene(this);
		scene.setSound(sound);
		physic.setScene(scene);
		scene.init();
	}

	@Override
	public void update(final Graphics g) {
	}

	/**
	 * Key Controller.
	 * 
	 * @param keyCode
	 *            Code of the toggled key
	 * @param isPressed
	 *            True for pressed, false for released
	 */
	private void toggleKey(final int keyCode, final boolean isPressed) {
		if (keyCode == KeyEvent.VK_A) {
			scene.toggleKey(Commons.KEY_LEFT, isPressed);
		}
		if (keyCode == KeyEvent.VK_D) {
			scene.toggleKey(Commons.KEY_RIGHT, isPressed);
		}
		if (keyCode == KeyEvent.VK_S) {
			scene.toggleKey(Commons.KEY_DOWN, isPressed);
		}
		if (keyCode == KeyEvent.VK_W) {
			scene.toggleKey(Commons.KEY_UP, isPressed);
		}
		if (keyCode == KeyEvent.VK_SPACE) {
			scene.toggleKey(Commons.KEY_JUMP, isPressed);
		}
		if (keyCode == KeyEvent.VK_J) {
			scene.toggleKey(Commons.KEY_ATACK1, isPressed);
		}
		if (keyCode == KeyEvent.VK_K) {
			scene.toggleKey(Commons.KEY_ATACK2, isPressed);
		}
		if (keyCode == KeyEvent.VK_L) {
			scene.toggleKey(Commons.KEY_ATACK3, isPressed);
		}
		if (keyCode == KeyEvent.VK_E) {
			scene.toggleKey(Commons.KEY_EVOLVE, isPressed);
		}
		if (keyCode == KeyEvent.VK_ESCAPE) {
			scene.toggleKey(Commons.KEY_MENU, isPressed);
		}
		if (keyCode == KeyEvent.VK_ENTER) {
			scene.toggleKey(Commons.KEY_ENTER, isPressed);
		}
	}
}
