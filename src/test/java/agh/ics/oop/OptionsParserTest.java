package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.List;

import static agh.ics.oop.model.MoveDirection.*;
import static org.junit.jupiter.api.Assertions.*;

class OptionsParserTest {

    @Test
    void testParseValidDirections() {
        String[] directions = {"f", "b", "l", "r"};
        List<MoveDirection> expectedDirections = Arrays.asList(FORWARD, BACKWARD, LEFT, RIGHT);
        List<MoveDirection> actualDirections = OptionsParser.parseDirections(directions);
        assertEquals(expectedDirections, actualDirections);
    }

    @Test
    void testParseInvalidDirections() {
        String[] directions = {"f", "invalid", "l", "r"};
        assertThrows(IllegalArgumentException.class, () -> OptionsParser.parseDirections(directions));
    }

    @Test
    void testParseNoDirections() {
        String[] directions = {};
        List<MoveDirection> expectedDirections = List.of();
        List<MoveDirection> actualDirections = OptionsParser.parseDirections(directions);
        assertEquals(expectedDirections, actualDirections);
    }

}




