package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnimalTest {

    @Test
    public void testAnimalMovement() {
        Animal animal = new Animal(new Vector2d(2, 2), 10, List.of(0, 2, 4, 6, 7, 5, 3, 2, 4));
        animal.setOrientation(0);

        animal.move(0);
        assertEquals(new Vector2d(2, 3), animal.getPosition());
        assertEquals(0, animal.getOrientation());

        animal.move(2);
        assertEquals(new Vector2d(3, 3), animal.getPosition());
        assertEquals(2, animal.getOrientation());

        animal.move(4);
        assertEquals(new Vector2d(2, 3), animal.getPosition());
        assertEquals(6, animal.getOrientation());

        animal.move(6);
        assertEquals(new Vector2d(2, 2), animal.getPosition());
        assertEquals(4, animal.getOrientation());

        animal.move(7);
        assertEquals(new Vector2d(3, 1), animal.getPosition());
        assertEquals(3, animal.getOrientation());

        animal.move(5);
        assertEquals(new Vector2d(3, 2), animal.getPosition());
        assertEquals(0, animal.getOrientation());

        animal.move(3);
        assertEquals(new Vector2d(4, 1), animal.getPosition());
        assertEquals(3, animal.getOrientation());

        animal.move(2);
        assertEquals(new Vector2d(3, 0), animal.getPosition());
        assertEquals(5, animal.getOrientation());

        animal.move(4);
        assertEquals(new Vector2d(4, 1), animal.getPosition());
        assertEquals(1, animal.getOrientation());

    }


}
