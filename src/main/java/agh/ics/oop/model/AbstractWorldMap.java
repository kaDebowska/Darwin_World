package agh.ics.oop.model;

import agh.ics.oop.presenter.BehaviourVariant;

import java.util.*;

import static java.lang.Math.abs;

public abstract class AbstractWorldMap implements WorldMap {
    protected Map<Vector2d, AnimalGroup> animalGroups;
    private final int animalStartNumber;
    private final int healthToReproduce;
    private final int reproductionCost;
    protected Map<Vector2d, Grass> grassClumps;
    private final int plantsEnergy;
    protected final int width;
    protected final int height;
    protected final int everDayPlantsNum;

    protected final int startPlantsNum;
    private List<MapChangeListener> listeners;
    private int day = 0;
    private int minMutations;
    private int maxMutations;
    private BehaviourVariant behaviourVariant;
    private int animalStartHealth;
    private int animalGenomeLength;
    private UUID uuid;
    private List<Integer> tombsInfo;
    private MapStatistics statistics;


    protected List<Vector2d> fertilePositions;

    protected List<Vector2d> positionsOfDeadAnimals;

    public AbstractWorldMap(BehaviourVariant behaviourVariant, int width, int height, int animalStartNumber, int animalStartHealth, int animalGenomeLength, int startPlantsNum, int everDayPlantsNum, int plantsEnergy, int healthToReproduce, int reproductionCost, int minMutations, int maxMutations) {
        this.behaviourVariant = behaviourVariant;
        this.width = width;
        this.height = height;
        this.animalStartNumber = animalStartNumber;
        this.animalStartHealth = animalStartHealth;
        this.animalGenomeLength = animalGenomeLength;
        this.startPlantsNum = startPlantsNum;
        this.everDayPlantsNum = everDayPlantsNum;
        this.plantsEnergy = plantsEnergy;
        this.healthToReproduce = healthToReproduce;
        this.reproductionCost = reproductionCost;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.animalGroups = new HashMap<>();
        this.listeners = new ArrayList<>();
        this.grassClumps = new HashMap<>();
        this.uuid = UUID.randomUUID();
        this.tombsInfo = new ArrayList<>();
        this.statistics = new MapStatistics(this);
        this.fertilePositions = new ArrayList<>();
    }


    @Override
    public void place(Animal animal) {
        Vector2d position = animal.getPosition();
        if (animalGroups.containsKey(position)) {
            animalGroups.get(position).addAnimal(animal);
        } else {
            AnimalGroup newList = new AnimalGroup(animal);
            animalGroups.put(position, newList);
        }
    }


    public boolean isTopOrBottomMapEdge(Vector2d position) {
        return position.getY() == this.height + 1 || position.getY() == -1;
    }

    public boolean isLeftOrRightMapEdge(Vector2d position) {
        return position.getX() == this.width + 1 || position.getX() == -1;
    }

    public int reflect(int orientation) {
        return (12 - orientation) % 8;
    }

    public Vector2d movingOnTheGlobe(Animal animal, Vector2d newPosition){
        if (this.isTopOrBottomMapEdge(newPosition) & this.isLeftOrRightMapEdge(newPosition)) {
            newPosition = new Vector2d(abs(newPosition.getX() - this.width) - 1, abs(newPosition.getY()) - 1);
            animal.setPosition(newPosition);
            animal.setOrientation(reflect(animal.getOrientation()));
        } else if (this.isLeftOrRightMapEdge(newPosition)) {
            newPosition = new Vector2d((abs(newPosition.getX() - this.width) - 1), newPosition.getY());
            animal.setPosition(newPosition);
        } else if (this.isTopOrBottomMapEdge(newPosition)) {
            var direction = animal.getOrientation();
//            if direction is not diagonally
            if(direction % 2 == 0) {
                newPosition = new Vector2d(newPosition.getX(), abs(newPosition.getY()) - 1);
            } else if(direction == 1 || direction == 3) {
                newPosition = new Vector2d(newPosition.getX() - 1, abs(newPosition.getY()) - 1);
            } else if (direction == 5 || direction == 7) {
                newPosition = new Vector2d(newPosition.getX() + 1, abs(newPosition.getY()) - 1);
            }
            animal.setPosition(newPosition);
            animal.setOrientation(reflect(animal.getOrientation()));
        }
        return newPosition;
    }

    @Override
    public void move(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        int direction = animal.getNextGene();
        animal.move(direction);
        Vector2d newPosition = movingOnTheGlobe(animal, animal.getPosition());

        if (!oldPosition.equals(newPosition)) {
            AnimalGroup oldGroup = animalGroups.get(oldPosition);
            if (oldGroup != null) {
                oldGroup.removeAnimal(animal);
                if (oldGroup.isEmpty()) {
                    animalGroups.remove(oldPosition);
                }
            }
            if (oldGroup.isEmpty()) {
                animalGroups.remove(oldPosition);
            }
            AnimalGroup newGroup = animalGroups.get(newPosition);
            if (newGroup == null) {
                newGroup = new AnimalGroup(animal);
                animalGroups.put(newPosition, newGroup);
            } else {
                newGroup.addAnimal(animal);
            }
        }
    }

    public void moveAnimals() {
        for (Animal animal : getAnimals()) {
            move(animal);
        }
    }


    @Override
    public Optional<WorldElement> objectAt(Vector2d position) {
        AnimalGroup animalGroupAtPosition = animalGroups.get(position);
        if (animalGroupAtPosition != null) {
            return Optional.of(animalGroupAtPosition);
        }
        return Optional.ofNullable(grassClumps.get(position));
    }


    public void subscribe(MapChangeListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(MapChangeListener listener) {
        listeners.remove(listener);
    }

    //    different name
    public void notifyListeners(String message) {
        if (this.listeners != null) {
            for (MapChangeListener listener : listeners) {
                listener.onMapChange(this, message);
            }
        }
    }

    public synchronized void handleEating() {
        List<Vector2d> positionsToHandle = new ArrayList<>(animalGroups.keySet());
        for (Vector2d position : positionsToHandle) {
            if (grassClumps.containsKey(position)) {
                Animal animalToEat = animalGroups.get(position).getOrderedAnimals().get(0);
                this.eat(animalToEat, animalToEat.getPosition());
            }
        }
    }


    private void eat(Animal animal, Vector2d position) {
        animal.restoreHealth(this.plantsEnergy);
        animal.countEaten();
        grassClumps.remove(position);
    }

    public synchronized void handleReproduction() {
        List<Animal[]> animalsToReproduce = new ArrayList<>();
        for (Vector2d position : animalGroups.keySet()) {
            if (animalGroups.get(position).isProlific()) {
                List<Animal> sortedAnimals = animalGroups.get(position).getOrderedAnimals();
                Animal animalAlpha = sortedAnimals.get(0);
                Animal animalBeta = sortedAnimals.get(1);
                if (animalAlpha.getHealth() >= healthToReproduce && animalBeta.getHealth() >= healthToReproduce) {
                    animalsToReproduce.add(new Animal[]{animalAlpha, animalBeta});
                }
            }
        }
        for (Animal[] pair : animalsToReproduce) {
            this.reproduce(pair[0], pair[1]);
        }
    }

    private void reproduce(Animal animalAlpha, Animal animalBeta) {
        animalAlpha.setHealth(animalAlpha.getHealth() - this.reproductionCost);
        animalBeta.setHealth(animalBeta.getHealth() - this.reproductionCost);

//        Caluclate the share of the alpha genome
        int genomeLength = animalAlpha.getGenome().size();
        int alphaShare = (int) Math.round((double) animalAlpha.getHealth() / (animalAlpha.getHealth() + animalBeta.getHealth()) * genomeLength);
        int betaShare = genomeLength - alphaShare;

        List<Integer> babyGenome = new ArrayList<>();

//        Get right or left side of alpha parent
        Random rand = new Random();
        if (rand.nextBoolean()) {
            babyGenome.addAll(animalAlpha.getGenome().subList(0, alphaShare));
            babyGenome.addAll(animalBeta.getGenome().subList(genomeLength - betaShare, genomeLength));
        } else {
            babyGenome.addAll(animalAlpha.getGenome().subList(genomeLength - alphaShare, genomeLength));
            babyGenome.addAll(animalBeta.getGenome().subList(0, betaShare));
        }

//        Mutate genome
        int numMutations = rand.nextInt((Math.min(maxMutations, babyGenome.size()) - minMutations) + 1) + minMutations;

        for (int i = 0; i < numMutations; i++) {
            int mutationIndex = rand.nextInt(babyGenome.size());
            int newGene = rand.nextInt(8);
            babyGenome.set(mutationIndex, newGene);
        }


        Animal babyAnimal = behaviourVariant == BehaviourVariant.NORMAL_ANIMAL
                ? new Animal(animalAlpha.getPosition(), this.reproductionCost * 2, babyGenome)
                : new CrazyAnimal(animalAlpha.getPosition(), this.reproductionCost * 2, babyGenome);
        animalAlpha.addChildren(babyAnimal);
        animalBeta.addChildren(babyAnimal);
        this.place(babyAnimal);
    }

    public void stepCounters() {
        for (Animal animal : getAnimals()) {
            animal.dailyFatigue();
        }
        this.day++;


    }

    public synchronized List<Vector2d> removeDeadAnimals() {
        List<Vector2d> emptyPositions = new ArrayList<>();
        Iterator<Map.Entry<Vector2d, AnimalGroup>> iterator = animalGroups.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Vector2d, AnimalGroup> entry = iterator.next();
            Vector2d position = entry.getKey();
            AnimalGroup group = entry.getValue();
            List<Animal> deadAnimals = new ArrayList<>();
            for (Animal animal : group.getAnimals()) {
                if (animal.getHealth() <= 0) {
                    deadAnimals.add(animal);
                }
            }
            for (Animal deadAnimal : deadAnimals) {
                this.tombsInfo.add(deadAnimal.getAge());
                group.removeAnimal(deadAnimal);
            }
            if (group.isEmpty()) {
                emptyPositions.add(position);
                iterator.remove();
            }

        }

        this.positionsOfDeadAnimals = emptyPositions;

        return emptyPositions;
    }


    public List<Animal> getAnimals() {
        if (this.animalGroups == null) {
            return new ArrayList<>();
        }
        List<Animal> animalList = new ArrayList<>();
        for (AnimalGroup animalGroup : this.animalGroups.values()) {
            List<Animal> groupAnimals = animalGroup.getAnimals();
            if (groupAnimals != null) {
                animalList.addAll(groupAnimals);
            }
        }
        return animalList;
    }

    public int getAnimalStartNumber() {
        return animalStartNumber;
    }

    public BehaviourVariant getBehaviourVariant() {
        return this.behaviourVariant;
    }

    public int getAnimalStartHealth() {
        return animalStartHealth;
    }

    public int getAnimalGenomeLength() {
        return animalGenomeLength;
    }

    public UUID getId() {
        return this.uuid;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getDay() {
        return day;
    }

    public List<Integer> getTombsInfo() {
        return tombsInfo;
    }

    public Map<Vector2d, Grass> getGrassClumps() {
        return grassClumps;
    }

    public Map<Vector2d, AnimalGroup> getAnimalGroups() {
        return animalGroups;
    }

    public String getMapInformation() {
        return statistics.getMapInformation();
    }

    @Override
    public boolean isFertilePosition(Vector2d position) {
        return this.fertilePositions.contains(position);
    }

    @Override
    public Boundary getCurrentBounds() {
        Vector2d bottomLeft = new Vector2d(0, 0);
        Vector2d topRight = new Vector2d(this.width, this.height);
        return new Boundary(bottomLeft, topRight);
    }

}

