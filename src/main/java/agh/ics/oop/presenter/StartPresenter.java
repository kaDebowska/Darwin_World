package agh.ics.oop.presenter;

import agh.ics.oop.SimulationApp;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class StartPresenter {

    private SimulationApp application;

    @FXML
//    private TextField movesField;

    public void setApplication(SimulationApp application) {
        this.application = application;
    }

    @FXML
    private void onSimulationStartClicked() {
//        String moves = movesField.getText();
        try {
            application.startNewSimulation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}