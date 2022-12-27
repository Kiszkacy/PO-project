package evolution.main;

import evolution.util.Vector2;

/**
 * Interface that defines basic environment-organism communication.
 */
public interface Environment {

    /**
     * Returns information about food availability at a given position.
     * @param where position where food is searched at
     * @return true if there's a food
     */
    boolean isFoodAt(Vector2 where);

    /**
     * Returns information if there's another creature also looking for a mate at a given position.
     * @param where position where creature is searched at
     * @param candidate creature looking for a mate
     * @return true if interested mate is found
     */
    boolean isMateAt(Vector2 where, Creature candidate);

    /**
     * Returns food from a given position.
     * @param from given position
     * @return food located at that position
     */
    Eatable getFood(Vector2 from);

    /**
     * Returns creature which is also looking for a mate at a given position.
     * @param from given position
     * @param candidate creature looking for a mate
     * @return interested creature
     */
    Creature getMate(Vector2 from, Creature candidate);
}
