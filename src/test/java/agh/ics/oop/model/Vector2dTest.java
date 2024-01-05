package agh.ics.oop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Vector2dTest {
    private Vector2d vector0;
    private Vector2d vector1;
    private Vector2d vector2;
    private Vector2d vector3;
    private Vector2d vector4;
    private Vector2d maxVector;
    private Vector2d minVector;

    @BeforeEach
    void setUp() {
        vector0 = new Vector2d(0, 0);
        vector1 = new Vector2d(1, 1);
        vector2 = new Vector2d(-2, -2);
        vector3 = new Vector2d(-2, 1);
        vector4 = new Vector2d(1, -2);
        maxVector = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
        minVector = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    @Test
    void testGetX() {
        assertEquals(0, vector0.getX());
    }

    @Test
    void testGetY() {
        assertEquals(0, vector0.getY());
    }

    @Test
    void testToString() {
        assertEquals("(0,0)", vector0.toString());
    }

    @Test
    void testPrecedes() {
        assertTrue(vector0.precedes(vector0));
        assertTrue(minVector.precedes(maxVector));
        assertFalse(vector4.precedes(vector3));
        assertFalse(vector3.precedes(vector4));
        assertFalse(maxVector.precedes(vector0));
    }

    @Test
    void testFollows() {
        assertTrue(vector1.follows(vector0));
        assertTrue(maxVector.follows(minVector));
        assertFalse(vector3.follows(vector4));
        assertFalse(vector4.follows(vector3));
        assertFalse(minVector.follows(vector0));
    }

    @Test
    void testAdd() {
        assertEquals(new Vector2d(-1, -1), vector1.add(vector2));
        assertEquals(vector1, vector1.add(vector0));
        assertEquals(vector1, vector0.add(vector1));
        assertEquals(vector2, vector2.add(vector0));
        assertEquals(vector2, vector0.add(vector2));
    }

    @Test
    void testSubtract() {
        assertEquals(new Vector2d(3, 3), vector1.substract(vector2));
        assertEquals(vector1, vector1.substract(vector0));
        assertEquals(vector2, vector2.substract(vector0));
    }

    @Test
    void testUpperRight() {
        assertEquals(new Vector2d(0, 1), vector0.upperRight(vector3));
        assertEquals(vector3.upperRight(vector4), vector4.upperRight(vector3));
        assertEquals(vector1, vector1.upperRight(vector4));
        assertEquals(maxVector, minVector.upperRight(maxVector));
    }

    @Test
    void testLowerLeft() {
        assertEquals(new Vector2d(-2, 0), vector0.lowerLeft(vector3));
        assertEquals(vector3.lowerLeft(vector4), vector4.lowerLeft(vector3));
        assertEquals(vector4, vector1.lowerLeft(vector4));
        assertEquals(minVector, maxVector.lowerLeft(minVector));
    }

    @Test
    void testOpposite() {
        assertEquals(new Vector2d(-1, -1), vector1.opposite());
        assertEquals(vector0, vector0.opposite());
    }

    @Test
    void testEquals() {
        Vector2d anotherVector1 = new Vector2d(0, 0);
        assertEquals(vector0, anotherVector1);
    }

    @Test
    void testHashCode() {
        Vector2d anotherVector0 = new Vector2d(0, 0);
        assertEquals(vector0.hashCode(), anotherVector0.hashCode());
    }

    @Test
    void crossTests() {
        assertEquals(vector0.substract(vector1), vector1.opposite());
        assertEquals(vector0.substract(vector2), vector2.opposite());
    }

    //    This one is strange
    @Test
    void testExceedingExtremeCases() {
        assertEquals(vector2, maxVector.add(maxVector));
        assertEquals(vector0, minVector.add(maxVector).add(vector1));
        assertEquals(minVector, maxVector.add(vector1));
        assertEquals(vector0, maxVector.substract(minVector).add(vector1));
        assertEquals(vector0, minVector.substract(minVector));
        assertEquals(maxVector, minVector.substract(vector1));
    }

}
