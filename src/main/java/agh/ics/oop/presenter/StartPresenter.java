package agh.ics.oop.presenter;

import agh.ics.oop.SimulationApp;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;

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
        initializeSpinner(mapWidth, 1, 1000, 10);
        initializeSpinner(mapHeight, 1, 1000, 10);
        initializeSpinner(startAnimalsField, 0, 100, 17);
        initializeSpinner(startPlantsField, 0, mapWidth.getValue() * mapHeight.getValue(), 13);

        mapWidth.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateMaxPlants();
        });

        mapHeight.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateMaxPlants();
        });
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

    private void updateMaxPlants() {
        int maxPlants = mapWidth.getValue() * mapHeight.getValue();
        int currentValue = startPlantsField.getValue();
        if (currentValue > maxPlants) {
            currentValue = maxPlants;
        }
        startPlantsField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, maxPlants, currentValue));
    }

    @FXML
    private void onSaveConfigurationClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Configuration");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SER files (*.ser)", "*.ser"));
        URL url = getClass().getResource("/config");
        fileChooser.setInitialDirectory(new File(url.getPath()));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            Configuration config = new Configuration();
            config.setMapWidth(mapWidth.getValue());
            config.setMapHeight(mapHeight.getValue());
            config.setStartAnimals(startAnimalsField.getValue());
            config.setStartPlants(startPlantsField.getValue());

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
                out.writeObject(config);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    private void onLoadConfigurationClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Configuration");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SER files (*.ser)", "*.ser"));
        URL url = getClass().getResource("/config");
        fileChooser.setInitialDirectory(new File(url.getPath()));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                Configuration config = (Configuration) in.readObject();
                mapWidth.getValueFactory().setValue(config.getMapWidth());
                mapHeight.getValueFactory().setValue(config.getMapHeight());
                startAnimalsField.getValueFactory().setValue(config.getStartAnimals());
                startPlantsField.getValueFactory().setValue(config.getStartPlants());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }



    @FXML
    private void onSimulationStartClicked() {
        int animalsAtStart = startAnimalsField.getValue();
        int plantsAtStart = startPlantsField.getValue();
        int width = mapWidth.getValue()  - 1;
        int height = mapHeight.getValue() - 1;
        try {
            application.startNewSimulation(width, height, animalsAtStart, plantsAtStart);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}