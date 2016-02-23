package de.zabuza.jumpPokemon.sprites;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.zabuza.jumpPokemon.Animation;
import de.zabuza.jumpPokemon.Art;
import de.zabuza.jumpPokemon.Commons;
import de.zabuza.jumpPokemon.Sheet;
import de.zabuza.jumpPokemon.scenes.LevelScene;
import de.zabuza.jumpPokemon.scenes.Scene;
import de.zabuza.physicEngine.PhysicEngine;
import de.zabuza.physicEngine.forms.Normal;

/**
 * Player class for the game.
 * 
 * @author Zabuza
 * 
 */
public class Player extends Sprite {

	// Only for x-coord
	/**
	 * Sidewayspeed of player when on ground.
	 */
	private static final float GROUNDSIDEWAYSPEED = 6.0f;
	/**
	 * Sidewayspeed of player when in air.
	 */
	private static final float AIRSIDEWAYSPEED = 2.5f;

	// Only for y-coord
	/**
	 * Amount of ticks in which player is accelerated upwards when jumping.
	 */
	private static final int JUMPTICKS = 7;
	/**
	 * Upwayspeed of player.
	 */
	private static final float UPWAYSPEED = 6.0f;
	/**
	 * Watershed while jumping when player will then accelerate downwards.
	 */
	private static final float JUMPWATERSHED = 10.0f;

	/**
	 * Intervall for players animations in ticks.
	 */
	private static final int ANIM_INTERVALL = 4;
	/**
	 * Intervall for players atack animations in ticks.
	 */
	private static final int ANIM_ATACK_INTERVALL = 2;

	/**
	 * Representates players atack1.
	 */
	private static final int ATACK_1 = 1;
	/**
	 * Representates players atack2.
	 */
	private static final int ATACK_2 = 2;
	/**
	 * Representates players atack3.
	 */
	private static final int ATACK_3 = 3;
	/**
	 * Representates when player is not atacking.
	 */
	private static final int ATACK_NO = 0;

	/**
	 * Render correction when players dir is right, the used atack is atack2 and
	 * tier is 0.
	 */
	private static final float RENDER_RIGHT_ATACK2_0 = 0.5f;
	/**
	 * Render correction when players dir is right, the used atack is atack2 and
	 * tier is 1.
	 */
	private static final float RENDER_RIGHT_ATACK2_1 = 0.3f;
	/**
	 * Render correction when players dir is right, the used atack is atack2 and
	 * tier is 2.
	 */
	private static final float RENDER_RIGHT_ATACK2_2 = 0.27f;

	/**
	 * Amount of which the tierSpeedBonus is increased.
	 */
	private static final float TIER_SPEED_BONUS_AMOUNT = 0.2f;

	/**
	 * Limit of how much punchs can be cast at the same time when players tier
	 * is 0.
	 */
	private static final int PUNCHLIMIT_0 = 2;
	/**
	 * Limit of how much punchs can be cast at the same time when players tier
	 * is 1.
	 */
	private static final int PUNCHLIMIT_1 = 3;
	/**
	 * Limit of how much punchs can be cast at the same time when players tier
	 * is 2.
	 */
	private static final int PUNCHLIMIT_2 = 4;

	/**
	 * Limit of how much firebeams can be cast at the same time.
	 */
	private static final int FIREBEAMLIMIT = 1;

	/**
	 * Tick after which player can start to move.
	 */
	private static final int STARTING_TICK = 12;

	/**
	 * Starting index of the atack2 animation when players tier is 0.
	 */
	private static final int ANIM_ATACK2_START_0 = 6;
	/**
	 * Starting index of the atack2 animation when players tier is 1.
	 */
	private static final int ANIM_ATACK2_START_1 = 6;
	/**
	 * Starting index of the atack2 animation when players tier is 2.
	 */
	private static final int ANIM_ATACK2_START_2 = 7;

	/**
	 * Firebeams y-coord offset when players tier is 0.
	 */
	private static final float FIREBEAM_Y_OFFSET_0 = 0.05f;
	/**
	 * Firebeams y-coord offset when players tier is 1.
	 */
	private static final float FIREBEAM_Y_OFFSET_1 = -0.1f;
	/**
	 * Firebeams y-coord offset when players tier is 2.
	 */
	private static final float FIREBEAM_Y_OFFSET_2 = 0.17f;
	/**
	 * Punchs y-coord offset.
	 */
	private static final float PUNCH_Y_OFFSET = 0.4f;
	/**
	 * Punchs random factor which affects its y-coord.
	 */
	private static final float PUNCH_RANDOM_FACTOR = 0.2f;

	/**
	 * Key container of the scene where the player is located.
	 */
	private boolean[] keys;
	/**
	 * Current amount of ticks in which player will be accelerated upwards when
	 * jumping.
	 */
	private int jumpTime;
	/**
	 * Tier of Pkmn, 0 for first, 1 for second, 2 for third.
	 */
	private int tier;
	/**
	 * Sidewayspeedbonus for tier.
	 */
	private float tierSpeedBonus;
	/**
	 * AnimXCorrection for rendering.
	 */
	private float renderCorrection;
	/**
	 * Limit of how much punchs can be cast at the same time.
	 */
	private int punchlimit;
	/**
	 * Limit of how much firebeams can be cast at the same time.
	 */
	private int firebeamlimit;

	/**
	 * True if player can evolve.
	 */
	private boolean canEvolve;
	/**
	 * True if player can atack.
	 */
	private boolean canAtack;
	/**
	 * Current tick of the player.
	 */
	private int tick;
	/**
	 * Current used atack. 0 for no atack, 1 for atack1, 2 for atack2, 3 for
	 * atack3.
	 */
	private int curAtack;

	/**
	 * Container for all current displayed punchs.
	 */
	private List<Punch> punchlist = new ArrayList<Punch>();
	/**
	 * Container for all current displayed firebeams.
	 */
	private List<Firebeam> firebeamlist = new ArrayList<Firebeam>();

	/**
	 * Creates a new Player at a custom starting position.
	 * 
	 * @param level
	 *            LevelScene which contains the Player
	 * @param x
	 *            X-coord of players starting position
	 * @param y
	 *            Y-coord of players starting position
	 */
	public Player(final LevelScene level, final int x, final int y) {
		setLevelScene(level);
		setForm(new Normal());

		tierSpeedBonus = 1f;
		renderCorrection = 0f;
		punchlimit = PUNCHLIMIT_0;
		firebeamlimit = FIREBEAMLIMIT;
		keys = Scene.getKeys();

		// Starting sheet, idle
		setSheet(getThatSheet("idle"));
		setAnim(new Animation(getSheet(), 0, ANIM_INTERVALL));

		// Starting position
		setX(x);
		setY(y);
		setWidth(getAnim().getWidth());
		setHeight(getAnim().getHeight());

		// Facing in right dir
		setDir(1);
		setStanding(true);
		setOnGround(true);
		canEvolve = true;
		canAtack = true;

		setVisible(true);
	}

	@Override
	public final void animate() {
		if (curAtack == ATACK_1) {
			animateAtack1();
		}
		if (curAtack == ATACK_2) {
			animateAtack2();
		}
		if (curAtack == ATACK_3) {
			animateAtack3();
		}
		if (curAtack == ATACK_NO) {
			animateNoAtack();
		}

		getAnim().animate();

		// Special x-correction for facing right atack2, displays pics more left
		// because sprite does not begin where pokemons body is
		if (getDir() > 0 && getSheet().getName().contains("atack2")) {
			if (getSheet().getName().contains("feurigel")) {
				renderCorrection = RENDER_RIGHT_ATACK2_0;
			} else if (getSheet().getName().contains("igelavar")) {
				renderCorrection = RENDER_RIGHT_ATACK2_1;
			} else if (getSheet().getName().contains("tornupto")) {
				renderCorrection = RENDER_RIGHT_ATACK2_2;
			}
			// Default correction
		} else {
			renderCorrection = 0f;
		}
		
		setWidth((int) (getAnim().getWidth()
						- (getAnim().getWidth() * renderCorrection)));
		setHeight(getAnim().getHeight());
		
	}

	/**
	 * Starts an atack, level indicates which atack is used.
	 * 
	 * @param level
	 *            1 for atack1, 2 for atack2, 3 for atack3
	 */
	public final void atack(final int level) {
		// New atack blocked while atacking
		canAtack = false;
		if (level < ATACK_1 || level > ATACK_3) {
			curAtack = ATACK_1;
		} else {
			curAtack = level;
		}
	}

	/**
	 * Returns if player can atack.
	 * 
	 * @return True if player can atack
	 */
	public final boolean canAtack() {
		return canAtack;
	}

	/**
	 * Returns if player can evolve.
	 * 
	 * @return True if player can evolve
	 */
	public final boolean canEvolve() {
		return canEvolve;
	}

	/**
	 * Starts evolution.
	 */
	public final void evolve() {
		getLevelScene().getSound().play(Art.samples[Art.SAMPLE_EVOLVE], this,
				1, 1);

		// Increase current tier (evolution) and tierSpeedBonus
		tier++;
		tierSpeedBonus += TIER_SPEED_BONUS_AMOUNT;

		// Reset tier and tierSpeedBonus if maximum is reached
		if (tier > 2) {
			tier = 0;
			tierSpeedBonus = 1f;
		}

		// Setting the new punchlimit
		switch (tier) {
		case 0:
			punchlimit = PUNCHLIMIT_0;
			break;
		case 1:
			punchlimit = PUNCHLIMIT_1;
			break;
		case 2:
			punchlimit = PUNCHLIMIT_2;
			break;
		default:
			break;
		}
		// checkEvolve() will reset that, needed to prevent instant
		// evolution (1-time pressed but 2 evolutions)
		canEvolve = false;
	}

	/**
	 * Gets the current used atack.
	 * 
	 * @return 1 for atack1, 2 for atack2 and 3 for atack3
	 */
	public final int getCurAtack() {
		return curAtack;
	}

	/**
	 * Gets the firebeamlist which contains all currently displayed firebeams.
	 * 
	 * @return Firebeamlist which contains all currently displayed firebeams.
	 */
	public final List<Firebeam> getFirebeamlist() {
		return firebeamlist;
	}

	/**
	 * Gets the punchlist which contains all currently displayed punchs.
	 * 
	 * @return Punchlist which contains all currently displayed punchs.
	 */
	public final List<Punch> getPunchlist() {
		return punchlist;
	}

	/**
	 * Gets the Sheet with the given name and Players tier.
	 * 
	 * @param name
	 *            Name for the Sheet, walking, idle, atack1, atack2 and atack3
	 *            are provided
	 * @return Sheet specified by name and Players tier
	 */
	public final Sheet getThatSheet(final String name) {
		switch (name.toLowerCase()) {
		case "walking":
			switch (tier) {
			case 0:
				return Art.feurigel_walking;
			case 1:
				return Art.igelavar_walking;
			case 2:
				return Art.tornupto_walking;
			default:
				return null;
			}
		case "idle":
			switch (tier) {
			case 0:
				return Art.feurigel_idle;
			case 1:
				return Art.igelavar_idle;
			case 2:
				return Art.tornupto_idle;
			default:
				return null;
			}
		case "atack1":
			switch (tier) {
			case 0:
				return Art.feurigel_atack1;
			case 1:
				return Art.igelavar_atack1;
			case 2:
				return Art.tornupto_atack1;
			default:
				return null;
			}
		case "atack2":
			switch (tier) {
			case 0:
				return Art.feurigel_atack2;
			case 1:
				return Art.igelavar_atack2;
			case 2:
				return Art.tornupto_atack2;
			default:
				return null;
			}
		default:
			return null;
		}
	}

	/**
	 * Gets the current tier of the Player, e.g. 0 for Feurigel and 2 for
	 * Tornupto.
	 * 
	 * @return Tier of the Player, e.g. 0 for Feurigel and 2 for Tornupto
	 */
	public final int getTier() {
		return tier;
	}

	@Override
	public final void move() {

		// Movement left and right, only if atack is not
		// used and key_atack1 is not hold
		if (keys[Commons.KEY_RIGHT] && curAtack == ATACK_NO
				&& !keys[Commons.KEY_ATACK1]) {
			moveRight();
		} else if (keys[Commons.KEY_LEFT] && curAtack == ATACK_NO
				&& !keys[Commons.KEY_ATACK1]) {
			moveLeft();
		}

		// Start jump if not in air, not atacking and key_atack1 is not hold
		if (keys[Commons.KEY_JUMP] && !isJumping() && isOnGround()
				&& curAtack == ATACK_NO && !keys[Commons.KEY_ATACK1]) {
			moveJump();
		}

		moveUpDown();
	}

	@Override
	public final void render(final Graphics g, final float alpha) {
		
//		setAnimX((int) ((getXOld() + (getX() - getXOld()) * alpha) - (getAnim()
//				.getWidth() * renderCorrection)));
//		setAnimY((int) (getYOld() + (getY() - getYOld()) * alpha));
		setAnimX(getX() - (getAnim().getWidth() * renderCorrection));
		setAnimY(getY());
		
		if (isVisible()) {
			if (getDir() < 0) {
				g.drawImage(getAnim().getImage(), (int) getAnimX(), (int) (Commons.HEIGHT
						- getAnim().getHeight() - getAnimY()), null);
			} else {
				g.drawImage(getAnim().getImage(), (int) getAnimX(), (int) (Commons.HEIGHT
						- getAnim().getHeight() - getAnimY()), (int) (getAnimX()
						+ getAnim().getWidth()), (int) (Commons.HEIGHT - getAnimY()),
						getAnim().getWidth(), 0, 0, getAnim().getHeight(), null);
			}
		}
		// TODO All animations must look default to right

		for (Punch punch : punchlist) {
			punch.render(g, alpha);
		}
		for (Firebeam beam : firebeamlist) {
			beam.render(g, alpha);
		}
	}

	@Override
	public final void tick() {
		tick++;
		setXOld(getX());
		setYOld(getY());
		// Begin to move after 12 ticks
		if (tick > STARTING_TICK) {
			move();
			updateCoords();
			checkAtack();
			checkEvolve();
			checkPunchs();
			checkFirebeams();
		}
		animate();

		for (Punch punch : punchlist) {
			punch.tick();
		}
		for (Firebeam beam : firebeamlist) {
			beam.tick();
		}
	}

	/**
	 * Animation when atack1 is used.
	 */
	private void animateAtack1() {
		// TODO Only 2 atacks while jumping, Sprite only changes 2 times
		// TODO while jumping, so atack1Sprite is there most time
		if (getSheet() != getThatSheet("atack1")) {
			setSheet(getThatSheet("atack1"));
			setAnim(new Animation(getSheet(), 0, ANIM_ATACK_INTERVALL, 1));
			generatePunch();
		}
		if (getAnim().isFinished()) {
			curAtack = ATACK_NO;
			canAtack = true;
		}
	}

	/**
	 * Animation when atack2 is used.
	 */
	private void animateAtack2() {
		if (getSheet() != getThatSheet("atack2")) {
			setSheet(getThatSheet("atack2"));
			switch (tier) {
			case 0:
				setAnim(new Animation(getSheet(), 0, ANIM_ATACK_INTERVALL, 1,
						0, ANIM_ATACK2_START_0 - 1));
				break;
			case 1:
				setAnim(new Animation(getSheet(), 0, ANIM_ATACK_INTERVALL, 1,
						0, ANIM_ATACK2_START_1 - 1));
				break;
			case 2:
				setAnim(new Animation(getSheet(), 0, ANIM_ATACK_INTERVALL, 1,
						0, ANIM_ATACK2_START_2 - 1));
				break;
			default:
				break;
			}
		}
		// If starting animation is finished and key is
		// hold whole time, generate a firebeam
		if (getAnim().isFinished() && keys[Commons.KEY_ATACK2]) {
			switch (tier) {
			case 0:
				setAnim(new Animation(getSheet(), 0, ANIM_ATACK_INTERVALL,
						Animation.INFINITY_LOOPS, ANIM_ATACK2_START_0));
				generateFirebeam();
				break;
			case 1:
				setAnim(new Animation(getSheet(), 0, ANIM_ATACK_INTERVALL,
						Animation.INFINITY_LOOPS, ANIM_ATACK2_START_1));
				generateFirebeam();
				break;
			case 2:
				setAnim(new Animation(getSheet(), 0, ANIM_ATACK_INTERVALL,
						Animation.INFINITY_LOOPS, ANIM_ATACK2_START_2));
				generateFirebeam();
				break;
			default:
				break;
			}
		}
		// If there is a firebeam and key is not hold, let it die
		if (firebeamlist.size() > 0 && !keys[Commons.KEY_ATACK2]) {
			for (Firebeam beam : firebeamlist) {
				beam.die();
			}
		}
		if (!keys[Commons.KEY_ATACK2]) {
			curAtack = ATACK_NO;
			canAtack = true;
		}
	}

	/**
	 * Animation when atack3 is used.
	 */
	private void animateAtack3() {
		curAtack = ATACK_NO;
		canAtack = true;
	}

	/**
	 * Animation when no atack is used.
	 */
	private void animateNoAtack() {
		// If standing still and sheet is not the idle sprite, set it
		if (isStanding() && isOnGround() && getSheet() != getThatSheet("idle")) {
			setSheet(getThatSheet("idle"));
			setAnim(new Animation(getSheet(), 0, ANIM_INTERVALL));
			// If walking on ground and sheet is not the walking sprite, set it
		} else if (!isStanding() && isOnGround()
				&& getSheet() != getThatSheet("walking")) {
			// TODO WIRD VON JUMP AUF BODEN NICHT
			// GETRIGGERED DA JUMP == WALKINGSPRITE!!!
			// TODO NEED JUMP SPRITE
			setSheet(getThatSheet("walking"));
			setAnim(new Animation(getSheet(), 0, ANIM_INTERVALL));
		}
	}

	/**
	 * Checks if player can and wants to atack, triggers atack.
	 */
	private void checkAtack() {
		if (canAtack) {
			if (keys[Commons.KEY_ATACK3]) {
				atack(ATACK_3);
			} else if (keys[Commons.KEY_ATACK2]) {
				atack(ATACK_2);
			} else if (keys[Commons.KEY_ATACK1]) {
				atack(ATACK_1);
			}
		}
	}

	/**
	 * Checks if player can and wants to evolve, triggers evolve.
	 */
	private void checkEvolve() {
		if (keys[Commons.KEY_EVOLVE] && canEvolve) {
			evolve();
		} else if (!keys[Commons.KEY_EVOLVE]) {
			canEvolve = true;
		}
		// Block evolving if in air or atacking
		if ((!isOnGround() || isJumping() || curAtack != ATACK_NO) && canEvolve) {
			canEvolve = false;
		}
	}

	/**
	 * Checks all {@link Firebeam}s and removes them from the list if they are
	 * dead.
	 */
	private void checkFirebeams() {
		List<Firebeam> copy = new ArrayList<Firebeam>();
		for (Firebeam beam : firebeamlist) {
			copy.add(beam);
		}
		for (Firebeam beam : copy) {
			if (beam.isDead()) {
				firebeamlist.remove(beam);
			}
		}
	}

	/**
	 * Checks all {@link Punch}s and removes them from the list if they are
	 * dead.
	 */
	private void checkPunchs() {
		List<Punch> copy = new ArrayList<Punch>();
		for (Punch punch : punchlist) {
			copy.add(punch);
		}
		for (Punch punch : copy) {
			if (punch.isDead()) {
				punchlist.remove(punch);
			}
		}
	}

	/**
	 * Generates a new {@link Firebeam} if limit is not reached.
	 */
	private void generateFirebeam() {
		if (firebeamlist.size() >= firebeamlimit) {
			return;
		} else {
			float beamY = Commons.HEIGHT;
			// TODO Y-Coord should be calculated by the beam, not player
			switch (tier) {
			case 0:
				beamY -= (getAnim().getHeight() * FIREBEAM_Y_OFFSET_0);
				break;
			case 1:
				beamY -= (getAnim().getHeight() * FIREBEAM_Y_OFFSET_1);
				break;
			case 2:
				beamY -= (getAnim().getHeight() * FIREBEAM_Y_OFFSET_2);
				break;
			default:
				break;
			}
			firebeamlist.add(new Firebeam(this, beamY));
		}
	}

	/**
	 * Generates a new {@link Punch} if limit is not reached.
	 */
	private void generatePunch() {
		if (punchlist.size() >= punchlimit) {
			return;
		} else {
			// TODO Y-Coord should be calculated by the punch, not player
			Random rnd = new Random();
			float punchY = Commons.HEIGHT
					- getY()
					- (getAnim().getHeight() * PUNCH_Y_OFFSET)
					+ (rnd.nextFloat() * PUNCH_RANDOM_FACTOR * getAnim()
							.getHeight());
			punchlist.add(new Punch(this, punchY));
		}
	}

	/**
	 * Movement for jumping. Starts a jump by playing its sound and setting the
	 * right animation and jumpTicks.
	 */
	private void moveJump() {
		getLevelScene().getSound().play(Art.samples[Art.SAMPLE_PLAYER_JUMP],
				this, 1, 1);
		setJumping(true);
		setOnGround(false);
		// Walking sprite xPic 1 for jumping animation
		setSheet(getThatSheet("walking"));
		setAnim(new Animation(getSheet(), 0, ANIM_INTERVALL, 1, 1, 1));
		// Ticks for jumping-acceleration
		jumpTime = JUMPTICKS;
	}

	/**
	 * Movement for left. Calculates the right xa which will be added to Players
	 * x-coord
	 */
	private void moveLeft() {
		setStanding(false);
		setDir(-1);
		if (!isJumping() && isOnGround() && curAtack == ATACK_NO) {
			setXA(getXA() - GROUNDSIDEWAYSPEED * tierSpeedBonus);
		} else if (curAtack != ATACK_NO && !isJumping() && isOnGround()) {
			return;
		} else {
			setXA(getXA() - AIRSIDEWAYSPEED * tierSpeedBonus);
		}
	}

	/**
	 * Movement for right. Calculates the right xa which will be added to
	 * Players x-coord
	 */
	private void moveRight() {
		// Start movement
		setStanding(false);
		// Facing dir for right
		setDir(1);
		// Use sidewayspeed for ground or air to increment x-coord
		// Only walk on ground if not atacking
		if (!isJumping() && isOnGround() && curAtack == ATACK_NO) {
			setXA(getXA() + GROUNDSIDEWAYSPEED * tierSpeedBonus);
		} else if (curAtack != ATACK_NO && !isJumping() && isOnGround()) {
			return;
		} else {
			setXA(getXA() + AIRSIDEWAYSPEED * tierSpeedBonus);
		}
	}

	/**
	 * Movement for up and down. Calculates the right ya which will be added to
	 * Players y-coord. Mostly used for jumping and falling.
	 */
	private void moveUpDown() {
		// TODO Das sollte von der PhysicEngine übernommen werden,
		// vllt. eine dort eine Methode
		// TODO jump() welche werte aus der Form nimmt
		// (selbes gilt für Testball usw.)

		// Accelerate upwards jumpTimes
		if (jumpTime > 0) {
			// Use upwayspeed to increment y-coord
			setYA(getYA() + UPWAYSPEED);
			jumpTime--;
			// Now switch dir to downwards if watershed is reached
		} else if (getYA() < JUMPWATERSHED && getYA() > 0 && !isOnGround()) {
			// Use downwayspeed to decrement y-coord
			setYA(getForm().getDownwaySpeed());
			// If not atacking in air use walking sprite
			// xPic 2 for falling animation
			if (curAtack == ATACK_NO) {
				setSheet(getThatSheet("walking"));
				setAnim(new Animation(getSheet(), 0, ANIM_INTERVALL, 1, 2, 2));
			}
			// Begin to accelerate downwards till speed limit is reached
		} else if (getYA() < 0 && getYA() > getForm().getFallingSpeedLimit()
				&& !isOnGround()) {
			// Use gravity to accelerate downwards
			setYA(getYA() * PhysicEngine.GRAVITY);
		}
	}
}
