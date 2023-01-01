package evolution;

import evolution.threads.SimulationThread;
import evolution.gui.App;
import evolution.gui.SimulationTracker;
import evolution.util.*;
import javafx.application.Application;

import java.nio.file.Paths;
import java.util.Scanner;

import static evolution.util.EasyPrint.p;
import static evolution.util.EasyPrint.pcol;

public class Main {

    public static void main(String[] args) {
        try {
            int testType = 2; // 0 -> classic sim test | 1 -> thread test | 2 -> gui test | otherwise -> random tests

            switch (testType) {
                /** CLASSIC APP TEST */
                case 0:
                    // input setup
                    boolean inputRequired = false;
                    Scanner input = new Scanner(System.in);
                    // sim parameters
                    int tickCount = 100;
                    boolean manualTicks = false;
                    int tickStep = 1;
                    // load config
                    Simulation app = new Simulation(Paths.get("src/main/resources/default").toUri().toURL().getPath());
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
                        if (manualTicks && (i + 1) % tickStep == 0 && (i + 1) != tickCount) { // don't print last tick if its last tick in sim
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
                    break;
                /** THREAD TEST */
                case 1:
                    int threadCount = 5;
                    int[] ticks = new int[]{1, 10, 100, 1000, 10000};
                    int[] ticksPerSec = new int[]{60, 60, 60, 600, 1000};
                    String[] configFile = new String[]{Paths.get("src/main/resources/default").toUri().toURL().getPath(),
                            Paths.get("src/main/resources/default").toUri().toURL().getPath(),
                            Paths.get("src/main/resources/default").toUri().toURL().getPath(),
                            Paths.get("src/main/resources/default").toUri().toURL().getPath(),
                            Paths.get("src/main/resources/default").toUri().toURL().getPath()};
                    for(int i = 0; i < threadCount; i++) {
                        Simulation simulation = new Simulation(configFile[i]);
                        Thread thread = new Thread(new SimulationThread(simulation, ticks[i], ticksPerSec[i]));
                        thread.start();
                    }
                    break;
                /** GUI TEST */
                case 2:
                    Application.launch(App.class);
                    break;
                /** RANDOM TESTS */
                default:
                    CSVDumper.save(new String[][]{{}}, "test", true);
                    break;
            }
        } catch (Exception e) {
            ExceptionHandler.printCriticalInfo(e);
        }
    }
}
