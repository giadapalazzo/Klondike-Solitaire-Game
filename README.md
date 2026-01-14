# Klondike Solitaire Game - README
Project Overview
A full-featured implementation of Klondike Solitaire in Java, supporting multiple game variants (Basic and Whitehead) using Model-View-Controller (MVC) architecture.
Game Variants
Basic Klondike

Traditional Klondike Solitaire rules
Alternating red/black card sequences in cascade piles
Only Kings can be placed in empty cascade piles
Only the top card in each cascade pile is visible

Whitehead Klondike

All cascade pile cards are dealt face-up
Same-color builds (red on red, black on black)
Multiple card moves require all cards to be the same suit
Any card value can be placed in empty cascade piles


Key Features
Core Game Mechanics

Move validation: Ensures all moves follow Klondike rules
Pile management: Handles cascade, foundation, draw, and discard piles
Scoring system: Tracks score based on foundation pile progress
Game over detection: Identifies when no more moves are possible

Design Patterns

Factory Pattern: KlondikeCreator creates appropriate game variants
MVC Architecture: Separates game logic, display, and user input
Interface-based design: Enables easy addition of new variants

Running the Game
Command-Line Arguments
bashjava klondike.Klondike [variant] [numPiles] [numDraw]
Parameters:

variant (required): basic or whitehead
numPiles (optional): Number of cascade piles (default: 7)
numDraw (optional): Number of visible draw cards (default: 3)

Examples:
bash# Basic game with defaults (7 piles, 3 draw cards)
java klondike.Klondike basic

# Whitehead variant with 8 piles and default draw
java klondike.Klondike whitehead 8

# Basic game with 9 piles and 5 draw cards
java klondike.Klondike basic 9 5
Design Decisions & Changes
Assignment 4 Refactoring
Minimal changes to original code:

Core BasicKlondike implementation remained largely unchanged
CascadePile interface was kept flexible enough to support both variants
No modifications to controller or view interfaces required

Key abstractions:

CascadePile interface allows different visibility and move validation rules
Factory pattern (KlondikeCreator) enables clean variant selection
Whitehead variant extends or modifies only the specific rules that differ

Code Reuse Strategy
To minimize duplication between BasicKlondike and WhiteheadKlondike:

Shared validation logic in helper methods
Common pile management operations
Consistent state management approach

Testing
Comprehensive JUnit tests cover:

✅ Valid and invalid deck configurations
✅ All move types (pile-to-pile, draw-to-pile, to-foundation)
✅ Edge cases (empty piles, game over conditions)
✅ Both Basic and Whitehead variant-specific rules
✅ Controller integration with both models

Technical Implementation
Key Classes
BasicKlondike

Implements standard Klondike rules
Manages four pile types: cascade, foundation, draw, discard
Validates moves according to traditional rules

WhiteheadKlondike

Overrides visibility, color matching, and empty pile rules
Maintains same interface as BasicKlondike
Implements same-suit multi-card move validation

KlondikeCreator

Factory class with GameType enum (BASIC, WHITEHEAD)
Static create() method returns appropriate model instance

CascadePileImpl

Handles card storage and visibility
Implements move validation specific to each variant
Supports multi-card pile transfers

Game Controls (Textual Interface)

mpp <source> <numCards> <dest> - Move cards between cascade piles
md <dest> - Move draw card to cascade pile
mpf <source> <foundation> - Move cascade card to foundation
mdf <foundation> - Move draw card to foundation
dd - Discard draw card (recycles when empty)
q - Quit game



Author
Giada Palazzo
Computer Science & Philosophy, Northeastern University

Note: This project demonstrates MVC architecture, factory design patterns, interface-based programming, and extensible object-oriented design principles.Claude is AI and can make mistakes. Please double-check responses.
