package entities;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class Background extends JPanel {
    private final ArrayList<Star> stars = new ArrayList<>();
    private final int numStars = 200; //Number of stars to display

    public Background() {
        setOpaque(false);
        setDoubleBuffered(true);

        //Timer to update star positions and repaint the panel
        Timer timer = new Timer(16, e -> {
            for (Star s : stars) s.update();
            repaint();
        });
        timer.start();

        //Listener to regenerate stars when the component is resized
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                stars.clear();
                for (int i = 0; i < numStars; i++) {
                    stars.add(new Star(getWidth(), getHeight()));
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Draw each star
        for (Star s : stars) {
            s.draw((Graphics2D) g);
        }
    }

    private static class Star {
        private float x, y, speed;
        private final int width, height;

        public Star(int width, int height) {
            this.width = width;
            this.height = height;
            reset(); //Reinitialize star position and speed
        }

        private void reset() {
            x = (float)(Math.random() * width);
            y = (float)(Math.random() * height - height); //Start above the screen
            speed = (float)(Math.random() * 3 + 1); 
        }

        public void update() {
            y += speed; //Move star down
            if (y > height) {
                reset(); //Reset star if it goes off screen
            }
        }

        public void draw(Graphics2D g) {
            int alpha = (int)(100 + speed / 4 * 155); //Calculate transparency based on speed
            g.setColor(new Color(255, 255, 255, Math.min(alpha, 255))); 
            g.fillOval((int)x, (int)y, (int)(speed / 1.5), (int)(speed / 1.5)); 
        }
    }
}