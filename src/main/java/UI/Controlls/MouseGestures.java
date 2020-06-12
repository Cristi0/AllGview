package UI.Controlls;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class MouseGestures {

    class DragContext {
        double x;
        double y;
    }

    DragContext dragContext = new DragContext();

    /**
     * Seteaza un nod sa poata fie mutat cu mouse-ul
     * @param node
     */
    public void makeDraggable( Node node) {

        node.setOnMousePressed( onMousePressedEventHandler);
        node.setOnMouseDragged( onMouseDraggedEventHandler);
        node.setOnMouseReleased(onMouseReleasedEventHandler);

    }

    EventHandler<MouseEvent> onMousePressedEventHandler = event -> {


            Node node = (Node) (event.getSource());

            dragContext.x = node.getTranslateX() - event.getSceneX();
            dragContext.y = node.getTranslateY() - event.getSceneY();

    };

    EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {

            Node node = (Node) (event.getSource());

            node.setTranslateX( dragContext.x + event.getSceneX());
            node.setTranslateY( dragContext.y + event.getSceneY());


    };

    EventHandler<MouseEvent> onMouseReleasedEventHandler = event -> {
    };

}
