package de.zabuza.jumpPokemon.sprites;

import java.awt.Graphics;

import de.zabuza.jumpPokemon.Animation;
import de.zabuza.jumpPokemon.Art;
import de.zabuza.jumpPokemon.Commons;
import de.zabuza.jumpPokemon.scenes.LevelScene;
import de.zabuza.jumpPokemon.scenes.Scene;
import de.zabuza.physicEngine.PhysicEngine;
import de.zabuza.physicEngine.forms.Ball;

/**
 * Testball class.
 * 
 * @author Zabuza
 * 
 */
public class Testball extends Sprite {

	/**
	 * Sidewayspeed of ball when on ground.
	 */
	private static final float GROUNDSIDEWAYSPEED = 6.0f;
	/**
	 * Sidewayspeed of ball when in air.
	 */
	private static final float AIRSIDEWAYSPEED = 2.5f;
	/**
	 * Sidewayspeed limit.
	 */
	private static final float SIDEWAYSPEEDLIMIT = 50f;
	/**
	 * Upwayspeed of ball.
	 */
	private static final float UPWAYSPEED = 5.0f;
	/**
	 * Watershed while jumping when ball will then accelerate downwards.
	 */
	private static final float JUMPWATERSHED = 10.0f;
	/**
	 * Amount of maximal jumps the ball can do.
	 */
	private static final int MAX_JUMPS = 19;
	/**
	 * Intervall for balls animations in ticks.
	 */
	private static final int ANIM_INTERVALL = 4;

	/**
	 * Current amount of ticks in which ball will be accelerated upwards when
	 * jumping.
	 */
	private int jumpTime;
	/**
	 * Key container of the scene where the ball is located.
	 */
	private boolean[] keys;

	/**
	 * How much jumps the ball can do.
	 */
	private int jumps = MAX_JUMPS;

	/**
	 * Creates a new Testball.
	 * 
	 * @param level
	 *            LevelScene which contains the Testball
	 * @param x
	 *            X-coord of balls starting position
	 * @param y
	 *            Y-coord of balls starting position
	 */
	public Testball(final LevelScene level, final int x, final int y) {
		setLevelScene(level);
		setForm(new Ball());

		keys = Scene.getKeys();

		setSheet(Art.testball);
		setAnim(new Animation(getSheet(), 0, ANIM_INTERVALL,
				Animation.INFINITY_LOOPS, 0, 0));
		setX(x);
		setY(y);
		setWidth(getAnim().getWidth());
		setHeight(getAnim().getHeight());

		setStanding(true);
		setJumping(false);
		setOnGround(false);

		setVisible(true);
	}

	@Override
	public void animate() {
	}

	/**
	 * Gets the current amount of jumps the ball can do.
	 * 
	 * @return Amount of balls jumps
	 */
	public final int getJumps() {
		return jumps;
	}

	@Override
	public final void move() {
		// Reset jumps and y-coord when key is pressed
		if (keys[Commons.KEY_ATACK3]) {
			setY(Commons.HEIGHT - getAnim().getHeight());
			setOnGround(false);
			setJumping(false);
			setStanding(true);
			setXA(0);
			setYA(0);
			jumps = MAX_JUMPS;
		}

		if (keys[Commons.KEY_RIGHT]) {
			moveRight();
		} else if (keys[Commons.KEY_LEFT]) {
			moveLeft();
		}

		// Start a new jump when not jumping, on ground and
		// jumpTicks greater than 0
		if (!isJumping() && isOnGround() && jumps > 0) {
			jumps--;
			moveJump();
		}

		moveUpDown();
	}

	@Override
	public final void render(final Graphics g, final float alpha) {

		setAnimX(getXOld() + (getX() - getXOld()) * alpha);
		setAnimY(getYOld() + (getY() - getYOld()) * alpha);

		if (isVisible()) {
			g.drawImage(getAnim().getImage(), (int) getAnimX(), (int) (Commons.HEIGHT
					- getAnim().getHeight() - getAnimY()), null);
		}
	}

	@Override
	public final void tick() {
		setXOld(getX());
		setYOld(getY());
		move();
		updateCoords();
		animate();
	}

	/**
	 * Movement for jumping. Starts a jump by playing its sound and setting the
	 * right animation and jumpTicks.
	 */
	private void moveJump() {
		getLevelScene().getSound().play(Art.samples[Art.SAMPLE_SHELL_BUMP],
				this, 1, 1);
		setJumping(true);
		setOnGround(false);
		// Ticks for jumping-acceleration
		jumpTime = jumps;
	}

	/**
	 * Movement for left. Calculates the right xa which will be added to
	 * Testballs x-coord
	 */
	private void moveLeft() {
		setStanding(false);
		setDir(-1);
		if (!isJumping() && isOnGround()) {
			setXA(getXA() - GROUNDSIDEWAYSPEED);
			if (getXA() <= -SIDEWAYSPEEDLIMIT) {
				setXA(-SIDEWAYSPEEDLIMIT);
			}
		} else if (!isJumping() && isOnGround()) {
			return;
		} else {
			setXA(getXA() - AIRSIDEWAYSPEED);
			if (getXA() <= -SIDEWAYSPEEDLIMIT) {
				setXA(-SIDEWAYSPEEDLIMIT);
			}
		}
	}

	/**
	 * Movement for right. Calculates the right xa which will be added to
	 * Testballs x-coord
	 */
	private void moveRight() {
		// Start movement
		setStanding(false);
		// Facing dir for right
		setDir(1);
		// Use sidewayspeed for ground or air to increment x-coord
		if (!isJumping() && isOnGround()) {
			setXA(getXA() + GROUNDSIDEWAYSPEED);
			if (getXA() >= SIDEWAYSPEEDLIMIT) {
				setXA(SIDEWAYSPEEDLIMIT);
			}
		} else if (!isJumping() && isOnGround()) {
			return;
		} else {
			setXA(getXA() + AIRSIDEWAYSPEED);
			if (getXA() >= SIDEWAYSPEEDLIMIT) {
				setXA(SIDEWAYSPEEDLIMIT);
			}
		}
	}

	/**
	 * Movement for up and down. Calculates the right ya which will be added to
	 * Testballs y-coord. Mostly used for jumping and falling.
	 */
	private void moveUpDown() {
		// Accelerate upwards jumpTimes
		if (jumpTime > 0) {
			// Use upwayspeed to increment y-coord
			setYA(getYA() + UPWAYSPEED);
			jumpTime--;
			// Now switch dir to downwards if watershed is reached
		} else if (getYA() < JUMPWATERSHED && getYA() > 0 && !isOnGround()) {
			// Use downwayspeed to decrement y-coord
			setYA(getForm().getDownwaySpeed());
			// Begin to accelerate downwards till speed limit is reached
		} else if (getYA() < 0 && getYA() > getForm().getFallingSpeedLimit()
				&& !isOnGround()) {
			// Use gravity to accelerate downwards
			setYA(getYA() * PhysicEngine.GRAVITY);
		}
	}
}
