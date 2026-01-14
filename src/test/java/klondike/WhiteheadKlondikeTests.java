package klondike;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import klondike.model.hw02.CardImpl;
import klondike.model.hw02.KlondikeCard;
import klondike.model.hw04.WhiteheadKlondike;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the Whitehead Klondike class.
 */
public class WhiteheadKlondikeTests {

  private WhiteheadKlondike game;
  private List<KlondikeCard> deck;

  /**
   * sets up new game before the tests begin and creates new deck.
   */
  @Before
  public void setUp() {
    game = new WhiteheadKlondike();
    deck = game.createNewDeck();
  }


  @Test
  public void testStartGameValid() {
    game.startGame(deck, false, 7, 3);
    assertEquals(7, game.getNumPiles());
    assertEquals(4, game.getNumFoundations());
    assertEquals(3, game.getNumDraw());
  }

  @Test(expected = IllegalStateException.class)
  public void testStartGameTwiceFails() {
    game.startGame(deck, false, 7, 3);
    game.startGame(deck, false, 7, 3);
  }

  @Test
  public void testAllCardsFaceUp() {
    game.startGame(deck, false, 7, 3);
    for (int i = 0; i < game.getNumPiles(); i++) {
      for (int j = 0; j < game.getPileHeight(i); j++) {
        assertTrue("Card should be visible", game.isCardVisible(i, j));
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileSamePileFails() {
    game.startGame(deck, false, 3, 1);
    game.movePile(0, 1, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDifferentColorFails() {
    game.startGame(deck, false, 2, 1);
    game.movePile(0, 1, 1);
  }

  @Test
  public void testMovePileValidSameColor() {
    game.startGame(deck, false, 2, 1);
    KlondikeCard top0 = game.getCardAt(0, game.getPileHeight(0) - 1);
    KlondikeCard top1 = game.getCardAt(1, game.getPileHeight(1) - 1);

    if (top1.getValue() == top0.getValue() - 1
        && ((top1.getSuit() == KlondikeCard.Suit.HEARTS
        || top1.getSuit() == KlondikeCard.Suit.DIAMONDS)
            == (top0.getSuit() == KlondikeCard.Suit.HEARTS
        || top0.getSuit() == KlondikeCard.Suit.DIAMONDS))) {
      game.movePile(1, 1, 0);
      assertEquals(1 + 1, game.getPileHeight(0));
    } else {
      // Skip if random deck order doesn't allow valid move
      System.out.println("Skipped valid move test because top cards not compatible.");
    }

  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDrawWhenEmptyFails() {
    game.startGame(deck, false, 7, 3);
    // Empty drawPile artificially
    while (!game.getDrawCards().isEmpty()) {
      game.discardDraw();
    }
    game.moveDraw(0);
  }

  @Test
  public void testMoveDrawValid() {
    List<KlondikeCard> customDeck = new ArrayList<>(deck);
    customDeck.set(0, new CardImpl(KlondikeCard.Suit.HEARTS, 7));

    int numPiles = 7;
    int numDraw = 3;
    int cardsInCascades = numPiles * (numPiles + 1) / 2; // 28
    customDeck.set(cardsInCascades, new CardImpl(KlondikeCard.Suit.HEARTS, 6));

    game.startGame(customDeck, false, numPiles, numDraw);

    int before = game.getPileHeight(0);
    game.moveDraw(0);
    assertEquals(before + 1, game.getPileHeight(0));
  }



  @Test
  public void testMoveToFoundationStartsWithAce() {
    game.startGame(deck, false, 1, 1);
    for (int i = 0; i < game.getNumPiles(); i++) {
      for (int j = 0; j < game.getPileHeight(i); j++) {
        if (game.getCardAt(i, j).getValue() == 1) {
          game.moveToFoundation(i, 0);
          assertEquals(1, game.getScore());
          return;
        }
      }

    }
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveToFoundationInvalidValueFails() {
    List<KlondikeCard> customDeck = new ArrayList<>(deck);
    customDeck.set(0, new CardImpl(KlondikeCard.Suit.HEARTS, 2));

    game.startGame(customDeck, false, 1, 1);
    game.moveToFoundation(0, 0);
  }


  @Test
  public void testDiscardDrawMovesCard() {
    game.startGame(deck, false, 7, 3);
    int before = game.getDrawCards().size(); // top numDraw cards
    int totalDrawPileBefore = game.getNumDrawCardsLeft(); // total cards in drawPile

    game.discardDraw();

    assertEquals(totalDrawPileBefore - 1, game.getNumDrawCardsLeft());

    int after = game.getDrawCards().size();
    assertTrue(after <= 3);
  }


  @Test
  public void testScoreInitiallyZero() {
    game.startGame(deck, false, 7, 3);
    assertEquals(0, game.getScore());
  }

  @Test
  public void testIsGameOverFalseInitially() {
    game.startGame(deck, false, 7, 3);
    assertFalse(game.isGameOver());
  }

  @Test
  public void testCreateNewDeckHas52UniqueCards() {
    assertEquals(52, deck.size());
    long unique = deck.stream()
        .map(c -> c.getSuit().toString() + c.getValue())
        .distinct()
        .count();
    assertEquals(52, unique);
  }

}
