package agh.ics.oop.model;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public class Statistics {
    private AbstractWorldMap map;

    public Statistics(AbstractWorldMap map) {
        this.map = map;
    }

    private boolean areCyclicPermutations(List<Integer> list1, List<Integer> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }

        List<Integer> temp = new ArrayList<>(list1);
        temp.addAll(list1);

        String list1String = temp.toString();
        String list2String = list2.toString();

        return list1String.contains(list2String);
    }

    public List<Integer> getMostCommonGenome() {
        Map<List<Integer>, Integer> genomeCount = new HashMap<>();
        List<Animal> animals = map.getAnimals();

        for (Animal animal : animals) {
            List<Integer> genome = new ArrayList<>(animal.getGenome());
            boolean found = false;
            for (List<Integer> key : genomeCount.keySet()) {
                if (areCyclicPermutations(genome, key)) {
                    genomeCount.put(key, genomeCount.get(key) + 1);
                    found = true;
                    break;
                }
            }
            if (!found) {
                genomeCount.put(genome, 1);
            }
        }

        return genomeCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public String getAverageLifespanOfDead() {
        double average = map.getTombsInfo().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);

        return String.format("%.2f", average);
    }

    public String getAverageKidsNo() {
        double averageKids = map.getAnimals().stream()
                .mapToInt(Animal::getKidsNumber)
                .average()
                .orElse(0);

        return String.format("%.2f", averageKids);
    }

    public String getAverageHealth() {
        double averageHealth = map.getAnimals().stream()
                .mapToInt(Animal::getHealth)
                .average()
                .orElse(0);

        return String.format("%.2f", averageHealth);
    }

    public String getMapInformation() {
        String dayNo = String.valueOf(map.getDay());
        String animalNo = String.valueOf(map.getAnimals().size());
        String plantsNo = String.valueOf(map.getGrassClumps().size());
        String averageHealth = getAverageHealth();
        String unoccupiedPositionsNo = String.valueOf((map.getWidth() * map.getHeight()) - Stream.concat(map.getAnimalGroups().keySet().stream(), map.getGrassClumps().keySet().stream()).collect(Collectors.toSet()).size());
        String mostCommonGenome = getMostCommonGenome().toString();
        String deadAverage = getAverageLifespanOfDead();
        String kidsAverage = getAverageKidsNo();

        // Combine all the information into a single string
        return String.format("%s;%s;%s;%s;%s;%s;%s;%s",
                dayNo, animalNo, plantsNo, unoccupiedPositionsNo, mostCommonGenome, deadAverage, kidsAverage, averageHealth);
    }
}
