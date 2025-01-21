package checkers.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final int clientID;
    private final Socket socket;
    private final Server server;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

  // In ClientHandler constructor:
public ClientHandler(Socket socket, Server server, int clientID) {
    this.socket = socket;
    this.server = server;
    this.clientID = clientID;
    try {
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // This message goes to the client, prefixed with SERVER:
        sendMessage("SERVER: Connection established. Welcome, Player #" + clientID + "!");

        // Let all players know a new player joined
        server.broadcastMessage("SERVER: Player #" + clientID + " has entered the game.");
    } catch (IOException e) {
        closeEverything();
    }
}


    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                if (messageFromClient != null) {
                    server.handleMove(this, messageFromClient);
                }
            } catch (IOException e) {
                closeEverything();
                break;
            }
        }
    }

    public void sendMessage(String message) {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything();
        }
    }

    public int getClientID() {
        return clientID;
    }

    public void closeEverything(){
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
