package evolution.events;

import evolution.main.Creature;
import evolution.main.Eatable;

public class ConsumeEvent implements Event {

    private final Eatable what; // TODO no constructor error
    private final Creature by;

    // overrides

    @Override
    public Object[] getAllData() {
        return new Object[]{this.what, this.by};
    }

    // constructors

    // TODO

    // getters/setters

    public Eatable getWhat() {
        return this.what;
    }


    public Creature getBy() {
        return this.by;
    }
}
