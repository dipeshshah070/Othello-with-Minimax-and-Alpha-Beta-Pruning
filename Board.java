import java.util.ArrayList;
import java.util.List;
/*
* This class represents the board of the Othello game and its rules
* and checks, validate, and make move
* */
public class Board {
    public static int SIZE = 8;
    private Player[][] grid;
    private static final int[] DR = {-1,-1,-1,0,0,1,1,1};//direction for row
    private static final int[] DC = {-1,0,1,-1,1,-1,0,1}; // direction for cols

// creating a 8x8 board with the default four starting pieces
    public Board(){
        this.grid = new Player[SIZE][SIZE];

        for (int r = 0; r < SIZE; r++){
            for (int c = 0; c < SIZE; c++){
                this.grid[r][c] = Player.EMPTY;
            }
        }
//four starting pieces
        this.grid[3][3] = Player.WHITE;
        this.grid[4][4] = Player.WHITE;
        this.grid[4][3] = Player.BLACK;
        this.grid[3][4] = Player.BLACK;
    }
    //check if it is within the bound
    public boolean isWithinBounds (int r, int c){
        if (((r >= 0) && (r < SIZE)) && ((c>=0) && (c < SIZE))){
            return true;
        }else{
            return false;
        }
    }
    //checking a move to see what pieces it will flip if this move is executed
    public List<Move> getFlipsForMove (int r, int c, Player player){
        List<Move> allFlips = new ArrayList<>();
        Player opponent = player.getOpponent();

        //check all the directions
        for (int i=0; i < DR.length; i++){
            int dr = DR[i];
            int dc = DC[i];

            List<Move> pFlips = new ArrayList<>();

            int currentRow = r + dr;
            int currentCol = c + dc;

            while (isWithinBounds(currentRow, currentCol)){
                //if we find an opponent piece, add it as a potential flip
                if (this.grid[currentRow][currentCol] == opponent){
                    pFlips.add(new Move(currentRow, currentCol));
                }
                //if we find our own piece (players piece)
                else if (this.grid[currentRow][currentCol] == player) {
                    allFlips.addAll(pFlips);
                    break;
                }
                //if it is an empty square, the direction will be invalid
                else { //if this.grid[currentRow][currentCol] == Player.EMPTY
                    break;
                }
                currentRow += DR[i];
                currentCol += DC[i];
            }
        }
        return allFlips;
    }
//gets a list of all valid moves for the player( black or white).
    public List<Move> getValidMoves (Player player){
        List<Move> validMoves = new ArrayList<>();
        for(int r = 0; r < SIZE; r++){
            for (int c=0; c< SIZE; c++){
                if (this.grid[r][c] == Player.EMPTY){
                    List<Move>  flips = getFlipsForMove(r, c, player);
                    if (!flips.isEmpty()){
                        validMoves.add(new Move(r, c));
                    }
                }
            }
        }
        return validMoves;
    }
    //Prints the current state of the board
    public void show(){
        System.out.println("  A B C D E F G H");

        for (int r = 0; r < SIZE; r++){
            System.out.print((r+1)+ " ");
            for (int c = 0; c < SIZE; c++){
                switch (this.grid[r][c]){
                    case EMPTY:
                        System.out.print(". ");
                        break;
                    case WHITE:
                        System.out.print("W ");
                        break;
                    case BLACK:
                        System.out.print("B ");
                        break;
                }
            }
            System.out.println();
        }
    }

    //to calculate how many disc the player have in the board
    public int calculateScore(Player player){
        int score = 0;
        for (int r=0; r < grid.length; r++){
            for (int c=0; c < grid[0].length; c++){
                if (this.grid[r][c] == player) {
                    score++;
                }
            }
        }
        return score;
    }
//copy of the board for AI to think about the next moves without changing the original board
    public Board(Board otherBoard){
        this.grid = new Player[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++){
            for (int c = 0; c < SIZE; c++){
                this.grid[r][c] = otherBoard.grid[r][c];
            }
        }
    }
//Makes a move on the board, doesnot change the current board, instead returns a new board with the moves.
    public Board makeMove(Move move, Player player){
        //create a copy of the board
        Board newBoard = new Board(this);
        //adding the new pieces
        newBoard.grid[move.getRow()][move.getCol()] = player;

        //flip the pieces
        List<Move> flips = newBoard.getFlipsForMove(move.getRow(), move.getCol(), player);

        for (Move flipMove : flips) {
            newBoard.grid[flipMove.getRow()][flipMove.getCol()] = player;
        }
        //return the new board
        return newBoard;
    }
//to get the location of the piece.
    public Player getPiece(int r, int c){
        //return the location
        Player location = this.grid[r][c];
        return location;
    }

    public boolean isGameOver(){
        return getValidMoves(Player.BLACK).isEmpty() && getValidMoves(Player.WHITE).isEmpty();

    }

}
