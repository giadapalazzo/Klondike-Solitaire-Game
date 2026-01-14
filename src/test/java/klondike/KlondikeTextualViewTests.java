package klondike;

import static org.junit.Assert.assertTrue;

import java.util.List;
import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.KlondikeCard;
import klondike.model.hw02.KlondikeModel;
import klondike.view.KlondikeTextualView;
import klondike.view.TextualView;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the textual representation of the Klondike game view.
 */
public class KlondikeTextualViewTests {

  private KlondikeModel<KlondikeCard> model;
  private TextualView view;

  /**
   * instantiates model and view before beginning the testing.
   */
  @Before
  public void setUp() {
    model = new BasicKlondike();
    view = new KlondikeTextualView(model);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullModelThrows() {
    new KlondikeTextualView(null);
  }

  @Test
  public void testToStringShowsDrawAndFoundation() {
    List<KlondikeCard> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    String out = view.toString();
    assertTrue(out.contains("Draw:"));
    assertTrue(out.contains("Foundation:"));
  }

  @Test
  public void testToStringContainsPileMarkers() {
    List<KlondikeCard> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    String out = view.toString();
    assertTrue(out.contains("?") || out.contains("X"));
  }
}
