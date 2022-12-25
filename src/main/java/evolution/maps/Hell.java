package evolution.maps;

import evolution.events.Event;
import evolution.main.Animal;
import evolution.util.Config;
import evolution.util.Direction;
import evolution.util.Vector2;

import java.util.Random;

public class Hell extends AnimalMap {

    @Override
    public void update(Event event) {
        // TODO update shouldn't do anything?
    }

    @Override
    public boolean move(Animal obj, Vector2 from, Vector2 to){
        Vector2 newPosition = to.copy();
        if(to.x < 0 || to.x >= size.x || to.y < 0 || to.y >= size.y){
            Random rd = new Random();
            newPosition = new Vector2(rd.nextInt(size.x), rd.nextInt(size.y));
        }
        obj.setPos(newPosition);
        super.move(obj, from, newPosition);
        if(!obj.getPos().equals(to)) obj.setEnergy(obj.getEnergy() - Config.getReproduceEnergy());
        return true;
    }
}
