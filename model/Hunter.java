package model;

/**
 * Represents the hunter in the Hunt the Wumpus
 * game. Only contains an x position and a y
 * position.
 * @author Terron Ishihara
 * @author Zachary Van Uum
 * @author Kegan Schaub
 *
 */
public class Hunter {

	private int xPos;
	private int yPos;
	
	public Hunter(int x, int y) {
		xPos = x;
		yPos = y;
	}

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

}