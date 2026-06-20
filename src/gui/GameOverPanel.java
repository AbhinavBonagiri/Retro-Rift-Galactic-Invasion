package gui;

import java.awt.*;
import javax.swing.*;

public class GameOverPanel extends JPanel {
    MainFrame mainFrame;
    private JPanel labelsPanel; //Declare labelsPanel as an instance variable
    private JLabel gameOverLabel;
    private JLabel highscoreLabel;

    //Initializes all the buttons(exit button, gameover with highscore)
    public GameOverPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Color.black);

        //Create and configure labelsPanel
        labelsPanel = new JPanel();
        labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.Y_AXIS));
        labelsPanel.setBackground(Color.black);
        labelsPanel.setMaximumSize(new Dimension(400, 200)); //Used for the centering(vertical glue)

        //Create and configure labels
        gameOverLabel = new JLabel("Game Over");
        gameOverLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        gameOverLabel.setForeground(new Color(240, 240, 240));
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        highscoreLabel = new JLabel("Highscore: " + mainFrame.getScore());
        highscoreLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        highscoreLabel.setForeground(new Color(240, 240, 240));
        highscoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Add components to labelsPanel
        labelsPanel.add(Box.createVerticalGlue());
        labelsPanel.add(gameOverLabel);
        labelsPanel.add(highscoreLabel);
        labelsPanel.add(Box.createVerticalGlue());

        add(labelsPanel, BorderLayout.CENTER);

        //Exit button initilization
        JButton backButton = new JButton("Back to Main Menu");
        backButton.setFont(new Font("Monospaced", Font.PLAIN, 18));
        backButton.setForeground(new Color(240, 240, 240));
        backButton.setBackground(new Color(20, 20, 20));
        backButton.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        backButton.addActionListener(e -> mainFrame.showMainMenu());
        add(backButton, BorderLayout.SOUTH);
    }

    //This method is used to update the highscore
    public void showPanel() {
        highscoreLabel.setText("Highscore: " + mainFrame.getScore()); //Update highscore
        revalidate();
        repaint();
    }
}