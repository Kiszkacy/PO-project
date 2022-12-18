package evolution.main;

import evolution.util.Config;
import evolution.util.Vector2;
import java.util.Random;

/**
 * Main class held responsible for "giving life" to a simulation. Gives orders to animals, creates
 * new plants and notifies environment about new day.
 */
public class World {

    private final WorldEnvironment environment;


    public void init() {
        Vector2 size = environment.getSize();

        // place animals
        for(int i = 0; i < Config.getStartingAnimalCount(); i++) {
            environment.placeAnimal(new Animal(new Vector2(new Random().nextInt(size.x), new Random().nextInt(size.y)), this.environment));
        }

        // place plants
        for(int i = 0; i < Config.getStartingPlantCount(); i++) {
            Plant plant = new Plant(Config.getPlantNutritionalValue());
            plant.setPos(new Vector2(new Random().nextInt(size.x), new Random().nextInt(size.y)));
            while (!this.environment.placePlant(plant)) {
                plant.setPos(new Vector2(new Random().nextInt(size.x), new Random().nextInt(size.y)));
            }
        }
    }

    /**
     * Method describing what actions happen in what order in every day of simulation's lifespan.
     */
    public void dayCycle() {
        environment.newDay();
        animalsMove();
        animalsEat();
        animalsReproduce();
        growPlants();
    }


    public void animalsMove() {
        for(Animal a : this.environment.getAnimalMap().getObjects()) {
            a.move();
        }
    }


    public void animalsEat() {
        for(Animal a : this.environment.getAnimalMap().getObjects()) {
            a.lookForFood();
        }
    }


    public void animalsReproduce() {
        for(Animal a : this.environment.getAnimalMap().getObjects()) {
            a.lookForMate();
        }
    }


    public void growPlants() {
        Vector2 size = this.environment.getSize();

        for(int i = 0; i < Config.getPlantGrowCount(); i++) {
            Plant plant = new Plant(Config.getPlantNutritionalValue());
            plant.setPos(new Vector2(new Random().nextInt(size.x), new Random().nextInt(size.y)));
            while (!this.environment.placePlant(plant)) {
                plant.setPos(new Vector2(new Random().nextInt(size.x), new Random().nextInt(size.y)));
            }
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
