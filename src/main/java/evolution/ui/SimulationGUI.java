package evolution.ui;

import evolution.App;
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
    App app;
    final Thread thread;
    final SimulationThreadForGui simulation;
    Text field = new Text("0");
    GridPane grid;


    public SimulationGUI(String configFile, int tickCount, int ticksPerSec){
        app = new App(configFile);
        simulation = new SimulationThreadForGui(app, tickCount, ticksPerSec, this);
        thread = new Thread(simulation);
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
        World world = app.getWorld();
        field.setText(Integer.toString(app.getWorld().getEnvironment().getAnimalMap().getObjects().size()));
    }

    private Scene createScene(){
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(field, 0, 0, 2, 1);

        Button start = new Button("Start/Stop");
        start.setOnAction(action -> {
            if (simulation.isRunning()) simulation.pauseThread();
            else simulation.resumeThread();

        });
        grid.add(start, 1, 0);

        return new Scene(grid, 200, 200);
    }

    public void show(){
        Platform.runLater(this::updateScene);
    }


}
