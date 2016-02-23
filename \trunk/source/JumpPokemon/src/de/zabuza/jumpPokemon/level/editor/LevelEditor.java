package de.zabuza.jumpPokemon.level.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import de.zabuza.jumpPokemon.Art;
import de.zabuza.jumpPokemon.level.Level;

/**
 * Level editor of the game. Load and saves with the game compatible levels.
 * 
 * @author Zabuza
 * 
 */
public class LevelEditor extends JFrame implements ActionListener {
	/**
	 * VersionsUID.
	 */
	private static final long serialVersionUID = 7461321112832160393L;

	// TODO Such constants are too often used, should be
	// exported to a static utility method, maybe to Commons.
	/**
	 * Hexadezimal for FF.
	 */
	private static final int HEX_FF = 0xFF;

	/**
	 * Maximal size for level names.
	 */
	private static final int LEVEL_NAME_SIZE = 10;

	/**
	 * Amount of all different tile data.
	 */
	private static final int TILE_DATA_AMOUNT = 8;

	/**
	 * Maximal index of a byte.
	 */
	private static final int MAX_BYTE_INDEX = 255;

	/**
	 * Starts the level editor in a single window.
	 * 
	 * @param args
	 *            Arguments are not supported
	 */
	public static void main(final String[] args) {
		new LevelEditor().setVisible(true);
	}

	/**
	 * Level load button.
	 */
	private JButton loadButton;
	/**
	 * Level save button.
	 */
	private JButton saveButton;
	/**
	 * Field for levels filename.
	 */
	private JTextField nameField;
	/**
	 * Component in which the level can be viewed and edited.
	 */
	private LevelEditView levelEditView;

	/**
	 * Component with that level tiles and data can be elected.
	 */
	private TilePicker tilePicker;

	/**
	 * Container for the tile data checkboxes.
	 */
	private JCheckBox[] bitmapCheckboxes = new JCheckBox[TILE_DATA_AMOUNT];

	/**
	 * Creates a new LevelEditor. To start creat a new and set its visibility
	 * true.
	 */
	public LevelEditor() {
		super("Map Edit");
		Art.init(getGraphicsConfiguration(), null);

		try {
			//TODO Filepath sollte iwo untergebracht sein.
			Level.loadBehaviors(new DataInputStream(new FileInputStream(
					"res/tiles.dat")));
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.toString(),
					"Failed to load tile behaviors", JOptionPane.ERROR_MESSAGE);
		}

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width, screenSize.height);
		setLocation((screenSize.width - getWidth()) / 2,
				(screenSize.height - getHeight()) / 2);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		tilePicker = new TilePicker();
		JPanel tilePickerPanel = new JPanel(new BorderLayout());
		tilePickerPanel.add(BorderLayout.WEST, tilePicker);
		tilePickerPanel.add(BorderLayout.CENTER, buildBitmapPanel());
		tilePickerPanel.setBorder(new TitledBorder(new EtchedBorder(),
				"Tile picker"));

		JPanel midPanel = new JPanel(new BorderLayout());
		midPanel.add(BorderLayout.EAST, tilePickerPanel);
		levelEditView = new LevelEditView(tilePicker);
		midPanel.add(BorderLayout.CENTER, new JScrollPane(levelEditView));

		JPanel borderPanel = new JPanel(new BorderLayout());
		borderPanel.add(BorderLayout.CENTER, midPanel);
		borderPanel.add(BorderLayout.NORTH, buildButtonPanel());
		setContentPane(borderPanel);

		tilePicker.addTilePickChangedListener(this);
	}

	@Override
	public final void actionPerformed(final ActionEvent e) {
		try {
			if (e.getSource() == loadButton) {
				//TODO Sollte mit Fileload gui gemacht sein
				levelEditView.setLevel(Level.load(new DataInputStream(
						new FileInputStream(nameField.getText().trim()))));
			}
			if (e.getSource() == saveButton) {
				//TODO Soltle mit Filesave gui gemacht sein
				levelEditView.getLevel().save(
						new DataOutputStream(new FileOutputStream(nameField
								.getText().trim())));
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.toString(),
					"Failed to load/save", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Builds the panel for the tilepicker and its checkboxes.
	 * 
	 * @return Builded panel
	 */
	public final JPanel buildBitmapPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
		for (int i = 0; i < bitmapCheckboxes.length; i++) {
			bitmapCheckboxes[i] = new JCheckBox(Level.BIT_DESCRIPTIONS[i]);
			panel.add(bitmapCheckboxes[i]);
			if (Level.BIT_DESCRIPTIONS[i].startsWith("- ")) {
				bitmapCheckboxes[i].setEnabled(false);
			}

			final int id = i;
			bitmapCheckboxes[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent ae) {
					int bm = Level.getTileBehaviors(tilePicker.getPickedTile()
							& HEX_FF)
							& HEX_FF;
					bm &= MAX_BYTE_INDEX - (1 << id);
					if (bitmapCheckboxes[id].isSelected()) {
						bm |= (1 << id);
					}
					Level.setTileBehaviors(tilePicker.getPickedTile() & HEX_FF,
							(byte) bm);

					try {
						Level.saveBehaviors(new DataOutputStream(
								new FileOutputStream("res/tiles.dat")));
					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(LevelEditor.this,
								e.toString(), "Failed to load tile behaviors",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}
		return panel;
	}

	/**
	 * Builds the panel which contains the level load and save buttons.
	 * 
	 * @return Builded panel
	 */
	public final JPanel buildButtonPanel() {
		loadButton = new JButton("Load");
		saveButton = new JButton("Save");
		nameField = new JTextField("test.lvl", LEVEL_NAME_SIZE);
		loadButton.addActionListener(this);
		saveButton.addActionListener(this);
		JPanel panel = new JPanel();
		panel.add(nameField);
		panel.add(loadButton);
		panel.add(saveButton);
		return panel;
	}

	/**
	 * Sets the current picked tile.
	 * 
	 * @param pickedTile
	 *            Data of the current picked tile
	 */
	public final void setPickedTile(final byte pickedTile) {
		int bm = Level.getTileBehaviors(pickedTile & HEX_FF) & HEX_FF;

		for (int i = 0; i < bitmapCheckboxes.length; i++) {
			bitmapCheckboxes[i].setSelected((bm & (1 << i)) > 0);
		}
	}
}