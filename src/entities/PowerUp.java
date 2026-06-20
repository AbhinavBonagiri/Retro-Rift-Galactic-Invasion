package entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PowerUp extends Sprite{
    private BufferedImage image;
    private int x, y;
    private int type;
    private int size = 20;

    //Initiliaze powerup attributes/load sprite
    public PowerUp(int x, int y) {
        try {
            this.x = x;
            this.y = y;
            //Generates random powerup
            this.type = (int)(Math.random() * 2) + 1;
            image = ImageIO.read(getClass().getResourceAsStream("/Assets/images/Powerup.png"));
        } catch (IOException ex) {
        }
    }

    public void update() {
        y += 2;
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    //Collision rectangle
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, size, size);
    }

    public int getType() {
        return type;
    }
}