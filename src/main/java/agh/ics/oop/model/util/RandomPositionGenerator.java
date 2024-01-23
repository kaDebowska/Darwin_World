package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    private  List<Vector2d> positions;
    private int counter;

    //    deep copy
    public RandomPositionGenerator(RandomPositionGenerator original) {
        this.positions = new ArrayList<>(original.positions);
        this.counter = original.getCounter();
    }

    public RandomPositionGenerator(List<Vector2d> list, int counter) {
        this.positions = list;
        this.counter = counter;
    }

    public RandomPositionGenerator(int width, int height, int counter) {

        this(width, height, counter, 0, 0);
    }


    public RandomPositionGenerator(int width, int height, int counter, int startX, int startY){
        this.positions = new ArrayList<>();
        this.counter = counter;

        // Populate the list with all possible variations of position
        for (int x = startX; x <= width; x++) {
            for (int y = startY; y <= height; y++) {
                positions.add(new Vector2d(x, y));
            }
        }
        if(this.counter > positions.size()){
            this.counter = positions.size();
        }
    }

    public void setCounter(int counter) {
        if(counter > positions.size()){
            this.counter = positions.size();
        } else {
            this.counter = counter;
        }

    }

    public int getCounter() {
        return counter;
    }

    public List<Vector2d> getPositions() {
        return positions;
    }

    public void addPosition(Vector2d position) {
        this.positions.add(position);
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return new Iterator<>() {

            @Override
            public boolean hasNext() {
                return counter > 0;
            }

            @Override
            public Vector2d next() {
                counter--;
                int randomIndex = ThreadLocalRandom.current().nextInt(positions.size());
                Vector2d randomVector = positions.get(randomIndex);
                positions.remove(randomIndex);
                return randomVector;
            }
        };
    }

}
