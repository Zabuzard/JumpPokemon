package de.zabuza.jumpPokemon;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.midi.Sequence;

import de.zabuza.jumpPokemon.level.Level;
import de.zabuza.soundEngine.SoundEngine;
import de.zabuza.soundEngine.sample.SoundSample;

//TODO Ressourcen gehören zum jeweiligen Level, nicht als globale Utility class.
//TODO Checkstyle vorerst übersprungen da sowieso nur Provisorium
//und nicht Endlösung.
/**
 * Contains all resources as sounds, music and images. Can cut Sprites into
 * Imagearrays.
 * 
 * @author Zabuza
 * 
 */
public final class Art {

	/**
	 * Sample for getting a coin.
	 */
	public static final int SAMPLE_GET_COIN = 0;
	/**
	 * Sample for jumping.
	 */
	public static final int SAMPLE_PLAYER_JUMP = 1;
	/**
	 * Sample for punching.
	 */
	public static final int SAMPLE_PLAYER_PUNCH = 2;
	/**
	 * Sample for a firebeam.
	 */
	public static final int SAMPLE_PLAYER_FIREBEAM = 3;
	/**
	 * Sample for bumping.
	 */
	public static final int SAMPLE_SHELL_BUMP = 4;
	/**
	 * Sample for evolving.
	 */
	public static final int SAMPLE_EVOLVE = 5;

	/**
	 * Sheets for Feurigel.
	 */
	public static Sheet feurigel_idle, feurigel_walking, feurigel_atack1,
			feurigel_atack2;
	/**
	 * Sheets for Igelavar.
	 */
	public static Sheet igelavar_idle, igelavar_walking, igelavar_atack1,
			igelavar_atack2;
	/**
	 * Sheet for Tornupto.
	 */
	public static Sheet tornupto_idle, tornupto_walking, tornupto_atack1,
			tornupto_atack2;
	/**
	 * Sheet for the Testball.
	 */
	public static Sheet testball;
	/**
	 * Sheets for Punchs.
	 */
	public static Sheet punch1, punch2, punch3;
	/**
	 * Sheets for Firebeams.
	 */
	public static Sheet firebeam1, firebeam2, firebeam3;

	/**
	 * Contains many tokens as images.
	 */
	public static Image[][] font;

	public static Image[][] level;
	/**
	 * The title screen image.
	 */
	public static Image titleScreen;
	/**
	 * The game logo for the title screen.
	 */
	public static Image titleFont;

	public static Level level1;

	/**
	 * Contains all {@link SoundSample}s.
	 */
	public static SoundSample[] samples = new SoundSample[100];

	/**
	 * Contains all {@link Sequence}s.
	 */
	public static final Sequence[] songs = new Sequence[2];

	/**
	 * Loads all ressources.
	 * 
	 * @param gc
	 *            Components {@link GraphicsConfiguration}
	 * @param sound
	 *            Components {@link SoundEngine}
	 */
	public static void init(final GraphicsConfiguration gc,
			final SoundEngine sound) {
		try {
			// XXX Debug Markierung
			long debugTime = System.currentTimeMillis();
			long wholeTime = System.currentTimeMillis();
			System.out.println("Starte Laden aller Ressourcen...");

			feurigel_idle = new Sheet(cutImage(gc,
					"/res/img/feurigel_idle.png", 60, 72), "feurigel_idle");
			feurigel_walking = new Sheet(cutImage(gc,
					"/res/img/feurigel_walking.png", 80, 52),
					"feurigel_walking");
			feurigel_atack1 = new Sheet(cutImage(gc,
					"/res/img/feurigel_atack1.png", 60, 76), "feurigel_atack1");
			feurigel_atack2 = new Sheet(cutImage(gc,
					"/res/img/feurigel_atack2.png", 128, 108),
					"feurigel_atack2");
			igelavar_idle = new Sheet(cutImage(gc,
					"/res/img/igelavar_idle.png", 100, 72), "igelavar_idle");
			igelavar_walking = new Sheet(cutImage(gc,
					"/res/img/igelavar_walking.png", 104, 72),
					"igelavar_walking");
			igelavar_atack1 = new Sheet(cutImage(gc,
					"/res/img/igelavar_atack1.png", 96, 80), "igelavar_atack1");
			igelavar_atack2 = new Sheet(cutImage(gc,
					"/res/img/igelavar_atack2.png", 144, 120),
					"igelavar_atack2");
			tornupto_idle = new Sheet(cutImage(gc,
					"/res/img/tornupto_idle.png", 140, 180), "tornupto_idle");
			tornupto_walking = new Sheet(cutImage(gc,
					"/res/img/tornupto_walking.png", 188, 148),
					"tornupto_walking");
			tornupto_atack1 = new Sheet(cutImage(gc,
					"/res/img/tornupto_atack1.png", 136, 168),
					"tornupto_atack1");
			tornupto_atack2 = new Sheet(cutImage(gc,
					"/res/img/tornupto_atack2.png", 188, 220),
					"tornupto_atack2");
			punch1 = new Sheet(cutImage(gc, "/res/img/punch1.png", 130, 40),
					"punch1");
			punch2 = new Sheet(cutImage(gc, "/res/img/punch2.png", 150, 48),
					"punch2");
			punch3 = new Sheet(cutImage(gc, "/res/img/punch3.png", 190, 62),
					"pucnh3");
			firebeam1 = new Sheet(cutImage(gc, "/res/img/firebeam1.png", 256,
					16), "firebeam1");
			firebeam2 = new Sheet(cutImage(gc, "/res/img/firebeam2.png", 256,
					64), "firebeam2");
			firebeam3 = new Sheet(cutImage(gc, "/res/img/firebeam3.png", 256,
					64), "firebeam3");
			font = cutImage(gc, "/res/img/font.gif", 8, 8);
			level = cutImage(gc, "/res/img/mapsheet.png", Commons.TILE_SIZE,
					Commons.TILE_SIZE);
			testball = new Sheet(
					cutImage(gc, "/res/img/testball.png", 101, 101), "testball");
			titleFont = getImage(gc, "/res/img/jumpPokemon_heading.png");
			titleScreen = getImage(gc,
					"/res/img/jumpPokemon_titlebackground.jpg");

			// XXX Debug Markierung
			System.out.println(System.currentTimeMillis() - debugTime
					+ " Alle Bilder geladen.");
			System.out.println("Lade Sounds");
			debugTime = System.currentTimeMillis();

			if (sound != null) {
				samples[SAMPLE_GET_COIN] = sound
						.loadSample("/res/snd/coin.wav");
				samples[SAMPLE_PLAYER_JUMP] = sound
						.loadSample("/res/snd/jump.wav");
				samples[SAMPLE_PLAYER_PUNCH] = sound
						.loadSample("/res/snd/firepunch.wav");
				samples[SAMPLE_PLAYER_FIREBEAM] = sound
						.loadSample("/res/snd/firebeam.wav");
				samples[SAMPLE_SHELL_BUMP] = sound
						.loadSample("/res/snd/bump.wav");
				samples[SAMPLE_EVOLVE] = sound
						.loadSample("/res/snd/levelup.wav");

				// XXX Debug Markierung
				System.out.println(System.currentTimeMillis() - debugTime
						+ " Alle Sounds geladen.");
				debugTime = System.currentTimeMillis();
				System.out.println("Lade Musik...");

				songs[0] = sound.loadSequence(Art.class
						.getResourceAsStream("/res/mus/title.mid"));
				songs[1] = sound.loadSequence(Art.class
						.getResourceAsStream("/res/mus/sandbox.mid"));

				// XXX Debug Markierung
				System.out.println(System.currentTimeMillis() - debugTime
						+ " Musik geladen.");
			}

			// XXX Debug Markierung
			debugTime = System.currentTimeMillis();
			System.out.println("Lade Level...");

			Level.loadBehaviors(new DataInputStream(Art.class
					.getResourceAsStream("/res/tiles.dat")));
			level1 = Level.load(new DataInputStream(Art.class
					.getResourceAsStream("/res/level1.lvl")));

			// XXX Debug Markierung
			System.out.println(System.currentTimeMillis() - debugTime
					+ " Level geladen.");
			System.out.println("Ressourcen alle geladen. Dauer: "
					+ (System.currentTimeMillis() - wholeTime));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cuts a given sprite into an Image Array.
	 * 
	 * @param gc
	 *            Components {@Link GraphicsConfiguration}
	 * @param imageName
	 *            Location and name of the image as ressource
	 * @param xSize
	 *            Width of a single image in the sprite
	 * @param ySize
	 *            Height of a single image in the sprite
	 * @return Image Array which contains the cutted Sprite
	 */
	private static Image[][] cutImage(final GraphicsConfiguration gc,
			final String imageName, final int xSize, final int ySize)
			throws IOException {
		Image source = getImage(gc, imageName);
		Image[][] images = new Image[source.getHeight(null) / ySize][source
				.getWidth(null) / xSize];
		for (int y = 0; y < source.getHeight(null) / ySize; y++) {
			for (int x = 0; x < source.getWidth(null) / xSize; x++) {
				Image image = gc.createCompatibleImage(xSize, ySize,
						Transparency.BITMASK);
				Graphics2D g = (Graphics2D) image.getGraphics();
				g.setComposite(AlphaComposite.Src);
				g.drawImage(source, -x * xSize, -y * ySize, null);
				g.dispose();
				images[y][x] = image;
			}
		}
		return images;
	}

	/**
	 * Loads a given ressource in an {@link Image} object.
	 * 
	 * @param gc
	 *            Components {@link GraphicsConfiguration}
	 * @param imageName
	 *            Location and name of the image as ressource
	 * @return Image from loaded ressource
	 */
	private static Image getImage(final GraphicsConfiguration gc,
			final String imageName) throws IOException {

		// XXX Debug Markierung
		long debugTime = System.currentTimeMillis();

		BufferedImage source = ImageIO.read(Art.class
				.getResourceAsStream(imageName));
		Image image = gc.createCompatibleImage(source.getWidth(),
				source.getHeight(), Transparency.BITMASK);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setComposite(AlphaComposite.Src);
		g.drawImage(source, 0, 0, null);
		g.dispose();

		// XXX Debug Markierung
		System.out.println(System.currentTimeMillis() - debugTime
				+ " Geladen Bild :" + imageName);

		return image;
	}

	/**
	 * Utility class, has no effect.
	 */
	private Art() {

	}
}