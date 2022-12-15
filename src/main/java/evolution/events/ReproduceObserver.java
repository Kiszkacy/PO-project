package evolution.events;

public interface ReproduceObserver extends Observer {
    
    void onReproduce(ReproduceEvent event);
}
