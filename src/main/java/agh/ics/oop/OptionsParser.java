package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;


import java.util.ArrayList;
import java.util.List;


public class OptionsParser {

    // Method v.1.0

    // The choice between ArrayList and LinkedList for storing movements depends on the future usage of MoveDirections.
    // If we anticipate frequent additions and removals, LinkedList might be more efficient.
    // If the list is primarily used for iteration or random access, ArrayList seems to be more suitable.
    public static List<MoveDirection> parseDirections(String[] directions) {

        List<MoveDirection> moveDirections = new ArrayList<>();

        for (String direction : directions) {
            switch (direction) {
                case "f", "forward" -> moveDirections.add(MoveDirection.FORWARD);
                case "b", "backward" -> moveDirections.add(MoveDirection.BACKWARD);
                case "l", "left" -> moveDirections.add(MoveDirection.LEFT);
                case "r", "right" -> moveDirections.add(MoveDirection.RIGHT);
                default -> {
                    throw new IllegalArgumentException(direction + " is not legal move specification");
                }
            }
        }

        return moveDirections;
    }
}
