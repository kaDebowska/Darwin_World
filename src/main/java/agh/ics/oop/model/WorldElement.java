package agh.ics.oop.model;

// It seems that abstract class would fit more.
public interface WorldElement {


    Vector2d getPosition();


    boolean isAt(Vector2d position);

    // Forces classes that implements the interface to override toString().
    @Override
    String toString();


}
