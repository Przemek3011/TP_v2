package checkers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private static int currentTurnIndex = 0; // Tracks whose turn it is
    private static int requiredPlayers = 2; // Modify based on your game setup
    private static boolean isGameReady = false;

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    private static int clientCounter = 0;
    private int clientID;
    public ClientHandler(Socket socket, int numberOfPlayers) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            synchronized (ClientHandler.class) {
                clientCounter++;
                this.clientID = clientCounter;
                this.clientUsername = "Client " + clientCounter;

                // Set requiredPlayers dynamically based on the first instance
                if (clientHandlers.isEmpty()) {
                    requiredPlayers = numberOfPlayers;
                }

                clientHandlers.add(this);
                broadcastMessage("SERVER: " + clientUsername + " has entered the game");

                // Check if the required number of players have joined
                if (clientHandlers.size() == requiredPlayers) {
                    isGameReady = true;
                    broadcastMessage("SERVER: All players have joined. The game is ready to start!");

                    // Notify the first player
                    notifyCurrentPlayer();
                }
            }

            bufferedWriter.write("SERVER: Connection established. Welcome, " + clientUsername + "!");
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();

                synchronized (ClientHandler.class) {
                    // Check if the game is ready
                    if (!isGameReady) {
                        bufferedWriter.write("SERVER: Waiting for other players to join.");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        continue;
                    }

                    // Check if it's this player's turn
                    if (currentTurnIndex != clientHandlers.indexOf(this)) {
                        bufferedWriter.write("SERVER: It's not your turn. Please wait.");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        continue;
                    }

                    // Validate and process the move
                   
                        broadcastMessage(clientUsername + " made a move: " + messageFromClient);
                        switchTurn(); // Update to the next player's turn
                
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    private void switchTurn() {
        synchronized (ClientHandler.class) {
            // Move to the next player
            currentTurnIndex = (currentTurnIndex + 1) % clientHandlers.size();

            // Notify the next player
            notifyCurrentPlayer();
        }
    }

    private void notifyCurrentPlayer() {
        try {
            ClientHandler currentPlayer = clientHandlers.get(currentTurnIndex);
            currentPlayer.bufferedWriter.write("SERVER: It's your turn. Make your move!");
            currentPlayer.bufferedWriter.newLine();
            currentPlayer.bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    private boolean isValidMove(String messageFromClient) {
        // Add validation logic for moves here
        // For example, check if the move follows game rules or is in the correct format
        return isValidTwoNumbers(messageFromClient);
    }

    public boolean isValidTwoNumbers(String messageFromClient) {
        String[] parts = messageFromClient.split(" ");

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

    public void broadcastMessage(String messageToSend) {
        synchronized (ClientHandler.class) {
            for (ClientHandler clientHandler : clientHandlers) {
                try {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                } catch (IOException e) {
                    closeEverything(clientHandler.socket, clientHandler.bufferedReader, clientHandler.bufferedWriter);
                }
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " has left");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
