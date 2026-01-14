package klondike.model.hw02;

import java.util.List;
/**
 * Represents the public operations for a cascade pile in Klondike Solitaire.
 * Defines the behaviors that any cascade pile implementation must support.
 */

public interface CascadePile {

  /**
   * Adds a card to this pile.
   *
   * @param card the card to add
   */
  void addCard(KlondikeCard card);

  /**
   * Returns the card at the given index.
   *
   * @param index the index of the card
   * @return the card at that position
   * @throws IllegalArgumentException if index is invalid
   */
  KlondikeCard getCard(int index);

  /**
   * Returns the number of cards in this pile.
   *
   * @return size of the pile
   */
  int size();

  /**
   * Checks if this pile is empty.
   *
   * @return true if empty, false otherwise
   */
  boolean isEmpty();

  /**
   * Returns a copy of all cards in this pile.
   *
   * @return list of cards
   */
  List<KlondikeCard> getCards();

  /**
   * Returns the top card of this pile.
   *
   * @return top card
   * @throws IllegalStateException if empty
   */
  KlondikeCard peek();

  /**
   * Removes the top card from this pile.
   *
   * @throws IllegalStateException if empty
   */
  void removeLast();

  /**
   * Returns whether the card at the given index is visible.
   *
   * @param index the card index
   * @return true if visible, false otherwise
   */
  boolean isVisible(int index);

  /**
   * Checks if the given card can be added to this pile following Klondike rules.
   *
   * @param card the card to check
   * @return true if it can be added
   */
  boolean canAdd(KlondikeCard card);

  /**
   * Moves a run of cards from this pile to another pile.
   *
   * @param dest the destination pile
   * @param numCards the number of cards to move
   * @throws IllegalArgumentException if invalid move
   */
  void moveTo(CascadePile dest, int numCards);
}
