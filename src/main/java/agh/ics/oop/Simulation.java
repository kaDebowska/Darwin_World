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
            try {
                map.place(animal);
            } catch (PositionAlreadyOccupiedException ex) {
                ex.printStackTrace();
            }
        }
        while (running) {
            for (Animal animal : this.listOfAnimals) {
                this.map.move(animal);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
