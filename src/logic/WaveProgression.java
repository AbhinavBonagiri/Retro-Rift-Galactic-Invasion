package logic;

public class WaveProgression {
    //Wave attributes
    private int currentWave = 0;
    private final int baseEnemiesPerWave = 5;
    private int enemiesToSpawn = baseEnemiesPerWave;
    private int enemiesSpawned = 0;
    private boolean waveInProgress = false;

    //Wave attributes
    private long enemySpawnInterval = 1500; // ms
    private int enemyHealth = 100;
    private int bossWaveInterval = 2;
    private boolean bossThisWave = false;

    //Image path fields
    private final String bossImagePath = "/Assets/images/GenericBoss.png";
    private final String enemyImage = "/Assets/images/Alien Spaceship.png";
    private final String harderEnemyImage = "/Assets/images/Red Alien Spaceship.png";
    private final String bossMovementPattern = "zigzag";

    //Methods to be used in future/currently?
    public int getCurrentWave() { return currentWave; }
    public int getEnemiesToSpawn() { return enemiesToSpawn; }
    public int getEnemiesSpawned() { return enemiesSpawned; }
    public boolean isWaveInProgress() { return waveInProgress; }
    public long getEnemySpawnInterval() { return enemySpawnInterval; }
    public int getEnemyHealth() { return enemyHealth; }
    public boolean isBossWave() { return bossThisWave; }
    public String getBossImagePath() { return bossImagePath; }
    public String getEnemyImage() { return enemyImage; }
    public String getHarderEnemyImage() { return harderEnemyImage; }
    public String getBossMovementPattern() { return bossMovementPattern; }

    //Important method that handles progression of waves
    public void startNextWave() {
        currentWave++;
        bossThisWave = (currentWave % bossWaveInterval == 0);
        if (bossThisWave) {
            enemiesToSpawn = 1; // Only one boss
        } else {
            enemiesToSpawn = baseEnemiesPerWave + (currentWave - 1) * 2;
        }
        enemiesSpawned = 0;
        waveInProgress = true;
        // Lower spawn interval, but not below 400ms
        enemySpawnInterval = Math.max(400, 1500 - (currentWave - 1) * 100);
        // Increase enemy health
        enemyHealth = 100 + (currentWave - 1) * 30;
    }

    //More wave methods
    public void incrementEnemiesSpawned() { enemiesSpawned++; }
    public void setWaveInProgress(boolean inProgress) { waveInProgress = inProgress; }
}