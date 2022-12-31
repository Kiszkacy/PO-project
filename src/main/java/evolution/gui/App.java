package evolution.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Wrapper class for AppController. Loads required files.
 */
public class App extends Application {

    // overrides

    @Override
    public void start(Stage primaryStage) throws IOException {
        // handle possible null
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/App.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Total Simulation");
        primaryStage.setScene(scene);

        AppController controller = loader.getController();
        // has to set stage first
        controller.setStage(primaryStage);
        controller.init();

        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
    }
}
