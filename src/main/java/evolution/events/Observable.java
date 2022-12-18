package evolution.events;

/**
 * Defines object that can create some kind of observable event.
 */
public interface Observable {

    boolean addObserver(Observer observer);
    boolean removeObserver(Observer observer);
    void notify(Event event);
}
