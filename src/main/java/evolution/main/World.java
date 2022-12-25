package evolution.main;

import evolution.util.Config;
import evolution.util.Vector2;

import java.util.Random;

import static evolution.util.EasyPrint.p;

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
            Animal animal = new Animal(this.environment, new Vector2(new Random().nextInt(size.x), new Random().nextInt(size.y)));
            environment.placeAnimal(animal);
        }

        // place plants
        for(int i = 0; i < Config.getStartingPlantCount(); i++) {
            Plant plant = new Plant();
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
        Vector2 size = this.environment.getSize();
        // plantGrowCount seems to have impact on performance of generatePreferredTiles
        this.environment.getPlantMap().generatePreferredTiles();

        for(int i = 0; i < Config.getPlantGrowCount(); i++) {
            if (this.environment.getPlantMap().getObjects().size() >= size.x*size.y) return;
        // can iterate many times, if there are only few empty places left
            while (!this.environment.placePlant());
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
