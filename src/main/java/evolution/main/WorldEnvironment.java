package evolution.main;

import evolution.events.*;
import evolution.maps.AnimalMap;
import evolution.maps.PlantMap;
import evolution.memories.AnimalMemory;
import evolution.util.Color;
import evolution.util.Config;
import evolution.util.Vector2;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

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

        // sort animals by predefined conditions so isFoodAt and isAnimalAt methods can easily check if animal is the most powerful creature on that tile
        for(int y = 0; y < this.size.y; y++) {
            for(int x = 0; x < this.size.x; x++) {
                LinkedList<Animal> animals = this.animalMap.getObjectsAt(new Vector2(x, y));
                animals.sort((a1, a2) -> {
                    if (a1.getEnergy() > a2.getEnergy()) return 1;
                    else if (a1.getEnergy() < a2.getEnergy()) return -1;
                    if (a1.getAge() > a2.getAge()) return 1;
                    else if (a1.getAge() < a2.getAge()) return -1;
                    if (((AnimalMemory)a1.getBrain().getMemory()).getChildrenCount() > ((AnimalMemory)a2.getBrain().getMemory()).getChildrenCount()) return 1;
                    else if (((AnimalMemory)a1.getBrain().getMemory()).getChildrenCount() < ((AnimalMemory)a2.getBrain().getMemory()).getChildrenCount()) return -1;
                    return new Random().nextInt(3)-1; // random -1,0,1
                });
            }
        }
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
     * @throws RuntimeException if animal could not be found on the map
     */
    public void removeAnimal(Animal animal) throws RuntimeException {
        if (!this.animalMap.remove(animal, animal.getPos())) {
            throw new RuntimeException(String.format("could not remove (cannot be found): '%s' from: %s", animal, animal.getPos()));
        }
    }

    /**
     * Removes given plant from the environment using its position to find it.
     * @param plant plant that will be removed
     * @throws RuntimeException if plant could not be found on the map
     */
    public void removePlant(Plant plant) {
        if (!this.plantMap.remove(plant, plant.getPos()))
            throw new RuntimeException(String.format("could not remove (cannot be found): '%s' from: %s", plant, plant.getPos()));
    }

    // overrides

    @Override
    public boolean isFoodAt(Vector2 where, Creature candidate) {
        if (this.plantMap.isEmpty(where)) return false;
        LinkedList<Animal> animals = this.animalMap.getObjectsAt(where);
        if (animals.indexOf((Animal)candidate) > 0) return false;

        return true;
    }

    @Override
    public boolean isMateAt(Vector2 where, Creature candidate) {
        if (!mateAvailability[where.y][where.x]) return false; // check if available
        if (this.animalMap.sizeOf(where) <= 1) return false; // check if there are at least 2 animals

        LinkedList<Animal> animals = this.animalMap.getObjectsAt(where);
        int idx = animals.indexOf((Animal)candidate);
        if (idx >= 2) return false; // candidate must be in top 2
        if (animals.get(1-idx).getEnergy() < Config.getReproduceRequiredEnergy()) return false; // other animal must have required energy

        return true;
    }

    @Override
    public Eatable getFood(Vector2 from, Creature candidate) {
        if (this.plantMap.isEmpty(from)) return null;
        LinkedList<Animal> animals = this.animalMap.getObjectsAt(from);
        if (animals.indexOf((Animal)candidate) > 0) return null;

        return this.plantMap.getObjectsAt(from).getFirst();
    }

    @Override
    public Creature getMate(Vector2 from, Creature candidate) {
        if (!mateAvailability[from.y][from.x]) return null; // check if available
        if (this.animalMap.sizeOf(from) <= 1) return null; // check if there are at least 2 animals

        LinkedList<Animal> animals = this.animalMap.getObjectsAt(from);
        int idx = animals.indexOf((Animal)candidate);
        if (idx >= 2) return null; // candidate must be in top 2
        if (animals.get(1-idx).getEnergy() < Config.getReproduceRequiredEnergy()) return null; // other animal must have required energy

        this.mateAvailability[from.y][from.x] = false;
        return animals.get(1-idx);
    }

    @Override
    public void onDeath(DeathEvent event) {
        Killable died = event.getDied();
        if (died instanceof Animal) this.removeAnimal((Animal)died);
        if (died instanceof Plant)  this.removePlant((Plant)died);
    }

    @Override
    public void onConsume(ConsumeEvent event) {
        Plant plant = (Plant)event.getWhat();
        plant.die();
    }

    @Override
    public void onPositionChanged(MoveEvent event) {
        if (!this.animalMap.move((Animal)event.getMoved(), event.getFrom(), event.getTo()))
            throw new RuntimeException(String.format("MOVE: could not remove (cannot be found): '%s' from: %s", event.getMoved(), (event.getMoved()).getPos()));
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


    public WorldEnvironment(Vector2 size) {
        if (size.x <= 0 || size.y <= 0) throw new RuntimeException(String.format("WorldEnvironment size must be positive, received: '%s'", size));
        if (size.x > Config.getMapSize().x || size.y > Config.getMapSize().y)
            throw new RuntimeException(String.format("environment size must be equal or lower to map size, received: '%s' expected at most: '%s", size, Config.getMapSize()));

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
        if (!(plantMap.getSize().equals(animalMap.getSize())))
            throw new RuntimeException(String.format("animalMap and plantMap must be of the same size, received: '%s' and '%s'", animalMap.getSize(), plantMap.getSize()));

        this.size = Config.getMapSize();
        this.plantMap = plantMap;
        this.animalMap = animalMap;
        this.init();
    }


    public WorldEnvironment(Vector2 size, PlantMap plantMap, AnimalMap animalMap) {
        if (size.x <= 0 || size.y <= 0) throw new RuntimeException(String.format("WorldEnvironment size must be positive, received: '%s'", size));
        if (!(plantMap.getSize().equals(animalMap.getSize())))
            throw new RuntimeException(String.format("animalMap and plantMap must be of the same size, received: '%s' and '%s'", animalMap.getSize(), plantMap.getSize()));
        if (size.x > animalMap.getSize().x || size.y > animalMap.getSize().y)
            throw new RuntimeException(String.format("environment size must be equal or lower to map size, received: '%s' expected at most: '%s", size, animalMap.getSize()));

        this.size = size;
        this.plantMap = plantMap;
        this.animalMap = animalMap;
        this.init();
    }

    // getters/setters

    public AnimalMap getAnimalMap() {
        return this.animalMap;
    }


    public PlantMap getPlantMap() {
        return plantMap;
    }


    public Vector2 getSize() {
        return this.size;
    }
}
