package agh.ics.oop.model;

public class DeadAnimal {
    private int daysSinceDeath;
    private final Vector2d placeOfDeath;

    public DeadAnimal(Vector2d placeOfDeath){
        this.placeOfDeath = placeOfDeath;
        this.daysSinceDeath = 0;
    }

    public void incrementDaysSinceDeath() {
        this.daysSinceDeath++;
    }

    public int getDaysSinceDeath() {
        return daysSinceDeath;
    }

    public Vector2d getPlaceOfDeath() {
        return placeOfDeath;
    }
}
