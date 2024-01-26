package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomPositionGenerator;
import agh.ics.oop.presenter.BehaviourVariant;

import java.util.*;

public class CarcassMap extends AbstractWorldMap{
    private Map<Vector2d, Integer> allFertilePositions;

    private RandomPositionGenerator positionsOutsideCarcass;

    private final int fertilenessTime;

    private int plantsAroundCarcass;

    private int plantsOutsideCarcass;

    public CarcassMap(BehaviourVariant behaviourVariant, int width, int height, int animalStartNumber, int animalStartHealth, int animalGenomeLength, int startPlantsNum, int everDayPlantsNum, int plantsEnergy, int fertilenessTime, int healthToReproduce, int reproductionCost, int minMutations, int maxMutations){
        super(behaviourVariant, width, height, animalStartNumber, animalStartHealth, animalGenomeLength, startPlantsNum, everDayPlantsNum, plantsEnergy, healthToReproduce, reproductionCost, minMutations, maxMutations);
        this.allFertilePositions = new HashMap<>();
        this.fertilenessTime = fertilenessTime;

        this.plantsAroundCarcass = (int) (0.8 * startPlantsNum);
        this.plantsOutsideCarcass = startPlantsNum - plantsAroundCarcass;
        this.positionsOutsideCarcass = generatePositionsOutsideCarcass(startPlantsNum);
        updateAndRemoveExpiredFertileness();
        putPlantsOutsideCarcass(startPlantsNum);
    }


    public Map<Vector2d, Integer> getAllFertilePositions() {
        return allFertilePositions;
    }


    public Map<Vector2d, Integer> getPositionsAroundCarcass(List<Vector2d> positionsOfDeadAnimals) {

        for (Vector2d position : positionsOfDeadAnimals) {
            int maxX = position.getX() + 1;
            int maxY = position.getY() + 1;
            int startX = position.getX() - 1;
            int startY = position.getY() - 1;
            int reflectedX = -1;

            if (position.getY() == 0) {
                startY = position.getY();
            } else if (position.getY() == super.height) {
                maxY = position.getY();
            }

            if (position.getX() == 0) {
                startX = position.getX();
                reflectedX = super.width;
            } else if (position.getX() == super.width) {
                maxX = position.getX();
                reflectedX = 0;
            }

            if ((position.getY() == 0 && position.getX() == 0) || (position.getY() == super.height && position.getX() == 0)) {
                reflectedX = super.width;
            } else if ((position.getY() == 0 && position.getX() == super.width) || (position.getY() == super.height && position.getX() == super.width)) {
                reflectedX = 0;
            }
            RandomPositionGenerator positionsAroundCarcass = new RandomPositionGenerator(
                    maxX,
                    maxY,
                    0,
                    startX,
                    startY);
            positionsAroundCarcass.setCounter(positionsAroundCarcass.getPositions().size());
            if(reflectedX >= 0){
                RandomPositionGenerator reflectedPositionsAroundCarcass = new RandomPositionGenerator(
                        reflectedX,
                        maxY,
                        0,
                        reflectedX,
                        startY);
                reflectedPositionsAroundCarcass.setCounter(reflectedPositionsAroundCarcass.getPositions().size());
                for(Vector2d reflectedPosition : reflectedPositionsAroundCarcass){
                    positionsAroundCarcass.addPosition(reflectedPosition);
                    positionsAroundCarcass.setCounter(positionsAroundCarcass.getCounter() + 1);
                }
            }


            for(Vector2d positionAroundCarcass : positionsAroundCarcass){
                this.allFertilePositions.put(positionAroundCarcass, this.fertilenessTime);

            }
            this.fertilePositions = new ArrayList<>(this.allFertilePositions.keySet());

        }

        return this.allFertilePositions;

    }

    public RandomPositionGenerator generatePositionsOutsideCarcass(int plantsNum) {
        calculatePlantsToGrow(plantsNum);
        RandomPositionGenerator positionsOutsideCarcass = new RandomPositionGenerator(this.width, this.height, this.plantsOutsideCarcass);
        if (!this.allFertilePositions.isEmpty()) {
            RandomPositionGenerator positionsAroundCarcass = new RandomPositionGenerator(new ArrayList<>(allFertilePositions.keySet()), this.plantsAroundCarcass);
//            removing positions that are carcass
            for (Vector2d position : positionsAroundCarcass) {
                positionsOutsideCarcass.getPositions().remove(position);
            }
        }
//        removing positions that contains grass already
        for (Vector2d grassPosition : grassClumps.keySet()) {
            positionsOutsideCarcass.getPositions().remove(grassPosition);
        }

        return positionsOutsideCarcass;

    }



    public void updateAndRemoveExpiredFertileness() {
        allFertilePositions.entrySet().removeIf(entry -> {
            entry.setValue(entry.getValue() - 1);
            Vector2d position = entry.getKey();

            if (entry.getValue() <= 0) {
                this.positionsOutsideCarcass.getPositions().add(position);
                this.fertilePositions.remove(position);
                return true;
            }

            return false;
        });
    }



    public void putPlantsAroundCarcass(int plantsNum){
        calculatePlantsToGrow(plantsNum);
        List<Vector2d> positionsList = new ArrayList<>(allFertilePositions.keySet());
        if (!positionsList.isEmpty()) {
            RandomPositionGenerator positionsAroundCarcass = new RandomPositionGenerator(positionsList, this.plantsAroundCarcass);

            for (Vector2d grassPosition : positionsAroundCarcass) {
//                grass is already in this position
                if (this.grassClumps.containsKey(grassPosition)) {
                    positionsAroundCarcass.setCounter(positionsAroundCarcass.getCounter() + 1);
                } else {
                    grassClumps.put(grassPosition, new Grass(grassPosition));
                }
            }
            positionsAroundCarcass.setCounter(this.plantsAroundCarcass);
        }


    }
    public void calculatePlantsToGrow(int plantsNum) {
//        new plants division if carcass positions is too little for 80% of all plants
        if (this.plantsAroundCarcass > allFertilePositions.size()) {
            this.plantsAroundCarcass = allFertilePositions.size();
            this.plantsOutsideCarcass = plantsNum - this.plantsAroundCarcass;
        }
//      new plants division if there is not enough space for plants to put (8 empty places in total -> 10 plants to put)
        if (this.positionsOutsideCarcass != null && this.plantsOutsideCarcass > positionsOutsideCarcass.getPositions().size()) {
            this.plantsOutsideCarcass = positionsOutsideCarcass.getPositions().size();
        }
    }

    public void putPlantsOutsideCarcass(int plantsNum){
        calculatePlantsToGrow(plantsNum);
        this.positionsOutsideCarcass.setCounter(this.plantsOutsideCarcass);

        if (this.positionsOutsideCarcass.getPositions().isEmpty()) {
            return;
        }
        for (Vector2d grassPosition : this.positionsOutsideCarcass) {
            grassClumps.put(grassPosition, new Grass(grassPosition));
        }
        this.positionsOutsideCarcass.setCounter(this.plantsOutsideCarcass);

    }


    public void putPlants(){
        this.plantsAroundCarcass = (int) (0.8 * everDayPlantsNum);
        this.plantsOutsideCarcass = everDayPlantsNum - plantsAroundCarcass;
        getPositionsAroundCarcass(super.positionsOfDeadAnimals);
        this.positionsOutsideCarcass = generatePositionsOutsideCarcass(everDayPlantsNum);
        updateAndRemoveExpiredFertileness();
        putPlantsAroundCarcass(everDayPlantsNum);
        putPlantsOutsideCarcass(everDayPlantsNum);
    }

}
