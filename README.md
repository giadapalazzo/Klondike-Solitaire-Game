# Klondike Solitaire Game

Full-featured Klondike Solitaire implementation in Java with multiple game variants using MVC architecture.

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![License](https://img.shields.io/badge/License-Academic-blue?style=flat-square)

## 🎮 Features

- **Multiple Game Variants**: Play Basic or Whitehead Klondike
- **MVC Architecture**: Clean separation of game logic, display, and user input
- **Factory Pattern**: Extensible design for adding new variants
- **Command-Line Interface**: Play directly from your terminal
- **Comprehensive Testing**: JUnit test suite covering all game mechanics

## 🎯 Game Variants

### Basic Klondike
Traditional rules with alternating red/black sequences and Kings-only empty piles.

### Whitehead Klondike
All cards face-up, same-color builds, suit-matching for multi-card moves.

## 🚀 Quick Start
```bash
# Clone the repository
git clone https://github.com/giadapalazzo/Klondike-Solitaire-Game.git

# Compile the project
javac klondike/Klondike.java

# Run basic game (7 piles, 3 draw cards)
java klondike.Klondike basic

# Run whitehead variant with custom settings
java klondike.Klondike whitehead 8 5
```

## 📖 Usage
```bash
java klondike.Klondike [variant] [numPiles] [numDraw]
```

**Examples:**
- `java klondike.Klondike basic` - Start basic game with defaults
- `java klondike.Klondike whitehead 9` - Whitehead with 9 piles
- `java klondike.Klondike basic 7 5` - Basic with 7 piles, 5 draw cards

## 🎮 Game Controls

| Command | Action |
|---------|--------|
| `mpp <src> <num> <dest>` | Move cards between piles |
| `md <dest>` | Move draw card to pile |
| `mpf <src> <foundation>` | Move to foundation |
| `mdf <foundation>` | Move draw to foundation |
| `dd` | Discard/recycle draw card |
| `q` | Quit game |

## 🏗️ Architecture
```
Model (Game Logic) ↔ Controller (Input Handler) ↔ View (Display)
```

- **Model**: `BasicKlondike`, `WhiteheadKlondike` implementing `KlondikeModel`
- **View**: `KlondikeTextualView` for text-based display
- **Controller**: `KlondikeTextualController` for input processing
- **Factory**: `KlondikeCreator` for variant instantiation

## 🧪 Testing

Run the comprehensive JUnit test suite:
```bash
# Tests cover all game mechanics, edge cases, and variant-specific rules
javac -cp .:junit-4.13.2.jar klondike/test/*.java
java -cp .:junit-4.13.2.jar org.junit.runner.JUnitCore klondike.test.TestSuite
```

## 📚 What I Learned

- **Design Patterns**: Factory pattern for extensible game variants
- **MVC Architecture**: Separation of concerns in game design
- **Interface Design**: Creating flexible, reusable abstractions
- **Code Reuse**: Minimizing duplication across similar implementations
- **Test-Driven Development**: Comprehensive unit testing

## 🎓 Academic Context

Developed for **CS 3500: Object-Oriented Design** at Northeastern University  
Fall 2025 | Assignment 4: Model Variants

## 👤 Author

**Giada Palazzo**  
Computer Science & Philosophy @ Northeastern University

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue?style=flat-square&logo=linkedin)](https://www.linkedin.com/in/giada-palazzo-5581b42a9/)
[![Email](https://img.shields.io/badge/Email-Contact-red?style=flat-square&logo=gmail)](mailto:palazzo.g@northeastern.edu)

---

⭐ Star this repo if you found it helpful!
