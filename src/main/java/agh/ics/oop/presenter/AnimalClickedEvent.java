package agh.ics.oop.presenter;

import agh.ics.oop.model.Animal;
import javafx.event.Event;
import javafx.event.EventType;

public class AnimalClickedEvent extends Event {
    public static final EventType<AnimalClickedEvent> ANIMAL_CLICKED_EVENT_TYPE = new EventType<>(ANY, "ANIMAL_CLICKED");

    private final Animal animal;

    public AnimalClickedEvent(Animal animal) {
        super(ANIMAL_CLICKED_EVENT_TYPE);
        this.animal = animal;
    }

    public Animal getAnimal() {
        return animal;
    }
}
