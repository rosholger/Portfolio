import java.awt.Color;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;



public class GameModel{
	private static Object grid;
	private String gameType;
	private int width;
	private int height;
	private String player = "1"; // Starts
	private String opponent = "2";
	int[][] grids;
	private JButton[][] gameBoardGrid;
	private GameBoard gameBoard;
	ImageIcon icon1;
	ImageIcon icon2;
	private ImageIcon icon;
	GameResult results;
	private int counter;

	public GameModel(String gameType, int width, int height){
		this.gameType = gameType;
		this.width = width;
		this.height = height;
		this.grids = new int[width][height];
		
		setInitialCheckers(grids);
		setIcons();
		gameBoard = new GameBoard(this.width, this.height, this);

		
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
	void logicGameOne(){
		
	}
	public ImageIcon getIcon1(){
		return icon1;
	}
	public ImageIcon getIcon2(){
		return icon2;
	}
	public Color getColor(){
		if(gameType.equals("Othello")){Color color = new Color(0, 120,0);return color;}
		else{Color color = new Color(0, 105,148);return color;}
	}
	void setIcons(){
		if(gameType.equals("Othello")){this.icon1 = createImageIcon("whiteChecker.png"); this.icon2 = createImageIcon("blackChecker.png");}
		if(gameType.equals("andra")){this.icon1 = createImageIcon("lol.png"); this.icon1 = createImageIcon("lol22.png");}
	}
	
	void setInitialCheckers(int[][] grid){
		if(gameType.equals("Othello")){

			grid[(grid.length/2)-1][(grid.length/2)-1] = 1;
			grid[(grid.length/2)][(grid.length/2)-1] = 2;
			grid[(grid.length/2)-1][(grid.length/2)] = 2;
			grid[(grid.length/2)][(grid.length/2)] = 1;	
			
			results = new GameResult(2,2);
		}
		///else{);}
	}
	void move(JButton[][] g, int x, int y){
		if(gameType.equals("Othello")){
			moveAllGameOne(g, x, y);
		}
	}
	void getIconOfCell(int x, int y){
		gameBoard.getGrid()[x][y].getIcon();
	}
	
	class CoordPair
	{
	    private int x;
	    private int y;

	    public CoordPair(int xCoord, int yCoord)
	    {
	        x  = xCoord;
	        y = yCoord;
	    }


	}
	
	void updateScore(String player, int cntr){
		if(player.equals("1")){
			results.first  += cntr;
			results.second -= (cntr-1);
		}
		else if(player.equals("2")){
			results.second += cntr;
			results.first -= (cntr-1);
		}
	}
	
	class GameResult {
	    private int first;
	    private int second;

	    public GameResult(int first, int second) {
	        this.first = first;
	        this.second = second;
	    }
	    public int getFirst(){
	    	return first;
	    }
	    public int getSecond(){
	    	return second;
	    }
		public void setFirst(int i) {
			this.first = i;
			
		}
		public void setSecond(int i){
			this.second = i;
		}
	}
	
	void moveAllGameOne(JButton[][] g,int x, int y){

		int newX = 0, newY = 0;
		int tempx = 0;
		int tempy = 0;
		counter = 0;
		player = (gameBoard.getTurn() == "1") ? "1" : "2";
		opponent = (player == "1" ? "2" : "1");
		icon = (player == "1" ? icon1 : icon2);
			
			gameBoard.getGrid()[x][y].setSelected(true);
			gameBoard.getGrid()[x][y].setName(player);
			counter++;
			gameBoard.getGrid()[x][y].setIcon(icon);
			
			
			ArrayList<CoordPair> compass = new ArrayList<CoordPair>();
			ArrayList<CoordPair> flipThese = new ArrayList<CoordPair>();
			ArrayList<CoordPair> hmm = new ArrayList<CoordPair>();
			// TODO : ...
			compass.add(new CoordPair(0,1));
			compass.add(new CoordPair(1,1));
			compass.add(new CoordPair(1,0));
			compass.add(new CoordPair(1,-1));
			compass.add(new CoordPair(0,-1));
			compass.add(new CoordPair(-1,-1));
			compass.add(new CoordPair(-1,0));
			compass.add(new CoordPair(-1,1));

			
			for(int j = 0; j < compass.size(); j++){

				newX = x + compass.get(j).x; 
				newY = y + compass.get(j).y;
				if(newX < this.width && newY < this.height && newX >= 0 && newY >= 0 && gameBoard.getGrid()[newX][newY].getName().equals(opponent)){

					while(gameBoard.getGrid()[newX][newY].getName().equals(opponent) && (newX < this.width && newY < this.height && newX >= 0 && newY >= 0)){
						tempx = newX;
						tempy = newY;
						newX += compass.get(j).x;
						newY += compass.get(j).y;
						if(newX < 0 || newY < 0  || newX >= this.width || newY >= this.height){newX = tempx; newY = tempy;break;}
						else{hmm.add(new CoordPair(newX, newY));}
						
					}
					if(gameBoard.getGrid()[newX][newY].getName().equals(player) && (newX < this.width && newY < this.height && newX >= 0 && newY >= 0)){
						while(true){
							newX -= compass.get(j).x;
							newY -= compass.get(j).y;
							if(newX == x && newY == y || (newX < 0 || newY < 0  || newX >= this.width || newY >= this.height)){break;}
							else{flipThese.add(new CoordPair(newX, newY));}	
						}
					}
				}
			}
			
			for(int k = 0; k < flipThese.size(); k++){
				counter++;
				gameBoard.getGrid()[flipThese.get(k).x][flipThese.get(k).y].setSelected(true);
				gameBoard.getGrid()[flipThese.get(k).x][flipThese.get(k).y].setName(player);
				gameBoard.getGrid()[flipThese.get(k).x][flipThese.get(k).y].setIcon(icon);
			}
			
			
			gameBoard.setTurn();
			updateScore(player, counter);
			
	}
	
	public boolean isLegalMove(int x, int y){
		if(this.gameType.equals("Othello")){

			if(!gameBoard.getGrid()[x][y].isSelected())	{
				return true;
			}else{return false;}	
		}
		else if(gameType.equals("det andra")){
			return false;
		}
		else{return false;}
	}
	void moveAllGameTwo(ActionEvent e){
		
	}
	
	public static void main(String[] args){
		new GameModel("Othello",8,8);
	}

}
