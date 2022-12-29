package evolution.util;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;

import static evolution.util.EasyPrint.pcol;

/**
 * Singleton pattern. Loaded once at start of the simulation. Holds all information about simulation's settings.
 */
public class Config {

    private static final ThreadLocal<String> version = ThreadLocal.withInitial(() -> "1.0"); // current version of config file
    private static ThreadLocal<Config> instance = ThreadLocal.withInitial(() -> new Config());
    private ThreadLocal<Vector2> mapSize = new ThreadLocal<>();
    private ThreadLocal<String> animalMapType = new ThreadLocal<>();
    private ThreadLocal<Integer> startingPlantCount = new ThreadLocal<>();
    private ThreadLocal<Integer> plantNutritionalValue = new ThreadLocal<>();
    private ThreadLocal<Integer> plantGrowCount = new ThreadLocal<>();
    private ThreadLocal<String> plantMapType = new ThreadLocal<>();
    private ThreadLocal<Integer> startingAnimalCount = new ThreadLocal<>();
    private ThreadLocal<Integer> startingAnimalEnergy = new ThreadLocal<>();
    private ThreadLocal<Integer> reproduceRequiredEnergy = new ThreadLocal<>();
    private ThreadLocal<Integer> reproduceEnergy = new ThreadLocal<>();
    private ThreadLocal<Vector2> mutationCount = new ThreadLocal<>();
    private ThreadLocal<String> genomeType = new ThreadLocal<>();
    private ThreadLocal<Integer> genomeSize = new ThreadLocal<>();
    private ThreadLocal<String> brainType = new ThreadLocal<>();


    private static void createInstance() {
        instance = ThreadLocal.withInitial(() -> new Config());
    }


    public static void loadConfig(String path) throws Exception {
        // create json parser
        JSONParser parser = new JSONParser();
        // look for file in resources directory
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(path);
        if (url == null) throw new RuntimeException(String.format("config file not found, searched for: '%s'", path));
        // create JSONObject instance
        JSONObject obj = (JSONObject) parser.parse(new FileReader(url.getPath()));
        // check file version
        if (!version.get().equals(obj.get("version"))) throw new RuntimeException(String.format("outdated config file, version: '%s', expected: '%s'", obj.get("version"), version));
        // load settings
        JSONObject settings = (JSONObject) obj.get("settings");
        JSONArray mapSize = (JSONArray) settings.get("mapSize");
        instance.get().mapSize.set(new Vector2((int) (long) mapSize.get(0), (int) (long) mapSize.get(1)));
        instance.get().animalMapType.set((String) settings.get("animalMapType"));
        instance.get().startingPlantCount.set((int) (long) settings.get("startingPlantCount"));
        instance.get().plantNutritionalValue.set((int) (long) settings.get("plantNutritionalValue"));
        instance.get().plantGrowCount.set((int) (long) settings.get("plantGrowCount"));
        instance.get().plantMapType.set((String) settings.get("plantMapType"));
        instance.get().startingAnimalCount.set((int) (long) settings.get("startingAnimalCount"));
        instance.get().startingAnimalEnergy.set((int) (long) settings.get("startingAnimalEnergy"));
        instance.get().reproduceRequiredEnergy.set((int) (long) settings.get("reproduceRequiredEnergy"));
        instance.get().reproduceEnergy.set((int) (long) settings.get("reproduceEnergy"));
        JSONArray mutationCount = (JSONArray) settings.get("mutationCount");
        instance.get().mutationCount.set(new Vector2((int) (long) mutationCount.get(0), (int) (long) mutationCount.get(1)));
        instance.get().genomeType.set((String) settings.get("genomeType"));
        instance.get().genomeSize.set((int) (long) settings.get("genomeSize"));
        instance.get().brainType.set((String) settings.get("brainType"));
    }

    // overrides

    @Override
    public String toString() {
        return String.format("CONFIG: | %s %s %s %s %s %s %s %s %s %s %s %s %s %s |",
                instance.get().mapSize.get(), instance.get().animalMapType.get(), instance.get().startingPlantCount.get(),
                instance.get().plantNutritionalValue.get(), instance.get().plantGrowCount.get(), instance.get().plantMapType.get(),
                instance.get().startingAnimalCount.get(), instance.get().startingAnimalEnergy.get(),
                instance.get().reproduceRequiredEnergy.get(), instance.get().reproduceEnergy.get(),
                instance.get().mutationCount.get(), instance.get().genomeType.get(), instance.get().genomeSize.get(),
                instance.get().brainType.get());
    }

    // constructors

    private Config() { // no one is able to create another config instance

    }

    // getters/setters

    public static Vector2 getMapSize() {
        return instance.get().mapSize.get();
    }


    public static String getAnimalMapType() {
        return instance.get().animalMapType.get();
    }


    public static int getStartingPlantCount() {
        return instance.get().startingPlantCount.get();
    }


    public static int getPlantNutritionalValue() {
        return instance.get().plantNutritionalValue.get();
    }


    public static int getPlantGrowCount() {
        return instance.get().plantGrowCount.get();
    }


    public static String getPlantMapType() {
        return instance.get().plantMapType.get();
    }


    public static int getReproduceRequiredEnergy() {
        return instance.get().reproduceRequiredEnergy.get();
    }


    public static int getReproduceEnergy() {
        return instance.get().reproduceEnergy.get();
    }


    public static int getStartingAnimalCount() {
        return instance.get().startingAnimalCount.get();
    }


    public static int getStartingAnimalEnergy() {
        return instance.get().startingAnimalEnergy.get();
    }


    public static Vector2 getMutationCount() {
        return instance.get().mutationCount.get();
    }


    public static String getGenomeType() {
        return instance.get().genomeType.get();
    }


    public static int getGenomeSize() {
        return instance.get().genomeSize.get();
    }


    public static String getBrainType() {
        return instance.get().brainType.get();
    } 
}

