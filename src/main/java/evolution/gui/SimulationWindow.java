package evolution.gui;

import evolution.Simulation;
import evolution.util.CSVDumper;
import evolution.util.ExceptionHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.*;
import javafx.fxml.*;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Wrapper class for SimulationWindowController. Loads required files.
 */
public class SimulationWindow {

    private SimulationWindowController controller;


    public void run() {
        this.controller.start();
    }

    // constructors

    public SimulationWindow(String config, int tickCount, int ticksPerSec, boolean dumpData) {
        try {
            // load FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SimulationWindow.fxml"));
            Parent root = loader.load();
            // setup scene
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Paths.get("src/main/resources/SimulationWindow.css").toUri().toURL().toExternalForm());
            // load & setup controller
            this.controller = loader.getController();
            this.controller.init(new Simulation(config), tickCount, ticksPerSec, dumpData);
            // setup stage
            Stage stage = new Stage();
            stage.setMinHeight(720);
            stage.setMinWidth(760);
            stage.setTitle(String.format("Simulation %s", config.split("config-")[config.split("config-").length-1]));
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(e -> {
                if(this.controller.getThread().isAlive()) this.controller.getSimulationThread().killThread();
                if(dumpData) {
                    try {
                        CSVDumper.save(this.controller.getDumpData(), config.split("config-")[config.split("config-").length-1], false);
                    } catch (IOException ex) {
                        ExceptionHandler.printCriticalInfo(ex);
                    }
                }
            }); // kill thread when closing window
        } catch (Exception e) {
            ExceptionHandler.printCriticalInfo(e);
        }
    }
}
