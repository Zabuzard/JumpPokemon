package de.zabuza.jumpPokemon.scenes;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import de.zabuza.jumpPokemon.Commons;
import de.zabuza.jumpPokemon.sprites.Sprite;
import de.zabuza.soundEngine.SoundEngine;
import de.zabuza.soundEngine.SoundListener;

/**
 * Abstract class for Scenes.
 * 
 * @author Zabuza
 * 
 */
public abstract class Scene implements SoundListener {

	/**
	 * Size of the key container.
	 */
	private static final int KEY_CONTAINER_SIZE = 11;

	/**
	 * Key container, true for pressed, false for released.
	 */
	private static boolean[] keys = new boolean[KEY_CONTAINER_SIZE];

	/**
	 * Gets the key container of the scene, true for pressed, false for
	 * released.
	 * 
	 * @return Key container of the scene
	 */
	public static boolean[] getKeys() {
		return keys;
	}

	/**
	 * Current used SoundEngine of the scene.
	 */
	private SoundEngine sound;

	/**
	 * Sprites container for the scene.
	 */
	private List<Sprite> sprites = new ArrayList<Sprite>();

	/**
	 * Adds a sprite to the scene.
	 * 
	 * @param sprite
	 *            Sprite to add
	 * @return True if sprite was added to the scene
	 */
	public final boolean addSprite(final Sprite sprite) {
		return sprites.add(sprite);
	}

	/**
	 * Gets the SoundEngine of the scene.
	 * 
	 * @return SoundEngine from the scene
	 */
	public final SoundEngine getSound() {
		return sound;
	}

	/**
	 * Inits the scene.
	 */
	public abstract void init();

	/**
	 * Removes a sprite from the scene.
	 * 
	 * @param sprite
	 *            Sprite to remove
	 * @return True if sprite was removed from the scene
	 */
	public final boolean removeSprite(final Sprite sprite) {
		return sprites.remove(sprite);
	}

	/**
	 * Renders the scene on a {@link Graphics} object.
	 * 
	 * @param g
	 *            Graphics object to draw on
	 * @param alpha
	 *            1 if all ticks for the second are done, 0 if none is done, 0.5
	 *            if half etc.
	 */
	public abstract void render(Graphics g, float alpha);

	/**
	 * Sets the SoundEngine for the scene. Adds the scene to the listener list
	 * of the SoundEngine
	 * 
	 * @param thatSound
	 *            SoundEngine to set
	 */
	public final void setSound(final SoundEngine thatSound) {
		thatSound.setListener(this);
		this.sound = thatSound;
	}

	/**
	 * Triggered every Tick, logic of scene.
	 */
	public abstract void tick();

	/**
	 * Toogles a key and saves it in the keys[] container.
	 * 
	 * @param key
	 *            Key index specified by {@link Commons} which is toogled
	 * @param isPressed
	 *            True for pressed, false for released
	 */
	public final void toggleKey(final int key, final boolean isPressed) {
		keys[key] = isPressed;
	}

	/**
	 * Gets the sprite container of the scene.
	 * 
	 * @return Sprite container from the scene.
	 */
	protected final List<Sprite> getSprites() {
		return sprites;
	}
}