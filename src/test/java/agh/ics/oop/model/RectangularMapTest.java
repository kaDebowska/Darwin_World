package agh.ics.oop.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangularMapTest {

    private final RectangularMap map = new RectangularMap(5, 5);
    private final Vector2d defaultPlacement = new Vector2d(2, 2);


    @Test
    void canMoveToAndPlace() throws PositionAlreadyOccupiedException {
        // Testing possibility of move outside the map boundaries
        assertTrue(map.canMoveTo(new Vector2d(5, 5)));
        assertTrue(map.canMoveTo(new Vector2d(0, 0)));
        assertFalse(map.canMoveTo(new Vector2d(6, 5)));
        assertFalse(map.canMoveTo(new Vector2d(0, -1)));

        // Testing the possibility to move in the occupied cell/place
        assertTrue(map.canMoveTo(defaultPlacement));
        Animal animal = new Animal();
        assertTrue(map.place(animal));
        assertFalse(map.canMoveTo(defaultPlacement));
    }

//    @Test
//    public void testMoveAndPlaceAndIsOccupied() throws PositionAlreadyOccupiedException {
//        Vector2d newPlace = new Vector2d(2, 3);
//        Animal animal = new Animal();
//        assertTrue(map.place(animal));
//
//        // Testing move to each direction
//        map.move(animal, MoveDirection.FORWARD);
//        assertEquals(animal.getPosition(), newPlace);
//        assertTrue(map.isOccupied(newPlace));
//        assertFalse(map.isOccupied(defaultPlacement));
//        map.move(animal, MoveDirection.RIGHT);
//        assertEquals(animal.getOrientation(), MapDirection.EAST);
//        map.move(animal, MoveDirection.LEFT);
//        assertEquals(animal.getOrientation(), MapDirection.NORTH);
//        map.move(animal, MoveDirection.BACKWARD);
//        assertEquals(animal.getPosition(), defaultPlacement);
//        assertTrue(map.isOccupied(defaultPlacement));
//        assertFalse(map.isOccupied(newPlace));
//
//        // Testing move to an occupied spot
//        Animal otherAnimal = new Animal(newPlace);
//        assertTrue(map.place(otherAnimal));
//        map.move(otherAnimal, MoveDirection.BACKWARD);
//        assertEquals(otherAnimal.getPosition(), newPlace);
//        assertTrue(map.isOccupied(defaultPlacement));
//        assertTrue(map.isOccupied(newPlace));
//    }

    @Test
    public void testPlaceAndIsOccupied() throws PositionAlreadyOccupiedException {
        Vector2d placement = new Vector2d(1, 1);
        Animal animal = new Animal(placement);
        assertTrue(map.place(animal));
        assertTrue(map.isOccupied(placement));
        Assertions.assertThrows(PositionAlreadyOccupiedException.class, () -> map.place(new Animal(placement)));
    }

    @Test
    void testPlaceAndObjectAt() throws PositionAlreadyOccupiedException {
        Animal animal = new Animal();
        assertTrue(map.place(animal));
        assertEquals(animal, map.objectAt(defaultPlacement));
    }

}