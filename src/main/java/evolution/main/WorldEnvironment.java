package evolution.main;

import evolution.events.*;
import evolution.maps.AnimalMap;
import evolution.maps.PlantMap;
import evolution.util.Config;
import evolution.util.Vector2;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Communicates with all organisms alive in simulation. In control of maps containing animals' and plants'
 * positions. Responsible for handling all Animal's events. Specifies reproduction conditions.
 */
public class WorldEnvironment implements Environment, DeathObserver, MoveObserver, ConsumeObserver, ReproduceObserver {

    private Vector2 size;
    private PlantMap plantMap;
    private AnimalMap animalMap;
    private boolean[][] mateAvailability;

    /**
     * This method is required to be called at the end of constructor.
     */
    private void init() {
        this.mateAvailability = new boolean[this.size.y][this.size.x];
        for(boolean[] row : this.mateAvailability)
            Arrays.fill(row, true);
    }

    /**
     * Should be called once at the start of a simulation day. Resets all required data.
     */
    public void newDay() {
        for(boolean[] row : this.mateAvailability)
            Arrays.fill(row, true);
    }

    /**
     * Adds animal to environment to its corresponding position. There can be multiple animals on the same position.
     * @param animal animal that will be placed
     * @return true if placement is successful, false otherwise
     */
    public boolean placeAnimal(Animal animal) {
        this.animalMap.add(animal, animal.getPos());
        animal.addObserver(this);
        return true;
    }

    /**
     * Adds plant to environment to its corresponding position. There can be only one plant on a given position.
     * @param plant plant that will be placed
     * @return true if placement is successful, false otherwise
     */
    public boolean placePlant(Plant plant) {
        if (!this.plantMap.isEmpty(plant.getPos())) return false;
        this.plantMap.add(plant, plant.getPos());
        plant.addObserver(this);
        return true;
    }

    /**
     * Removes given animal from the environment using its position to find it.
     * @param animal animal that will be removed
     * @return true if animal was successfully removed, false if animal was not found
     */
    public boolean removeAnimal(Animal animal) {
        return this.animalMap.remove(animal, animal.getPos());
    }

    /**
     * Removes given plant from the environment using its position to find it.
     * @param plant plant that will be removed
     * @return true if plant was successfully removed, false if animal was not found
     */
    public boolean removePlant(Plant plant) {
        return this.plantMap.remove(plant, plant.getPos());
    }

    // overrides

    @Override
    public boolean isFoodAt(Vector2 where) {
        return !this.plantMap.isEmpty(where);
    }

    @Override
    public boolean isMateAt(Vector2 where, Creature candidate) {
        if (!mateAvailability[where.y][where.x]) return false; // check if available
        if (this.animalMap.sizeOf(where) <= 1) return false; // check if there are at least 2 animals

        LinkedList<Animal> animals = this.animalMap.getObjectsAt(where);
        animals.sort((a1, a2) -> { // TODO should this be implemented in animalMap insert/move ?
            if (a1.getEnergy() > a2.getEnergy()) return 1;
            else if (a1.getEnergy() < a2.getEnergy()) return -1;
            return 0;
        });
        int idx = animals.indexOf((Animal)candidate);
        if (idx >= 2) return false; // candidate must be in top 2

        if (animals.get(1-idx).getEnergy() < Config.getReproduceRequiredEnergy()) return false; // other animal must have required energy
        return true;
    }

    @Override
    public Eatable getFood(Vector2 from) {
        return this.plantMap.getObjectsAt(from).getFirst();
    }

    @Override
    public Creature getMate(Vector2 from, Creature candidate) {
        // everything already checked in isMateAt method
        this.mateAvailability[from.y][from.x] = false;
        LinkedList<Animal> animals = this.animalMap.getObjectsAt(from);
        int idx = animals.indexOf((Animal)candidate);
        return animals.get(1-idx);
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
    public void update(Event event) {
        this.plantMap.update(event); // IMPORTANT: maps are notified before events are handled
        this.animalMap.update(event);

        if (event instanceof MoveEvent)
            this.onPositionChanged((MoveEvent)event);
        else if (event instanceof DeathEvent)
            this.onDeath((DeathEvent)event);
        else if (event instanceof ConsumeEvent)
            this.onConsume((ConsumeEvent)event);
        else if (event instanceof ReproduceEvent)
            this.onReproduce((ReproduceEvent)event);
    }

    // constructors

    public WorldEnvironment() {
        this.size = Config.getMapSize();

        try {
            this.plantMap = (PlantMap)Class.forName("evolution.maps."+Config.getPlantMapType()).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("plantMap type: '%s' not found", Config.getPlantMapType()));
        }

        try {
            this.animalMap = (AnimalMap)Class.forName("evolution.maps."+Config.getAnimalMapType()).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("animalMap type: '%s' not found", Config.getAnimalMapType()));
        }

        this.init();
    }


    public WorldEnvironment(Vector2 size) { // TODO size exceptions
        this.size = size;

        try {
            this.plantMap = (PlantMap)Class.forName("evolution.maps."+Config.getPlantMapType()).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("plantMap type: '%s' not found", Config.getPlantMapType()));
        }

        try {
            this.animalMap = (AnimalMap)Class.forName("evolution.maps."+Config.getAnimalMapType()).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("animalMap type: '%s' not found", Config.getAnimalMapType()));
        }

        this.init();
    }


    public WorldEnvironment(PlantMap plantMap, AnimalMap animalMap) {
        this.size = Config.getMapSize();
        this.plantMap = plantMap;
        this.animalMap = animalMap;
        this.init();
    }


    public WorldEnvironment(Vector2 size, PlantMap plantMap, AnimalMap animalMap) { // TODO size exceptions
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
