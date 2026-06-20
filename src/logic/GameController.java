package logic;

import entities.Boss;
import entities.Enemy;
import entities.Player;
import entities.PowerUp;
import entities.Projectile;
import java.awt.*;
import java.util.ArrayList;

public class GameController {
    // Time between enemy spawns in milliseconds
    private SoundManager soundManager = new SoundManager();
    private final long enemySpawnInterval = 1500;
    private long lastSpawnTime = System.currentTimeMillis();

    //Time between power-up spawns
    private final long powerUpSpawnInterval = 5000;
    private long lastPowerUpSpawnTime = System.currentTimeMillis();

    private final Player player;

    //Active entities
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final ArrayList<Projectile> projectiles = new ArrayList<>();
    private final ArrayList<PowerUp> powerUps = new ArrayList<>();

    //Handles how many enemies per wave, boss waves, etc.
    private final WaveProgression waveProgression = new WaveProgression();

    //Player’s score
    private int score = 0;

    public GameController(Player player){
        this.player = player;
        soundManager.loadSoundEffect("shoot", "/Assets/sounds/shoot.wav");
    }

    public void drawEnemies(Graphics g) {
        //Avoid ConcurrentModificationException with a copy
        for (Enemy enemy : new ArrayList<>(enemies)) {
            enemy.draw(g);
        }
    }

    public void drawProjectiles(Graphics g) {
        //Avoid ConcurrentModificationException with a copy
        for (Projectile p : new ArrayList<>(projectiles)) {
            p.draw(g);
        }
    }

    public void drawPowerUps(Graphics g) {
        //Avoid ConcurrentModificationException with a copy
        for (PowerUp powerUp : new ArrayList<>(powerUps)) {
            powerUp.draw(g);
        }
    }

    //Updates each frame with enemies, waves, projectiles, etc
    public void update(int panelWidth) {
        long now = System.currentTimeMillis();

        //Starts next wave if enemies empty
        if (!waveProgression.isWaveInProgress() && enemies.isEmpty()) {
            waveProgression.startNextWave();
        }

        //Spawn enemies if wave is over
        if (waveProgression.isWaveInProgress() && waveProgression.getEnemiesSpawned() < waveProgression.getEnemiesToSpawn() 
            && now - lastSpawnTime >= waveProgression.getEnemySpawnInterval()) {
            if (waveProgression.isBossWave()) {
                spawnBoss(panelWidth);
            } else {
                spawnEnemy(panelWidth);
            }
            lastSpawnTime = now;
            waveProgression.incrementEnemiesSpawned();
        }

        //Spawn power-up every fixed interval
        if (now - lastPowerUpSpawnTime >= powerUpSpawnInterval) {
            lastPowerUpSpawnTime = now;
            spawnPowerUp(panelWidth);
        }

        //Update all entities
        for (Enemy enemy : enemies) {
            enemy.update();
        }
        for (PowerUp pu : powerUps) {
            pu.update();
        }
        spawnEnemyProjectiles();
        updateProjectiles(panelWidth);

        offScreenEntities();
        checkCollisions();

        //End wave when all spawned enemies are gone
        if (waveProgression.isWaveInProgress()
            && waveProgression.getEnemiesSpawned() == waveProgression.getEnemiesToSpawn()
            && enemies.isEmpty()) {

            waveProgression.setWaveInProgress(false);
        }
    }

    //Spawn a regular enemy at a random x-position above the screen
    private void spawnEnemy(int panelWidth) {
        int x = (int) (Math.random() * (panelWidth - 50));
        Enemy e = new Enemy("/Assets/images/Alien Spaceship.png", "linear", x, -50);
        e.setHealth(waveProgression.getEnemyHealth());
        enemies.add(e);
    }

    //Spawn a tougher boss in the center
    private void spawnBoss(int panelWidth) {
        int x = panelWidth / 2 - 75;
        Boss boss = new Boss("/Assets/images/GenericBoss.png", "linear", x, -50);
        boss.setHealth(waveProgression.getEnemyHealth() * 5);
        boss.setType("boss");
        enemies.add(boss);
    }

    //Spawn a power-up at a random x-position above the screen
    private void spawnPowerUp(int panelWidth) {
        int x = (int) (Math.random() * (panelWidth - 50));
        powerUps.add(new PowerUp(x, -50));
    }

    //Let each enemy shoot if aligned with player and cooldown passed
    private void spawnEnemyProjectiles() {
        for (Enemy enemy : enemies) {
            if (enemy.canShoot(player.getY()) && enemy.canShootNow()) {
                createProjectile(enemy.getX(), enemy.getY(), enemy);
                enemy.recordShot();
            }
        }
    }

    //Player-fired projectile
    public void createProjectile(int x, int y) {
        soundManager.playSoundEffect("shoot");
        Projectile p = new Projectile();
        p.setOwner(Projectile.Owner.PLAYER);
        p.setDamage(player.getDamage());
        p.setPosition(x, y);
        projectiles.add(p);
    }

    //Enemy-fired projectile
    public void createProjectile(int x, int y, Enemy enemy) {
        Projectile p = new Projectile();
        p.setOwner(Projectile.Owner.ENEMY);
        p.setDamage(enemy.getDamage());
        p.setPosition(x, y);
        projectiles.add(p);
    }

    //Move projectiles and remove any that go offscreen
    public void updateProjectiles(int panelWidth) {
        ArrayList<Projectile> toRemove = new ArrayList<>();
        for (Projectile p : projectiles) {
            p.shoot();
            if (p.getX() > panelWidth) {
                toRemove.add(p);
            }
        }
        projectiles.removeAll(toRemove);
    }

    //Remove any enemies that go offscreen
    private void offScreenEntities(){
        ArrayList<Enemy> removeEnemies = new ArrayList<>();
        for (Enemy e : enemies) {
            if (e.getY() > 650) removeEnemies.add(e);
        }
        enemies.removeAll(removeEnemies);

        ArrayList<Projectile> removeProj = new ArrayList<>();
        for (Projectile p : projectiles) {
            if (p.getY() > 675 || p.getY() < 0) removeProj.add(p);
        }
        projectiles.removeAll(removeProj);
    }

    //Handle all collision detection between projectiles, enemies, player, and power-ups
    private void checkCollisions(){
        ArrayList<Enemy> deadEnemies = new ArrayList<>();
        ArrayList<Projectile> collidedProjectiles = new ArrayList<>();
        ArrayList<PowerUp> collidedPowerUps = new ArrayList<>();

        //Projectiles collisions with entities
        for (Projectile p : projectiles) {
            for (Enemy e : enemies) {
                if (p.getOwner() == Projectile.Owner.PLAYER && p.getBounds().intersects(e.getBounds())) {
                    e.takeDamage(p.getDamage());
                    collidedProjectiles.add(p);
                    if (e.isHealthZero()) {
                        deadEnemies.add(e);
                        score += "boss".equals(e.getType()) ? 1000 : 100;
                    }
                    break;
                }
                else if (p.getOwner() == Projectile.Owner.ENEMY
                    && p.getBounds().intersects(player.getBounds())) {

                    player.takeDamage(p.getDamage());
                    collidedProjectiles.add(p);
                    break;
                }
            }
        }

        //Power-ups collisions
        for (PowerUp pu : powerUps) {
            if (pu.getBounds().intersects(player.getBounds())) {
                if (pu.getType() == 1) {
                    //Health boost
                    player.setHealth(Math.min(player.getHealth() + 50, player.getMaxHealth()));
                }
                else if (pu.getType() == 2) {
                    //Damage boost
                    player.setDamage(player.getDamage() + 25);
                }
                collidedPowerUps.add(pu);
            }
        }

        //Remove everything that collided or died
        enemies.removeAll(deadEnemies);
        projectiles.removeAll(collidedProjectiles);
        powerUps.removeAll(collidedPowerUps);
    }

    public int getScore() {
        return score;
    }

    public Player getPlayer(){
        return player;
    }
}