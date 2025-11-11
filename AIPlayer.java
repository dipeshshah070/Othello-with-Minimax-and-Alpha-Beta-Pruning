import java.util.List;
/*Main AI for the othello game.
this class holds all the logic fot AI which includes
heuristic evaluations, Minimax and Alpha-Beta pruning
 */
public class AIPlayer {
    private static final int[][] Heu_Weights = {
            {120, -20, 20, 5, 5, 20, -20, 120},
            {-20, -40, -5, -5, -5, -5, -40, -20},
            {20, -5, 15, 3, 3, 15, -5, 20},
            {5, -5, 3, 3, 3, 3, -5, 5},
            {5, -5, 3, 3, 3, 3, -5, 5},
            {20, -5, 15, 3, 3, 15, -5, 20},
            {-20, -40, -5, -5, -5, -5, -40, -20},
            {120, -20, 20, 5, 5, 20, -20, 120}
    };
    private long statestExamined;  //counter to count for the debug mode
    private static final int SEARCH_DEPTH = 2; //how many moves to think ahead

    /*
     * The heuristic evaluation function.
     * It analyzes a board state and returns a score.
     * A high positive score is good for the 'aiPlayer'.
     * A high negative score is bad (good for the opponent).
     * return The integer score of the board.
     */
// This method looks at the board and give it a score. judging
    public int evaluate(Board board, Player aiPlayer) {
        Player opponent = aiPlayer.getOpponent();
        int aiScore = 0;
        int opponentScore = 0;

        //looping through the whole board
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                //helper
                Player piece = board.getPiece(r, c);

                if (piece == aiPlayer) {
                    aiScore += Heu_Weights[r][c];
                } else if (piece == opponent) {
                    opponentScore += Heu_Weights[r][c];
                }
            }
        }
        int positionalScore = aiScore - opponentScore;  //the score for AI position if its 0 then tie if its positive AI winning

        // number of moves ai and opponent hase
        int aiMoves = board.getValidMoves(aiPlayer).size();
        int opponentMoves = board.getValidMoves(opponent).size();
        int mobilityScore = aiMoves - opponentMoves; //positive is good for AI

        // disc/pieces count score
        int aiDiscs = board.calculateScore(aiPlayer);
        int opponentDiscs = board.calculateScore(opponent);
        int discScore = aiDiscs - opponentDiscs; //if it is positive better for AI

        //final weighted score
        int finalScore;
        int totalDiscs = aiDiscs + opponentDiscs;

        //different strategy for early/ mid-game and late game
        if (totalDiscs < 50) {
            //early/Midgame: focusing on mobility
            finalScore = (positionalScore * 10) + (mobilityScore * 200);
        } else {
            //late game: focus on maximizing disc cout
            finalScore = (positionalScore * 10) + (discScore * 1000);
        }
        return finalScore;
    }

    //this method finds and returns the best move that AI can find uisng Minimax or Alpha-Beta
    public Move findBestMove(Board board, Player aiPlayer, boolean useAlphaBeta, boolean debugMode) {
        this.statestExamined = 0;
        Move bestMoveFound = null;

        //Start the best score at very, very low number as AI is the maximizer
        //any legal move will be better than this
        int bestScore = Integer.MIN_VALUE; //lowest value posible as Ai is maximizer
        Player opponentPlayer = aiPlayer.getOpponent();
        //debug
        if (debugMode) {
            System.out.println("AI is thinking... (Depth: " + SEARCH_DEPTH + ")");
        }
        //looping through all of AI valid moves
        for (Move move : board.getValidMoves(aiPlayer)){
            //pretending to make the first move but dont make it
            Board newBoard = board.makeMove(move, aiPlayer);
            if (debugMode) {
                System.out.println("AI (Max) considering first-level move: " + move);
            }

            //calling minimax to see what the opponent will do

            int score;

            if (useAlphaBeta){
                //Calling the fast method
                //integer.MIN and MAX are initial alpha beta value
                //search depth - 1 because for opponent and isMaximizer false because the opponent is minimizer
                score = minimaxAlphaBeta(newBoard, SEARCH_DEPTH -1, Integer.MIN_VALUE, Integer.MAX_VALUE, false, aiPlayer, opponentPlayer, debugMode);
            }else {
               score  = minimax(newBoard, SEARCH_DEPTH -1, false, aiPlayer, opponentPlayer,debugMode);
            }
            if (debugMode){
                System.out.println("  -> AI's move " + move + " resulted in a final score of: " + score);
            }

            //chechking if this move score is the best one
            if (score > bestScore){
                bestScore = score;
                bestMoveFound = move;

            }
        }
        return bestMoveFound;
    }
    public long getStatestExamined(){
        return this.statestExamined;
    }

    //function that calls itself for solve smaller parts (recursive)
    //depth is how many more moves to look ahead
    public int minimax(Board board, int depth, boolean isMaximzingPlayer, Player aiPlayer, Player opponentPlayer, boolean debugMode) {
        this.statestExamined++;
        //if the game is over or we've thought deep enough depth = 0
        //we will return the score
        if (depth == 0 || board.isGameOver()) {
            int score = evaluate(board, aiPlayer); //calculated score
            //debug to show final heuristic value at the end of a sequence
            if (debugMode) {
                String indent = " ".repeat(SEARCH_DEPTH - depth);
                System.out.println(indent + " -> Leaf Node. Heuristic value: " + score);
            }
            return score;

        }
        List<Move> moves;
        if (isMaximzingPlayer){
            moves = board.getValidMoves(aiPlayer);
        }else{
            moves = board.getValidMoves(opponentPlayer);
        }
        if (moves.isEmpty()){
            return minimax(board, depth -1, !isMaximzingPlayer, aiPlayer, opponentPlayer, debugMode);
        }

        //Debug Print

        //AI's maximizers step
        if (isMaximzingPlayer) {
            int bestScore = Integer.MIN_VALUE;
            //trying every move
            for (Move move : moves) {
                String indent = " ".repeat(SEARCH_DEPTH - depth);
                if (debugMode){
                    System.out.println(indent + " Max is considering move: "+ move);
                }
                //pretend to make the move
                Board newBoard = board.makeMove(move, aiPlayer);

                //calling the same function to see what the Opponent will do in response.
                //we will do depth -1 and ismaximizing false
                int score = minimax(newBoard, depth - 1, false, aiPlayer, opponentPlayer, debugMode);
                if (debugMode){
                    System.out.println(indent + " -> Move "+ move+ " resulted in score: "+ score);
                }
                //best score that we found
                bestScore = Math.max(bestScore, score);
            }
            return bestScore;
        } else { //if isMaximizingPlayer is false
            int bestScore = Integer.MAX_VALUE;

            //try every possible move
            for (Move move : moves) {
                String indent = "  ".repeat(SEARCH_DEPTH - depth);
                if (debugMode) {
                    System.out.println(indent + "Min considering move: " + move);
                }
                Board newBoard = board.makeMove( move , opponentPlayer);
                //calling the same function to see what AI will do
                int score = minimax(newBoard, depth-1, true, aiPlayer,opponentPlayer, debugMode);
                if (debugMode) {
                    System.out.println(indent + "  -> Move " + move + " resulted in score: " + score);
                }
                bestScore = Math.min(bestScore, score);
            }
            return bestScore;
        }
    }
    //optimized version of Minimax with alpha beta pruning.
    public int minimaxAlphaBeta(Board board, int depth, int alpha, int beta, boolean isMaximzingPlayer, Player aiPlayer, Player opponentPlayer, boolean debugMode) {
        this.statestExamined++;
        //if the game is over or we've thought deep enough depth = 0
        //we will return the score
        if (depth == 0 || board.isGameOver()) {
            int score = evaluate(board, aiPlayer);
            if (debugMode) {
                String indent = "  ".repeat(SEARCH_DEPTH - depth);
                System.out.println(indent + "  -> Leaf Node. Heuristic Value: " + score);
            }
            return score;
        }
        List<Move> moves;
        if (isMaximzingPlayer) {
            moves = board.getValidMoves(aiPlayer);
        } else {
            moves = board.getValidMoves(opponentPlayer);
        }
        if (moves.isEmpty()){
            return minimaxAlphaBeta(board, depth -1, alpha, beta, !isMaximzingPlayer, aiPlayer, opponentPlayer, debugMode);
        }

        //Debug Print


        //AI's maximizers step
        if (isMaximzingPlayer) {
            int bestScore = Integer.MIN_VALUE;
            //trying every move
            for (Move move : moves) {
                String indent = "  ".repeat(SEARCH_DEPTH - depth);
                if (debugMode) System.out.println(indent + "Max considering move: " + move);
                //pretend to make the move
                Board newBoard = board.makeMove(move, aiPlayer);

                //calling the same function to see what thwhee Opponent will do in response.
                //we will do depth -1 and not ismaximizing
                int score = minimaxAlphaBeta(newBoard, depth - 1, alpha, beta,false, aiPlayer, opponentPlayer, debugMode);
                if (debugMode){
                    System.out.println(indent + "  -> Move " + move + " resulted in score: " + score);
                }
                //best score that we found
                bestScore = Math.max(bestScore, score);
                //checking if my opponent score is already worse than my best score
                alpha = Math.max(alpha, bestScore);
                if (beta <= alpha){
                    if (debugMode) {
                        System.out.println(indent + "  -> PRUNING (beta=" + beta + " <= alpha=" + alpha + ")");
                    }
                    break; //PRUNE,opponent beta has better move elsewhere
                }
            }
            return bestScore;
        } else { //if isMaximizingPlayer is false
            int bestScore = Integer.MAX_VALUE;

            //try every possible move
            for (Move move : moves) {
                String indent = "  ".repeat(SEARCH_DEPTH - depth);
                if (debugMode) {
                    System.out.println(indent + "Min considering move: " + move);
                }
                Board newBoard = board.makeMove( move , opponentPlayer);
                //calling the same function to see what AI will do
                int score = minimaxAlphaBeta(newBoard, depth-1, alpha, beta, true, aiPlayer,opponentPlayer, debugMode);
                if (debugMode){
                    System.out.println(indent + "  -> Move " + move + " resulted in score: " + score);
                }
                bestScore = Math.min(bestScore, score);
                beta = Math.min(beta, bestScore);
                if (beta <= alpha){
                    if (debugMode) System.out.println(indent + "  -> PRUNING (beta=" + beta + " <= alpha=" + alpha + ")");
                    break; // Prune
                }
            }
            return bestScore;
        }
    }
}
