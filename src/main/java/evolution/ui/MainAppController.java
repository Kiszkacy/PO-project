package evolution.ui;

import evolution.util.Config;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

import static evolution.util.EasyPrint.p;

public class MainAppController implements Initializable {
    @FXML
    public Button StartButton;
    @FXML
    public Button ClearButton;
    @FXML
    private GridPane grid;
    private Map<String, TextField> configs = new HashMap<>();
    private Stage stage;

    @FXML
    public void createScene(){
        int current_height = grid.getRowCount();

        Field[] attributes = Config.class.getDeclaredFields();
        for (Field field : attributes) {
            if (field.getName().equals("instance") || field.getName().equals("version")) continue;
            Label attribute = new Label(field.getName() + ":");
            TextField textField = new TextField();
            configs.put(field.getName(), textField);

            grid.add(attribute, 0, current_height);
            grid.add(textField, 1, current_height++);
        }

        GridPane.setRowIndex(StartButton, current_height);
        GridPane.setRowIndex(ClearButton, current_height);
    }

    public void runSimulationInstance(){
        // tell user that something is wrong?
        if (!isConfigsValid()) return;
        saveConfig();
        SimulationGUI sim = new SimulationGUI(getSettings(), 999, 99);
        sim.run();
    }

    public static String getSettings(){
        //temporary
        return "config1.json";
    }

    public void clearAllSettings(){
        for (TextField textField: configs.values()) {
            textField.clear();
        }
    }

    private String saveConfig(){
        JSONObject configObjects = new JSONObject();
        JSONObject settings = new JSONObject();
        configObjects.put("version",Config.getVersion());
        for (String key: configs.keySet()) {
            settings.put(key, configs.get(key).getCharacters());
        }
        configObjects.put("settings", settings);
        p(configObjects);
        try {
            String path = new File(".").getCanonicalPath()+"\\configs\\config" +
                    java.time.ZonedDateTime.now().toString().replace(":", "").replace(".", "").replace("/", "") + ".json";
            p(path);
            try (final FileWriter writer = new FileWriter(path)) {
                writer.write(configObjects.toString());
                writer.flush();
                return path;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void runFileChooser() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        fileChooser.setTitle("Open Config file");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            //temporary
//            Config.loadConfig(file.getAbsolutePath());
            Config.loadConfig("config1.json");
            Method[] allMethods = Config.class.getDeclaredMethods();
            for (Method method: allMethods) {
                if (!method.getName().startsWith("get") || method.getName().equals("getVersion")) continue;
                String configName = method.getName().replace("get", "");
                configName = Character.toLowerCase(configName.charAt(0)) + configName.substring(1);
                configs.get(configName).setText(method.invoke(Config.class).toString());
            }
        }
    }

    private boolean isConfigsValid(){
        for (TextField textField : configs.values()) {
            if (textField.getCharacters().isEmpty()) return false;
        }
        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
