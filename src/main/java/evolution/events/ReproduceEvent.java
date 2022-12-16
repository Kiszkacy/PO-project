package evolution.events;

import evolution.main.Creature;

public class ReproduceEvent implements Event {

    private final Creature firstParent; // TODO no constructor error
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
}
