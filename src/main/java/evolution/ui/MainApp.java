package evolution.ui;


import evolution.util.Config;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import static evolution.util.EasyPrint.p;

public class MainApp extends Application {

    private List<TextField> textFields;
    private Map<String, String> configs;
    private Stage stage;

    public void start(Stage primaryStage) {
        stage = primaryStage;
        Scene scene = createScene();
        stage.setTitle("JavaFX Welcome");
        stage.setScene(scene);
        stage.show();

    }

    private Scene createScene(){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        int current_height = 0;

        Text sceneTitle = new Text("Simulation settings");
        sceneTitle.setFont(new Font(20));
        grid.add(sceneTitle, current_height, 0, 2, 1);

        Button selectConfig = new Button("Select Config");
        selectConfig.setOnAction(action -> {runFileChooser();});
        grid.add(selectConfig, 2, current_height++);

        Field[] attributes = Config.class.getDeclaredFields();
        textFields = new ArrayList<>();
        // starts at i = 2, to skipp first to attributes of config
        for (int i = 2; i < attributes.length; i++) {

            Label attribute = new Label(attributes[i].getName()+":");
            TextField textField = new TextField();
            textFields.add(textField);

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
        for (TextField textField: textFields) {
            textField.clear();
        }
    }

    private void runFileChooser(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        fileChooser.setTitle("Open Config file");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            p(file.getPath());
        }
    }

    @Override
    public void init() throws Exception {
        super.init();
    }
}
