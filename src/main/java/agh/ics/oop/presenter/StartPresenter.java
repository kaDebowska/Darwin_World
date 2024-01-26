package agh.ics.oop.presenter;

import agh.ics.oop.SimulationApp;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

public class StartPresenter {
    @FXML
    private Spinner<Integer> mapWidth;
    @FXML
    public Spinner<Integer> mapHeight;
    @FXML
    public Spinner<Integer> startPlantsField;

    @FXML
    public Spinner<Integer> everyDayPlantsField;
    @FXML
    public Spinner<Integer> plantsEnergyField;
    @FXML
    private Spinner<Integer> startAnimalsField;
    @FXML
    public Spinner<Integer> initialHealth;
    @FXML
    public Spinner<Integer> healthToReproduce;
    @FXML
    public Spinner<Integer> reproductionCost;
    @FXML
    public Spinner<Integer> minMutationField;
    @FXML
    public Spinner<Integer> maxMutationField;
    @FXML
    public Spinner<Integer> genomeLength;
    @FXML
    public Spinner<Integer> fertilityTime;
    @FXML
    private ComboBox<BehaviourVariant> animalTypeComboBox;
    @FXML
    private ComboBox<MapVariant> plantsGrowingTypeComboBox;
    @FXML
    private CheckBox saveLogsCheckBox;
    @FXML
    private Button selectFolderButton;
    private File saveFolder;


    private SimulationApp application;

    public void setApplication(SimulationApp application) {
        this.application = application;
    }

    public void initialize() {
        initializeSpinner(mapWidth, 1, 1000, 13);
        initializeSpinner(mapHeight, 1, 1000, 19);
        initializeSpinner(startPlantsField, 0, mapWidth.getValue() * mapHeight.getValue(), 13);
        initializeSpinner(everyDayPlantsField, 0, mapWidth.getValue() * mapHeight.getValue(), 13);
        initializeSpinner(plantsEnergyField, 1, 71, 3);
        initializeSpinner(startAnimalsField, 0, 100, 17);
        initializeSpinner(initialHealth, 1, 1000, 31);
        initializeSpinner(healthToReproduce, 0, 100, 23);
        initializeSpinner(reproductionCost, 0, healthToReproduce.getValue(), 11);
        initializeSpinner(genomeLength, 0, 100, 23);
        initializeSpinner(maxMutationField, 0, genomeLength.getValue(), 7);
        initializeSpinner(minMutationField, 0, maxMutationField.getValue(), 2);
        initializeSpinner(fertilityTime, 0, 100, 0);
        animalTypeComboBox.getItems().addAll(BehaviourVariant.values());
        animalTypeComboBox.setValue(BehaviourVariant.NORMAL_ANIMAL);


        plantsGrowingTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateFertilityTimeState(fertilityTime, plantsGrowingTypeComboBox.getValue());
        });

        plantsGrowingTypeComboBox.getItems().addAll(MapVariant.values());
        plantsGrowingTypeComboBox.setValue(MapVariant.GLOBE_MAP);

        mapWidth.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateMaxSpinnerValue(startPlantsField, mapWidth.getValue() * mapHeight.getValue());
        });

        mapHeight.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateMaxSpinnerValue(startPlantsField, mapWidth.getValue() * mapHeight.getValue());
        });

        maxMutationField.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateMaxSpinnerValue(minMutationField, maxMutationField.getValue());
        });

        genomeLength.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateMaxSpinnerValue(maxMutationField, genomeLength.getValue());
        });

        healthToReproduce.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateMaxSpinnerValue(reproductionCost, healthToReproduce.getValue());
        });

        selectFolderButton.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            URL url = getClass().getResource("/logs");
            if (url != null) {
                try {
                    File logsDir = new File(url.toURI());
                    directoryChooser.setInitialDirectory(logsDir);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
            saveFolder = directoryChooser.showDialog(null);
        });
    }

    private void initializeSpinner(Spinner<Integer> spinner, int min, int max, int initialValue) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue);
        spinner.setValueFactory(valueFactory);
//        fertilityTime.setDisable(true);
        spinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                spinner.increment(0);
            }
        });
    }

    private void updateMaxSpinnerValue(Spinner<Integer> spinner, int max) {
        int currentValue = spinner.getValue();
        if (currentValue > max) {
            currentValue = max;
        }
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, max, currentValue));
    }

    private void updateFertilityTimeState(Spinner<Integer> spinner, MapVariant mapVariant) {
        if(mapVariant == MapVariant.CARCASS_MAP) {
            spinner.setDisable(false);
            spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 17));
        } else {
            spinner.setDisable(true);
            spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0));
        }
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
            config.setStartPlants(startPlantsField.getValue());
            config.setEveryDayPlants(everyDayPlantsField.getValue());
            config.setPlantsEnergy(plantsEnergyField.getValue());
            config.setStartAnimals(startAnimalsField.getValue());
            config.setBehaviourVariant(animalTypeComboBox.getValue());
            config.setMapVariant(plantsGrowingTypeComboBox.getValue());
            config.setInitialHealth(initialHealth.getValue());
            config.setHealthToReproduce(healthToReproduce.getValue());
            config.setReproductionCost(reproductionCost.getValue());
            config.setGenomeLength(genomeLength.getValue());
            config.setMaxMutationField(maxMutationField.getValue());
            config.setMinMutationField(minMutationField.getValue());
            config.setFertilityTime(fertilityTime.getValue());
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
                startPlantsField.getValueFactory().setValue(config.getStartPlants());
                everyDayPlantsField.getValueFactory().setValue(config.getEveryDayPlants());
                plantsEnergyField.getValueFactory().setValue(config.getPlantsEnergy());
                startAnimalsField.getValueFactory().setValue(config.getStartAnimals());
                animalTypeComboBox.setValue(config.getBehaviourVariant());
                plantsGrowingTypeComboBox.setValue(config.getMapVariant());
                initialHealth.getValueFactory().setValue(config.getInitialHealth());
                healthToReproduce.getValueFactory().setValue(config.getHealthToReproduce());
                reproductionCost.getValueFactory().setValue(config.getReproductionCost());
                genomeLength.getValueFactory().setValue(config.getGenomeLength());
                maxMutationField.getValueFactory().setValue(config.getMaxMutationField());
                minMutationField.getValueFactory().setValue(config.getMinMutationField());
                fertilityTime.getValueFactory().setValue(config.getFertilityTime());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    private void onSimulationStartClicked() {
        int animalsAtStart = startAnimalsField.getValue();
        int plantsAtStart = startPlantsField.getValue();
        int everyDayPlants = everyDayPlantsField.getValue();
        int width = mapWidth.getValue() - 1;
        int height = mapHeight.getValue() - 1;
        BehaviourVariant behaviourVariant = animalTypeComboBox.getValue();
//        MapVariant mapVariant = plantsGrowingTypeComboBox.getValue();
        int plantsEnergy = plantsEnergyField.getValue();
        int animalInitialHealth = initialHealth.getValue();
        int animalGenomeLength = genomeLength.getValue();
        int reproductionTreshold = healthToReproduce.getValue();
        int reproductionPrice = reproductionCost.getValue();
        int minMutations = minMutationField.getValue();
        int maxMutations = maxMutationField.getValue();
        int daysOfFertility = fertilityTime.getValue();
        boolean loggingEnabled = saveLogsCheckBox.isSelected();
        try {
            application.startNewSimulation(behaviourVariant, width, height, animalsAtStart, plantsAtStart, everyDayPlants, plantsEnergy,
                    animalInitialHealth, animalGenomeLength, reproductionTreshold, reproductionPrice, minMutations, maxMutations,
                    daysOfFertility, loggingEnabled, this.saveFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}