package agh.ics.oop.model;

import agh.ics.oop.presenter.BehaviourVariant;

import java.util.List;
import java.util.Map;

public class WorldMapBuilder {
    private int width;
    private int height;
    private int plantsNum;
    private int plantsEnergy;
    private int boostGrowth = 0;
    private BehaviourVariant behaviourVariant;
    private int animalStartNumber;
    private int animalStartHealth;
    private int animalGenomeLength;
    private int healthToReproduce;
    private int reproductionCost;
    private int minMutations;
    private int maxMutations;


    public WorldMapBuilder setGlobeParameters(int width, int height, int plantsNum, int plantsEnergy) {
        this.width = width;
        this.height = height;
        this.plantsNum = plantsNum;
        this.plantsEnergy = plantsEnergy;
        return this;
    }

    public WorldMapBuilder setBoostGrowth(int boostGrowth) {
        this.boostGrowth = boostGrowth;
        return this;
    }


    public WorldMapBuilder setAnimalParameters(BehaviourVariant behaviourVariant, int animalStartNumber, int animalStartHealth, int animalGenomeLength) {
        this.behaviourVariant = behaviourVariant;
        this.animalStartNumber = animalStartNumber;
        this.animalStartHealth = animalStartHealth;
        this.animalGenomeLength = animalGenomeLength;
        return this;
    }

    public WorldMapBuilder setReproductionParameters(int healthToReproduce, int reproductionCost, int minMutations, int maxMutations) {
        this.healthToReproduce = healthToReproduce;
        this.reproductionCost = reproductionCost;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        return this;
    }


    public AbstractWorldMap build() {
        if (boostGrowth > 0) {
            return new GlobeMap(behaviourVariant, width, height, animalStartNumber, animalStartHealth, animalGenomeLength, plantsNum, plantsEnergy, healthToReproduce, reproductionCost, minMutations, maxMutations);
        } else {
            return new GlobeMap(behaviourVariant, width, height, animalStartNumber, animalStartHealth, animalGenomeLength, plantsNum, plantsEnergy, healthToReproduce, reproductionCost, minMutations, maxMutations);
        }
    }
}