package klondike.model.hw02;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents one cascade pile in Klondike and is a helper class.
 */
public class CascadePileImpl implements CascadePile {
  private final List<KlondikeCard> cards;

  /**
   * Constructs an empty cascade pile.
   */
  public CascadePileImpl() {
    this.cards = new ArrayList<>();
  }

  /**
   * Constructs a cascade pile with the given initial cards.
   *
   * @param cards list of cards to start with
   */
  public CascadePileImpl(List<KlondikeCard> cards) {
    this.cards = new ArrayList<>(cards);
  }

  @Override
  public void addCard(KlondikeCard card) {
    this.cards.add(card);
  }

  @Override
  public KlondikeCard getCard(int index) {
    if (index < 0 || index >= cards.size()) {
      throw new IllegalArgumentException("Invalid card index: " + index);
    }
    return cards.get(index);
  }

  @Override
  public int size() {
    return cards.size();
  }

  @Override
  public boolean isEmpty() {
    return cards.isEmpty();
  }

  @Override
  public List<KlondikeCard> getCards() {
    return new ArrayList<>(cards);
  }

  @Override
  public KlondikeCard peek() {
    if (cards.isEmpty()) {
      throw new IllegalStateException("Pile is empty");
    }
    return getLast();
  }

  @Override
  public void removeLast() {
    if (cards.isEmpty()) {
      throw new IllegalStateException("Pile is empty");
    }
    cards.remove(cards.size() - 1);
  }

  @Override
  public boolean isVisible(int index) {
    return index == cards.size() - 1;
  }

  @Override
  public boolean canAdd(KlondikeCard card) {
    if (cards.isEmpty()) {
      return card.getValue() == 13; // King
    }
    KlondikeCard top = peek();
    boolean oppositeColor = top.isRed() != card.isRed();
    return oppositeColor && card.getValue() == top.getValue() - 1;
  }




  @Override
  public void moveTo(CascadePile dest, int numCards) {
    if (!(dest instanceof CascadePileImpl)) {
      throw new IllegalArgumentException("Invalid destination pile type");
    }

    CascadePileImpl destination = (CascadePileImpl) dest;

    if (numCards <= 0 || numCards > cards.size()) {
      throw new IllegalArgumentException("Invalid number of cards to move");
    }

    List<KlondikeCard> moving = new ArrayList<>(
        cards.subList(cards.size() - numCards, cards.size()));

    if (!destination.canAdd(moving.get(0))) {
      throw new IllegalArgumentException("Invalid move to destination pile");
    }

    destination.cards.addAll(moving);
    for (int i = 0; i < numCards; i++) {
      removeLast();
    }
  }

  /** Helper method to get the last card in the pile. */

  private KlondikeCard getLast() {
    return cards.get(cards.size() - 1);
  }



}