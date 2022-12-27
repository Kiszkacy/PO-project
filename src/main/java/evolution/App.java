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


    private void init() throws Exception {
        this.loadConfig();
        this.world = new World();
    }


    private void loadConfig() throws Exception {
        Config.loadConfig(this.configPath);
    }

    // constructors

    public App(String configPath) {
        this.configPath = configPath;
        try {
            this.init();
        } catch (Exception e) {
            ExceptionHandler.printCriticalInfo(e);
        }
    }

    // getters/setters

    public String getConfigPath() {
        return this.configPath;
    }


    public World getWorld() {
        return this.world;
    }
}
