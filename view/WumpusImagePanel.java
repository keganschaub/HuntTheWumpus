package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.Room;
import model.WumpusModel;

/**
 * A class that extends JPanel and updates according
 * to {@link WumpusModel}. Acts at the image display
 * in {@link WumpusGUIView}.
 * @author Terron Ishihara
 * @author Zachary Van Uum
 * @author Kegan Schaub
 */
public class WumpusImagePanel extends JPanel implements Observer {

	private static final long serialVersionUID = -5736682259078871316L;
	private BufferedImage blood;
	private BufferedImage goop;
	private BufferedImage ground;
	private BufferedImage slime;
	private BufferedImage slimePit;
	private BufferedImage hunter;
	private BufferedImage wumpus;
	
	private WumpusModel game;
	
	public WumpusImagePanel(WumpusModel model){
		setBackground(Color.BLACK);
		this.game = model;
		loadImages();

	}

	private void loadImages() {
		try {
			blood = ImageIO.read(new File("./WumpusImageSet/Blood.png"));
		} catch (IOException e) {
			System.out.println("Could not find 'Blood.png'");
		}
		try {
			goop = ImageIO.read(new File("./WumpusImageSet/Goop.png"));

		} catch (IOException e) {
			System.out.println("Could not find 'Goop.png'");
		}
		try {
			ground = ImageIO.read(new File("./WumpusImageSet/Ground.png"));
		} catch (IOException e) {
			System.out.println("Could not find 'Ground.png'");
		}
		try {
			slime = ImageIO.read(new File("./WumpusImageSet/Slime.png"));
		} catch (IOException e) {
			System.out.println("Could not find 'Slime.png'");
		}
		try {
			slimePit = ImageIO.read(new File("./WumpusImageSet/SlimePit.png"));
		} catch (IOException e) {
			System.out.println("Could not find 'SlimePit.png'");
		}
		try {
			hunter = ImageIO.read(new File("./WumpusImageSet/TheHunter.png"));
		} catch (IOException e) {
			System.out.println("Could not find 'Hunter.png'");
		}
		try {
			wumpus = ImageIO.read(new File("./WumpusImageSet/Wumpus.png"));
		} catch (IOException e) {
			System.out.println("Could not find 'Wumpus.png'");
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		
		Graphics2D gr = (Graphics2D) g;
		
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				
				// Multiply by 50 since each image is 50x50 pixels large
				int x=j*50;
				int y=i*50;
				
				Room[][] board = game.getBoard();
				
				if(board[i][j].isVisited()){
					
					gr.drawImage(ground, x, y, null);
					
					if(board[i][j].isGoop() && !board[i][j].isPit()
							&& !board[i][j].isWumpus()){
						gr.drawImage(goop, x, y, null);
					}
					else if(board[i][j].isBlood() &&
							!board[i][j].isGoop() && !board[i][j].isPit()
							&& !board[i][j].isWumpus()){
						gr.drawImage(blood, x, y, null);
					}
					else if(board[i][j].isSlime() &&
							!board[i][j].isGoop() && !board[i][j].isPit()){
						gr.drawImage(slime, x, y, null);
					}
					else if(board[i][j].isPit()){
						gr.drawImage(slimePit, x, y, null);
					}
					else if(board[i][j].isWumpus()){
						gr.drawImage(wumpus, x, y, null);
					}
				}
				if(board[i][j].isHunter()){
					gr.drawImage(hunter, x, y, null);
				}	
			}
		}
		
	}

	public void update(Observable arg0, Object arg1) {
		game = (WumpusModel) arg0;
		revalidate();
		repaint();
	}
	
}