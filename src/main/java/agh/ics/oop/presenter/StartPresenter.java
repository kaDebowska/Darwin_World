package agh.ics.oop.presenter;

import agh.ics.oop.SimulationApp;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

import java.io.IOException;

public class StartPresenter {
    @FXML
    private Spinner<Integer> mapWidth;
    @FXML
    public Spinner<Integer> mapHeight;
    @FXML
    private Spinner<Integer> startAnimalsField;
    @FXML
    public Spinner<Integer> startPlantsField;

    private SimulationApp application;

    public void setApplication(SimulationApp application) {
        this.application = application;
    }

    public void initialize() {
        initializeSpinner(mapWidth, 4, 1000, 10);
        initializeSpinner(mapHeight, 4, 1000, 10);
        initializeSpinner(startAnimalsField, 1, 100, 17);
        initializeSpinner(startPlantsField, 1, 100, 13);
    }

    private void initializeSpinner(Spinner<Integer> spinner, int min, int max, int initialValue) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue);
        spinner.setValueFactory(valueFactory);
        spinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                spinner.increment(0);
            }
        });
    }


    @FXML
    private void onSimulationStartClicked() {
        int animalsAtStart = startAnimalsField.getValue();
        int plantsAtStart = startPlantsField.getValue();
        int width = mapWidth.getValue();
        int height = mapHeight.getValue();
        try {
            application.startNewSimulation(width, height, animalsAtStart, plantsAtStart);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}