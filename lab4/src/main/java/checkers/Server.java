package checkers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private int numberOfPlayers;

    public Server(ServerSocket serverSocket, int numberOfPlayers) {
        this.serverSocket = serverSocket;
        this.numberOfPlayers = numberOfPlayers;
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected!");
                ClientHandler clientHandler = new ClientHandler(socket, numberOfPlayers); // Pass numberOfPlayers
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            closeServerSocket();
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            int numberOfPlayers = 3; // Example: change dynamically as needed
            Server server = new Server(serverSocket, numberOfPlayers);
            System.out.println("Server is running...");
            server.startServer();
        } catch (IOException e) {
            System.err.println("Error starting the server: " + e.getMessage());
        }
    }
}
