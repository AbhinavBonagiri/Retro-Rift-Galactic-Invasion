package entities;

public class MovementPatterns {

    public static double[] circularMovement(double centerX, double centerY, double radius, double time) {
        double angle = time % (2 * Math.PI); // Normalize time to prevent overflow
        double x = centerX + radius * Math.cos(angle);
        double y = centerY + radius * Math.sin(angle);
        return new double[] { x, y };
    }

    // Diagonal movement pattern
    public static double[] diagonalMovement(double startX, double startY, double speed, double time) {
        double x = startX + speed * time;
        double y = startY + speed * time;
        return new double[] { x, y };
    }

    // Zigzag movement pattern
    public static double[] zigzagMovement(double startX, double startY, double amplitude, double frequency, double speed, double time) {
        double x = startX + (speed * time);
        double y = startY + amplitude * Math.sin(frequency * time);
        return new double[] { x, y };
    }

    // Spiral movement pattern
    public static double[] spiralMovement(double centerX, double centerY, double radius, double speed, double time) {
        double angle = time % (2 * Math.PI); // Normalize time to prevent overflow
        double currentRadius = radius + speed * time * 0.5; // Gradually increase radius for spiral effect
        double x = centerX + currentRadius * Math.cos(angle);
        double y = centerY + currentRadius * Math.sin(angle);
        return new double[] { x, y };
    }
}