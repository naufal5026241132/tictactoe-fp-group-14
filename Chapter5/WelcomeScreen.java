package Chapter5;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.geom.Point2D;
import java.net.URL; // Added for loading resources
import javax.sound.sampled.*; // Added for audio
import javax.swing.ImageIcon; // Added for loading image
import java.io.IOException; // Added import for IOException

/**
 * The WelcomeScreen class displays the welcome screen for the Tic-Tac-Toe game.
 * It appears before the main game starts, allowing game mode selection
 * with an animated GIF background and background audio. (Player name input removed)
 */
public class WelcomeScreen extends JFrame {
    private static final long serialVersionUID = 1L;

    public static final String WELCOME_TITLE = "Tic Tac Toe - Chapter 5";
    public static final Font FONT_TITLE = new Font("Monospaced", Font.BOLD, 40);
    public static final Font FONT_MESSAGE = new Font("Monospaced", Font.PLAIN, 20);
    public static final Font FONT_BUTTON = new Font("Monospaced", Font.BOLD, 22);
    // Colors from previous pixel-art theme
    public static final Color COLOR_BUTTON_BG = new Color(255, 215, 0); // Gold
    public static final Color COLOR_BUTTON_SELECTED_BG = new Color(218, 165, 32); // Darker Gold
    public static final Color COLOR_TEXT = new Color(50, 50, 50); // Dark grey - NOT USED FOR TITLE/MESSAGE
    public static final Color COLOR_BUTTON_BORDER = new Color(178, 34, 34); // Firebrick red

    private JButton pvPModeButton;
    private JButton pvCModeButton;
    private GameMode selectedGameMode; // To track the currently selected mode

    // Player name input fields (REMOVED)
    // private JTextField player1NameField;
    // private JTextField player2NameField;
    // private JTextField userNameField;

    // Panels to hold player name inputs based on mode (REMOVED)
    // private JPanel dynamicInputPanel;
    // private JPanel pvPNamesPanel;
    // private JPanel pvCNamesPanel;

    // Background image and audio
    private Image backgroundImage;
    private Clip backgroundAudioClip;

    // Custom JPanel for drawing the GIF background
    private class BackgroundPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        public BackgroundPanel() {
            setOpaque(false); // Make this panel transparent so background can be drawn
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                // Draw the GIF background scaled to fill the panel
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                // Fallback to solid color if image not loaded
                g.setColor(new Color(74, 182, 237)); // Use the old gradient start color as fallback
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    /**
     * Constructor to set up the welcome screen UI.
     */
    public WelcomeScreen() {
        super(WELCOME_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // Welcome screen is not resizable

        // Load background GIF
        try {
            URL imageUrl = getClass().getClassLoader().getResource("images/background.gif");
            if (imageUrl != null) {
                backgroundImage = new ImageIcon(imageUrl).getImage();
                System.out.println("background.gif loaded successfully!");
            } else {
                System.err.println("background.gif not found. Using fallback solid color background.");
            }
        } catch (Exception e) {
            System.err.println("Error loading background.gif: " + e.getMessage());
        }

        // Load background audio
        try {
            URL audioUrl = getClass().getClassLoader().getResource("audio/background.wav");
            if (audioUrl != null) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioUrl);
                backgroundAudioClip = AudioSystem.getClip();
                backgroundAudioClip.open(audioStream);
                playBackgroundAudio(); // Start playing on load
                System.out.println("background.wav loaded successfully!");
            } else {
                System.err.println("background.wav not found. No background audio will play.");
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error loading background.wav: " + e.getMessage());
        }

        BackgroundPanel mainPanel = new BackgroundPanel(); // Use custom BackgroundPanel
        mainPanel.setLayout(new BorderLayout(20, 20)); // Add padding
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40)); // Larger padding

        // Title Label
        JLabel titleLabel = new JLabel("<html><center>Welcome to<br>Tic-Tac-Toe</center></html>", SwingConstants.CENTER);
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(Color.WHITE); // Changed to WHITE
        titleLabel.setOpaque(false);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Message Label "BY GROUP 14 C"
        JLabel messageLabel = new JLabel("BY GROUP 14 C", SwingConstants.CENTER);
        messageLabel.setFont(FONT_MESSAGE);
        messageLabel.setForeground(Color.WHITE); // Changed to WHITE
        messageLabel.setOpaque(false);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        // Panel to hold messageLabel and modeSelectionPanel
        JPanel centerContentPanel = new JPanel(); // Renamed from headerContentPanel for clarity in this context
        centerContentPanel.setOpaque(false);
        centerContentPanel.setLayout(new BoxLayout(centerContentPanel, BoxLayout.Y_AXIS));
        centerContentPanel.add(messageLabel);
        centerContentPanel.add(Box.createRigidArea(new Dimension(0, 25))); // Spacing

        // Game Mode Selection Panel using JButtons
        JPanel modeSelectionPanel = new JPanel();
        modeSelectionPanel.setOpaque(false); // Make panel background transparent
        modeSelectionPanel.setLayout(new GridLayout(1, 2, 25, 0)); // 1 row, 2 columns, with horizontal gap

        // Player vs Player Button
        pvPModeButton = new JButton("Player vs Player");
        styleModeButton(pvPModeButton);
        pvPModeButton.addActionListener(e -> selectMode(GameMode.PLAYER_VS_PLAYER));
        modeSelectionPanel.add(pvPModeButton);

        // Player vs Computer Button
        pvCModeButton = new JButton("Player vs Computer");
        styleModeButton(pvCModeButton);
        pvCModeButton.addActionListener(e -> selectMode(GameMode.PLAYER_VS_COMPUTER));
        modeSelectionPanel.add(pvCModeButton);

        centerContentPanel.add(modeSelectionPanel); // Add mode selection panel directly to centerContentPanel
        centerContentPanel.add(Box.createRigidArea(new Dimension(0, 40))); // Add spacing below buttons

        // No dynamic input panel anymore, so directly add centerContentPanel to mainPanel
        mainPanel.add(centerContentPanel, BorderLayout.CENTER);

        // Set initial selection
        selectMode(GameMode.PLAYER_VS_PLAYER); // Default to Player vs Player

        // Start Game Button
        JButton startGameButton = new JButton("Start Game");
        startGameButton.setFont(FONT_BUTTON);
        startGameButton.setBackground(COLOR_BUTTON_BG);
        startGameButton.setForeground(Color.WHITE); // Changed to WHITE for better contrast on yellow button
        startGameButton.setFocusPainted(false);
        startGameButton.setBorder(new LineBorder(COLOR_BUTTON_BORDER, 3, true));
        startGameButton.setOpaque(true);
        startGameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startGameButton.setPreferredSize(new Dimension(250, 60)); // Larger start button size

        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Set the selected mode in GameMain
                GameMain.currentGameMode = selectedGameMode;

                // Player names are now default in GameMain, no user input
                // GameMain.player1Name = player1NameField.getText().trim(); (REMOVED)
                // GameMain.player2Name = player2NameField.getText().trim(); (REMOVED)
                // GameMain.userName = userNameField.getText().trim(); (REMOVED)

                stopBackgroundAudio(); // Stop audio when game starts
                dispose(); // Close the welcome screen

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

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Make panel background transparent
        buttonPanel.add(startGameButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel); // Add the main panel to the frame
        pack(); // Pack all components
        setLocationRelativeTo(null); // Center the window

        // Ensure audio stops if window is closed without starting game
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopBackgroundAudio();
            }
        });
    }

    /** Styles a mode selection button */
    private void styleModeButton(JButton button) {
        button.setFont(FONT_MESSAGE);
        button.setBackground(COLOR_BUTTON_BG);
        button.setForeground(Color.WHITE); // Changed to WHITE for better contrast
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(COLOR_BUTTON_BORDER, 2, true));
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(220, 60)); // Larger mode button size
    }

    /** Handles mode selection and updates button appearance. (No name input logic) */
    private void selectMode(GameMode mode) {
        selectedGameMode = mode;

        // Reset all buttons to default style
        pvPModeButton.setBackground(COLOR_BUTTON_BG);
        pvPModeButton.setBorder(new LineBorder(COLOR_BUTTON_BORDER, 2, true));
        pvPModeButton.setForeground(Color.WHITE); // Changed to WHITE

        pvCModeButton.setBackground(COLOR_BUTTON_BG);
        pvCModeButton.setBorder(new LineBorder(COLOR_BUTTON_BORDER, 2, true));
        pvCModeButton.setForeground(Color.WHITE); // Changed to WHITE

        // Apply selected style to the chosen button
        if (selectedGameMode == GameMode.PLAYER_VS_PLAYER) {
            pvPModeButton.setBackground(COLOR_BUTTON_SELECTED_BG);
            pvPModeButton.setBorder(new LineBorder(COLOR_BUTTON_SELECTED_BG.darker(), 3, true));
            pvPModeButton.setForeground(Color.WHITE); // Text color for selected button
        } else {
            pvCModeButton.setBackground(COLOR_BUTTON_SELECTED_BG);
            pvCModeButton.setBorder(new LineBorder(COLOR_BUTTON_SELECTED_BG.darker(), 3, true));
            pvCModeButton.setForeground(Color.WHITE); // Text color for selected button
        }
        // Input panel visibility logic (REMOVED)
    }

    /** Helper method to create styled JLabels (REMOVED as no longer needed for input fields)
     private JLabel createLabel(String text) {
     JLabel label = new JLabel(text, SwingConstants.CENTER);
     label.setFont(FONT_MESSAGE.deriveFont(Font.BOLD, 16));
     label.setForeground(COLOR_TEXT);
     label.setOpaque(false);
     label.setAlignmentX(Component.CENTER_ALIGNMENT);
     return label;
     }
     */

    /** Plays the background audio in a loop. */
    private void playBackgroundAudio() {
        if (backgroundAudioClip != null) {
            if (backgroundAudioClip.isRunning()) {
                backgroundAudioClip.stop();
            }
            backgroundAudioClip.setFramePosition(0); // Rewind to the beginning
            backgroundAudioClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop indefinitely
        }
    }

    /** Stops the background audio. */
    private void stopBackgroundAudio() {
        if (backgroundAudioClip != null && backgroundAudioClip.isRunning()) {
            backgroundAudioClip.stop();
            backgroundAudioClip.close(); // Release resources
        }
    }

    // You can uncomment this main method for independent testing of the WelcomeScreen
    /*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WelcomeScreen().setVisible(true);
            }
        });
    }
    */
}