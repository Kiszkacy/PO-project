package evolution;

import evolution.brains.Brain;
import evolution.util.*;
import static evolution.util.EasyPrint.*;

public class Main {

    public static void main(String[] args) {
        try {
            // test | app running
            App app = new App("config.json");
            p("tick 0:\n" + app.getWorld().getEnvironment().getAnimalMap().toString());
            app.tick();
            p("tick 1:\n" + app.getWorld().getEnvironment().getAnimalMap().toString());

        } catch (Exception e) {
            pcol(Color.RED, e.getMessage());
            pcol(Color.RED, "at: " + e.getStackTrace()[0].getClassName());
            pcol(Color.RED, "inside: " + e.getStackTrace()[0].getMethodName());
            pcol(Color.RED, "line: " + e.getStackTrace()[0].getLineNumber());
        }
    }
}
