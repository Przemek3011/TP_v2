package checkers.Game;

import java.util.List;

public class SuperVariant implements Variant{

    private final int[][] board;

    public SuperVariant(int[][] board) {
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

    private boolean isValidMultiJump(Tile start, List<Tile> jumps) {
        if (jumps == null || jumps.isEmpty()) {
            return false;
        }
        Tile current = start;
        for (Tile next : jumps) {
            if (!isValidSuperJump(current, next)) {
                return false;
            }
            current = next;
        }
        return true;
    }

    private boolean isValidAdjacentMove(Tile start, Tile end) {
        if (board[end.getX()][end.getY()] != 1) {
            return false;
        }
        int dx = Math.abs(start.getX() - end.getX());
        int dy = Math.abs(start.getY() - end.getY());
        return (dx == 1 && dy == 1 || dx == 0 && dy == 2);
    }

    // TODO: check
    private boolean isValidSuperJump(Tile start, Tile end) {
        if (board[end.getX()][end.getY()] != 1) {
            return false;
        }

        int dx = end.getX() - start.getX();
        int dy = end.getY() - start.getY();

        // Is the move straight
        if (!(dx == 0 || Math.abs(dx) == Math.abs(dy))) {
            return false;
        }

        // Is the move symmetrical
        if (Math.abs(dx) % 2 != 0 || Math.abs(dy) % 2 != 0) {
            return false;
        }

        int midX = start.getX() + dx / 2;
        int midY = start.getY() + dy / 2;

        // Is there a piece
        if (board[midX][midY] == 0 || board[midX][midY] == 1) {
            return false;
        }

        // Are other tiles on the way free
        int steps = Math.max(Math.abs(dx), Math.abs(dy));
        for (int i = 1; i < steps; i++) {
            int currentX = start.getX() + (dx * i) / steps;
            int currentY = start.getY() + (dy * i) / steps;
            if (board[currentX][currentY] != 1) {
                return false;
            }
        }
        return true;
    }
}
