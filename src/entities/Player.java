package entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Sprite {
    private BufferedImage image;    
    private int x, y;            
    //Player stats  
    private int health = 100;      
    private int maxHealth = 100;    
    private int damage = 50;       

    //Constructor(init. player and load sprite)
    public Player(int x, int y) {
        try {
            image = ImageIO.read(
                getClass().getResourceAsStream("/Assets/images/Ship_piskel.png")
            );
            this.x = x;
            this.y = y;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getMaxHealth() {
        return maxHealth;
    }
    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getDamage() {
        return damage;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void takeDamage(int damage) {
        health -= damage;
    }
    public boolean isHealthZero() {
        return health <= 0;
    }

    //Collision bounds (for hit detection)
    public Rectangle getBounds() {
        return new Rectangle(x, y, 35, 35);
    }

    public void move(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //Render the player sprite
    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }
}