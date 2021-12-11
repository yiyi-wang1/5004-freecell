/**
 * This represents the suit of Card.
 * @author Yiyi Wang
 *
 */
public enum Suit {
	CLUBS, DIAMNONDS, HEARTS, SPADES;
	
	@Override
	public String toString() {
		if(this == CLUBS) {
			return "CLUBS";
		}
		if(this == DIAMNONDS) {
			return "DIAMNONDS";
		}
		if(this == HEARTS) {
			return "HEARTS";
		}
		if(this == SPADES) {
			return "SPADES";
		}
	return"";
	}
}
