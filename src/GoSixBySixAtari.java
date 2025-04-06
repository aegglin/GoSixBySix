import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class GoSixBySixAtari extends JFrame {

    public GoSixBySixAtari() {
        // call JFrame constructor and setup methods
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Go Six By Six");
        GoSixBySixPanel panel = new GoSixBySixPanel();
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        add(panel, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        new GoSixBySixAtari();
    }
}

class GoSixBySixPanel extends JPanel {

    private static final int IMAGE_SIZE_PIXELS = 65;

    private static final String[] IMAGE_FILENAMES = { "GoPanel.png", "BlackStone65.png", "WhiteStone65.png" };
    Image[] images = new Image[3];

    private GoSixBySixAtariState state = new GoSixBySixAtariState();

    public GoSixBySixPanel() {

        // call JPanel setup methods
        setBackground(Color.GRAY);
        setPreferredSize(new Dimension(420, 420));
        setLayout(new GridLayout(GoSixBySixAtariState.BOARD_SIZE, GoSixBySixAtariState.BOARD_SIZE));
        setSize(420, 420);

        try {
            for (int i = 0; i < IMAGE_FILENAMES.length; i++) {
                File imageFile = new File(IMAGE_FILENAMES[i]);
                images[i] = ImageIO.read(imageFile);
            }
        } catch (IOException e) {
            System.err.println("Error reading in the files.");
            e.printStackTrace();
        }

        addMouseListener(new MouseHandler());
    }

    // used to redo the board after each move
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int padding = 10;

        // Get the actual drawing area dimensions considering padding
        int width = getWidth() - 2 * padding;
        int height = getHeight() - 2 * padding;

        // Draw a rectangle within the padded area
        g.setColor(Color.RED);
        g.fillRect(padding, padding, width, height);

        for (int row = 0; row < GoSixBySixAtariState.BOARD_SIZE; row++)
            for (int col = 0; col < GoSixBySixAtariState.BOARD_SIZE; col++) {
                int boardY = IMAGE_SIZE_PIXELS * row - padding;
                int boardX = IMAGE_SIZE_PIXELS * col - padding;

                g2d.drawImage(images[0], boardX, boardY, this);
                int squareContents = state.getPiece(row, col);

                //Align the stone on the intersection of the lines, not the square itself
                int stoneY = (IMAGE_SIZE_PIXELS * row) - (IMAGE_SIZE_PIXELS / 3);
                int stoneX = (IMAGE_SIZE_PIXELS * col) - (IMAGE_SIZE_PIXELS / 3);

                if (squareContents == GoSixBySixAtariState.EMPTY)
                    g2d.drawImage(images[GoSixBySixAtariState.EMPTY], boardX, boardY, this);
                else if (squareContents == GoSixBySixAtariState.BLACK)
                    g2d.drawImage(images[GoSixBySixAtariState.BLACK], stoneX, stoneY, this);
                else if (squareContents == GoSixBySixAtariState.WHITE)
                    g2d.drawImage(images[GoSixBySixAtariState.WHITE], stoneX, stoneY, this);
            }
    }

    class MouseHandler extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent event) {
            requestFocusInWindow();
            int pressRow = event.getY() / IMAGE_SIZE_PIXELS;
            System.out.println("PressRow is: " + pressRow);
            int pressCol = event.getX() / IMAGE_SIZE_PIXELS;
            System.out.println("PressCol is: " + pressCol);

            state.makeMove(pressRow, pressCol);
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            requestFocusInWindow();
            repaint();
        }
    }
}
