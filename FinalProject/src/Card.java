/**
 * This represents the Card in freecell game.
 * @author Yiyi Wang
 *
 */
public class Card {
	private int value;
	private Suit suit;

	/**
	 * Construct the card with value and Suit.
	 * @param value
	 * @param suit
	 * @throws IllegalArgumentException
	 */
	public Card(int value, Suit suit) throws IllegalArgumentException{
		if(value < 1 || value > 13) {
			throw new IllegalArgumentException("Invalid Card Value.\n");
		}
		this.value = value;
		this.suit = suit;
	}

	//Getter methods
	public int getValue() {
		return this.value;
	}
	
	public Suit getSuit() {
		return this.suit;
	}
	
	@Override
	public String toString() {
		String name = String.valueOf(value);
		return name + suit;
	}
}
