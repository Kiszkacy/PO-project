package evolution.threads;

import evolution.Simulation;
import evolution.gui.SimulationTracker;
import evolution.gui.SimulationWindowController;

public class SimulationThreadGUI extends SimulationThread{

    private SimulationWindowController gui;

    // overrides

    @Override
    public void simulationTick() throws InterruptedException {
        simulation.tick();
        tick++;
        gui.show();
        Thread.sleep((long) (Math.ceil(1000.0 / this.ticksPerSec)));
    }

    // constructors

    public SimulationThreadGUI(Simulation simulation, int ticksPerSec, SimulationWindowController gui) {
        super(simulation, ticksPerSec);
        this.gui = gui;
    }


    public SimulationThreadGUI(Simulation simulation, int targetTick, int ticksPerSec, SimulationWindowController gui) {
        super(simulation, targetTick, ticksPerSec);
        this.gui = gui;
    }


    public SimulationThreadGUI(Simulation simulation, int targetTick, int ticksPerSec, SimulationTracker simulationTracker, SimulationWindowController gui) {
        super(simulation, targetTick, ticksPerSec, simulationTracker);
        this.gui = gui;
    }

    // getters/setters

    public boolean isRunning() {
        return this.running;
    }
}
