package agh.ics.oop.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;

import java.io.Serializable;

public class Configuration implements Serializable {
    private int mapWidth;
    private int mapHeight;
    private BehaviourVariant behaviourVariant;
    private int startAnimals;
    private int startPlants;
    private int plantsEnergy;
    private int initialHealth;
    private int healthToReproduce;
    private int reproductionCost;
    private int minMutationField;
    private int maxMutationField;
    private int genomeLength;


    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getStartAnimals() {
        return startAnimals;
    }

    public int getStartPlants() {
        return startPlants;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public void setStartAnimals(int startAnimals) {
        this.startAnimals = startAnimals;
    }

    public void setStartPlants(int startPlants) {
        this.startPlants = startPlants;
    }

    public BehaviourVariant getBehaviourVariant() {
        return behaviourVariant;
    }

    public void setBehaviourVariant(BehaviourVariant behaviourVariant) {
        this.behaviourVariant = behaviourVariant;
    }

    public int getPlantsEnergy() {
        return plantsEnergy;
    }

    public void setPlantsEnergy(int plantsEnergy) {
        this.plantsEnergy = plantsEnergy;
    }

    public int getInitialHealth() {
        return initialHealth;
    }

    public void setInitialHealth(int initialHealth) {
        this.initialHealth = initialHealth;
    }

    public int getHealthToReproduce() {
        return healthToReproduce;
    }

    public void setHealthToReproduce(int healthToReproduce) {
        this.healthToReproduce = healthToReproduce;
    }

    public int getReproductionCost() {
        return reproductionCost;
    }

    public void setReproductionCost(int reproductionCost) {
        this.reproductionCost = reproductionCost;
    }

    public int getMinMutationField() {
        return minMutationField;
    }

    public void setMinMutationField(int minMutationField) {
        this.minMutationField = minMutationField;
    }

    public int getMaxMutationField() {
        return maxMutationField;
    }

    public void setMaxMutationField(int maxMutationField) {
        this.maxMutationField = maxMutationField;
    }

    public int getGenomeLength() {
        return genomeLength;
    }

    public void setGenomeLength(int genomeLength) {
        this.genomeLength = genomeLength;
    }
}
