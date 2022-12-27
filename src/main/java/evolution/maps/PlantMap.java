package evolution.maps;

import evolution.events.Observer;
import evolution.main.Map;
import evolution.main.Plant;
import evolution.util.Vector2;
import java.util.List;

public abstract class PlantMap extends Map<Plant> implements Observer {

    protected List<Vector2> preferredTiles;
    protected List<Vector2> notPreferredTiles;

    public abstract void generatePreferredTiles();

    // constructors

    public PlantMap() {
        super();
    }


    public PlantMap(Vector2 size) {
        super(size);
    }

    // getters/setters

    public List<Vector2> getPreferredTiles() {
        return preferredTiles;
    }

    public List<Vector2> getNotPreferredTiles() {
        return notPreferredTiles;
    }
}
