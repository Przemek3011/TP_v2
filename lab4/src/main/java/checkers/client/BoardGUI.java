package checkers.client;

import checkers.Game.*;
import javax.swing.*;
import java.awt.*;

public class BoardGUI extends JFrame {
    private JTextField inputField;
    private JButton sendButton;
    private int numberOfPlayers;
    private JPanel boardPanel;         // Panel for the game board
    private JTextArea messagesArea;    // Single area for ALL messages (server + client)
    private Client client;
    private Board board;              // Reference to the game board

    public BoardGUI(Client client, int numberOfPlayers) {
        this.numberOfPlayers=numberOfPlayers;
        this.client = client;
        this.board = new Board(); // If you have a Board class for game logic/state
        board.createBoard(numberOfPlayers);
        // Set up the frame
        setTitle("Checkers Game");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ---- Bottom panel for input (text field + send button) ----
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("Send");
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // ---- Main center panel (holds the board in center, messages area on right) ----
        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        // Board panel (17x25)
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(17, 25));
        initializeBoard();
        mainPanel.add(boardPanel, BorderLayout.CENTER);

        // Single text area on the right for all messages
        messagesArea = new JTextArea();
        messagesArea.setEditable(false);
        messagesArea.setLineWrap(true);
        messagesArea.setWrapStyleWord(true);
        JScrollPane messagesScrollPane = new JScrollPane(messagesArea);
        messagesScrollPane.setPreferredSize(new Dimension(300, 10)); 
        mainPanel.add(messagesScrollPane, BorderLayout.EAST);

        setVisible(true);

        // Send button action: send whatever is typed
        sendButton.addActionListener(e -> sendMessage());
        // Pressing ENTER in the inputField also triggers send
        inputField.addActionListener(e -> sendMessage());
    }

    // Fill the board with initial state
    private void initializeBoard() {
        int[][] boardData = board.getBoard(); // For example, a 17x25 int[][]
        for (int i = 0; i < boardData.length; i++) {
            for (int j = 0; j < boardData[i].length; j++) {
                JPanel cell = new JPanel();
                cell.setBackground(getColorForCell(boardData[i][j]));
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                boardPanel.add(cell);
            }
        }
    }

    // Update board after receiving new data
    public void updateBoard(int[][] newBoard) {
        boardPanel.removeAll();
        for (int i = 0; i < newBoard.length; i++) {
            for (int j = 0; j < newBoard[i].length; j++) {
                JPanel cell = new JPanel();
                cell.setBackground(getColorForCell(newBoard[i][j]));
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                boardPanel.add(cell);
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    // Append ALL messages (whether server broadcast or chat) to this single area on the right
    public void appendMessage(String message) {
        messagesArea.append(message + "\n");
        messagesArea.setCaretPosition(messagesArea.getDocument().getLength());
    }

    // Helper to pick a color based on a cell value
    private Color getColorForCell(int value) {
        switch (value) {
            case 1: return Color.WHITE;  // Example: empty
            case 2: return Color.RED;    
            case 3: return Color.BLUE;   
            case 4: return Color.GREEN;  
            case 5: return Color.YELLOW; 
            case 6: return Color.ORANGE; 
            case 7: return Color.PINK;   
            default: return Color.LIGHT_GRAY; 
        }
    }

    // Sends the user's typed message to the server
    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            client.sendMessageToServer(message);
            inputField.setText("");
        }
    }
}
