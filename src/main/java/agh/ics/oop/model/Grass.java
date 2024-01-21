package agh.ics.oop.model;

public class Grass implements WorldElement {

    private final Vector2d position;
    private final static String GRASS_SYMBOL = "*";


    public Grass(Vector2d position) {
        this.position = position;
    }



    public Vector2d getPosition() {
        return position;
    }

    @Override
    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    @Override
    public String toString() {
        return GRASS_SYMBOL;
    }
}
