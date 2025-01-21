package checkers.Game;

import java.util.List;

public class Game {
    private FactoryBoard board;
    private MovementFactory movement;
    private int numberOfPlayers;
    private int ID;
    int [][] Board;
    public Game(int numberOfPlayers,int ID, String variant) {
        this.numberOfPlayers = numberOfPlayers;
        this.ID=ID;
        board = new Board();
        board.createBoard(numberOfPlayers);

        // Create the movement logic for the initialized board
        movement = new Movement(board);

        // Adjust the board based on the number of players
        Board = board.getBoard();
    
    }

    /**
     * Validate and execute a move on the board.
     *
     * @param ID    The player's ID (not used in validation here but can be extended for player-specific logic).
     * @param x1    The starting x-coordinate.
     * @param y1    The starting y-coordinate.
     * @param x2    The ending x-coordinate.
     * @param y2    The ending y-coordinate.
     * @param moves A list of additional moves for multi-jump scenarios.
     * @return True if the move is valid and executed, false otherwise.
     */
    public boolean move(int ID, int x1, int y1, int x2, int y2, List<int[]> moves) {
        // Check if the coordinates are out of bounds
        if (isOutOfBounds(x1, y1) || isOutOfBounds(x2, y2)) {
            return false;
        }



        // Multi-jump validation
        boolean isValid = movement.isValidMove(x1, y1, moves);
        if (isValid) {
            for (int[] move : moves) {
                swap(x1, y1, move[0], move[1]);
                x1 = move[0];
                y1 = move[1];
            }
        }
        return isValid;
    }

    /**
     * Swap two positions on the board.
     *
     * @param x1 The starting x-coordinate.
     * @param y1 The starting y-coordinate.
     * @param x2 The ending x-coordinate.
     * @param y2 The ending y-coordinate.
     */
    private void swap(int x1, int y1, int x2, int y2) {
        board.swap(x1, y1, x2, y2, board.getBoard());
    }

    /**
     * Check if the given coordinates are out of bounds.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return True if out of bounds, false otherwise.
     */
    private boolean isOutOfBounds(int x, int y) {
        return x < 0 || x > 24 || y < 0 || y > 16;
    }

    /**
     * Get the current state of the board.
     *
     * @return A 2D array representing the board.
     */
    public int[][] getBoard() {
        return board.getBoard();
    }
    public boolean hasWon(int ID){
        switch(ID){
            case 2:
            if(Board[16][12]==2 && Board[15][11]==2 && Board[15][10]==2 &&Board[14][9]==2 && Board[14][11]==2 && Board[14][13]==2 && Board[13][9]==2 && Board[13][10]==2 && Board[13][12]==2 && Board[13][14]==2 )
            return true;
            break;
            case 3: 
            if(Board[12][0]==6 && Board[12][2]==6 && Board[12][4]==6 && Board[12][6]==6 && Board[11][1]==6 && Board[11][3]==6 && Board[11][5]==6 && Board[10][2]==6 && Board[10][4]==6 && Board[9][5]==6 )
            return true;
            break;
            case 4: 
            if(Board[4][0]==4 && Board[4][2]==4 && Board[4][4]==4 && Board[4][6]==4 && Board[5][1]==4 && Board[5][3]==4 && Board[5][5]==4 && Board[6][2]==4 && Board[6][4]==4 && Board[7][3]==4)
            return true;
            break;
            case 5:
            if(Board[0][12]==5 && Board[1][11]==5 && Board[1][10]==5 &&Board[2][9]==5 && Board[2][11]==5 && Board[2][13]==5 && Board[3][9]==5 && Board[3][10]==5 && Board[3][12]==5 && Board[3][14]==5 )
            return true;
            case 6:
            if(Board[4][24]==6 && Board[4][22]==6 && Board[4][20]==6 && Board[4][18]==6 && Board[5][23]==6 && Board[5][21]==6 && Board[5][19]==6 && Board[6][22]==6 && Board[6][20]==6 && Board[7][21]==6)
            return true;
            break;
            case 7:
            if(Board[12][24]==7 && Board[12][22]==7 && Board[12][20]==7 && Board[12][18]==7 && Board[11][23]==7 && Board[11][21]==7 && Board[11][19]==7 && Board[10][22]==7 && Board[10][20]==7 && Board[9][21]==7)
            return true;
            break;
            default: 
            return false;
        }
        return false;
    }
}
