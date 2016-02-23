package de.zabuza.jumpPokemon.level.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import de.zabuza.jumpPokemon.Commons;
import de.zabuza.jumpPokemon.level.Level;
import de.zabuza.jumpPokemon.level.LevelRenderer;

/**
 * LevelEditView class. Displays a editable level.
 * 
 * @author Zabuza
 * 
 */
public class LevelEditView extends JComponent implements MouseListener,
		MouseMotionListener {
	/**
	 * VersionsUID.
	 */
	private static final long serialVersionUID = -7696446733303717142L;

	// TODO Make width and height editable in the editor.
	/**
	 * Width of the level in tiles.
	 */
	private static final short LEVEL_WIDTH = 256;
	/**
	 * Height of the level in tiles.
	 */
	private static final short LEVEL_HEIGHT = 20;
	/**
	 * Color of the level background as int.
	 */
	private static final int BACKGROUND_COLOR = 0x8090ff;
	/**
	 * Key code for a right mouse click.
	 */
	private static final int MOUSE_RIGHT = 3;
	/**
	 * Amount of tiles to draw.
	 */
	private static final int TILE_AMOUNT = 3;

	/**
	 * LevelRenderer which renders the level of the view.
	 */
	private LevelRenderer levelRenderer;
	/**
	 * Level to render and edit.
	 */
	private Level level;

	/**
	 * X-coord of current selected tile.
	 */
	private int xTile = -1;
	/**
	 * Y-coord of current selected tile.
	 */
	private int yTile = -1;
	/**
	 * TilePicker component in which tiles and their data can be elected.
	 */
	private TilePicker tilePicker;

	/**
	 * Creates a new LevelEditView with a custom tilePicker.
	 * 
	 * @param thatTilePicker
	 *            TilePicker for the view
	 */
	public LevelEditView(final TilePicker thatTilePicker) {
		this.tilePicker = thatTilePicker;
		level = new Level(LEVEL_WIDTH, LEVEL_HEIGHT);
		Dimension size = new Dimension(level.getWidth() * Commons.TILE_SIZE,
				level.getHeight() * Commons.TILE_SIZE);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);

		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public final void addNotify() {
		super.addNotify();
		levelRenderer = new LevelRenderer(level, getGraphicsConfiguration(),
				level.getWidth() * Commons.TILE_SIZE, level.getHeight()
						* Commons.TILE_SIZE);
		levelRenderer.setRenderBehaviors(true);
	}

	/**
	 * Gets the current used level.
	 * 
	 * @return Current used level
	 */
	public final Level getLevel() {
		return level;
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
	}

	@Override
	public final void mouseDragged(final MouseEvent e) {
		xTile = e.getX() / Commons.TILE_SIZE;
		yTile = e.getY() / Commons.TILE_SIZE;

		level.setBlock(xTile, yTile, tilePicker.getPickedTile());
		levelRenderer.repaint(xTile - 1, yTile - 1, TILE_AMOUNT, TILE_AMOUNT);

		repaint();
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
	}

	@Override
	public final void mouseExited(final MouseEvent e) {
		xTile = -1;
		yTile = -1;
		repaint();
	}

	@Override
	public final void mouseMoved(final MouseEvent e) {
		xTile = e.getX() / Commons.TILE_SIZE;
		yTile = e.getY() / Commons.TILE_SIZE;
		repaint();
	}

	@Override
	public final void mousePressed(final MouseEvent e) {
		xTile = e.getX() / Commons.TILE_SIZE;
		yTile = e.getY() / Commons.TILE_SIZE;

		if (e.getButton() == MOUSE_RIGHT) {
			tilePicker.setPickedTile(level.getBlock(xTile, yTile));
		} else {
			level.setBlock(xTile, yTile, tilePicker.getPickedTile());
			levelRenderer.repaint(xTile - 1, yTile - 1, TILE_AMOUNT,
					TILE_AMOUNT);

			repaint();
		}
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
	}

	@Override
	public final void paintComponent(final Graphics g) {
		g.setColor(new Color(BACKGROUND_COLOR));
		g.fillRect(0, 0, level.getWidth() * Commons.TILE_SIZE,
				level.getHeight() * Commons.TILE_SIZE);
		levelRenderer.render(g, 0);
		g.setColor(Color.BLACK);
		g.drawRect(xTile * Commons.TILE_SIZE - 1,
				yTile * Commons.TILE_SIZE - 2, Commons.TILE_SIZE + 2,
				Commons.TILE_SIZE + 2);
	}

	/**
	 * Sets the level to view and edit.
	 * 
	 * @param thatLevel
	 *            Level to set
	 */
	public final void setLevel(final Level thatLevel) {
		this.level = thatLevel;
		Dimension size = new Dimension(
				thatLevel.getWidth() * Commons.TILE_SIZE, thatLevel.getHeight()
						* Commons.TILE_SIZE);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		repaint();
		levelRenderer.setLevel(thatLevel);
	}
}