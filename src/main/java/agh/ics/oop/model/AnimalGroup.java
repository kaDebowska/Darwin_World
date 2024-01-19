package agh.ics.oop.model;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AnimalGroup implements WorldElement {
    private List<Animal> animals;

    public AnimalGroup(List<Animal> animals) {
        this.animals = animals;
    }

    public AnimalGroup(Animal animal) {
        this.animals = new ArrayList<>();
        animals.add(animal);
    }

    public void addAnimal(Animal animal) {
        this.animals.add(animal);
    }

    public void removeAnimal(Animal animal) {
        this.animals.remove(animal);
    }

    public boolean isProlific() {
        return this.animals.size() >= 2;
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

    public List<Animal> getOrderedAnimals() {
        return this.animals.stream()
                .sorted(Comparator.comparing(Animal::getHealth)
                        .thenComparing(Animal::getAge)
                        .thenComparing(Animal::getKidsNumber))
                .collect(Collectors.toList());
    }

    public List<Animal> getAnimals() {
        return animals;
    }

}
