package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GrassField extends AbstractWorldMap {
    private final Map<Vector2d, Grass> grassClumps;


    public GrassField(int n) {
        this.grassClumps = new HashMap<>();
        int maxGrassRange = (int) Math.sqrt(n * 10);

        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(maxGrassRange, maxGrassRange, n);
        for (Vector2d grassPosition : randomPositionGenerator) {
            grassClumps.put(grassPosition, new Grass(grassPosition));
        }
    }


    @Override
    public boolean isOccupied(Vector2d position) {
        return super.isOccupied(position) || grassClumps.containsKey(position);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return super.isOccupied(position) ? super.objectAt(position) : grassClumps.get(position);
    }

    @Override
    public Boundary getCurrentBounds() {
        Vector2d bottomLeft = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Vector2d topRight = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);

        Set<Vector2d> positions = new HashSet<>();
        positions.addAll(animals.keySet());
        positions.addAll(grassClumps.keySet());

        for (Vector2d position : positions) {
            bottomLeft = bottomLeft.lowerLeft(position);
            topRight = topRight.upperRight(position);
        }
        return new Boundary(bottomLeft, topRight);
    }

}