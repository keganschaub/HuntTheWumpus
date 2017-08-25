package model;

/**
 * Represents a room in the Hunt the Wumpus game.
 * Only contains boolean values to determine
 * what is or is not inside the room. Provides
 * a toString to represent a simple room.
 * @author Terron Ishihara
 * @author Zachary Van Uum
 * @author Kegan Schaub
 *
 */
public class Room {

	private boolean wumpus;
	private boolean pit;
	private boolean slime;
	private boolean blood;
	private boolean goop;
	private boolean hunter;
	private boolean visited;
	
	public Room() {
		wumpus = false;
		pit = false;
		slime = false;
		blood = false;
		goop = false;
		hunter = false;
		visited = false;
	}
	
	/**
	 * Returns a String representation of
	 * a single Room. This toString is used
	 * in the toString for the WumpusModel
	 */
	@Override
	public String toString() {
		if (isVisited() == true) {
			if (isHunter() == true) {
				return "[O]";
			}
			if (isWumpus() == true)
				return "[W]";
			if (isPit() == true)
				return "[P]";
			if (isGoop() == true)
				return "[G]";
			if (isSlime() == true)
				return "[S]";
			if (isBlood() == true)
				return "[B]";
			return "[ ]";
		} 
		return "[X]";
	}

	public boolean isWumpus() {
		return wumpus;
	}

	public void setWumpus(boolean wumpus) {
		this.wumpus = wumpus;
	}

	public boolean isPit() {
		return pit;
	}

	public void setPit(boolean pit) {
		this.pit = pit;
	}

	public boolean isSlime() {
		return slime;
	}

	public void setSlime(boolean slime) {
		this.slime = slime;
	}

	public boolean isBlood() {
		return blood;
	}

	public void setBlood(boolean blood) {
		this.blood = blood;
	}

	public boolean isGoop() {
		return goop;
	}

	public void setGoop(boolean goop) {
		this.goop = goop;
	}

	public boolean isHunter() {
		return hunter;
	}

	public void setHunter(boolean hunter) {
		this.hunter = hunter;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

}