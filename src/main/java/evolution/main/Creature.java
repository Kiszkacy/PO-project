package evolution.main;

/**
 * Interface that defines organism which is alive, can move and is able to eat as well as reproduce.
 */
public interface Creature extends Killable {

    void move();
    boolean lookForFood();
    void eat(Eatable what);
    boolean lookForMate();
    Creature reproduce(Creature with);
}
