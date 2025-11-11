//3 starts for the game
/*
* Representing the three posible states on othello black white or empty
* getOpponent() is a helper method that returns the opponent if it was black it will return white and vice versa
* */
public enum Player {
    EMPTY,
    BLACK,
    WHITE;
    public Player getOpponent(){
        if (this == BLACK){
            return WHITE;
        }else if(this == WHITE){
            return BLACK;
        }
        return EMPTY;
    }
}
