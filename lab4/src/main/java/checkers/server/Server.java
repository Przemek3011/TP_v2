package checkers.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private final ServerSocket serverSocket;

    // Now server holds number of players, received from UserApplication.
    private final int numOfPlayers;

    // For better encapsulation, clientHandlers are held by server as well.
    private final ArrayList<ClientHandler> clientHandlers;

    private static int clientCounter = 0;
    private int currentTurnIndex = 0; // Tracks whose turn it is

    public Server(ServerSocket serverSocket, int numOfPlayers) {
        this.serverSocket = serverSocket;
        this.numOfPlayers = numOfPlayers;
        this.clientHandlers = new ArrayList<>();
    }

    public void startServer(){
        try {
            while(!serverSocket.isClosed() && clientHandlers.size() < numOfPlayers){
                Socket socket = serverSocket.accept();
                System.out.println("A player connected.");
                ClientHandler clientHandler= new ClientHandler(socket, this, ++clientCounter);
                clientHandlers.add(clientHandler);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
            System.out.println("All players connected.");
            notifyCurrentPlayer();
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    public synchronized void handleMove(ClientHandler clientHandler, String move) {
        int playerIndex = clientHandlers.indexOf(clientHandler);
        if (playerIndex != currentTurnIndex) {
            clientHandler.sendMessage("SERVER: It's not your turn. Please wait.");
            return;
        }
        if (!isValidMove(move)) {
            clientHandler.sendMessage("SERVER: Invalid move. Try again.");
            return;
        }
        // Broadcast move to all players
        broadcastMessage("Player #" + clientHandler.getClientID() + " made a move: " + move);

        // Switch to the next player's turn
        switchTurn();
    }

    public synchronized void switchTurn() {
        currentTurnIndex = (currentTurnIndex + 1) % clientHandlers.size();
        notifyCurrentPlayer();
    }

    public synchronized void notifyCurrentPlayer() {
        ClientHandler currentPlayer = clientHandlers.get(currentTurnIndex);
        currentPlayer.sendMessage("SERVER: It's your turn. Make your move!");
    }

    private boolean isValidMove(String move) {
        // TODO: Add true move validation.
        String[] parts = move.split(" ");
        if (parts.length != 2) {
            return false;
        }
        try {
            Integer.parseInt(parts[0]);
            Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    // Server is responsible for broadcasting messages to all Handlers.
    public synchronized void broadcastMessage(String message) {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.sendMessage(message);
        }
    }

    public void closeServerSocket(){
        try {
            if(serverSocket != null) {
                serverSocket.close();
            }
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
