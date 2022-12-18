package evolution;

import evolution.main.World;
import evolution.util.Config;

public class App {

    private String configPath;
    private World world;


    public void tick() {
        world.dayCycle();
    }


    private void init() {
        this.loadConfig();
        this.world = new World();
    }


    private void loadConfig() {
        Config.loadConfig(this.configPath);
    }

    // constructors

    public App(String configPath) {
        this.configPath = configPath;
        this.init();
    }

    // getters/setters

    public String getConfigPath() {
        return this.configPath;
    }


    public World getWorld() {
        return this.world;
    }
}
