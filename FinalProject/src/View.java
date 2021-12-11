import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Color;
/**
 * This present the View for freecell game
 * @author Yiyi Wang
 *
 */
public class View extends JFrame {

	private IModel model;//it takes model as field, skip the controller 
	private ArrayList<Card> source;//the source for moving card or cards
	private ArrayList<Card> destination;//the destination for moving
	private ArrayList<Card> selectedCards;//the list of cards to be moved
	
 	public static final int WIN_WIDTH = 850;
	public static final int WIN_HEIGHT = 750;
	public static final int CARD_WIDTH = 60;
	public static final int CARD_HEIGHT = 80;
	public static final int X_INTERVAL1 = 30;
	public static final int X_INTERVAL2 = 490;
	public static final int X_INTERVAL3 = 80;
	public static final int Y_INTERVAL1 = 30;
	public static final int Y_INTERVAL2 = 180;
	public static final int Y_INTERVAL3 = CARD_HEIGHT/4;
	
	public View(IModel model1) {
		
		this.model = model1;
		source = new ArrayList<Card>();
		destination = new ArrayList<Card>();
		selectedCards = new ArrayList<Card>();
		
		//set the window
		setTitle("Freecell");
		setSize(WIN_WIDTH, WIN_HEIGHT);
		setLocation(600, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		//Adding Screen panel to show the piles in freecell game;
		Color c = new Color(0,102,0);
		JPanel screen = new PileView(model);
		screen.setLayout(new BorderLayout());
		screen.setBounds(0, 20, 850, 750);
		screen.setBackground(c);
		this.getContentPane().add(screen);
		
		//Adding newGame Button;
		JButton newGame = new JButton("New Game");
		newGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				model.newGame();//start the game when the button is clicked
				repaint();
			}
		});
		this.getContentPane().add(newGame);
		newGame.setBounds(0, 0, 100, 20);
		
		//Adding Restart Button;
		JButton restart = new JButton("Restart");
		restart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				model.restart();//restart the game when the button is clicked
				repaint();
			}
		});
		this.getContentPane().add(restart);
		restart.setBounds(100, 0, 100, 20);
		
		//Adding Undo Button;
		JButton undo = new JButton("Undo");
		undo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(model.undoable()) {//firstly check if it is undoable
					model.undo();
					repaint();
				}else {
					JOptionPane.showMessageDialog(screen,"Cannot Undo\n");//if cannot undo, pop out a window to tell the player
				}
			}
		});
		this.getContentPane().add(undo);
		undo.setBounds(200, 0, 100, 20);
		
		//show the frame and panels;
		display();
		
		//Adding MouseListener to screen panel
		screen.addMouseListener(new MouseAdapter() {
			
			/**
			 * This method will determine the following two things
			 * 1. if the player want to move one card or multiple cards
			 * 2. where the player want to move from (determine the source)
			 * @param e: the mouse event (pressed)
			 */
			@Override
			public void mousePressed(MouseEvent e) {
				if(model.isStart()){
					int x = e.getX();
					int y = e.getY();
					int pileNum = 0;//to represent the number of piles
					int cardNum = 0;//to represent the index of cards in piles
					
					//Analyze the x, y coordinate
					//only allow pickup from freecells or cascades 
					if(y < Y_INTERVAL2) {//the mouse is at the freecell area or foundation area 
						if(x>= X_INTERVAL2) {//the mouse is at freecell area
							for(pileNum = 0; pileNum < 4; pileNum++) {//check each freecell to see if the x is in it
								int top_left_x = X_INTERVAL2+ pileNum * (X_INTERVAL1 + CARD_WIDTH);
								if(x >= top_left_x && x <= top_left_x + CARD_WIDTH) {//if the x is within the freecell area, point the source to the freecell
									source = model.getFreecells().get(pileNum);//freecell only have one card so do not need to change the value of selectedCards
									break;//do not need to check other freecells
								}
							}
						}else {//the mouse is at the foundation area
							return;//do nothing because player should not move any card in foundations
						}
					}else {//the mouse is at cascades area
						if(x >= X_INTERVAL3 && x <= (X_INTERVAL3 + CARD_WIDTH * 8 + X_INTERVAL1 * 7)) {
							for(pileNum = 0; pileNum < 8; pileNum++) {//check each cascade to see if the x is in it
								int top_left_x = X_INTERVAL3+ pileNum * (X_INTERVAL1 + CARD_WIDTH);
								if(x >= top_left_x && x <= top_left_x + CARD_WIDTH) {//this will determine which cascades
									//now need to determine which card is picking under the cascades by checking y coordinate
									//this will determine if y is in the area from the first card to the card before last card
									for(cardNum = 0 ; cardNum < model.getCascades().get(pileNum).size()-1; cardNum++) {
										int top_left_y = Y_INTERVAL2+cardNum*Y_INTERVAL3;
										if(y>=top_left_y && y <top_left_y+Y_INTERVAL3) {//if the y is located to a card
												//add the cards from the picked up card to the last card in cascades to selectedCard list
												for(int i = cardNum; i < model.getCascades().get(pileNum).size(); i++) {
													selectedCards.add(model.getCascades().get(pileNum).get(i));
												}
													source = model.getCascades().get(pileNum);
													break;
											
											}
										}
										//if y is not located to the card from index 0 to the card before last one
										//try to see if y could be located to the last card in the cascades
										int top_left_y = Y_INTERVAL2+(model.getCascades().get(pileNum).size()-1)*Y_INTERVAL3;
										if(y >=top_left_y && y <top_left_y+CARD_HEIGHT) {//this means the y is at the last card of the cascades
											source = model.getCascades().get(pileNum);
											break;
										}
									}
								}
						}
					}			
				}
			}
			
			/**
			 * This method will determine the destination of move
			 * @param e: the mouse event (released)
			 */
			public void mouseReleased(MouseEvent e) {
				if(source == null || source.isEmpty()) {
					return;
				}
				if(model.isStart()) {
					int x = e.getX();
					int y = e.getY();
					int pileNum = 0;//to represent the number of piles
					//Analyze the x, y coordinate
					//only allow pickup from freecells or cascades 
					if(y < Y_INTERVAL2) {//the mouse is at the freecell area or foundation area 
						if(x>= X_INTERVAL2) {//the mouse is at freecell area
							for(pileNum = 0; pileNum < 4; pileNum++) {
								int top_left_x = X_INTERVAL2+ pileNum * (X_INTERVAL1 + CARD_WIDTH);
								if(x >= top_left_x && x <= top_left_x + CARD_WIDTH) {
									destination = model.getFreecells().get(pileNum);
									model.move(source, destination);//call the move method (only one card is moved)
									break;
								}
							}
						}else {//the mouse is at the foundation are, we should not put card there
							return;
						}
					}else {//the mouse is at cascades area
						if(x >= X_INTERVAL3 && x <= (X_INTERVAL3 + CARD_WIDTH * 8 + X_INTERVAL1 * 7)) {
							for(pileNum = 0; pileNum < 8; pileNum++) {
								int top_left_x = X_INTERVAL3+ pileNum * (X_INTERVAL1 + CARD_WIDTH);
								if(x >= top_left_x && x <= top_left_x + CARD_WIDTH) {
									destination = model.getCascades().get(pileNum);
									if(selectedCards.isEmpty()) {//this means we are picking up only one card
										model.move(source, destination);//call the move method
									}else {
										model.multipleMove(selectedCards, source, destination);//call the multipleMove method
									}
									break;
								}
						}
					}
				}
				//need to point the source and destination to null after one move
				source = null;
				destination = null;
				selectedCards.clear();//and clear the selectedCard
				screen.repaint();
				}
				//check if the game is over
				//if it is over, pop a window to tell player
				if(model.isGameOver()) {
					JOptionPane.showMessageDialog(screen,"You Win!\n");
				}
			}

		});
	}

	/**
	 * This method will display the panels and frame
	 */
	public void display() {
		setVisible(true);
	}
	
}
