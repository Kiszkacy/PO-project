package evolution.ui;

import evolution.Simulation;
import evolution.SimulationThreadForGui;
import evolution.main.World;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static evolution.util.EasyPrint.p;

public class SimulationGUI{
    Simulation simulation;
    final Thread thread;
    final SimulationThreadForGui simulationThread;
    Text field = new Text("0");
    GridPane grid;


    public SimulationGUI(String configFile, int tickCount, int ticksPerSec){
        simulation = new Simulation(configFile);
        simulationThread = new SimulationThreadForGui(simulation, tickCount, ticksPerSec, this);
        thread = new Thread(simulationThread);
        Stage stage = new Stage();
        stage.setTitle("My New Stage Title");

        stage.setScene(createScene());
        stage.show();
        stage.setOnCloseRequest(e -> {
            // exit safely from simulation, when window is being closed
            if(thread.isAlive()){
                // when simulation is still running
            }
        });
    }
    public void run(){
        thread.start();
    }

    public void updateScene(){
        World world = simulation.getWorld();
        field.setText(Integer.toString(simulation.getWorld().getEnvironment().getAnimalMap().getObjects().size()));
    }

    private Scene createScene(){
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(field, 0, 0);

        Button start = new Button("Start/Stop");
        start.setOnAction(action -> {
            if (simulationThread.isRunning()) simulationThread.pauseThread();
            else simulationThread.resumeThread();

        });
        grid.add(start, 1, 0);

        return new Scene(grid, 200, 200);
    }

    public void show(){
        Platform.runLater(this::updateScene);
    }


}
