import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GoSixBySix {

    private static final int BOARD_SIZE = 6;
    private static final JPanel[][] boardTiles = new JPanel[BOARD_SIZE][BOARD_SIZE];

    private static final int BOARD_SQUARE_SIZE = 35;

    public GoSixBySix() {

    }

    public void createPanel () {

        /*
            There are multiple levels to the hierarchy here: 
            First, a JFrame holds the entire GUI
            Parent, a JPanel, is held by the JFrame
            Each tile is another JPanel added to parent
         */
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JPanel parent = new JPanel();
        parent.setBackground(Color.GRAY);
        parent.setPreferredSize(new Dimension(200, 200));
        parent.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));

        //Must handle IO exception
        try {
            //Image is an abstract super class
            Image image = ImageIO.read(new File("C:\\Users\\aiden\\java\\GoSixBySix\\GoPanel.png"));
            Image scaledImage = image.getScaledInstance(BOARD_SQUARE_SIZE, BOARD_SQUARE_SIZE, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledImage);

            for (int r = 0; r < BOARD_SIZE; r++) {
                for (int c = 0; c < BOARD_SIZE; c++) {
                    JPanel panel = new JPanel(new GridLayout());
                    boardTiles[r][c] = panel;
                    boardTiles[r][c].add(new JLabel(icon));
                    parent.add(boardTiles[r][c]);
                }
            }
        }
        catch (IOException e) {
            System.err.println("IO Exception found.");
            e.printStackTrace();
        }

        frame.getContentPane().add(parent);
        frame.pack(); // resize
        frame.setVisible(true);

    }

    class MouseHandler extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent event) {
            requestFocusInWindow();
            int pressRow = event.getY() / BOARD_SQUARE_SIZE;
            int pressCol = event.getX() / BOARD_SQUARE_SIZE;
        }
    }

    public static void main(String[] args) {
        GoSixBySix gsbs = new GoSixBySix();
        gsbs.createPanel();
    }
}