package klondike.controller;

import java.util.List;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;

/**
 *Represents a controller for a game of Klondike.
 * The controller is responsible for running the game by coordinating
 * input from the user and output to the view, while updating and querying
 * the game model as needed.
 */
public interface KlondikeController {
  /**
   *   Plays a new game of Klondike using the provided model and deck.
   *
   * @param model the KlondikeModel representing the game state
   * @param deck the list of cards to use for the game
   * @param shuffle whether the deck should be shuffled before the game starts
   * @param numPiles the number of cascade piles in the game
   * @param numDraw the number of draw cards visible at a time
   * @param <C> the type of card used in the game
   */
  <C extends Card> void playGame(KlondikeModel<C> model, List<C> deck,
                                 boolean shuffle, int numPiles, int numDraw);
}
