package evolution.maps;

import evolution.events.Observer;
import evolution.main.Map;
import evolution.main.Plant;
import evolution.util.Vector2;
import java.util.LinkedList;

public abstract class PlantMap extends Map<Plant> implements Observer {

    protected LinkedList<Vector2> preferredTiles;
    protected LinkedList<Vector2> notPreferredTiles;


    abstract void generatePreferredTiles();

    // constructors

    public PlantMap() {
        super();
    }


    public PlantMap(Vector2 size) {
        super(size);
    }
}
