package checkers.Game;

import java.util.List;

/**
 *  Game logic class for "Chaos" variant of the game.
 */
public class ChaosVariant implements Variant {

    private final int[][] board;

    public ChaosVariant(int[][] board) {
        this.board = board;
    }

    @Override
    public boolean isValidMove(Tile start, List<Tile> moves) {
        if (moves == null || moves.isEmpty()) {
            return false;
        }
        if (moves.size() == 1 && isValidAdjacentMove(start, moves.get(0))) {
            return true;
        }
        return isValidMultiJump(start, moves);
    }

    private boolean isValidStandardJump(Tile start, Tile end) {
        if (board[end.getX()][end.getY()] != 1) {
            return false;
        }
        int dx = Math.abs(start.getX() - end.getX());
        int dy = Math.abs(start.getY() - end.getY());

        int midX = (start.getX() + end.getX()) / 2;
        int midY = (start.getY() + end.getY()) / 2;
        if (dx == 2 && dy == 2 || dx == 0 && dy == 4) {
            return (board[midX][midY] != 0 && board[midX][midY] != 1);
        }
        return false;
    }

    private boolean isValidAdjacentMove(Tile start, Tile end) {
        if (board[end.getX()][end.getY()] != 1) {
            return false;
        }
        int dx = Math.abs(start.getX() - end.getX());
        int dy = Math.abs(start.getY() - end.getY());
        return (dx == 1 && dy == 1 || dx == 0 && dy == 2);
    }

    private boolean isValidMultiJump(Tile start, List<Tile> jumps) {
        if (jumps == null || jumps.isEmpty()) {
            return false;
        }
        Tile current = start;
        for (Tile next : jumps) {
            if (!isValidStandardJump(current, next)) {
                return false;
            }
            current = next;
        }
        return true;
    }
}

