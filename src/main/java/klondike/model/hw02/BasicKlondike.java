package klondike.model.hw02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Basic implementation of the KlondikeModel Interface. Creates a 52 card deck
 * and provides core functionality of Klondike solitaire game
 */
public class BasicKlondike implements KlondikeModel<KlondikeCard> {
  private List<CascadePile> cascades;
  private List<List<KlondikeCard>> foundations;
  private List<KlondikeCard> drawPile;
  private List<KlondikeCard> discardPile;
  private boolean started;
  private int numDraw;

  /**
   * Creates an empty game model that must be started with startGame().
   */
  public BasicKlondike() {
    this.started = false;
  }

  @Override
  public List<KlondikeCard> createNewDeck() {
    List<KlondikeCard> deck = new ArrayList<>();
    for (int v = 1; v <= 13; v++) {
      for (KlondikeCard.Suit suit : KlondikeCard.Suit.values()) {
        deck.add(new CardImpl(suit, v));
      }
    }
    return deck;
  }

  @Override
  public void startGame(List<KlondikeCard> deck, boolean shuffle, int numPiles, int numDraw) {
    if (deck == null) {
      throw new IllegalArgumentException("Deck cannot be null");
    }

    if (numPiles < 1 || numDraw < 1) {
      throw new IllegalArgumentException("Must have at least one pile and one draw");
    }

    validateDeck(deck);
    int neededForCascades = numPiles * (numPiles + 1) / 2;
    if (deck.size() < neededForCascades) {
      throw new IllegalArgumentException("Not enough cards to deal cascades");
    }

    List<KlondikeCard> copy = new ArrayList<>(deck);
    if (shuffle) {
      Collections.shuffle(copy, new Random());
    }

    cascades = new ArrayList<>();
    foundations = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      foundations.add(new ArrayList<>());
    }
    drawPile = new ArrayList<>();
    discardPile = new ArrayList<>();

    int index = 0;

    for (int i = 0; i < numPiles; i++) {
      cascades.add(new CascadePileImpl());
    }

    for (int r = 0; r < numPiles; r++) {
      for (int p = r; p < numPiles; p++) {
        if (index >= copy.size()) {
          throw new IllegalArgumentException("Not enough cards for cascade setup");
        }
        cascades.get(p).addCard(copy.get(index));
        index++;
      }
    }

    while (index < copy.size()) {
      drawPile.add(copy.get(index));
      index++;
    }

    this.numDraw = numDraw;
    this.started = true;
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile) {
    checkStarted();
    if (!canMovePile(srcPile, numCards, destPile)) {
      throw new IllegalStateException("Invalid move from pile " + srcPile + " to " + destPile);
    }
    CascadePile source = cascades.get(srcPile);
    CascadePile destination = cascades.get(destPile);
    source.moveTo(destination, numCards);
  }

  /**
   * Checks if moving the given number of cards from one cascade pile to another is valid.
   */
  private boolean canMovePile(int srcPile, int numCards, int destPile) {
    if (srcPile < 0 || srcPile >= cascades.size()
        || destPile < 0 || destPile >= cascades.size()
        || srcPile == destPile) {
      return false;
    }

    CascadePile source = cascades.get(srcPile);
    CascadePile destination = cascades.get(destPile);

    if (numCards <= 0 || numCards > source.size()) {
      return false;
    }

    if (!isCardVisible(srcPile, source.size() - numCards)) {
      return false;
    }

    KlondikeCard cardToMove = source.getCard(source.size() - numCards);
    return destination.canAdd(cardToMove);
  }

  @Override
  public void moveDraw(int destPile) {
    checkStarted();
    if (!canMoveDraw(destPile)) {
      throw new IllegalStateException("Invalid move from draw to pile " + destPile);
    }
    KlondikeCard cardToMove = drawPile.remove(0);
    cascades.get(destPile).addCard(cardToMove);
  }

  private boolean canMoveDraw(int destPile) {
    if (drawPile.isEmpty() || destPile < 0 || destPile >= cascades.size()) {
      return false;
    }
    return cascades.get(destPile).canAdd(drawPile.get(0));
  }

  private boolean canMoveToFoundation(int srcPile, int foundationPile) {
    if (srcPile < 0 || srcPile >= cascades.size()
        || foundationPile < 0 || foundationPile >= foundations.size()) {
      throw new IllegalArgumentException("Invalid pile index");
    }

    CascadePile source = cascades.get(srcPile);
    List<KlondikeCard> foundation = foundations.get(foundationPile);

    if (source.isEmpty()) {
      return false;
    }

    KlondikeCard cardToMove = source.peek();

    if (foundation.isEmpty()) {
      return cardToMove.getValue() == 1;
    } else {
      KlondikeCard topCard = foundation.get(foundation.size() - 1);
      return cardToMove.getSuit() == topCard.getSuit()
          && cardToMove.getValue() == topCard.getValue() + 1;
    }
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile) {
    checkStarted();
    if (!canMoveToFoundation(srcPile, foundationPile)) {
      throw new IllegalStateException("Invalid move to foundation");
    }

    CascadePile source = cascades.get(srcPile);
    KlondikeCard cardToMove = source.peek();
    foundations.get(foundationPile).add(cardToMove);
    source.removeLast();
  }

  @Override
  public void moveDrawToFoundation(int foundationPile) {
    checkStarted();
    if (!canMoveDrawToFoundation(foundationPile)) {
      throw new IllegalStateException("Invalid move from draw to foundation");
    }
    KlondikeCard card = drawPile.remove(0);
    foundations.get(foundationPile).add(card);
  }

  private boolean canMoveDrawToFoundation(int foundationPile) {
    if (drawPile.isEmpty() || foundationPile < 0 || foundationPile >= foundations.size()) {
      return false;
    }
    return canAddToFoundation(drawPile.get(0), foundationPile);
  }

  @Override
  public void discardDraw() {
    checkStarted();

    if (drawPile.isEmpty()) {
      if (discardPile.isEmpty()) {
        throw new IllegalStateException("No cards to discard or recycle.");
      }
      drawPile.addAll(discardPile);
      discardPile.clear();
    }
    discardPile.add(drawPile.remove(0));
  }

  @Override
  public int getNumRows() {
    checkStarted();
    int max = 0;
    for (CascadePile pile : cascades) {
      max = Math.max(max, pile.size());
    }
    return max;
  }

  @Override
  public int getNumPiles() {
    checkStarted();
    return cascades == null ? 0 : cascades.size();
  }

  @Override
  public int getPileHeight(int pileNum) {
    checkStarted();
    return cascades.get(pileNum).size();
  }

  @Override
  public boolean isCardVisible(int pileNum, int card) {
    checkStarted();
    if (pileNum < 0 || pileNum >= cascades.size()) {
      throw new IllegalArgumentException("Invalid pile number: " + pileNum);
    }
    CascadePile pile = cascades.get(pileNum);
    if (card < 0 || card >= pile.size()) {
      throw new IllegalArgumentException("Invalid card number: " + card);
    }
    return pile.isVisible(card);
  }

  @Override
  public KlondikeCard getCardAt(int pileNum, int card) {
    checkStarted();
    if (pileNum < 0 || pileNum >= cascades.size()) {
      throw new IllegalArgumentException("Invalid pile number: " + pileNum);
    }
    CascadePile pile = cascades.get(pileNum);
    if (card < 0 || card >= pile.size()) {
      throw new IllegalArgumentException("Invalid card number: " + card);
    }
    if (!isCardVisible(pileNum, card)) {
      throw new IllegalArgumentException("Cannot get card at this position, card is not visible");
    }
    return pile.getCard(card);
  }

  @Override
  public KlondikeCard getCardAt(int foundationPile) {
    checkStarted();
    if (foundationPile < 0 || foundationPile >= foundations.size()) {
      throw new IllegalArgumentException("Invalid foundation pile number: " + foundationPile);
    }
    List<KlondikeCard> pile = foundations.get(foundationPile);
    return pile.isEmpty() ? null : pile.get(pile.size() - 1);
  }

  @Override
  public int getNumFoundations() {
    checkStarted();
    return 4;
  }

  @Override
  public int getNumDraw() {
    checkStarted();
    return numDraw;
  }

  @Override
  public List<KlondikeCard> getDrawCards() {
    checkStarted();
    return new ArrayList<>(drawPile.subList(0, Math.min(numDraw, drawPile.size())));
  }

  @Override
  public boolean isGameOver() {
    checkStarted();
    boolean allFoundationsComplete = true;
    for (List<KlondikeCard> pile : foundations) {
      if (pile.size() < 13) {
        allFoundationsComplete = false;
        break;
      }
    }
    if (allFoundationsComplete) {
      return true;
    }
    for (CascadePile pile : cascades) {
      if (!pile.isEmpty()) {
        KlondikeCard topCard = pile.peek();
        for (int f = 0; f < foundations.size(); f++) {
          if (canAddToFoundation(topCard, f)) {
            return false;
          }
        }
      }
    }
    if (!drawPile.isEmpty()) {
      KlondikeCard drawCard = drawPile.get(0);
      for (int f = 0; f < foundations.size(); f++) {
        if (canAddToFoundation(drawCard, f)) {
          return false;
        }
      }
      for (CascadePile cascade : cascades) {
        if (cascade.canAdd(drawCard)) {
          return false;
        }
      }
    }
    return drawPile.isEmpty() && discardPile.isEmpty();
  }

  @Override
  public int getScore() {
    checkStarted();
    int score = 0;
    for (List<KlondikeCard> pile : foundations) {
      score += pile.size();
    }
    return score;
  }

  /**
   * Checks if the game has been started.  If not throws an illegal state exception.
   */
  private void checkStarted() {
    if (!started) {
      throw new IllegalStateException("Game not started.");
    }
  }

  /**
   * checks if the deck is valid. Meaning it contains one of every card
   *
   * @param deck List of KlondikeCard that has all the cards.
   *
   * @return true if is valid else false
   */

  private boolean isValidDeck(List<KlondikeCard> deck) {
    if (deck == null || deck.isEmpty()) {
      return false;
    }

    Set<KlondikeCard> uniqueCards = new HashSet<>();
    for (KlondikeCard card : deck) {
      if (card.getValue() < 1 || card.getValue() > 13) {
        return false;
      }
      if (!uniqueCards.add(card)) {
        return false;
      }
    }

    return uniqueCards.size() == 52;
  }

  /**
   * Determines if the given card can be added to specified foundation.
   *
   * @param card the klondikeCard to check.
   *
   * @param foundationPile the index of the foundation pile
   *
   * @return true if card can be added to the foundation pile else false
   */

  private boolean canAddToFoundation(KlondikeCard card, int foundationPile) {
    List<KlondikeCard> pile = foundations.get(foundationPile);
    if (pile.isEmpty()) {
      return card.getValue() == 1; // Ace
    }
    KlondikeCard top = pile.get(pile.size() - 1);
    return top.getSuit() == card.getSuit() && card.getValue() == top.getValue() + 1;
  }

  /**
   * Validates the given deck is a proper klondike deck. That it doesn't contain duplicates
   * and that each suit is present
   *
   * @param deck the list of Klodike Card Objects to validate
   * @throws IllegalArgumentException if deck is null,empty, has duplicates, or lacks an ace
   */
  private void validateDeck(List<KlondikeCard> deck) {
    if (deck == null || deck.isEmpty()) {
      throw new IllegalArgumentException("Deck cannot be null or empty");
    }

    // Check for duplicate card objects (by reference, not by equals)
    for (int i = 0; i < deck.size(); i++) {
      for (int j = i + 1; j < deck.size(); j++) {
        if (deck.get(i) == deck.get(j)) {
          throw new IllegalArgumentException("Duplicate card object in deck");
        }
      }
    }

    // Group by suit
    Map<KlondikeCard.Suit, List<Integer>> bySuit = new java.util.HashMap<>();
    for (KlondikeCard card : deck) {
      bySuit.computeIfAbsent(card.getSuit(), k -> new ArrayList<>()).add(card.getValue());
    }

    // Each suit must contain an Ace (value 1)
    for (Map.Entry<KlondikeCard.Suit, List<Integer>> entry : bySuit.entrySet()) {
      List<Integer> values = entry.getValue();

      // Remove duplicates and sort
      Set<Integer> uniqueValues = new HashSet<>(values);
      List<Integer> sortedValues = new ArrayList<>(uniqueValues);
      Collections.sort(sortedValues);

      // Check if Ace exists
      if (!uniqueValues.contains(1)) {
        throw new IllegalArgumentException("Invalid deck: suit "
            + entry.getKey() + " has no valid run");
      }
    }
  }

}

