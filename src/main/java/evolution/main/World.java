package evolution.main;

import evolution.maps.AnimalMap;
import evolution.maps.PlantMap;
import evolution.util.Config;
import evolution.util.Vector2;
import java.util.Random;

public class World {

    private WorldEnvironment environment;


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
        int growCount = Config.getPlantGrowCount();
        Vector2 size = this.environment.getSize();

        for(int i = 0; i < growCount; i++) {
            Plant plant = new Plant(Config.getPlantNutritionalValue());
            plant.setPos(new Vector2(new Random().nextInt(size.x), new Random().nextInt(size.y)));
            while (!this.environment.placePlant(plant)) {
                plant.setPos(new Vector2(new Random().nextInt(size.x), new Random().nextInt(size.y)));
            }
        }
    }

    // constructors

    public World() {
        PlantMap plantMap; AnimalMap animalMap;
        try {
            plantMap = (PlantMap)Class.forName("evolution.maps."+Config.getPlantMapType()).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("plantMap type: '%s' not found", Config.getPlantMapType()));
        }

        try {
            animalMap = (AnimalMap)Class.forName("evolution.maps."+Config.getAnimalMapType()).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("animalMap type: '%s' not found", Config.getAnimalMapType()));
        }

        this.environment = new WorldEnvironment(Config.getMapSize(), plantMap, animalMap);
    }
}
