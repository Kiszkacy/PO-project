package evolution;

import evolution.ui.SimulationGUI;
import evolution.util.Color;
import evolution.util.ExceptionHandler;


public class SimulationThreadForGui extends SimulationThread{

    SimulationGUI gui;

    @Override
    public void runSimulation(){
        double startTime = System.nanoTime();
        try {
            while (tick != targetTick && running) { // kill itself when tick == targetTick
                app.tick();
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


    public SimulationThreadForGui(App app, int ticksPerSec, SimulationGUI gui) {
        super(app, ticksPerSec);
        this.gui = gui;
    }

    public SimulationThreadForGui(App app, int targetTick, int ticksPerSec, SimulationGUI gui) {
        super(app, targetTick, ticksPerSec);
        this.gui = gui;
    }

    public boolean isRunning() {
        return running;
    }
}
