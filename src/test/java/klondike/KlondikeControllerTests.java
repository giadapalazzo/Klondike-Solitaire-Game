package klondike;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.StringReader;
import java.util.List;
import klondike.controller.KlondikeTextualController;
import org.junit.Test;

/**
 * Tests for the KlondikeTextualController using mock objects.
 * These tests verify that the controller correctly processes input,
 * interacts with the model, and handles errors.
 */
public class KlondikeControllerTests {

  /**
   * Helper to simulate a controller run with the given user input.
   *
   * @param input  simulated user input
   * @param log    log of model method calls
   * @param output captures what the controller writes to the Appendable
   * @return the controller’s output as a string
   */
  private String run(String input, StringBuilder log, StringBuilder output) {
    KlondikeTextualController controller =
        new KlondikeTextualController(new StringReader(input), output);
    MockKlondikeModel model = new MockKlondikeModel(log);
    controller.playGame(model, List.of(), false, 7, 3);
    return output.toString();
  }

  @Test
  public void testQuitImmediately() {
    StringBuilder log = new StringBuilder();
    StringBuilder output = new StringBuilder();
    String result = run("q", log, output);
    assertTrue(result.contains("Game quit!"));
    assertTrue(result.contains("Score: 42"));
    assertTrue(!log.toString().contains("move"));
  }

  @Test
  public void testMovePileCommand() {
    StringBuilder log = new StringBuilder();
    run("mpp 1 2 3 q", log, new StringBuilder());
    assertTrue(log.toString().contains("movePile 0 2 2"));
  }

  @Test
  public void testMoveDrawCommand() {
    StringBuilder log = new StringBuilder();
    run("md 4 q", log, new StringBuilder());
    assertTrue(log.toString().contains("moveDraw 3"));
  }

  @Test
  public void testMoveToFoundationCommand() {
    StringBuilder log = new StringBuilder();
    run("mpf 2 1 q", log, new StringBuilder());
    assertTrue(log.toString().contains("moveToFoundation 1 0"));
  }

  @Test
  public void testMoveDrawToFoundationCommand() {
    StringBuilder log = new StringBuilder();
    run("mdf 1 q", log, new StringBuilder());
    assertTrue(log.toString().contains("moveDrawToFoundation 0"));
  }

  @Test
  public void testDiscardDrawCommand() {
    StringBuilder log = new StringBuilder();
    run("dd q", log, new StringBuilder());
    assertTrue(log.toString().contains("discardDraw"));
  }

  @Test
  public void testInvalidCommandShowsError() {
    StringBuilder log = new StringBuilder();
    StringBuilder output = new StringBuilder();
    String result = run("blah q", log, output);
    assertTrue(result.contains("Invalid move. Play again."));
  }

  @Test(expected = IllegalStateException.class)
  public void testAppendableFailureThrows() {
    MockAppendable badOutput = new MockAppendable();
    KlondikeTextualController controller =
        new KlondikeTextualController(new StringReader("mpp 1 2 3"), badOutput);
    controller.playGame(new MockKlondikeModel(new StringBuilder()), List.of(), false, 7, 3);
  }

  @Test(expected = IllegalStateException.class)
  public void testReadableFailureThrows() {
    MockReadable badInput = new MockReadable();
    KlondikeTextualController controller =
        new KlondikeTextualController(badInput, new StringBuilder());
    controller.playGame(new MockKlondikeModel(new StringBuilder()), List.of(), false, 7, 3);
  }

  @Test
  public void testGameOverEndsLoop() {
    StringBuilder log = new StringBuilder();
    StringBuilder output = new StringBuilder();
    MockKlondikeModel model = new MockKlondikeModel(log);
    model.setGameOver(true);
    KlondikeTextualController controller =
        new KlondikeTextualController(new StringReader(""), output);
    controller.playGame(model, List.of(), false, 7, 3);
    assertTrue(output.toString().contains("Game over. Score:"));
  }

  @Test
  public void testNullArgumentsThrow() {
    try {
      new KlondikeTextualController(null, new StringBuilder());
      fail("Expected IllegalArgumentException for null Readable");
    } catch (IllegalArgumentException e) {
      // expected
    }

    try {
      new KlondikeTextualController(new StringReader(""), null);
      fail("Expected IllegalArgumentException for null Appendable");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }
}
