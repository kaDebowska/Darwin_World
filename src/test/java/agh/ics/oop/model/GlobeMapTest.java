package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static agh.ics.oop.presenter.BehaviourVariant.NORMAL_ANIMAL;
import static org.junit.jupiter.api.Assertions.*;

class GlobeMapTest {
    @Test
    public void testCalculatingEquator(){
        GlobeMap globeMap1 = new GlobeMap(NORMAL_ANIMAL, 6, 6, 2, 50, 10, 10, 4, 30, 15, 20, 0, 10);
        Boundary equatorArea1 = globeMap1.calculateEquator();
        assertEquals(new Boundary(new Vector2d(0, 3), new Vector2d(1, 4)), equatorArea1);

        GlobeMap globeMap2 = new GlobeMap(NORMAL_ANIMAL, 5, 5, 2, 50, 10, 10, 4, 30, 15, 20, 0, 10);
        Boundary equatorArea2 = globeMap2.calculateEquator();
        assertEquals(new Boundary(new Vector2d(0, 2), new Vector2d(0, 3)), equatorArea2);

        GlobeMap globeMap3 = new GlobeMap(NORMAL_ANIMAL, 30, 1, 2, 50, 10, 10, 4, 30, 15, 20, 0, 10);
        Boundary equatorArea3 = globeMap3.calculateEquator();
        assertEquals(new Boundary(new Vector2d(0, 0), new Vector2d(12, 0)), equatorArea3);

        GlobeMap globeMap4 = new GlobeMap(NORMAL_ANIMAL, 33, 3, 2, 50, 10, 10, 4, 30, 15, 20, 0, 10);
        Boundary equatorArea4 = globeMap4.calculateEquator();
        assertEquals(new Boundary(new Vector2d(0, 1), new Vector2d(27, 1)), equatorArea4);

        GlobeMap globeMap5 = new GlobeMap(NORMAL_ANIMAL, 1, 60, 2, 50, 10, 10, 4, 30, 15, 20, 0, 10);
        Boundary equatorArea5 = globeMap5.calculateEquator();
        assertEquals(new Boundary(new Vector2d(0, 24), new Vector2d(1, 35)), equatorArea5);

        GlobeMap globeMap6 = new GlobeMap(NORMAL_ANIMAL, 3, 33, 2, 50, 10, 10, 4, 30, 15, 20, 0, 10);
        Boundary equatorArea6 = globeMap6.calculateEquator();
        assertEquals(new Boundary(new Vector2d(0, 14), new Vector2d(2, 20)), equatorArea6);

        GlobeMap globeMap7 = new GlobeMap(NORMAL_ANIMAL, 20, 40, 2, 50, 10, 10, 4, 30, 15, 20, 0, 10);
        Boundary equatorArea7 = globeMap7.calculateEquator();
        assertEquals(new Boundary(new Vector2d(0, 16), new Vector2d(3, 24)), equatorArea7);

        GlobeMap globeMap8 = new GlobeMap(NORMAL_ANIMAL, 3, 17, 2, 50, 10, 10, 4, 30, 15, 20, 0, 10);
        Boundary equatorArea8 = globeMap8.calculateEquator();
        assertEquals(new Boundary(new Vector2d(0, 8), new Vector2d(1, 11)), equatorArea8);
    }


}