package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractWorldMap implements WorldMap {

    protected Map<Vector2d, List<Animal>> animals;
    private List<MapChangeListener> listeners;
    private UUID uuid;

    public AbstractWorldMap() {
        this.animals = new HashMap<>();
        this.listeners = new ArrayList<>();
        this.uuid = UUID.randomUUID();
    }

    public List<Animal> getAnimalsAt(Vector2d position) {
        return animals.get(position);
    }


    @Override
    public boolean place(Animal animal) {
        Vector2d position = animal.getPosition();
        if (animals.containsKey(position)) {
            animals.get(position).add(animal);
        } else {
            List<Animal> newList = new ArrayList<>();
            newList.add(animal);
            animals.put(position, newList);
        }
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
            animals.get(oldPosition).remove(animal);
            if (animals.get(oldPosition).isEmpty()) {
                animals.remove(oldPosition);
            }
            if (animals.containsKey(newPosition)) {
                animals.get(newPosition).add(animal);
            } else {
                List<Animal> newList = new ArrayList<>();
                newList.add(animal);
                animals.put(newPosition, newList);
            }
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
        List<Animal> animalsAtPosition = animals.get(position);
        if (animalsAtPosition != null && !animalsAtPosition.isEmpty()) {
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