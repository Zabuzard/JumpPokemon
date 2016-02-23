package de.zabuza.jumpPokemon.menu;

import java.awt.Color;
import java.awt.Graphics;

import de.zabuza.jumpPokemon.Commons;
import de.zabuza.jumpPokemon.JumpPkmnComponent;
import de.zabuza.jumpPokemon.scenes.Scene;

//TODO Checkstyle vorerst übersprungen da sowieso nur Provisorium und nicht Endlösung.
/**
 * Menu class. Contains the game properties.
 * 
 * @author Zabuza
 * 
 */
public class Menu {
	// TODO Menu Steuerung ist erstmal nur provisorisch.
	// Muss komplett überarbeitet werden.
	// TODO Evtl. Menu class für jedes Menu mit Entry classes welche die
	// Klick-OPs und die eigenen Menus halten.
	// TODO Menu Sichtbarkeit und Öffnung durch JumpPkmnComponent müssen
	// auch noch überarbeitet werden.
	public static final int OPTION_BACK = 0;
	public static final int OPTION_VOLUME = 1;

	public static final int LAYER_MENU = 0;
	public static final int LAYER_VOLUME = 1;

	private JumpPkmnComponent comp;
	private int tick;
	private int x, y;
	private int width = 350;
	private int height = 250;
	private int curOption = OPTION_VOLUME;
	private int curLayer = LAYER_MENU;

	private boolean canDown = true;
	private boolean canUp = true;
	private boolean canEnter = true;
	private boolean canMenu = true;

	public boolean visible;

	/**
	 * Creates a new Menu. Used for changing game settings.
	 * 
	 * @param comp
	 *            Component which uses the menu
	 */
	public Menu(JumpPkmnComponent comp) {
		this.comp = comp;
		x = (Commons.WIDTH / 2) - (width / 2);
		y = (Commons.HEIGHT / 2) - (height / 2);
	}

	/**
	 * Gets the visibility of the menu
	 * 
	 * @return True if menu is visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Renders the menu on a {@link Graphics} object
	 * 
	 * @param g
	 *            Graphics object to draw on
	 * @param alpha
	 *            1 if all ticks for second are done, 0 if none is done, 0.5 if
	 *            half etc.
	 */
	public void render(Graphics g, float alpha) {
		g.setColor(Color.BLACK);
		g.fillRect(x, y, width, height);
		comp.drawString(g, "CurOption : " + curOption, x + 5, y + 5, 7);
		comp.drawString(g, "CurLayer : " + curLayer, x + 5, y + 30, 7);

		// Draw sound and music volume
		g.setColor(Color.WHITE);
		g.drawRect(x + 19, y + 99, 202, 42);
		g.drawRect(x + 19, y + 179, 202, 42);
		g.setColor(Color.GREEN.darker().darker());
		g.fillRect(x + 20, y + 100, (int) (comp.getSoundEngine()
				.getMusicVolume() * 200), 40);
		g.fillRect(x + 20, y + 180, (int) (comp.getSoundEngine()
				.getSoundVolume() * 200), 40);
		comp.drawString(g, "MusicVolume", x + 80, y + 115, 7);
		comp.drawString(g, "SoundVolume", x + 80, y + 195, 7);
	}

	/**
	 * Sets the visibility of the menu
	 * 
	 * @param visible
	 *            True if menu should be visible
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Logic of the menu
	 */
	public void tick() {
		tick++;
		// Start key-listening after 12 ticks (prevents instant close etc.)
		if (tick > 12) {
			// Not really visible provisoric menu control
			if (Scene.getKeys()[Commons.KEY_DOWN] && canDown) {
				canDown = false;
				curOption--;
				if (curOption < 0) {
					curOption = OPTION_VOLUME;
				}
			} else if (Scene.getKeys()[Commons.KEY_UP] && canUp) {
				canUp = false;
				curOption++;
				if (curOption > OPTION_VOLUME) {
					curOption = OPTION_BACK;
				}

			} else if (Scene.getKeys()[Commons.KEY_ENTER] && canEnter) {
				canEnter = false;
				if (curLayer == LAYER_MENU && curOption == OPTION_BACK) {
					tick = 0;
					comp.closeMenu();
				} else {
					curLayer = curOption;
				}
			} else if (Scene.getKeys()[Commons.KEY_MENU] && canMenu) {
				canMenu = false;
				if (curLayer == LAYER_MENU) {
					tick = 0;
					comp.closeMenu();
				} else {
					curLayer--;
				}
			}

			if (!Scene.getKeys()[Commons.KEY_DOWN] && !canDown) {
				canDown = true;
			}
			if (!Scene.getKeys()[Commons.KEY_UP] && !canUp) {
				canUp = true;
			}
			if (!Scene.getKeys()[Commons.KEY_ENTER] && !canEnter) {
				canEnter = true;
			}
			if (!Scene.getKeys()[Commons.KEY_MENU] && !canMenu) {
				canMenu = true;
			}

			// If right or left, change music and sound volume
			if (Scene.getKeys()[Commons.KEY_RIGHT]) {
				comp.getSoundEngine().setMusicVolume(
						comp.getSoundEngine().getMusicVolume() + 0.01f);
				comp.getSoundEngine().setSoundVolume(
						comp.getSoundEngine().getSoundVolume() + 0.01f);
			} else if (Scene.getKeys()[Commons.KEY_LEFT]) {
				comp.getSoundEngine().setMusicVolume(
						comp.getSoundEngine().getMusicVolume() - 0.01f);
				comp.getSoundEngine().setSoundVolume(
						comp.getSoundEngine().getSoundVolume() - 0.01f);
			}
		}
	}

}
