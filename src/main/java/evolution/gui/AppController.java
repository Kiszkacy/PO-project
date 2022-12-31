package evolution.gui;

import evolution.util.Config;
import evolution.util.ExceptionHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Main simulation GUI class. Creates window responsible for creating and loading config files using javafx library.
 * Can also start multiple simulations at once by creating separate window for each instance.
 */
public class AppController implements Initializable {
    @FXML
    public Button startButton;
    @FXML
    public Button clearButton;
    @FXML
    private GridPane grid;

    private final Map<String, TextField> configs = new HashMap<>();
    private Stage stage;

    /**
     * This method is required to be called once before using the controller.
     */
    public void init() {
        this.setupInputFields();
        // load default config settings
        Path resourceDirectory = Paths.get("src","main","resources","default");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        this.loadConfig(absolutePath);
    }

    /**
     * Creates input fields where user can specify simulation settings.
     */
    public void setupInputFields() {
        int height = grid.getRowCount();

        Field[] attributes = Config.class.getDeclaredFields();
        for (Field field : attributes) {
            if (field.getName().equals("instance") || field.getName().equals("version")) continue; // skip instance and version attribute
            // setup label & input
            Label attribute = new Label(field.getName() + ":");
            TextField textField = new TextField();
            configs.put(field.getName(), textField);
            // add label & input to grid
            grid.add(attribute, 0, height);
            grid.add(textField, 1, height++);
        }

        GridPane.setRowIndex(startButton, height);
        GridPane.setRowIndex(clearButton, height);
    }


    private boolean isConfigsValid() {
        // loading all possible classes
        String registerPath = "";
        ArrayList<String> animalMapType = new ArrayList<>();
        ArrayList<String> plantMapType = new ArrayList<>();
        ArrayList<String> genomeType = new ArrayList<>();
        ArrayList<String> brainType = new ArrayList<>();
        try {
            // load register path
            registerPath = Paths.get("src/main/resources/registeredClasses.json").toUri().toURL().getPath();
            // load registered classes
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject)parser.parse(new FileReader(registerPath));
            JSONArray JSONanimalMapType = (JSONArray)obj.get("animalMapType");
            for(Object o : JSONanimalMapType) animalMapType.add((String) o);
            JSONArray JSONplantMapType = (JSONArray)obj.get("plantMapType");
            for(Object o : JSONplantMapType) plantMapType.add((String) o);
            JSONArray JSONgenomeType = (JSONArray)obj.get("genomeType");
            for(Object o : JSONgenomeType) genomeType.add((String) o);
            JSONArray JSONbrainType = (JSONArray)obj.get("brainType");
            for(Object o : JSONbrainType) brainType.add((String) o);
        } catch (Exception e) {
            ExceptionHandler.printCriticalInfo(e);
        }
        for(Map.Entry<String, TextField> entry : configs.entrySet()) { // check if all fields are valid
            switch (entry.getKey()) {
                case "mapSize":
                    if (!Pattern.matches("^\\([1-9][0-9]*,[1-9][0-9]*\\)$", entry.getValue().getCharacters())) return false;
                    break;
                case "animalMapType":
                    if (!animalMapType.contains(entry.getValue().getCharacters().toString())) return false;
                    break;
                case "startingPlantCount":
                    if (!Pattern.matches("^[1-9][0-9]*$", entry.getValue().getCharacters())) return false;
                    break;
                case "plantNutritionalValue":
                    if (!Pattern.matches("^[1-9][0-9]*$", entry.getValue().getCharacters())) return false;
                    break;
                case "plantGrowCount":
                    if (!Pattern.matches("^[1-9][0-9]*$", entry.getValue().getCharacters())) return false;
                    break;
                case "plantMapType":
                    if (!plantMapType.contains(entry.getValue().getCharacters().toString())) return false;
                    break;
                case "startingAnimalCount":
                    if (!Pattern.matches("^[1-9][0-9]*$", entry.getValue().getCharacters())) return false;
                    break;
                case "startingAnimalEnergy":
                    if (!Pattern.matches("^[1-9][0-9]*$", entry.getValue().getCharacters())) return false;
                    break;
                case "reproduceRequiredEnergy":
                    if (!Pattern.matches("^[1-9][0-9]*$", entry.getValue().getCharacters())) return false;
                    break;
                case "reproduceEnergy":
                    if (!Pattern.matches("^[1-9][0-9]*$", entry.getValue().getCharacters())) return false;
                    break;
                case "mutationCount":
                    if (!Pattern.matches("^\\([0-9]+,[0-9]+\\)$", entry.getValue().getCharacters())) return false;
                    String[] args = entry.getValue().getCharacters().toString().replace(")", "").replace("(", "").split(",");
                    if (Integer.parseInt(args[0]) > Integer.parseInt(args[1])) return false; // e.g. (4,2)
                    break;
                case "genomeType":
                    if (!genomeType.contains(entry.getValue().getCharacters().toString())) return false;
                    break;
                case "genomeSize":
                    if (!Pattern.matches("^[1-9][0-9]*$", entry.getValue().getCharacters())) return false;
                    break;
                case "brainType":
                    if (!brainType.contains(entry.getValue().getCharacters().toString())) return false;
                    break;
            }
        }
        // check if given inputs are valid between each other e.g. if mutationCount <= genomeSize etc
        // load map surface
        String[] mapArgs = configs.get("mapSize").getCharacters().toString().replace(")", "").replace("(", "").split(",");
        int mapSurface = Integer.parseInt(mapArgs[0]) * Integer.parseInt(mapArgs[1]);
        if (Integer.parseInt(configs.get("startingPlantCount").getCharacters().toString()) > mapSurface) return false;
        if (Integer.parseInt(configs.get("startingAnimalCount").getCharacters().toString()) > mapSurface) return false;
        if (Integer.parseInt(configs.get("reproduceEnergy").getCharacters().toString()) > Integer.parseInt(configs.get("reproduceRequiredEnergy").getCharacters().toString())) return false;
        // load mutation count
        String[] mutArgs = configs.get("mutationCount").getCharacters().toString().replace(")", "").replace("(", "").split(",");
        if (Integer.parseInt(mutArgs[0]) > Integer.parseInt(mutArgs[1])) return false; // e.g. [5,3] <- mutationCount
        if (Integer.parseInt(mutArgs[1]) > Integer.parseInt(configs.get("genomeSize").getCharacters().toString())) return false; // e.g. [2,3] <- mutationCount & 1 <- genomeSize

        return true;
    }


    private void saveConfig(String to) {
        JSONObject configObjects = new JSONObject();
        JSONObject settings = new JSONObject();
        configObjects.put("version",Config.getVersion());
        for (String key: configs.keySet()) {
            if (Pattern.matches("^\\([0-9]+,[0-9]+\\)$", configs.get(key).getCharacters())) { // e.g. '(12,25)'
                JSONArray arr = new JSONArray();
                String[] sArr = configs.get(key).getCharacters().toString().replace(")", "").replace("(", "").split(",");
                for (String s : sArr) arr.add(Integer.parseInt(s));
                settings.put(key, arr);
            } else if (Pattern.matches("^[1-9][0-9]*$", configs.get(key).getCharacters())) { // e.g. '31'
                settings.put(key, Integer.parseInt(configs.get(key).getCharacters().toString()));
            } else { // normal string
                settings.put(key, configs.get(key).getCharacters().toString());
            }
        }
        configObjects.put("settings", settings);
        try {
            String path = new File(".").getCanonicalPath()+"\\configs\\config-" + to + ".json";
            try (final FileWriter writer = new FileWriter(path)) {
                writer.write(configObjects.toString());
                writer.flush();
            }
        } catch (IOException e) {
            ExceptionHandler.printCriticalInfo(e);
        }
    }


    private void loadConfig(String path) {
        try {
            Config.loadConfig(path);
            Method[] allMethods = Config.class.getDeclaredMethods();
            for (Method method: allMethods) {
                if (!method.getName().startsWith("get") || method.getName().equals("getVersion")) continue;
                String configName = method.getName().replace("get", "");
                configName = Character.toLowerCase(configName.charAt(0)) + configName.substring(1);
                configs.get(configName).setText(method.invoke(Config.class).toString());
            }
        } catch (Exception e) {
            ExceptionHandler.printCriticalInfo(e);
        }
    }

    // fxml

    @FXML
    private void runSimulationInstance() {
        if (!this.isConfigsValid()) return; // check if input is valid
        // get unique id (current time)
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        this.saveConfig(dtf.format(now));
        // run sim with prepared config file
        try {
            // TODO here make ticksPerSec dynamic (value 50)
            // TODO here last boolean value determines if CSV file is created (value true)
            SimulationWindow sim = new SimulationWindow(new File(".").getCanonicalPath()+"\\configs\\"+"config-"+dtf.format(now), -1, 50, true);
            sim.run();
        } catch (Exception e) {
            ExceptionHandler.printCriticalInfo(e);
        }
    }

    @FXML
    private void clearAllSettings(){
        for (TextField textField: configs.values()) {
            textField.clear();
        }
    }

    @FXML
    private void runFileChooser() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        fileChooser.setTitle("Open Config file");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            String path = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf('.'));
            this.loadConfig(path);
        }
    }

    // overrides

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    // getters/setters

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
