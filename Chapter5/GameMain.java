package Chapter5;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random; // For AI random moves
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import javax.sound.sampled.*; // Sound imports are already here

/**
 * Tic-Tac-Toe: Two-player Graphic version with better OO design.
 * The Board and Cell classes are separated in their own classes.
 */
public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L; // to prevent serializable warning

    // Define named constants for the drawing graphics
    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_CROSS = new Color(239, 105, 80);  // Red #EF6950
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225); // Blue #409AE1
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    // Define game objects
    private Board board;         // the game board
    private State currentState;  // the current state of the game
    private Seed currentPlayer;  // the current player
    private JLabel statusBar;    // for displaying status message

    // New: Game Mode and Random for AI
    public static GameMode currentGameMode = GameMode.PLAYER_VS_PLAYER; // Default mode, can be set by WelcomeScreen
    public static String player1Name = "Player 1"; // Default name Player 1
    public static String player2Name = "Player 2"; // Default name Player 2
    public static String userName = "You"; // Default name for player vs computer
    public static String computerName = "Computer"; // Default computer name

    private Random random;
    private static final int AI_MOVE_DELAY_MS = 800; // Delay for AI move in milliseconds

    /** Constructor to setup the UI and game components */
    public GameMain() {
        random = new Random(); // Initialize random for AI

        // This JPanel fires MouseEvent
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {  // mouse-clicked handler
                int mouseX = e.getX();
                int mouseY = e.getY();
                // Get the row and column clicked
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE; // Corrected: removed extra / e.getY()

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {
                        // Player's move
                        makeMove(row, col);

                        // If in Player vs Computer mode and game is still playing, trigger computer's move
                        if (currentGameMode == GameMode.PLAYER_VS_COMPUTER && currentState == State.PLAYING) {
                            // Use a Swing Timer to introduce a delay for the AI's move
                            Timer timer = new Timer(AI_MOVE_DELAY_MS, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent arg0) {
                                    performComputerMove();
                                    repaint(); // Repaint after computer's move
                                    ((Timer)arg0.getSource()).stop(); // Stop the timer after one execution
                                }
                            });
                            timer.setRepeats(false); // Ensure it only runs once
                            timer.start(); // Start the timer
                        }
                    }
                } else {        // game over
                    newGame();  // restart the game
                    SoundEffect.EAT_FOOD.play(); // Play a sound for starting new game
                }
                // Refresh the drawing canvas
                repaint();  // Callback paintComponent().
            }
        });

        // Setup the status bar (JLabel) to display status message
        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        super.setLayout(new BorderLayout());
        super.add(statusBar, BorderLayout.PAGE_END); // same as SOUTH
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        // account for statusBar in height
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        // Set up Game
        initGame();
        newGame();
    }

    /** Handles making a move for the current player at the given row and column. */
    private void makeMove(int row, int col) {
        currentState = board.stepGame(currentPlayer, row, col);

        // Play appropriate sound clip after the move
        if (currentState == State.PLAYING) {
            SoundEffect.EAT_FOOD.play();
        } else {
            SoundEffect.DIE.play(); // Game over (win or draw)
        }

        // Switch player
        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
    }

    /** Performs the computer's move (simple AI). */
    private void performComputerMove() {
        // Hanya bergerak jika giliran komputer dan game masih berjalan
        // PERBAIKAN: Ubah kondisi currentPlayer != Seed.NOUGHT menjadi currentPlayer == Seed.CROSS
        if (currentState != State.PLAYING || currentPlayer == Seed.CROSS) {
            return;
        }

        int bestRow = -1, bestCol = -1;

        // Try to win or block opponent
        for (int i = 0; i < 2; i++) { // i=0 for current player (computer), i=1 for opponent
            Seed checkSeed = (i == 0) ? currentPlayer : (currentPlayer == Seed.CROSS ? Seed.NOUGHT : Seed.CROSS);
            for (int row = 0; row < Board.ROWS; row++) {
                for (int col = 0; col < Board.COLS; col++) {
                    if (board.cells[row][col].content == Seed.NO_SEED) {
                        board.cells[row][col].content = checkSeed; // Temporarily make a move
                        if (board.hasWon(checkSeed, row, col)) {
                            bestRow = row;
                            bestCol = col;
                            board.cells[row][col].content = Seed.NO_SEED; // Undo temp move
                            makeMove(bestRow, bestCol); // Make the actual move
                            return; // Move found, exit
                        }
                        board.cells[row][col].content = Seed.NO_SEED; // Undo temp move
                    }
                }
            }
        }

        // If no winning/blocking move, try to take the center
        if (bestRow == -1 && board.cells[1][1].content == Seed.NO_SEED) {
            bestRow = 1;
            bestCol = 1;
        }
        // If center is taken, try a corner
        else {
            int[][] corners = {{0, 0}, {0, 2}, {2, 0}, {2, 2}};
            List<Point> availableCorners = new ArrayList<>();
            for (int[] corner : corners) {
                if (board.cells[corner[0]][corner[1]].content == Seed.NO_SEED) {
                    availableCorners.add(new Point(corner[0], corner[1]));
                }
            }
            if (!availableCorners.isEmpty()) {
                Point move = availableCorners.get(random.nextInt(availableCorners.size()));
                bestRow = move.x;
                bestCol = move.y;
            }
        }

        // If no strategic move, take any available cell
        if (bestRow == -1) {
            List<Point> emptyCells = new ArrayList<>();
            for (int row = 0; row < Board.ROWS; row++) {
                for (int col = 0; col < Board.COLS; col++) {
                    if (board.cells[row][col].content == Seed.NO_SEED) {
                        emptyCells.add(new Point(row, col));
                    }
                }
            }
            if (!emptyCells.isEmpty()) {
                Point move = emptyCells.get(random.nextInt(emptyCells.size()));
                bestRow = move.x;
                bestCol = move.y;
            }
        }

        // Make the chosen move (if a valid move was found)
        if (bestRow != -1) {
            makeMove(bestRow, bestCol);
        }
    }


    /** Initialize the game (run once) */
    public void initGame() {
        board = new Board();  // allocate the game-board
    }

    /** Reset the game-board contents and the current-state, ready for new game */
    public void newGame() {
        board.newGame(); // Use Board's newGame method to clear cells
        currentPlayer = Seed.CROSS;    // 'X' plays first
        currentState = State.PLAYING;  // ready to play

        // If computer is the first player, make its move
        if (currentGameMode == GameMode.PLAYER_VS_COMPUTER && currentPlayer == Seed.NOUGHT) {
            // Introduce delay for AI's first move as well
            Timer timer = new Timer(AI_MOVE_DELAY_MS, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    performComputerMove();
                    repaint();
                    ((Timer)arg0.getSource()).stop();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    /** Custom painting codes on this JPanel */
    @Override
    public void paintComponent(Graphics g) {  // Callback via repaint()
        super.paintComponent(g);
        setBackground(COLOR_BG); // set its background color

        board.paint(g);  // ask the game board to paint itself

        // Print status-bar message
        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            String turnText;
            if (currentGameMode == GameMode.PLAYER_VS_COMPUTER) {
                turnText = (currentPlayer == Seed.CROSS) ? "It's " + userName + "'s Turn (X)" : "It's " + computerName + "'s Turn (O)";
            } else {
                turnText = (currentPlayer == Seed.CROSS) ? "It's " + player1Name + "'s Turn (X)" : "It's " + player2Name + "'s Turn (O)";
            }
            statusBar.setText(turnText);
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again.");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText(player1Name + " (X) Won! Click to play again.");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            if (currentGameMode == GameMode.PLAYER_VS_COMPUTER) {
                statusBar.setText(computerName + " (O) Won! Click to play again.");
            } else {
                statusBar.setText(player2Name + " (O) Won! Click to play again.");
            }
        }
    }

    /** The entry "main" method */
    public static void main(String[] args) {
        // Run GUI construction codes in Event-Dispatching thread for thread safety
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Launch the welcome screen first
                new WelcomeScreen().setVisible(true);
                // The GameMain JFrame will be launched from the WelcomeScreen's button action
            }
        });
    }
}