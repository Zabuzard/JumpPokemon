package de.zabuza.jumpPokemon;

import java.awt.Image;

/**
 * Sheet class for animated sprites. Holds all images and their hitboxes
 * 
 * @author Zabuza
 * 
 */
public class Sheet {

	/**
	 * All images of the Sheet.
	 */
	private Image[][] images;
	/**
	 * Name of the Sheet.
	 */
	private String name;

	/**
	 * Create a sheet with images and a name, e.g. feurigel_idle. Will create a
	 * hitbox for each image
	 * 
	 * @param thatImages
	 *            Images for the sheet
	 * @param thatName
	 *            Name for the sheet, e.g. feurigel_idle
	 */
	public Sheet(final Image[][] thatImages, final String thatName) {
		this.images = thatImages;
		this.name = thatName;
	}

	/**
	 * Gets a row of the sheet as Image[].
	 * 
	 * @param row
	 *            Row of the sheet
	 * @return Row of the sheet as Image[]
	 */
	public final Image[] getImageRow(final int row) {
		if (row >= images.length) {
			return null;
		} else {
			return images[row];
		}
	}

	/**
	 * Gets the name of the Sheet.
	 * 
	 * @return Name of the Sheet
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Returnes the name of the Sheet.
	 * 
	 * @return Name of the Sheet.
	 */
	@Override
	public final String toString() {
		return getName();
	}
}
