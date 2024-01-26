package agh.ics.oop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static agh.ics.oop.presenter.BehaviourVariant.NORMAL_ANIMAL;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractWorldMapTest {
    private GlobeMap globeMap;

    @BeforeEach
    public void mapInitialize(){
        globeMap = new GlobeMap(NORMAL_ANIMAL, 5, 5, 2, 50, 10, 10, 5, 30, 15, 20, 0, 10);
    }


    @Test
    public void testMovementOnTheGlobe(){

        Animal animal = new Animal(new Vector2d(4, 4), 20, List.of(0));
        animal.setOrientation(0);

        animal.move(0);
        globeMap.movingOnTheGlobe(animal, animal.getPosition());
        assertEquals(new Vector2d(4, 5), animal.getPosition());
        assertEquals(0, animal.getOrientation());

        // try to move out of the top edge (diagonal direction)
        animal.move(1);
        globeMap.movingOnTheGlobe(animal, animal.getPosition());
        assertEquals(new Vector2d(4, 5), animal.getPosition());
        assertEquals(3, animal.getOrientation());

        // try to move out of the top edge (straight direction)
        animal.move(5);
        globeMap.movingOnTheGlobe(animal, animal.getPosition());
        assertEquals(new Vector2d(4, 5), animal.getPosition());
        assertEquals(4, animal.getOrientation());

        //try to move to top right corner
        animal.move(6);
        globeMap.movingOnTheGlobe(animal, animal.getPosition());
        assertEquals(new Vector2d(5, 5), animal.getPosition());
        assertEquals(2, animal.getOrientation());

        //try to move out of the top right edge (diagonal direction)
        animal.move(7);
        globeMap.movingOnTheGlobe(animal, animal.getPosition());
        assertEquals(new Vector2d(0, 5), animal.getPosition());
        assertEquals(3, animal.getOrientation());


        //try to move out of the bottom and left edge
        animal.setPosition(new Vector2d(0, 0));
        animal.move(2);
        globeMap.movingOnTheGlobe(animal, animal.getPosition());
        assertEquals(new Vector2d(5, 0), animal.getPosition());
        assertEquals(7, animal.getOrientation());

        //try to move out of the bottom edge
        animal.move(5);
        globeMap.movingOnTheGlobe(animal, animal.getPosition());
        assertEquals(new Vector2d(5, 0), animal.getPosition());
        assertEquals(0, animal.getOrientation());

    }

    @Test
    void removeDeadAnimals() {
        Animal animal1 = new Animal(new Vector2d(4, 4), 0, List.of(0));
        globeMap.place(animal1);
        Animal animal2 = new Animal(new Vector2d(5, 5), 0, List.of(0));
        globeMap.place(animal2);
        var emptyPositions = globeMap.removeDeadAnimals();

        assertEquals(globeMap.animalGroups, new HashMap<>());
        assertEquals(emptyPositions, Arrays.asList(new Vector2d(4, 4), new Vector2d(5, 5)));

    }
}