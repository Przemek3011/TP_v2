package checkers.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class ServerApplication {

    private static int initGame() {
        System.out.println("Hello! Welcome to Trylma!");
        Scanner scanner = new Scanner(System.in);
        while(true) {
            try {
                System.out.println("Enter number of players: 2, 3, 4 or 6.");
                int number = Integer.parseInt(scanner.nextLine());
                if (number != 2 && number != 3 && number != 4 && number != 6) {
                    System.out.println("This number is wrong.");
                } else {
                    return number;
                }
            } catch (NumberFormatException e) {
                System.out.println("Input is not a number.");
            }
        }
    }

    public static void main(String[] args) {
        int numberOfPlayers = initGame();
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            Server server = new Server(serverSocket,numberOfPlayers);
            server.startServer();
        } catch (IOException e) {
            System.err.println("Error starting the server: " + e.getMessage());
        }
    }
}
