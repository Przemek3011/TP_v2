package checkers.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final Socket socket;
    private final Scanner in;
    private final PrintWriter out;
    private final BoardGUI board;

    public Client (String serverAddress, int port)
            throws IOException {
        this.socket = new Socket(serverAddress, port);
        this.in = new Scanner(socket.getInputStream());
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.board = new BoardGUI(this);
    }

    public void sendMessageToServer(String message) {
        out.println(message);  
    }

    public String receiveMessageFromServer () throws IOException {
        return in.nextLine();  
    }

    public void closeConnection() throws IOException {
        socket.close();
    }

    public void launch() {
        new Thread(() -> {
            try {
                while (in.hasNextLine()) {
                    String line = receiveMessageFromServer();
                    if (line == null) break;

                    // TODO: new logic for reacting on server messages.
                    if (line.equalsIgnoreCase("make your move")) {
                        board.appendMessage("Your Turn! Make your move.");
                    } else if (line.startsWith("update:")) {
                        board.appendMessage("Board update:\n" + line.substring(7));
                    } else if (line.equalsIgnoreCase("game over")) {
                        board.appendMessage("Game is over. The results are: " + receiveMessageFromServer());
                    } else {
                        board.appendMessage(line); // Display any other messages from the server
                    }
                }
                closeConnection();
            } catch (IOException e) {
                board.appendMessage("Connection lost.");
            }
        }).start();
    }
}
