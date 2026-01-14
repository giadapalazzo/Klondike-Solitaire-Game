package klondike.model.hw04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import klondike.model.hw02.CardImpl;
import klondike.model.hw02.KlondikeCard;
import klondike.model.hw02.KlondikeModel;

/**
 * Represents the Whitehead Klondike variant of Klondike Solitaire.
 * Differs from standard game by making all cards face-up and enforcing
 * the same-color builds instead of alternating colors.
 */
public class WhiteheadKlondike implements KlondikeModel<KlondikeCard> {

  private List<List<KlondikeCard>> cascades;
  private List<Integer> visibleFromIndices;
  private List<List<KlondikeCard>> foundations;
  private List<KlondikeCard> drawPile;
  private List<KlondikeCard> discardPile;
  private boolean started;
  private int numDraw;

  /**
   * Creates an empty Whitehead Klondike game.
   */
  public WhiteheadKlondike() {
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
    if (started) {
      throw new IllegalStateException("Game already started");
    }
    if (deck == null) {
      throw new IllegalArgumentException("Deck cannot be null");
    }
    if (numDraw < 1) {
      throw new IllegalArgumentException("Number of draw cards must be positive");
    }

    if (numPiles < 1) {
      throw new IllegalArgumentException("Number of piles must be positive");
    }
    int cardsNeeded = numPiles * (numPiles + 1) / 2;
    if (cardsNeeded > deck.size()) {
      throw new IllegalArgumentException("Not enough cards for " + numPiles + " piles");
    }
    List<KlondikeCard> workingDeck = new ArrayList<>(deck);
    if (shuffle) {
      Collections.shuffle(workingDeck);
    }
    cascades = new ArrayList<>();
    visibleFromIndices = new ArrayList<>();
    for (int i = 0; i < numPiles; i++) {
      cascades.add(new ArrayList<>());
      visibleFromIndices.add(0);
    }
    foundations = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      foundations.add(new ArrayList<>());
    }

    int index = 0;
    for (int row = 0; row < numPiles; row++) {
      for (int pile = row; pile < numPiles; pile++) {
        if (index >= workingDeck.size()) {
          throw new IllegalArgumentException("Not enough cards for cascade setup");
        }
        cascades.get(pile).add(workingDeck.get(index));
        index++;
      }
    }

    drawPile = new ArrayList<>();
    while (index < workingDeck.size()) {
      drawPile.add(workingDeck.get(index));
      index++;
    }

    discardPile = new ArrayList<>();
    started = true;
    this.numDraw = numDraw;
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile) {
    ensureStarted();
    checkPileIndex(srcPile);
    checkPileIndex(destPile);

    if (srcPile == destPile) {
      throw new IllegalArgumentException("Cannot move to same pile");
    }

    List<KlondikeCard> source = cascades.get(srcPile);
    final List<KlondikeCard> dest = cascades.get(destPile);

    if (numCards < 1 || numCards > source.size()) {
      throw new IllegalArgumentException("Invalid number of cards");
    }

    int startIdx = source.size() - numCards;
    if (startIdx < visibleFromIndices.get(srcPile)) {
      throw new IllegalStateException("Cannot move invisible cards");
    }
    KlondikeCard firstCard = source.get(startIdx);

    if (dest.isEmpty()) {
      // Intentionally left empty in original code.
    } else {
      KlondikeCard topDest = dest.get(dest.size() - 1);
      if (!isSameColor(firstCard, topDest)) {
        throw new IllegalStateException("Must be same color in Whitehead");
      }
      if (firstCard.getValue() != topDest.getValue() - 1) {
        throw new IllegalStateException("Must be one value lower");
      }
    }

    if (numCards > 1) {
      for (int i = startIdx + 1; i < source.size(); i++) {
        KlondikeCard currentCard = source.get(i);
        KlondikeCard previousCard = source.get(i - 1);

        if (currentCard.getSuit() != previousCard.getSuit()) {
          throw new IllegalStateException("Cards must suit in Whitehead");
        }

        if (currentCard.getValue() != previousCard.getValue() - 1) {
          throw new IllegalStateException("Cards must be in descending order");
        }
      }
    }

    List<KlondikeCard> cardsToMove = new ArrayList<>(source.subList(startIdx, source.size()));
    source.subList(startIdx, source.size()).clear();
    dest.addAll(cardsToMove);
  }

  @Override
  public void moveDraw(int destPile) {
    ensureStarted();
    checkPileIndex(destPile);

    if (drawPile.isEmpty()) {
      throw new IllegalStateException("No draw cards available");
    }

    KlondikeCard drawCard = drawPile.get(0);
    List<KlondikeCard> dest = cascades.get(destPile);

    if (dest.isEmpty()) {
      // Intentionally left empty in original code.
    } else {
      KlondikeCard topDest = dest.get(dest.size() - 1);
      if (!isSameColor(drawCard, topDest)) {
        throw new IllegalStateException("Must be same color");
      }
      if (drawCard.getValue() != topDest.getValue() - 1) {
        throw new IllegalStateException("Must be one value lower");
      }
    }

    dest.add(drawPile.remove(0));
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile) {
    ensureStarted();
    checkPileIndex(srcPile);

    if (foundationPile < 0 || foundationPile >= foundations.size()) {
      throw new IllegalArgumentException("Invalid foundation pile");
    }

    List<KlondikeCard> source = cascades.get(srcPile);
    if (source.isEmpty()) {
      throw new IllegalStateException("Source pile is empty");
    }
    KlondikeCard card = source.get(source.size() - 1);
    List<KlondikeCard> foundation = foundations.get(foundationPile);
    if (foundation.isEmpty()) {
      if (card.getValue() != 1) { // Must be Ace
        throw new IllegalStateException("Foundation must start with Ace");
      }
    } else {
      KlondikeCard topFoundation = foundation.get(foundation.size() - 1);
      if (card.getSuit() != topFoundation.getSuit()) {
        throw new IllegalStateException("Must be same suit");
      }
      if (card.getValue() != topFoundation.getValue() + 1) {
        throw new IllegalStateException("Must be one value higher");
      }
    }
    foundation.add(source.remove(source.size() - 1));
  }

  @Override
  public void moveDrawToFoundation(int foundationPile) {
    ensureStarted();

    if (foundationPile < 0 || foundationPile >= foundations.size()) {
      throw new IllegalArgumentException("Invalid foundation pile");
    }

    if (drawPile.isEmpty()) {
      throw new IllegalStateException("No draw cards available");
    }

    KlondikeCard card = drawPile.get(0);
    List<KlondikeCard> foundation = foundations.get(foundationPile);

    if (foundation.isEmpty()) {
      if (card.getValue() != 1) {
        throw new IllegalStateException("Foundation must start with Ace");
      }
    } else {
      KlondikeCard topFoundation = foundation.get(foundation.size() - 1);
      if (card.getSuit() != topFoundation.getSuit()) {
        throw new IllegalStateException("Must be same suit");
      }
      if (card.getValue() != topFoundation.getValue() + 1) {
        throw new IllegalStateException("Must be one value higher");
      }
    }

    foundation.add(drawPile.remove(0));
  }

  @Override
  public void discardDraw() {
    ensureStarted();

    if (drawPile.isEmpty()) {
      if (discardPile.isEmpty()) {
        throw new IllegalStateException("No draw cards available");
      }
      drawPile.addAll(discardPile);
      discardPile.clear();

    }
    discardPile.add(drawPile.remove(0));
  }

  @Override
  public int getNumRows() {
    ensureStarted();
    return cascades.stream().mapToInt(List::size).max().orElse(0);
  }

  @Override
  public int getNumPiles() {
    ensureStarted();
    return cascades.size();
  }

  @Override
  public int getNumDraw() {
    ensureStarted();
    return numDraw;
  }

  @Override
  public boolean isGameOver() {
    ensureStarted();
    int totalInFoundations = foundations.stream().mapToInt(List::size).sum();
    return totalInFoundations == 52;
  }

  @Override
  public int getScore() {
    ensureStarted();
    int score = 0;
    for (List<KlondikeCard> f : foundations) {
      if (!f.isEmpty()) {
        score += f.get(f.size() - 1).getValue();
      }
    }
    return score;
  }

  @Override
  public int getPileHeight(int pileNum) {
    ensureStarted();
    checkPileIndex(pileNum);
    return cascades.get(pileNum).size();
  }

  @Override
  public KlondikeCard getCardAt(int pileNum, int card) {
    ensureStarted();
    checkPileIndex(pileNum);
    List<KlondikeCard> pile = cascades.get(pileNum);
    if (card < 0 || card >= pile.size()) {
      throw new IllegalArgumentException("Invalid card index.");
    }
    return pile.get(card);
  }

  @Override
  public KlondikeCard getCardAt(int foundationPile) {
    ensureStarted();
    if (foundationPile < 0 || foundationPile >= foundations.size()) {
      throw new IllegalArgumentException("Invalid foundation pile index.");
    }
    List<KlondikeCard> f = foundations.get(foundationPile);
    return f.isEmpty() ? null : f.get(f.size() - 1);
  }

  @Override
  public boolean isCardVisible(int pileNum, int card) {
    ensureStarted();
    checkPileIndex(pileNum);
    return card >= visibleFromIndices.get(pileNum);
  }

  @Override
  public List<KlondikeCard> getDrawCards() {
    ensureStarted();
    int end = Math.min(numDraw, drawPile.size());
    return new ArrayList<>(drawPile.subList(0, end));
  }

  @Override
  public int getNumFoundations() {
    ensureStarted();
    return foundations.size();
  }

  //helper methods

  /**
   * Checks that game has started.
   */
  private void ensureStarted() {
    if (!started) {
      throw new IllegalStateException("Game has not yet started");
    }
  }

  /**
   * Checks that pileNum is a valid pile index.
   *
   * @param pileNum index to be checked.
   */
  private void checkPileIndex(int pileNum) {
    if (pileNum < 0 || pileNum >= cascades.size()) {
      throw new IllegalArgumentException("Invalid pile index.");
    }
  }

  private boolean isSameColor(KlondikeCard card1, KlondikeCard card2) {
    return isRed(card1) == isRed(card2);
  }

  private boolean isRed(KlondikeCard card) {
    return card.getSuit() == KlondikeCard.Suit.DIAMONDS
        || card.getSuit() == KlondikeCard.Suit.HEARTS;
  }

  /**
   * Returns the number of draw cards remaining in the draw pile.
   *
   * @return the number of draw cards left
   */
  public int getNumDrawCardsLeft() {
    ensureStarted();
    return drawPile.size();
  }
}