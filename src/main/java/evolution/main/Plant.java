package evolution.main;

import evolution.events.DeathEvent;
import evolution.events.Event;
import evolution.events.Observer;
import evolution.util.Config;
import evolution.util.Vector2;
import java.util.LinkedList;
import java.util.Objects;

public class Plant implements Eatable, Mappable {

    private Vector2 pos;
    private int nutritionalValue;
    private LinkedList<Observer> observers = new LinkedList<>();

    // overrides

    @Override
    public Killable die() {
        this.notify(new DeathEvent(this));
        return this;
    }

    @Override
    public boolean addObserver(Observer observer) {
        this.observers.add(observer);
        return true;
    }

    @Override
    public boolean removeObserver(Observer observer) {
        return this.observers.remove(observer);
    }

    @Override
    public void notify(Event event) {
        for(Observer o : observers) {
            o.update(event);
        }
    }

    // constructors

    public Plant() {
        this.pos = Vector2.ZERO();
        this.nutritionalValue = Config.getPlantNutritionalValue();
    }

    public Plant(Vector2 pos) {
        this.pos = pos;
        this.nutritionalValue = Config.getPlantNutritionalValue();
    }

    public Plant(int nutritionalValue) {
        this.pos = Vector2.ZERO();
        this.nutritionalValue = nutritionalValue;
    }

    public Plant(Vector2 pos, int nutritionalValue) {
        this.pos = pos;
        this.nutritionalValue = nutritionalValue;
    }

    // getters/setters

    @Override
    public Vector2 getPos() {
        return this.pos;
    }

    @Override
    public void setPos(Vector2 v) {
        this.pos = v;
    }

    @Override
    public int getNutritionalValue() {
        return this.nutritionalValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plant plant = (Plant) o;
        return nutritionalValue == plant.nutritionalValue && pos.equals(plant.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos);
    }
}
