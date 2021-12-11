import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
/**
 * This represent the view of a card.
 * @author Yiyi Wang
 *
 */

public class CardImage extends JPanel{
	protected int posx; 
	protected int posy;
	protected BufferedImage image; 
	protected int imageW;
	protected int imageH;

	CardImage(int posx, int posy, BufferedImage image, int imageW, int imageH)
	{
		this.posx = posx;
		this.posy = posy; 
		this.image= image;
		this.imageW = imageW; 
		this.imageH = imageH;
	}
	
	/**
	 * This method will draw the card image.
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		g.drawImage(image,posx, posy,imageW,imageH,null);
	}

}
