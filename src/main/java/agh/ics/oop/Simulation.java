package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {

    private List<Animal> listOfAnimals;
    private List<MoveDirection> movements;
    private WorldMap map;

    // I prefer to use ArrayList here, since in the run method I need to often access animals by their index.
    public Simulation(List<Vector2d> positions, List<MoveDirection> movements, WorldMap map) {
        this.listOfAnimals = new ArrayList<>();
        for (Vector2d position : positions) {
            this.listOfAnimals.add(new Animal(position));
        }
        this.movements = movements;
        this.map = map;
    }

    @Override
    public void run() {
        int numberOfAnimals = this.listOfAnimals.size();

        for (Animal animal : this.listOfAnimals) {
            try {
                map.place(animal);
            } catch (PositionAlreadyOccupiedException ex) {
                ex.printStackTrace();
            }
        }
        if (this.movements != null && this.listOfAnimals != null && this.map != null) {
            for (int i = 0; i < this.movements.size(); i++) {
                Animal animal = this.listOfAnimals.get(i % numberOfAnimals);
                MoveDirection move = this.movements.get(i);
                this.map.move(animal, move);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
