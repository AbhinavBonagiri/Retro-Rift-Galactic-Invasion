package entities;
//Not fully implemented all features, inherits from parent class, and has special effects separate from parent class.
public class Boss extends Enemy {
    private int health;

    public Boss(String imagePath, String movementPattern, int x, int y) {
        super(imagePath, movementPattern, x, y);
        this.setShootCooldown(750); // Set cooldown for shooting
        this.health = 200;           
                 
    }

    @Override
    //Gives more control by having better health
    public boolean isHealthZero() {
        return this.health <= 0;
    }

    @Override
    //Gives more control over damage taken
    public void takeDamage(int damage) {
        this.health -= damage;
    }
}