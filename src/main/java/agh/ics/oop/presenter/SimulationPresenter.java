package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.List;

public class SimulationPresenter implements MapChangeListener {

    private WorldMap worldMap;

//    @FXML
//    private Label infoLabel;

    private static final int CELL_WIDTH = 40;
    private static final int CELL_HEIGHT = 40;
    private static final String EMPTY_CELL = " ";

    @FXML
    private GridPane mapGrid;


    @FXML
    private Label movesLabel;


    public void setWorldMap(WorldMap map) {
        this.worldMap = map;
        this.worldMap.subscribe(this);
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
                WorldElement element = worldMap.objectAt(position);
                String labelContent = (element == null) ? EMPTY_CELL : element.toString();
                addLabel(labelContent, x + 1, y + 1);
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
            this.movesLabel.setText(message);
        });
    }

    @FXML
    public void onSimulationStartClicked(Simulation simulation) {


        List<Simulation> simulations = new ArrayList<>();
        simulations.add(simulation);

        SimulationEngine simulationEngine = new SimulationEngine(simulations);
        simulationEngine.runAsync();
    }

}
