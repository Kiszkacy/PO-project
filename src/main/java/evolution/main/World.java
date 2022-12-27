package evolution.main;

import evolution.util.Config;
import evolution.util.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Main class held responsible for "giving life" to a simulation. Gives orders to animals, creates
 * new plants and notifies environment about new day.
 */
public class World {

    private final WorldEnvironment environment;

    /**
     * This method is required to be called at the end of constructor.
     */
    public void init() {
        // place starting animals
        Vector2 size = this.environment.getSize();
        for(int i = 0; i < Config.getStartingAnimalCount(); i++) {
            Animal animal = new Animal(this.environment, new Vector2(new Random().nextInt(size.x), new Random().nextInt(size.y)));
            this.environment.placeAnimal(animal);
        }
        // place starting plants
        this.environment.getPlantMap().generatePreferredTiles();
        for(int i = 0; i < Config.getStartingPlantCount(); i++) {
            this.growPlant();
        }
    }

    /**
     * Method describing what actions happen in what order in every day of simulation's lifespan.
     */
    public void dayCycle() {
        this.environment.newDay();
        this.animalsMove();
        this.animalsEat();
        this.animalsReproduce();
        this.growPlants();
    }


    public void animalsMove() {
        int size = this.environment.getAnimalMap().getObjects().size();
        for (int i = size-1; i >= 0; i--) {
            // Decreases expected size of list, if a case that animal died after moving and got deleted
            this.environment.getAnimalMap().getObjects().get(i).move();
        }
    }


    public void animalsEat() {
        int size = this.environment.getAnimalMap().getObjects().size();
        for(int i = 0; i < size; i++){
            this.environment.getAnimalMap().getObjects().get(i).lookForFood();
        }
    }


    public void animalsReproduce() {
        int size = this.environment.getAnimalMap().getObjects().size();
        for(int i = 0; i < size; i++){
            this.environment.getAnimalMap().getObjects().get(i).lookForMate();
        }
    }


    public void growPlants() {
        this.environment.getPlantMap().generatePreferredTiles();
        for(int i = 0; i < Config.getPlantGrowCount(); i++) {
            this.growPlant();
        }
    }


    private void growPlant() {
        List<Vector2> tiles;
        if (new Random().nextFloat() > 0.2) tiles = new ArrayList<>(this.environment.getPlantMap().getNotPreferredTiles());
        else                                tiles = new ArrayList<>(this.environment.getPlantMap().getPreferredTiles());

        Plant plant = new Plant(this.environment);
        int idx = new Random().nextInt(tiles.size());
        plant.setPos(tiles.get(idx));

        // can iterate many times if there are only few empty places left
        while (!this.environment.placePlant(plant) && tiles.size() > 1) {
            tiles.remove(idx);
            idx = new Random().nextInt(tiles.size());
            plant.setPos(tiles.get(idx));

        }
    }

    // constructors

    public World() {
        this.environment = new WorldEnvironment();
        this.init();
    }


    public World(WorldEnvironment environment) {
        this.environment = environment;
        this.init();
    }

    // getters/setters

    public WorldEnvironment getEnvironment() {
        return this.environment;
    }
}
