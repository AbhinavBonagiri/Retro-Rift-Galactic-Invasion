package entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Projectile extends Sprite {
    private BufferedImage image;
    private double x, y;
    private double dx = 0, dy = 0;
    private double angle;
    private double sinePhase = 0;
    private int width = 40;
    private int height = 40;
    private int damage = 10;

    //Used to identify who shot projectile
    public enum Owner {
        PLAYER, ENEMY
    }

    //Used to define what attack is this projectile for
    public enum Type {
        STRAIGHT, SPREAD
    }

    private Owner owner;
    private Type type = Type.STRAIGHT; //Defaults to straight projectile attack

    public Projectile() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Assets/images/Projectile.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setDirection(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void setAngle(double angleDegrees) {
        this.angle = Math.toRadians(angleDegrees);
        this.dx = Math.cos(this.angle) * 8;
        this.dy = Math.sin(this.angle) * 8;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void shoot() {
        if(owner == Projectile.Owner.PLAYER){
            this.y -= 10; //For player projectile goes up
        } else {
            this.y += 3; //Enemy is down
        }
    }

    //Checks if projectile is offscreen
    public boolean isOffScreen(int screenHeight) {
        return y < -height || y > screenHeight;
    }

    //Projectile's collision rectangle
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }

    public int getX() {
        return (int)x;
    }

    public int getY() {
        return (int)y;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void draw(Graphics g) {
        g.drawImage(image, (int)x, (int)y, width, height, null);
    }
}