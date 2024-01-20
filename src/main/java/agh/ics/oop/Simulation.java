package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {

    private WorldMap map;
    private volatile boolean running = true;

    public Simulation(WorldMap map) {
        this.map = map;
        populateMap();
    }

    public void populateMap() {
        Boundary boundary = map.getCurrentBounds();
        int width = boundary.topRight().getX() - boundary.bottomLeft().getX();
        int height = boundary.topRight().getY() - boundary.bottomLeft().getY();
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(width, height, map.getAnimalStartNumber());
        for (Vector2d animalPosition : randomPositionGenerator) {
            map.place(new CrazyAnimal(animalPosition, 50, 10));
        }
    }

    public void pause() {
        running = false;
    }

    public void resume() {
        running = true;
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
                map.notifyListeners(String.valueOf(map.getAnimals().size()));

                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
