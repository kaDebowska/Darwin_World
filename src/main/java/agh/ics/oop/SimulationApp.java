package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.presenter.BehaviourVariant;
import agh.ics.oop.presenter.SimulationPresenter;
import agh.ics.oop.presenter.StartPresenter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimulationApp extends Application {

    private List<Simulation> simulations = new ArrayList<>();
    private SimulationEngine simulationEngine = new SimulationEngine(this.simulations);

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("start.fxml"));

        BorderPane viewRoot = loader.load();

        StartPresenter presenter = loader.getController();
        presenter.setApplication(this);

        configureStage(primaryStage, viewRoot);
        primaryStage.show();

        //        smooth finish
        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public void startNewSimulation(BehaviourVariant behaviourVariant, int width, int height, int startAnimalsField,
                                   int startPlantsField, int plantsEnergy, int initialHealth, int genomeLength,
                                   int healthToReproduce, int reproductionCost, int minMutations, int maxMutations,
                                   boolean loggingEnabled, File saveFolder) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();

        SimulationPresenter presenter = loader.getController();
        AbstractWorldMap map = new WorldMapBuilder()
                .setGlobeParameters(width, height, startPlantsField, plantsEnergy)
                .setAnimalParameters(behaviourVariant, startAnimalsField, initialHealth, genomeLength)
                .setReproductionParameters(healthToReproduce, reproductionCost, minMutations, maxMutations)
                .build();


        if (loggingEnabled) {
            if (saveFolder != null) {
                MapCVSLogger fileDisplay = new MapCVSLogger(map.getId(), saveFolder);
                map.subscribe(fileDisplay);
            } else {
                System.err.println("Please select a directory to save the logs.");
            }
        }

        Stage stage = new Stage();
        configureStage(stage, viewRoot);
        stage.show();
        Simulation newSimulation = new Simulation(map);

        presenter.setWorldMap(newSimulation);

//        simulations.add(newSimulation);
//        simulationEngine.runAsync();
////        simulationEngine.runAsyncInThreadPool();

        List<Simulation> simulations = new ArrayList<>();
        simulations.add(newSimulation);

        SimulationEngine simulationEngine = new SimulationEngine(simulations);
        simulationEngine.runAsync();

        stage.setOnCloseRequest(event -> {
            newSimulation.pause();
//            event.consume();
        });
    }


    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("styles.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Darwin World D-3");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }


}
