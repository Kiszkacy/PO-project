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

    // TODO

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
