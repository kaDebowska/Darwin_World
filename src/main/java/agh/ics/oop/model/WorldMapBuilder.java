package agh.ics.oop.model;

import agh.ics.oop.presenter.BehaviourVariant;

import java.util.List;
import java.util.Map;

public class WorldMapBuilder {
    private int width;
    private int height;
    private int plantsEnergy;
    private BehaviourVariant behaviourVariant;
    private int animalStartNumber;
    private int animalStartHealth;
    private int animalGenomeLength;
    private int startPlantsNum;
    private int everDayPlantsNum;
    private int healthToReproduce;
    private int reproductionCost;
    private int minMutations;
    private int maxMutations;
    private int daysOfFertility;


    public WorldMapBuilder setGlobeParameters(int width, int height, int startPlantsNum, int everDayPlantsNum, int plantsEnergy) {
        this.width = width;
        this.height = height;
        this.startPlantsNum = startPlantsNum;
        this.everDayPlantsNum = everDayPlantsNum;
        this.plantsEnergy = plantsEnergy;
        return this;
    }

    public WorldMapBuilder setDaysOfFertility(int daysOfFertility) {
        this.daysOfFertility = daysOfFertility;
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
        if (daysOfFertility > 0) {
            return new CarcassMap(behaviourVariant, width, height, animalStartNumber, animalStartHealth, animalGenomeLength, startPlantsNum, everDayPlantsNum, plantsEnergy, daysOfFertility, healthToReproduce, reproductionCost, minMutations, maxMutations);
        } else {
            return new GlobeMap(behaviourVariant, width, height, animalStartNumber, animalStartHealth, animalGenomeLength, startPlantsNum, everDayPlantsNum, plantsEnergy, healthToReproduce, reproductionCost, minMutations, maxMutations);
        }
    }
}
