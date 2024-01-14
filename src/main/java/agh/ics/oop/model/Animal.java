package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Animal implements WorldElement {

    private int orientation;
    private Vector2d position;
    private List<Integer> genome;
    private Iterator<Integer> genomeIterator;

    private int health;



    public Animal() {
        this(new Vector2d(2, 2), 50, 10);
    }

    public Animal(Vector2d position, int health, int genomeLength) {
        Random rand = new Random();
        this.orientation = rand.nextInt(8);
        this.position = position;
        this.health = health;
        this.genome = new ArrayList<>();
        for (int i = 0; i < genomeLength; i++) {
            this.genome.add(rand.nextInt(8));
        }
        this.genomeIterator = genome.iterator();
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getOrientation() {
        return this.orientation;
    }

    void setPosition(Vector2d position) {
        this.position = position;
    }

    public Vector2d getPosition() {
        return position;
    }

    public List<Integer> getGenome() {
        return this.genome;
    }

    public int getNextGene() {
        if (!genomeIterator.hasNext()) {
            genomeIterator = genome.iterator();
        }
        return genomeIterator.next();
    }

    public void restoreHealth(int energy) {
        this.health += energy;
    }

    public void dailyFatigue() {
        this.health--;
    }

    @Override
    public String toString() {
        return String.valueOf(this.orientation);
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public Vector2d toUnitVector(int orientation) {
        return getVector2d(orientation);
    }

    static Vector2d getVector2d(int orientation) {
        return switch (orientation) {
            case 0 -> new Vector2d(0, 1);
            case 1 -> new Vector2d(1, 1);
            case 2 -> new Vector2d(1, 0);
            case 3 -> new Vector2d(1, -1);
            case 4 -> new Vector2d(0, -1);
            case 5 -> new Vector2d(-1, -1);
            case 6 -> new Vector2d(-1, 0);
            case 7 -> new Vector2d(-1, 1);
            default -> throw new IllegalArgumentException("Invalid orientation");
        };
    }


    public void move(MoveValidator moveValidator, int direction) {
        Vector2d newPosition;
        this.setOrientation((this.orientation + direction) % 8);
        newPosition = this.position.add(this.toUnitVector(this.orientation));
        if (moveValidator.canMoveTo(newPosition))
            this.setPosition(newPosition);
    }


}