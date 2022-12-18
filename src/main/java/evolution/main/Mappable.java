package evolution.main;

import evolution.util.Vector2;

public interface Mappable {

    Vector2 getPos();
    void setPos(Vector2 v);

    int hashCode();
}
