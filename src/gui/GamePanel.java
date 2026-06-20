package gui;

import entities.Background;
import entities.Enemy;
import entities.Player;
import entities.Projectile;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import logic.GameController;

public class GamePanel extends JPanel {
    private MainFrame mainFrame;
    private GameController gameController;
    private HUD hud; //Health indicator                        
    private Background background; //Background
    
    private boolean running = true;          
    // Movement/shooting flags (key hold state)
    private boolean movingUp, movingDown, movingLeft, movingRight, shoot;

    private Player player; //The player ship
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    
    private final int MOVE_SPEED = 5; //Player speed
    private long lastShootTime = 0;
    private final long shootCooldown = 300; 


    public GamePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.player = new Player(0, 0);
        this.gameController = new GameController(player);
        this.hud = new HUD(gameController);
        this.background = new Background();

        setLayout(new OverlayLayout(this));
        add(background);

        setFocusable(true);
        setupKeyBindings();

        //Center player after panel is realized
        SwingUtilities.invokeLater(() -> {
            int centerX = (getWidth() - player.getX()) / 2;
            int centerY = (getHeight() - player.getY()) / 2;
            player.setPosition(centerX, centerY);
        });
    }

    //Reset everything for a new game
    public void resetGameState() {
        player.setPosition((getWidth() - player.getX()) / 2, (getHeight() - player.getY()) / 2);
        player.setHealth(100);
        player.setMaxHealth(100);
        enemies.clear();
        projectiles.clear();
        gameController = new GameController(player);
        hud = new HUD(gameController);
        running = true;
    }

    //Bind keys to movement/shoot flags
    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        //Define key strokes and actions
        String[] keys = {"UP", "DOWN", "LEFT", "RIGHT", "W", "A", "S", "D", "SPACE"};
        for (String key : keys) {
            bindKey(inputMap, actionMap, key);
        }
    }
    private void bindKey(InputMap inputMap, ActionMap actionMap, String key) {
        
        inputMap.put(KeyStroke.getKeyStroke("pressed " + key), "move" + key + "Pressed");
        inputMap.put(KeyStroke.getKeyStroke("released " + key), "move" + key + "Released");

        actionMap.put("move" + key + "Pressed", createAction(key, true));
        actionMap.put("move" + key + "Released", createAction(key, false));
    } 

    //Toggle the appropriate flag when a key is pressed/released
    private AbstractAction createAction(String key, boolean pressed) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                switch (key) {
                    case "UP","W" -> movingUp = pressed;
                    case "DOWN","S" -> movingDown = pressed;
                    case "LEFT","A" -> movingLeft = pressed;
                    case "RIGHT","D" -> movingRight = pressed;
                    case "SPACE" -> shoot = pressed;
                }
            }
        };
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.black);
        player.draw(g);
        gameController.drawProjectiles(g);
        gameController.drawEnemies(g);
        gameController.drawPowerUps(g);
        hud.draw(g);
    }

    public int getScore() {
        return gameController.getScore();
    }

    //Main game loop: update, repaint at ~60 FPS
    public void startGameLoop() {
        new Thread(() -> {
            final int fps = 60, frameTime = 1000 / fps;
            while (running) {
                long start = System.currentTimeMillis();

                updateAction(); //Handle input & projectiles
                gameController.update(getWidth());
                repaint();

                long sleep = frameTime - (System.currentTimeMillis() - start);
                if (sleep > 0) {
                    try { Thread.sleep(sleep); }
                    catch (InterruptedException ignored) {}
                }
            }
        }).start();    
    }

    //Stop loop and show game-over screen
    public void stopGameLoop() {
        mainFrame.setScore(gameController.getScore());
        movingUp = movingDown = movingLeft = movingRight = shoot = false;
        running = false;
        System.out.println("Game loop stopped.");
    }

    //Move player, handle shooting & projectile updates
    private void updateAction() {
        if (movingUp && player.getY() > 0) player.move(0, -MOVE_SPEED);
        if (movingDown && player.getY() < mainFrame.getHeight()-75) player.move(0, MOVE_SPEED);
        if (movingLeft && player.getX() > 0) player.move(-MOVE_SPEED, 0);
        if (movingRight && player.getX() < mainFrame.getWidth()-50)  player.move(MOVE_SPEED, 0);

        if (shoot) {
            long now = System.currentTimeMillis();
            if (now - lastShootTime >= shootCooldown) {
                gameController.createProjectile(player.getX(), player.getY());
                lastShootTime = now;
            }
        }

        gameController.updateProjectiles(getWidth());

        //Check for player death
        if (player.getHealth() <= 0) {
            ((MainFrame) SwingUtilities.getWindowAncestor(this)).showGameOverPanel();
        }
    }
}