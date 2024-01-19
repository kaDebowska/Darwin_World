package agh.ics.oop.model;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CrazyAnimal extends Animal{
    public CrazyAnimal(Vector2d position, int health, int genomeLength) {
        super(position, health, genomeLength);
    }

    public CrazyAnimal(Vector2d position, int health, List<Integer> genome) {
        super(position, health, genome);
    }

    @Override
    public int getNextGene() {
        float chance = RAND.nextFloat();
        if (chance < 0.2) {
            this.skipToRandomGene();
        }
        return super.getNextGene();
    }
}
