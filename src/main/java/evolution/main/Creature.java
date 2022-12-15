package evolution.main;

public interface Creature extends Killable {

    void move();
    boolean lookForFood();
    void eat(Eatable what);
    boolean lookForMate();
    Creature reproduce(Creature with);
}
