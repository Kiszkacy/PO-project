package evolution.main;

import evolution.util.Config;
import evolution.util.Vector2;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Class responsible for handling objects and their positions in 2d space. Can hold multiple objects in the same place
 * and objects can occupy multiple tiles.
 * @param <T> what type of object is stored in this instance, object must implement Mappable interface
 */
public class Map<T extends Mappable> {
    protected final Vector2 size;
    protected LinkedList<T> objects = new LinkedList<T>();
    protected ArrayList<ArrayList<LinkedList<T>>> tiles;

    /**
     * This method is required to be called at the end of constructor.
     */
    protected void init() {
        this.tiles = new ArrayList<>(this.size.x);
        for(int i = 0; i < this.size.x; i++) {
            this.tiles.add(new ArrayList<LinkedList<T>>(this.size.y));
            for(int j = 0; j < this.size.y; j++)
                this.tiles.get(i).add(new LinkedList<T>());
        }
    }

    /**
     * Adds given object to a given tile.
     * @param obj object that will be appended
     * @param to position of a tile
     * @return true if object was added successfully
     */
    public boolean add(T obj, Vector2 to) {
        this.objects.add(obj);
        this.tiles.get(to.y).get(to.x).add(obj);
        return true;
    }

    /**
     * Removes given object from a given tile.
     * @param obj object that will be removed
     * @param from position of a tile
     * @return true if object was found and removed successfully
     */
    public boolean remove(T obj, Vector2 from) {
        if (!this.objects.removeIf(o -> o == obj)) return false; // comparing by reference !
        if (!this.tiles.get(from.y).get(from.x).removeIf(o -> o == obj)) return false; // comparing by reference !
        return true;
    }

    /**
     * Moves given object from a given tile to another tile.
     * @param obj object that will be moved
     * @param from position of a initial tile
     * @param to position of a target tile
     * @return true if object was found and moved successfully
     */
    public boolean move(T obj, Vector2 from, Vector2 to) {
        if (!this.tiles.get(from.y).get(from.x).removeIf(o -> o == obj)) return false; // comparing by reference !
        this.tiles.get(to.y).get(to.x).add(obj);
        return true;
    }

    /**
     * Returns amount of objects at a given square.
     * @param square position of a square
     * @return amount of objects
     */
    public int sizeOf(Vector2 square) {
        return this.tiles.get(square.y).get(square.x).size();
    }

    /**
     * Returns information if a given square is empty.
     * @param square position of a square
     * @return true if square is empty, false otherwise
     */
    public boolean isEmpty(Vector2 square) {
        return this.tiles.get(square.y).get(square.x).isEmpty();
    }

    /**
     * Returns list of all objects on a given square.
     * @param square position of a square
     * @return list of all objects on that square
     */
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
            throw new IllegalArgumentException(String.format("size parameter should have positive values, size: %s", size));
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
