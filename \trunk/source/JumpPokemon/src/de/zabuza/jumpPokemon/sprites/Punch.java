package de.zabuza.jumpPokemon.sprites;

import java.awt.Graphics;

import de.zabuza.jumpPokemon.Animation;
import de.zabuza.jumpPokemon.Art;
import de.zabuza.jumpPokemon.Sheet;
import de.zabuza.physicEngine.forms.NoPhysic;

/**
 * Punch class.
 * 
 * @author Zabuza
 * 
 */
public class Punch extends Sprite {

	/**
	 * Intervall of punchs animation in ticks.
	 */
	private static final int ANIM_INTERVALL = 3;
	/**
	 * X-coord offset when punchs dir is left.
	 */
	private static final float OFFSET_X_LEFT = 0.25f;
	/**
	 * X-coord offset when punchs dir is right.
	 */
	private static final float OFFSET_X_RIGHT = 0.75f;

	/**
	 * Player sprite which created the punch.
	 */
	private Player player;

	/**
	 * Create a new Punch at the given y-coord. X-coord is calculated by the
	 * Punch.
	 * 
	 * @param thatPlayer
	 *            Player which created the Punch
	 * @param y
	 *            Y-coord of the Punch
	 */
	public Punch(final Player thatPlayer, final float y) {
		this.player = thatPlayer;
		setLevelScene(thatPlayer.getLevelScene());
		setForm(new NoPhysic());

		setSheet(thatPlayer.getTier());
		setAnim(new Animation(getSheet(), 0, ANIM_INTERVALL, 1));
		setDir(thatPlayer.getDir());
		setY(y - getAnim().getHeight());
		// Used to calculate the x-coord
		move();

		setVisible(true);
		getLevelScene().getSound().play(Art.samples[Art.SAMPLE_PLAYER_PUNCH],
				this, 1, 1);
	}

	@Override
	public final void animate() {
		getAnim().animate();
		// If animation is finished, let the punch die
		if (getAnim().isFinished()) {
			die();
		}
	}

	@Override
	public final void move() {
		if (getDir() < 0) {
			setX(player.getX() + (player.getAnim().getWidth() * OFFSET_X_LEFT));
		} else {
			setX(player.getX() + (player.getAnim().getWidth() * OFFSET_X_RIGHT));
		}
	}

	@Override
	public final void render(final Graphics g, final float alpha) {
		setAnimX(getXOld() + (getX() - getXOld()) * alpha);
		setAnimY(getYOld() + (getY() - getYOld()) * alpha);

		//TODO Player and Testballs animY is relative to bottom corner and not absolute
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
		setXOld(getX());
		setYOld(getY());
		move();
		updateCoords();
		animate();
	}

	/**
	 * Sets the right {@link Sheet} for the Punch by using the tier of the
	 * {@link Player}.
	 * 
	 * @param tier
	 *            Tier of the player, e.g. 0 for Feurigel and 2 for Tornupto
	 */
	private void setSheet(final int tier) {
		switch (tier) {
		case 0:
			setSheet(Art.punch1);
			break;
		case 1:
			setSheet(Art.punch2);
			break;
		case 2:
			setSheet(Art.punch3);
			break;
		default:
			break;
		}
	}
}