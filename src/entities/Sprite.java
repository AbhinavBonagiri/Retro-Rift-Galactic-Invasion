package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Sprite {
    BufferedImage[] images;
    private int currFrame;
    private double animationSpeed;

    private int count; //A calculation of how many sprites in spritesheet

    public Sprite() {
        // Default constructor
    }

    public Sprite(String path, int spriteWidth, int spriteHeight) {
        BufferedImage spriteSheet = null;

        try {
            spriteSheet = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Automatically calculate the number of columns and rows
        int columns = spriteSheet.getWidth() / spriteWidth;
        int rows = spriteSheet.getHeight() / spriteHeight;

        //Array of split sprites in a spritesheet
        images = new BufferedImage[rows * columns];
        //Splitting the spritesheet into images
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                images[i + columns * j] = spriteSheet.getSubimage(
                    i * spriteWidth, 
                    j * spriteHeight, 
                    spriteWidth, 
                    spriteHeight
                );
            }
        }
        count = rows * columns;
    }

    //This is to draw sprite(only at specified frame)
    public void drawSprite(Graphics g, int index, int x, int y) {
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("Invalid sprite index: " + index);
        }
        //Draws at specified frame
        g.drawImage(images[index], x, y, null);
    }

    //This helps to draw sprite animation fully(all indices)
    public int getCount() {
        return count;
    }
}