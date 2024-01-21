package agh.ics.oop.model;

import java.util.*;

public class Animal implements WorldElement {

    private int orientation;
    private Vector2d position;
    protected List<Integer> genome;
    protected Iterator<Integer> genomeIterator;
    private int health;
    private int age;
    private int plantsEaten = 0;
    private Set<UUID> kids = new HashSet<>();
    private UUID uuid;
    protected static final Random RAND = new Random();


    public Animal(Vector2d position, int health, int genomeLength) {
        Random rand = new Random();
        this.orientation = rand.nextInt(8);
        this.position = position;
        this.health = health;
        this.genome = new LinkedList<>();
        for (int i = 0; i < genomeLength; i++) {
            this.genome.add(rand.nextInt(8));
        }
        this.genomeIterator = genome.listIterator();
        this.skipToRandomGene();
        this.uuid = UUID.randomUUID();
    }

    public Animal(Vector2d position, int health, List<Integer> genome) {
        Random rand = new Random();
        this.orientation = rand.nextInt(8);
        this.position = position;
        this.health = health;
        this.genome = new LinkedList<>(genome);
        this.genomeIterator = this.genome.listIterator();
        this.skipToRandomGene();
        this.uuid = UUID.randomUUID();
    }

    public void countEaten() {
        this.plantsEaten += 1;
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
            genomeIterator = genome.listIterator();
        }
        return genomeIterator.next();
    }

    protected void skipToRandomGene() {
        int jumps = RAND.nextInt(genome.size());
        genomeIterator = genome.listIterator(jumps);
    }

    public void restoreHealth(int energy) {
        this.health += energy;
    }

    public void dailyFatigue() {
        this.health--;
        this.age++;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAge() {
        return age;
    }

    public int getKidsNumber() {
        return kids.size();
    }

    public void addChildren(Animal other) {
        this.kids.add(other.getId());
    }

    @Override
    public String toString() {
        return String.valueOf(this.health);
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


    public void move(int direction) {
        Vector2d newPosition;
        this.setOrientation((this.orientation + direction) % 8);
        newPosition = this.position.add(this.toUnitVector(this.orientation));
        this.setPosition(newPosition);
    }


    public UUID getId() {
        return this.uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return Objects.equals(uuid, animal.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}