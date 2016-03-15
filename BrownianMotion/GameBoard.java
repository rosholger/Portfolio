
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Game board consisting of a JButton grid width*height. The board implements
 * the GameBoardInterface.
 */
public class GameBoard implements ActionListener {

	private int width;
	private int height;
	private Color bgColor = new Color(240, 240, 240);
	private GameBoardInterface game;

	private JFrame frame = new JFrame();
	private JPanel boardPanel = new JPanel();
	private JPanel mainPanel = new JPanel();
	private JPanel headerPanel = new JPanel();
	private JLabel text = new JLabel();
	private JButton restartButton = new JButton("Restart?");
	static JButton[][] grid;
	public Timer timer;

	HashMap<Integer, ImageIcon> iconMap = new HashMap<>();

	/**
	 * Constructor sets up all the swing utilities, the frame & creates the
	 * coordinate button grid.
	 * 
	 * @param game
	 */
	public GameBoard(GameBoardInterface game) {
		this.game = game;
		this.width = game.getLengthOfSide();
		this.height = game.getLengthOfSide();
		Map<Integer, String> idToFilenameMap = game.getStateIDToImageNameMap();
		for (Map.Entry<Integer, String> e : idToFilenameMap.entrySet()) {
			iconMap.put(e.getKey(), createImageIcon(e.getValue()));
		}

		text.setText(game.currentMessage());

		frame.setTitle(game.getTitle());

		boardPanel.setLayout(new GridLayout(width, height));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setPreferredSize(new Dimension(800, 600));
		boardPanel.setPreferredSize(new Dimension(800, 500));
		restartButton.setLayout(new BorderLayout());
		restartButton.setMaximumSize(new Dimension(4, 6));
		restartButton.setVisible(true);

		headerPanel.setPreferredSize(new Dimension(80, 60));
		headerPanel.add(text, BorderLayout.WEST);
		headerPanel.add(restartButton, BorderLayout.WEST);
		// panel3.setLayout(new GridLayout(0,3));

		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(boardPanel, BorderLayout.CENTER);
		mainPanel.setPreferredSize(new Dimension(800, 500));

		grid = new JButton[this.width][this.height];

		createGameBoard(game.state());

		frame.add(mainPanel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		restartButton.addActionListener(new AbstractAction("restartButton") {
			public void actionPerformed(ActionEvent e) {

				resetBoard();

				// frame.add(panel2);
				frame.pack();
				frame.setVisible(true);

			}
		});
	}

	/**
	 * Custom JBtton class adds getThisX, getThisY.
	 * 
	 * @author fredrik
	 *
	 */

	class GridButton extends JButton {

		private int gridx;
		private int gridy;

		public GridButton(String label, int gridx, int gridy) {
			super(label);
			this.gridx = gridx;
			this.gridy = gridy;
		}

		public int getThisX() {
			return gridx;
		}

		public int getThisY() {
			return gridy;
		}
	}

	/**
	 * Resets gameBoard to its initial state.
	 */
	public void resetBoard() {

		boardPanel.removeAll();
		game.reset();
		text.setText(game.currentMessage());
		createGameBoard(game.state());
		// boardPanel.repaint();
		// mainPanel.repaint();
		// panel3.repaint();
		frame.repaint();

	}

	/**
	 * Sets up the coordinate grid width*height.
	 * 
	 * @param placement
	 */
	public void createGameBoard(int[][] placement) {

		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {

				grid[j][i] = new GridButton("", j, i);
				grid[j][i].setBackground(bgColor);
				grid[j][i].setName(j + "," + i);
				grid[j][i].setOpaque(true);
				grid[j][i].addActionListener(this);
				grid[j][i].setSelected(false);
				grid[j][i].setBorder(BorderFactory.createLineBorder(Color.black));
				grid[j][i].setFocusPainted(false);
				grid[j][i].setSelected(true);
				grid[j][i].setName(placement[j][i] + "");
				grid[j][i].setIcon(iconMap.get(new Integer(placement[j][i])));

				boardPanel.add(grid[j][i]);
			}
		}

	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/**
	 * Handles the actionEvents for each JButton. On trigger, relates
	 * coordinates to game.move().
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		int x = ((GridButton) e.getSource()).getThisX();
		int y = ((GridButton) e.getSource()).getThisY();

		if (!game.move(x, y)) {
			int delay = 6000;
			ActionListener taskPerformer = new ActionListener() {
				public void actionPerformed(ActionEvent evt) {

					text.setText(game.currentMessage());
					timer.stop();
				}
			};
			timer = new Timer(delay, taskPerformer);
			timer.start();

		} else {

			text.setText(game.currentMessage());
			int[][] state = game.state();
			for (int i = 0; i < state.length; ++i) {
				for (int j = 0; j < state[i].length; ++j) {
					grid[i][j].setIcon(iconMap.get(new Integer(state[i][j])));
				}
			}
		}

		text.setText(game.currentMessage());
	}

}
