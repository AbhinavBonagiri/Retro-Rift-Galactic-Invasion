package entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Enemy extends Sprite {
    private BufferedImage image;        
    private int health = 100;           
    private int damage = 25;            
    private int x, y;                   
    private final int MOVE_SPEED = 2;
    private String movementPattern;     
    private double time = 0;            
    private String type = "enemy";      

    // Shooting control
    private long lastShotTime = 0;
    private long shootCooldown = 2000;

    // Parameters for non‐linear movement
    private int startX, startY;
    private double amplitude, frequency, radius;

    // Zigzag constructor for amplitude/frequency
    public Enemy(String path, String movementZigzag, int startX, int startY, double amplitude, double frequency) {
        super();
        try {
            image = ImageIO.read(getClass().getResourceAsStream(path));
            this.startX = this.x = startX;
            this.startY = this.y = startY;
            this.movementPattern = movementZigzag;
            this.amplitude = amplitude;
            this.frequency = frequency;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Linear/Diagonal constructor
    public Enemy(String path, String movementPattern, int startX, int startY) {
        super();
        try {
            image = ImageIO.read(getClass().getResourceAsStream(path));
            this.startX = this.x = startX;
            this.startY = this.y = startY;
            this.movementPattern = movementPattern;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Circular constructor
    public Enemy(String path, String movementCircular, int startX, int startY, double radius) {
        super();
        try {
            image = ImageIO.read(getClass().getResourceAsStream(path));
            this.startX = this.x = startX;
            this.startY = this.y = startY;
            this.movementPattern = movementCircular;
            this.radius = radius;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setShootCooldown(long cooldown) {
        this.shootCooldown = cooldown;
    }
    //Counts time and if it surpasses the cooldown, the enemy can shoot
    public boolean canShootNow() {
        return System.currentTimeMillis() - lastShotTime >= shootCooldown;
    }
    public void recordShot() {
        lastShotTime = System.currentTimeMillis();
    }

    // A method to set type tag (enemy, boss, etc.)
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    // Only shoot when above player and not when enemy just spawned
    public boolean canShoot(int pY) {
        return y <= pY && y > 50;
    }

    // Collision rectangle
    public Rectangle getBounds() {
        return new Rectangle(x, y, 35, 35);
    }

    public void setHealth(int health) {
        this.health = health;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }
    public int getDamage() {
        return damage;
    }
    public void takeDamage(int damage) {
        health -= damage;
    }
    public boolean isHealthZero() {
        return health <= 0;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    // Update position based on selected pattern
    public void update() {
        time += 0.1;
        switch (movementPattern) {
            case "circular" -> {
                double[] pos = MovementPatterns.circularMovement(startX, startY, radius, time);
                x = (int) pos[0];
                y = (int) pos[1];
            }
            case "zigzag" -> {
                double[] pos = MovementPatterns.zigzagMovement(startX, startY, amplitude, frequency, MOVE_SPEED, time);
                x = (int) pos[0];
                y = (int) pos[1];
            }
            case "spiral" -> {
                double[] pos = MovementPatterns.spiralMovement(startX, startY, radius, MOVE_SPEED, time);
                x = (int) pos[0];
                y = (int) pos[1];
            }
            case "diagonal" -> {
                double[] pos = MovementPatterns.diagonalMovement(startX, startY, MOVE_SPEED, time);
                x = (int) pos[0];
                y = (int) pos[1];
            }
            case "linear" -> {
                y += MOVE_SPEED;
            }
            default -> {
                y += MOVE_SPEED;
            }
        }
    }

    // Render the enemy
    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }
}