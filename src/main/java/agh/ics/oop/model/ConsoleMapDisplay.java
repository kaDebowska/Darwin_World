package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener {

    private int counter = 0;

    @Override
    public synchronized void onMapChange(WorldMap worldMap, String message) {
        counter++;
        System.out.println("\n----\n");
        System.out.printf("UUID: %s\n", worldMap.getId());
        System.out.println(Thread.currentThread());
        System.out.println(message);
        System.out.println(worldMap.toString());
        System.out.printf("There has been total of %d changes made\n", counter);
    }
}
