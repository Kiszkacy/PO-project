package evolution;

import evolution.main.*;
import evolution.ui.SimulationTracker;
import evolution.util.*;
import static evolution.util.EasyPrint.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            boolean threadTest = true;

            /** CLASSIC APP TEST */
            if (!threadTest) {
                // input setup
                boolean inputRequired = false;
                Scanner input = new Scanner(System.in);
                // sim parameters
                int tickCount = 100;
                boolean manualTicks = false;
                int tickStep = 1;
                // load config
                App app = new App("config.json");
                app.init();
                pcol(Color.GREEN, "Config loaded successfully.");
                // setup simulation tracker
                SimulationTracker simTracker = new SimulationTracker(app.getWorld());
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
                }
                // start sim
                pcol(Color.WHITE, "========= START =========");
                pcol(Color.WEAK_YELLOW, app.getWorld().getEnvironment().getAnimalMap().toString());
                pcol(Color.WEAK_GREEN, app.getWorld().getEnvironment().getPlantMap().toString());
                for (int i = 0; i < tickCount; i++) {
                    app.tick();
                    if (manualTicks && (i + 1) % tickStep == 0 && (i + 1) != tickCount) { // dont print last tick if its last tick in sim
                        p(String.format("tick: %s", i + 1));
                        pcol(Color.WEAK_YELLOW, app.getWorld().getEnvironment().getAnimalMap().toString());
                        pcol(Color.WEAK_GREEN, app.getWorld().getEnvironment().getPlantMap().toString());
                        input.nextLine();
                    }
                    CSVDumper.save(new String[][]{simTracker.dumpAllData()}, "data", true);
                }
                pcol(Color.WHITE, "========= END =========");
                pcol(Color.WEAK_YELLOW, app.getWorld().getEnvironment().getAnimalMap().toString());
                pcol(Color.WEAK_GREEN, app.getWorld().getEnvironment().getPlantMap().toString());
                p("SIZE: " + app.getWorld().getEnvironment().getAnimalMap().getObjects().size());
            }
            /** THREAD TEST */
            else {
                int threadCount = 5;
                int[] tickCount = new int[]{1, 10, 100, 1000, 10000};
                int[] ticksPerSec = new int[]{60, 60, 60, 600, 1000};
                String[] configFile = new String[]{"config1.json", "config2.json", "config3.json", "config4.json", "config5.json"};
                for(int i = 0; i < threadCount; i++) {
                    App app = new App(configFile[i]);
                    Thread thread = new Thread(new SimulationThread(app, tickCount[i], ticksPerSec[i]));
                    thread.start();
                }
            }
        } catch (Exception e) {
            ExceptionHandler.printCriticalInfo(e);
        }
    }
}
