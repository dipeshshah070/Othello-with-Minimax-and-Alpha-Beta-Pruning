import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/*
This Main class contain main() for starting the program
handles user input and runs until the game is finished
 */
public class Main {
    private static Scanner scanner;
    public static boolean USE_ALPHA_BETA = false; //to toggle
    public static boolean DEBUG_MODE = true; //debug mode

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Othello");
        System.out.println("Select Game Mode:");
        System.out.println("1: Human vs Human");
        System.out.println("2. Human vs AI");
        System.out.println("Enter 1 or 2: ");

        String mode = scanner.nextLine();

        if (mode.equals("1")){
            System.out.println("Human VS Human ");
            startHumanVsHumanGame(scanner);
        }else{
            System.out.println("Human VS AI ");
            startHumanVsAIGame(scanner);
        }
        System.out.println("Thank you");
        scanner.close();

        }
//Human vs AI.
    private static void startHumanVsAIGame(Scanner scanner) {
        Board board = new Board();
        Player currentPlayer = Player.BLACK; //Black moves first



        AIPlayer ai = new AIPlayer();

        System.out.println("Do you want to play as Black or White? (B or W)");
        String playerChoice = scanner.nextLine().toUpperCase();

        Player humanPlayer;
        Player aiPlayer;

        if (playerChoice.equals("B")) {
            humanPlayer = Player.BLACK;
            aiPlayer = Player.WHITE;
            System.out.println("You chose Black, AI is White.");
        } else {
            humanPlayer = Player.WHITE;
            aiPlayer = Player.BLACK;
            System.out.println("You chose White, AI is Black.");
        }

        int score = ai.evaluate(board, aiPlayer);
        System.out.println("the score for aiPlayer on the starting of board is: " + score);

        //start the game loop
        while (true) {
            board.show(); //show the initial board or the current stage
            System.out.println("It is " + currentPlayer + "'s turn.");

            //we get all possible moves
            List<Move> validMoves = board.getValidMoves(currentPlayer);

            //checking the state (no moves or game over)
            if (validMoves.isEmpty()) {
                System.out.println("No valid moves. Turn forfeit");
                //check if the other player has a valid move
                Player opponent = currentPlayer.getOpponent();
                if (board.getValidMoves(opponent).isEmpty()) {
                    //no one can move so game ends
                    System.out.println("Game Over! No valid moves found");
                    break;
                }
                currentPlayer = opponent;
                continue;
            }
            //if the validMoves are not empty
            //print the valid moves:
            if (currentPlayer == humanPlayer) {
                System.out.println("Valid Moves are: ");
                List<String> moveString = new ArrayList<>();
                for (Move move : validMoves) {
                    char colChar = (char) ('A' + move.getCol());
                    int rowNum = move.getRow() + 1;
                    moveString.add("" + colChar + rowNum);
                }
                System.out.println(String.join(",", moveString));
                System.out.println("Enter you move: ");
                String input = scanner.nextLine().toUpperCase(); // d5 = D5

                //validate the input
                Move chosenMove = null;
                try {
                    int col = input.charAt(0) - 'A';
                    int row = input.charAt(1) - '1';
                    chosenMove = new Move(row, col);
                } catch (Exception e) {
                    System.out.println("invalid input format, Try again");
                    continue;
                }
                if (!validMoves.contains(chosenMove)) {
                    System.out.println("'" + input + "' is not a legal move. try again!");
                    continue;
                }

                //make the move
                //if the move is valid
                board = board.makeMove(chosenMove, currentPlayer);
            } else {
                System.out.println("AI is thinking ....");

                //call ai method for best move
                Move aiMove = ai.findBestMove(board, aiPlayer, USE_ALPHA_BETA, DEBUG_MODE);
                //make the ai move
                board = board.makeMove(aiMove, aiPlayer);
                //print what Ai did
                char colChar = (char) ('A' + aiMove.getCol());
                int rowNum = aiMove.getRow() + 1;
                System.out.println("AI playes: " + colChar + rowNum);
                System.out.println("AI Examined " + ai.getStatestExamined() + " states");
            }
            //swap the player turn
            currentPlayer = currentPlayer.getOpponent();

            System.out.println("\n---------------------------------------------------\n");
        }
        //Final Score
        System.out.println("\n---- Final Score----");
        board.show();//show the final board

        //using the claculate method
        int blackScore = board.calculateScore(Player.BLACK);
        int whiteScore = board.calculateScore(Player.WHITE);

        System.out.println("Black: " + blackScore);
        System.out.println("White: " + whiteScore);

        if (blackScore > whiteScore) {
            System.out.println("BLack wins!!");
        } else if (whiteScore > blackScore) {
            System.out.println("White wins!!");
        } else {
            System.out.println("Its a tie!!!");
        }
    }
//human vs human
    public static void startHumanVsHumanGame(Scanner scanner){
        Board board = new Board();
        Player currentPlayer = Player.BLACK; //Black moves first

        //start the game loop
        while (true) {
            board.show(); //show the initial board or the current stage
            System.out.println("It is " + currentPlayer + "'s turn.");

            //we get all possible moves
            List<Move> validMoves = board.getValidMoves(currentPlayer);

            //checking the state (no moves or game over)
            if (validMoves.isEmpty()) {
                System.out.println("No valid moves. Turn forfeit");
                //check if the other player has a valid move
                Player opponent = currentPlayer.getOpponent();
                if (board.getValidMoves(opponent).isEmpty()) {
                    //no one can move so game ends
                    System.out.println("Game Over! No valid moves found");
                    break;
                }
                currentPlayer = opponent;
                continue;
            }
            //if the validMoves are not empty
            //print the valid moves:
            System.out.println("Valid Moves are: ");
            List<String> moveString = new ArrayList<>();
            for (Move move : validMoves) {
                char colChar = (char) ('A' + move.getCol());
                int rowNum = move.getRow() + 1;
                moveString.add("" + colChar + rowNum);
            }
            System.out.println(String.join(",", moveString));
            System.out.println("Enter you move: ");
            String input = scanner.nextLine().toUpperCase(); // d5 = D5

            //validate the input
            Move chosenMove = null;
            try {
                int col = input.charAt(0) - 'A';
                int row = input.charAt(1) - '1';
                chosenMove = new Move(row, col);
            } catch (Exception e) {
                System.out.println("invalid input format, Try again");
                continue;
            }
            if (!validMoves.contains(chosenMove)) {
                System.out.println("'" + input + "' is not a legal move. try again!");
                continue;
            }

            //make the move
            //if the move is valid
            board = board.makeMove(chosenMove, currentPlayer);
            //swap the player turn
            currentPlayer = currentPlayer.getOpponent();

            System.out.println("\n---------------------------------------------------\n");
        }
        //Final Score
        System.out.println("\n---- Final Score----");
        board.show();//show the final board

        //using the claculate method
        int blackScore = board.calculateScore(Player.BLACK);
        int whiteScore = board.calculateScore(Player.WHITE);

        System.out.println("Black: " + blackScore);
        System.out.println("White: " + whiteScore);

        if (blackScore > whiteScore) {
            System.out.println("BLack wins!!");
        } else if (whiteScore > blackScore) {
            System.out.println("White wins!!");
        } else {
            System.out.println("Its a tie!!!");
        }

    }
}