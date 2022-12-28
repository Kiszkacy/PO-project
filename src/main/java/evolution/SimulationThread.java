package evolution;

import evolution.util.Color;
import evolution.util.Config;
import evolution.util.ExceptionHandler;
import static evolution.util.EasyPrint.pcol;

public class SimulationThread implements Runnable {

    private int tick;
    private final int targetTick;
    private final int ticksPerSec;
    private final App app;

    // overrides

    @Override
    public void run() {
        pcol(Color.WEAK_GREEN, String.format("Thread with settings: <targetTick: '%s'>, <ticksPerSec: '%s'> has been successfully created!", this.targetTick, this.ticksPerSec));
        long startTime = System.nanoTime();
        try {
            this.app.init();
            pcol(Color.GREEN, String.format("Thread with settings: <targetTick: '%s'>, <ticksPerSec: '%s'> has successfully started working!", this.targetTick, this.ticksPerSec));
            startTime = System.nanoTime();
            while (tick != targetTick) { // kill itself when tick == targetTick
                app.tick();
                tick++;
                Thread.sleep((long) (Math.ceil(1000.0 / this.ticksPerSec)));
            }
        } catch (Exception e) {
            ExceptionHandler.printCriticalInfo(e);
        }
        long endTime = System.nanoTime();
        double duration = (endTime-startTime) / 1000000000.0;
        pcol(Color.BLUE, String.format("Thread with settings: <targetTick: '%s'>, <ticksPerSec: '%s'> has successfully stopped!", this.targetTick, this.ticksPerSec));
        pcol(Color.WEAK_BLUE, String.format("Thread with settings: <targetTick: '%s'>, <ticksPerSec: '%s'> report: '%s' ticks done in '%s's: performance: '%s' ticks per second",
                this.targetTick, this.ticksPerSec, this.targetTick, duration, this.targetTick/duration));
    }

    // constructors

    public SimulationThread(App app, int ticksPerSec) {
        this.tick = 0;
        this.targetTick = -1;
        this.ticksPerSec = ticksPerSec;
        this.app = app;
    }


    public SimulationThread(App app, int targetTick, int ticksPerSec) {
        this.tick = 0;
        this.targetTick = targetTick;
        this.ticksPerSec = ticksPerSec;
        this.app = app;
    }
}
