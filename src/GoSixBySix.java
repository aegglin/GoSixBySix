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

public class GoSixBySix extends JFrame {

    public GoSixBySix() {
        super(); // call the JFrame constructor
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Go Six By Six");
        this.add(new GoSixBySixPanel(), BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new GoSixBySix();
    }
}

class GoSixBySixPanel extends JPanel {

    private static final int IMAGE_SIZE_PIXELS = 80;

    private static final String[] IMAGE_FILENAMES = { "GoPanel.png", "BlackStone65.png", "WhiteStone65.png" };
    Image[] images = new Image[3];

    private GoSixBySixState state = new GoSixBySixState();

    public GoSixBySixPanel() {

        this.setBackground(Color.GRAY);
        this.setPreferredSize(new Dimension(480, 480));
        this.setLayout(new GridLayout(GoSixBySixState.BOARD_SIZE, GoSixBySixState.BOARD_SIZE));
        this.setSize(480, 480);

        try {
            for (int i = 0; i < IMAGE_FILENAMES.length; i++) {
                File imageFile = new File(IMAGE_FILENAMES[i]);
                System.out.printf("%s", imageFile.getName());
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

        for (int row = 0; row < GoSixBySixState.BOARD_SIZE; row++)
            for (int col = 0; col < GoSixBySixState.BOARD_SIZE; col++) {
                int boardY = IMAGE_SIZE_PIXELS * row;
                int boardX = IMAGE_SIZE_PIXELS * col;

                g2d.drawImage(images[0], boardX, boardY, this);
                int squareContents = state.getPiece(row, col);

                int stoneY = (IMAGE_SIZE_PIXELS * row) - (IMAGE_SIZE_PIXELS / 3);
                int stoneX = (IMAGE_SIZE_PIXELS * col) - (IMAGE_SIZE_PIXELS / 3);

                if (squareContents == GoSixBySixState.EMPTY)
                    g2d.drawImage(images[GoSixBySixState.EMPTY], boardX, boardY, this);
                else if (squareContents == GoSixBySixState.BLACK)
                    g2d.drawImage(images[GoSixBySixState.BLACK], stoneX, stoneY, this);
                else if (squareContents == GoSixBySixState.WHITE)
                    g2d.drawImage(images[GoSixBySixState.WHITE], stoneX, stoneY, this);
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

            state.makeMove(pressRow, pressCol, false);
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            requestFocusInWindow();
            repaint();
        }
    }
}
