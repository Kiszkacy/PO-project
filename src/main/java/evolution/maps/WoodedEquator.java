package evolution.maps;

import evolution.events.Event;
import evolution.util.Vector2;

import java.util.ArrayList;

public class WoodedEquator extends PlantMap {

    // overrides

    /**
     * Generates preferredTiles and notPreferredTiles,
     * preferredTiles consists of vertical strip in the middle of map, of width 0.2*size.y
     */
    @Override
    public void generatePreferredTiles() {
        if (preferredTiles.size() != 0) return;

        int equatorHeight = (int) Math.ceil(size.y * 0.2);
        for (int i = 0; i < size.y; i++) {
            for (int j = 0; j < size.x; j++) {
                if (equatorHeight < i && i < size.y - equatorHeight ) preferredTiles.add(new Vector2(j,i));
                else notPreferredTiles.add(new Vector2(j,i));
            }
        }
    }

    @Override
    public void update(Event event) {
        // TODO finish update
        // update does nothing probably
    }

    // constructors
    public WoodedEquator(){
        super();
        preferredTiles = new ArrayList<>();
        notPreferredTiles = new ArrayList<>();
    }

    public WoodedEquator(Vector2 size){
        super(size);
        preferredTiles = new ArrayList<>();
        notPreferredTiles = new ArrayList<>();
    }
}
