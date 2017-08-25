package tests;

import static org.junit.Assert.*;

import model.*;

import org.junit.Test;

/**
 * JUnit Test cases for WumpusModel
 * @author Terron Ishihara
 * @author Zachary Van Uum
 * @author Kegan Schaub
 */
public class WumpusModelTest {

	@Test
	public void testGameWin() {
		WumpusModel game = new WumpusModel(718);
		
		int hunterX = game.getHunter().getxPos();
		int hunterY = game.getHunter().getyPos();
		
		int wumpusX = game.getWumpus()[0];
		int wumpusY = game.getWumpus()[1];
		
		assertEquals(hunterX,3);
		assertEquals(hunterY,4);
		assertEquals(wumpusX,1);
		assertEquals(wumpusY,1);
		//Move hunter to the same hallway as the Wumpus
		game.move(hunterX-1, hunterY);
		hunterX = game.getHunter().getxPos();
		assertEquals(hunterX, 2);
		
		game.move(hunterX-1, hunterY);
		hunterX = game.getHunter().getxPos();
		assertEquals(hunterX, 1);
				
		System.out.println("After moving, the hunter is in room " + hunterX + ", " + hunterY + ".");
		//If shootArrow() returns true, the Wumpus has been shot
		assertTrue(game.shootArrow("LEFT"));
		assertTrue(game.isGameWon());
	}
	
	@Test
	public void testGameLoseByMissing() {
		WumpusModel game = new WumpusModel(718);
		
		int hunterX = game.getHunter().getxPos();
		int hunterY = game.getHunter().getyPos();
		
		int wumpusX = game.getWumpus()[0];
		int wumpusY = game.getWumpus()[1];
		
		assertEquals(hunterX,3);
		assertEquals(hunterY,4);
		assertEquals(wumpusX,1);
		assertEquals(wumpusY,1);
		//Move hunter to the same hallway as the Wumpus
		game.move(hunterX-1, hunterY);
		hunterX = game.getHunter().getxPos();
		assertEquals(hunterX, 2);
		
		game.move(hunterX-1, hunterY);
		hunterX = game.getHunter().getxPos();
		assertEquals(hunterX, 1);
				
		System.out.println("After moving, the hunter is in room " + hunterX + ", " + hunterY + ".");
		//If shootArrow() returns true, the Wumpus has been shot
		assertFalse(game.shootArrow("RIGHT"));
		assertFalse(game.isGameWon());
	}
	
	@Test
	public void testGameLoseByMovingIntoWumpus() {
		WumpusModel game = new WumpusModel(718);
		
		int hunterX = game.getHunter().getxPos();
		int hunterY = game.getHunter().getyPos();
		
		int wumpusX = game.getWumpus()[0];
		int wumpusY = game.getWumpus()[1];
		
		assertEquals(hunterX,3);
		assertEquals(hunterY,4);
		assertEquals(wumpusX,1);
		assertEquals(wumpusY,1);
		//Move hunter to the same room as Wumpus
		game.move(hunterX-1, hunterY);
		hunterX = game.getHunter().getxPos();
		game.move(hunterX-1, hunterY);
		hunterX = game.getHunter().getxPos();
		game.move(hunterX, hunterY-1);
		hunterY = game.getHunter().getyPos();
		game.move(hunterX, hunterY-1);
		hunterY = game.getHunter().getyPos();
		game.move(hunterX, hunterY-1);
		hunterY = game.getHunter().getyPos();
		
		System.out.println("After moving, the hunter is in room " + hunterX + ", " + hunterY + ".");
		// Player moved into Wumpus room. Game should be over.
		assertTrue(game.isGameOver());
	}
	
	@Test
	public void testGameLoseByMovingIntoPit() {
		WumpusModel game = new WumpusModel(718);
		
		int hunterX = game.getHunter().getxPos();
		int hunterY = game.getHunter().getyPos();
		
		int pitX = game.getPit()[0];
		int pitY = game.getPit()[1];
		
		assertEquals(hunterX,3);
		assertEquals(hunterY,4);
		assertEquals(pitX,9);
		assertEquals(pitY,8);
		//Move hunter to the same room as Pit
		for (int i=1; i<=6; i++) {
			game.move(hunterX+1, hunterY);
			hunterX = game.getHunter().getxPos();
		}
		for (int i=1; i<=4; i++) {
			game.move(hunterX, hunterY+1);
			hunterY = game.getHunter().getyPos();
		}
		
		System.out.println("After moving, the hunter is in room " + hunterX + ", " + hunterY + ".");
		// Player moved into Pit. Game should be over.
		assertTrue(game.isGameOver());
	}

}