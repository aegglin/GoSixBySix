import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GoSixBySix {

    public GoSixBySix() {

    }

    public void createPanel () {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
        panel.setPreferredSize(new Dimension(200, 150));
        panel.setLayout(new FlowLayout());

        //Must handle IO exception
        try {
            Image image = ImageIO.read(new File("C:\\Users\\aiden\\java\\GoSixBySix\\GoPanel.png"));
            Image scaledImage = image.getScaledInstance(10, 10, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledImage);
            JLabel label = new JLabel(icon);
            panel.add(label);
        }
        catch (IOException e) {
            System.err.println("IO Exception found.");
            e.printStackTrace();
        }

        frame.getContentPane().add(panel);
        frame.pack(); // resize
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        GoSixBySix gsbs = new GoSixBySix();
        gsbs.createPanel();
    }
}