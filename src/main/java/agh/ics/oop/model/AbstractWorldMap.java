package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public abstract class AbstractWorldMap implements WorldMap {
    protected Map<Vector2d, AnimalGroup> animals;
    private final int animalStartNumber;
    private final int healthToReproduce;
    private final int reproductionCost;
    protected Map<Vector2d, Grass> grassClumps;
    private final int plantsEnergy;
    protected final int width;
    protected final int height;
    protected final int plantsNum;
    private List<MapChangeListener> listeners;
    private int day = 0;
    private int minMutations;
    private int maxMutations;

    public AbstractWorldMap(int width, int height, int animalStartNumber, int plantsNum, int plantsEnergy, int healthToReproduce, int reproductionCost, int minMutations, int maxMutations) {
        this.width = width;
        this.height = height;
        this.animalStartNumber = animalStartNumber;
        this.plantsNum = plantsNum;
        this.plantsEnergy = plantsEnergy;
        this.healthToReproduce = healthToReproduce;
        this.reproductionCost = reproductionCost;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.animals = new HashMap<>();
        this.listeners = new ArrayList<>();
    }


    @Override
    public void place(Animal animal) {
        Vector2d position = animal.getPosition();
        if (animals.containsKey(position)) {
            animals.get(position).addAnimal(animal);
        } else {
            AnimalGroup newList = new AnimalGroup(animal);
            animals.put(position, newList);
        }
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
        animal.move(direction);
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
            AnimalGroup oldGroup = animals.get(oldPosition);
            oldGroup.removeAnimal(animal);
            if (oldGroup.isEmpty()) {
                animals.remove(oldPosition);
            }
            AnimalGroup newGroup = animals.get(newPosition);
            if (newGroup == null) {
                newGroup = new AnimalGroup(animal);
                animals.put(newPosition, newGroup);
            } else {
                newGroup.addAnimal(animal);
            }
        }
    }

    public void moveAnimals() {
        for (Animal animal : getAnimals()) {
            move(animal);
        }
    }


    @Override
    public WorldElement objectAt(Vector2d position) {
        AnimalGroup animalGroupAtPosition = animals.get(position);
        if (animalGroupAtPosition != null) {
            return animalGroupAtPosition;
        }
        return grassClumps.get(position);
    }

    public void subscribe(MapChangeListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(MapChangeListener listener) {
        listeners.remove(listener);
    }

    //    different name
    public void notifyListeners(String message) {
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

    public void handleReproduction() {
        for (Vector2d position : animals.keySet()) {
            if (animals.get(position).isProlific()) {
                List<Animal> sortedAnimals = animals.get(position).getOrderedAnimals();
                Animal animalAlpha = sortedAnimals.get(0);
                Animal animalBeta = sortedAnimals.get(1);
                if (animalAlpha.getHealth() >= healthToReproduce && animalBeta.getHealth() >= healthToReproduce) {
                    this.reproduce(animalAlpha,animalBeta);
                }
            }
        }
    }

    private void reproduce(Animal animalAlpha, Animal animalBeta) {
        animalAlpha.setHealth(animalAlpha.getHealth()-this.reproductionCost);
        animalBeta.setHealth(animalBeta.getHealth()-this.reproductionCost);

//        Caluclate the share of the alpha genome
        int genomeLength = animalAlpha.getGenome().size();
        int alphaShare = (int) Math.round((double) animalAlpha.getHealth() / (animalAlpha.getHealth() + animalBeta.getHealth()) * genomeLength);
        int betaShare = genomeLength - alphaShare;

        List<Integer> babyGenome = new ArrayList<>();

//        Get right or left side of alpha parent
        Random rand = new Random();
        if (rand.nextBoolean()) {
            babyGenome.addAll(animalAlpha.getGenome().subList(0, alphaShare));
            babyGenome.addAll(animalBeta.getGenome().subList(genomeLength - betaShare, genomeLength));
        } else {
            babyGenome.addAll(animalAlpha.getGenome().subList(genomeLength - alphaShare, genomeLength));
            babyGenome.addAll(animalBeta.getGenome().subList(0, betaShare));
        }

//        Mutate genome
        int numMutations = rand.nextInt((Math.min(maxMutations, babyGenome.size()) - minMutations) + 1) + minMutations;

        for (int i = 0; i < numMutations; i++) {
            int mutationIndex = rand.nextInt(babyGenome.size());
            int newGene = rand.nextInt(8);
            babyGenome.set(mutationIndex, newGene);
        }


        Animal babyAnimal = new Animal(animalAlpha.getPosition(), this.reproductionCost*2, babyGenome);
        animalAlpha.addChildren(babyAnimal);
        animalBeta.addChildren(babyAnimal);
        this.place(babyAnimal);
    }


    public void stepCounters() {
        for (Animal animal : getAnimals()) {
            animal.dailyFatigue();
        }
        this.day++;
    }

    public void removeDeadAnimals() {
        List<Vector2d> emptyPositions = new ArrayList<>();

        for (Map.Entry<Vector2d, AnimalGroup> entry : animals.entrySet()) {
            Vector2d position = entry.getKey();
            AnimalGroup group = entry.getValue();
            List<Animal> deadAnimals = group.getAnimals().stream()
                    .filter(animal -> animal.getHealth() <= 0)
                    .collect(Collectors.toList());
            for (Animal deadAnimal : deadAnimals) {
                group.removeAnimal(deadAnimal);
            }
            if (group.isEmpty()) {
                emptyPositions.add(position);
            }
        }

        for (Vector2d emptyPosition : emptyPositions) {
            animals.remove(emptyPosition);
        }
    }


    public List<Animal> getAnimals() {
        List<Animal> animalList = new ArrayList<>();
        for (AnimalGroup animalGroup : this.animals.values()) {
            animalList.addAll(animalGroup.getAnimals());
        }
        return animalList;
    }

    public int getAnimalStartNumber() {
        return animalStartNumber;
    }

    @Override
    public Boundary getCurrentBounds() {
        Vector2d bottomLeft = new Vector2d(0, 0);
        Vector2d topRight = new Vector2d(this.width, this.height);
        return new Boundary(bottomLeft, topRight);
    }

}