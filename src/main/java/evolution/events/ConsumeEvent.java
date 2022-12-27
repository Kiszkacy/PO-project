package evolution.events;

import evolution.main.Creature;
import evolution.main.Eatable;
import java.util.Objects;

public class ConsumeEvent implements Event {

    private final Eatable what;
    private final Creature by;

    // overrides

    @Override
    public Object[] getAllData() {
        return new Object[]{this.what, this.by};
    }

    // constructors

    public ConsumeEvent(Eatable what, Creature by){
        this.what = what;
        this.by = by;
    }

    // getters/setters

    public Eatable getWhat() {
        return this.what;
    }


    public Creature getBy() {
        return this.by;
    }

    // hash & equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsumeEvent that = (ConsumeEvent) o;
        return Objects.equals(this.what, that.what) && Objects.equals(this.by, that.by);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.what, this.by);
    }
}
