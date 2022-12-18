package evolution.main;

import evolution.util.Config;
import evolution.util.Vector2;
import java.util.ArrayList;
import java.util.LinkedList;

public class Map<T extends Mappable> {
    protected final Vector2 size;
    // TODO implement array of MAX_SIZE to which new objects will be added/removed from instead of list ?
    protected LinkedList<T> objects = new LinkedList<T>();
    protected ArrayList<ArrayList<LinkedList<T>>> tiles;


    protected void init() {
        this.tiles = new ArrayList<>(this.size.x);
        for(int i = 0; i < this.size.x; i++) {
            this.tiles.add(new ArrayList<LinkedList<T>>(this.size.y));
            for(int j = 0; j < this.size.y; j++)
                this.tiles.get(i).add(new LinkedList<T>());
        }
    }


    public boolean add(T obj, Vector2 to) {
        this.objects.add(obj);
        this.tiles.get(to.y).get(to.x).add(obj);
        return true;
    }


    public boolean remove(T obj, Vector2 from) {
        this.objects.remove(obj);
        this.tiles.get(from.y).get(from.x).remove(obj);
        return true;
    }


    public boolean move(T obj, Vector2 from, Vector2 to) {
        this.tiles.get(from.y).get(from.x).remove(obj);
        this.tiles.get(to.y).get(to.x).add(obj);
        return true;
    }


    public int sizeOf(Vector2 square) {
        return this.tiles.get(square.y).get(square.x).size();
    }


    public boolean isEmpty(Vector2 square) {
        return this.tiles.get(square.y).get(square.x).isEmpty();
    }


    public LinkedList<T> getObjectsAt(Vector2 square) {
        return this.tiles.get(square.y).get(square.x);
    }

    // overrides

    @Override
    public String toString() {
        String result = "Y/X |";
        for(int i = 0; i < this.size.x; i++)
            result += String.format(" %2d", i);
        result += "\n";
        for(int i = -2; i < this.size.x; i++)
            result += String.format("---", i);
        result += "\n";

        for(int y = 0; y < this.size.y; y++) {
            result += String.format(" %2d |", y);
            for(int x = 0; x < this.size.x; x++) {
                result += String.format(" %2d", this.tiles.get(y).get(x).size());
            }
            result += "\n";
        }

        return result;
    }

    // constructors

    public Map() {
        this.size = Config.getMapSize();
        this.init();
    }


    public Map(Vector2 size) {
        if (!(size.x > 0 && size.y > 0)) {
            throw new IllegalArgumentException(String.format("size parameter should have positive values, size: %s", size.toString()));
        }
        this.size = size;
        this.init();
    }

    // getters/setters

    public Vector2 getSize() {
        return this.size;
    }

    public LinkedList<T> getObjects() {
        return this.objects;
    }

    public ArrayList<ArrayList<LinkedList<T>>> getTiles() {
        return tiles;
    }
}
