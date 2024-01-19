package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractWorldMap implements WorldMap {

    protected Map<Animal, Vector2d> animals;
    private List<MapChangeListener> listeners;
    private UUID uuid;

    protected final int width;
    protected final int height;

    private Map<Vector2d, Grass> grassClumps;
    private final int startPlantsNum;

    private final int plantsGrowingEveryDayNum;

    public AbstractWorldMap(int width, int height, int startPlantsNum, int plantsGrowingEveryDayNum) {
        this.animals = new HashMap<>();
        this.listeners = new ArrayList<>();
        this.uuid = UUID.randomUUID();
        this.width = width;
        this.height = height;
        this.startPlantsNum = startPlantsNum;
        this.plantsGrowingEveryDayNum = plantsGrowingEveryDayNum;

    }

    public List<Animal> getAnimalsAt(Vector2d position) {
        return animals.entrySet().stream()
                .filter(entry -> entry.getValue().equals(position))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }


    @Override
    public boolean place(Animal animal) {
        Vector2d position = animal.getPosition();
        animals.put(animal, position);
        String message = String.format("An animal has been placed at %s.", position);
        this.notifyListeners(message);
        return true;
    }

    @Override
    public void move(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        int direction = animal.getNextGene();
        animal.move(this, direction);
        Vector2d newPosition = animal.getPosition();
        if (!oldPosition.equals(newPosition)) {
            animals.put(animal, newPosition);
        }
        String message = String.format("An animal has been moved from %s to %s.", oldPosition, newPosition);
        this.notifyListeners(message);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.containsValue(position);
    }


    @Override
    public WorldElement objectAt(Vector2d position) {
        List<Animal> animalsAtPosition = animals.entrySet().stream()
                .filter(entry -> entry.getValue().equals(position))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if (!animalsAtPosition.isEmpty()) {
            if (animalsAtPosition.size() == 1) {
                return animalsAtPosition.get(0);
            } else {
                return new AnimalGroup(animalsAtPosition);
            }
        } else {
            return null;
        }
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
    void notifyListeners(String message) {
        if (this.listeners != null) {
            for (MapChangeListener listener : listeners) {
                listener.onMapChange(this, message);
            }
        }
    }

    @Override
    public List<Animal> getOrderedAnimals(List<Animal> animalList) {
        return animalList.stream()
                .sorted(Comparator.comparing(Animal::getHealth)
                        .thenComparing(Animal::getAge)
                        .thenComparing(Animal::getKidsNumber))
                .collect(Collectors.toList());
    }

    @Override
    public UUID getId() {
        return  this.uuid;
    }
}