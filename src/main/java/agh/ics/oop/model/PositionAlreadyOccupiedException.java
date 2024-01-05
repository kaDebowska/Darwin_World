package agh.ics.oop.model;

public class PositionAlreadyOccupiedException extends Exception{

    public PositionAlreadyOccupiedException(Vector2d vector2d) {
        super(String.format("Position (%d, %d) is already occupied", vector2d.getX(), vector2d.getY()));
    }
}
