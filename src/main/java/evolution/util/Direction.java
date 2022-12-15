package evolution.util;

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


    public Vector2 vectorize() {
        return this.dir;
    }


    public static int size() {
        return Direction.values().length;
    }


    public Direction next() {
        return Direction.values()[(this.ordinal()+1) % Direction.values().length];
    }


    public Direction prev() {
        return Direction.values()[(this.ordinal()-1) % Direction.values().length];
    }


    public Direction rotate(Direction by) {return Direction.values()[(this.ordinal() + by.ordinal()) % Direction.values().length];}


    Direction(Vector2 dir) {
        this.dir = dir;
    }
}
