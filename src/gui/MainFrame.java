package gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainFrame extends JFrame implements ActionListener {
    
    private CardLayout cardLayout; //Layout for swapping screens
    private int currScore; //Stores the last game’s score

    //Width/height fields
    public int WIDTH = this.getHeight();
    public int HEIGHT = this.getWidth();

    //Different screens/panels
    private GamePanel gamePanel;
    private MainPanel mainPanel;
    private GameOverPanel gameOverPanel;
    private LeaderboardPanel leaderboardPanel;

    //Entry point for launching the GUI in a synchronized way. The calling thread waits until the frame is built.
    public static void startGUI() throws InterruptedException {
        MainFrame gui = new MainFrame();
        SwingUtilities.invokeLater(() -> gui.createFrame(gui));

        // Wait until createFrame notifies
        synchronized (gui) {
            gui.wait();
        }
        
    }

    //Initializes the frame: size, layout, and adds all panels.
    public void createFrame(Object semaphore) {
        // Frame settings
        this.setSize(WIDTH, HEIGHT);
        this.setMinimumSize(new Dimension(500, 750));
        this.setResizable(false);
        this.setTitle("Galactic Rift");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Use CardLayout to switch between screens
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        // Instantiate each panel
        mainPanel = new MainPanel(this);
        gamePanel = new GamePanel(this);
        gameOverPanel = new GameOverPanel(this);
        leaderboardPanel = new LeaderboardPanel(this);
        gamePanel.setVisible(false);

        // Add them with identifiers
        this.add(mainPanel,"MainPanel");
        this.add(gamePanel,"GamePanel");
        this.add(gameOverPanel,"GameOverPanel");
        this.add(leaderboardPanel, "LeaderboardPanel");

        this.setVisible(true);

        // Notify startGUI() that initialization is complete
        synchronized (semaphore) {
            semaphore.notify();
        }
        try {
            this.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
        } finally {
            // Notify startGUI() that initialization is complete
            synchronized (semaphore) {
                semaphore.notify();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //For future menu/button actions
    }

    //Show the game screen and start its loop
    public void showGamePanel() {
        gamePanel.resetGameState();
        cardLayout.show(this.getContentPane(), "GamePanel");
        gamePanel.startGameLoop();
    }

    //Current frame width accessor
    public int getWIDTH() {
        return getWidth();
    }

    //Current frame height accessor
    public int getHEIGHT() {
        return getHeight();
    }

    //Gets latest score
    public int getScore() {
        return gamePanel.getScore();
    }

    //Store the current score (used when ending a game)
    public void setScore(int score) {
        this.currScore = score;
    }

    //Back to the main menu screen
    public void showMainMenu() {
        cardLayout.show(this.getContentPane(), "MainPanel");
    }

    //Display the leaderboard screen with updated scores
    public void showLeaderboard() {
        leaderboardPanel.updateHighscores();
        cardLayout.show(this.getContentPane(), "LeaderboardPanel");
        leaderboardPanel.showHighscores();
    }

    //Called when the game ends
    public void showGameOverPanel() {
        gamePanel.stopGameLoop();
        currScore = gamePanel.getScore();
        leaderboardPanel.addHighscore(currScore);
        cardLayout.show(this.getContentPane(), "GameOverPanel");
        gameOverPanel.showPanel();
    }
}