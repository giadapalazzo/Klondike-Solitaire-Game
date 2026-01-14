package klondike;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.KlondikeCard;
import klondike.model.hw02.KlondikeCard.Suit;
import klondike.model.hw02.KlondikeModel;
import org.junit.Before;
import org.junit.Test;

/**
 * More detailed and edge-case tests for KlondikeModel.
 */
public class ExtendedKlondikeModelTests {

  private KlondikeModel<KlondikeCard> model;
  private List<KlondikeCard> deck;

  /**
   * Sets up the model and deck before beginning the tests.
   */
  @Before
  public void setUp() {
    model = new BasicKlondike();
    deck = model.createNewDeck();
  }

  @Test
  public void testDeckHasAllSuitsAndValues() {
    model = new BasicKlondike();
    List<KlondikeCard> deck = model.createNewDeck();
    long clubs = deck.stream().filter(c -> c.getSuit() == Suit.CLUBS).count();
    long hearts = deck.stream().filter(c -> c.getSuit() == Suit.HEARTS).count();
    long spades = deck.stream().filter(c -> c.getSuit() == Suit.SPADES).count();
    final long diamonds = deck.stream().filter(c -> c.getSuit() == Suit.DIAMONDS).count();
    assertEquals(13, clubs);
    assertEquals(13, hearts);
    assertEquals(13, spades);
    assertEquals(13, diamonds);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameRejectsEmptyDeck() {
    model.startGame(new ArrayList<>(), false, 7, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameRejectsNegativeValues() {
    model.startGame(deck, false, -1, -2);
  }

  @Test
  public void testPileHeightsMatchSetup() {
    model.startGame(deck, false, 7, 3);
    for (int i = 0; i < 7; i++) {
      assertEquals("Pile " + i + " height", i + 1, model.getPileHeight(i));
    }
  }

  @Test
  public void testOnlyTopCardsVisible() {
    model.startGame(deck, false, 7, 3);
    for (int i = 0; i < model.getNumPiles(); i++) {
      int h = model.getPileHeight(i);
      for (int j = 0; j < h; j++) {
        assertEquals("Only top card visible", j == h - 1, model.isCardVisible(i, j));
      }
    }
  }

  @Test
  public void testGetCardAtFoundationInitiallyNull() {
    model.startGame(deck, false, 7, 3);
    for (int f = 0; f < model.getNumFoundations(); f++) {
      assertNull(model.getCardAt(f));
    }
  }

  @Test
  public void testMoveToFoundationWithAceIncreasesScore() {
    model.startGame(deck, false, 7, 3);
    int before = model.getScore();
    for (int i = 0; i < model.getNumPiles(); i++) {
      int top = model.getPileHeight(i) - 1;
      KlondikeCard card = model.getCardAt(i, top);
      if (card.getValue() == 1) {
        model.moveToFoundation(i, 0);
        assertEquals(before + 1, model.getScore());
        return;
      }
    }
  }
}
