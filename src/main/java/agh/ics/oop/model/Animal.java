package agh.ics.oop.model;

public class Animal implements WorldElement {

    private MapDirection orientation;

    private Vector2d position;


    public Animal() {
        this(new Vector2d(2, 2));
    }

    public Animal(Vector2d position) {
        this.orientation = MapDirection.NORTH;
        this.position = position;
    }

    private void setOrientation(MapDirection orientation) {
        this.orientation = orientation;
    }

    private void setPosition(Vector2d position) {
        this.position = position;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return this.orientation.toString();
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }


    public void move(MoveValidator moveValidator, MoveDirection direction) {
        Vector2d newPosition;
        switch (direction) {
            case RIGHT -> this.setOrientation(this.orientation.next());
            case LEFT -> this.setOrientation(this.orientation.previous());
            case FORWARD -> {
                newPosition = this.position.add(this.orientation.toUnitVector());
                if (moveValidator.canMoveTo(newPosition))
                    this.setPosition(newPosition);
            }
            case BACKWARD -> {
                newPosition = this.position.substract(this.orientation.toUnitVector());
                if (moveValidator.canMoveTo(newPosition))
                    this.setPosition(newPosition);
            }
        }
    }


}
