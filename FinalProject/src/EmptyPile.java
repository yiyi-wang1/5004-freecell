import java.awt.Graphics;
import javax.swing.JPanel;
/**
 * This represent the view of empty pile.
 * @author Yiyi Wang
 *
 */
public class EmptyPile extends JPanel{

	protected int posx; 
	protected int posy;
	protected int imageW;
	protected int imageH;
	
	EmptyPile(int x, int y, int w, int h){
		posx = x;
		posy = y;
		imageW = w;
		imageH =h;
	}
	
	/**
	 * This method will draw a rectangle for empty pile
	 */
	public void paintComponent(Graphics g)
	{
		g.drawRect(posx, posy, imageW, imageH);
	}
}
