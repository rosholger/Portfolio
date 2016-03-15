import java.util.HashMap;
import java.util.Map;

/**
 * The game Othello, implements GameBoardInterface.
 */

public class OthelloBoard implements GameBoardInterface {
	private static final int sideLength = 8;
	private int currentPlayer;
	private int otherPlayer;
	private int[][] state;
	private int count;
	private int error;

	/**
	 * Returns sideLength.
	 */
	public int getLengthOfSide() {
		return sideLength;
	}

	/**
	 * Returns "Othello".
	 */
	public String getTitle() {
		return "Othello";
	}

	public OthelloBoard() {
		reset();
	}

	/**
	 * Resets the checkers and sets up its initial placements.
	 */
	public void reset() {
		currentPlayer = 1;
		otherPlayer = 2;
		state = new int[sideLength][sideLength];
		state[3][3] = 1;
		state[3][4] = 2;
		state[4][3] = 2;
		state[4][4] = 1;
		count = 4; // four of the squares are already filled.
	}

	public boolean isGameOver() {
		return count == sideLength * sideLength;
	}

	/**
	 * Flips all in between checkers if there are any to flip. Scans based on
	 * xdelta, ydelta direction. If no valid move, returns false.
	 * 
	 * @param xstart
	 * @param ystart
	 * @param xdelta
	 * @param ydelta
	 * @param player
	 * @return
	 */
	private boolean change(int xstart, int ystart, int xdelta, int ydelta, int player) {
		int x = xstart;
		int y = ystart;
		do {
			x += xdelta;
			y += ydelta;
			if (x < 0) {
				return false;
			}
			if (y < 0) {
				return false;
			}
			if (x >= state.length) {
				return false;
			}
			if (y >= state[x].length) {
				return false;
			}
			if (state[x][y] == 0) {
				return false;
			}

		} while (state[x][y] != player);
		int xend = x;
		int yend = y;
		x = xstart;
		y = ystart;
		if (Math.abs(xend - xstart) <= 1 && Math.abs(yend - ystart) <= 1) { // No
																			// change,
																			// ie
																			// not
																			// valid
			return false;
		}
		do {
			state[x][y] = player;
			x += xdelta;
			y += ydelta;
		} while (x != xend || y != yend);
		return true;
	}

	/**
	 * If x, y is empty and available, call change with each directional compass
	 * coordinate. If a change was made, switches players.
	 */
	public boolean move(int x, int y) {
		if (state[x][y] == 0) {
			boolean valid = false;
			valid = change(x, y, 1, 0, currentPlayer) ? true : valid;
			valid = change(x, y, 1, 1, currentPlayer) ? true : valid;
			valid = change(x, y, 0, 1, currentPlayer) ? true : valid;
			valid = change(x, y, -1, 1, currentPlayer) ? true : valid;
			valid = change(x, y, -1, 0, currentPlayer) ? true : valid;
			valid = change(x, y, -1, -1, currentPlayer) ? true : valid;
			valid = change(x, y, 0, -1, currentPlayer) ? true : valid;
			valid = change(x, y, 1, -1, currentPlayer) ? true : valid;
			if (!valid) {
				error = 1;
				return false;
			}
			// int tx = x - 1;
			// while (tx >= 0 && state[tx][y] != currentPlayer) {
			// state[tx][y] = currentPlayer;
			// tx--;
			// }
			// tx = x + 1;
			// while (tx < state.length && state[tx][y] != currentPlayer) {
			// state[tx][y] = currentPlayer;
			// tx++;
			// }
			// int ty = y - 1;
			// while (ty >= 0 && state[x][ty] != currentPlayer) {
			// state[x][ty] = currentPlayer;
			// ty--;
			// }
			// ty = y + 1;
			// while (ty < state[x].length && state[x][ty] != currentPlayer) {
			// state[x][ty] = currentPlayer;
			// ty++;
			// }
			// Switch players
			int t = currentPlayer;
			currentPlayer = otherPlayer;
			otherPlayer = t;
			count++;
			return true;
		} else {
			error = 1;
			return false;
		}
	}

	/**
	 * Handles all communication between the game and the user. Handles
	 * finishing messages and current player.
	 */
	public String currentMessage() {
		if (count == sideLength * sideLength) {
			int player1Count = 0;
			int player2Count = 0;
			for (int x = 0; x < state.length; ++x) {
				for (int y = 0; y < state[x].length; ++y) {
					if (state[x][y] == 1) {
						player1Count++;
					} else {
						player2Count++;
					}
				}
			}
			if (player1Count > player2Count) {
				return "Player 1 wins!";
			} else if (player2Count > player1Count) {
				return "Player 2 wins!";
			} else {
				return "Its a draw!";
			}

		}
		if (error == 1) {
			error = 0;
			return "Invalid move: There needs to be a line of black/white between your piece & your opponents.";
		} else {
			return "Player " + currentPlayer + "s turn";
		}
	}

	/**
	 * Returns the state matrix.
	 */
	public int[][] state() {
		return state;
	}

	/**
	 * Maps this games icons, all two of them.
	 */
	public Map<Integer, String> getStateIDToImageNameMap() {
		HashMap<Integer, String> mapping = new HashMap<>();
		mapping.put(1, "whiteChecker.png");
		mapping.put(2, "blackChecker.png");
		return mapping;
	}

	/**
	 * Create a GameBoard and initialize it with a SlidingPuzzel.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new GameBoard(new OthelloBoard());
	}
}