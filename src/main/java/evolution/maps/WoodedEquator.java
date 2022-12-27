package evolution.maps;

import evolution.events.Event;
import evolution.util.Vector2;
import java.util.ArrayList;


/**
 * Plants prefer to grow on the equator.
 */
public class WoodedEquator extends PlantMap {

    // overrides

    /**
     * Generates preferredTiles and notPreferredTiles,
     * preferredTiles consists of vertical strip in the middle of map, of width 0.2*size.y rounded up (about 20% of map)
     */
    @Override
    public void generatePreferredTiles() {
        if (this.preferredTiles.size() != 0) return; // calculate only once

        int equatorHeight = (int) Math.ceil(this.size.y * 0.2);
        int equatorStart = (int) Math.ceil((this.size.y-equatorHeight) / 2.0);
        for (int i = 0; i < this.size.y; i++) {
            for (int j = 0; j < this.size.x; j++) {
                if (equatorStart <= i && i < equatorStart + equatorHeight) this.preferredTiles.add(new Vector2(j,i));
                else this.notPreferredTiles.add(new Vector2(j,i));
            }
        }
    }

    @Override
    public void update(Event event) {

    }

    // constructors

    public WoodedEquator(){
        super();
        this.preferredTiles = new ArrayList<>();
        this.notPreferredTiles = new ArrayList<>();
    }


    public WoodedEquator(Vector2 size){
        super(size);
        this.preferredTiles = new ArrayList<>();
        this.notPreferredTiles = new ArrayList<>();

    }
}
