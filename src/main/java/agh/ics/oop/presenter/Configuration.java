package agh.ics.oop.presenter;

import java.io.Serializable;

public class Configuration implements Serializable {
    private int mapWidth;
    private int mapHeight;
    private int startAnimals;
    private int startPlants;

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getStartAnimals() {
        return startAnimals;
    }

    public int getStartPlants() {
        return startPlants;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public void setStartAnimals(int startAnimals) {
        this.startAnimals = startAnimals;
    }

    public void setStartPlants(int startPlants) {
        this.startPlants = startPlants;
    }
}
