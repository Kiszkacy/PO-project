package evolution.maps;

import evolution.events.Observer;
import evolution.main.Animal;
import evolution.main.Map;
import evolution.util.Vector2;

public abstract class AnimalMap extends Map<Animal> implements Observer {

    // constructors

    public AnimalMap() {
        super();
    }


    public AnimalMap(Vector2 size) {
        super(size);
    }
}
