import java.util.ArrayList;
import java.lang.Math;
import java.util.Collections;
/**
 * This will present the freecell game model by implementing IModel
 * @author Yiyi Wang
 *
 */
public class FreeCellModel implements IModel{
	private ArrayList<Card> deck;
	private ArrayList<ArrayList<Card>> foundations;
	private ArrayList<ArrayList<Card>> freecells;
	private ArrayList<ArrayList<Card>> cascades;
	private boolean over;
	private boolean start;
	private CommandManager commandManager;
	public static final int FOUNDATION_NUM = 4;
	public static final int FREECELL_NUM = 4;
	public static final int CASCADE_NUM = 8;
	public static final int CARD_NUM = 13;

	/**
	 * Construct the free cell game without argument.
	 * There is 3 playing area in free cell game:
	 * Foundations: 4 foundations will represent 4 suits respectively. When all foundations all collect 13 cards, the player wins.
	 * Free cells: 4 free cells to hold card as buffer area, each free cell will hold only 1 card.
	 * Cascades: 8 cascades will hold 52 cards as the game starts, 4 cascades will hold 7 cards and 4 cascades will hold 6 cards. 
	 * 2 boolean value to show the status of the game
	 * CommandManager to execute or undo the command
	 */
	public FreeCellModel() {
		
		//create the deck
		deck = new ArrayList<Card>(52);
		
		//create the 4 foundations;
		//each foundation will be an ArrayList<Card> and hold 13 cards
		//each foundation will represent one suit
		foundations = new ArrayList<ArrayList<Card>>(FOUNDATION_NUM);
		for(int i = 0; i < 4; i++) {
			foundations.add(new ArrayList<Card>(CARD_NUM));
		}
		
		//create the 4 free cells;
		//one free cell will hold 1 card
		freecells = new ArrayList<ArrayList<Card>>(FREECELL_NUM);
		for(int i = 0; i < 4; i++) {
			freecells.add(new ArrayList<Card>(1));
		}
		
		//create the 8 cascades;
		cascades = new ArrayList<ArrayList<Card>>(CASCADE_NUM);
		for(int i = 0; i < 8; i++) {
			cascades.add(new ArrayList<Card>());
		}
		
		//default values are false when the game is not started
		over = true;
		start = false;
		//create a new CommandManger to execute the command from player
		commandManager = new CommandManager();
	}
	
	
	/**
	 * This method will create a new deck for the game.
	 * Each deck will have 52 cards.
	 * @return the ArrayList of Card
	 */
	public ArrayList<Card> createDeck() {
		
		//first create the cards with clubs
		for(int i = 1; i <= CARD_NUM; i++) {
			deck.add(new Card(i, Suit.CLUBS));
		}
		//create the cards with diamonds
		for(int i = 1; i <= CARD_NUM; i++) {
			deck.add(new Card(i, Suit.DIAMNONDS));
		}
		//create the cards with hearts
		for(int i = 1; i <= CARD_NUM; i++) {
			deck.add(new Card(i, Suit.HEARTS));
		}
		//create the cards with spades
		for(int i = 1; i <= CARD_NUM; i++) {
			deck.add(new Card(i, Suit.SPADES));
		}
		
		return deck;
	}
	
	/**
	 * This method will shuffle all cards in the deck so the deal is different for each new game.
	 */
	public void shuffle() {
		Collections.shuffle(deck);
	}

	/**
	 * The method will assign the cards to cascades
	 * @param deck: the current deck of the game; it could be shuffled or not shuffled.
	 * @throws IllegalArgumentException
	 */
	public void deal(ArrayList<Card> deck) throws IllegalArgumentException{
		if(deck == null || deck.isEmpty()) {
			throw new IllegalArgumentException("Invalid Deck.\n");
		}
		
		//putting the cards to cascades
		//the first 4 cascades will hold 7 cards;
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 7; j++)
				cascades.get(i).add(deck.get(i*7+j));
		}
		//the last 4 cascades will hold 6 cards;
		for(int i = 4; i < 8; i++) {
			for(int j = 0; j < 6; j++) 
				cascades.get(i).add(deck.get(7*4+(i-4)*6+j));
		}
	}
	
	/**
	 * This method will clear the foundations, freecells and cascades when a new game is started or the game is restarted.
	 */
	public void clear() {
		for(ArrayList<Card> f: foundations) {
			f.clear();
		}
		for(ArrayList<Card> fc: freecells) {
			fc.clear();
		}
		for(ArrayList<Card> c: cascades) {
			c.clear();
		}
	}
	
	@Override
	public void newGame() {
		start = true;
		over = false;
		this.deck.clear();//clear the deck so when the new game is started, it will not add more than 52 cards for the game.
		commandManager.clear();//clear the history of commandManager, when a new game is started, player cannot undo the last move from last game.
		clear();//clear the foundations, freecells and cascades.
		this.deck = createDeck();
		shuffle();
		deal(this.deck);
		autoCollect();//do auto collect when the game starts
	}
	
	@Override
	public void restart() {
		if(this.start) {
			clear();//clear the foundations, freecells and cascades.
			commandManager.clear();//clear the history of commandManager
			deal(this.deck);//still use the deck from this game but redeal the deck
			autoCollect();//do auto collect when the game starts
		}else {
			return;
		}
	}
	

	@Override
	public ArrayList<Card> getDeck(){
		return this.deck;
	}
	
	@Override
	public ArrayList<ArrayList<Card>> getFoundations(){
		return this.foundations;
	}
	
	@Override
	public ArrayList<ArrayList<Card>> getCascades(){
		return this.cascades;
	}
	
	@Override
	public ArrayList<ArrayList<Card>> getFreecells(){
		return this.freecells;
	}
	
	/**
	 * This method will return the current status of game.
	 * @return the boolean value of over; if true, the game is over;
	 */
	public boolean getOver() {
		return this.over;
	}
	
	@Override
	public void move(ArrayList<Card> source, ArrayList<Card> destination) {
		if(isMovableForOneCard(source,destination)) {
			//create a new command for executing
			commandManager.executeCommand(new MoveOneCardCommand(this, source, destination));
		}
		
	}

	@Override
	public boolean isMovableForOneCard(ArrayList<Card> source, ArrayList<Card> destination){
		if(this.isGameOver()) {
			return false;
		}
		if(source == null || destination == null) {
			return false;
		}
		if(source.isEmpty()) {
			return false;
		}
		//if the destination is empty, always could move the card
		if(destination.isEmpty() == true) {
			return true;
		}else {
			Card sourceCard = source.get(source.size()-1); //the last card of the source list
			Card destinationCard = destination.get(destination.size()-1); //the last card of the destination list;
			return isConnect(sourceCard, destinationCard);//check if the two cards could be connected
		}
	}


	@Override
	public void multipleMove(ArrayList<Card> cards, ArrayList<Card> source, ArrayList<Card> destination) {
		if(isMovableForMultipleCards(cards,source,destination)) {
			//create a new command for executing
			commandManager.executeCommand(new MultipleMoveCommand(this,cards,source,destination));
		}
	}

	@Override
	public boolean isMovableForMultipleCards(ArrayList<Card> cards, ArrayList<Card> source,
			ArrayList<Card> destination){
		if(this.isGameOver()) {
			return false;
		}
		if(cards == null) {
			return false;
		}
		if(source == null || destination == null) {
			return false;
		}
		if(source.isEmpty()) {
			return false;
		}
		//use the equation to calculate how many cards could be moved
		//(2^M)×(N + 1), where M is the number of empty cascades and N is the number of empty cells
		int emptyCascades = 0, emptyCells = 0;
		//check how many empty cascades and empty cells
		for(int i = 0; i < CASCADE_NUM; i++) {
			if(cascades.get(i).isEmpty()) {
				emptyCascades++;
			}
		}
		for(int i = 0; i < FREECELL_NUM; i++) {
			if(freecells.get(i).isEmpty()) {
				emptyCells++;
			}
		}
		double moveNums = (Math.pow(2,emptyCascades)) * (emptyCells +1);
		
		//could not move cards more than the moveNums
		if(cards.size() > moveNums) {
			return false;
		}
		else {
			for(int i = 0; i < cards.size()-1; i++) {
				//check if the picked up card list is in descending order with different color
				if(isConnect(cards.get(i+1), cards.get(i)) == false) {
					return false;//if any two cards are not connected, return false;
				}
			}
			//after checking the number and connectivity in card list,
			//always could move if the destination is empty
			if(destination.isEmpty()) {
				return true;
			}//if the destination is not empty, check if the first card in card list could be connected to the last card from destination
			return isConnect(cards.get(0),destination.get(destination.size()-1));
		}
	}

	@Override
	public boolean isConnect(Card source, Card destination) {
		if(source.getSuit() == Suit.CLUBS || source.getSuit() == Suit.SPADES) {//if the card is in black
			if(destination.getSuit() == Suit.CLUBS || destination.getSuit() == Suit.SPADES) {//the other card is also black
				return false;//return false
			}else {
				if(destination.getValue() == source.getValue()+1) {//check if the value of source is 1 less than the value of destination
					return true;
				}
				return false;
			}
		}else {//also check when the card is in red
			if(destination.getSuit() == Suit.DIAMNONDS || destination.getSuit() == Suit.HEARTS) {
				return false;
			}else {
				if(destination.getValue() == source.getValue()+1) {
					return true;
				}
				return false;
			}
		}
	}

	@Override
	public void undo() {
		commandManager.undo();
	}
	
	@Override
	//this is checking before undo
	//there are two scenarios that it is not undoable
	//1. the game is over
	//2. the  undo stack is empty(calling commandManager to check)
	public boolean undoable() {
		if(this.isGameOver()) {
			return false;
		}
		else{
			return commandManager.undoable();
		}
	}

	/**
	 * This is a helper function for auto collection. 
	 * @param foundation: the checking foundations
	 * @param value: the current value of the last card in each foundation; if the foundation is empty, the value will be 0
	 * @param suitNum: the int number to represent the suit
	 * @return true if this function finish one auto collect
	 */
	public boolean autoCollectHelper(ArrayList<Card> foundation, int value, int suitNum) {
		Suit suit = Suit.CLUBS;//set a default value
		
		//switch the suit value based on the suitNum
		//this allow the auto collect function to reuse this helper function for all 4 foundations
		switch(suitNum) {
		case 0: suit = Suit.CLUBS;break;
		case 1: suit = Suit.DIAMNONDS;break;
		case 2: suit = Suit.HEARTS;break;
		case 3: suit = Suit.SPADES;break;
		}
		
		//firstly check each cascades
		for(ArrayList<Card> c: this.cascades) {
			if(c.isEmpty()) {
				continue;//if any is empty, then skip this cascade
			}
			Card last = c.get(c.size()-1);//get the last card of the cascades
			if(last.getValue() == value+1 && last.getSuit() == suit) {//check if the card could be return to foundation
				foundation.add(last);
				c.remove(last);
				return true;
			}
		}
		//also need to check each freecell
		for(ArrayList<Card> f: this.freecells) {
			if(f.isEmpty()) {
				continue;
			}
			Card cell = f.get(f.size()-1);
			if(cell.getValue() == value+1 && cell.getSuit() == suit) {
				foundation.add(cell);
				f.remove(cell);
				return true;
			}
		}
		return false; //if there is no move from cascades or freecells to foundation, return false;
	}
	
	@Override
	public void autoCollect(){
		//use this boolean value to keep auto collecting until there is nothing to collect
		boolean stillCollectable = true;
		
		while(stillCollectable) {
			stillCollectable = false;//set it to false to avoid infinite loop
		
			//foundation[0] is for CLUBS
			//foundation[1] is for DIAMNONDS	
			//foundation[2] is for HEARTS
			//foundation[3] is for SPADES
			
			//use for loop to check all foundations
			for(int i = 0; i < FOUNDATION_NUM; i++) {
				if(foundations.get(i).isEmpty()){
					//if the foundations is empty, the current value is 0
					if (autoCollectHelper(foundations.get(i), 0, i)) {//if the auto collect do happen,
						stillCollectable = true;//set the value to true so the loop could be continue
						break;//end the for loop because need to run the while loop again
					}
					
				}else {//if the foundation is not empty, the current value is the last card value in each foundation
					if(autoCollectHelper(foundations.get(i), foundations.get(i).get(foundations.get(i).size()-1).getValue(), i)) {
						stillCollectable = true;
						break;
					}
				}
			}
		}
	}

	/**
	 * This is a helper function to check if the foundation collect all the 13 cards
	 * if so the foundation is finished collecting
	 * @param foundation: the foundation to check
	 * @param suit: the suit that this foundation represent
	 * @return true if the foundation collect all cards
	 */
	public boolean checkFoundations(ArrayList<Card> foundation, Suit suit){
		//if the size foundation is less than 13, return false
		if(foundation.isEmpty()) {
			return false;
		}
		if(foundation.size() < CARD_NUM) {
			return false;
		}
		
		boolean finished = true;
		//if one card in foundation is not in ascending order or not equal to the suit value
		//finished is set to false
		for(int i = 0; i < CARD_NUM; i++) {
			if(foundation.get(i).getValue() != i+1 || foundation.get(i).getSuit() != suit) {
				finished = false;
			}
		}
		return finished;

	}
	
	@Override
	public boolean isStart() {
		return start;
	}
	
	@Override
	public boolean isGameOver() { 
		if(this.isStart() == false) {
			return true;
		}
		//if all foundations are finished, the game is over
		if(checkFoundations(foundations.get(0), Suit.CLUBS) && checkFoundations(foundations.get(1), Suit.DIAMNONDS) &&
				checkFoundations(foundations.get(2), Suit.HEARTS) && checkFoundations(foundations.get(3), Suit.SPADES)){
			over = true;
			start = false; //set the start value to false;
		}
		return over;
	}

	/**
	 * This is a private class implementing the Command interface to represent the move one card command
	 * @author Yiyi Wang
	 *
	 */
	private class MoveOneCardCommand implements Command{
		private FreeCellModel model;
		private ArrayList<ArrayList<Card>> preFoundations;
		private ArrayList<ArrayList<Card>> preFreecells;
		private ArrayList<ArrayList<Card>> preCascades;
		private boolean preOverValue;
		private ArrayList<Card> source;
		private ArrayList<Card> destination;
	
		/**
		 * This construct the Move one card command.
		 * @param model: the current state of model 
		 * @param source: the source to move card
		 * @param destination: the destination to accept card
		 */
		private MoveOneCardCommand(FreeCellModel model, ArrayList<Card> source, ArrayList<Card> destination) {
			this.model = model;
			this.source = source;
			this.destination = destination;
			
			//copy the state of foundations
			//it need to be full copy
			this.preFoundations = new ArrayList<ArrayList<Card>>(4);
			for(int i = 0 ; i < FOUNDATION_NUM; i++) {
				preFoundations.add(new ArrayList<Card>(13));
			}
			for(int i = 0; i < FOUNDATION_NUM; i++) {
				for(int j = 0; j < model.getFoundations().get(i).size(); j++) {
					preFoundations.get(i).add(model.getFoundations().get(i).get(j));
				}
			}
			
			//copy the state of freecells
			this.preFreecells = new ArrayList<ArrayList<Card>>(4);
			for(int i = 0; i < FREECELL_NUM; i++) {
				preFreecells.add(new ArrayList<Card>(1));
			}
			for(int i = 0; i < FREECELL_NUM; i++) {
				for(int j = 0; j < model.getFreecells().get(i).size(); j++) {
					preFreecells.get(i).add(model.getFreecells().get(i).get(j));
				}
			}
			
			//copy the cascades
			this.preCascades = new ArrayList<ArrayList<Card>>(8);
			for(int i = 0; i < CASCADE_NUM; i++) {
				preCascades.add(new ArrayList<Card>());
			}
			for(int i = 0; i < CASCADE_NUM; i++) {
				for(int j = 0; j < model.getCascades().get(i).size(); j++) {
					preCascades.get(i).add(model.getCascades().get(i).get(j));
				}
			}
			//copy the current status of game
			preOverValue = model.getOver();
		
		}
		
		@Override
		public void execute() {
			Card move = source.get(source.size()-1);//the card to be moved
			source.remove(source.size()-1); //remove the last card
			destination.add(move);//add it to the destination list
			model.autoCollect();//need to do auto collect after move
			model.over = model.isGameOver();//check if the game is over
		}

		@Override
		public void undo() {
			//copy from previous value to the model	
			model.clear();//need to clear the current value in the model first
			for(int i = 0; i < FOUNDATION_NUM; i++) {
				for(int j = 0; j < preFoundations.get(i).size(); j++) {
					model.getFoundations().get(i).add(preFoundations.get(i).get(j));
				}
			}
			
			for(int i = 0; i < FREECELL_NUM; i++) {
				for(int j = 0; j < preFreecells.get(i).size(); j++) {
					model.getFreecells().get(i).add(preFreecells.get(i).get(j));
				}
			}
			
			for(int i = 0; i < CASCADE_NUM; i++) {
				for(int j = 0; j < preCascades.get(i).size(); j++) {
					model.getCascades().get(i).add(preCascades.get(i).get(j));
				}
			}
			model.over = preOverValue;
		}
	
	}
	
	/**
	 * This is a private class implementing the Command interface to represent the move multiple cards command
	 * @author Yiyi Wang
	 *
	 */
	private class MultipleMoveCommand implements Command{
		private FreeCellModel model;
		private ArrayList<ArrayList<Card>> preFoundations;
		private ArrayList<ArrayList<Card>> preFreecells;
		private ArrayList<ArrayList<Card>> preCascades;
		private boolean preOverValue;
		private ArrayList<Card> cards;
		private ArrayList<Card> source;
		private ArrayList<Card> destination;
	
		/**
		 * This construct the Move one card command.
		 * @param model: the current state of model 
		 * @param cards: the card list to move
		 * @param source: the source to move cards
		 * @param destination: the destination to accept cards
		 */
		private MultipleMoveCommand(FreeCellModel model, ArrayList<Card> cards, ArrayList<Card> source, ArrayList<Card> destination) {
			this.model = model;
			this.cards = cards;
			this.source = source;
			this.destination = destination;
			
			//copy the state of foundations
			//it need to be full copy
			this.preFoundations = new ArrayList<ArrayList<Card>>(4);
			for(int i = 0 ; i < FOUNDATION_NUM; i++) {
				preFoundations.add(new ArrayList<Card>(13));
			}
			for(int i = 0; i < FOUNDATION_NUM; i++) {
				for(int j = 0; j < model.getFoundations().get(i).size(); j++) {
					preFoundations.get(i).add(model.getFoundations().get(i).get(j));
				}
			}
			//copy the state of freecells
			this.preFreecells = new ArrayList<ArrayList<Card>>(4);
			for(int i = 0; i < FREECELL_NUM; i++) {
				preFreecells.add(new ArrayList<Card>(1));
			}
			for(int i = 0; i < FREECELL_NUM; i++) {
				for(int j = 0; j < model.getFreecells().get(i).size(); j++) {
					preFreecells.get(i).add(model.getFreecells().get(i).get(j));
				}
			}
			//copy the state of cascades
			this.preCascades = new ArrayList<ArrayList<Card>>(8);
			for(int i = 0; i < CASCADE_NUM; i++) {
				preCascades.add(new ArrayList<Card>());
			}
			for(int i = 0; i < CASCADE_NUM; i++) {
				for(int j = 0; j < model.getCascades().get(i).size(); j++) {
					preCascades.get(i).add(model.getCascades().get(i).get(j));
				}
			}
			preOverValue = model.getOver();
		
		}
		
		@Override
		public void execute() {
			destination.addAll(cards);//add the card list to destination
			source.removeAll(cards);//remove the cards from source
			model.autoCollect();//do auto collect after move 
			model.over = model.isGameOver();//check if the game is over
		
		}

		@Override
		public void undo() {
			//copy the value from pre to the mode	
			model.clear();
			for(int i = 0; i < FOUNDATION_NUM; i++) {
				for(int j = 0; j < preFoundations.get(i).size(); j++) {
					model.getFoundations().get(i).add(preFoundations.get(i).get(j));
				}
			}
			
			for(int i = 0; i < FREECELL_NUM; i++) {
				for(int j = 0; j < preFreecells.get(i).size(); j++) {
					model.getFreecells().get(i).add(preFreecells.get(i).get(j));
				}
			}
			
			for(int i = 0; i < CASCADE_NUM; i++) {
				for(int j = 0; j < preCascades.get(i).size(); j++) {
					model.getCascades().get(i).add(preCascades.get(i).get(j));
				}
			}
			model.over = preOverValue;
		}
	
	}
}
