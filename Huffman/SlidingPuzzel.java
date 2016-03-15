import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

/**
 * Creates the game SlidingPuzzel, dependent on GameBoardInterface.
 */
public class SlidingPuzzel implements GameBoardInterface {

	private int sideLength;
	private int[][] state;
	private int zerox;
	private int zeroy;
	private int score = 0;
	private int error;

	/**
	 * Constructor, sets the state for the game as sideLength*sideLength.
	 * 
	 * @param sideLength
	 */
	public SlidingPuzzel(int sideLength) {
		this.sideLength = sideLength;
		state = new int[sideLength][sideLength];
		reset();
	}

	/**
	 * Returns sideLength
	 */
	public int getLengthOfSide() {
		return sideLength;
	}

	/**
	 * Returns "Sliding Puzzel"
	 */
	public String getTitle() {
		return "Sliding Puzzel";
	}

	/**
	 * Resets the board, and sets up the initial placement of each checker, in a
	 * random order.
	 */
	public void reset() {
		score = 0;
		LinkedList<Integer> choices = new LinkedList<>();
		int i = 0;
		for (int x = 0; x < state.length; ++x) {
			for (int y = 0; y < state[x].length; ++y) {
				choices.add(i);
				i++;
			}
		}
		Random rnd = new Random();
		for (int x = 0; x < state.length; ++x) {
			for (int y = 0; y < state[x].length; ++y) {
				int chosenIndex = rnd.nextInt(choices.size());
				state[x][y] = choices.get(chosenIndex);
				if (state[x][y] == 0) {
					zerox = x;
					zeroy = y;
				}
				choices.remove(chosenIndex);
			}
		}
	}

	/**
	 * Determines if the game is over.
	 */
	public boolean isGameOver() {
		for (int x = 0; x < state.length; ++x) {
			for (int y = 0; y < state[x].length; ++y) {
				if (x == state.length - 1 && y == state[x].length - 1) {
					return true;
				}
				if (state[x][y] != (x + 1) + (y * state.length)) {
					// System.out.println(x + " " + y + " should be " +
					// ((x+1)+(y*state.length)));
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Moves everything from x,y in a straight line one step towards the empty
	 * tile, if x,y is in a vertical or horisontal line to the empty tile.
	 */
	public boolean move(int x, int y) {
		if (state[x][y] == 0) {
			return false;
		}

		// (x,y) is diagonal to (zerox,zeroy), not allowed!
		if (zerox != x && zeroy != y) {
			error = 1;
			return false;
		}

		score -= Math.abs(zerox - x) + Math.abs(zeroy - y);

		int dx = (int) Math.signum(x - zerox);
		int dy = (int) Math.signum(y - zeroy);
		int currx = zerox;
		int curry = zeroy; // HAHA curry
		while (currx != x || curry != y) {
			state[currx][curry] = state[currx + dx][curry + dy];
			currx += dx;
			curry += dy;
		}
		state[x][y] = 0;
		zerox = x;
		zeroy = y;
		return true;
	}

	/**
	 * Returns the current message.
	 */
	public String currentMessage() {
		if (isGameOver()) {
			return "You won! Your score is " + score;
		} else if (error == 1) {
			error = 0;
			return "You can only move tiles that are horizontal or vertieval to the empty tile";
		}
		return "Pick a tile to move, score = " + score;
	}

	/**
	 * Returns the state matrix.
	 */
	public int[][] state() {
		return state;
	}

	/**
	 * Returns a mapping from state id's to image file paths.
	 */
	public Map<Integer, String> getStateIDToImageNameMap() {
		HashMap<Integer, String> mapping = new HashMap<>();
		mapping.put(0, "slidingTileEmpty.png");
		for (int i = 1; i <= sideLength * sideLength - 1; ++i) {
			mapping.put(i, "slidingTile" + i + ".png");
		}
		return mapping;
	}

	/**
	 * Create a GameBoard and initialize it with a SlidingPuzzel.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new GameBoard(new SlidingPuzzel(4));
	}
}