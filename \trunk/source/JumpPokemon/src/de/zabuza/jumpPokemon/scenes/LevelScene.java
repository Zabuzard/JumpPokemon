package de.zabuza.jumpPokemon.scenes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;

import de.zabuza.jumpPokemon.Art;
import de.zabuza.jumpPokemon.Camera;
import de.zabuza.jumpPokemon.Commons;
import de.zabuza.jumpPokemon.JumpPkmnComponent;
import de.zabuza.jumpPokemon.level.Level;
import de.zabuza.jumpPokemon.level.LevelRenderer;
import de.zabuza.jumpPokemon.sprites.Player;
import de.zabuza.jumpPokemon.sprites.Sprite;
import de.zabuza.jumpPokemon.sprites.Testball;
import de.zabuza.physicEngine.PhysicEngine;

/**
 * LevelScene of the game.
 * 
 * @author Zabuza
 * 
 */
public class LevelScene extends Scene {

	/**
	 * Height of the ground.
	 */
	public static final int GROUND_HEIGHT = 5;
	/**
	 * Width of the striped ground scala.
	 */
	private static final int GROUND_STRIPES_WIDTH = 100;
	/**
	 * Offset from level end and player.
	 */
	private static final int LEVEL_END_OFFSET = 50;
	/**
	 * X-coord of players starting position.
	 */
	private static final int PLAYER_START_X = 30;
	/**
	 * Y-coord of players starting position.
	 */
	private static final int PLAYER_START_Y = 100;
	/**
	 * X-coord of balls starting position.
	 */
	private static final int TESTBALL_START_X = 700;
	/**
	 * Y-coord of balls starting position.
	 */
	private static final int TESTBALL_START_Y = 450;

	/**
	 * Component which created the Scene.
	 */
	@SuppressWarnings("unused")
	private JumpPkmnComponent component;
	/**
	 * Player sprite of the LevelScene.
	 */
	private Player player;
	/**
	 * Testball sprite of the LevelScene.
	 */
	private Testball testball;
	/**
	 * Level of the LevelScene which contains the data.
	 */
	private Level level;
	/**
	 * Camera of the LevelScene which follows the player.
	 */
	private Camera cam;

	/**
	 * PhysicEngine which will affect all sprites in the LevelScene.
	 */
	private PhysicEngine physic;
	/**
	 * LevelRenderer which renders the level.
	 */
	private LevelRenderer layer;
	/**
	 * Current used Configuration.
	 */
	private GraphicsConfiguration gc;

	/**
	 * Creates a new LevelScene.
	 * 
	 * @param thatComponent
	 *            Component which contains the {@link Scene}s
	 * @param thatGc
	 *            Current used configuration
	 * @param thatPhysic
	 *            PhysicEngine for the LevelScene
	 */
	public LevelScene(final JumpPkmnComponent thatComponent,
			final GraphicsConfiguration thatGc, final PhysicEngine thatPhysic) {
		this.component = thatComponent;
		this.gc = thatGc;
		this.physic = thatPhysic;
	}

	/**
	 * Gets the current used PhysicEngine.
	 * 
	 * @return Current used PhysicEngine
	 */
	public final PhysicEngine getPhysicEngine() {
		return physic;
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
		getSound().startMusic(Art.songs[1]);

		level = Art.level1;

		layer = new LevelRenderer(level, gc, Commons.WIDTH, Commons.HEIGHT);
		
		if(Commons.DEBUGGING) {
			layer.setRenderBehaviors(true);
		} else {
			layer.setRenderBehaviors(false);
		}

		testball = new Testball(this, TESTBALL_START_X, TESTBALL_START_Y);
		player = new Player(this, PLAYER_START_X, PLAYER_START_Y);

		addSprite(testball);
		addSprite(player);

		cam = new Camera(level, player, Commons.WIDTH, Commons.HEIGHT);
	}

	@Override
	public final void render(final Graphics g, final float alpha) {

		layer.setCam(cam);
		layer.render(g, alpha);

		// Draw a black Line on the ground
		g.setColor(Color.BLACK);
		g.fillRect(0, Commons.HEIGHT - GROUND_HEIGHT, cam.getWidth(),
				GROUND_HEIGHT);

		g.translate(-cam.getX(), -cam.getY());

		// Draw gray stripes as scala on the black ground
		g.setColor(Color.GRAY);
		for (int x = 0; x < (level.getWidth() * Commons.TILE_SIZE); x += (GROUND_STRIPES_WIDTH * 2)) {
			g.fillRect(x, Commons.HEIGHT - GROUND_HEIGHT, GROUND_STRIPES_WIDTH,
					GROUND_HEIGHT);
		}

		// Draw sprites
		for (Sprite sprite : getSprites()) {
			sprite.render(g, alpha);
		}

		g.translate(cam.getX(), cam.getY());

		/*
		// TODO Checkstyle debugging area
		// Display debug information
		// Debug player
		component.drawString(g, "XA:" + Math.round(player.getXA()), 75, 5, 0);
		component.drawString(g, "YA: " + Math.round(player.getYA()), 120, 5, 0);
		component.drawString(g, "X: " + Math.round(player.getX()), 180, 5, 0);
		component.drawString(g, "Y: " + Math.round(player.getY()), 250, 5, 0);
		component.drawString(g, "CanAtack: " + player.canAtack(), 4, 17, 0);
		component.drawString(g, "AtackingLevel: " + player.getCurAtack(), 140,
				17, 0);
		component.drawString(g, "CanEvolve: " + player.canEvolve(), 4, 29, 0);
		component.drawString(g, "Punchlist: " + player.getPunchlist().size(),
				4, 39, 0);
		component.drawString(g, "Firebeamlist: "
				+ player.getFirebeamlist().size(), 4, 49, 0);
		g.setColor(Color.GREEN);
		g.fillOval((int) (player.getX() - cam.getX()), Commons.HEIGHT - 7, 7, 7);
		g.fillOval((int) (player.getX() + player.getWidth() - cam.getX()),
							Commons.HEIGHT - 7, 7, 7);
		g.drawLine((int)player.getX() - cam.getX(), 0, (int)player.getX()
					- cam.getX(), Commons.HEIGHT);
		g.drawLine((int)(player.getX() + player.getAnim().getWidth()
					- cam.getX()), 0, (int)(player.getX()
					+ player.getAnim().getWidth() - cam.getX()),
					Commons.HEIGHT);
		g.setColor(Color.RED);
		g.fillOval((int) (player.getAnimX() - cam.getX()), Commons.HEIGHT - 7,
					7, 7);
		g.fillOval((int) (player.getAnimX() + player.getWidth() - cam.getX()),
							Commons.HEIGHT - 7, 7, 7);
		g.drawLine((int) (player.getAnimX() - cam.getX()), 0,
					(int) (player.getAnimX() - cam.getX()), Commons.HEIGHT);
		g.drawLine((int) (player.getAnimX() + player.getAnim().getWidth()
					- cam.getX()), 0, (int) (player.getAnimX()
					+ player.getAnim().getWidth() - cam.getX()),
					Commons.HEIGHT);

		// Debug testball
		component.drawString(g, "X: " + Math.round(testball.getX()), 700, 5, 0);
		component.drawString(g, "Y: " + Math.round(testball.getY()), 770, 5, 0);
		component.drawString(g, "XA: " + Math.round(testball.getXA()), 840, 5,
				0);
		component.drawString(g, "YA: " + Math.round(testball.getYA()), 910, 5,
				0);
		component
				.drawString(g, "onGround:" + testball.isOnGround(), 700, 35, 0);
		component
				.drawString(g, "Standing:" + testball.isStanding(), 850, 35, 0);
		component.drawString(g, "Jumping:" + testball.isJumping(), 700, 55, 0);
		component.drawString(g, "JumpTicks:" + testball.getJumps(), 850, 55, 0);
		*/
	}

	@Override
	public final void tick() {
		// Trigger tick of all sprites
		for (Sprite sprite : getSprites()) {
			sprite.tick();
			checkBlocks(sprite);
			checkLevelBounds(sprite);
		}
		cam.follow();
	}

	/**
	 * Checks if a sprite is blocked by a tile.
	 * If true it resets its coords to its old coords.
	 * @param sprite Sprite to check
	 */
	private void checkBlocks(Sprite sprite) {
		boolean blocking;
		int thatX;
		int thatY;
		
		//Falls Sprite nach rechts oder
		//garnicht beschleunigt wird, nehme rechtes Ende,
		//Ansonsten nehme linkes Ende.
		if(sprite.getXA() >= 0) {
			thatX = (int) ((sprite.getX() + sprite.getWidth()) / Commons.TILE_SIZE);
		} else {
			thatX = (int) (sprite.getX() / Commons.TILE_SIZE);
		}
		//Falls Sprite nach oben beschleunigt wird,
		//nehme oberes Ende, ansonsten unteres Ende.
		if(sprite.getYA() > 0) {
			thatY = (int) ((Commons.HEIGHT - sprite.getY() - sprite.getHeight())
					/ Commons.TILE_SIZE);
		} else {
			thatY = (int) ((Commons.HEIGHT - sprite.getY()) / Commons.TILE_SIZE);
		}
		
		blocking = level.isBlocking(thatX, thatY, sprite.getXA(), sprite.getYA());
		
		//XXX DEBUGGING
		blocking = false;
		
		if(blocking) {
			//TODO Ab hier muss Sprite übernehmen, nur es weiss was zu tun bei einem block.
			//Vllt. methode blocked(indicator) zweiteres gibt Richtung an, top,bottom,left,right
			//Oder man nutzt gleich das gesamte Polygon und überprüft für die gesamte Weite alle Blöcke
			//Es muss das Polygon OUTLINE überprüft werden, alle Punkte auf diesem Pfad müssen auf
			//Block getestet werden. Jedoch nur jeder "neue" Block muss getestet werden, da mehrere Punkte
			//in einem Block liegen. Mit dem Sprite Mittelpunkt kann dann die Location des Blocks bestimmt
			//werden (z.b. top). Es muss solange geprüft werden bis ein Block=true erscheint oder alle
			//Punkte durch sind (Fall für Rekursion?)
			sprite.setX(sprite.getXOld());
			sprite.setY(sprite.getYOld());
			sprite.setYA(0);
			sprite.setOnGround(true);
			sprite.setJumping(false);
		}
	}

	/**
	 * Checks the bounds of the level and prevents falling out of level by
	 * sprites.
	 * 
	 * @param sprite
	 *            Sprite to check coordinates
	 */
	private void checkLevelBounds(final Sprite sprite) {
		// Check y-coord, if it is below 5 reset and indicate that
		// sprite is onGround and not jumping
		if (sprite.getY() < GROUND_HEIGHT) {
			sprite.setY(GROUND_HEIGHT);
			sprite.setYA(0);
			sprite.setOnGround(true);
			sprite.setJumping(false);
		}

		// Check x-bounds
		// TODO Rechte Grenze muss Körperlänge des Pokes,
		// nicht Sheetlänge oder was fixes dazu nehmen
		// TODO Need right AND left Koord vom Körper (grüne Punkte)
		if (sprite.getX() > (level.getWidth() * Commons.TILE_SIZE) - LEVEL_END_OFFSET) {
			sprite.setX((level.getWidth() * Commons.TILE_SIZE) - LEVEL_END_OFFSET);
		}
		if (sprite.getX() < 0) {
			sprite.setX(0);
		}
	}
}