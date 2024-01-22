package agh.ics.oop.model;

import agh.ics.oop.presenter.AnimalClickedEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;

public class WorldElementBox extends StackPane {
    private static final int BOX_SIZE = 30;

    public WorldElementBox(WorldElement element, String avHealthStr, Animal providedAnimal) {
        Rectangle box = new Rectangle(BOX_SIZE, BOX_SIZE);
        box.setOpacity(0.7);

        double avHealth = Double.parseDouble(avHealthStr.replace(',', '.'));

        Label label = null;

        if (element instanceof AnimalGroup) {
            Animal animal = ((AnimalGroup) element).getOrderedAnimals().get(0);
            double hue = 30;
            double saturation = 1;
            double brightness = Math.min(1, (double) animal.getHealth() / (2 * avHealth));

            if (animal.equals(providedAnimal)) {
                box.setFill(Color.RED);
            } else {
                box.setFill(Color.hsb(hue, saturation, brightness));
            }

            label = new Label(animal.toString());
            label.setStyle("-fx-text-fill: white;");

            box.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                box.setFill(Color.RED);
                box.fireEvent(new AnimalClickedEvent(animal));
            });

        } else {
            box.setFill(Color.GREEN);
        }

        this.getChildren().add(box);
        if (label != null) {
            this.getChildren().add(label);
        }
    }
}




