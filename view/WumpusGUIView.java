package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import model.Room;
import model.WumpusModel;
import model.Hunter;

/**
 * A view for the WumpusModel class that uses
 * a GUI to play the game.
 * @author Terron Ishihara
 * @author Zachary Van Uum
 * @author Kegan Schaub
 *
 */
public class WumpusGUIView extends JFrame implements Observer {

	private static final long serialVersionUID = -4507072315623269042L;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem newGame;
	private JMenuItem exit;
	private JTextArea display;
	private JTextArea help;
	private JTabbedPane gamePane;
	private JPanel textDisplay;
	private WumpusImagePanel imagePanel;
	
	private ArrowKeyListener akl;
	private ShootArrowListener sal;
	
	private WumpusModel game;
	
	public static void main(String[] args) {
		JFrame view = new WumpusGUIView();
		view.setVisible(true);
	}
	
	public WumpusGUIView() {
		layoutModel();
	}
	
	private void layoutModel() {
		setResizable(false);
		setSize(700,700);
		setLocationRelativeTo(null);
		setTitle("Hunt the Wumpus");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		game = new WumpusModel();
		
		menuBar = new JMenuBar();
		menu = new JMenu("File");
		newGame = new JMenuItem("New Game");
		exit = new JMenuItem("Exit");
		menu.add(newGame);
		menu.add(exit);
		menuBar.add(menu);
		this.add(menuBar,BorderLayout.NORTH);
		
		display = new JTextArea();
		display.setEditable(false);
		display.setFont(new Font("Courier", Font.PLAIN, 20));
		display.setText("Welcome to Hunt the Wumpus!");
		display.setLineWrap(true);
		display.setSize(500,500);
		
		gamePane = new JTabbedPane();
		this.add(gamePane);
		
		textDisplay = new JPanel();
		textDisplay.setBackground(Color.WHITE);
		textDisplay.add(display);

		imagePanel = new WumpusImagePanel(game);
		imagePanel.setBackground(Color.BLACK);
		
		gamePane.addTab("Text", textDisplay);
		gamePane.addTab("Images", imagePanel);
		
		addObservers();
		
		help = new JTextArea();
		help.setEditable(false);
		help.setText("Icons:                     Controls:\n"	+
					 "Hunter == O                Use arrow keys to move\n" + 
					 "Slime == S                 Press A to fire arrow \n" +
					 "Blood == B                                       \n" +
					 "Goop == G                                        \n");
		help.setFont(new Font("Courier", Font.PLAIN, 14));
		this.add(help,BorderLayout.SOUTH);

		registerListeners();
		addObservers();
	}
	
	private void registerListeners() {
		newGame.addActionListener(new NewGameListener());
		exit.addActionListener(new ExitGameListener());
		akl = new ArrowKeyListener();
		display.addKeyListener(akl);
		imagePanel.addKeyListener(akl);
	}
	
	private void addObservers(){
		game.addObserver(this);
		game.addObserver(imagePanel);
	}
	
	private class NewGameListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			help.setText("Icons:                     Controls:\n"	+
					     "Hunter == O                Use arrow keys to move\n" + 
					     "Slime == S                 Press A to fire arrow \n" +
					     "Blood == B                                       \n" +
					     "Goop == G                                        \n");
			
			game = new WumpusModel();

			gamePane.removeTabAt(0);
			gamePane.removeTabAt(0);
			
			textDisplay = new JPanel();
			textDisplay.setBackground(Color.WHITE);
			display.setText(game.toString());
			textDisplay.add(display);
			imagePanel = new WumpusImagePanel(game);
			
			gamePane.addTab("Text", textDisplay);
			gamePane.addTab("Images", imagePanel);
			
			imagePanel.addKeyListener(akl);
			
			addObservers();
		}
	}
	
	private class ExitGameListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			System.exit(0);
		}
	}
	
	private class ArrowKeyListener implements KeyListener {
		
		@Override
		public void keyPressed(KeyEvent ke) {
			if (!game.isGameOver()) {
				Hunter hunter = game.getHunter();
				int x = hunter.getxPos();
				int y = hunter.getyPos();

				if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
					game.move(x, y - 1);
				} else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
					game.move(x, y + 1);
				} else if (ke.getKeyCode() == KeyEvent.VK_UP) {
					game.move(x - 1, y);
				} else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
					game.move(x + 1, y);
				} else if (ke.getKeyCode() == KeyEvent.VK_A) {
					display.removeKeyListener(akl);
					imagePanel.removeKeyListener(akl);
					sal = new ShootArrowListener();
					display.addKeyListener(sal);
					imagePanel.addKeyListener(sal);
					help.setText("Icons:                     Controls:\n"	+
						         "Hunter == O                Press the arrow key corresponding \n" + 
						         "Slime == S                 to the direction you would like to\n" +
						         "Blood == B                 fire the arrow                    \n" +
						         "Goop == G                                                    \n");
				} else {
					// Do nothing
				}
			}
		}

		@Override // Don't need
		public void keyReleased(KeyEvent arg0) {
		}

		@Override // Don't need
		public void keyTyped(KeyEvent arg0) {
		}
		
	}
	
	private class ShootArrowListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent ke) {
			if (!game.isGameOver() && !game.isGameWon()) {
				if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
					game.shootArrow("LEFT");
					display.removeKeyListener(sal);
					display.addKeyListener(akl);
				} else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
					game.shootArrow("RIGHT");
					display.removeKeyListener(sal);
					display.addKeyListener(akl);
				} else if (ke.getKeyCode() == KeyEvent.VK_UP) {
					game.shootArrow("UP");
					display.removeKeyListener(sal);
					display.addKeyListener(akl);
				} else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
					game.shootArrow("DOWN");
					display.removeKeyListener(sal);
					display.addKeyListener(akl);
				} else {
					// Do nothing
				}
			}
		}

		@Override // Don't need
		public void keyReleased(KeyEvent arg0) {
		}

		@Override // Don't need
		public void keyTyped(KeyEvent arg0) {
		}
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(game.isGameWon()){
			appendText();
			display.setText(game.toString());
		}
		else if(game.isGameOver()){
			appendText();
			display.setText(game.toString());
		}
		else if(!game.isGameOver() && !game.isGameWon()){
			appendText();
			display.setText(game.toString());
		}
	}
	
	// Updates the help textArea that gives instructions and 
	// descriptions of the current room that the hunter is in.
	private void appendText(){
		int x = game.getHunter().getxPos();
		int y = game.getHunter().getyPos();
		
		if(game.isGameWon()){
			help.setText("You killed the wumpus!\n\n\n\n");
		} else {
			// The last line of text changes for each of the last cases.
			Room[][] board = game.getBoard();
			if (board[x][y].isWumpus())
				help.setText("You angered the wumpus and died.\n\n\n\n");
			else if (board[x][y].isPit())
				help.setText("You fell to your death in a pit. Nicely done.\n\n\n\n");
			else if (board[x][y].isGoop())
				help.setText("Icons:                     Controls:\n"
						+ "Hunter == O                Use arrow keys to move\n"
						+ "Slime == S                 Press A to fire arrow \n"
						+ "Blood == B                                       \n"
						+ "Goop == G                                        \n"
						+ "You are in a room full of goop.");
			else if (board[x][y].isBlood())
				help.setText("Icons:                     Controls:\n"
						+ "Hunter == O                Use arrow keys to move\n"
						+ "Slime == S                 Press A to fire arrow \n"
						+ "Blood == B                                       \n"
						+ "Goop == G                                        \n"
						+ "You are in a room full of blood.");
			else if (board[x][y].isSlime())
				help.setText("Icons:                     Controls:\n"
						+ "Hunter == O                Use arrow keys to move\n"
						+ "Slime == S                 Press A to fire arrow \n"
						+ "Blood == B                                       \n"
						+ "Goop == G                                        \n"
						+ "You are in a room full of slime.");
			else
				help.setText("Icons:                     Controls:\n"
						+ "Hunter == O                Use arrow keys to move\n"
						+ "Slime == S                 Press A to fire arrow \n"
						+ "Blood == B                                       \n"
						+ "Goop == G                                        \n"
						+ "There is nothing particularly interesting about this room.");
		}
	}

}