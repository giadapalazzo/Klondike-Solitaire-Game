package klondike;

import java.util.List;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;

/**
 * A mock implementation of {@link KlondikeModel} for testing the controller.
 * Logs method calls into a StringBuilder so tests can verify controller behavior.
 */
public class MockKlondikeModel implements KlondikeModel<Card> {
  private final StringBuilder log;
  private boolean gameOver = false;

  /**
   * Constructs a mock model that records all method calls.
   *
   * @param log the log to record method invocations
   */
  public MockKlondikeModel(StringBuilder log) {
    this.log = log;
  }

  /**
   * Sets whether this mock model should report that the game is over.
   * Used in tests to simulate end-of-game conditions.
   *
   * @param over true to simulate game over, false otherwise
   */
  public void setGameOver(boolean over) {
    this.gameOver = over;
  }

  @Override
  public List<Card> createNewDeck() {
    return List.of();
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw) {
    log.append("startGame ").append(numPiles).append(" ").append(numDraw).append("\n");
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile) {
    log.append("movePile ").append(srcPile).append(" ")
        .append(numCards).append(" ").append(destPile).append("\n");
  }

  @Override
  public void moveDraw(int destPile) {
    log.append("moveDraw ").append(destPile).append("\n");
  }

  @Override
  public void moveToFoundation(int srcPile, int destFoundation) {
    log.append("moveToFoundation ").append(srcPile).append(" ")
        .append(destFoundation).append("\n");
  }

  @Override
  public void moveDrawToFoundation(int destFoundation) {
    log.append("moveDrawToFoundation ").append(destFoundation).append("\n");
  }

  @Override
  public void discardDraw() {
    log.append("discardDraw\n");
  }

  @Override
  public boolean isGameOver() {
    return gameOver;
  }

  @Override
  public int getScore() {
    return 42;
  }

  // Stubbed methods not needed for controller tests

  @Override
  public List<Card> getDrawCards() {
    return List.of();
  }

  @Override
  public int getNumFoundations() {
    return 0;
  }

  @Override
  public int getNumPiles() {
    return 0;
  }

  @Override
  public int getNumDraw() {
    return 0;
  }

  @Override
  public int getNumRows() {
    return 0;
  }

  @Override
  public Card getCardAt(int pile, int card) {
    return null;
  }

  @Override
  public Card getCardAt(int foundation) {
    return null;
  }

  @Override
  public int getPileHeight(int pile) {
    return 0;
  }

  @Override
  public boolean isCardVisible(int pile, int card) {
    return true;
  }
}
