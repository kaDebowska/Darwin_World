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

    private SimulationApp application;

    public void setApplication(SimulationApp application) {
        this.application = application;
    }

    public void initialize() {
        // Create a new SpinnerValueFactory
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 2);

        // Set the value factory to the spinner
        startAnimalsField.setValueFactory(valueFactory);

        // Add a listener to commit the editor text when it loses focus
        startAnimalsField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                startAnimalsField.increment(0); // won't change value, but will commit editor
            }
        });
    }

    @FXML
    private void onSimulationStartClicked() {
        int animalsAtStart = startAnimalsField.getValue();
        try {
            application.startNewSimulation(animalsAtStart);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}