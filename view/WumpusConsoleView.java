package view;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import model.WumpusModel;
import model.Hunter;

/**
 * A view for the WumpusModel class that prints
 * the board and instructions to the console.
 * User interacts with the view by typing commands
 * while the view passes the commands to the model.
 * Implements {@link Observer} and updates accordingly
 * to the {@link Observable} WumpusModel.
 * @author Terron Ishihara
 * @author Zachary Van Uum
 * @author Kegan Schaub
 */
public class WumpusConsoleView implements Observer {
	
	private static WumpusModel game;
	private static WumpusConsoleView view;
	
	public static void main(String[] args) {
		view = new WumpusConsoleView();
		game.addObserver(view);
		
		Scanner scan = new Scanner(System.in);

		// Must print the initial board since nothing
		// will have changed in the model upon instantiation.
		System.out.println(game);
		
		// Continues to ask for and execute commands until game
		// is over or game is won.
		while (!game.isGameOver() && !game.isGameWon()) {
			System.out.print("Type \"right\", \"left\", \"up\", or \"down\" to move,\n"
							+ "type \"new\" to start a new game,\n"
							+ "type \"arrow\" to fire the arrow: ");
			String tempInput = scan.next();
			String input = tempInput.toLowerCase();
			char command = input.charAt(0);
			
			// Continue to prompt user until valid command is entered
			while (command != 'r' && command != 'l' && command != 'u'
					&& command != 'd' && command != 'a' && command != 'n') {
				System.out
						.print("\nInvalid command.\n"
								+ "Type \"right\", \"left\", \"up\", or \"down\" to move\n"
								+ "type \"new\" to start a new game,\n"
								+ "or type \"arrow\" to fire the arrow: ");
				tempInput = scan.next();
				input = tempInput.toLowerCase();
				command = input.charAt(0);
			}

			Hunter hunter = game.getHunter();
			int x = hunter.getxPos();
			int y = hunter.getyPos();
			
			// Process command
			switch (command) {
			case 'l':
				game.move(x, y - 1);
				break;
			case 'r':
				game.move(x, y + 1);
				break;
			case 'u':
				game.move(x - 1, y);
				break;
			case 'd':
				game.move(x + 1, y);
				break;
			case 'a':
				System.out
						.print("\nType \"right\", \"left\", \"up\", or \"down\" to select direction: ");
				String tempArrow = scan.next();
				String arrow = tempArrow.toLowerCase();
				char arrowCommand = arrow.charAt(0);
				
				// Continue to prompt user until valid command is entered
				while ( arrowCommand != 'r' && arrowCommand != 'l'
						&& arrowCommand != 'd' && arrowCommand != 'u') {
					System.out
						.print("\nInvalid command.\n"
								+ "Type \"right\", \"left\", \"up\", or \"down\" to select direction: ");
					tempArrow = scan.next();
					arrow = tempArrow.toLowerCase();
					arrowCommand = arrow.charAt(0);
				}

				switch (arrowCommand) {
				case 'l':
					game.shootArrow("LEFT");
					break;
				case 'r':
					game.shootArrow("RIGHT");
					break;
				case 'u':
					game.shootArrow("UP");
					break;
				case 'd':
					game.shootArrow("DOWN");
					break;
				default:
					System.out.println("Error evaluating arrow command");
				}
				break;
			case 'n':
				game = new WumpusModel();
				System.out.println(game);
				break;
			default:
				System.out.println("Error evaluating command");
			}
		}

	}

	public WumpusConsoleView() {
		game = new WumpusModel(); // WumpusModel can take seed (mostly for testing)
	}
	
	// Updates the view each time model notifies view
	// of a change by printing the toString from WumpusModel
	public void update(Observable arg0, Object arg1) {
		System.out.println();
		System.out.println(game);
	}

}