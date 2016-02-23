package de.zabuza.jumpPokemon.sprites;

import java.awt.Graphics;
import java.awt.Image;

import de.zabuza.jumpPokemon.Animation;
import de.zabuza.jumpPokemon.Sheet;
import de.zabuza.jumpPokemon.collision.Hitbox;
import de.zabuza.jumpPokemon.scenes.LevelScene;
import de.zabuza.physicEngine.PhysicEngine;
import de.zabuza.physicEngine.forms.Form;
import de.zabuza.soundEngine.SoundSource;

/**
 * Abstract class for Sprites.
 * 
 * @author Zabuza
 * 
 */
public abstract class Sprite implements SoundSource, Hitbox {

	/**
	 * Current Sheet to animate.
	 */
	private Sheet sheet;
	/**
	 * Current Animation.
	 */
	private Animation anim;
	
	/**
	 * LevelScene which contains the Sprite.
	 */
	private LevelScene levelScene;
	/**
	 * Physically Form of the Sprite.
	 */
	private Form form;
	/**
	 * X, y are current coords, xOld and yOld are old coords, xa, ya will be
	 * added to x and y.
	 */
	private float x, y, xOld, yOld, xa, ya;
	/**
	 * Width of the sprite.
	 */
	private int width;
	/**
	 * Height of the sprite.
	 */
	private int height;
	/**
	 * X- and y-coord of sprites Animation.
	 */
	private float animX, animY;

	/**
	 * True if the sprite is standing still.
	 */
	private boolean standing;
	/**
	 * True if the sprite is jumping.
	 */
	private boolean jumping;
	/**
	 * True if the sprite is on the ground and not in the air.
	 */
	private boolean onGround;

	/**
	 * Dir of Sprite -1 for left, 1 for right.
	 */
	private int dir;
	/**
	 * True if the sprite is visible.
	 */
	private boolean visible;
	/**
	 * True if the sprite is dead.
	 */
	private boolean dead;

	/**
	 * Animation of the Sprite.
	 */
	public abstract void animate();

	/**
	 * Gets the x-coord of sprites {@link Animation}.
	 * 
	 * @return x-coord of sprites {@link Animation}
	 */
	@Override
	public final float getAnimX() {
		return animX;
	}

	/**
	 * Gets the y-coord of sprites {@link Animation}.
	 * 
	 * @return y-coord of sprites {@link Animation}
	 */
	@Override
	public final float getAnimY() {
		return animY;
	}

	/**
	 * Gets the dir of the sprite, -1 for left and 1 for right.
	 * 
	 * @return Dir of the sprite, -1 for left and 1 for right
	 */
	public final int getDir() {
		return dir;
	}

	/**
	 * Gets the current Form of the sprite.
	 * 
	 * @return Current Form of the sprite
	 */
	public final Form getForm() {
		return form;
	}

	/**
	 * Gets the current used Sheet of the Sprite.
	 * 
	 * @return Current used Sheet of the Sprite
	 */
	public final Sheet getSheet() {
		// TODO Methode muss protected sein,
		// PhysicEngine braucht aber solang dort das TO-DO ist noch public.
		return sheet;
	}

	@Override
	public final float getX() {
		return x;
	}

	/**
	 * Gets the current xa of the sprite.
	 * 
	 * @return Current xa
	 */
	@Override
	public final float getXA() {
		return xa;
	}

	/**
	 * Gets the current old x-coord of the sprite.
	 * 
	 * @return Current old x-coord
	 */
	public final float getXOld() {
		return xOld;
	}

	@Override
	public final float getY() {
		return y;
	}

	/**
	 * Gets the current ya of the sprite.
	 * 
	 * @return Current ya
	 */
	@Override
	public final float getYA() {
		return ya;
	}

	/**
	 * Gets the current old y-coord of the sprite.
	 * 
	 * @return Current old y-coord
	 */
	public final float getYOld() {
		return yOld;
	}

	/**
	 * Returnes if the Sprite is dead and should be removed.
	 * 
	 * @return True if the Sprite is dead and should be removed
	 */
	public final boolean isDead() {
		return dead;
	}

	/**
	 * Returns if sprite is jumping.
	 * 
	 * @return True if sprite is jumping
	 */
	public final boolean isJumping() {
		return jumping;
	}

	/**
	 * Returns if sprite is on the ground and not in the air.
	 * 
	 * @return True if sprite is on the ground
	 */
	public final boolean isOnGround() {
		return onGround;
	}

	/**
	 * Returns if sprite is standing still.
	 * 
	 * @return True if sprite is standing
	 */
	public final boolean isStanding() {
		return standing;
	}

	/**
	 * Returns the visiblity of the Sprite, true for visible.
	 * 
	 * @return Visibility of the Sprite, true for visible
	 */
	public final boolean isVisible() {
		return visible;
	}

	/**
	 * Movement of Sprite, mostly it will affect xa and ya before using
	 * updateCoords().
	 */
	public abstract void move();

	/**
	 * Renders the Sprite on a {@link Graphics} object.
	 * 
	 * @param g
	 *            Graphics object to draw on
	 * @param alpha
	 *            1 if all ticks for the second are done, 0 if none is done, 0.5
	 *            if half etc.
	 */
	public abstract void render(Graphics g, float alpha);

	/**
	 * Sets the current Animation.
	 * 
	 * @param thatAnim
	 *            Animation to set
	 */
	public final void setAnim(final Animation thatAnim) {
		// TODO Methode muss protected sein,
		// PhysicEngine braucht aber solang dort das TO-DO ist noch public.
		this.anim = thatAnim;
	}

	/**
	 * Sets if sprite is jumping.
	 * 
	 * @param thatJumping
	 *            True if sprite is jumping
	 */
	public final void setJumping(final boolean thatJumping) {
		this.jumping = thatJumping;
	}

	/**
	 * Sets if sprite is on the ground and not in the air.
	 * 
	 * @param thatOnGround
	 *            True if sprite is on the ground
	 */
	public final void setOnGround(final boolean thatOnGround) {
		this.onGround = thatOnGround;
	}

	/**
	 * Sets the current used Sheet of the Sprite.
	 * 
	 * @param thatSheet
	 *            Sheet to set
	 */
	public final void setSheet(final Sheet thatSheet) {
		// TODO Methode muss protected sein,
		// PhysicEngine braucht aber solang dort das TO-DO ist noch public.
		this.sheet = thatSheet;
	}

	/**
	 * Sets if sprite is standing still.
	 * 
	 * @param thatStanding
	 *            True if sprite is standing
	 */
	public final void setStanding(final boolean thatStanding) {
		this.standing = thatStanding;
	}

	/**
	 * Sets the current x-coord of the sprite.
	 * 
	 * @param thatX
	 *            X-coord to set
	 */
	public final void setX(final float thatX) {
		this.x = thatX;
	}

	/**
	 * Sets the current xa of the sprite.
	 * 
	 * @param thatXA
	 *            XA to set
	 */
	public final void setXA(final float thatXA) {
		this.xa = thatXA;
	}

	/**
	 * Sets the current y-coord of the sprite.
	 * 
	 * @param thatY
	 *            Y-coord to set
	 */
	public final void setY(final float thatY) {
		this.y = thatY;
	}

	/**
	 * Sets the current ya of the sprite.
	 * 
	 * @param thatYA
	 *            YA to set
	 */
	public final void setYA(final float thatYA) {
		this.ya = thatYA;
	}

	/**
	 * Triggered every Tick, logic of Sprite.
	 */
	public abstract void tick();

	/**
	 * Updates the current coordinates x and y by adding xa and ya. The
	 * {@link PhysicEngine} will modify the coordinates here.
	 */
	public final void updateCoords() {
		levelScene.getPhysicEngine().affect(this);
		// Update the coords by adding xa and xy
		setX(getX() + xa);
		setY(getY() + ya);
	}

	/**
	 * Lets the Sprite die, if the Sprite is dead it should be removed.
	 */
	protected final void die() {
		setVisible(false);
		dead = true;
	}

	//TODO Should be protected and not public, for testing purpose
	/**
	 * Gets the current Animation.
	 * 
	 * @return Current Animation
	 */
	public final Animation getAnim() {
		return anim;
	}

	/**
	 * Gets the current LevelScene.
	 * 
	 * @return Current LevelScene
	 */
	protected final LevelScene getLevelScene() {
		return levelScene;
	}

	/**
	 * Sets the x-coord of sprites {@link Animation}.
	 * 
	 * @param thatAnimX
	 *            x-coord of sprites {@link Animation}
	 */
	protected final void setAnimX(final float thatAnimX) {
		this.animX = thatAnimX;
	}

	/**
	 * Sets the y-coord of sprites {@link Animation}.
	 * 
	 * @param thatAnimY
	 *            y-coord of sprites {@link Animation}
	 */
	protected final void setAnimY(final float thatAnimY) {
		this.animY = thatAnimY;
	}

	/**
	 * Sets the dir of the sprite, -1 for left and 1 for right.
	 * 
	 * @param thatDir
	 *            Dir of the sprite, -1 for left and 1 for right
	 */
	protected final void setDir(final int thatDir) {
		if (thatDir < 0) {
			this.dir = -1;
		} else if (thatDir > 0) {
			this.dir = 1;
		} else {
			this.dir = thatDir;
		}
	}

	/**
	 * Sets the current Form of the sprite.
	 * 
	 * @param thatForm
	 *            Form to set
	 */
	protected final void setForm(final Form thatForm) {
		this.form = thatForm;
	}

	/**
	 * Sets the current LevelScene.
	 * 
	 * @param thatLevelScene
	 *            Levelscene to set
	 */
	protected final void setLevelScene(final LevelScene thatLevelScene) {
		this.levelScene = thatLevelScene;
	}

	/**
	 * Sets the visiblity of the Sprite, true for visible. Mostly used by Sprite
	 * creation and the method die().
	 * 
	 * @param thatVisible
	 *            Visiblity of the Sprite, true for visible
	 */
	protected final void setVisible(final boolean thatVisible) {
		this.visible = thatVisible;
	}

	/**
	 * Sets the current old x-coord of the sprite.
	 * 
	 * @param thatXOld
	 *            Old x-coord to set
	 */
	protected final void setXOld(final float thatXOld) {
		this.xOld = thatXOld;
	}

	/**
	 * Sets the current old y-coord of the sprite.
	 * 
	 * @param thatYOld
	 *            Old y-coord to set
	 */
	protected final void setYOld(final float thatYOld) {
		this.yOld = thatYOld;
	}

	/**
	 * Gets sprites width.
	 * @return The width
	 */
	@Override
	public int getWidth() {
		return width;
	}

	/**
	 * Sets sprites width.
	 * @param thatWidth The width to set
	 */
	protected void setWidth(int thatWidth) {
		this.width = thatWidth;
	}
	
	/**
	 * Gets sprites height.
	 * @return The height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets sprites width.
	 * @param thatWidth The width to set
	 */
	protected void setHeight(int thatHeight) {
		this.height = thatHeight;
	}
	
	@Override
	public Image getImage() {
		return getAnim().getImage();
	}
	
	@Override
	public int getImageWidth() {
		return getImage().getWidth(null);
	}
}
