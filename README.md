# Othello-with-Minimax-and-Alpha-Beta-Pruning
Othello AI Player (Java)

This is a complete, command-line implementation of the classic two-player game Othello (also known as Reversi), written in pure Java.

It features a fully functional game engine that supports both Human vs. Human and Human vs. AI game modes. The AI opponent is a sophisticated "thinking" agent that uses the Minimax search algorithm with Alpha-Beta Pruning to determine the best possible move.

This project was built to demonstrate core concepts in Object-Oriented Programming (OOP) and Artificial Intelligence, including game tree search, heuristics, and algorithm optimization.

Features

Complete Othello Game Engine: A robust Board class that correctly enforces all rules of Othello, including move validation and piece-flipping.

Dual Game Modes: A menu in Main.java allows the user to choose between a "Human vs. Human" game or a "Human vs. AI" game.

Minimax Algorithm: The AI uses the classic recursive Minimax algorithm (minimax method) to "think" several moves into the future, assuming the opponent will also play perfectly.

Alpha-Beta Pruning: A highly optimized version of Minimax (minimaxAlphaBeta method) that "prunes" (ignores) bad branches of the game tree, leading to a massive speed increase.

Advanced Heuristic Evaluation: The AI's "brain" (evaluate method) uses a hybrid strategy to judge the board:

Static Positional Map: Uses a 2D array (Heu_Weights) to assign high value to corners and edges, and negative values to dangerous squares.

Dynamic Calculation: Calculates Mobility (number of moves) and Coin Parity (piece count).

Game-Phase Strategy: The AI focuses on Mobility in the early/mid-game and switches to focusing on Disc Count in the late-game.

Configurable AI Toggles: The Main.java file contains easy-to-use static boolean toggles to:

USE_ALPHA_BETA: Switch between the fast (pruning) AI and the slow (classic Minimax) AI.

DEBUG_MODE: Turn on detailed console logs of the AI's thought process, including the moves it's considering, the heuristic values, and when it "prunes" a branch.

How to Run

This project is written in pure Java and has no external dependencies.

Clone or download the repository.

Open a terminal in the project's root directory (where the .java files are).

Compile all Java files:

javac *.java


Run the main program:

java Main


The program will start and prompt you to choose a game mode.

Project Structure

Main.java: The "Conductor." Runs the main game loop, handles user input, and manages game modes.

Board.java: The "Game Engine." Knows all the rules of Othello, how to find valid moves, and how to make moves.

AIPlayer.java: The "AI Brain." Contains the evaluate heuristic, the minimax algorithm, and the minimaxAlphaBeta optimization.

Player.java: A simple enum to represent the BLACK, WHITE, and EMPTY states.

Move.java: A simple record to store (row, col) coordinates.
