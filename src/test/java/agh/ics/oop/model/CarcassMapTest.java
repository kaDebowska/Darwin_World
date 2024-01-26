package agh.ics.oop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static agh.ics.oop.presenter.BehaviourVariant.NORMAL_ANIMAL;
import static org.junit.jupiter.api.Assertions.*;

class CarcassMapTest {


    @Test
    public void testGettingPositionsAroundCarcass(){
        Animal animal1 = new Animal(new Vector2d(2, 2), 0, 5);
        CarcassMap carcassMap1 = new CarcassMap(NORMAL_ANIMAL,10, 10, 2, 50, 10, 10, 7,30, 7, 15, 20, 0, 10);
        carcassMap1.place(animal1);
        List<Vector2d> positionsOfDeadAnimals1 = carcassMap1.removeDeadAnimals();
        Map<Vector2d, Integer> fertilePlaces1 = carcassMap1.getPositionsAroundCarcass(positionsOfDeadAnimals1);

//        System.out.println("Dead animal at " + animal1.getPosition());
//        System.out.println(fertilePlaces1);
        Map<Vector2d, Integer> fertilePlaces1Test = new HashMap<>(Map.of(
                new Vector2d(1, 1), 7,
                new Vector2d(2, 1), 7,
                new Vector2d(3, 1), 7,
                new Vector2d(1, 2), 7,
                new Vector2d(2, 2), 7,
                new Vector2d(3, 2), 7,
                new Vector2d(1, 3), 7,
                new Vector2d(2, 3), 7,
                new Vector2d(3, 3), 7
        ));
        assertEquals(fertilePlaces1Test, fertilePlaces1);

        Animal animal2 = new Animal(new Vector2d(10, 10), 0, 5);
        CarcassMap carcassMap2 = new CarcassMap(NORMAL_ANIMAL,10, 10, 2, 50, 10, 10, 7,30, 7, 15, 20, 0, 10);
        carcassMap2.place(animal2);
        List<Vector2d> positionsOfDeadAnimals2 = carcassMap2.removeDeadAnimals();
        Map<Vector2d, Integer> fertilePlaces2 = carcassMap2.getPositionsAroundCarcass(positionsOfDeadAnimals2);

//        System.out.println("Dead animal at " + animal2.getPosition());
//        System.out.println(fertilePlaces2);
        Map<Vector2d, Integer> fertilePlaces2Test = new HashMap<>(Map.of(
                new Vector2d(10, 9), 7,
                new Vector2d(10, 10), 7,
                new Vector2d(9, 9), 7,
                new Vector2d(9, 10), 7,
                new Vector2d(0, 9), 7,
                new Vector2d(0, 10), 7
        ));
        assertEquals(fertilePlaces2Test, fertilePlaces2);


        Animal animal3 = new Animal(new Vector2d(0, 10), 0, 5);
        CarcassMap carcassMap3 = new CarcassMap(NORMAL_ANIMAL,10, 10, 2, 50, 10, 10, 7,30, 7, 15, 20, 0, 10);
        carcassMap3.place(animal3);
        List<Vector2d> positionsOfDeadAnimals3 = carcassMap3.removeDeadAnimals();
        Map<Vector2d, Integer> fertilePlaces3 = carcassMap3.getPositionsAroundCarcass(positionsOfDeadAnimals3);

//        System.out.println("Dead animal at " + animal3.getPosition());
//        System.out.println(fertilePlaces3);
        Map<Vector2d, Integer> fertilePlaces3Test = new HashMap<>(Map.of(
                new Vector2d(0, 9), 7,
                new Vector2d(1, 9), 7,
                new Vector2d(0, 10), 7,
                new Vector2d(1, 10), 7,
                new Vector2d(10, 9), 7,
                new Vector2d(10, 10), 7
        ));
        assertEquals(fertilePlaces3Test, fertilePlaces3);

        Animal animal4 = new Animal(new Vector2d(0, 0), 0, 5);

        CarcassMap carcassMap4 = new CarcassMap(NORMAL_ANIMAL,10, 10, 2, 50, 10, 10, 7,30, 7, 15, 20, 0, 10);
        carcassMap4.place(animal4);
        List<Vector2d> positionsOfDeadAnimals4 = carcassMap4.removeDeadAnimals();
        Map<Vector2d, Integer> fertilePlaces4 = carcassMap4.getPositionsAroundCarcass(positionsOfDeadAnimals4);

//        System.out.println("Dead animal at " + animal4.getPosition());
//        System.out.println(fertilePlaces4);
        Map<Vector2d, Integer> fertilePlaces4Test = new HashMap<>(Map.of(
                new Vector2d(0, 0), 7,
                new Vector2d(1, 0), 7,
                new Vector2d(0, 1), 7,
                new Vector2d(1, 1), 7,
                new Vector2d(10, 0), 7,
                new Vector2d(10, 1), 7
        ));
        assertEquals(fertilePlaces4Test, fertilePlaces4);

        Animal animal5 = new Animal(new Vector2d(10, 0), 0, 5);
        CarcassMap carcassMap5 = new CarcassMap(NORMAL_ANIMAL,10, 10, 2, 50, 10, 10, 7,30, 7, 15, 20, 0, 10);
        carcassMap5.place(animal5);
        List<Vector2d> positionsOfDeadAnimals5 = carcassMap5.removeDeadAnimals();
        Map<Vector2d, Integer> fertilePlaces5 = carcassMap5.getPositionsAroundCarcass(positionsOfDeadAnimals5);

        System.out.println("Dead animal at " + animal5.getPosition());
        System.out.println(fertilePlaces5);
        Map<Vector2d, Integer> fertilePlaces5Test = new HashMap<>(Map.of(
                new Vector2d(9, 0), 7,
                new Vector2d(10, 0), 7,
                new Vector2d(9, 1), 7,
                new Vector2d(10, 1), 7,
                new Vector2d(0, 0), 7,
                new Vector2d(0, 1), 7
        ));
        assertEquals(fertilePlaces5Test, fertilePlaces5);


        Animal animal6 = new Animal(new Vector2d(0, 5), 0, 5);
        CarcassMap carcassMap6 = new CarcassMap(NORMAL_ANIMAL,10, 10, 2, 50, 10, 10, 7,30, 7, 15, 20, 0, 10);
        carcassMap6.place(animal6);
        List<Vector2d> positionsOfDeadAnimals6 = carcassMap6.removeDeadAnimals();
        Map<Vector2d, Integer> fertilePlaces6 = carcassMap6.getPositionsAroundCarcass(positionsOfDeadAnimals6);

//        System.out.println("Dead animal at " + animal6.getPosition());
//        System.out.println(fertilePlaces6);
        Map<Vector2d, Integer> fertilePlaces6Test = new HashMap<>(Map.of(
                new Vector2d(0, 4), 7,
                new Vector2d(1, 4), 7,
                new Vector2d(0, 5), 7,
                new Vector2d(1, 5), 7,
                new Vector2d(0, 6), 7,
                new Vector2d(1, 6), 7,
                new Vector2d(10, 4), 7,
                new Vector2d(10, 5), 7,
                new Vector2d(10, 6), 7
        ));
        assertEquals(fertilePlaces6Test, fertilePlaces6);


        Animal animal7 = new Animal(new Vector2d(10, 5), 0, 5);
        CarcassMap carcassMap7 = new CarcassMap(NORMAL_ANIMAL,10, 10, 2, 50, 10, 10, 7,30, 7, 15, 20, 0, 10);
        carcassMap7.place(animal7);
        List<Vector2d> positionsOfDeadAnimals7 = carcassMap7.removeDeadAnimals();
        Map<Vector2d, Integer> fertilePlaces7 = carcassMap7.getPositionsAroundCarcass(positionsOfDeadAnimals7);


//        System.out.println("Dead animal at " + animal7.getPosition());
//        System.out.println(fertilePlaces7);
        Map<Vector2d, Integer> fertilePlaces7Test = new HashMap<>(Map.of(
                new Vector2d(9, 4), 7,
                new Vector2d(10, 4), 7,
                new Vector2d(9, 5), 7,
                new Vector2d(10, 5), 7,
                new Vector2d(9, 6), 7,
                new Vector2d(10, 6), 7,
                new Vector2d(0, 4), 7,
                new Vector2d(0, 5), 7,
                new Vector2d(0, 6), 7
        ));
        assertEquals(fertilePlaces7Test, fertilePlaces7);


        Animal animal8 = new Animal(new Vector2d(1, 10), 0, 5);
        CarcassMap carcassMap8 = new CarcassMap(NORMAL_ANIMAL,10, 10, 2, 50, 10, 10, 7,30, 7, 15, 20, 0, 10);
        carcassMap8.place(animal8);

        List<Vector2d> positionsOfDeadAnimals8 = carcassMap8.removeDeadAnimals();
        Map<Vector2d, Integer> fertilePlaces8 = carcassMap8.getPositionsAroundCarcass(positionsOfDeadAnimals8);

//        System.out.println("Dead animal at " + animal8.getPosition());
//        System.out.println(fertilePlaces8);
        Map<Vector2d, Integer> fertilePlaces8Test = new HashMap<>(Map.of(
                new Vector2d(0, 9), 7,
                new Vector2d(1, 9), 7,
                new Vector2d(2, 9), 7,
                new Vector2d(0, 10), 7,
                new Vector2d(1, 10), 7,
                new Vector2d(2, 10), 7
        ));
        assertEquals(fertilePlaces8Test, fertilePlaces8);


        Animal animal9 = new Animal(new Vector2d(1, 0), 0, 5);
        CarcassMap carcassMap9 = new CarcassMap(NORMAL_ANIMAL,10, 10, 2, 50, 10, 10, 7,30, 7, 15, 20, 0, 10);
        carcassMap9.place(animal9);
        List<Vector2d> positionsOfDeadAnimals9 = carcassMap9.removeDeadAnimals();
        Map<Vector2d, Integer> fertilePlaces9 = carcassMap9.getPositionsAroundCarcass(positionsOfDeadAnimals9);

//        System.out.println("Dead animal at " + animal9.getPosition());
//        System.out.println(fertilePlaces9);
        Map<Vector2d, Integer> fertilePlaces9Test = new HashMap<>(Map.of(
                new Vector2d(0, 0), 7,
                new Vector2d(1, 0), 7,
                new Vector2d(2, 0), 7,
                new Vector2d(0, 1), 7,
                new Vector2d(1, 1), 7,
                new Vector2d(2, 1), 7
        ));
        assertEquals(fertilePlaces9Test, fertilePlaces9);
    }
}



