package de.zabuza.physicEngine.forms;

/**
 * NoPhysic Form. Not affected by all physics
 * 
 * @author Zabuza
 * 
 */
public class NoPhysic implements Form {

	@Override
	public final float getAirInertia() {
		return 0;
	}

	@Override
	public final float getDownwaySpeed() {
		return 0;
	}

	@Override
	public final float getFallingSpeedLimit() {
		return 0;
	}

	@Override
	public final float getGroundInertia() {
		return 0;
	}
}