package evolution.main;

import evolution.brains.Brain;
import evolution.events.*;
import evolution.genomes.Genome;
import evolution.util.Color;
import evolution.util.Config;
import evolution.util.Direction;
import evolution.util.Vector2;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

import static evolution.util.EasyPrint.p;
import static evolution.util.EasyPrint.pcol;

public class Animal implements Creature, Mappable {

    private Vector2 pos;
    private Direction dir;
    private Brain brain;
    private int energy;
    private Environment environment;
    private final LinkedList<Observer> observers = new LinkedList<>();

    // overrides

    @Override
    public void move() {
        this.setEnergy(this.energy-1);
        this.brain.think();
        int geneType = this.brain.getActiveGene();
        this.dir = this.dir.rotate(Direction.values()[geneType]);
        Vector2 oldPosition = this.pos.copy();
        this.pos = this.pos.add(this.dir.vectorize());
        this.notify(new MoveEvent(this, oldPosition, this.pos));
    }

    /**
     * Asks environment whether the food is available to animal,
     * if yes calls eat method and returns true
     * otherwise returns false
     */
    @Override
    public boolean lookForFood() {
        // TODO ask environment for food
        if(!this.environment.isFoodAt(this.pos)) return false;
        this.eat(this.environment.getFood(this.pos));
        return true;
    }

    /**
     * Eats given object gaining its getNutritionalValue and notifying observer of this event
     * @param what eatable object that will be consumed by this
     */
    @Override
    public void eat(Eatable what) {
        this.energy += what.getNutritionalValue();
        this.notify(new ConsumeEvent(what, this));
    }

    /**
     *  Checks if animal has enough energy to reproduce and if there is mate on its position
     *  if yes calls reproduce method and return true otherwise returns false
     * @return if child
     */
    @Override
    public boolean lookForMate() {
        if (this.energy < Config.getReproduceRequiredEnergy()) return false;
        if (!this.environment.isMateAt(this.pos, this)) return false;

        this.reproduce(this.environment.getMate(this.pos, this));
        return true;
    }

    /**
     * Creates new Animal object, child with the same position as self, energy equal to 2 times ReproduceRequiredEnergy
     * and genomes being a mix of parens genomes in ratio equal to their energy. Decreases both parents energy by
     * ReproduceRequiredEnergy.
     * @param with second parent
     * @return Animal child
     */
    @Override
    public Creature reproduce(Creature with) {
        try{
            // create new child components
            double genomeRatio = (((Animal)with).energy*1.0f)/(this.energy + ((Animal)with).energy);
            Random rd = new Random();
            Genome childGenome = this.brain.getGenome().copy().mix(((Animal) with).brain.getGenome(), genomeRatio, rd.nextBoolean());
            childGenome.mutate(rd.nextInt(childGenome.getSize()));
            Brain childBrain = (Brain)Class.forName("evolution.brains."+Config.getBrainType()).getConstructor(Genome.class).newInstance(new Object[]{childGenome});
            // energy transfer
            this.setEnergy(this.energy-Config.getReproduceEnergy());
            ((Animal) with).setEnergy(((Animal) with).energy - Config.getReproduceEnergy());
            // create child
            Animal child = new Animal(this.pos, childBrain, Config.getReproduceRequiredEnergy()*2, this.environment);
            // notify observers
            this.notify(new ReproduceEvent(this, with, child));
            return child;

        } catch (Exception e) {
            pcol(Color.RED, e.getMessage());
            pcol(Color.RED, "at: " + e.getStackTrace()[0].getClassName());
            pcol(Color.RED, "inside: " + e.getStackTrace()[0].getMethodName());
            pcol(Color.RED, "line: " + e.getStackTrace()[0].getLineNumber());
            throw new RuntimeException(String.format("problem creating object of class: '%s'", Config.getBrainType()));
        }
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
    public Animal() {
        this.pos = Vector2.ZERO();
        this.dir = Direction.values()[new Random().nextInt(Direction.size())];
        try {
            this.brain = (Brain)Class.forName("evolution.brains."+Config.getBrainType()).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("brain type: '%s' not found", Config.getBrainType()));
        }
        this.energy = Config.getStartingAnimalEnergy();
    }

    public Animal(Vector2 pos) {
        this.pos = pos;
        this.dir = Direction.values()[new Random().nextInt(Direction.size())];
        try {
            this.brain = (Brain)Class.forName("evolution.brains."+Config.getBrainType()).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("brain type: '%s' not found", Config.getBrainType()));
        }
        this.energy = Config.getStartingAnimalEnergy();
    }

    public Animal(Brain brain) {
        this.pos = Vector2.ZERO();
        this.dir = Direction.values()[new Random().nextInt(Direction.size())];
        this.brain = brain;
        this.energy = Config.getStartingAnimalEnergy();
    }

    public Animal(Vector2 pos, Environment environment) {
        this.pos = pos;
        this.dir = Direction.values()[new Random().nextInt(Direction.size())];
        try {
            this.brain = (Brain)Class.forName("evolution.brains."+Config.getBrainType()).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("brain type: '%s' not found", Config.getBrainType()));
        }
        this.energy = Config.getStartingAnimalEnergy();
        this.environment = environment;
    }

    public Animal(Vector2 pos, Brain brain) {
        this.pos = pos;
        this.dir = Direction.values()[new Random().nextInt(Direction.size())];
        this.brain = brain;
        this.energy = Config.getStartingAnimalEnergy();
    }

    public Animal(Vector2 pos, Brain brain, Environment environment) {
        this.pos = pos;
        this.dir = Direction.values()[new Random().nextInt(Direction.size())];
        this.brain = brain;
        this.energy = Config.getStartingAnimalEnergy();
        this.environment = environment;
    }

    public Animal(Vector2 pos, Brain brain, int energy, Environment environment) {
        this.pos = pos;
        this.dir = Direction.values()[new Random().nextInt(Direction.size())];
        this.brain = brain;
        this.energy = energy;
        this.environment = environment;
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
        if (energy <= 0) this.notify(new DeathEvent(this));
        this.energy = energy;
    }

    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }

    // hash

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        // TODO Brain equals function
        return energy == animal.energy && pos.equals(animal.pos) && dir == animal.dir && brain.equals(animal.brain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos, dir, brain, energy);
    }
}
