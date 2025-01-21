package checkers.Game;


import java.util.List;

public interface MovementFactory {
    boolean isValidMove(int x1,int y1,List<int[]> moves);


}