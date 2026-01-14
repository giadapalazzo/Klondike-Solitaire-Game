# template-assignment-handout

No changes were made to my preexisting classes in the klondike package.
In the model package two classes were made the KlondikeCreator and WhiteheadKlondike. WhiteheadKlondike 
implements KlondikeModel<KlondikeCard> interface and uses simpler data structures than BasicKlondike.

KlondikeCreator is a factory class for creating game variants.
Contains GameType enum with values: BASIC, WHITEHEAD
Provides static factory method create(GameType type) that returns appropriate model instance

Another class was added in the klondike package. Klondike is the main class with command-line argument parsing
Accepts game type (basic/whitehead) and optional parameters (numPiles, numDraw)
Creates appropriate model using KlondikeCreator
