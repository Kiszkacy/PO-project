package evolution.maps;

import evolution.events.Event;
import evolution.main.Animal;
import evolution.util.Direction;
import evolution.util.Vector2;

public class Earth extends AnimalMap {

    @Override
    public void update(Event event) {
        // TODO update shouldn't do anything?
    }

    @Override
    public boolean move(Animal obj, Vector2 from, Vector2 to){
        Vector2 newPosition = to.copy();
        if (to.x < 0) newPosition.x = this.size.x-1;
        if (to.x >= this.size.x) newPosition.x = 0;
        if (to.y < 0) {
            newPosition.y = 0;
            obj.setDir(obj.getDir().rotate(Direction.DOWN));
        }
        if (to.y >= this.size.y){
            newPosition.y = this.size.y - 1;
            obj.setDir(obj.getDir().rotate(Direction.DOWN));
        }
        obj.setPos(newPosition);
        super.move(obj, from, newPosition);
        return true;
    }
}
