package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.chart.LineChart;

import java.util.Optional;

public class SimulationPresenter implements MapChangeListener, AnimalObserver {
    private Simulation simulation;

    @FXML
    private Button pauseButton;
    @FXML
    private Button resumeButton;

    private static final int CELL_WIDTH = 30;
    private static final int CELL_HEIGHT = 30;
    private static final String EMPTY_CELL = " ";

    @FXML
    private GridPane mapGrid, infoGridAnimalLong, infoGridAnimal2column;
    @FXML
    private Label animalID, genome, activeGene, health, plantsEaten, kidsNo, offspring, age;
    @FXML
    public Label dayNo, avHealth, animalNo, plantsNo, unoccupiedPositionsNo, mostCommonGenome, deadAverage, kidsAverage;
    @FXML
    private TextArea animalInfo;
    @FXML
    private LineChart<Number, Number> chart;
    private XYChart.Series<Number, Number> animalsSeries;
    private XYChart.Series<Number, Number> plantsSeries;
    private Animal selectedAnimal;

    public void setWorldMap(Simulation simulation) {
        this.simulation = simulation;
        AbstractWorldMap worldMap = this.simulation.getMap();
        worldMap.subscribe(this);
    }

    @FXML
    public void initialize() {
        pauseButton.setOnAction(e -> pauseSimulation());
        resumeButton.setOnAction(e -> resumeSimulation());

        animalsSeries = new XYChart.Series<>();
        animalsSeries.setName("Animals");
        chart.getData().add(animalsSeries);

        plantsSeries = new XYChart.Series<>();
        plantsSeries.setName("Plants");
        chart.getData().add(plantsSeries);

        mapGrid.addEventFilter(AnimalClickedEvent.ANIMAL_CLICKED_EVENT_TYPE, event -> {
            Animal animal = ((AnimalClickedEvent) event).getAnimal();
            onAnimalClicked(animal);
        });
    }

    public void onAnimalClicked(Animal animal) {
        if (this.selectedAnimal != null) {
            // Unsubscribe the previously selected animal
            this.selectedAnimal.unsubscribe(this);
        }

        infoGridAnimalLong.setVisible(true);
        infoGridAnimal2column.setVisible(true);

        // Subscribe the newly clicked animal and set it as the selected animal
        animal.subscribe(this);
        this.selectedAnimal = animal;
        this.selectedAnimal.callObservers();
    }


    public void pauseSimulation() {
        if (simulation != null) {
            simulation.pause();
            pauseButton.setDisable(true);
            resumeButton.setDisable(false);
        }
    }

    public void resumeSimulation() {
        if (simulation != null) {
            Platform.runLater(simulation::resume);
            pauseButton.setDisable(false);
            resumeButton.setDisable(true);
        }
    }

    public void drawMap(WorldMap worldMap, String normalizer) {
        if (worldMap == null) {
            throw new IllegalArgumentException("WorldMap cannot be null");
        }

        clearGrid();

        Boundary boundary = worldMap.getCurrentBounds();
        int width = boundary.topRight().getX() - boundary.bottomLeft().getX();
        int height = boundary.topRight().getY() - boundary.bottomLeft().getY();

        addLabel("x/y", 0, 0);
        mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));

        for (int i = 1; i <= width + 1; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
            addLabel(Integer.toString(i + boundary.bottomLeft().getX() - 1), i, 0);
        }

        for (int i = 1; i <= height + 1; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
            addLabel(Integer.toString(boundary.topRight().getY() - i + 1), 0, i);
        }

        for (int x = 0; x <= width; x++) {
            for (int y = 0; y <= height; y++) {
                Vector2d position = new Vector2d(x + boundary.bottomLeft().getX(), boundary.topRight().getY() - y);
                Optional<WorldElement> element = worldMap.objectAt(position);
                if (element.isPresent()) {
//                    String labelContent = element.map(WorldElement::toString).orElse(EMPTY_CELL);
//                    addLabel(labelContent, x + 1, y + 1);
                    WorldElementBox box = new WorldElementBox(element.get(), normalizer, this.selectedAnimal);
                    mapGrid.add(box, x + 1, y + 1);

                } else {
                    addLabel(EMPTY_CELL, x + 1, y + 1);
                }
            }
        }
    }


    private void addLabel(String text, int colIndex, int rowIndex) {
        Label label = new Label(text);
        GridPane.setHalignment(label, HPos.CENTER);
        mapGrid.add(label, colIndex, rowIndex);
    }


    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    @Override
    public void onMapChange(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            String[] values = message.split(";");
            this.drawMap(worldMap, values[7]);
            dayNo.setText(values[0]);
            animalNo.setText(values[1]);
            plantsNo.setText(values[2]);
            avHealth.setText(values[7]);
            unoccupiedPositionsNo.setText(values[3]);
            mostCommonGenome.setText(values[4]);
            deadAverage.setText(values[5]);
            kidsAverage.setText(values[6]);

            int day = Integer.parseInt(values[0]);
            int animalsCount = Integer.parseInt(values[1]);
            int plantsCount = Integer.parseInt(values[2]);

            animalsSeries.getData().add(new XYChart.Data<>(day, animalsCount));
            plantsSeries.getData().add(new XYChart.Data<>(day, plantsCount));
            for (XYChart.Series<Number, Number> s : chart.getData()) {
                for (XYChart.Data<Number, Number> d : s.getData()) {
                    d.getNode().setVisible(false);

                }
            }
        });
    }


    @Override
    public void onDailyFatigue(Animal animal, String message) {
        Platform.runLater(() -> {
            String[] values = message.split(";");
            animalID.setText(values[0]);
            genome.setText(values[1]);
            activeGene.setText(values[2]);
            health.setText(values[3]);
            plantsEaten.setText(values[4]);
            kidsNo.setText(values[5]);
            offspring.setText(values[6]);
            age.setText(values[7]);
        });
    }

}
