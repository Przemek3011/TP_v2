package checkers.Game;

import java.util.List;

public class FastMovement implements MovementFactory {
    private FactoryBoard Board;
    private int[][] board;

    public FastMovement(FactoryBoard Board){
        this.Board = Board;
        this.board = Board.getBoard();
    }

   
    @Override
    public boolean isValidMove(int x1, int y1, List<int[]> moves) {
        if (moves == null || moves.isEmpty()) {
            return false;
        }

       
        if (moves.size() == 1) {
            int[] end = moves.get(0);
            
            if (isValidAdjacentMove(x1, y1, end) || isValidStandardJump(x1, y1, end[0], end[1])) {
                return true;
            }
            
            return false;
        }

        
        return isValidMultiJump(x1, y1, moves);
    }

    /**
     * Checks if x2,y2 is an empty adjacent cell (one step away).
     * For instance, in Chinese Checkers, adjacency can be 
     * diagonal or orth diagonal, etc.
     */
    public boolean isValidAdjacentMove(int x1, int y1, int[] end) {
        int x2 = end[0];
        int y2 = end[1];

       
        if (!isEmptyCell(x2, y2)) {
            return false;
        }

       
        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);
     
        return (dx <= 1 && dy <= 1 && (dx + dy != 0));
    }

    /**
     * Checks if we can jump exactly 2 squares diagonally or in some pattern,
     * with a piece to jump over in the middle.
     */
    public boolean isValidStandardJump(int x1, int y1, int x2, int y2) {
        // Must land on empty
        if (!isEmptyCell(x2, y2)) {
            return false;
        }

        int dx = x2 - x1;
        int dy = y2 - y1;

        // For a standard diagonal jump, maybe dx==2 && dy==2 (or -2).
        // If you allow vertical/horizontal jumps, adapt accordingly.
        if (Math.abs(dx) != 2 || Math.abs(dy) != 2) {
            return false;
        }

        int midX = (x1 + x2) / 2;
        int midY = (y1 + y2) / 2;

        if (isEmptyCell(midX, midY)) {
            return false;
        }

        return true;
    }

    /**
     * For "super jumps" that might skip more than 2 squares in a straight or diagonal line.
     * This is an advanced rule in some versions of Chinese checkers.
     */
    private boolean isValidSuperJump(int x1, int y1, int x2, int y2) {
       
        if (!isEmptyCell(x2, y2)) {
            return false;
        }

        int dx = x2 - x1;
        int dy = y2 - y1;


        if (!(dx == 0 || dy == 0 || Math.abs(dx) == Math.abs(dy))) {
            return false;
        }

    
        if (Math.abs(dx) % 2 != 0 || Math.abs(dy) % 2 != 0) {
            return false;
        }

        int midX = x1 + dx / 2;
        int midY = y1 + dy / 2;

        // The midpoint must have a piece (not empty)
        if (isEmptyCell(midX, midY)) {
            return false;
        }

     
        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        return true;
    }

    /**
     * If we have multiple steps (like x2,y2, x3,y3, x4,y4...), 
     * check each step in sequence from the previous step.
     */
    private boolean isValidMultiJump(int x1, int y1, List<int[]> jumps) {
        int currX = x1;
        int currY = y1;

        for (int[] step : jumps) {
            int nx = step[0];
            int ny = step[1];
           
            if (isValidStandardJump(currX, currY, nx, ny) 
                || isValidSuperJump(currX, currY, nx, ny)) 
            {
                
                currX = nx;
                currY = ny;
            } 
            else if (isValidAdjacentMove(currX, currY, step)) {
               
                currX = nx;
                currY = ny;
            }
            else {
                
                return false;
            }
        }
        return true; 
    }

    /** Helper: check if board[x][y] is empty. */
    private boolean isEmptyCell(int x, int y) {
        // check bounds if needed
        if (x < 0 || x >= board.length || y < 0 || y >= board[0].length) {
            return false;
        }
        // if '1' means empty cell:
        return (board[x][y] == 1);
    }
}