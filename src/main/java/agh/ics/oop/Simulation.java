package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.RandomPositionGenerator;
import agh.ics.oop.presenter.BehaviourVariant;

public class Simulation implements Runnable {

    private AbstractWorldMap map;
    private volatile boolean running = true;

    public Simulation(AbstractWorldMap map) {
        this.map = map;
        populateMap();
    }

    public void populateMap() {
        Boundary boundary = map.getCurrentBounds();
        int width = boundary.topRight().getX() - boundary.bottomLeft().getX();
        int height = boundary.topRight().getY() - boundary.bottomLeft().getY();
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(width, height, map.getAnimalStartNumber());
        BehaviourVariant behaviourVariant = map.getBehaviourVariant();
        int initialHealth = map.getAnimalStartHealth();
        int genomeLength = map.getAnimalGenomeLength();
        for (Vector2d animalPosition : randomPositionGenerator) {
            Animal animal = behaviourVariant == BehaviourVariant.NORMAL_ANIMAL
                    ? new Animal(animalPosition, initialHealth, genomeLength)
                    : new CrazyAnimal(animalPosition, initialHealth, genomeLength);
            map.place(animal);
        }
    }

    public void pause() {
        this.running = false;
    }

    public void resume() {
        this.running = true;
    }

    @Override
    public void run() {
//        for (Animal animal : this.listOfAnimals) {
//                map.place(animal);
//        }
        while (running) {
            try {
                map.removeDeadAnimals();
                map.moveAnimals();
                map.handleEating();
                map.handleReproduction();
                map.putPlants();
                map.stepCounters();
                map.notifyListeners("Number of animals: " + String.valueOf(map.getAnimals().size()));

                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
