package agh.ics.oop.model;

//import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.*;

import static java.lang.Math.abs;

public class GlobeMap extends AbstractWorldMap{

    private final int width;
    private final int height;
    private Map<Vector2d, Grass> grassClumps;
    private final int plantsNum;


    private int plantsOnEquator;

    private int plantsOutsideEquator;

    private RandomPositionGenerator positionsOnEquator;

    private RandomPositionGenerator positionsOutsideEquator;

    private Boundary equatorBounds;

    public GlobeMap(int width, int height, int plantsNum){
        this.width = width;
        this.height = height;
        this.grassClumps = new HashMap<>();
        this.plantsNum = plantsNum;
        this.plantsOnEquator = (int) (0.8 * plantsNum);
        this.plantsOutsideEquator = plantsNum - plantsOnEquator;
        this.equatorBounds = calculateEquator();
        this.positionsOnEquator = generateEquatorPositions();
        this.positionsOutsideEquator = generatePositionsOutsideEquator();
        putPlantsOnEquator();
        putPlantsOutsideEquator();

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
            width = this.width;
            height = this.equatorBounds.topRight().getX() == this.width ?
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


    public boolean isTopOrBottomMapEdge(Vector2d position){
        return position.getY() == this.height + 1 || position.getY() == - 1;
    }

    public boolean isLeftOrRightMapEdge(Vector2d position){
        return position.getX() == this.width + 1 || position.getX() == -1;
    }

    public int reflect(int orientation) {
        return (12 - orientation) % 8;
    }


    @Override
    public void move(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        int direction = animal.getNextGene();
        animal.move(this, direction);
        Vector2d newPosition = animal.getPosition();

        if (this.isTopOrBottomMapEdge(newPosition) & this.isLeftOrRightMapEdge(newPosition)){
            newPosition = new Vector2d((abs(newPosition.getX() - this.width) - 1), (abs(newPosition.getY()) - 1));
            animal.setOrientation(reflect(animal.getOrientation()));
            animal.setPosition(newPosition);
        }
        else if (this.isLeftOrRightMapEdge(newPosition)){
            newPosition = new Vector2d((abs(newPosition.getX() - this.width) - 1), newPosition.getY());
            animal.setPosition(newPosition);
        }
        else if (this.isTopOrBottomMapEdge(newPosition)){
            animal.setOrientation(reflect(animal.getOrientation()));
//            animal.move(this, 0);
        }

        if (!oldPosition.equals(newPosition)) {
            animals.remove(oldPosition);
            animals.put(newPosition, animal);
            if(grassClumps.containsKey(newPosition)) {
                grassClumps.remove(newPosition);
            }
        }

        String message = String.format("An animal has been moved from %s to %s.", oldPosition, newPosition);
        this.notifyListeners(message);
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

    @Override
    public boolean canMoveTo(Vector2d position) {
        return true;
    }


    @Override
    public WorldElement objectAt(Vector2d position) {
        return super.isOccupied(position) ? super.objectAt(position) : grassClumps.get(position);
    }


    @Override
    public boolean isOccupied(Vector2d position) {
        return super.isOccupied(position) || grassClumps.containsKey(position);
    }

    @Override
    public Boundary getCurrentBounds() {
        Vector2d bottomLeft = new Vector2d(0, 0);
        Vector2d topRight = new Vector2d(this.width, this.height);
        return new Boundary(bottomLeft, topRight);
    }
}

