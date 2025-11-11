/*
* A record class to hold the (row, col) which is the coordinates of the move
* records automatically provides a constructor like .equals()
* */
public record Move(int row, int col) {
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
/*
* toString() will convert the row and column into easy readable format like D3,E2
* */
    public String toString() {
        // 'this.col' is 0-7 -> 'A'-'H'
        char colChar = (char)('A' + this.col);

        // 'this.row' is 0-7 -> 1-8
        int rowNum = this.row + 1;

        // This will return a string like "D6"
        return "" + colChar + rowNum;
    }
}
