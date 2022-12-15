package evolution.events;

import evolution.main.Killable;
import evolution.main.Plant;

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
}
