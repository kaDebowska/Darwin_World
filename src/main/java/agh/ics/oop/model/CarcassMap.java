package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.List;

public class CarcassMap extends AbstractWorldMap{
    private List<DeadAnimal> deadAnimals;

    public CarcassMap(){
        super();
        this.deadAnimals = new ArrayList<>();
    }

    public void putPlants(){

    }

}
