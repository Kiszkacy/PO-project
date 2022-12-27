package evolution.events;

import evolution.main.Mappable;
import evolution.util.Vector2;
import java.util.Objects;

public class MoveEvent implements Event {

    private final Mappable moved;
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

    // hash & equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveEvent moveEvent = (MoveEvent) o;
        return Objects.equals(this.moved, moveEvent.moved) && Objects.equals(this.from, moveEvent.from) && Objects.equals(this.to, moveEvent.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.moved, this.from, this.to);
    }
}
