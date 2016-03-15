import java.util.Map;

/**
 * An interface for a n*n board game. In an MVC-program the model would be a
 * class implementing this inteface and the view and controller would only refer
 * to it through this interface.
 * 
 * @author holger
 *
 */
public interface GameBoardInterface {
	/**
	 * Make a "move", changing the games internal state if the move was legal.
	 * Returns true if the move was legal or false otherwise.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean move(int x, int y);

	/**
	 * Returns the current "message", for example "Player 1s turn" or
	 * "Illegal move" etc.
	 * 
	 * @return
	 */
	public String currentMessage();

	/**
	 * Returns a representation of the boards state, what "pieces" are at each
	 * location etc. Modifying this matrix will result in undefined behavior.
	 * 
	 * @return
	 */
	public int[][] state();

	/**
	 * Returns a mapping from the integer id's in the state matrix to names of
	 * image files. For example "1 means black queen" etc. The caller is free to
	 * modify this map.
	 * 
	 * @return
	 */
	public Map<Integer, String> getStateIDToImageNameMap();

	/**
	 * Returns the length of a side, eg the n in an n*n board.
	 * 
	 * @return
	 */
	public int getLengthOfSide();

	/**
	 * Returns the title of the game. For example "Chess".
	 * 
	 * @return
	 */
	public String getTitle();

	/**
	 * Reset the game. Makes the state functionally equivalent to how it was
	 * when the class was initialized. The implementing class decides what
	 * constitutes functionally equivalent.
	 */
	public void reset();

	/**
	 * Returns true if the game is "over". The implementing class decides what
	 * constitutes a game over.
	 * 
	 * @return
	 */
	public boolean isGameOver();

}
