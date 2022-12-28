package evolution.util;

/**
 * Container holding two objects of predefined types, otherwise known as a two elemental tuple.
 * @param <A> type of the first element
 * @param <B> type of the second element
 */
public class Pair<A, B> {

    public A first;
    public B second;


    // overrides

    @Override
    public String toString() {
        return String.format("<%s, %s>", this.first, this.second);
    }

    // constructors

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }
}
