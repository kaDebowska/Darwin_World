package agh.ics.oop.model;
import agh.ics.oop.model.util.RandomPositionGenerator;
import agh.ics.oop.presenter.BehaviourVariant;

import java.util.*;

import static agh.ics.oop.presenter.BehaviourVariant.NORMAL_ANIMAL;

public class GlobeMap extends AbstractWorldMap{
    private int plantsOnEquator;
    private int plantsOutsideEquator;
    private RandomPositionGenerator positionsOnEquator;
    private RandomPositionGenerator positionsOutsideEquator;
    private Boundary equatorBounds;

    public GlobeMap(int width, int height, int plantsNum){
        super(NORMAL_ANIMAL, width, height, 2, 50, 10, plantsNum, 100, 30, 15, 0, 10);
        this.grassClumps = new HashMap<>();
        this.equatorBounds = calculateEquator();
        putPlants();
    }

    public GlobeMap(BehaviourVariant behaviourVariant, int width, int height, int animalStartNumber, int animalStartHealth, int animalGenomeLength, int plantsNum, int platsEnergy, int healthToReproduce, int reproductionCost, int minMutations, int maxMutations){
        super(behaviourVariant, width, height, animalStartNumber, animalStartHealth, animalGenomeLength, plantsNum, platsEnergy, healthToReproduce, reproductionCost, minMutations, maxMutations);
        this.grassClumps = new HashMap<>();
        this.equatorBounds = calculateEquator();
    }


    public RandomPositionGenerator generateEquatorPositions(){
        int width = 0;
        int height = 0;
        // equator that contains only part of one row
        if (this.equatorBounds.topRight().getY() - this.equatorBounds.bottomLeft().getY() == 0){
            width = this.equatorBounds.topRight().getX();
            height = 0;
        }
        else{
            width = super.width;
            height = this.equatorBounds.topRight().getX() == super.width ?
                    this.equatorBounds.topRight().getY() - this.equatorBounds.bottomLeft().getY():
                    this.equatorBounds.topRight().getY() - this.equatorBounds.bottomLeft().getY() - 1;
        }

        int startX = this.equatorBounds.bottomLeft().getX();
        int startY = this.equatorBounds.bottomLeft().getY();
        RandomPositionGenerator randomPositionGenerator =
                new RandomPositionGenerator(
                        width + this.equatorBounds.bottomLeft().getX(),
                        height + this.equatorBounds.bottomLeft().getY(),
                        (width + this.equatorBounds.bottomLeft().getX() - startX +1 ) * (height + this.equatorBounds.bottomLeft().getY() - startY + 1),
                        startX,
                        startY);
        // add positions of fields that don't cover full row (eg. equator contains two rows and two fields)
        if (width != this.equatorBounds.topRight().getX()){
            RandomPositionGenerator additionalPositionGenerator =
                    new RandomPositionGenerator(
                            this.equatorBounds.topRight().getX(),
                            this.equatorBounds.topRight().getY(),
                            this.equatorBounds.topRight().getX() + 1,
                            0,
                            this.equatorBounds.topRight().getY());

            for (Vector2d additionalPosition : additionalPositionGenerator){
                randomPositionGenerator.addPosition(additionalPosition);
                randomPositionGenerator.setCounter(randomPositionGenerator.getCounter() + 1);
            }

        }
        int numberOfEquatorPositions = randomPositionGenerator.getPositions().size();
//        new plants division if equator is too little for 80% of all plants
        if (this.plantsOnEquator > numberOfEquatorPositions){
            this.plantsOnEquator = numberOfEquatorPositions;
            this.plantsOutsideEquator = this.plantsNum - this.plantsOnEquator;
        }

        return randomPositionGenerator;
    }

    public RandomPositionGenerator generatePositionsOutsideEquator(){
        RandomPositionGenerator positionsOutsideEquator = new RandomPositionGenerator(this.width, this.height, ((this.width + 1) * (this.height + 1)));
        RandomPositionGenerator positionsOnEquatorCopy = new RandomPositionGenerator(this.positionsOnEquator);

        for (Vector2d position : positionsOnEquatorCopy) {
            positionsOutsideEquator.getPositions().remove(position);
        }

        positionsOnEquator.setCounter(this.plantsOnEquator);
        positionsOutsideEquator.setCounter(this.plantsOutsideEquator);
        return positionsOutsideEquator;

    }


    @Override
    public void putPlants(){
        this.plantsOnEquator = (int) (0.8 * plantsNum);
        this.plantsOutsideEquator = plantsNum - plantsOnEquator;

        this.positionsOnEquator = generateEquatorPositions();
        this.positionsOutsideEquator = generatePositionsOutsideEquator();

        putPlantsOnEquator();
        putPlantsOutsideEquator();
    }

    public void putPlantsOnEquator(){
        for (Vector2d grassPosition : this.positionsOnEquator) {
            grassClumps.put(grassPosition, new Grass(grassPosition));
        }
        this.positionsOnEquator.setCounter(this.plantsOnEquator);
    }

    public void putPlantsOutsideEquator(){
        for (Vector2d grassPosition : this.positionsOutsideEquator) {
            grassClumps.put(grassPosition, new Grass(grassPosition));
        }
        positionsOutsideEquator.setCounter(this.plantsOutsideEquator);

    }


    public Boundary calculateEquator(){
        int width = this.width + 1;
        int height = this.height + 1;

        int globeSurface = width * height;
        int equatorSurface = (int) (0.2 * globeSurface);
        int equatorHeight =  (equatorSurface/width);
        equatorHeight = equatorHeight > height ? 1 : equatorHeight;
        int additionalArea = equatorSurface % width;
        int bottomLeftY = height/2 - equatorHeight/2;
        if(equatorHeight < 2){
            bottomLeftY = height % 2 == 1 ? height/2 - equatorHeight/2: height/2 - equatorHeight/2 - 1;
        }
        int upperRightY = additionalArea == 0 ? bottomLeftY + equatorHeight - 1 : bottomLeftY + equatorHeight;
        int upperRightX = additionalArea == 0 ? width - 1 : additionalArea - 1;
//      equator doesn't fit whole row
        if(equatorSurface < width){
            upperRightX = equatorSurface;
            upperRightY = bottomLeftY;
        }


        Vector2d bottomLeft = new Vector2d(0, bottomLeftY);
        Vector2d upperRight = new Vector2d(upperRightX, upperRightY);
        return new Boundary(bottomLeft, upperRight);

    }


}

