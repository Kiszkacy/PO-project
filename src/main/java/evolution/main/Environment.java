package evolution.main;

import evolution.util.Vector2;

public interface Environment {
    boolean isFoodAt(Vector2 where);
    boolean isMateAt(Vector2 where, Creature candidate);
    Eatable getFood(Vector2 from);
    Creature getMate(Vector2 from, Creature candidate);
}
