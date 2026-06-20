package gui;

import java.awt.*;
import javax.swing.*;

//Main menu screen with buttons to start the game or view the leaderboard
public class MainPanel extends JPanel {
    private JButton startButton;
    private JButton leaderboardButton;

    public MainPanel(MainFrame mainFrame) {
        setLayout(null);//Manual positioning
        setBackground(Color.BLACK);

        // Title label 
        JLabel titleLabel = new JLabel("GALACTIC RIFT");
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 50));
        titleLabel.setForeground(Color.white);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds((mainFrame.getWidth() - 400) / 2, mainFrame.getHeight() / 3, 400, 50);
        add(titleLabel);


        //Start game button
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Monospaced", Font.BOLD, 18));
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(new Color(0, 0, 0, 180));
        startButton.setBorder(BorderFactory.createLineBorder(Color.white, 2));
        startButton.setFocusPainted(false);
        startButton.setBounds((mainFrame.getWidth() - 200) / 2, (mainFrame.getHeight()) / 2, 200, 50);
        startButton.addActionListener(e -> mainFrame.showGamePanel());
        add(startButton);

        //Leaderboard button
        leaderboardButton = new JButton("Leaderboard");
        leaderboardButton.setFont(new Font("Monospaced", Font.BOLD, 18));
        leaderboardButton.setForeground(Color.WHITE);
        leaderboardButton.setBackground(new Color(0, 0, 0, 180));
        leaderboardButton.setBorder(BorderFactory.createLineBorder(Color.white, 2));
        leaderboardButton.setFocusPainted(false);
        leaderboardButton.setBounds((mainFrame.getWidth() - 200) / 2, (mainFrame.getHeight()) / 2 + 80, 200, 50);
        leaderboardButton.addActionListener(e -> mainFrame.showLeaderboard());
        add(leaderboardButton);
    }
}
