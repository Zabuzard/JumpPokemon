package de.zabuza.jumpPokemon.level;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.zabuza.jumpPokemon.Art;

/**
 * Level class. Contains a level tile map and their data. Provides some utility
 * methods.
 * 
 * @author Zabuza
 * 
 */
public class Level {

	/**
	 * Value to shift a byte zero indexs.
	 */
	private static final int BYTE_ZERO = 0;
	/**
	 * Value to shift a byte one index.
	 */
	private static final int BYTE_ONE = 1;
	/**
	 * Value to shift a byte two indexs.
	 */
	private static final int BYTE_TWO = 2;
	/**
	 * Value to shift a byte three indexs.
	 */
	private static final int BYTE_THREE = 3;
	/**
	 * Value to shift a byte four indexs.
	 */
	private static final int BYTE_FOUR = 4;
	/**
	 * Value to shift a byte five indexs.
	 */
	private static final int BYTE_FIVE = 5;
	/**
	 * Value to shift a byte six indexs.
	 */
	private static final int BYTE_SIX = 6;
	/**
	 * Value to shift a byte seven indexs.
	 */
	private static final int BYTE_SEVEN = 7;

	/**
	 * Hexadezimal for FF.
	 */
	private static final int HEX_FF = 0xFF;
	/**
	 * Hexadezimal for FFFF.
	 */
	private static final int HEX_FFFF = 0xFFFF;

	/**
	 * Names of all tile behaviors.
	 */
	public static final String[] BIT_DESCRIPTIONS = { "BLOCK UPPER",
			"BLOCK ALL", "BLOCK LOWER", "SPECIAL", "BUMPABLE", "BREAKABLE",
			"PICKUPABLE", "ANIMATED", };

	/**
	 * Contains the tile behaviors for every tile of the ressource.
	 */
	private static byte[] tileBehaviors = new byte[Art.level.length
			* Art.level[0].length];

	/**
	 * Bit which indicates that a tile blocks falling trough it.
	 */
	public static final int BIT_BLOCK_UPPER = 1 << BYTE_ZERO;
	/**
	 * Bit which indicates that a tile blocks everything.
	 */
	public static final int BIT_BLOCK_ALL = 1 << BYTE_ONE;
	/**
	 * Bit which indicates that a tile blocks jumping trough it.
	 */
	public static final int BIT_BLOCK_LOWER = 1 << BYTE_TWO;
	/**
	 * Bit which indicates that a tile is special.
	 */
	public static final int BIT_SPECIAL = 1 << BYTE_THREE;
	/**
	 * Bit which indicates that a tile is bumpable.
	 */
	public static final int BIT_BUMPABLE = 1 << BYTE_FOUR;
	/**
	 * Bit which indicates that a tile is breakable.
	 */
	public static final int BIT_BREAKABLE = 1 << BYTE_FIVE;
	/**
	 * Bit which indicates that a tile is pickupable.
	 */
	public static final int BIT_PICKUPABLE = 1 << BYTE_SIX;
	/**
	 * Bit which indicates that a tile is animated.
	 */
	public static final int BIT_ANIMATED = 1 << BYTE_SEVEN;

	/**
	 * File header of levels stored in the filesystem as .lvl.
	 */
	private static final int FILE_HEADER = 0xba11ade9;

	/**
	 * Gets the behaviors of a tile at custom index from ressources image.
	 * 
	 * @param index
	 *            Index of the tile from ressources image
	 * @return Tiles behaviors as byte
	 */
	public static byte getTileBehaviors(final int index) {
		return tileBehaviors[index];
	}

	/**
	 * Loads and returns a new level from a stream, e.g. from the filesystem.
	 * 
	 * @param dis
	 *            DataInputStream from where the level should be loaded
	 * @return New level object with the streams data
	 * @throws IOException
	 *             If an IOException occured
	 */
	public static Level load(final DataInputStream dis) throws IOException {
		long header = dis.readLong();
		if (header != Level.FILE_HEADER) {
			throw new IOException("Bad level header");
		}
		// XXX Remove suppress unused, evtl. in it will be used in future
		// but now the alert icon annoys
		@SuppressWarnings("unused")
		int version = dis.read() & HEX_FF;

		int width = dis.readShort() & HEX_FFFF;
		int height = dis.readShort() & HEX_FFFF;
		Level level = new Level(width, height);
		level.map = new byte[width][height];
		level.data = new byte[width][height];
		for (int i = 0; i < width; i++) {
			dis.readFully(level.map[i]);
			dis.readFully(level.data[i]);
		}

		return level;
	}

	/**
	 * Loads the tile behaviors from a stream, e.g. from the filesystem.
	 * 
	 * @param dis
	 *            DataInputStream to where the tile behaviors are stored, e.g.
	 *            on the filesystem
	 * @throws IOException
	 *             If an IOException occured
	 */
	public static void loadBehaviors(final DataInputStream dis)
			throws IOException {
		dis.readFully(Level.tileBehaviors);
	}

	/**
	 * Saves the tile behaviors in a stream, e.g. to the filesystem.
	 * 
	 * @param dos
	 *            DataOutputStream to where the tile behaviors should be stored,
	 *            e.g. to the filesystem
	 * @throws IOException
	 *             If an IOException occured
	 */
	public static void saveBehaviors(final DataOutputStream dos)
			throws IOException {
		dos.write(Level.tileBehaviors);
	}

	/**
	 * Sets the behaviors of a tile at custom index from ressources image.
	 * 
	 * @param index
	 *            Index of the tile from ressources image
	 * @param data
	 *            Tiles behaviors as byte
	 */
	public static void setTileBehaviors(final int index, final byte data) {
		tileBehaviors[index] = data;
	}

	/**
	 * Width of the level.
	 */
	private int width;

	/**
	 * Height of the level.
	 */
	private int height;

	/**
	 * Array which contains all level tiles as byte[x][y].
	 */
	private byte[][] map;

	/**
	 * Array which contains the data of all level tiles as byte[x][y].
	 */
	private byte[][] data;

	/**
	 * Creates a new level with custom width and height.
	 * 
	 * @param thatWidth
	 *            Width of the new level
	 * @param thatHeight
	 *            Height of the new level
	 */
	public Level(final int thatWidth, final int thatHeight) {
		this.width = thatWidth;
		this.height = thatHeight;

		map = new byte[thatWidth][thatHeight];
		data = new byte[thatWidth][thatHeight];
	}

	/**
	 * Gets the tile at custom coordinates.
	 * 
	 * @param thatX
	 *            X-coord of the tile
	 * @param thatY
	 *            Y-coord of the tile
	 * @return Tile at custom coordinates
	 */
	public final byte getBlock(final int thatX, final int thatY) {
		int x = thatX;
		int y = thatY;

		if (thatX < 0) {
			x = 0;
		} else if (thatX >= width) {
			x = width - 1;
		}

		if (thatY < 0) {
			y = 0;
		} else if (thatY >= height) {
			y = height - 1;
		}
		
		return map[x][y];
	}

	/**
	 * Gets the height of the level.
	 * 
	 * @return Height of the level
	 */
	public final int getHeight() {
		return height;
	}

	/**
	 * Gets the width of the level.
	 * 
	 * @return Width of the level
	 */
	public final int getWidth() {
		return width;
	}

	/**
	 * Returns if a tile at custom coordinates blocks e.g. a sprite.
	 * 
	 * @param x
	 *            X-coord of the tile
	 * @param y
	 *            Y-coord of the tile
	 * @param xa
	 *            Acceleration in x-dir of e.g. a sprite
	 * @param ya
	 *            Acceleration in y-dir of e.g. a sprite
	 * @return True if the tile is blocking
	 */
	public final boolean isBlocking(final int x, final int y, final float xa,
			final float ya) {
		byte block = getBlock(x, y);
		boolean blocking = ((tileBehaviors[block & HEX_FF]) & BIT_BLOCK_ALL) > 0;
		blocking |= (ya > 0)
				&& ((tileBehaviors[block & HEX_FF]) & BIT_BLOCK_UPPER) > 0;
		blocking |= (ya < 0)
				&& ((tileBehaviors[block & HEX_FF]) & BIT_BLOCK_LOWER) > 0;

		return blocking;
	}

	/**
	 * Saves the level into a stream, e.g. to the filesystem.
	 * 
	 * @param dos
	 *            DataOutputStream to where the level should be saved, e.g. the
	 *            filesystem
	 * @throws IOException
	 *             If an IOException occured
	 */
	public final void save(final DataOutputStream dos) throws IOException {
		dos.writeLong(Level.FILE_HEADER);
		dos.write((byte) 0);

		dos.writeShort((short) width);
		dos.writeShort((short) height);

		for (int i = 0; i < width; i++) {
			dos.write(map[i]);
			dos.write(data[i]);
		}
	}

	/**
	 * Sets a tile at custom coordinates.
	 * 
	 * @param x
	 *            X-coord of tile to set
	 * @param y
	 *            Y-coord of tile to set
	 * @param b
	 *            Tile as byte to set
	 */
	public final void setBlock(final int x, final int y, final byte b) {
		if (x < 0) {
			return;
		}
		if (y < 0) {
			return;
		}
		if (x >= width) {
			return;
		}
		if (y >= height) {
			return;
		}
		map[x][y] = b;
	}

	/**
	 * Sets the data of a tile at custom coordinates.
	 * 
	 * @param x
	 *            X-coord of tile to set
	 * @param y
	 *            Y-coord of tile to set
	 * @param b
	 *            Tiles data as byte to set
	 */
	public final void setBlockData(final int x, final int y, final byte b) {
		if (x < 0) {
			return;
		}
		if (y < 0) {
			return;
		}
		if (x >= width) {
			return;
		}
		if (y >= height) {
			return;
		}
		data[x][y] = b;
	}
}