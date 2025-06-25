package Chapter5;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Kelas WelcomeScreen menampilkan layar selamat datang untuk permainan Tic-Tac-Toe.
 * Ini akan muncul sebelum permainan utama dimulai.
 */
public class WelcomeScreen extends JFrame {
    private static final long serialVersionUID = 1L;

    public static final String WELCOME_TITLE = "Tic Tac Toe - Chapter 5";
    public static final Font FONT_TITLE = new Font("OCR A Extended", Font.BOLD, 30);
    public static final Font FONT_MESSAGE = new Font("OCR A Extended", Font.PLAIN, 16);
    public static final Color COLOR_BACKGROUND = new Color(200, 230, 250); // Light blue background
    public static final Color COLOR_BUTTON_BG = new Color(100, 180, 220); // Medium blue button
    public static final Color COLOR_TEXT = Color.BLACK;

    /**
     * Konstruktor untuk menyiapkan antarmuka pengguna layar selamat datang.
     */
    public WelcomeScreen() {
        super(WELCOME_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // Make the welcome screen not resizable

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(20, 20)); // Add some padding
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title Label
        JLabel titleLabel = new JLabel("<html><center>Selamat Datang di<br>Tic-Tac-Toe</center></html>", SwingConstants.CENTER);
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(COLOR_TEXT);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Message Label (optional, for "Chapter 5" context)
        JLabel messageLabel = new JLabel("By Group 14", SwingConstants.CENTER);
        messageLabel.setFont(FONT_MESSAGE);
        messageLabel.setForeground(COLOR_TEXT);
        panel.add(messageLabel, BorderLayout.CENTER);

        // Start Game Button
        JButton startGameButton = new JButton("Mulai Permainan");
        startGameButton.setFont(FONT_MESSAGE);
        startGameButton.setBackground(COLOR_BUTTON_BG);
        startGameButton.setForeground(Color.WHITE);
        startGameButton.setFocusPainted(false); // Remove focus border
        startGameButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Button padding
        startGameButton.setOpaque(true);
        startGameButton.setBorderPainted(false);
        startGameButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover

        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the welcome screen
                dispose();
                // Start the main game
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        JFrame frame = new JFrame(GameMain.TITLE);
                        frame.setContentPane(new GameMain());
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.pack();
                        frame.setLocationRelativeTo(null); // center the application window
                        frame.setVisible(true);
                    }
                });
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(COLOR_BACKGROUND);
        buttonPanel.add(startGameButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
        pack(); // Pack all components
        setLocationRelativeTo(null); // Center the window
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
