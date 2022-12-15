package evolution.main;

import evolution.brains.Brain;
import evolution.events.DeathEvent;
import evolution.events.Event;
import evolution.events.Observer;
import evolution.util.Direction;
import evolution.util.Vector2;
import java.util.LinkedList;
import java.util.Random;

public class Animal implements Creature, Mappable {

    private Vector2 pos;
    private Direction dir;
    private Brain brain;
    private int energy;
    private Environment environment;
    private LinkedList<Observer> observers = new LinkedList<>();

    // overrides

    @Override
    public void move() {
        this.brain.think();
        int geneType = this.brain.getActiveGene();
        // TODO rotate dir by geneType dir type
        this.pos.add(this.dir.vectorize());
    }

    @Override
    public boolean lookForFood() {
        // TODO ask environment for food
        return false;
    }

    @Override
    public void eat(Eatable what) {
        this.energy += what.getNutritionalValue();
        // TODO notify observers
    }

    @Override
    public boolean lookForMate() {
        // TODO ask environment for mate
        return false;
    }

    @Override
    public Creature reproduce(Creature with) {
        // TODO notify observers
        // TODO return children
        return this;
    }

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

    @Override
    public String toString() {
        return String.format("<genome:'%s'; energy:'%d'; at %s>", this.brain.getGenome(), this.energy, this.pos);
    }

    // constructors
    // TODO environment
    public Animal() {
        this.pos = Vector2.ZERO();
        this.dir = Direction.values()[new Random().nextInt(Direction.size())];
//        this.brain = new Brain();
        // TODO default brain ?
    }

    public Animal(Vector2 pos) {
        this.pos = pos;
        this.dir = Direction.values()[new Random().nextInt(Direction.size())];
//        this.brain = new Brain();
        // TODO default brain ?
    }

    public Animal(Brain brain) {
        this.pos = Vector2.ZERO();
        this.dir = Direction.values()[new Random().nextInt(Direction.size())];
        this.brain = brain;
    }

    public Animal(Vector2 pos, Brain brain) {
        this.pos = pos;
        this.dir = Direction.values()[new Random().nextInt(Direction.size())];
        this.brain = brain;
    }

    // getters/setters

    @Override
    public Vector2 getPos() {
        return this.pos;
    }

    @Override
    public void setPos(Vector2 pos) {
        this.pos = pos;
    }


    public int getEnergy() {
        return this.energy;
    }


    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
