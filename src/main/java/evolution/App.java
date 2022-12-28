package evolution;

import evolution.main.World;
import evolution.util.Config;
import evolution.util.ExceptionHandler;

public class App {

    private String configPath;
    private World world;


    public void tick() {
        world.dayCycle();
    }


    public void init() throws Exception {
        this.loadConfig();
        this.world = new World();
    }


    private void loadConfig() throws Exception {
        Config.loadConfig(this.configPath);
    }

    // constructors

    public App(String configPath) {
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
