package evolution.util;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;

/**
 * Singleton pattern. Loaded once at start of the simulation. Holds all information about simulation's settings.
 */
public class Config { // TODO threadLocal

    private static final String version = "1.0"; // current version of config file
    private static Config instance = null;
    private Vector2 mapSize;
    private String animalMapType;
    private int startingPlantCount;
    private int plantNutritionalValue;
    private int plantGrowCount;
    private String plantMapType;
    private int startingAnimalCount;
    private int startingAnimalEnergy;
    private int reproduceRequiredEnergy;
    private int reproduceEnergy;
    private Vector2 mutationCount;
    private String genomeType;
    private int genomeSize;
    private String brainType;


    private static void createInstance() {
        instance = new Config();
    }


    public static void loadConfig(String path) throws Exception {
        createInstance();
        // create json parser
        JSONParser parser = new JSONParser();
        // look for file in resources directory
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(path);
        if (url == null) throw new RuntimeException(String.format("config file not found, searched for: '%s'", path));
        // create JSONObject instance
        JSONObject obj = (JSONObject) parser.parse(new FileReader(url.getPath()));
        // check file version
        if (!version.equals(obj.get("version"))) throw new RuntimeException(String.format("outdated config file, version: '%s', expected: '%s'", obj.get("version"), version));
        // load settings
        JSONObject settings = (JSONObject) obj.get("settings");
        JSONArray mapSize = (JSONArray) settings.get("mapSize");
        instance.mapSize = new Vector2((int) (long) mapSize.get(0), (int) (long) mapSize.get(1));
        instance.animalMapType = (String) settings.get("animalMapType");
        instance.startingPlantCount = (int) (long) settings.get("startingPlantCount");
        instance.plantNutritionalValue = (int) (long) settings.get("plantNutritionalValue");
        instance.plantGrowCount = (int) (long) settings.get("plantGrowCount");
        instance.plantMapType = (String) settings.get("plantMapType");
        instance.startingAnimalCount = (int) (long) settings.get("startingAnimalCount");
        instance.startingAnimalEnergy = (int) (long) settings.get("startingAnimalEnergy");
        instance.reproduceRequiredEnergy = (int) (long) settings.get("reproduceRequiredEnergy");
        instance.reproduceEnergy = (int) (long) settings.get("reproduceEnergy");
        JSONArray mutationCount = (JSONArray) settings.get("mutationCount");
        instance.mutationCount = new Vector2((int) (long) mutationCount.get(0), (int) (long) mutationCount.get(1));
        instance.genomeType = (String) settings.get("genomeType");
        instance.genomeSize = (int) (long) settings.get("genomeSize");
        instance.brainType = (String) settings.get("brainType");
    }

    // overrides

    public String toString() {
        return String.format("CONFIG: | %s %s %s %s %s %s %s %s %s %s %s %s %s %s |",
                instance.mapSize, instance.animalMapType, instance.startingPlantCount,
                instance.plantNutritionalValue, instance.plantGrowCount, instance.plantMapType,
                instance.startingAnimalCount, instance.startingAnimalEnergy,
                instance.reproduceRequiredEnergy, instance.reproduceEnergy,
                instance.mutationCount, instance.genomeType, instance.genomeSize,
                instance.brainType);
    }

    // constructors

    private Config() { // no one is able to create another config instance

    }

    // getters/setters

    public static Vector2 getMapSize() {
        return instance.mapSize;
    }


    public static String getAnimalMapType() {
        return instance.animalMapType;
    }


    public static int getStartingPlantCount() {
        return instance.startingPlantCount;
    }


    public static int getPlantNutritionalValue() {
        return instance.plantNutritionalValue;
    }


    public static int getPlantGrowCount() {
        return instance.plantGrowCount;
    }


    public static String getPlantMapType() {
        return instance.plantMapType;
    }


    public static int getReproduceRequiredEnergy() {
        return instance.reproduceRequiredEnergy;
    }


    public static int getReproduceEnergy() {
        return instance.reproduceEnergy;
    }


    public static int getStartingAnimalCount() {
        return instance.startingAnimalCount;
    }


    public static int getStartingAnimalEnergy() {
        return instance.startingAnimalEnergy;
    }


    public static Vector2 getMutationCount() {
        return instance.mutationCount;
    }


    public static String getGenomeType() {
        return instance.genomeType;
    }


    public static int getGenomeSize() {
        return instance.genomeSize;
    }


    public static String getBrainType() {
        return instance.brainType;
    } 
}

