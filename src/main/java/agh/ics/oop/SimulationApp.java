package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.presenter.SimulationPresenter;
import agh.ics.oop.presenter.StartPresenter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SimulationApp extends Application {

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

    public void startNewSimulation(int startAnimalsField, int startPlantsField) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();

        SimulationPresenter presenter = loader.getController();
        AbstractWorldMap globeMap = new GlobeMap(10, 10, startAnimalsField, startPlantsField, 4, 20, 10, 0, 10);
        presenter.setWorldMap(globeMap);

        Stage stage = new Stage();
        configureStage(stage, viewRoot);
        stage.show();
        Simulation newSimulation = new Simulation(globeMap);

        presenter.onSimulationStartClicked(newSimulation);
    }


    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("styles.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }


}
