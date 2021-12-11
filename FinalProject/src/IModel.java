import java.util.ArrayList;

/**
 * This will be the interface for FreeCell Model
 * @author Yiyi Wang
 *
 */
public interface IModel {
	
/**
*This method is a getter function to return the current state of deck in FreeCell game.
* @return the ArrayListof Cards to represent the deck in FreeCell game.
*/	
ArrayList<Card> getDeck();	

/**
 * This method is a getter function to return the current state of foundations in FreeCell game.
 * @return the ArrayList of ArrayListof Cards to represent the 4 foundations in FreeCell game.
 */
ArrayList<ArrayList<Card>> getFoundations();

/**
 * This method is a getter function to return the current state of free cells in FreeCell game.
 * @return the ArrayList of ArrayListof Cards to represent the 4 free cells in FreeCell game.
 */
ArrayList<ArrayList<Card>> getFreecells();

/**
 * This method is a getter function to return the current state of cascades in FreeCell game.
 * @return the ArrayList of ArrayListof Cards to represent the 8 cascades in FreeCell game.
 */
ArrayList<ArrayList<Card>> getCascades();

/**
 * This method will start a new game with a new deal of cards.
 */
void newGame();

/**
 * This method will allow player to restart the game with current deal.
 */
void restart();

/**
 * This method will determine if the game is started.
 * @return true if the game is started.
 */
boolean isStart();

/**
 * This method will move one card from a cascade to another, or move one card from cascades to free cells
 * @param source: the card list to pop the last card
 * @param destination: the card list to push the card
 */
void move(ArrayList<Card>source, ArrayList<Card>destination);


/**
 * This method will determine if the last card of source could be moved to destination;
 * @param source: the card list to pop the last card
 * @param destination: the card list to push the card
 * @return true if the card can be moved
 */
boolean isMovableForOneCard(ArrayList<Card>source, ArrayList<Card>destination);


/**
 * This method will move more than one cards from a start cascade to the destination cascade
 * @param cards: the list of cards to move
 * @param source: the start card list for moving
 * @param destination: the destination card list to receive cards
 */
void multipleMove(ArrayList<Card>cards, ArrayList<Card>source, ArrayList<Card>destination);

/**
 * This method will determine if the list of cards can be moved to the destination;
 * @param cards: the list of cards to move
 * @param source: the start card list for moving
 * @param destination: the destination card list to receive cards
 * @return true if the card can be moved
 */
boolean isMovableForMultipleCards(ArrayList<Card>cards, ArrayList<Card>source, ArrayList<Card>destination);

/**
 * This method will determine if two cards can be connected
 * All the cards in the cascade should be placed in descending order with alternating colors
 * For example, Clubs8 can connect with Hearts7
 * @param source: the card to connect with destination
 * @param destination: the destination card to be connected with
 * @return true if the source card could be connected with the destination one
 */
boolean isConnect(Card source, Card destination);

/**
 * This method will allow player to undo the move.
 */
void undo();

/**
 * This method will determine if player can undo
 * @return true if user can undo
 */
boolean undoable();

/**
 * This method will auto collect the cards from cascades or freecells if it could be return to foundation;
 */
void autoCollect();

/**
 * This method will determine if the game is over.
 * The freeCell game is over when all the cards are returned to the foundations
 * @return true if the game is over; Otherwise return false.
 */
boolean isGameOver();

}
