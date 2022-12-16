package evolution.events;

import evolution.main.Mappable;
import evolution.util.Vector2;

public class MoveEvent implements Event {

    private final Mappable moved; // TODO no constructor error
    private final Vector2 from;
    private final Vector2 to;

    // overrides

    @Override
    public Object[] getAllData() {
        return new Object[]{this.moved, this.from, this.to};
    }

    // constructors

    public MoveEvent(Mappable moved, Vector2 from, Vector2 to) {
        this.moved = moved;
        this.from = from;
        this.to = to;
    }

    // getters/setters

    public Mappable getMoved() {
        return this.moved;
    }


    public Vector2 getFrom() {
        return this.from;
    }


    public Vector2 getTo() {
        return this.to;
    }
}
