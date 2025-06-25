package Chapter5;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

/**
 * The Cell class models each individual cell of the game board.
 */
public class Cell {
    // Define named constants for drawing
    public static final int SIZE = 120; // cell width/height (square)
    // Symbols (cross/nought) are displayed inside a cell, with padding from border
    public static final int PADDING = SIZE / 5;
    public static final int SEED_SIZE = SIZE - PADDING * 2;

    // Define properties (package-visible)
    /** Content of this cell (Seed.EMPTY, Seed.CROSS, or Seed.NOUGHT) */
    Seed content;
    /** Row and column of this cell */
    int row, col;

    /** Constructor to initialize this cell with the specified row and col */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        content = Seed.NO_SEED;
    }

    /** Reset this cell's content to EMPTY, ready for new game */
    public void newGame() {
        content = Seed.NO_SEED;
    }

    /** Paint itself on the graphics canvas, given the Graphics context */
    public void paint(Graphics g) {
        // Draw the Seed if it is not empty
        int x1 = col * SIZE + PADDING;
        int y1 = row * SIZE + PADDING;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Enable anti-aliasing

        if (content == Seed.CROSS || content == Seed.NOUGHT) {
            // Image loading is handled in Seed enum, so we just draw the image here
            g2d.drawImage(content.getImage(), x1, y1, SEED_SIZE, SEED_SIZE, null);
        }
    }
}