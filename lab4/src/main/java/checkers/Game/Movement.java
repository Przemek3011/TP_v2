package checkers.Game;

import java.util.List;

public class Movement implements MovementFactory {
    private FactoryBoard Board;
    int[][] board;

    public Movement(FactoryBoard Board) {
        this.Board = Board;
        Board = new Board();
        board=Board.getBoard();
    }

    @Override
    public boolean isValidMove(int x1, int y1, List<int[]> moves) {
        if (moves == null || moves.isEmpty()) {
            return false;
        }
        if (moves.size() == 1 && isValidAdjacentMove(x1, y1, moves.get(0))) {
            return true;
        }
        return isValidMultiJump(x1, y1, moves);
    }

    public boolean isValidStandardJump(int x1, int y1, int x2, int y2) {
        if (board[x2] [y2] != 1) { 
            return false;
        }
        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);

        int midX = (x1 + x2) / 2;
        int midY = (y1 + y2) / 2;

        return dx == 2 && dy == 2 && board[midX] [midY] > 1;
    }

    public boolean isValidAdjacentMove(int x1, int y1, int[] end) {
        int x2 = end[0];
        int y2 = end[1];

        if (board[x2][y2] != 1) {
            return false;
        }

        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);
        return (dx == 1 && dy == 1) || (dx==0 && dy==2);
    }

    public boolean isValidMultiJump(int x1, int y1, List<int[]> jumps) {
        int currentX = x1;
        int currentY = y1;

        for (int[] next : jumps) {
            int nextX = next[0];
            int nextY = next[1];

            if (!isValidStandardJump(currentX, currentY, nextX, nextY)) {
                return false;
            }

            currentX = nextX;
            currentY = nextY;
        }
        return true;
    }
}
