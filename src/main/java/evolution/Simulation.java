package evolution;

import evolution.main.World;
import evolution.util.Config;

/**
 * Main simulation class. After calling init() method once can run using tick() method. One tick is equal to one in-simulation day.
 */
public class Simulation {

    private String configPath;
    private World world;


    public void tick() {
        world.dayCycle();
    }

    /**
     * This method is required to be called once before running the simulation.
     * @throws Exception if something went wrong
     */
    public void init() throws Exception {
        this.loadConfig();
        this.world = new World();
    }


    private void loadConfig() throws Exception {
        Config.loadConfig(this.configPath);
    }

    // constructors

    public Simulation(String configPath) {
        this.configPath = configPath;
    }

    // getters/setters

    public String getConfigPath() {
        return this.configPath;
    }


    public World getWorld() {
        return this.world;
    }
}
