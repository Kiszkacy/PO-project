package evolution.ui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class MainApp extends Application {

    public void start(Stage primaryStage) throws IOException {
        // handle possible null
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainApp.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Total Simulation");
        primaryStage.setScene(scene);


        MainAppController controller = loader.getController();
        // has to set stage first
        controller.setStage(primaryStage);
        controller.createScene();

        primaryStage.show();

    }

    @Override
    public void init() throws Exception {
        super.init();
    }
}
