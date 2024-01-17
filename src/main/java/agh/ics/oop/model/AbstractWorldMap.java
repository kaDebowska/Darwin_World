package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractWorldMap implements WorldMap {
    protected Map<Vector2d, AnimalGroup> animals;

    private final int healthToReproduce;
    protected Map<Vector2d, Grass> grassClumps;
    private final int plantsEnergy;
    protected final int width;
    protected final int height;
    protected final int plantsNum;
    private List<MapChangeListener> listeners;
    private UUID uuid;

    public AbstractWorldMap(int width, int height, int plantsNum, int plantsEnergy, int healthToReproduce) {
        this.width = width;
        this.height = height;
        this.plantsNum = plantsNum;
        this.plantsEnergy = plantsEnergy;
        this.healthToReproduce = healthToReproduce;
        this.animals = new HashMap<>();
        this.listeners = new ArrayList<>();
        this.uuid = UUID.randomUUID();
    }


    public List<Animal> getAnimalsAt(Vector2d position) {
        return animals.get(position).getAnimals();
    }


    @Override
    public boolean place(Animal animal) {
        Vector2d position = animal.getPosition();
        if (animals.containsKey(position)) {
            animals.get(position).addAnimal(animal);
        } else {
            AnimalGroup newList = new AnimalGroup(animal);
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
        animal.move(direction);
        Vector2d newPosition = animal.getPosition();
        if (!oldPosition.equals(newPosition)) {
            animals.get(oldPosition).removeAnimal(animal);
            if (animals.get(oldPosition).getAnimals().isEmpty()) {
                animals.remove(oldPosition);
            }
            if (animals.containsKey(newPosition)) {
                animals.get(newPosition).addAnimal(animal);
            } else {
                AnimalGroup newList = new AnimalGroup(animal);
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
        List<Animal> animalsAtPosition = animals.get(position).getAnimals();
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

    public void handleEating() {
        for (Vector2d position : animals.keySet()) {
            if (grassClumps.containsKey(position)) {
                Animal animalToEat = animals.get(position).getOrderedAnimals().get(0);
                this.eat(animalToEat, animalToEat.getPosition());
            }
        }
    }

    private void eat(Animal animal, Vector2d position) {
        animal.restoreHealth(this.plantsEnergy);
        animal.countEaten();
        grassClumps.remove(position);
    }

    public void handleReproducing() {
        for (Vector2d position : animals.keySet()) {
            if (animals.get(position).isProlific()) {
                List<Animal> sortedAnimals = animals.get(position).getOrderedAnimals();
                Animal animalAlpha = sortedAnimals.get(0);
                Animal animalBeta = sortedAnimals.get(1);
                if (animalAlpha.getHealth() >= this.healthToReproduce && animalBeta.getHealth() >= this.healthToReproduce) {
//                    this.reproduce(animalAlpha,animalBeta);
                }
            }
        }
    }

    @Override
    public UUID getId() {
        return  this.uuid;
    }

    @Override
    public Boundary getCurrentBounds() {
        Vector2d bottomLeft = new Vector2d(0, 0);
        Vector2d topRight = new Vector2d(this.width, this.height);
        return new Boundary(bottomLeft, topRight);
    }

}