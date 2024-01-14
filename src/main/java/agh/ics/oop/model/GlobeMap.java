package agh.ics.oop.model;

//import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.*;

import static java.lang.Math.abs;

public class GlobeMap extends AbstractWorldMap{

    private final int width;
    private final int height;
    private final Map<Vector2d, Grass> grassClumps;
    private final int plantsNum;
    private Boundary equatorBounds;

    public GlobeMap(int width, int height, int plantsNum){
        this.width = width;
        this.height = height;
        this.grassClumps = new HashMap<>();
        this.plantsNum = plantsNum;
        this.equatorBounds = calculateEquator();
        putPlants();
    }

    public void putPlants(){
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(this.width, this.height, this.plantsNum);
        for (Vector2d grassPosition : randomPositionGenerator) {
            grassClumps.put(grassPosition, new Grass(grassPosition));
        }
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return super.isOccupied(position) ? super.objectAt(position) : grassClumps.get(position);
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
            newPosition = new Vector2d(abs(newPosition.getX() - this.width) - 1, abs(newPosition.getY() - 1));
            animal.setPosition(newPosition);
            animal.setOrientation(reflect(animal.getOrientation()));
        }
        else if (this.isLeftOrRightMapEdge(newPosition)){
            newPosition = new Vector2d((abs(newPosition.getX() - this.width) - 1), newPosition.getY());
            animal.setPosition(newPosition);
        }
        else if (this.isTopOrBottomMapEdge(newPosition)){
            newPosition = new Vector2d(newPosition.getX(), abs(newPosition.getY()) - 1);
            animal.setPosition(newPosition);
            animal.setOrientation(reflect(animal.getOrientation()));
        }

        if (!oldPosition.equals(newPosition)) {
            animals.remove(oldPosition);
            animals.put(newPosition, animal);
        }

        String message = String.format("An animal has been moved from %s to %s.", oldPosition, newPosition);
        this.notifyListeners(message);
    }




    public Boundary calculateEquator(){
        int globeSurface = this.width * this.height;
        int equatorSurface = (int) (0.2 * globeSurface);
        int equatorHeight =  (equatorSurface/width);
        equatorHeight = equatorHeight > height ? 1 : equatorHeight;
        int additionalArea = equatorSurface % width;
        int bottomLeftY = height/2 - equatorHeight/2;
        if(equatorHeight < 2){
            bottomLeftY = height % 2 == 1 ? height/2 - equatorHeight/2: height/2 - equatorHeight/2 - 1;
        }
//        int bottomLeftY = height % 2 == 1 ? height/2 - equatorHeight/2: height/2 - equatorHeight/2 - 1;
        int upperRightY = additionalArea == 0 ? bottomLeftY + equatorHeight : bottomLeftY + equatorHeight + 1;
        int upperRightX = additionalArea == 0 ? width : additionalArea;
        if(equatorSurface < width){
            upperRightX = equatorSurface;
            upperRightY = bottomLeftY + 1;
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
    public Boundary getCurrentBounds() {
        Vector2d bottomLeft = new Vector2d(0, 0);
        Vector2d topRight = new Vector2d(this.width, this.height);
        return new Boundary(bottomLeft, topRight);
    }
}

