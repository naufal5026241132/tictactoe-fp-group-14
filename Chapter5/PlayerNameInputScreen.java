package Chapter5;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder; // Added import for LineBorder

/**
 * A simple screen for inputting player names after selecting the game mode.
 * This class extends JDialog to be a modal window.
 */
public class PlayerNameInputScreen extends JDialog {
    private static final long serialVersionUID = 1L;

    // Fonts and Colors (can be customized to match pixel art theme if desired)
    public static final Font FONT_LABEL = new Font("Monospaced", Font.BOLD, 18);
    public static final Font FONT_FIELD = new Font("Monospaced", Font.PLAIN, 16);
    public static final Font FONT_BUTTON_PLAY = new Font("Monospaced", Font.BOLD, 20);
    public static final Color COLOR_BG_PANEL = new Color(220, 240, 255); // Light blue
    public static final Color COLOR_FIELD_BG = new Color(240, 248, 255); // Alice Blue
    public static final Color COLOR_FIELD_BORDER = new Color(178, 34, 34); // Firebrick red
    public static final Color COLOR_BUTTON_PLAY_BG = new Color(0, 150, 0); // Green for start
    public static final Color COLOR_BUTTON_PLAY_TEXT = Color.WHITE;

    private GameMode gameMode;
    private JTextField player1NameField;
    private JTextField player2NameField; // Only for PvP
    private JTextField userNameField;    // Only for PvC

    /**
     * Constructor for the PlayerNameInputScreen.
     * @param owner The parent JFrame (WelcomeScreen in this case).
     * @param mode The selected GameMode.
     */
    public PlayerNameInputScreen(JFrame owner, GameMode mode) {
        super(owner, "Enter Player Names", true); // true for modal dialog
        this.gameMode = mode;
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Vertical layout
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30)); // Padding
        mainPanel.setBackground(COLOR_BG_PANEL);

        // --- Title / Instruction Label ---
        JLabel instructionLabel = new JLabel("Enter Player Names", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(instructionLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer

        // --- Dynamic Input Fields Panel ---
        JPanel inputFieldsPanel = new JPanel();
        inputFieldsPanel.setOpaque(false); // Transparent background
        inputFieldsPanel.setLayout(new BoxLayout(inputFieldsPanel, BoxLayout.Y_AXIS)); // Vertical for fields
        inputFieldsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);


        if (gameMode == GameMode.PLAYER_VS_PLAYER) {
            player1NameField = createTextField("Player 1");
            player2NameField = createTextField("Player 2");

            inputFieldsPanel.add(createLabel("Player 1 Name (X):"));
            inputFieldsPanel.add(player1NameField);
            inputFieldsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            inputFieldsPanel.add(createLabel("Player 2 Name (O):"));
            inputFieldsPanel.add(player2NameField);

        } else { // GameMode.PLAYER_VS_COMPUTER
            userNameField = createTextField("You");

            inputFieldsPanel.add(createLabel("Your Name (X):"));
            inputFieldsPanel.add(userNameField);
        }

        mainPanel.add(inputFieldsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Spacer

        // --- Start Game Button ---
        JButton startGameButton = new JButton("PLAY GAME");
        startGameButton.setFont(FONT_BUTTON_PLAY);
        startGameButton.setBackground(COLOR_BUTTON_PLAY_BG);
        startGameButton.setForeground(COLOR_BUTTON_PLAY_TEXT);
        startGameButton.setFocusPainted(false);
        startGameButton.setBorder(new LineBorder(COLOR_FIELD_BORDER.darker(), 3, true)); // Darker border
        startGameButton.setOpaque(true);
        startGameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startGameButton.setPreferredSize(new Dimension(200, 50));
        startGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);


        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Collect names and set them in GameMain's static variables
                if (gameMode == GameMode.PLAYER_VS_PLAYER) {
                    GameMain.player1Name = player1NameField.getText().trim();
                    GameMain.player2Name = player2NameField.getText().trim();
                } else {
                    GameMain.userName = userNameField.getText().trim();
                }

                // If names are empty, use default
                if (GameMain.player1Name.isEmpty()) GameMain.player1Name = "Player 1";
                if (GameMain.player2Name.isEmpty()) GameMain.player2Name = "Player 2";
                if (GameMain.userName.isEmpty()) GameMain.userName = "You";

                // PERBAIKAN: Tambahkan baris berikut untuk mengatur currentGameMode di GameMain
                GameMain.currentGameMode = gameMode;

                dispose(); // Close this dialog

                // Launch the main game
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        JFrame frame = new JFrame(GameMain.TITLE);
                        frame.setContentPane(new GameMain());
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.pack();
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                    }
                });
            }
        });

        mainPanel.add(startGameButton);

        setContentPane(mainPanel);
        pack(); // Adjust window size to fit components
        setLocationRelativeTo(owner); // Center relative to WelcomeScreen
    }

    /** Helper method to create styled JLabels */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(FONT_LABEL);
        label.setForeground(Color.BLACK); // Labels typically black for readability
        label.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align labels in BoxLayout
        return label;
    }

    /** Helper method to create styled JTextFields */
    private JTextField createTextField(String defaultText) {
        JTextField field = new JTextField(defaultText);
        field.setFont(FONT_FIELD);
        field.setBackground(COLOR_FIELD_BG);
        field.setForeground(Color.BLACK); // Text in field typically black
        field.setMaximumSize(new Dimension(250, 35)); // Consistent size
        field.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align in BoxLayout
        field.setHorizontalAlignment(JTextField.CENTER); // Center text
        field.setBorder(BorderFactory.createLineBorder(COLOR_FIELD_BORDER, 2));
        return field;
    }
}