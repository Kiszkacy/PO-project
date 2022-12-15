package evolution.events;

public interface Observable {

    boolean addObserver(Observer observer);
    boolean removeObserver(Observer observer);
    void notify(Event event);
}
