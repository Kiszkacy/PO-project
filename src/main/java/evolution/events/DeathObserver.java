package evolution.events;

public interface DeathObserver extends Observer {
    
    void onDeath(DeathEvent event);
}
