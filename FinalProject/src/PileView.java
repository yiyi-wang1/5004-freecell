import java.awt.Component;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
/**
 * This will represent the View of all piles in the freecell game
 * including foundations, freecells and cascades
 * @author Yiyi Wang
 *
 */
public class PileView extends JPanel {
	private IModel model;
	public static final int CARD_WIDTH = 60;
	public static final int CARD_HEIGHT = 80;
	public static final int X_INTERVAL1 = 30;
	public static final int X_INTERVAL2 = 490;
	public static final int X_INTERVAL3 = 80;
	public static final int Y_INTERVAL1 = 30;
	public static final int Y_INTERVAL2 = 180;
	public static final int Y_INTERVAL3 = CARD_HEIGHT/4;
	
	/**
	 * Construct the PileView with model
	 * @param model
	 */
	public PileView (IModel model) {
		this.model = model;
	}
	
	/**
	 * Rewrite the paintComponent function so it will paint all piles when repaint is called
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//clear all components because the paintFoundations, paintFreecells and paintCascades will create new components
		Component[] list = this.getComponents();
		for(Component c : list) {
			if(c instanceof CardImage || c instanceof EmptyPile) {
				this.remove(c);
			}
		}
		paintFoundations(g,model.getFoundations());
		paintFreecells(g,model.getFreecells());
		paintCascades(g,model.getCascades());
		
	}
	
	/**
	 * Draw the foundations at screen
	 * @param g: the screen graphics
	 * @param foundations: the current state of foundations
	 */
	public void paintFoundations(Graphics g, ArrayList<ArrayList<Card>> foundations){
		//use for loop to draw all foundations
		for(int i = 0; i < 4; i++) {
			if(foundations.get(i).isEmpty()) {//if the foundation is empty
				EmptyPile f = new EmptyPile(X_INTERVAL1+i* (X_INTERVAL1 + CARD_WIDTH), Y_INTERVAL1, CARD_WIDTH, CARD_HEIGHT);
				this.add(f);//add it to this panel
				f.paintComponent(g);//paint it

			}else {
				String name = foundations.get(i).get(foundations.get(i).size()-1).toString()+".png";//foundation will show the last card in the list
				try {
					//create new CardImage by reading the card image
					CardImage f1 = new CardImage (X_INTERVAL1+i* (X_INTERVAL1 + CARD_WIDTH), Y_INTERVAL1, ImageIO.read(new File("cardimages/"+name)), CARD_WIDTH, CARD_HEIGHT);
					this.add(f1);
					f1.paintComponent(g);
				} catch (IOException e) {
					System.out.println("Unable to read the file:" + name);//if fail to read, print an error message
					e.printStackTrace();
				}
			}
		}

	}
	
	/**
	 * Draw the freecells at screen
	 * @param g: the screen graphics
	 * @param foundations: the current state of freecells
	 */
	public void paintFreecells(Graphics g, ArrayList<ArrayList<Card>> freecells) {
		//similar to the paintFoundations method
		for(int i = 0; i < 4; i++) {
			if(freecells.get(i).isEmpty()) {
				//the location (x coordinate and y coordinate) is different from paintFoundations()
				EmptyPile f = new EmptyPile(X_INTERVAL2+i* (X_INTERVAL1 + CARD_WIDTH), Y_INTERVAL1, CARD_WIDTH, CARD_HEIGHT);
				this.add(f);
				f.paintComponent(g);
			}else {
				String name = freecells.get(i).get(0).toString()+".png";//freecell always shows the first card in the list(also is the only card in the list)
				try {
					CardImage f1 = new CardImage (X_INTERVAL2+i* (X_INTERVAL1 + CARD_WIDTH),Y_INTERVAL1,ImageIO.read(new File("cardimages/"+name)), CARD_WIDTH,CARD_HEIGHT);
					this.add(f1);
					f1.paintComponent(g);
				} catch (IOException e) {
					System.out.println("Unable to read the file:" + name);
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Draw the cascades at screen
	 * @param g: the screen graphics
	 * @param foundations: the current state of cascades
	 */
	public void paintCascades(Graphics g, ArrayList<ArrayList<Card>> cascades) {
		//use two for loop to draw the cascades
		for(int i = 0; i < 8; i++) {
			if(cascades.get(i).isEmpty()) {//if the cascade is empty, do not need to draw the cards
				EmptyPile c = new EmptyPile(X_INTERVAL3+i* (X_INTERVAL1 + CARD_WIDTH), Y_INTERVAL2, CARD_WIDTH, CARD_HEIGHT);
				this.add(c);
				c.paintComponent(g);				
			}else {//if the cascade is not empty, need another for loop to draw all cards belonged to it
				for(int j = 0; j < cascades.get(i).size(); j++) {
					Card c = cascades.get(i).get(j);
					String name = c.toString()+".png";
					try {
						//the x and y coordinate is different for each card
						//use i to determine the x coordinate
						//use j to determine the y coordinate
						CardImage c1 = new CardImage(X_INTERVAL3+i* (X_INTERVAL1 + CARD_WIDTH), Y_INTERVAL2+j*Y_INTERVAL3, ImageIO.read(new File("cardimages/"+name)), CARD_WIDTH, CARD_HEIGHT);
						this.add(c1);
						c1.paintComponent(g);
					} catch (IOException e) {
						System.out.println("Unable to read the file:" + name);
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	
}
