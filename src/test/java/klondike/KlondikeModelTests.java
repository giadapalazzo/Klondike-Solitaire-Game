package klondike;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.KlondikeCard;
import klondike.model.hw02.KlondikeModel;
import org.junit.Before;
import org.junit.Test;

/**
 * Basic tests for the KlondikeModel implementation.
 */
public class KlondikeModelTests {

  private KlondikeModel<KlondikeCard> model;
  private List<KlondikeCard> deck;

  /**
   * sets up values for model and deck before beginning the testing.
   */
  @Before
  public void setUp() {
    model = new BasicKlondike();
    deck = model.createNewDeck();
  }

  @Test
  public void testCreateNewDeckHas52UniqueCards() {
    assertEquals("Deck must contain 52 cards", 52, deck.size());
    assertEquals("Deck must have 52 unique cards", 52, deck.stream().distinct().count());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameRejectsNullDeck() {
    model.startGame(null, false, 7, 3);
  }

  @Test
  public void testStartGameInitializesCorrectly() {
    model.startGame(deck, false, 7, 3);
    assertTrue("Should have piles", model.getNumPiles() > 0);
    assertTrue("Should have rows", model.getNumRows() > 0);
    assertEquals("Score starts at 0", 0, model.getScore());
    assertFalse("Game should not be over", model.isGameOver());
  }

  @Test
  public void testDrawPileSizeDoesNotExceedNumDraw() {
    model.startGame(deck, false, 7, 3);
    assertTrue("Draw pile size ≤ numDraw",
        model.getDrawCards().size() <= model.getNumDraw());
  }

  @Test(expected = IllegalStateException.class)
  public void testMovePileToItselfThrows() {
    model.startGame(deck, false, 7, 3);
    model.movePile(0, 1, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDrawFailsWhenEmpty() {
    model.startGame(deck, false, 7, 3);
    while (!model.getDrawCards().isEmpty()) {
      model.discardDraw();
    }
    model.moveDraw(0);
  }

  @Test
  public void testDiscardDrawRemovesCard() {
    model.startGame(deck, false, 7, 3);
    if (!model.getDrawCards().isEmpty()) {
      KlondikeCard first = model.getDrawCards().get(0);
      model.discardDraw();
      assertFalse("First draw card should be removed",
          model.getDrawCards().contains(first));
    }
  }
}
