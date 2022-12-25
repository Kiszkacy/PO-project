package evolution.maps;

import evolution.events.Observer;
import evolution.main.Map;
import evolution.main.Plant;
import evolution.util.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static evolution.util.EasyPrint.p;

public abstract class PlantMap extends Map<Plant> implements Observer {

    protected List<Vector2> preferredTiles;
    protected List<Vector2> notPreferredTiles;


    public abstract void generatePreferredTiles();

    public Plant newPlant(){
        Random rd = new Random();
        float probability = rd.nextFloat();
        List<Vector2> chooseTiles;
        if (probability > 0.2) chooseTiles = preferredTiles;
        else chooseTiles = notPreferredTiles;
        Vector2 plantPosition = chooseTiles.get(rd.nextInt(chooseTiles.size()));
//        p(plantPosition);
//        for (Vector2 vec : chooseTiles) {
//            p(vec);
//        }
        return new Plant(plantPosition);
    }

    // constructors

    public PlantMap() {
        super();
    }


    public PlantMap(Vector2 size) {
        super(size);
    }

    // getters/ setters


    public List<Vector2> getPreferredTiles() {
        return preferredTiles;
    }

    public List<Vector2> getNotPreferredTiles() {
        return notPreferredTiles;
    }
}
