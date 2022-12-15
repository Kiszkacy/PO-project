package evolution;

import evolution.util.*;
import static evolution.util.EasyPrint.*;

public class Main {

    public static void main(String[] args) {
        try {
            // test | rotate Direction (implement in Animal movement)
            pcol(Color.YELLOW, Direction.LEFT.rotate(Direction.DOWNRIGHT));
            // test | app run & config load
            App app = new App("config.json");
            app.init();
            app.run();
            pcol(Color.BLUE, Config.getMapSize());
        } catch (Exception e) {
            pcol(Color.RED, e.getMessage());
        }
    }
}
