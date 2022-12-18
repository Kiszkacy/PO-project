package evolution.maps;

import evolution.events.DeathEvent;
import evolution.events.DeathObserver;
import evolution.events.Event;
import evolution.events.MoveEvent;
import evolution.main.Animal;
import evolution.util.Vector2;

public class ToxicCorpses extends PlantMap implements DeathObserver {
    private int[][] deathMap;

    // constructors

    public ToxicCorpses(Vector2 size){
        super.init();
        this.deathMap = new int[size.y][size.x];
    }

    // overrides

    // TODO return or save somewhere PreferredTiles
    @Override
    public void generatePreferredTiles() {
        int amountOFTiles = size.y*size.x;
        // values are in increasing order (i.e 0 index is has smallest value)
        int[] topValues = new int[(int) Math.ceil(amountOFTiles * 0.2)];
        Vector2[] topValuesIndexes = new Vector2[(int) Math.ceil(amountOFTiles * 0.2)];
        // can be optimized probably, for example using older topValues

        for(int i = 0; i < size.y; i++){
            for(int j = 0; j < size.x; j++){

                int amountOfDeath = deathMap[i][j];
                if (topValues[0] >= amountOfDeath) continue;
                // insert new value in ordered manner

                topValues[0] = amountOfDeath;
                Vector2 position = new Vector2(j, i);
                topValuesIndexes[0] = position;

                for (int k = 1; k < topValues.length && amountOfDeath > topValues[k]; k++){
                    topValues[k-1] = topValues[k];
                    topValues[k] = amountOfDeath;
                    topValuesIndexes[k-1] = topValuesIndexes[k];
                    topValuesIndexes[k] = position;
                }
            }
        }
    }

    /**
     * If event is death of animal, increment value of death map on index equal to its position
     * @param event death of killable object
     */
    @Override
    public void onDeath(DeathEvent event) {
        if (event.getDied() instanceof Animal){
            Vector2 position = ((Animal) event.getDied()).getPos();
            deathMap[position.y][position.x] += 1;
        }
    }

    @Override
    public void update(Event event) {
        if (event instanceof DeathEvent)
            this.onDeath((DeathEvent) event);
    }
}