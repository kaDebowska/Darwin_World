package agh.ics.oop.model;

import java.util.List;
import java.util.Optional;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap {

    /**
     * Place an animal on the map.
     *
     * @param animal The animal to place on the map.
     * @return True if the animal was placed. The animal cannot be placed if the move is not valid.
     */
    void place(Animal animal);

    /**
     * Moves an animal (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect.
     */
//    void move(Animal animal, MoveDirection direction);

    void move(Animal animal);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position Position to check.
     * @return True if the position is occupied.
     */

//    boolean isOccupied(Vector2d position);

    /**
     * Return an animal at a given position.
     *
     * @param position The position of the animal.
     * @return animal or null if the position is not occupied.
     */
    Optional<WorldElement> objectAt(Vector2d position);

    void subscribe(MapChangeListener listener);

    /**
     * Return bounds of a given map.
     *
     * @return a Boundary object that stores bottomLeft and topRight corner of a map.
     */

    void putPlants();

    Boundary getCurrentBounds();

    void handleEating();

    List<Animal> getAnimals();

    void handleReproduction();

    void notifyListeners(String step);

    List<Vector2d> removeDeadAnimals();

    void stepCounters();

    void moveAnimals();

    boolean isFertilePosition(Vector2d position);

}
