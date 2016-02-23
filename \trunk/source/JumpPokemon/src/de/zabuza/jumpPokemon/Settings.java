package de.zabuza.jumpPokemon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

/**
 * Class for the game settings. Filepath is "../settings.properties".
 * 
 * @author Zabuza
 * 
 */
public class Settings {

	// private String filepath = "/res/settings.properties";
	/**
	 * File path of the settings.
	 */
	private String filepath = "settings.properties";
	/**
	 * Properties object where the settings are saved in.
	 */
	private Properties properties;

	/**
	 * Create a new settings object.
	 */
	public Settings() {
		properties = new Properties();
	}

	/**
	 * Loads settings of the saved file and applies the properties to the game
	 * settings.
	 * 
	 * @param comp
	 *            Component which settings will be affected
	 */
	public final void loadSettings(final JumpPkmnComponent comp) {
		try {
			// properties.load(Settings.class.getResourceAsStream(filepath));
			try {
				properties.load(new FileInputStream(filepath));
			} catch (FileNotFoundException e) {
				saveSettings(comp);
				properties.load(new FileInputStream(filepath));
			}

			float musicVolume = Float.parseFloat((String) properties
					.get("musicVolume"));
			float soundVolume = Float.parseFloat((String) properties
					.get("soundVolume"));

			comp.getSoundEngine().setMusicVolume(musicVolume);
			comp.getSoundEngine().setSoundVolume(soundVolume);
		} catch (NullPointerException | IOException e) {
			System.out.println("Error while loading settings of : " + filepath);
			e.printStackTrace();
		}
	}

	/**
	 * Saves the current settings of the game in a file. Filepath is
	 * "../settings.properties".
	 * 
	 * @param comp
	 *            Component which settings will be affected
	 */
	public final void saveSettings(final JumpPkmnComponent comp) {
		PrintStream target = null;
		try {
			String musicVolume = "" + comp.getSoundEngine().getMusicVolume();
			String soundVolume = "" + comp.getSoundEngine().getSoundVolume();

			properties.put("musicVolume", musicVolume);
			properties.put("soundVolume", soundVolume);

			// URL url = Settings.class.getResource(filepath);
			// properties.store(new FileOutputStream(
			// new File(url.toURI())), null);
			properties.store(new FileOutputStream(new File(filepath)), null);

		} catch (IOException e) {
			System.out.println("Error while saving settings in : " + filepath);
			e.printStackTrace();
		} finally {
			if (target != null) {
				target.close();
			}
		}
	}
}
