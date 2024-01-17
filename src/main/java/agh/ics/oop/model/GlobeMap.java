package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.*;

public class GlobeMap extends AbstractWorldMap{
    private final Map<Vector2d, Grass> grassClumps;

    private final int plantsNum;

    private Boundary equatorBounds;
    public GlobeMap(int width, int height, int plantsNum){
        super(width, height);
        this.grassClumps = new HashMap<>();
        this.plantsNum = plantsNum;
        this.equatorBounds = calculateEquator();
        putPlants();
    }

    public void putPlants(){
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(super.width, super.height, this.plantsNum);
        for (Vector2d grassPosition : randomPositionGenerator) {
            grassClumps.put(grassPosition, new Grass(grassPosition));
        }
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

