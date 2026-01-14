package klondike;

import java.io.IOException;
import klondike.view.TextualView;


/**
 * A mock view that records when render() is called.
 */
public class MockView implements TextualView {
  private final StringBuilder log;

  /**
   * constructs the String builder log.
   *
   * @param log string builder used to construct klondike.MockView/
   */
  public MockView(StringBuilder log) {
    this.log = log;
  }

  @Override
  public void render() throws IOException {
    log.append("render called\n");
  }
}