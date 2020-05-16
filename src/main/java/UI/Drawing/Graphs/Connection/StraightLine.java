package UI.Drawing.Graphs.Connection;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;


public class StraightLine implements Line {

    private final Parent parent;

    private EventHandler<? super MouseEvent> previousEventMoved = null;

    private static javafx.scene.shape.Line previous = null; // daca linia incepe aceasta va avea o valoare, altfel va fi null

    public StraightLine(Parent parent) {
        this.parent = parent;
    }

    @Override
    public Shape draw(StackPane stack) {
        if (previous == null) {
            javafx.scene.shape.Line line = new javafx.scene.shape.Line();

            line.setStartX(stack.getTranslateX() + stack.getWidth() / 2);
            line.setStartY(stack.getTranslateY() + stack.getHeight() / 2);

            line.startXProperty().bind(stack.translateXProperty().add(stack.getWidth() / 2));
            line.startYProperty().bind(stack.translateYProperty().add(stack.getHeight() / 2));

            line.setEndX(stack.getTranslateX() + stack.getWidth() / 2);
            line.setEndY(stack.getTranslateY() + stack.getHeight() / 2);
            line.setViewOrder(10);

            previousEventMoved = parent.getOnMouseMoved();
            parent.setOnMouseMoved(event -> {
                line.setEndX(event.getX());
                line.setEndY(event.getY());
            });
            previous = line;
            return line;
        } else {
            parent.setOnMouseMoved(previousEventMoved);

            previous.setEndX(stack.getTranslateX() + stack.getWidth() / 2);
            previous.setEndY(stack.getTranslateY() + stack.getHeight() / 2);

            previous.endXProperty().bind(stack.translateXProperty().add(stack.getWidth() / 2));
            previous.endYProperty().bind(stack.translateYProperty().add(stack.getHeight() / 2));

            previous = null;
            return null;
        }
    }

}
