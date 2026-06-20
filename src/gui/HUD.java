package gui;

import entities.Player;
import java.awt.*;
import logic.GameController;

public class HUD {
    private GameController controller;

    public HUD(GameController controller) {
        this.controller = controller;
    }

    //Render health bar and score.
    public void draw(Graphics g) {
        Player player = controller.getPlayer();
        if (player == null) return; //Displays nothing

        drawHealthBar(g, player);
        drawScore(g, controller.getScore());
    }

    //Draws a colored health bar at the bottom‐left.
    private void drawHealthBar(Graphics g, Player player) {
        int maxHealth = player.getMaxHealth();
        int currentHealth = player.getHealth();

        // Position and size
        int barX = 20;
        int barY = g.getClipBounds().height - 70;
        int barWidth = 200;
        int barHeight = 20;

        //Calculate fill width by ratio
        double healthRatio = (double) currentHealth / maxHealth;
        int fillWidth = (int) (barWidth * healthRatio);

        //Draw background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(barX, barY, barWidth, barHeight);

        //Draw colored health fill
        Color fillColor = healthRatio > 0.5 ? Color.GREEN : healthRatio > 0.2 ? Color.ORANGE : Color.RED;
        g.setColor(fillColor);
        g.fillRect(barX, barY, fillWidth, barHeight);

        //Outline
        g.setColor(Color.GRAY);
        g.drawRect(barX, barY, barWidth, barHeight);

        //Text overlay
        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        g.drawString("HP: " + currentHealth + " / " + maxHealth, barX + 5, barY + 15);
    }

    //Draws the current score just above the health bar.
    private void drawScore(Graphics g, int score) {
        g.setFont(new Font("Monospaced", Font.BOLD, 20));
        g.setColor(Color.GRAY);
        g.drawString("Score: " + score, 20, g.getClipBounds().height - 30);
    }
}