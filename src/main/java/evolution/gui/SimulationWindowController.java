package evolution.gui;

import evolution.Simulation;
import evolution.genomes.Genome;
import evolution.main.Animal;
import evolution.memories.AnimalMemory;
import evolution.threads.SimulationThreadGUI;
import evolution.util.Pair;
import evolution.util.Vector2;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.stream.Stream;

/**
 * GUI class that displays the state of the simulation and communicates with user via basic buttons.
 */
public class SimulationWindowController implements Initializable {

    @javafx.fxml.FXML
    private Button startButton;
    @javafx.fxml.FXML
    private VBox statsBox;
    @javafx.fxml.FXML
    private VBox animalTrackBox;
    @javafx.fxml.FXML
    private GridPane grid;
    @javafx.fxml.FXML
    private Button mostCommonGenomeButton;
    @javafx.fxml.FXML
    private Button clearTrackingButton;

    private Simulation simulation;
    private SimulationTracker simulationTracker;
    private SimulationThreadGUI simulationThread;
    private Thread thread;
    private final ArrayList<ArrayList<String>> dumpData = new ArrayList<>();
    private boolean dumpingData;
    private Animal trackedAnimal = null;
    private boolean isTracking = false;


    public void init(Simulation simulation, int tickCount, int ticksPerSec, boolean dumpData) {
        this.simulation = simulation;
        this.simulationTracker = new SimulationTracker();
        this.simulationThread = new SimulationThreadGUI(this.simulation, tickCount, ticksPerSec, this.simulationTracker, this);
        this.thread = new Thread(this.simulationThread);
        this.dumpingData = dumpData;
    }


    public void start() {
        this.thread.start();
    }


    public void update(){
        if (this.dumpingData) this.dumpData.add(new ArrayList<>(Arrays.asList(this.simulationTracker.dumpAllData())));
        this.updateSimulationStats();
        this.updateSimulationView();
        if (this.isTracking) this.updateTrackedAnimalStats();
    }


    public void show(){
        Platform.runLater(this::update);
    }


    public void updateSimulationStats() {
        ((Label)statsBox.lookup("#statAnimalCount")).setText(String.valueOf(this.simulationTracker.getAnimalCount()));
        ((Label)statsBox.lookup("#statPlantCount")).setText(String.valueOf(this.simulationTracker.getPlantCount()));
        ((Label)statsBox.lookup("#statEmptyTilesCount")).setText(String.valueOf(this.simulationTracker.getEmptyTilesCount()));
        ArrayList<Pair<Genome, Integer>> genomes = this.simulationTracker.getGenomesCount();
        if (genomes.size() == 0) {
            ((Label)statsBox.lookup("#statMostCommonGenome")).setText("none");
        } else {
            Pair<Genome, Integer> mostCommonGenome = genomes.get(0);
            ((Label) statsBox.lookup("#statMostCommonGenome")).setText(
                    String.format("%s, count: %s", mostCommonGenome.first, mostCommonGenome.second)
            );
        }
        ((Label)statsBox.lookup("#statAverageAnimalEnergy")).setText(String.valueOf(this.simulationTracker.getAverageAnimalEnergy()));
        ((Label)statsBox.lookup("#statAverageAnimalLifespan")).setText(String.valueOf(this.simulationTracker.getAverageAnimalLifespan()));
    }


    public void updateSimulationView() {
        Vector2 size = this.simulation.getWorld().getEnvironment().getSize();

        this.setupGrid(this.grid, size);
        this.fillGrid(size, this.simulation);
    }


    private double getCellWidth(GridPane grid, Vector2 size) {
        return grid.getLayoutBounds().getWidth() / size.x - Math.max(1.0, 24*(1.0 / size.x));
    }


    private double getCellHeight(GridPane grid, Vector2 size) {
        return grid.getLayoutBounds().getHeight() / size.y - Math.max(1.0, 24*(1.0 / size.y));
    }


    private void setupGrid(GridPane grid, Vector2 size) {
        // clear old
        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();
        grid.getChildren().clear();

        // setup rows & cols
        for(int y = 0; y < size.y; y++) {
            grid.getRowConstraints().add(new RowConstraints(this.getCellHeight(grid, size)));
        }

        for(int x = 0; x < size.x; x++) {
            grid.getColumnConstraints().add(new ColumnConstraints(this.getCellWidth(grid, size)));
        }
    }


    private void fillGrid(Vector2 size, Simulation simulation) {
        this.drawPlants(size, simulation);
        this.drawAnimals(size, simulation);
    }


    private void drawPlants(Vector2 size, Simulation simulation) {
        for(int y = 0; y < size.y; y++) {
            for(int x = 0; x < size.x; x++) {
                if (!simulation.getWorld().getEnvironment().getPlantMap().isEmpty(new Vector2(x, y))) {
                    grid.add(new Rectangle(this.getCellWidth(this.grid, size), this.getCellHeight(this.grid, size), javafx.scene.paint.Color.rgb(84, 236, 104)), x, y);
                } else { // if no plants draw checkerboard pattern
                    int color = ((x+y) % 2 == 1) ? 255 : 225;
                    grid.add(new Rectangle(this.getCellWidth(this.grid, size), this.getCellHeight(this.grid, size), javafx.scene.paint.Color.rgb(color, color, color)), x, y);
                }
            }
        }
    }


    private void drawAnimals(Vector2 size, Simulation simulation) {
        for(int y = 0; y < size.y; y++) {
            for(int x = 0; x < size.x; x++) {
                if (!simulation.getWorld().getEnvironment().getAnimalMap().isEmpty(new Vector2(x, y))) {
                    LinkedList<Animal> animals = simulation.getWorld().getEnvironment().getAnimalMap().getObjectsAt(new Vector2(x, y));

                    int animalCount = animals.size();
                    int subGridSize = getAnimalGridSize(animalCount);

                    GridPane subGrid = new GridPane();

                    for(int y1 = 0; y1 < subGridSize; y1++) {
                        subGrid.getRowConstraints().add(new RowConstraints(this.getCellHeight(this.grid, size) / subGridSize));
                    }
                    for(int x1 = 0; x1 < subGridSize; x1++) {
                        subGrid.getColumnConstraints().add(new ColumnConstraints(this.getCellWidth(this.grid, size) / subGridSize));
                    }

                    int counter = 0;
                    for(Animal a : animals) {
                        Ellipse ellipse = new Ellipse(this.getCellWidth(this.grid, size) / subGridSize / 2, this.getCellHeight(this.grid, size) / subGridSize / 2);

                        if (a == this.trackedAnimal) { // by reference !
                            double val = Math.min(Math.max(0, a.getEnergy()), 40)/40.0; // 0.0 - 1.0 clamp
                            ellipse.setStroke(javafx.scene.paint.Color.rgb(128+(int)(127*val), 80+(int)(130*val), (int)(43*val)));
                            ellipse.setFill(javafx.scene.paint.Color.rgb(128+(int)(127*val), 80+(int)(130*val), (int)(43*val)));
                        } else { // not a tracked animal normal colors
                            double val = Math.min(Math.max(0, a.getEnergy()), 40)/40.0; // 0.0 - 1.0 clamp
                            ellipse.setStroke(javafx.scene.paint.Color.rgb(64+(int)(191*val), 64+(int)(96*val), 64+(int)(96*val)));
                            ellipse.setFill(javafx.scene.paint.Color.rgb(64+(int)(191*val), 32+(int)(96*val), 32+(int)(96*val)));
                        }
                        ellipse.setId(String.format("%s-%s-%s", x, y, counter)); // posX posY order in list
                        SimulationWindowController that = this;
                        ellipse.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                that.animalPickHandler(event);
                                event.consume();
                            }
                        });
                        subGrid.add(ellipse, counter % subGridSize, counter / subGridSize);
                        counter++;
                    }

                    grid.add(subGrid, x, y);
                }
            }
        }
    }


    private int getAnimalGridSize(int count) {
        int i = 1;
        while ((i*i) < count) i++;
        return i;
    }


    private void updateTrackedAnimalStats() {
        ((Label)animalTrackBox.lookup("#trackedAnimalPos")).setText(String.valueOf(this.trackedAnimal.getPos()));
        ((Label)animalTrackBox.lookup("#trackedAnimalGenome")).setText(String.valueOf(this.trackedAnimal.getBrain().getGenome()));
        ((Label)animalTrackBox.lookup("#trackedAnimalActiveGene")).setText(String.valueOf(this.trackedAnimal.getBrain().getActiveGene()));
        int energy = this.trackedAnimal.getEnergy();
        ((Label)animalTrackBox.lookup("#trackedAnimalEnergy")).setText(String.valueOf(energy));
        ((Label)animalTrackBox.lookup("#trackedAnimalPlantsEaten")).setText(String.valueOf(((AnimalMemory)this.trackedAnimal.getBrain().getMemory()).getPlantsEaten()));
        ((Label)animalTrackBox.lookup("#trackedAnimalChildrenCount")).setText(String.valueOf(((AnimalMemory)this.trackedAnimal.getBrain().getMemory()).getChildrenCount()));
        if (energy <= 0) {
            ((Label)animalTrackBox.lookup("#trackedAnimalAge")).setText("dead, died at: day " + String.valueOf(this.simulationThread.getTick()+1));
            this.isTracking = false; // stop tracking dead animal
        } else {
            ((Label)animalTrackBox.lookup("#trackedAnimalAge")).setText(String.valueOf(this.trackedAnimal.getAge()));
        }
    }


    private void animalPickHandler(Event e) {
        if (this.simulationThread.isRunning()) return; // only works when sim is stopped

        ((Ellipse)e.getSource()).setStroke(javafx.scene.paint.Color.rgb(252, 210, 43));
        ((Ellipse)e.getSource()).setFill(javafx.scene.paint.Color.rgb(252, 210, 43));

        Integer[] args = Stream.of(((Ellipse)e.getTarget()).getId().split("-")).map(Integer::valueOf).toArray(Integer[]::new);
        LinkedList<Animal> animals = simulation.getWorld().getEnvironment().getAnimalMap().getObjectsAt(new Vector2(args[0], args[1]));
        Animal animal = animals.get(args[2]);
        this.trackedAnimal = animal;
        this.isTracking = true;
    }

    // fxml

    @FXML
    private void startButtonHandler(Event e) {
        if (startButton.getStyleClass().contains("button-start")) {
            startButton.getStyleClass().remove("button-start");
            startButton.getStyleClass().add("button-stop");
            startButton.setText("STOP");
            this.simulationThread.resumeThread();
        } else if (startButton.getStyleClass().contains("button-stop")) {
            startButton.getStyleClass().remove("button-stop");
            startButton.getStyleClass().add("button-start");
            startButton.setText("START");
            this.simulationThread.pauseThread();
        }
    }

    @FXML
    private void mostCommonGenomeButtonHandler(Event e) {
        if (this.simulationThread.isRunning()) return; // only works when sim is stopped

        for(Animal a : this.simulation.getWorld().getEnvironment().getAnimalMap().getObjects()) {
            if (a.getBrain().getGenome().equals(this.simulationTracker.getGenomesCount().get(0).first)) {
                // id might be the same if animals are exactly equal (comparing by reference will fix it but i am honestly done with javafx)
                String id = String.format("%s-%s-%s", a.getPos().x, a.getPos().y, this.simulation.getWorld().getEnvironment().getAnimalMap().getObjectsAt(a.getPos()).indexOf(a));
                ((Ellipse)grid.lookup(String.format("#%s", id))).setStroke(javafx.scene.paint.Color.rgb(154, 28, 250));
                ((Ellipse)grid.lookup(String.format("#%s", id))).setFill(javafx.scene.paint.Color.rgb(154, 28, 250));
            }
        }
    }

    @FXML
    private void clearTrackingButtonHandler(Event e) {
        this.trackedAnimal = null;
        this.isTracking = false;
        // reset labels
        ((Label)animalTrackBox.lookup("#trackedAnimalPos")).setText("");
        ((Label)animalTrackBox.lookup("#trackedAnimalGenome")).setText("");
        ((Label)animalTrackBox.lookup("#trackedAnimalActiveGene")).setText("");
        ((Label)animalTrackBox.lookup("#trackedAnimalEnergy")).setText("");
        ((Label)animalTrackBox.lookup("#trackedAnimalPlantsEaten")).setText("");
        ((Label)animalTrackBox.lookup("#trackedAnimalChildrenCount")).setText("");
        ((Label)animalTrackBox.lookup("#trackedAnimalAge")).setText("");
        ((Label)animalTrackBox.lookup("#trackedAnimalAge")).setText("");
    }

    // overrides

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // setup startButton
        startButton.getStyleClass().add("button-stop");
        startButton.setText("STOP");
        startButton.setPrefWidth(100.0);
        // setup statsBox

        // setup grid
        grid.setGridLinesVisible(true);
    }

    // getters/setters

    public Simulation getSimulation() {
        return this.simulation;
    }


    public Thread getThread() {
        return this.thread;
    }


    public SimulationThreadGUI getSimulationThread() {
        return this.simulationThread;
    }


    public SimulationTracker getSimulationTracker() {
        return this.simulationTracker;
    }


    public String[][] getDumpData() {
        return dumpData.stream().map(l -> l.toArray(String[]::new)).toArray(String[][]::new);
    }
}
