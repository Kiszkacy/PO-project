package evolution.main;

import evolution.util.Vector2;

/**
 * Interface that defines object that can be placed on a map.
 */
public interface Mappable {

    Vector2 getPos();
    void setPos(Vector2 v);
    int hashCode();
}
