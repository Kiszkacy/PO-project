package evolution.maps;

import evolution.events.DeathEvent;
import evolution.events.DeathObserver;
import evolution.events.Event;
import evolution.events.MoveEvent;
import evolution.util.Vector2;

public class ToxicCorpses extends PlantMap implements DeathObserver {
    private int[][] deathMap;

    // constructors

    public ToxicCorpses(Vector2 size){
        super.init();
        this.deathMap = new int[size.y][size.x];
    }

    // overrides

    @Override
    public void generatePreferredTiles() {
        int amountOFTiles = size.y*size.x;
        int[] topValues = new int[amountOFTiles];
        Vector2[] topValuesIndexes = new Vector2[amountOFTiles];
        // can be optimized
        for(int i = 0; i < size.y; i++){
            for(int j = 0; j < size.x; j++){

            }
        }
    }

    @Override
    public void onDeath(DeathEvent event) {
        // TODO do something if thing that died was of type Animal
    }

    @Override
    public void update(Event event) {
        if (event instanceof DeathEvent)
            this.onDeath((DeathEvent) event);
    }
}