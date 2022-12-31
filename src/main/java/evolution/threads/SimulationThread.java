package evolution.threads;

import evolution.Simulation;
import evolution.gui.SimulationTracker;
import evolution.util.Color;
import evolution.util.ExceptionHandler;
import static evolution.util.EasyPrint.pcol;

public class SimulationThread implements Runnable {

    protected int tick;
    protected final int targetTick;
    protected final int ticksPerSec;
    protected final Simulation simulation;
    protected SimulationTracker simulationTracker;
    protected boolean running;
    protected boolean exit;


    private void init() {
        try {
            this.simulation.init();
            if (this.simulationTracker != null) this.simulationTracker.init(this.simulation.getWorld());
        } catch (Exception e) {
            ExceptionHandler.printCriticalInfo(e);
        }
    }


    protected void simulationTick() throws InterruptedException {
        simulation.tick();
        tick++;
        Thread.sleep((long) (Math.ceil(1000.0 / this.ticksPerSec)));
    }


    protected void deathMessage(double duration){
        pcol(Color.BLUE, String.format("Thread with settings: <targetTick: '%s'>, <ticksPerSec: '%s'> has successfully stopped!", this.targetTick, this.ticksPerSec));
        pcol(Color.WEAK_BLUE, String.format("Thread with settings: <targetTick: '%s'>, <ticksPerSec: '%s'> report: '%s' ticks done in '%s's: performance: '%s' ticks per second",
                this.targetTick, this.ticksPerSec, this.targetTick, duration, this.targetTick/duration));
    }


    public void resumeThread(){
        this.running = true;
    }


    public void pauseThread(){
        this.running = false;
    }


    public void killThread() {
        this.exit = true;
    }

    // overrides

    @Override
    public void run() {
        pcol(Color.WEAK_GREEN, String.format("Thread with settings: <targetTick: '%s'>, <ticksPerSec: '%s'> has been successfully created!", this.targetTick, this.ticksPerSec));
        this.init();
        pcol(Color.GREEN, String.format("Thread with settings: <targetTick: '%s'>, <ticksPerSec: '%s'> has successfully started working!", this.targetTick, this.ticksPerSec));
        double startTime = System.nanoTime();
        while (tick != targetTick) {
            try {
                if (exit) break;
                if (running) simulationTick();
                Thread.sleep((long) 1.0);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        long endTime = System.nanoTime();
        double aliveFor = (endTime-startTime) / 1000000000.0;
        this.deathMessage(aliveFor);
    }

    // constructors

    public SimulationThread(Simulation simulation, int ticksPerSec) {
        this.tick = 0;
        this.targetTick = -1;
        this.ticksPerSec = ticksPerSec;
        this.simulation = simulation;
        this.running = true;
        this.exit = false;
    }


    public SimulationThread(Simulation simulation, int targetTick, int ticksPerSec) {
        this.tick = 0;
        this.targetTick = targetTick;
        this.ticksPerSec = ticksPerSec;
        this.simulation = simulation;
        this.running = true;
        this.exit = false;
    }


    public SimulationThread(Simulation simulation, int targetTick, int ticksPerSec, SimulationTracker simulationTracker) {
        this.tick = 0;
        this.targetTick = targetTick;
        this.ticksPerSec = ticksPerSec;
        this.simulation = simulation;
        this.running = true;
        this.exit = false;
        this.simulationTracker = simulationTracker;
    }

    // getters/setters

    public boolean isRunning() {
        return this.running;
    }


    public Simulation getSimulation() {
        return this.simulation;
    }


    public void setSimulationTracker(SimulationTracker simulationTracker) {
        this.simulationTracker = simulationTracker;
    }


    public SimulationTracker getSimulationTracker() {
        return this.simulationTracker;
    }


    public int getTick() {
        return this.tick;
    }
}
