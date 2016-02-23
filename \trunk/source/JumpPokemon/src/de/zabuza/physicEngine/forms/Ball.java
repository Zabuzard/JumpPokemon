package de.zabuza.physicEngine.forms;

/**
 * Ball Form. Standard speed, very low inertia (causes long sliding)
 * 
 * @author Zabuza
 * 
 */
public class Ball implements Form {

	/**
	 * Downwayspeed of the ball.
	 */
	private static final float DOWNWAYSPEED = -5.0f;
	/**
	 * Limit of balls fallingspeed.
	 */
	private static final float FALLINGSPEEDLIMIT = -14f;
	/**
	 * Inertia of ball when its on the ground.
	 */
	private static final float GROUND_INERTIA = 0.99f;
	/**
	 * Inertia of ball when its in the air.
	 */
	private static final float AIR_INERTIA = 0.995f;

	@Override
	public final float getAirInertia() {
		return AIR_INERTIA;
	}

	@Override
	public final float getDownwaySpeed() {
		return DOWNWAYSPEED;
	}

	@Override
	public final float getFallingSpeedLimit() {
		return FALLINGSPEEDLIMIT;
	}

	@Override
	public final float getGroundInertia() {
		return GROUND_INERTIA;
	}
}