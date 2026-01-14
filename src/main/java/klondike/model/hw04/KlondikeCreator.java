package klondike.model.hw04;

import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.KlondikeCard;
import klondike.model.hw02.KlondikeModel;

/**
 * Factory for creating different Klondike game variants.
 */
public final class KlondikeCreator {

  private KlondikeCreator() {

  }

  /**
   * preventing instantiation.
   */
  public enum GameType { BASIC, WHITEHEAD }

  /**
   * Checks which game type is entered and then calls the proper class.
   * If it is not one of the expected types, it will return Unknown Game Type.
   *
   * @param type Game type entered by user either basic or whitehead.
   * @return calls the proper class or throws Illegal argument exception.
   */
  public static KlondikeModel<KlondikeCard> create(GameType type) {
    return switch (type) {
      case BASIC -> new BasicKlondike();
      case WHITEHEAD -> new WhiteheadKlondike();
    };
  }
}
