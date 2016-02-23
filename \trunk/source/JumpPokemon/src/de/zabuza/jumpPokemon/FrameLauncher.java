package de.zabuza.jumpPokemon;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * Jump Pokemon ! Launches the game in a single window
 * 
 * @author Zabuza
 * 
 */
public final class FrameLauncher {

	/**
	 * Width of the border which appears in the L&F of no resizable
	 * {@link JFrame}s.
	 */
	private static final int BORDER_WIDTH = 10;

	/**
	 * Starts the game in a {@link JFrame}.
	 * 
	 * @param args
	 *            No arguments are provided
	 */
	public static void main(final String[] args) {
		JFrame frame = new JFrame("Jump Pokemon!");
		JumpPkmnComponent runPkmn = new JumpPkmnComponent(Commons.WIDTH,
				Commons.HEIGHT);
		frame.getContentPane().add(runPkmn);
		frame.pack();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Remove "no resizable" - border of L&F
		frame.setSize(frame.getWidth() - BORDER_WIDTH, frame.getHeight()
				- BORDER_WIDTH);
		// Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((screenSize.width - frame.getWidth()) / 2,
				(screenSize.height - frame.getHeight()) / 2);

		frame.setVisible(true);

		// Start the gamecomponent
		runPkmn.start();
	}

	/**
	 * Utility class, has no effect.
	 */
	private FrameLauncher() {

	}
}