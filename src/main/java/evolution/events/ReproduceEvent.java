package evolution.events;

import evolution.main.Creature;
import java.util.Objects;

public class ReproduceEvent implements Event {

    private final Creature firstParent;
    private final Creature secondParent;
    private final Creature child;

    // overrides

    @Override
    public Object[] getAllData() {
        return new Object[]{this.firstParent, this.secondParent, this.child};
    }

    // constructors

    public ReproduceEvent(Creature firstParent, Creature secondParent, Creature child) {
        this.firstParent = firstParent;
        this.secondParent = secondParent;
        this.child = child;
    }

    // getters/setters

    public Creature getFirstParent() {
        return this.firstParent;
    }


    public Creature getSecondParent() {
        return this.secondParent;
    }


    public Creature getChild() {
        return this.child;
    }

    // hash & equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReproduceEvent that = (ReproduceEvent) o;
        return Objects.equals(this.firstParent, that.firstParent) && Objects.equals(this.secondParent, that.secondParent) && Objects.equals(this.child, that.child);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.firstParent, this.secondParent, this.child);
    }
}
