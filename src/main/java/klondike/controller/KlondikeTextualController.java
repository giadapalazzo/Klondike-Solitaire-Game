package klondike.controller;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;
import klondike.view.KlondikeTextualView;

/**
 * A textual controller for a game of Klondike Solitaire.
 * This controller reads user input from a Readable source,
 * writes game output to an  Appendable, and interacts with a
 * KlondikeModel to play the game. It follows a command-based input format
 * allowing the user to move cards, discard draws, or quit the game
 */
public class KlondikeTextualController implements KlondikeController {
  private final Readable rd;
  private final Appendable ap;

  /**
   * Constructs a KlondikeTextualController that uses
   * the given Readable and Appendable for user input and output.
   *
   * @param rd the readable input source
   * @param ap the appendable output destination
   */
  public KlondikeTextualController(Readable rd, Appendable ap) {
    if (rd == null || ap == null) {
      throw new IllegalArgumentException("Null arguments not allowed.");
    }
    this.rd = rd;
    this.ap = ap;
  }


  @Override
  public <C extends Card> void playGame(KlondikeModel<C> model, List<C> deck, boolean shuffle,
                                        int numPiles, int numDraw) {
    if (model == null) {
      throw new IllegalArgumentException("Null model");
    }
    Scanner scan = new Scanner(rd);
    KlondikeTextualView view = new KlondikeTextualView(model, ap);
    try {
      try {
        model.startGame(deck, shuffle, numPiles, numDraw);
      } catch (IllegalArgumentException e) {
        throw new IllegalStateException("Invalid game parameters", e);
      }
      renderState(view, model);

      while (!model.isGameOver()) {
        if (!scan.hasNext()) {
          break;
        }
        String cmd = scan.next();
        if (cmd.equalsIgnoreCase("q")) {
          quitGame(model, view);
          return;
        }
        try {
          boolean moveMade = processCommand(cmd, scan, model, view);
          if (moveMade) {
            renderState(view, model);
          }
        } catch (IllegalStateException e) {
          if (e.getMessage() != null && e.getMessage().equals("User quit")) {
            return;
          }
          if (e.getMessage() != null && e.getMessage().equals("no input")) {
            throw e;
          }
          ap.append("Invalid move. Play again");
          if (e.getMessage() != null && !e.getMessage().isEmpty()) {
            ap.append(" ").append(e.getMessage());
          }
          ap.append("\n");
        } catch (IllegalArgumentException e) {
          ap.append("Invalid move. Play again");
          if (e.getMessage() != null && !e.getMessage().isEmpty()) {
            ap.append(" ").append(e.getMessage());
          }
          ap.append("\n");
        }
      }
      if (model.isGameOver()) {
        if (model.getScore() == deck.size()) {
          ap.append("You win!\n");
        } else {
          ap.append("Game over. Score: ").append(String.valueOf(model.getScore())).append("\n");
        }
      }

    } catch (IOException e) {
      throw new IllegalStateException("Transmission failed");
    }

  }

  /**
   * Helps render the board and keep score together.
   *
   * @param view the textual view for rendering the game
   * @param model the Klondike Model representing the current game state
   * @param <C> the type of Card used by the model
   * @throws IOException IOException if this fails
   */
  private <C extends Card> void renderState(KlondikeTextualView view, KlondikeModel<C> model)
      throws IOException {
    view.render();
    ap.append("Score: ").append(String.valueOf(model.getScore())).append("\n");
  }

  /**
   * Handles user quitting the game by outputting the final game state and score.
   *
   * @param model the Klondike Model representing the current game state
   * @param <C> the type of Card used by the model
   * @throws IOException if writing output fails
   */
  private <C extends Card> void quitGame(KlondikeModel<C> model,
                                         KlondikeTextualView view) throws IOException {
    ap.append("Game quit!\nState of game when quit:\n");
    view.render();
    ap.append("Score: ").append(String.valueOf(model.getScore())).append("\n");
  }
  /**
   * Processes a single command entered by the user, reading additional arguments
   * as needed and applying the corresponding action on the model.
   *
   * @param cmd   the initial command string entered by the user
   * @param scan  the scanner reading subsequent input values
   * @param m     the model to apply actions on
   * @throws IOException if writing output fails
   */

  private <C extends Card> boolean processCommand(String cmd, Scanner scan,
                                                  KlondikeModel<C> m, KlondikeTextualView view)
      throws IOException {
    try {
      switch (cmd) {
        case "mpp" -> {
          int src = nextInt(scan, m, view);
          int num = nextInt(scan, m, view);
          int dest = nextInt(scan, m, view);
          m.movePile(src - 1, num, dest - 1);
        }
        case "md" -> {
          int pile = nextInt(scan, m, view);
          m.moveDraw(pile - 1);
        }
        case "mpf" -> {
          int src = nextInt(scan, m, view);
          int fnd = nextInt(scan, m, view);
          m.moveToFoundation(src - 1, fnd - 1);
        }
        case "mdf" -> {
          int fnd = nextInt(scan, m, view);
          m.moveDrawToFoundation(fnd - 1);
        }
        case "dd" -> m.discardDraw();
        default -> {
          ap.append("Invalid move. Play again. Unknown command\n");
          return false;
        }
      }
      return true;
    } catch (IllegalStateException e) {
      if (e.getMessage() != null
          && (e.getMessage().equals("User quit") || e.getMessage().equals("no input"))) {
        throw e;
      }
      ap.append("Invalid move. Play again.");
      if (e.getMessage() != null && !e.getMessage().isEmpty()) {
        ap.append(" ").append(e.getMessage());
      }
      ap.append("\n");
      return false;
    } catch (IllegalArgumentException e) {
      ap.append("Invalid move. Play again.");
      if (e.getMessage() != null && !e.getMessage().isEmpty()) {
        ap.append(" ").append(e.getMessage());
      }
      ap.append("\n");
      return false;
    }
  }
  /**
   * Reads the next integer input from the user, retrying as needed until a valid
   * integer is entered or a quit command is detected.
   *
   * @param s the scanner used to read user input
   * @return the next valid integer entered by the user
   * @throws IllegalStateException if the user quits or input is exhausted
   */

  private int nextInt(Scanner s, KlondikeModel<?> model, KlondikeTextualView view)
      throws IOException {
    while (s.hasNext()) {
      String n = s.next();
      if (n.equalsIgnoreCase("q")) {
        quitGame(model, view);
        throw new IllegalStateException("User quit");
      }
      try {
        return Integer.parseInt(n);
      } catch (NumberFormatException e) {
        // Ignore invalid input and continue scanning for the next token
      }
    }
    throw new IllegalStateException("no input");
  }
}