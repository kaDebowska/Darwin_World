package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {

    private List<Animal> listOfAnimals;
    private WorldMap map;
    private volatile boolean running = true;

    public Simulation(List<Vector2d> positions, WorldMap map) {
        this.listOfAnimals = new ArrayList<>();
        for (Vector2d position : positions) {
            this.listOfAnimals.add(new Animal(position, 50, 10));
        }
        this.map = map;
    }

    public void pause() {
        running = false;
    }

    public void resume() {
        running = true;
    }

    @Override
    public void run() {
        for (Animal animal : this.listOfAnimals) {
                map.place(animal);
        }
        while (running) {
            try {
                map.removeDeadAnimals();
                map.moveAnimals();
                map.handleEating();
                map.handleReproduction();
                map.putPlants();
                map.stepCounters();
                map.notifyListeners(String.valueOf(map.getAnimals().size()));

                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
