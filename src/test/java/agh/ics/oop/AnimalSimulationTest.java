package agh.ics.oop;

import agh.ics.oop.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalSimulationTest {

    private MoveValidator validator;

    @BeforeEach
    public void setup() {
        this.validator = new RectangularMap(4, 4);
    }

//    @Test
//    public void testAnimalOrientation() {
//        Animal animal = new Animal();
//        assertEquals(MapDirection.NORTH, animal.getOrientation());
//        animal.move(validator, MoveDirection.LEFT);
//        assertEquals(MapDirection.WEST, animal.getOrientation());
//        animal.move(validator, MoveDirection.LEFT);
//        assertEquals(MapDirection.SOUTH, animal.getOrientation());
//        animal.move(validator, MoveDirection.LEFT);
//        assertEquals(MapDirection.EAST, animal.getOrientation());
//        animal.move(validator, MoveDirection.LEFT);
//        assertEquals(MapDirection.NORTH, animal.getOrientation());
//        animal.move(validator, MoveDirection.RIGHT);
//        assertEquals(MapDirection.EAST, animal.getOrientation());
//        animal.move(validator, MoveDirection.RIGHT);
//        assertEquals(MapDirection.SOUTH, animal.getOrientation());
//        animal.move(validator, MoveDirection.RIGHT);
//        assertEquals(MapDirection.WEST, animal.getOrientation());
//        animal.move(validator, MoveDirection.RIGHT);
//        assertEquals(MapDirection.NORTH, animal.getOrientation());
//    }

    @Test
    public void testAnimalMovement() {
        GlobeMap globeMap = new GlobeMap(4, 4, 4);
        Animal animal = new Animal(globeMap, new Vector2d(2, 2), 0);
        animal.move(globeMap, 2);
        assertEquals(new Vector2d(3, 2), animal.getPosition());
        animal.move(globeMap, 2);
        assertEquals(new Vector2d(3, 1), animal.getPosition());
        animal.move(globeMap, 2);
        assertEquals(new Vector2d(2, 1), animal.getPosition());
        animal.move(globeMap, 2);
        assertEquals(new Vector2d(2, 2), animal.getPosition());
        animal.move(globeMap, 1);
        assertEquals(new Vector2d(3, 3), animal.getPosition());
        animal.move(globeMap, 7);
        assertEquals(new Vector2d(3, 4), animal.getPosition());
        // try to move out of the top edge
        animal.move(globeMap, 1);
        assertEquals(new Vector2d(3, 4), animal.getPosition());
        assertEquals(animal.getOrientation(), 5);

        animal.move(globeMap, 5);
        assertEquals(new Vector2d(4, 4), animal.getPosition());
        assertEquals(animal.getOrientation(), 2);

        //try to move out of the right edge
        animal.move(globeMap, 8);
        assertEquals(new Vector2d(0, 4), animal.getPosition());
        assertEquals(animal.getOrientation(), 2);

        // try to move out of the left edge
        animal.move(globeMap, 4);
        assertEquals(new Vector2d(4, 4), animal.getPosition());
        assertEquals(animal.getOrientation(), 6);

        //try to move out of the right and top edge
        animal.move(globeMap, 3);
        assertEquals(new Vector2d(0, 4), animal.getPosition());
        assertEquals(animal.getOrientation(), 5);

    }

//    @Test
//    public void testAnimalStaysInMap() {
//        Vector2d vector0 = new Vector2d(0, 0);
//        Vector2d vector4 = new Vector2d(4, 4);
//        Animal animal0 = new Animal(vector0);
//        animal0.move(validator, MoveDirection.BACKWARD);
//        assertTrue(animal0.isAt(vector0));
//        animal0.move(validator, MoveDirection.RIGHT);
//        animal0.move(validator, MoveDirection.BACKWARD);
//        assertTrue(animal0.isAt(vector0));
//        animal0.move(validator, MoveDirection.RIGHT);
//        animal0.move(validator, MoveDirection.FORWARD);
//        assertTrue(animal0.isAt(vector0));
//        animal0.move(validator, MoveDirection.RIGHT);
//        animal0.move(validator, MoveDirection.FORWARD);
//        assertTrue(animal0.isAt(vector0));
//
//        Animal animal4 = new Animal(vector4);
//        animal4.move(validator, MoveDirection.FORWARD);
//        assertTrue(animal4.isAt(vector4));
//        animal4.move(validator, MoveDirection.RIGHT);
//        animal4.move(validator, MoveDirection.FORWARD);
//        assertTrue(animal4.isAt(vector4));
//        animal4.move(validator, MoveDirection.RIGHT);
//        animal4.move(validator, MoveDirection.BACKWARD);
//        assertTrue(animal4.isAt(vector4));
//        animal4.move(validator, MoveDirection.RIGHT);
//        animal4.move(validator, MoveDirection.BACKWARD);
//        assertTrue(animal4.isAt(vector4));
//    }

    // Correct interpretation of an input is tested in OptionParserTest class.
    // The test was updated to address changes in the return type of the parseDirection() method.
    // The test was updated to address changes in the arguments of the move() method.


}
