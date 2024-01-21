package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarcassMap extends AbstractWorldMap{
    private Map<Vector2d, Integer> fertilePlaces;

    private RandomPositionGenerator positionsOutsideCarcass;

    private final int fertilenessTime;

    private int plantsAroundCarcass;

    private int plantsOutsideCarcass;

    public CarcassMap(int width, int height, int plantsNum, int startPlantsNum, int fertilenessTime, int plantsEnergy, int healthToReproduce, int reproductionCost, int minMutations, int maxMutations){
        super(width, height, plantsNum, plantsEnergy, healthToReproduce, reproductionCost, minMutations, maxMutations);
        this.fertilePlaces = new HashMap<>();
        this.fertilenessTime = fertilenessTime;

        this.plantsAroundCarcass = (int) (0.8 * startPlantsNum);
        this.plantsOutsideCarcass = startPlantsNum - plantsAroundCarcass;
        this.positionsOutsideCarcass = generatePositionsOutsideCarcass();
        updateAndRemoveExpiredFertileness();
        calculatePlantsToGrow(startPlantsNum);
        putPlantsOutsideCarcass();
    }

    public CarcassMap(int width, int height, int plantsNum, int fertilenessTime){
        super(width, height, plantsNum, fertilenessTime, 100, 30, 15, 0);
        this.fertilePlaces = new HashMap<>();
        this.fertilenessTime = fertilenessTime;
    }

    public Map<Vector2d, Integer> getFertilePlaces() {
        return fertilePlaces;
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
                this.fertilePlaces.put(positionAroundCarcass, this.fertilenessTime);
            }
        }

        return this.fertilePlaces;

    }

    public RandomPositionGenerator generatePositionsOutsideCarcass() {
        RandomPositionGenerator positionsOutsideCarcass = new RandomPositionGenerator(this.width, this.height, this.plantsOutsideCarcass);
        if (!this.fertilePlaces.isEmpty()) {
            RandomPositionGenerator positionsAroundCarcass = new RandomPositionGenerator(new ArrayList<>(fertilePlaces.keySet()), this.plantsAroundCarcass);
            for (Vector2d position : positionsAroundCarcass) {
                positionsOutsideCarcass.getPositions().remove(position);
            }
        }

        return positionsOutsideCarcass;

    }



    public void updateAndRemoveExpiredFertileness() {
        fertilePlaces.entrySet().removeIf(entry -> {
            entry.setValue(entry.getValue() - 1);
            Vector2d position = entry.getKey();

            if (entry.getValue() <= 0) {
                this.positionsOutsideCarcass.getPositions().add(position);
                return true;
            }

            return false;
        });
    }



    public void putPlantsAroundCarcass(int plantsNum){

        List<Vector2d> positionsList = new ArrayList<>(fertilePlaces.keySet());
        if (!positionsList.isEmpty()) {
            RandomPositionGenerator positionsAroundCarcass = new RandomPositionGenerator(positionsList, this.plantsAroundCarcass);

            for (Vector2d grassPosition : positionsAroundCarcass) {
                grassClumps.put(grassPosition, new Grass(grassPosition));
            }
            positionsAroundCarcass.setCounter(this.plantsAroundCarcass);
        }


    }
    public void calculatePlantsToGrow(int plantsNum) {
        //        new plants division if carcass positions is too little for 80% of all plants
        if (this.plantsAroundCarcass > fertilePlaces.size()) {
            this.plantsAroundCarcass = fertilePlaces.size();
            this.plantsOutsideCarcass = plantsNum - this.plantsAroundCarcass;
        }
    }

    public void putPlantsOutsideCarcass(){

        this.positionsOutsideCarcass.setCounter(this.plantsOutsideCarcass);
        for (Vector2d grassPosition : this.positionsOutsideCarcass) {
            grassClumps.put(grassPosition, new Grass(grassPosition));
        }
        this.positionsOutsideCarcass.setCounter(this.plantsOutsideCarcass);

    }


    public void putPlants(){
        this.plantsAroundCarcass = (int) (0.8 * plantsNum);
        this.plantsOutsideCarcass = plantsNum - plantsAroundCarcass;
        getPositionsAroundCarcass(super.positionsOfDeadAnimals);
        this.positionsOutsideCarcass = generatePositionsOutsideCarcass();
        updateAndRemoveExpiredFertileness();
        putPlantsAroundCarcass(plantsNum);
        calculatePlantsToGrow(plantsNum);
        putPlantsOutsideCarcass();
    }

}
