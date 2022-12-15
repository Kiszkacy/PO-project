package evolution.main;

import evolution.events.*;
import evolution.maps.AnimalMap;
import evolution.maps.PlantMap;
import evolution.util.Vector2;
import java.util.Arrays;

public class WorldEnvironment implements Environment, DeathObserver, MoveObserver, ConsumeObserver, ReproduceObserver {

    private Vector2 size;
    private PlantMap plantMap;
    private AnimalMap animalMap;
    private boolean[][] mateAvailability;


    public void init() {
        this.mateAvailability = new boolean[this.size.y][this.size.x];
        for(boolean[] row : this.mateAvailability)
            Arrays.fill(row, true);
    }


    public void newDay() {
        for(boolean[] row : this.mateAvailability)
            Arrays.fill(row, true);
    }


    public boolean placeAnimal(Animal animal) {
        this.animalMap.add(animal, animal.getPos());
        return true;
    }


    public boolean placePlant(Plant plant) {
        if (!this.plantMap.isEmpty(plant.getPos())) return false;
        this.plantMap.add(plant, plant.getPos());
        return true;
    }


    public boolean removeAnimal(Animal animal) {
        return this.animalMap.remove(animal, animal.getPos());
    }


    public boolean removePlant(Plant plant) {
        return this.plantMap.remove(plant, plant.getPos());
    }

    // overrides

    @Override
    public boolean isFoodAt(Vector2 where) {
        return !this.plantMap.isEmpty(where);
    }

    @Override
    public boolean isMateAt(Vector2 where) {
        return !this.animalMap.isEmpty(where);
    }

    @Override
    public Eatable getFood(Vector2 from) {
        return this.plantMap.getObjectsAt(from).getFirst();
    }

    @Override
    public Creature getMate(Vector2 from) {
        return null;
    }

    @Override
    public void onDeath(DeathEvent event) {
        Killable died = event.getDied();
        if (died instanceof Animal) this.removeAnimal((Animal)died);
        if (died instanceof Plant) this.removePlant((Plant)died);
    }

    @Override
    public void onConsume(ConsumeEvent event) {
        Plant plant = (Plant)event.getWhat();
        plant.die();
    }

    @Override
    public void onPositionChanged(MoveEvent event) {
        this.animalMap.move((Animal)event.getMoved(), event.getFrom(), event.getTo());
    }

    @Override
    public void onReproduce(ReproduceEvent event) {
        this.placeAnimal((Animal)event.getChild());
    }
    
    @Override
    public void update(Event event) { // TODO notify maps about events (ORDER IS IMPORTANT! (i think))
        if (event instanceof MoveEvent)
            this.onPositionChanged((MoveEvent)event);
        else if (event instanceof DeathEvent)
            this.onDeath((DeathEvent)event);
        else if (event instanceof ConsumeEvent)
            this.onConsume((ConsumeEvent)event);
        else if (event instanceof ReproduceEvent)
            this.onReproduce((ReproduceEvent)event);
    }

    // constructor

    public WorldEnvironment(Vector2 size, PlantMap plantMap, AnimalMap animalMap) {
        this.size = size;
        this.plantMap = plantMap;
        this.animalMap = animalMap;
        this.init();
    }

    // getters/setters

    public AnimalMap getAnimalMap() {
        return this.animalMap;
    }


    public Vector2 getSize() {
        return this.size;
    }
}
