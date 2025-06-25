package Chapter5;

import java.awt.*;

/**
 * The Board class models the ROWS-by-COLS game board.
 */
public class Board {
    // Define named constants
    public static final int ROWS = 3;  // ROWS x COLS cells
    public static final int COLS = 3;
    // Define named constants for drawing
    public static final int CANVAS_WIDTH = Cell.SIZE * COLS;  // the drawing canvas
    public static final int CANVAS_HEIGHT = Cell.SIZE * ROWS;
    public static final int GRID_WIDTH = 10;  // Grid-line's width (Increased for bolder lines)
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2; // Grid-line's half-width
    public static final Color COLOR_GRID = new Color(101, 67, 33); // Dark brown for a pixel-art ground/wood feel
    public static final int Y_OFFSET = 1;  // Fine tune for better display

    // Define properties (package-visible)
    /** Composes of 2D array of ROWS-by-COLS Cell instances */
    Cell[][] cells;

    /** Constructor to initialize the game board */
    public Board() {
        initGame();
    }

    /** Initialize the game objects (run once) */
    public void initGame() {
        cells = new Cell[ROWS][COLS]; // allocate the array
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                // Allocate element of the array
                cells[row][col] = new Cell(row, col);
                // Cells are initialized in the constructor
            }
        }
    }

    /** Reset the game board, ready for new game */
    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].newGame(); // clear the cell content
            }
        }
    }

    /**
     * The given player makes a move on (selectedRow, selectedCol).
     * Update cells[selectedRow][selectedCol]. Compute and return the
     * new game state (PLAYING, DRAW, CROSS_WON, NOUGHT_WON).
     */
    public State stepGame(Seed player, int selectedRow, int selectedCol) {
        // Update game board
        cells[selectedRow][selectedCol].content = player;

        // Compute and return the new game state
        if (cells[selectedRow][0].content == player  // 3-in-the-row
                && cells[selectedRow][1].content == player
                && cells[selectedRow][2].content == player
                || cells[0][selectedCol].content == player // 3-in-the-column
                && cells[1][selectedCol].content == player
                && cells[2][selectedCol].content == player
                || selectedRow == selectedCol     // 3-in-the-diagonal
                && cells[0][0].content == player
                && cells[1][1].content == player
                && cells[2][2].content == player
                || selectedRow + selectedCol == 2 // 3-in-the-opposite-diagonal
                && cells[0][2].content == player
                && cells[1][1].content == player
                && cells[2][0].content == player) {
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        } else {
            // Nobody win. Check for DRAW (all cells occupied) or PLAYING.
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    if (cells[row][col].content == Seed.NO_SEED) {
                        return State.PLAYING; // still have empty cells
                    }
                }
            }
            return State.DRAW; // no empty cell, it's a draw
        }
    }

    /** Check if the player with "seed" has won after placing at (row, col) */
    public boolean hasWon(Seed player, int row, int col) {
        // Simplified hasWon logic to be used by AI
        return (cells[row][0].content == player && cells[row][1].content == player && cells[row][2].content == player) ||
                (cells[0][col].content == player && cells[1][col].content == player && cells[2][col].content == player) ||
                (row == col && cells[0][0].content == player && cells[1][1].content == player && cells[2][2].content == player) ||
                (row + col == 2 && cells[0][2].content == player && cells[1][1].content == player && cells[2][0].content == player);
    }

    /** Paint itself on the graphics canvas, given the Graphics context */
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw alternating background for cells (Creative Board)
        Color color1 = new Color(180, 200, 180); // Light green-grey
        Color color2 = new Color(160, 180, 160); // Darker green-grey
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if ((row + col) % 2 == 0) {
                    g2d.setColor(color1);
                } else {
                    g2d.setColor(color2);
                }
                g2d.fillRect(col * Cell.SIZE, row * Cell.SIZE, Cell.SIZE, Cell.SIZE);
            }
        }

        // Draw the grid-lines on top of the cell backgrounds
        g2d.setStroke(new BasicStroke(GRID_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); // Use BasicStroke for GRID_WIDTH
        g2d.setColor(COLOR_GRID);
        for (int row = 1; row < ROWS; ++row) {
            g2d.drawLine(0, Cell.SIZE * row, CANVAS_WIDTH, Cell.SIZE * row);
        }
        for (int col = 1; col < COLS; ++col) {
            g2d.drawLine(Cell.SIZE * col, 0, Cell.SIZE * col, CANVAS_HEIGHT);
        }

        // Draw all the cells (X/O symbols)
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g);  // ask the cell to paint itself
            }
        }
    }
}