package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {

    protected Map<Vector2d, Animal> animals;

    private final Map<Vector2d, Grass> grassClumps;
    private List<MapChangeListener> listeners;
    private UUID uuid;

    private final int width;
    private final int height;

    public AbstractWorldMap(int width, int height) {
        this.animals = new HashMap<>();
        this.grassClumps = new HashMap<>();
        this.listeners = new ArrayList<>();
        this.uuid = UUID.randomUUID();
        this.width = width;
        this.height = height;

    }


    private boolean isWithinMap(Vector2d position) {
        return position.precedes(new Vector2d(this.width, this.height)) && position.follows(new Vector2d(0, 0));
    }


    public boolean canMoveTo(Vector2d position) {
        return isWithinMap(position);
    }

    public boolean isTopOrBottomMapEdge(Vector2d position){
        return position.getY() == this.height + 1 || position.getY() == - 1;
    }

    public boolean isLeftOrRightMapEdge(Vector2d position){
        return position.getX() == this.width + 1 || position.getX() == -1;
    }

    @Override
    public Boundary getCurrentBounds() {
        Vector2d bottomLeft = new Vector2d(0, 0);
        Vector2d topRight = new Vector2d(this.width, this.height);
        return new Boundary(bottomLeft, topRight);
    }

    @Override
    public boolean place(Animal animal) throws PositionAlreadyOccupiedException {
        Vector2d position = animal.getPosition();
        if (canMoveTo(position)) {
            animals.put(position, animal);
            String message = String.format("An animal has been placed at %s.", animal.getPosition());
            this.notifyListeners(message);
            return true;
        }
        throw new PositionAlreadyOccupiedException(animal.getPosition());
    }

    @Override
    public void move(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        int direction = animal.getNextGene();
        animal.move(this, direction);
        Vector2d newPosition = animal.getPosition();
        if (!oldPosition.equals(newPosition)) {
            animals.remove(oldPosition);
            animals.put(newPosition, animal);
        }
        String message = String.format("An animal has been moved from %s to %s.", oldPosition, newPosition);
        this.notifyListeners(message);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return animals.get(position);
    }

    @Override
    public String toString() {
        Boundary boundary = this.getCurrentBounds();
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        return mapVisualizer.draw(boundary.bottomLeft(), boundary.topRight());
    }

    public void subscribe(MapChangeListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(MapChangeListener listener) {
        listeners.remove(listener);
    }

    //    different name
    private void notifyListeners(String message) {
        if (this.listeners != null) {
            for (MapChangeListener listener : listeners) {
                listener.onMapChange(this, message);
            }
        }
    }

    @Override
    public UUID getId() {
        return  this.uuid;
    }
}
