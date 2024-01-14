package agh.ics.oop.model;


import java.util.List;

public class AnimalGroup implements WorldElement {
    private List<Animal> animals;

    public AnimalGroup(List<Animal> animals) {
        this.animals = animals;
    }

    @Override
    public Vector2d getPosition() {
        return animals.get(0).getPosition();
    }

    @Override
    public boolean isAt(Vector2d position) {
        for (Animal animal : animals) {
            if (animal.getPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return animals.get(0).toString();
    }

    public List<Animal> getAnimals() {
        return animals;
    }

}
