package model;

import java.util.Observable;
import java.util.Random;

/**
 * A model for the Hunt the Wumpus game. Provides all methods
 * needed for playing the game and executing actions the
 * user chooses. Extends {@link Observable} to coordinate
 * with implementations of a view of the game.
 * 
 * @author Terron Ishihara
 * @author Zachary Van Uum
 * @author Kegan Schaub
 * 
 */
public class WumpusModel extends Observable {

	private Hunter hunter;
	private Room[][] board;
	private Random gen;
	private boolean gameOver;
	private boolean gameWon;
	
	public WumpusModel() {
		gen = new Random();
		setUpBoard();
	}
	
	public WumpusModel(long seed) {
		gen = new Random( seed );
		setUpBoard();
	}
	
	private void setUpBoard() {
		board = new Room[10][10];
		
		// Fill board with new Rooms
		for (int i=0; i<board.length; i++) {
			for (int j=0; j<board[0].length; j++) {
				board[i][j] = new Room();
			}
		}
		
		gameOver = false;
		gameWon = false;
		
		setWumpus();
		setPit();
		setHunter();
	}
	
	private void setWumpus() {
		// Since Wumpus is the first thing set,
		// there's no need to check any existing
		// objects in the room
		int x = gen.nextInt(board.length);
		int y = gen.nextInt(board[0].length);
		
		board[x][y].setWumpus(true);
		setBlood(x,y);			// Surround Wumpus with blood
	}
	
	private void setBlood(int x, int y) {
		for (int i=-1; i<=1; i++) {
			for (int j=-1; j<=1; j++) {
				board[wrapAround( x+i,'x' )][wrapAround( y+j,'y')].setBlood(true);
			}
		}
	}
	
	private void setPit() {
		int x = gen.nextInt(board.length);
		int y = gen.nextInt(board[0].length);
		
		// Ensure that location to be set is not
		// already inhabited by another object
		while ( board[x][y].isHunter() == true ||
				board[x][y].isWumpus() == true ||
				board[x][y].isPit() == true ) {
			x = gen.nextInt(board.length);
			y = gen.nextInt(board[0].length);
		}
		
		board[x][y].setPit(true);
		setSlime(x,y);			// Surround Pit with slime
	}
	
	private void setSlime(int x, int y) {
		for (int i=-1; i<=1; i++) {
			for (int j=-1; j<=1; j++) {
				board[wrapAround( x+i,'x' )][wrapAround( y+j,'y')].setSlime(true);
				// Since blood is already set, check to see if
				// any spaces count as goop
				if (board[wrapAround( x+i,'x' )][wrapAround( y+j,'y')].isBlood() == true)
					board[wrapAround( x+i,'x' )][wrapAround( y+j,'y')].setGoop(true);
			}
		}
	}
	
	private void setHunter() {
		int x = gen.nextInt(board.length);
		int y = gen.nextInt(board[0].length);
		
		// Ensure that location to be set is not
		// already inhabited by another object
		while ( board[x][y].isBlood() == true ||
				board[x][y].isSlime() == true ||
				board[x][y].isWumpus() == true || 
				board[x][y].isPit() == true ) {
			x = gen.nextInt(board.length);
			y = gen.nextInt(board[0].length);
		}
		
		hunter = new Hunter(x,y);
		board[hunter.getxPos()][hunter.getyPos()].setHunter(true);
		board[hunter.getxPos()][hunter.getyPos()].setVisited(true);
			
	}
	
	// Takes in an index and returns its new, wrapped around equivalent
	// Allows for varying number of rows and columns
	private int wrapAround(int index, char xOrY) {
		if ( xOrY == 'x' ) {
			if ( index < 0 )
				return index + board.length;
			if ( index >= board.length )
				return index % board.length;
		}
		else {
			if ( index < 0 )
				return index + board[0].length;
			if ( index >= board[0].length )
				return index % board[0].length;
		}
		return index;
	}
	
	/**
	 * Reveals everything on the board. This is only called
	 * once the game is over or the game has been won.
	 */
	public void revealBoard() {
		for (int i=0; i<board.length; i++) {
			for (int j=0; j<board.length; j++) {
				board[i][j].setVisited(true);
			}
		}
	}
	
	/**
	 * Returns a String representation of the board
	 * as well as a description of the currently
	 * occupied room.
	 */
	public String toString() {
		String result = "";
		for (int i=0; i<board.length; i++) {
			for (int j=0; j<board.length; j++) {
				result = result + board[i][j].toString();
			}
			result = result + "\n";
		}
		int x = hunter.getxPos();
		int y = hunter.getyPos();
		
		if ( board[x][y].isWumpus() )
			result = result + "You have entered the wumpus' chamber.\n";
		else if ( board[x][y].isPit() )
			result = result + "You fell into a pit. Nicely done.\n";
		else if ( board[x][y].isGoop() )
			result = result + "You are in a room full of goop.\n";
		else if ( board[x][y].isBlood() )
			result = result + "You are in a room full of blood.\n";
		else if ( board[x][y].isSlime() )
			result = result + "You are in a room full of slime.\n";
		else 
			result = result + "There is nothing particularly interesting about this room.\n";

		return result;
	}
	
	/**
	 * Used to move the hunter around the board. Uses a
	 * private wrapAround helper method to account for
	 * wrap around. Ends the game if hunter moves into
	 * a wumpus room or a pit.
	 * @param x
	 * The x-coordinate to which the hunter should move.
	 * @param y
	 * The y-coordinate to which the hunter should move.
	 */
	public void move(int x, int y) {
		board[hunter.getxPos()][hunter.getyPos()].setHunter(false);
		
		hunter.setxPos(wrapAround(x,'x'));
		hunter.setyPos(wrapAround(y,'y'));
		
		if (board[hunter.getxPos()][hunter.getyPos()].isWumpus() == true ||
				board[hunter.getxPos()][hunter.getyPos()].isPit() == true)
			setGameOver(true);
		else {
			board[hunter.getxPos()][hunter.getyPos()].setHunter(true);
			board[hunter.getxPos()][hunter.getyPos()].setVisited(true);
			super.setChanged();
			super.notifyObservers();
		}
	}
	
	/**
	 * Shoots the arrow in the specified direction.
	 * The arrow itself is not animated, but the method
	 * determines if the player has won or lost upon
	 * choosing to shoot the arrow in the specified direction.
	 * @param direction
	 * A String representing the direction the arrow
	 * should be shot. "UP", "DOWN", "LEFT", "RIGHT"
	 * are the only directions available.
	 */
	public boolean shootArrow(String direction) {
		if (direction.equals("UP")) {
			for (int i = hunter.getxPos(); i >= 0; i--) {
				if (board[i][hunter.getyPos()].isWumpus()) {
					setGameWon(true);
					return true;
				}
			}
			setGameOver(true);
		}
		if (direction.equals("DOWN")) {
			for (int i = hunter.getxPos(); i<board.length; i++) {
				if (board[i][hunter.getyPos()].isWumpus()) {
					setGameWon(true);
					return true;
				}
			}
			setGameOver(true);
		}
		if (direction.equals("LEFT")) {
			for (int i = hunter.getyPos(); i>=0; i--) {
				if (board[hunter.getxPos()][i].isWumpus()) {
					setGameWon(true);
					return true;
				}
			}
			setGameOver(true);
		}
		if (direction.equals("RIGHT")) {
			for (int i = hunter.getyPos(); i<board[0].length; i++) {
				if (board[hunter.getxPos()][i].isWumpus()) {
					setGameWon(true);
					return true;
				}
			}
			setGameOver(true);
		}
		return false;
	}
	
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
		revealBoard();
		super.setChanged();
		super.notifyObservers();
		System.out.println("You dead, homie.");
	}
	
	public boolean isGameOver() {
		return gameOver;
	}
	
	public void setGameWon(boolean gameWon) {
		this.gameWon = gameWon;
		revealBoard();
		super.setChanged();
		super.notifyObservers();
		System.out.println("You killed the Wumpus. Good job, homie.");
	}
	
	public boolean isGameWon() {
		return gameWon;
	}

	public Hunter getHunter() {
		return hunter;
	}
	
	// For testing purposes
	public int[] getWumpus() {
		int[] coord = new int[2];
		for (int i=0; i<board.length; i++) {
			for (int j=0; j<board[0].length; j++) {
				if (board[i][j].isWumpus()) {
					coord[0] = i;
					coord[1] = j;
				}
			}
		}
		return coord;
	}
	
	// For testing purposes
	public int[] getPit() {
		int[] coord = new int[2];
		for (int i=0; i<board.length; i++) {
			for (int j=0; j<board[0].length; j++) {
				if (board[i][j].isPit()) {
					coord[0] = i;
					coord[1] = j;
				}
			}
		}
		return coord;
	}

	public Room[][] getBoard() {
		return board;
	}
	
}