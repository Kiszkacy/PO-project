package evolution.events;

public interface MoveObserver extends Observer {
    
    void onPositionChanged(MoveEvent event);
}
