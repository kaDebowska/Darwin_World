package agh.ics.oop.model;

public class RectangularMap extends AbstractWorldMap {

    private final int width;
    private final int height;

    public RectangularMap(int width, int height) {
        this.width = width;
        this.height = height;
    }

    private boolean isWithinMap(Vector2d position) {
        return position.precedes(new Vector2d(this.width, this.height)) && position.follows(new Vector2d(0, 0));
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return super.canMoveTo(position) && isWithinMap(position);
    }

    @Override
    public Boundary getCurrentBounds() {
        Vector2d bottomLeft = new Vector2d(0, 0);
        Vector2d topRight = new Vector2d(this.width, this.height);
        return new Boundary(bottomLeft, topRight);
    }

    @Override
    public void putPlants() {

    }
}