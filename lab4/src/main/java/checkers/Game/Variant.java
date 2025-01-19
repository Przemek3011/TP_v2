package checkers.Game;

import java.util.List;

public interface Variant {
    boolean isValidMove(Tile start, List<Tile> moves);
}
