package de.zabuza.jumpPokemon.level.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import de.zabuza.jumpPokemon.Art;
import de.zabuza.jumpPokemon.Commons;

/**
 * TilePicker class. Used to elect tiles and their data for level editing.
 * 
 * @author Zabuza
 * 
 */
public class TilePicker extends JComponent implements MouseListener,
		MouseMotionListener {

	/**
	 * VersionsUID.
	 */
	private static final long serialVersionUID = -7696446733303717142L;

	/**
	 * Color of the component background as int.
	 */
	private static final int BACKGROUND_COLOR = 0x8090ff;
	/**
	 * Hexadezimal for FF.
	 */
	private static final int HEX_FF = 0xFF;

	/**
	 * X-coord of the current selected tile.
	 */
	private int xTile = -1;
	/**
	 * Y-coord of the current selected tile.
	 */
	private int yTile = -1;

	/**
	 * Current picked tile.
	 */
	private byte pickedTile;
	/**
	 * Size of all tiles together from the ressource.
	 */
	private Dimension size;

	/**
	 * LevelEditor which listenes to all changes.
	 */
	private LevelEditor tilePickChangedListener;

	/**
	 * Creates a new TilePicker.
	 */
	public TilePicker() {
		size = new Dimension(Art.level[0][0].getWidth(null)
				* Art.level[0].length, Art.level[0][0].getHeight(null)
				* Art.level.length);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);

		addMouseListener(this);
		addMouseMotionListener(this);
	}

	/**
	 * Adds a LevelEditor as listener to all changes.
	 * 
	 * @param editor
	 *            Listener to add
	 */
	public final void addTilePickChangedListener(final LevelEditor editor) {
		this.tilePickChangedListener = editor;
		if (tilePickChangedListener != null) {
			tilePickChangedListener.setPickedTile(pickedTile);
		}
	}

	/**
	 * Gets the current picked tile.
	 * 
	 * @return Current picked tile
	 */
	public final byte getPickedTile() {
		return pickedTile;
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
	}

	@Override
	public final void mouseDragged(final MouseEvent e) {
		xTile = e.getX() / Commons.TILE_SIZE;
		yTile = e.getY() / Commons.TILE_SIZE;

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

		setPickedTile((byte) (xTile + yTile * Art.level.length));
		repaint();
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
	}

	@Override
	public final void paintComponent(final Graphics g) {
		g.setColor(new Color(BACKGROUND_COLOR));
		g.fillRect(0, 0, size.width, size.height);

		for (int y = 0; y < Art.level.length; y++) {
			for (int x = 0; x < Art.level[0].length; x++) {
				g.drawImage(Art.level[y][x], (x * Commons.TILE_SIZE),
						(y * Commons.TILE_SIZE), null);
			}
		}

		g.setColor(Color.WHITE);
		int xPickedTile = (pickedTile & HEX_FF) % Art.level.length;
		int yPickedTile = (pickedTile & HEX_FF) / Art.level[0].length;
		g.drawRect(xPickedTile * Commons.TILE_SIZE, yPickedTile
				* Commons.TILE_SIZE, Commons.TILE_SIZE - 2,
				Commons.TILE_SIZE - 2);

		g.setColor(Color.BLACK);
		g.drawRect(xTile * Commons.TILE_SIZE - 2,
				yTile * Commons.TILE_SIZE - 2, Commons.TILE_SIZE + 2,
				Commons.TILE_SIZE + 2);
	}

	/**
	 * Sets the current picked tile.
	 * 
	 * @param block
	 *            Tile to set
	 */
	public final void setPickedTile(final byte block) {
		pickedTile = block;
		repaint();
		if (tilePickChangedListener != null) {
			tilePickChangedListener.setPickedTile(pickedTile);
		}
	}
}