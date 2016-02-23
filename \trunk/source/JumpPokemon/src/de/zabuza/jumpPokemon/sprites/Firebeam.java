package de.zabuza.jumpPokemon.sprites;

import java.awt.Graphics;

import de.zabuza.jumpPokemon.Animation;
import de.zabuza.jumpPokemon.Art;
import de.zabuza.jumpPokemon.Commons;
import de.zabuza.jumpPokemon.Sheet;
import de.zabuza.physicEngine.forms.NoPhysic;

/**
 * Firebeam class.
 * 
 * @author Zabuza
 * 
 */
public class Firebeam extends Sprite {

	/**
	 * Length of the firebeam sound in seconds.
	 */
	private static final float SOUND_LENGTH = 1.435f;
	/**
	 * Intervall of the animation in ticks.
	 */
	private static final int ANIM_INTERVALL = 3;
	/**
	 * Render correction of x-coord when players dir is left and tier is 1.
	 */
	private static final float RENDER_X_LEFT_1 = 0.05f;
	/**
	 * Render correction of x-coord when players dir is left and tier is 2.
	 */
	private static final float RENDER_X_LEFT_2 = 0.1f;
	/**
	 * Render correction of x-coord when players dir is right and tier is 0.
	 */
	private static final float RENDER_X_RIGHT_0 = 0.5f;
	/**
	 * Render correction of x-coord when players dir is right and tier is 1.
	 */
	private static final float RENDER_X_RIGHT_1 = 0.65f;
	/**
	 * Render correction of x-coord when players dir is right and tier is 2.
	 */
	private static final float RENDER_X_RIGHT_2 = 0.63f;

	/**
	 * Player sprite who created the firebeam.
	 */
	private Player player;
	/**
	 * Contains the current y-coord of the player, used if e.g. player is
	 * jumping.
	 */
	private float playerYOffset;
	/**
	 * Simulate sound loop by starting a new sound every soundTick.
	 */
	private int soundTick = (int) (Commons.TICKS_PER_SECOND * SOUND_LENGTH);
	/**
	 * Current tick of the sprite.
	 */
	private int tick;

	/**
	 * Create a new Firebeam at the given y-coord. X-coord is calculated by the
	 * Firebeam.
	 * 
	 * @param thatPlayer
	 *            Player which created the Firebeam
	 * @param y
	 *            Y-coord of the Firebeam
	 */
	public Firebeam(final Player thatPlayer, final float y) {
		this.player = thatPlayer;
		setLevelScene(thatPlayer.getLevelScene());
		setForm(new NoPhysic());

		setSheet(thatPlayer.getTier());
		setAnim(new Animation(getSheet(), 0, ANIM_INTERVALL));
		
		setDir(thatPlayer.getDir());
		setY(y - getAnim().getHeight());
		// Used to calculate the x-coord
		move();

		setVisible(true);
		getLevelScene().getSound().play(
				Art.samples[Art.SAMPLE_PLAYER_FIREBEAM], this, 1, 1);
	}

	@Override
	public final void animate() {
		getAnim().animate();
	}

	@Override
	public final void move() {
		if (player.getDir() < 0) {
			if (player.getTier() == 1) {
				setX(player.getX()
						+ (player.getAnim().getWidth() * RENDER_X_LEFT_1));
			} else if (player.getTier() == 2) {
				setX(player.getX()
						+ (player.getAnim().getWidth() * RENDER_X_LEFT_2));
			} else {
				setX(player.getX());
			}
		} else {
			if (player.getTier() == 0) {
				setX(player.getX()
						+ (player.getAnim().getWidth() * RENDER_X_RIGHT_0));
			} else if (player.getTier() == 1) {
				setX(player.getX()
						+ (player.getAnim().getWidth() * RENDER_X_RIGHT_1));
			} else if (player.getTier() == 2) {
				setX(player.getX()
						+ (player.getAnim().getWidth() * RENDER_X_RIGHT_2));
			}
		}

		playerYOffset = player.getY();
	}

	@Override
	public final void render(final Graphics g, final float alpha) {
		setAnimX(getXOld() + (getX() - getXOld()) * alpha);
		setAnimY((getYOld() + (getY() - getYOld()) * alpha) - playerYOffset);

		if (isVisible()) {
			if (getDir() >= 0) {
				g.drawImage(getAnim().getImage(), (int) getAnimX(), (int) getAnimY(), null);
			} else {
				g.drawImage(getAnim().getImage(), (int) (getAnimX()
						- getAnim().getWidth()), (int) getAnimY(), (int) getAnimX(),
						(int) (getAnimY() + getAnim().getHeight()), getAnim()
								.getWidth(), 0, 0, getAnim().getHeight(), null);
			}
		}
	}

	@Override
	public final void tick() {
		tick++;
		setXOld(getX());
		setYOld(getY());
		// TODO Sound stoppt noch nicht wenn Firebeam stirbt
		// Simulate sound loop by starting a new sound every soundTick
		if (tick == soundTick) {
			getLevelScene().getSound().play(
					Art.samples[Art.SAMPLE_PLAYER_FIREBEAM], this, 1, 1);
			soundTick += (int) (Commons.TICKS_PER_SECOND * SOUND_LENGTH);
		}
		move();
		updateCoords();
		animate();
	}

	/**
	 * Sets the right {@link Sheet} for the Firebeam by using the tier of the
	 * {@link Player}.
	 * 
	 * @param tier
	 *            Tier of the player, e.g. 0 for Feurigel and 2 for Tornupto
	 */
	private void setSheet(final int tier) {
		switch (tier) {
		case 0:
			setSheet(Art.firebeam1);
			break;
		case 1:
			setSheet(Art.firebeam2);
			break;
		case 2:
			setSheet(Art.firebeam3);
			break;
		default:
			break;
		}
	}
}
