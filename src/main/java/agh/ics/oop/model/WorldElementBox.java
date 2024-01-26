package agh.ics.oop.model;

import agh.ics.oop.presenter.AnimalClickedEvent;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;

public class WorldElementBox extends StackPane {
    private static final int BOX_SIZE = 30;
    private static Rectangle lastClickedBox = null; // Add this line

    public WorldElementBox(WorldElement element, String avHealthStr, Animal providedAnimal, boolean isFertilePosition) {
        Rectangle box = new Rectangle(BOX_SIZE, BOX_SIZE);
        box.setOpacity(0.7);

        double avHealth = Double.parseDouble(avHealthStr.replace(',', '.'));

        if(isFertilePosition) {
            DropShadow dropShadow = new DropShadow();
            dropShadow.setColor(Color.BLUE);
            box.setEffect(dropShadow);

//            box.setStroke(Color.BLUE);
        }

        Label label = null;
        Color normalColor;

        if (element instanceof AnimalGroup) {
            Animal animal = ((AnimalGroup) element).getOrderedAnimals().get(0);
            double hue = 30;
            double saturation = 1;
            double brightness = Math.min(1, (double) animal.getHealth() / (2 * avHealth));

            normalColor = Color.hsb(hue, saturation, brightness);

            if (animal.equals(providedAnimal)) {
                box.setFill(Color.RED);
            } else {
                box.setFill(normalColor);
            }

            label = new Label(animal.toString());
            label.setStyle("-fx-text-fill: white;");
            label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                if (lastClickedBox != null) {
                    lastClickedBox.setFill(normalColor);
                }
                box.setFill(Color.RED);
                lastClickedBox = box;
                box.fireEvent(new AnimalClickedEvent(animal));
            });

            box.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                if (lastClickedBox != null) {
                    lastClickedBox.setFill(normalColor);
                }
                box.setFill(Color.RED);
                lastClickedBox = box;
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

    public WorldElementBox() {
        Rectangle box = new Rectangle(BOX_SIZE, BOX_SIZE);
        box.setOpacity(0.7);

        box.setFill(Color.WHITE);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLUE);
        box.setEffect(dropShadow);

        this.getChildren().add(box);

    }
}





