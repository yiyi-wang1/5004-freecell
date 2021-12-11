/**
 * This is a driver class for the free cell game
 * @author Yiyi Wang
 *
 */
public class ModelDriver {
	
    public static void main(String [] args)
    {
    	IModel model1 = new FreeCellModel();
    	View view = new View(model1);
    }
}