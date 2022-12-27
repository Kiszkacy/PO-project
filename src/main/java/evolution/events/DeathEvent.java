package evolution.events;

import evolution.main.Killable;
import java.util.Objects;

public class DeathEvent implements Event {

    private final Killable died;

    // overrides

    @Override
    public Object[] getAllData() {
        return new Object[]{this.died};
    }

    // constructors

    public DeathEvent(Killable died) {
        this.died = died;
    }

    // getters/setters

    public Killable getDied() {
        return this.died;
    }

    // hash & equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeathEvent that = (DeathEvent) o;
        return Objects.equals(this.died, that.died);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.died);
    }
}
