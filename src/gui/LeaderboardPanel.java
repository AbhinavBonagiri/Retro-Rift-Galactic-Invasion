package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class LeaderboardPanel extends JPanel{
    private MainFrame mainFrame;
    private ArrayList<Integer> highscores = new ArrayList<>(); //Stores scores
    private JPanel scoresPanel; //Holds all the score labels
    private JLabel highscoreTitle;
    private JButton backButton;

    public LeaderboardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Color.black);

        //Title at top
        highscoreTitle = new JLabel("Highscores", SwingConstants.CENTER);
        highscoreTitle.setFont(new Font("Monospaced", Font.BOLD, 36));
        highscoreTitle.setForeground(Color.WHITE);
        add(highscoreTitle, BorderLayout.NORTH);

        //Back button at bottom
        backButton = new JButton("Back to Main Menu");
        backButton.setFont(new Font("Monospaced", Font.PLAIN, 18));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(20, 20, 20));
        backButton.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        backButton.addActionListener(e -> mainFrame.showMainMenu());
        add(backButton, BorderLayout.SOUTH);

        //Center panel to list the top scores
        scoresPanel = new JPanel();
        scoresPanel.setLayout(new BoxLayout(scoresPanel, BoxLayout.Y_AXIS));
        scoresPanel.setBackground(Color.black);
    }

    //Keep only the top 10 scores, sorted descending.
    public void updateHighscores() {
        highscores.sort(Comparator.reverseOrder());
        while (highscores.size() > 10) {
            highscores.remove(highscores.size() - 1);
        }
    }

    //Rebuild and display the score list in the center of this panel.
    public void showHighscores() {
        scoresPanel.removeAll(); //Clear previous entries
        updateHighscores();

        int count = Math.min(highscores.size(), 10);
        for (int i = 0; i < count; i++) {
            String text = (i + 1) + ". " + highscores.get(i);
            JLabel label = new JLabel(text, SwingConstants.CENTER);
            label.setFont(new Font("Monospaced", Font.PLAIN, 24));
            label.setForeground(Color.WHITE);
            scoresPanel.add(label);
        }

        //Replace existing center component with updated scoresPanel
        remove(scoresPanel);
        add(scoresPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    //Adds highscore to the list
    public void addHighscore(int highscore){
        boolean isGreater = false;
        for(int num : highscores){
            if(highscore > num) isGreater = true;
        }
        if(highscores.size() < 10 || isGreater){
            highscores.add(highscore);
        } 
    }
}