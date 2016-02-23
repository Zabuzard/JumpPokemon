package de.zabuza.physicEngine;

import de.zabuza.jumpPokemon.Animation;
import de.zabuza.jumpPokemon.scenes.LevelScene;
import de.zabuza.jumpPokemon.scenes.Scene;
import de.zabuza.jumpPokemon.sprites.Player;
import de.zabuza.jumpPokemon.sprites.Sprite;
import de.zabuza.physicEngine.forms.NoPhysic;

//TODO Muss mehr portable sein, darf nix außerhalb des package importieren,
//sollte Schnittstellen haben die dann z.B. von Sprite extended werden.
/**
 * Portable PhysicEngine. Uses {@link forms.Form}s which contains many
 * constants. Use affect(Sprite) to change its coordinates with the engine.
 * 
 * @author Zabuza
 * 
 */
public class PhysicEngine {

	// TODO Sollte eigentlich private sein aber Player braucht
	// es gerade noch, sollte aber eig die Engine machen.
	/**
	 * Gravity influence as factor.
	 */
	public static final float GRAVITY = 1.4f;
	/**
	 * Air drag influence as factor.
	 */
	private static final float AIR_DRAG = 0.85f;
	/**
	 * Limit of inertia.
	 */
	private static final float INERTIA_LIMIT = 0.5f;
	/**
	 * Intervall for players animations in ticks.
	 */
	private static final int ANIM_INTERVALL = 4;

	// XXX Remove suppress unused in future, evtl. it will be needed
	/**
	 * Current Scene which uses the PhysicEngine.
	 */
	@SuppressWarnings("unused")
	private Scene scene;

	/**
	 * Creates a new PhysicEngine.
	 */
	public PhysicEngine() {

	}

	/**
	 * Affects the coordinates of a {@link Sprite} by using their physically
	 * {@link forms.Form}.
	 * 
	 * @param sprite
	 *            Sprite to affect
	 */
	public final void affect(final Sprite sprite) {
		// Only affect if form is not NoPhysic
		if (!(sprite.getForm() instanceof NoPhysic)) {
			gravity(sprite);
			inertia(sprite);
			airDrag(sprite);
		}
	}

	/**
	 * Sets the Scene where the PhysicEngine is used.
	 * 
	 * @param thatScene
	 *            Scene where the PhysicEngine is used
	 */
	public final void setScene(final Scene thatScene) {
		this.scene = thatScene;
	}

	/**
	 * Affects the coordinates of a Sprite by using air drag.
	 * 
	 * @param sprite
	 *            Sprite to affect with air drag
	 */
	private void airDrag(final Sprite sprite) {
		// Use the air drag to decrease the
		// acceleration in y-dir when jumping upwards
		if (sprite.isJumping() && sprite.getYA() > 0) {
			sprite.setYA(sprite.getYA() * AIR_DRAG);
		}
	}

	/**
	 * Affects the coordinates of a Sprite by using gravity.
	 * 
	 * @param sprite
	 *            Sprite to affect with gravity
	 */
	private void gravity(final Sprite sprite) {
		// Generall gravity influence if not jumping
		if (!sprite.isJumping() && sprite.getY() > LevelScene.GROUND_HEIGHT) {
			if (sprite.getYA() > sprite.getForm().getDownwaySpeed()) {
				// Use downwayspeed to decrement y-coord
				sprite.setYA(sprite.getForm().getDownwaySpeed());
				if (sprite instanceof Player) {
					// If not atacking in air use walking sprite
					// xPic 2 for falling animation
					if (((Player) sprite).getCurAtack() == 0) {
						// TODO Das sollte der Player machen, nicht die Engine
						sprite.setSheet(((Player) sprite)
								.getThatSheet("walking"));
						sprite.setAnim(new Animation(sprite.getSheet(), 0,
								ANIM_INTERVALL, 1, 2, 2));
					}
				}
				// Begin to accelerate downwards till speed limit is reached
			} else if (sprite.getYA() < 0
					&& sprite.getYA() > sprite.getForm().getFallingSpeedLimit()) {
				// Use gravity to accelerate downwards
				sprite.setYA(sprite.getYA() * GRAVITY);
			}
		}
	}

	/**
	 * Affects the coordinates of a Sprite by using inertia.
	 * 
	 * @param sprite
	 *            Sprite to affect with inertia
	 */
	private void inertia(final Sprite sprite) {
		// Use ground and air inertia to decrease the acceleration in x-dir
		if (sprite.isOnGround()) {
			sprite.setXA(sprite.getXA() * sprite.getForm().getGroundInertia());
		} else {
			sprite.setXA(sprite.getXA() * sprite.getForm().getAirInertia());
		}

		// Stop influence of inertia which causes
		// sliding in x-dir when limit is reached
		if (Math.abs(sprite.getXA()) < INERTIA_LIMIT) {
			sprite.setXA(0);
			sprite.setStanding(true);
		}
	}
}
