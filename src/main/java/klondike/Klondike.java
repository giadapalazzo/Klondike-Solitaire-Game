package klondike;

import java.io.InputStreamReader;
import klondike.controller.KlondikeController;
import klondike.controller.KlondikeTextualController;
import klondike.model.hw02.KlondikeCard;
import klondike.model.hw02.KlondikeModel;
import klondike.model.hw04.KlondikeCreator;

/**
 * Command-line launcher for Klondike Solitaire variants.
 */
public final class Klondike {
  /**
   *    * Launches a Klondike Solitaire game with the chosen variant and parameters.
   *
   * @param args command-line arguments: [variant] [numPiles] [numDraw]
   * @throws IllegalArgumentException if no or invalid game type is provided
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      throw new IllegalArgumentException("You must specify a game type: basic or whitehead");

    }
    KlondikeCreator.GameType gameType;
    int numPiles = 7;
    int numDraw = 3;


    String gameTypeArg = args[0].toLowerCase();
    gameType = switch (gameTypeArg) {
      case "basic" -> KlondikeCreator.GameType.BASIC;
      case "whitehead" -> KlondikeCreator.GameType.WHITEHEAD;
      default -> throw new IllegalArgumentException("Unknown game type: " + gameTypeArg);
    };

    if (args.length > 1) {
      try {
        numPiles = Integer.parseInt(args[1]);
        if (numPiles < 1) {
          numPiles = 7;
        }
      } catch (NumberFormatException e) {
        numPiles = 7;
      }
    }

    if (args.length > 2) {
      try {
        numDraw = Integer.parseInt(args[2]);
        // If invalid value, keep default
        if (numDraw < 1) {
          numDraw = 3;
        }
      } catch (NumberFormatException e) {
        // Keep default value
        numDraw = 3;
      }
    }
    KlondikeModel<KlondikeCard> model = KlondikeCreator.create(gameType);
    KlondikeController controller = new KlondikeTextualController(
        new InputStreamReader(System.in), System.out);

    try {
      controller.playGame(model, model.createNewDeck(), false, numPiles, numDraw);
    } catch (IllegalStateException e) {
      System.err.println("Game error: " + e.getMessage());
    } catch (Exception e) {
      System.err.println("An unexpected error occurred");
      e.printStackTrace();
    }


  }



}


