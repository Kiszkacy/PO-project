package evolution;

import evolution.main.World;
import evolution.util.Config;
import java.lang.reflect.InvocationTargetException;

public class App {

    private String configPath;
    private World world;


    public void run() {
        world.dayCycle();
    }


    public void init() {
        this.loadConfig();
        this.world = new World();
    }


    public void loadConfig() {
        Config.loadConfig(this.configPath);
    }

    // constructors

    public App(String configPath) {
        this.configPath = configPath;
    }
}
