package evolution.events;

public interface ConsumeObserver extends Observer {
    
    void onConsume(ConsumeEvent event);
}
