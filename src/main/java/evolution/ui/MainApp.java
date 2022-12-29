package evolution.ui;


import evolution.App;
import evolution.SimulationThread;
import evolution.util.Config;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static evolution.util.EasyPrint.p;

public class MainApp extends Application {

    private List<TextField> settings;

    public void start(Stage primaryStage) {

        Scene scene = createScene();
        primaryStage.setTitle("JavaFX Welcome");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Scene createScene(){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        int current_height = 0;

        Text sceneTitle = new Text("Simulation settings");
        sceneTitle.setFont(new Font(20));
        grid.add(sceneTitle, current_height++, 0, 2, 1);

        Field[] attributes = Config.class.getDeclaredFields();
        settings = new ArrayList<>();
        // starts at i = 2, to skipp first to attributes of config
        for (int i = 2; i < attributes.length; i++) {

            Label attribute = new Label(attributes[i].getName()+":");
            TextField textField = new TextField();
            settings.add(textField);

            grid.add(attribute,0, current_height);
            grid.add(textField, 1, current_height++);
        }

        Button start = new Button("Start simulation");
        start.setOnAction(action -> {runSimulationInstance();});
        grid.add(start, 1, current_height++);

        Button clear = new Button("Clear settings");
        clear.setOnAction(action -> {clearAllSettings();});
        grid.add(clear, 2, current_height-1);

        return new Scene(grid, 800, 800);
    }

    private void runSimulationInstance(){
        SimulationGUI sim = new SimulationGUI(getSettings(), 999, 99);
        sim.run();
    }

    private static String getSettings(){
        //temporary
        return "config1.json";
    }

    private void clearAllSettings(){
        for (TextField textField: settings) {
            textField.clear();
        }
    }

    @Override
    public void init() throws Exception {
        super.init();
    }
}
