package klondike.model.hw02;

import java.util.Objects;

/**
 * Implementation of a playing card for Klondike Solitaire.
 */
public class CardImpl implements KlondikeCard {

  private final Suit suit;
  private final int value; // 1 = Ace, 11 = Jack, 12 = Queen, 13 = King

  /**
   * Constructor for CardImpl. Ensures that the value of a card is between 1 and 13
   * and that the suit is not nul
   * if these aren't followed then it will throw an illegal argument exception.
   *
   * @param suit the suit of the cards.
   *
   * @param value the value of the cards between 1 and 13
   */
  public CardImpl(final Suit suit, final int value) {
    if (value < 1 || value > 13) {
      throw new IllegalArgumentException("Card value must be between 1 (Ace) and 13(King)");
    }
    this.suit = Objects.requireNonNull(suit);
    this.value = value;
  }

  @Override
  public Suit getSuit() {
    return suit;
  }

  @Override
  public int getValue() {
    return value;
  }

  @Override
  public boolean isRed() {
    return suit.isRed();
  }

  @Override
  public String toString() {
    String rank = switch (value) {
      case 1 -> "A";
      case 11 -> "J";
      case 12 -> "Q";
      case 13 -> "K";
      default -> String.valueOf(value);
    };
    return rank + suit.getSymbol();
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CardImpl)) {
      return false;
    }
    CardImpl cardImpl = (CardImpl) o;
    return value == cardImpl.value && suit == cardImpl.suit;
  }

  @Override
  public int hashCode() {
    return Objects.hash(suit, value);
  }
}
