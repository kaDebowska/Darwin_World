package agh.ics.oop.model;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;

public class Animal implements WorldElement {

    private int orientation;
    private Vector2d position;
    private List<Integer> genome;
    private Iterator<Integer> genomeIterator;

    private WorldMap map;


    public Animal() {
        this(new Vector2d(2, 2));
    }

    public Animal(Vector2d position) {
        this.orientation = new Random().nextInt(8);
        this.position = position;
        this.genome = List.of(6,6,6,2,1,3,7);
        this.genomeIterator = genome.iterator();
    }

    public Animal(WorldMap map, Vector2d position, int orientation){
        this(map, position);
        this.orientation = orientation;
    }

    public Animal(WorldMap map, Vector2d position){
        this(position);
        this.map = map;
    }



    private void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    private void setPosition(Vector2d position) {
        this.position = position;
    }

//    public MapDirection getOrientation() {
//        return orientation;
//    }

    public Vector2d getPosition() {
        return position;
    }

    public int getOrientation() {
        return orientation;
    }

    public List<Integer> getGenome() {
        return this.genome;
    }

    public int getNextGene() {
        if (!genomeIterator.hasNext()) {
            genomeIterator = genome.iterator();
        }
        return genomeIterator.next();
    }

    @Override
    public String toString() {
        return String.valueOf(this.orientation);
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public Vector2d toUnitVector(int orientation) {
        return getVector2d(orientation);
    }

    static Vector2d getVector2d(int orientation) {
        return switch (orientation) {
            case 0 -> new Vector2d(0, 1);
            case 1 -> new Vector2d(1, 1);
            case 2 -> new Vector2d(1, 0);
            case 3 -> new Vector2d(1, -1);
            case 4 -> new Vector2d(0, -1);
            case 5 -> new Vector2d(-1, -1);
            case 6 -> new Vector2d(-1, 0);
            case 7 -> new Vector2d(-1, 1);
            default -> throw new IllegalArgumentException("Invalid orientation");
        };
    }


    public void move(AbstractWorldMap abstractWorldMap, int direction) {
        Vector2d newPosition;
        this.setOrientation((this.orientation + direction) % 8);
        newPosition = this.position.add(this.toUnitVector(this.orientation));
        if (abstractWorldMap.canMoveTo(newPosition))
            this.setPosition(newPosition);
        else if (abstractWorldMap.isTopOrBottomMapEdge(newPosition) & abstractWorldMap.isLeftOrRightMapEdge(newPosition)){
            newPosition = new Vector2d((abs(newPosition.getX() - map.getCurrentBounds().topRight().getX()) - 1), (abs(newPosition.getY()) - 1));
            this.setPosition(newPosition);
            this.setOrientation((this.orientation + 4) % 8);
        }
        else if (abstractWorldMap.isLeftOrRightMapEdge(newPosition)){
            newPosition = new Vector2d((abs(newPosition.getX() - map.getCurrentBounds().topRight().getX()) - 1), newPosition.getY());
            this.setPosition(newPosition);
        }
        else if (abstractWorldMap.isTopOrBottomMapEdge(newPosition)){
            this.setOrientation((this.orientation + 4) % 8);
        }
    }
}
