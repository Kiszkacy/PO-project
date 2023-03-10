package evolution.main;

import evolution.brains.Brain;
import evolution.events.*;
import evolution.genomes.Genome;
import evolution.memories.AnimalMemory;
import evolution.memories.Memory;
import evolution.util.*;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

import static evolution.util.EasyPrint.pcol;

public class Animal implements Creature, Mappable {

    private Vector2 pos;
    private Direction dir;
    private final Brain brain;
    private int energy;
    private int aliveFor;
    private final Environment environment;
    private final LinkedList<Observer> observers = new LinkedList<>();

    /**
     * Increasing animal's age by one.
     */
    public void age() {
        this.aliveFor += 1;
    }

    // overrides

    @Override
    public void move() {
        this.brain.think();
        int geneType = this.brain.getActiveGene();
        this.dir = this.dir.rotate(Direction.values()[geneType]);
        Vector2 oldPosition = this.pos.copy();
        this.pos = this.pos.add(this.dir.vectorize());
        this.notify(new MoveEvent(this, oldPosition, this.pos.copy()));
        this.setEnergy(this.energy-1);
    }

    /**
     * Asks environment whether the food is available to animal,
     * if yes calls eat method
     * @return true if food is available, false otherwise
     */
    @Override
    public boolean lookForFood() {
        if(!this.environment.isFoodAt(this.pos, this)) return false;
        this.eat(this.environment.getFood(this.pos, this));
        return true;
    }

    /**
     * Eats given object gaining its getNutritionalValue and notifying observer of this event
     * @param what eatable object that will be consumed by this
     */
    @Override
    public void eat(Eatable what) {
        this.setEnergy(this.energy+what.getNutritionalValue());
        ((AnimalMemory)this.brain.getMemory()).setPlantsEaten(((AnimalMemory)this.brain.getMemory()).getPlantsEaten()+1);
        this.notify(new ConsumeEvent(what, this));
    }

    /**
     *  Checks if animal has enough energy to reproduce and if there is mate on its position
     *  if yes calls reproduce method
     * @return true if mate was found, false otherwise
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
            double genomeRatio = (((Animal)with).energy*1.0)/(this.energy + ((Animal)with).energy);
            Random rd = new Random();
            Genome childGenome = this.brain.getGenome().copy().mix(((Animal) with).brain.getGenome(), genomeRatio, rd.nextBoolean());
            int mutationCount = Config.getMutationCount().y-Config.getMutationCount().x;
            if (mutationCount != 0) childGenome.mutate(rd.nextInt(mutationCount) + Config.getMutationCount().x);
            Brain childBrain = (Brain)Class.forName("evolution.brains."+Config.getBrainType()).getConstructor(Memory.class, Genome.class).newInstance(new Object[]{new AnimalMemory(), childGenome});
            // energy transfer
            this.setEnergy(this.energy-Config.getReproduceEnergy());
            ((Animal) with).setEnergy(((Animal) with).energy - Config.getReproduceEnergy());
            // create child
            Animal child = new Animal(this.environment, this.pos.copy(), childBrain, Config.getReproduceEnergy()*2);
            // notify observers
            this.notify(new ReproduceEvent(this, with, child));
            // increase child count
            ((AnimalMemory)this.brain.getMemory()).setChildrenCount(((AnimalMemory)this.brain.getMemory()).getChildrenCount()+1);

            return child;
        } catch (Exception e) {
            ExceptionHandler.printCriticalInfo(e);
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

    public Animal(Environment environment) {
        this.pos = Vector2.ZERO();
        this.dir = Direction.values()[new Random().nextInt(Direction.size())];
        try {
            this.brain = (Brain)Class.forName("evolution.brains."+Config.getBrainType()).getConstructor(Memory.class).newInstance(new Object[]{new AnimalMemory()});
        } catch (Exception e) {
            throw new RuntimeException(String.format("brain type: '%s' not found", Config.getBrainType()));
        }
        this.energy = Config.getStartingAnimalEnergy();
        this.aliveFor = 0;
        this.environment = environment;
    }


    public Animal(Environment environment, Vector2 pos) {
        this.pos = pos;
        this.dir = Direction.values()[new Random().nextInt(Direction.size())];
        try {
            this.brain = (Brain)Class.forName("evolution.brains."+Config.getBrainType()).getConstructor(Memory.class).newInstance(new Object[]{new AnimalMemory()});
        } catch (Exception e) {
            throw new RuntimeException(String.format("brain type: '%s' not found", Config.getBrainType()));
        }
        this.energy = Config.getStartingAnimalEnergy();
        this.aliveFor = 0;
        this.environment = environment;
    }


    public Animal(Environment environment, Brain brain) {
        this.pos = Vector2.ZERO();
        this.dir = Direction.values()[new Random().nextInt(Direction.size())];
        this.brain = brain;
        this.energy = Config.getStartingAnimalEnergy();
        this.aliveFor = 0;
        this.environment = environment;
    }


    public Animal(Environment environment, Vector2 pos, Brain brain) {
        this.pos = pos;
        this.dir = Direction.values()[new Random().nextInt(Direction.size())];
        this.brain = brain;
        this.energy = Config.getStartingAnimalEnergy();
        this.aliveFor = 0;
        this.environment = environment;
    }


    public Animal(Environment environment, Vector2 pos, Brain brain, int energy) {
        this.pos = pos;
        this.dir = Direction.values()[new Random().nextInt(Direction.size())];
        this.brain = brain;
        this.energy = energy;
        this.aliveFor = 0;
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
        // checking this.energy > 0 to avoid using notify on already dead animal
        if (energy <= 0 && this.energy > 0) this.notify(new DeathEvent(this));
        this.energy = energy;
    }


    public int getAge() {
        return this.aliveFor;
    }


    public void setAge(int age) {
        this.aliveFor = age;
    }


    public Brain getBrain() {
        return this.brain;
    }


    public Direction getDir() {
        return dir;
    }


    public void setDir(Direction dir) {
        this.dir = dir;
    }

    // hash & equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return energy == animal.energy && pos.equals(animal.pos) && dir == animal.dir && brain.equals(animal.brain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos, dir, brain, energy);
    }
}
