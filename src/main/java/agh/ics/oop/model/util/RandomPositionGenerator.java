package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    private final List<Vector2d> positions;
    private int counter;

    public RandomPositionGenerator(int width, int height, int counter) {

        this.positions = new ArrayList<>();
        this.counter = counter;

        // Populate the list with all possible variations of position
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                positions.add(new Vector2d(x, y));
            }
        }
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
