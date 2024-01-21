package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimulationPresenter implements MapChangeListener {

    private Simulation simulation;

    @FXML
    private Button pauseButton;
    @FXML
    private Button resumeButton;

    private static final int CELL_WIDTH = 40;
    private static final int CELL_HEIGHT = 40;
    private static final String EMPTY_CELL = " ";

    @FXML
    private GridPane mapGrid;

    @FXML
    private Label dayNo;
    @FXML
    private Label animalNo;
    @FXML
    private Label plantsNo;
    @FXML
    private Label unoccupiedPositionsNo;
    @FXML
    private Label mostCommonGenome;
    @FXML
    private Label deadAverage;
    @FXML
    private Label kidsAverage;


    public void setWorldMap(Simulation simulation) {
        this.simulation = simulation;
        AbstractWorldMap worldMap = this.simulation.getMap();
        worldMap.subscribe(this);
    }

    @FXML
    public void initialize() {
        pauseButton.setOnAction(e -> pauseSimulation());
        resumeButton.setOnAction(e -> resumeSimulation());
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

    public void drawMap(WorldMap worldMap) {
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
                    String labelContent = element.map(WorldElement::toString).orElse(EMPTY_CELL);
                    addLabel(labelContent, x + 1, y + 1);
//                    WorldElementBox box = new WorldElementBox(element.get());
//                    mapGrid.add(box, x + 1, y + 1);
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
            this.drawMap(worldMap);
            String[] values = message.split(";");
            dayNo.setText(values[0]);
            animalNo.setText(values[1]);
            plantsNo.setText(values[2]);
            unoccupiedPositionsNo.setText(values[3]);
            mostCommonGenome.setText(values[4]);
            deadAverage.setText(values[5]);
            kidsAverage.setText(values[6]);
        });
    }


}
