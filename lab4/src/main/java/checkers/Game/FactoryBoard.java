package checkers.Game;

public interface FactoryBoard {
    int[][] createBoard(int numberOfPlayers); 
    void swap(int x1,int y1,int x2,int y2,int[][] board);
    int[][] getBoard();   
} 
