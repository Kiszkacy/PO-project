package evolution;

import evolution.util.Color;
import evolution.util.ExceptionHandler;
import static evolution.util.EasyPrint.pcol;

public class SimulationThread implements Runnable {

    protected int tick;
    protected final int targetTick;
    protected final int ticksPerSec;
    protected final Simulation simulation;
    boolean running = true;
    double duration = 0;

    // overrides

    public void run() {
        pcol(Color.WEAK_GREEN, String.format("Thread with settings: <targetTick: '%s'>, <ticksPerSec: '%s'> has been successfully created!", this.targetTick, this.ticksPerSec));
        long startTime = System.nanoTime();
        try {
            this.simulation.init();
            pcol(Color.GREEN, String.format("Thread with settings: <targetTick: '%s'>, <ticksPerSec: '%s'> has successfully started working!", this.targetTick, this.ticksPerSec));
        } catch (Exception e) {
            ExceptionHandler.printCriticalInfo(e);
        }

        while (tick != targetTick){
            if (running) runSimulation();
            try {
                Thread.sleep((long) 1.0);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void runSimulation(){
        double startTime = System.nanoTime();
        try {
            while (tick != targetTick && running) { // kill itself when tick == targetTick
                simulation.tick();
                tick++;
                Thread.sleep((long) (Math.ceil(1000.0 / this.ticksPerSec)));
            }
        }catch (Exception e) {
            ExceptionHandler.printCriticalInfo(e);
        }
        long endTime = System.nanoTime();
        duration += (endTime-startTime) / 1000000000.0;
        if(tick == targetTick) endingMessages();
    }

    public void resumeThread(){
        running = true;
    }

    public void pauseThread(){
        running = false;
    }

    protected void endingMessages(){
        long endTime = System.nanoTime();
        pcol(Color.BLUE, String.format("Thread with settings: <targetTick: '%s'>, <ticksPerSec: '%s'> has successfully stopped!", this.targetTick, this.ticksPerSec));
        pcol(Color.WEAK_BLUE, String.format("Thread with settings: <targetTick: '%s'>, <ticksPerSec: '%s'> report: '%s' ticks done in '%s's: performance: '%s' ticks per second",
                this.targetTick, this.ticksPerSec, this.targetTick, duration, this.targetTick/duration));
    }

    // constructors

    public SimulationThread(Simulation simulation, int ticksPerSec) {
        this.tick = 0;
        this.targetTick = -1;
        this.ticksPerSec = ticksPerSec;
        this.simulation = simulation;
    }


    public SimulationThread(Simulation simulation, int targetTick, int ticksPerSec) {
        this.tick = 0;
        this.targetTick = targetTick;
        this.ticksPerSec = ticksPerSec;
        this.simulation = simulation;
    }

    public boolean isRunning() {
        return running;
    }
}
