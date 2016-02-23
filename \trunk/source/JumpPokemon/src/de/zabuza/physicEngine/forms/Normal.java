package de.zabuza.physicEngine.forms;

/**
 * Normal Form. Medium speed and inertia.
 * 
 * @author Zabuza
 * 
 */
public class Normal implements Form {

	/**
	 * Downwayspeed of the normal form.
	 */
	private static final float DOWNWAYSPEED = -5.0f;
	/**
	 * Limit of normal forms fallingspeed.
	 */
	private static final float FALLINGSPEEDLIMIT = -14f;
	/**
	 * Inertia of normal form when its on the ground.
	 */
	private static final float GROUND_INERTIA = 0.75f;
	/**
	 * Inertia of normal form when its in the air.
	 */
	private static final float AIR_INERTIA = 0.9f;

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