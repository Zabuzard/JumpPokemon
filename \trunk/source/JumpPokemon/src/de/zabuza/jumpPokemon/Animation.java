package de.zabuza.jumpPokemon;

import java.awt.Image;

/**
 * Used for animations. Calculates the right image out of an Image[] and its
 * hitbox.
 * 
 * @author Zabuza
 * 
 */
public class Animation {

	/**
	 * Constant for infinity loops.
	 */
	public static final int INFINITY_LOOPS = 0;

	/**
	 * Array which contains all images of the animation.
	 */
	private Image[] images;
	/**
	 * Current index of the animation in the Image[].
	 */
	private int curX;
	/**
	 * Current amount of loops.
	 */
	private int curLoops;
	/**
	 * Amount of loops which animation will do after it is finished.
	 */
	private int loops;
	/**
	 * Starting index of the animation.
	 */
	private int start;
	/**
	 * Ending index of the animation.
	 */
	private int end;
	/**
	 * Current amount of ticks of the animation.
	 */
	private int tick;
	/**
	 * Interval in ticks after which the displayed image will change.
	 */
	private int intervall;
	/**
	 * True if the animation is finished.
	 */
	private boolean finished;

	/**
	 * Creates an infinity animation over the whole Image[].
	 * 
	 * @param sheet
	 *            Sheet which contains the Image[] for usage
	 * @param row
	 *            Row of the Image[] for usage in the sheet
	 * @param thatIntervall
	 *            Interval between the images of the Image[] in ticks
	 */
	public Animation(final Sheet sheet, final int row, final int thatIntervall) {
		this(sheet, row, thatIntervall, INFINITY_LOOPS, 0, sheet
				.getImageRow(row).length - 1);
	}

	/**
	 * Creates an animation with custom loops over the whole Image[].
	 * 
	 * @param sheet
	 *            Sheet which contains the Image[] for usage
	 * @param row
	 *            Row of the Image[] for usage in the sheet
	 * @param thatIntervall
	 *            Interval between the images of the Image[] in ticks
	 * @param thatLoops
	 *            Loops over the Image[], 0 for infinity
	 */
	public Animation(final Sheet sheet, final int row, final int thatIntervall,
			final int thatLoops) {
		this(sheet, row, thatIntervall, thatLoops, 0,
				sheet.getImageRow(row).length - 1);
	}

	/**
	 * Creates an animation with custom loops from start to the ending index of
	 * the Image[].
	 * 
	 * @param sheet
	 *            Sheet which contains the Image[] for usage
	 * @param row
	 *            Row of the Image[] for usage in the sheet
	 * @param thatIntervall
	 *            Interval between the images of the Image[] in ticks
	 * @param thatLoops
	 *            Loops over the Image[], 0 for infinity
	 * @param thatStart
	 *            Starting index of the Image[] for the animation
	 */
	public Animation(final Sheet sheet, final int row, final int thatIntervall,
			final int thatLoops, final int thatStart) {
		this(sheet, row, thatIntervall, thatLoops, thatStart, sheet
				.getImageRow(row).length - 1);
	}

	/**
	 * Creates an animation with custom loops from start to end index of the
	 * Image[].
	 * 
	 * @param sheet
	 *            Sheet which contains the Image[] for usage
	 * @param row
	 *            Row of the Image[] for usage in the sheet
	 * @param thatIntervall
	 *            Interval between the images of the Image[] in ticks
	 * @param thatLoops
	 *            Loops over the Image[], 0 for infinity
	 * @param thatStart
	 *            Starting index of the Image[] for the animation
	 * @param thatEnd
	 *            Ending index of the Image[] for the animation
	 */
	public Animation(final Sheet sheet, final int row, final int thatIntervall,
			final int thatLoops, final int thatStart, final int thatEnd) {
		finished = false;
		images = sheet.getImageRow(row);
		this.intervall = thatIntervall;
		setLoops(thatLoops);
		setStart(thatStart);
		setEnd(thatEnd);
		curX = this.start;
	}

	/**
	 * Animates the animation. Calculates the current image and its hitbox of
	 * the animation.
	 */
	public final void animate() {
		// Only change if not finished yet
		if (!finished) {
			tick++;
			if (tick % intervall == 0) {
				if (curX >= end) {
					// Only calculate curLoops if not infinity
					// (prevents potential overflows)
					if (loops != INFINITY_LOOPS) {
						curLoops++;
					}
					// Only finish if not infinity and curLoops
					// greater than loops
					if (loops != INFINITY_LOOPS && curLoops >= loops) {
						finished = true;
					} else {
						curX = start;
					}
				} else {
					curX++;
				}
			}
		}
	}

	/**
	 * Gets the height of the current image.
	 * 
	 * @return Height of the current image
	 */
	public final int getHeight() {
		return images[curX].getHeight(null);
	}

	/**
	 * Gets the current image of the animation.
	 * 
	 * @return Current image of the animation
	 */
	public final Image getImage() {
		return images[curX];
	}

	/**
	 * Gets the width of the current image.
	 * 
	 * @return Width of the current image
	 */
	public final int getWidth() {
		return images[curX].getWidth(null);
	}

	/**
	 * Returns if the animation is finished.
	 * 
	 * @return True if animation is finished
	 */
	public final boolean isFinished() {
		return finished;
	}

	/**
	 * Sets the ending index of the Image[].
	 * 
	 * @param thatEnd
	 *            Ending index of the Image[]
	 */
	private void setEnd(final int thatEnd) {
		if (thatEnd < 0) {
			this.end = 0;
		} else if (thatEnd >= images.length) {
			this.end = images.length;
		} else if (thatEnd < start) {
			this.end = start;
		} else {
			this.end = thatEnd;
		}
	}

	/**
	 * Sets the loops of the animation till its finished. 0 for infinity loops
	 * 
	 * @param thatLoops
	 *            Loops of the animation
	 */
	private void setLoops(final int thatLoops) {
		this.loops = Math.abs(thatLoops);
	}

	/**
	 * Sets the starting index of the Image[].
	 * 
	 * @param thatStart
	 *            Starting index of the Image[]
	 */
	private void setStart(final int thatStart) {
		if (thatStart < 0) {
			this.start = 0;
		} else if (thatStart >= images.length) {
			this.start = images.length;
		} else {
			this.start = thatStart;
		}
	}
}
