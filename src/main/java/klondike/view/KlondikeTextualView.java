package klondike.view;

import java.io.IOException;
import java.util.List;
import klondike.model.hw02.KlondikeModel;

/**
 * A textual view of the Klondike game model.
 * This class is responsible for rendering the current game state
 * into a human-readable form.
 */
public class KlondikeTextualView implements TextualView {

  private final KlondikeModel<?> model;
  private final Appendable ap;

  /**
   * Constructs a textual view for the given Klondike model.
   * Output is written to the standard output stream.
   *
   * @param model the KlondikeModel to be rendered
   * @throws IllegalArgumentException if the model is null
   */
  public KlondikeTextualView(KlondikeModel<?> model) {
    this(model, System.out);
  }

  /**
   * Constructs a textual view for the given Klondike model,
   * sending rendered output to the specified {@link Appendable}.
   *
   * @param model the KlondikeModel to be rendered
   * @param ap the Appendable to which the view output is written
   * @throws IllegalArgumentException if either argument is null
   */
  public KlondikeTextualView(KlondikeModel<?> model, Appendable ap) {
    if (model == null || ap == null) {
      throw new IllegalArgumentException("Model or Appendable cannot be null");
    }
    this.model = model;
    this.ap = ap;
  }

  @Override
  public void render() throws IOException {
    ap.append(this.toString()).append("\n");
  }


  /**
   * Renders the current game as a string.
   * The string includes the draw pile, foundation piles, and cascade piles.
   *
   * @return a textual representation of game state
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    List<?> draw = model.getDrawCards();

    sb.append("Draw: ");
    if (draw.isEmpty()) {
      sb.append("<none>");
    } else {
      for (int i = 0; i < draw.size(); i++) {
        sb.append(draw.get(i));
        if (i < draw.size() - 1) {
          sb.append(", ");
        }
      }
    }
    sb.append("\n");

    sb.append("Foundation: ");
    for (int i = 0; i < model.getNumFoundations(); i++) {
      var top = model.getCardAt(i);
      sb.append(top == null ? "<none>" : top);
      if (i < model.getNumFoundations() - 1) {
        sb.append(", ");
      }
    }
    sb.append("\n");

    for (int r = 0; r < model.getNumRows(); r++) {
      for (int p = 0; p < model.getNumPiles(); p++) {
        int h = model.getPileHeight(p);
        if (h == 0 && r == 0) {
          sb.append(" X ");
        } else if (r < h) {
          sb.append(model.isCardVisible(p, r)
              ? " " + model.getCardAt(p, r)
              : " ?");
        } else {
          sb.append("   ");
        }
        if (p < model.getNumPiles() - 1) {
          sb.append(" ");
        }
      }
      sb.append("\n");
    }

    return sb.toString();
  }
}
