package klondike.model.hw02;

/**
 * Extended card interface for Klondike, exposing suit, value, and color.
 */
public interface KlondikeCard extends Card {
  /**
   * Represents the four suits in a standard deck of cards,
   * along with properties  like their display symbol and color (red or black).
   */
  enum Suit {
    CLUBS("♣", false),
    SPADES("♠", false),
    HEARTS("♡", true),
    DIAMONDS("♢", true);

    private final String symbol;
    private final boolean isRed;

    Suit(String symbol, boolean isRed) {
      this.symbol = symbol;
      this.isRed = isRed;
    }

    public String getSymbol() {
      return symbol;
    }

    public boolean isRed() {
      return isRed;
    }
  }

  /**
   * Gives the suit of the card.
   *
   *
   * @return the suit of the card.
   */

  Suit getSuit();

  /**
   * gets the value of the card.
   *
   * @return the value of the card
   *
   */
  int getValue();

  /**
   * returns true if the card is red.(hearts or diamonds).
   *
   * @return true if the card is red
   */

  boolean isRed();


}
