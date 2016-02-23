package de.zabuza.physicEngine.forms;

import de.zabuza.jumpPokemon.sprites.Sprite;
import de.zabuza.physicEngine.PhysicEngine;

//TODO Der Aufbau ist irgendwie komisch, sollte anders gemacht werden
/**
 * Interface for Forms. Needed for {@link Sprite}s which want to be affected by
 * a {@link PhysicEngine}.
 * 
 * @author Zabuza
 * 
 */
public abstract interface Form {

	/**
	 * Gets the air inertia of the Form.
	 * 
	 * @return Air inertia of the Form
	 */
	float getAirInertia();

	/**
	 * Gets the downway speed of the Form.
	 * 
	 * @return Downway speed of the Form
	 */
	float getDownwaySpeed();

	/**
	 * Gets the falling speed limit of the Form.
	 * 
	 * @return Falling speed limit of the Form
	 */
	float getFallingSpeedLimit();

	/**
	 * Gets the ground inertia of the Form.
	 * 
	 * @return Ground inertia of the Form
	 */
	float getGroundInertia();

}
