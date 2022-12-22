package evolution;

import evolution.util.*;
import static evolution.util.EasyPrint.*;

public class Main {

    public static void main(String[] args) {
        try {
            // test | app running
            App app = new App("config.json");
            pcol(Color.WHITE, "START");
            pcol(Color.WEAK_GREEN, app.getWorld().getEnvironment().getAnimalMap().toString());
            for (int i = 0; i < 10000; i++) {
                app.tick();
            }
            pcol(Color.WHITE, "END");
            pcol(Color.WEAK_BLUE, app.getWorld().getEnvironment().getAnimalMap().toString());
            p("SIZE: " + app.getWorld().getEnvironment().getAnimalMap().getObjects().size());
        } catch (Exception e) {
            pcol(Color.RED, e.getMessage());
            pcol(Color.RED, "at: " + e.getStackTrace()[0].getClassName());
            pcol(Color.RED, "inside: " + e.getStackTrace()[0].getMethodName());
            pcol(Color.RED, "line: " + e.getStackTrace()[0].getLineNumber());
        }
    }
}
