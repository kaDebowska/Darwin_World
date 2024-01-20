package agh.ics.oop.presenter;

import agh.ics.oop.SimulationApp;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

import java.io.IOException;

public class StartPresenter {
    @FXML
    private Spinner<Integer> startAnimalsField;
    @FXML
    public Spinner<Integer> startPlantsField;

    private SimulationApp application;

    public void setApplication(SimulationApp application) {
        this.application = application;
    }

    public void initialize() {
        SpinnerValueFactory<Integer> animalValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 17);
        SpinnerValueFactory<Integer> plantsValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 13);
        startAnimalsField.setValueFactory(animalValueFactory);
        startPlantsField.setValueFactory(plantsValueFactory);
        startAnimalsField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                startAnimalsField.increment(0);
            }
        });
        startPlantsField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                startPlantsField.increment(0);
            }
        });
    }

    @FXML
    private void onSimulationStartClicked() {
        int animalsAtStart = startAnimalsField.getValue();
        int plantsAtStart = startPlantsField.getValue();
        try {
            application.startNewSimulation(animalsAtStart, plantsAtStart);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}