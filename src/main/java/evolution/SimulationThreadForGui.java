package evolution;

import evolution.ui.SimulationGUI;
import evolution.util.ExceptionHandler;


public class SimulationThreadForGui extends SimulationThread{

    SimulationGUI gui;

    @Override
    public void runSimulation(){
        double startTime = System.nanoTime();
        try {
            while (tick != targetTick && running) { // kill itself when tick == targetTick
                simulation.tick();
                tick++;
                gui.show();
                Thread.sleep((long) (Math.ceil(1000.0 / this.ticksPerSec)));
            }
        }catch (Exception e) {
        ExceptionHandler.printCriticalInfo(e);
        }
        long endTime = System.nanoTime();
        duration += (endTime-startTime) / 1000000000.0;
        if(tick == targetTick) endingMessages();
    }


    public SimulationThreadForGui(Simulation simulation, int ticksPerSec, SimulationGUI gui) {
        super(simulation, ticksPerSec);
        this.gui = gui;
    }

    public SimulationThreadForGui(Simulation simulation, int targetTick, int ticksPerSec, SimulationGUI gui) {
        super(simulation, targetTick, ticksPerSec);
        this.gui = gui;
    }

    public boolean isRunning() {
        return running;
    }
}
