package entities;

import java.awt.Graphics;
import java.util.ArrayList;

public class Attack {
    // All active projectiles
    private ArrayList<Projectile> projectiles = new ArrayList<>();

    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }

    public void shoot() {
        for (Projectile p : projectiles) {
            p.shoot();
        }
        // Remove projectiles that are off-screen 
        projectiles.removeIf(p -> p.isOffScreen(800));
    }

    public void draw(Graphics g) {
        for (Projectile p : projectiles) {
            p.draw(g);
        }
    }

    public void fireStraight(double x, double y, Projectile.Owner owner) {
        Projectile p = new Projectile();
        p.setPosition(x, y);
        p.setOwner(owner);
        p.setType(Projectile.Type.STRAIGHT);
        // Direction of the projectile (upward for PLAYER, downward for ENEMY)
        p.setDirection(0, owner == Projectile.Owner.PLAYER ? -10 : 5);
        projectiles.add(p);
    }

    public void fireSpread(double x, double y, Projectile.Owner owner, int count, double spreadAngle) {
        double baseAngle = owner == Projectile.Owner.PLAYER ? -90 : 90;
        double startAngle = baseAngle - spreadAngle / 2;
        double angleStep = spreadAngle / (count - 1);

        // Create and fire the specified number of projectiles
        for (int i = 0; i < count; i++) {
            Projectile p = new Projectile();
            p.setOwner(owner);
            p.setType(Projectile.Type.SPREAD);
            p.setPosition(x, y);
            p.setAngle(startAngle + i * angleStep);
            projectiles.add(p);
        }
    }
}