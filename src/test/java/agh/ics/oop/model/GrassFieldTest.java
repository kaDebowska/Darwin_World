//package agh.ics.oop.model;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class GrassFieldTest {
//
//    private GrassField field;
//    private Animal animal;
//
//    @BeforeEach
//    void setUp() throws PositionAlreadyOccupiedException {
//        field = new GrassField(5);
//        animal = new Animal();
//        field.place(animal);
//    }
//
//    @Test
//    public void testCanMoveTo() {
//        assertFalse(field.canMoveTo(new Vector2d(2, 2)));
//        assertTrue(field.canMoveTo(new Vector2d(2, 3)));
//    }
//
//    @Test
//    public void testPlace() throws PositionAlreadyOccupiedException {
//        Assertions.assertThrows(PositionAlreadyOccupiedException.class, () -> field.place(animal));
//
//        Animal newAnimal = new Animal(new Vector2d(0, 0));
//        assertTrue(field.place(newAnimal));
//    }
//
////    @Test
////    public void testMove() {
////        field.move(animal, MoveDirection.FORWARD);
////        assertEquals(new Vector2d(2, 3), animal.getPosition());
////    }
//
//    @Test
//    public void testIsOccupied() {
//        assertTrue(field.isOccupied(new Vector2d(2, 2)));
//        assertFalse(field.isOccupied(new Vector2d((int) Math.sqrt(5 * 10) + 1, 0)));
//    }
//
//    @Test
//    public void testObjectAt() {
//        assertEquals(animal, field.objectAt(new Vector2d(2, 2)));
//        assertNull(field.objectAt(new Vector2d((int) Math.sqrt(5 * 10) + 1, 0)));
//    }
//}
