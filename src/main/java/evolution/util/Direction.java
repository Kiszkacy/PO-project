package evolution.util;

/**
 * Enum responsible for indicating the direction of something in 2d space. Uses Vector2 pseudo-constructors.
 */
public enum Direction {
    UP(Vector2.UP()),
    UPRIGHT(Vector2.UPRIGHT()),
    RIGHT(Vector2.RIGHT()),
    DOWNRIGHT(Vector2.DOWNRIGHT()),
    DOWN(Vector2.DOWN()),
    DOWNLEFT(Vector2.DOWNLEFT()),
    LEFT(Vector2.LEFT()),
    UPLEFT(Vector2.UPLEFT());


    private final Vector2 dir;

    /**
     * Returns vector poniting in the corresponding direction. Vector is positioned in the "3x3 box" (values ranging from -1 to 1).
     * @return appropriate vector
     */
    public Vector2 vectorize() {
        return this.dir;
    }

    /**
     * Returns amount of all possible distinguishable directions.
     * @return that amount
     */
    public static int size() {
        return Direction.values().length;
    }

    /**
     * Returns next distinguishable clockwise direction.
     * @return next clockwise direction
     */
    public Direction next() {
        return Direction.values()[(this.ordinal()+1) % Direction.values().length];
    }

    /**
     * Returns next distinguishable counterclockwise direction.
     * @return next counterclockwise direction
     */
    public Direction prev() {
        return Direction.values()[(Direction.values().length+this.ordinal()-1) % Direction.values().length];
    }

    /**
     * Returns direction rotated relative to another given direction.
     * @param by direction to be rotated by
     * @return
     */
    public Direction rotate(Direction by) {return Direction.values()[(this.ordinal() + by.ordinal()) % Direction.values().length];}


    Direction(Vector2 dir) {
        this.dir = dir;
    }
}
