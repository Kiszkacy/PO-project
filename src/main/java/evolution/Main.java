package evolution;

import evolution.util.*;
import static evolution.util.EasyPrint.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            // input setup
            boolean inputRequired = true;
            Scanner input = new Scanner(System.in);
            // sim parameters
            int tickCount = 10000;
            boolean manualTicks = false;
            int tickStep = 1;
            // load config
            App app = new App("config.json");
            pcol(Color.GREEN, "Config loaded successfully.");
            // setup ticks
            p("tick count: ", "");
            if (inputRequired) tickCount = Integer.parseInt(input.nextLine());
            else p(tickCount);
            // setup manual ticks
            p("stop at predefined ticks?: ", "");
            if (inputRequired) manualTicks = Boolean.parseBoolean(input.nextLine());
            else p(manualTicks);
            if (manualTicks) {
                p("stop every n-th tick: ", "");
                if (inputRequired) tickStep = Integer.parseInt(input.nextLine());
                else p(tickStep, "");
                p(" tick");
            }
            // start sim
            pcol(Color.WHITE, "========= START =========");
            pcol(Color.WEAK_YELLOW, app.getWorld().getEnvironment().getAnimalMap().toString());
            pcol(Color.WEAK_GREEN, app.getWorld().getEnvironment().getPlantMap().toString());
            for (int i = 0; i < tickCount; i++) {
                app.tick();
                if (manualTicks && (i+1) % tickStep == 0 && (i+1) != tickCount) { // dont print last tick if its last tick in sim
                    p(String.format("tick: %s", i+1));
                    pcol(Color.WEAK_YELLOW, app.getWorld().getEnvironment().getAnimalMap().toString());
                    pcol(Color.WEAK_GREEN, app.getWorld().getEnvironment().getPlantMap().toString());
                    input.nextLine();
                }
            }
            pcol(Color.WHITE, "========= END =========");
            pcol(Color.WEAK_YELLOW, app.getWorld().getEnvironment().getAnimalMap().toString());
            pcol(Color.WEAK_GREEN, app.getWorld().getEnvironment().getPlantMap().toString());
            p("SIZE: " + app.getWorld().getEnvironment().getAnimalMap().getObjects().size());
        } catch (Exception e) {
            ExceptionHandler.printCriticalInfo(e);
        }
    }
}
