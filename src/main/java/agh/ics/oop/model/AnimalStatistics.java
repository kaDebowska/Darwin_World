package agh.ics.oop.model;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnimalStatistics {

    private Animal animal;

    public AnimalStatistics(Animal animal) {
        this.animal = animal;
    }

    public String getAnimalInformation() {
        String animalID = String.valueOf(this.animal.getId());
        String genome = this.animal.getGenome().toString();
        String activeGene = String.valueOf(this.animal.getNextGene());
        String health = String.valueOf(this.animal.getHealth());
        String plantsEaten = String.valueOf(this.animal.getPlantsEaten());
        String kidsNo = String.valueOf(this.animal.getKidsNumber());
        String offspring = String.valueOf(this.animal.getNumberOfDescendants());
        String age = String.valueOf(this.animal.getAge());

        return String.format("%s;%s;%s;%s;%s;%s;%s;%s",
                animalID, genome, activeGene, health, plantsEaten, kidsNo, offspring, age);
    }

}
