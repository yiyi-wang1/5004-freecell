import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
/**
 * This is a test class for testing the FreeCell Model
 * @author Yiyi Wang
 *
 */
public class FreeCellModelTest {
	
	private IModel freecell = new FreeCellModel();
	private Card c1;
	private Card c2;
	private Card c3;
	private Card c4;
	private Card c5;
	private Card c6;
	private Card c7;
	private Card c8;
	private Card c9;
	private ArrayList<Card> source;
	private ArrayList<Card> destination;
	private ArrayList<Card> cardlist;
	private ArrayList<Card> source2;
	
	@Before
	public void init() {
		c2 = new Card(13,Suit.DIAMNONDS);
    	c3 = new Card(12,Suit.HEARTS);
    	c4 = new Card(8,Suit.HEARTS);
    	c5 = new Card(7,Suit.CLUBS);
    	c6 = new Card(6,Suit.DIAMNONDS);
    	c7 = new Card(7,Suit.SPADES);
    	c8 = new Card(6,Suit.DIAMNONDS);
    	c9 = new Card(5,Suit.CLUBS);
    	source = new ArrayList<Card>();
    	source2 = new ArrayList<Card>();
    	destination = new ArrayList<Card>();
    	cardlist = new ArrayList<Card>();
    	
    	destination.add(c2);
    	destination.add(c3);
    	destination.add(c4);
    	
    	source.add(c6);
    	source.add(c5);
    	
    	cardlist.add(c7);
    	cardlist.add(c8);
    	cardlist.add(c9);
    	
    	source2.add(c6);
    	source2.add(c5);
    	source2.add(c7);
    	source2.add(c8);
    	source2.add(c9);
    	
	}

	/**
	 * use a helper function to test
	 * this will move cards from the first cascade to freecells
	 */
	private void testHelper() {
		freecell.newGame();
		c1 = freecell.getCascades().get(0).get(freecell.getCascades().get(0).size() -1);
		freecell.move(freecell.getCascades().get(0), freecell.getFreecells().get(0));
		freecell.move(freecell.getCascades().get(0), freecell.getFreecells().get(1));
		freecell.move(freecell.getCascades().get(0), freecell.getFreecells().get(2));
		freecell.move(freecell.getCascades().get(0), freecell.getFreecells().get(3));
	}
	
	//the getter will also be tested in other test cases
	@Test
	public void testGetFoundations() {
		for(ArrayList<Card> f: freecell.getFoundations()) {
			assertTrue(f.isEmpty());
		}
	}

	@Test
	public void testGetFreecells() {
		for(ArrayList<Card> fc: freecell.getFreecells()) {
			assertTrue(fc.isEmpty());
		}
	}
	
	@Test
	public void testGetCascades() {
		for(ArrayList<Card> c: freecell.getCascades()) {
			assertTrue(c.isEmpty());
		}
		testHelper();
		for(ArrayList<Card> c: freecell.getCascades()) {
			assertFalse(c.isEmpty());
		}
	}
	
	@Test
	public void testRestart() {
		testHelper();
		ArrayList<Card> predeck = freecell.getDeck();
		freecell.restart();
		assertEquals(predeck, freecell.getDeck());//if the game is restarted, the deck should not be changed
	}
	
	@Test
	public void testIsStart() {
		assertFalse(freecell.isStart());
		testHelper();
		assertTrue(freecell.isStart());
	}
	
	//use the cards and card list to test the move method because the model will have random order of cards and it is difficult to see the result
	@Test
	public void testMove() {
		freecell.newGame();
		freecell.move(source, destination);
		ArrayList<Card> expectedSource = new ArrayList<Card>();
		expectedSource.add(c6);
		ArrayList<Card> expectedDestination = new ArrayList<Card>();
		expectedDestination.add(c2);
		expectedDestination.add(c3);
		expectedDestination.add(c4);
		expectedDestination.add(c5);
		assertEquals(expectedSource,source);
		assertEquals(expectedDestination,destination);
	}
	
	@Test
	public void testIsMovableForOneCard() {
		ArrayList<Card> emptylist = new ArrayList<Card>();
		assertFalse(freecell.isMovableForOneCard(null, destination));
		assertFalse(freecell.isMovableForOneCard(source, null));
		assertFalse(freecell.isMovableForOneCard(emptylist, destination));
		assertFalse(freecell.isMovableForOneCard(source,destination));//the game is not start yet
		freecell.newGame();
		assertTrue(freecell.isMovableForOneCard(source,destination));
		assertFalse(freecell.isMovableForOneCard(source2,destination));
	}
	
	@Test
	public void testMultipleMove() {
		freecell.newGame();
		freecell.multipleMove(cardlist, source2, destination);
		ArrayList<Card> expectedSource = new ArrayList<Card>();
		expectedSource.add(c6);
		expectedSource.add(c5);
		ArrayList<Card> expectedDestination = new ArrayList<Card>();
		expectedDestination.add(c2);
		expectedDestination.add(c3);
		expectedDestination.add(c4);
		expectedDestination.add(c7);
		expectedDestination.add(c8);
		expectedDestination.add(c9);
		assertEquals(expectedSource,source2);
		assertEquals(expectedDestination,destination);
	}
	
	@Test
	public void testIsMovableForMultipleCards() {
		ArrayList<Card> emptylist = new ArrayList<Card>();
		assertFalse(freecell.isMovableForMultipleCards(null, source2, destination));
		assertFalse(freecell.isMovableForMultipleCards(cardlist, null, destination));
		assertFalse(freecell.isMovableForMultipleCards(cardlist, source2, null));
		assertFalse(freecell.isMovableForMultipleCards(cardlist,emptylist, destination));
		assertFalse(freecell.isMovableForMultipleCards(cardlist,source2,destination));//the game is not start yet
		freecell.newGame();
		assertTrue(freecell.isMovableForMultipleCards(cardlist,source2,destination));
		testHelper();//Test if all free cells are occupied;
		assertFalse(freecell.isMovableForMultipleCards(cardlist,source2,destination));
	}
	
	@Test
	public void testIsConnect() {
		assertFalse(freecell.isConnect(c3, c2));//same color in descending order
		assertFalse(freecell.isConnect(c3, c5));//different color not in descending order
		assertTrue(freecell.isConnect(c5, c4));//different color and in descending order
	}

	//Test the one card move undo
	@Test
	public void testundo1() {
		testHelper();
		freecell.undo();
		assertTrue(freecell.getFreecells().get(3).isEmpty());
	}
	
	//Test the multiple card move undo
	@Test
	public void testundo2() {
		freecell.newGame();
		freecell.getCascades().get(0).clear();
		freecell.multipleMove(cardlist, source2, freecell.getCascades().get(0));
		assertEquals(3,freecell.getCascades().get(0).size());
		freecell.undo();
		assertTrue(freecell.getCascades().get(0).isEmpty());
	}
	
	//Test the undoable method from the model
	@Test
	public void testUndoable() {
		assertFalse(freecell.undoable());//cannot undo because the game is not started
		testHelper();
		assertTrue(freecell.undoable());
	}
	
	@Test
	public void testAutoCollect() {
		//manually add cards to cascades to test the auto collect function
		Card ace = new Card (1, Suit.CLUBS);
		freecell.getCascades().get(0).add(ace);
		freecell.autoCollect();
		assertEquals(ace, freecell.getFoundations().get(0).get(0));
		Card clubs2 = new Card (2, Suit.CLUBS);
		freecell.getCascades().get(0).add(clubs2);
		freecell.autoCollect();
		assertEquals(clubs2, freecell.getFoundations().get(0).get(1));
	}
	
	@Test
	public void testIsGameOver() {
		assertTrue(freecell.isGameOver());
		testHelper();
		assertFalse(freecell.isGameOver());
		
		//manually set a scenario that all foundations are finished
		for(ArrayList<Card> f: freecell.getFoundations()) {
			f.clear();
		}
		for(int i = 0; i < 13; i++) {
			freecell.getFoundations().get(0).add(new Card (i+1, Suit.CLUBS));
		}
		for(int i = 0; i < 13; i++) {
			freecell.getFoundations().get(1).add(new Card (i+1, Suit.DIAMNONDS));
		}
		for(int i = 0; i < 13; i++) {
			freecell.getFoundations().get(2).add(new Card (i+1, Suit.HEARTS));
		}
		//only add 12 cards to the spades foundation
		for(int i = 0; i < 12; i++) {
			freecell.getFoundations().get(3).add(new Card (i+1, Suit.SPADES));
		}
		assertFalse(freecell.isGameOver());//the game should not be finished 
		freecell.getFoundations().get(3).add(new Card (13, Suit.HEARTS));//adding wrong card in foundations
		assertFalse(freecell.isGameOver());//the game should not be finished 
		freecell.getFoundations().get(3).remove(12); //remove the last one
		freecell.getFoundations().get(3).add(new Card (13, Suit.SPADES));//adding the correct card
		assertTrue(freecell.isGameOver());
		
	}
	
}

