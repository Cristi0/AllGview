package UI.Drawing.Graphs.Points;

import UI.controller.GraphController;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class GraphCircle implements Point {
    int radius = 10;

    float red = 1;      //todo: de citit din fisiere de proprietati
    float green = 0;
    float blue = 0;
    float opacity = 0.5f;

    /**
     * Numarul cercului curent.
     * @return intreg
     */
    private int getNumberCircle(){
        return GraphController.drawable.size();
    }

    /**
     * Creeaza un stackPane si atribuie proprietatile necesare
     * @return Stackpane
     */
    private StackPane stackPaneProperties(double x, double y) {
        StackPane stack = new StackPane();
        stack.setTranslateX(x);
        stack.setTranslateY(y);
        stack.setCenterShape(true);
        stack.setViewOrder(9);
        return stack;
    }

    /**
     * Creeaza un cerc si atribuie proprietatile necesare
     * @return Circle
     */
    private Circle circleProperties() {
        Circle circle = new Circle();
        circle.setCenterX(0.0);
        circle.setCenterY(0.0);

        circle.setStroke(Color.valueOf("#485460"));
        circle.setStrokeWidth(2);
        circle.setViewOrder(8);

        //circle.setFill(new LinearGradient(0,0,1,1,true, CycleMethod.NO_CYCLE,new Stop[]{new Stop(0,Color.BLACK),new Stop(1,Color.BLUE)}));
        circle.setFill(Color.WHITE);
        circle.setRadius(radius);

        return circle;
    }

    /**
     * Crearea vizuala a numarului cercului
     * @param circle, pozitia cercului
     * @return Text, numarul cercului
     */
    public Text drawCircleNumber(Circle circle) {
        Text text = new Text((getNumberCircle() + 1) + "");
        text.translateXProperty().bindBidirectional(circle.translateXProperty());
        text.translateYProperty().bindBidirectional(circle.translateYProperty());

        text.setViewOrder(7);
        return text;
    }

    /**
     * Deseneaza nodul, un cerc, unde in interiorul acestuia se afla un numar
     * @param x, valoarea de pe coordonata x
     * @param y, valoarea de pe coordonata y
     * @return nodul
     */
    @Override
    public StackPane draw(double x, double y) {
        StackPane stack = stackPaneProperties(x, y);

        Circle circle = circleProperties();

        Text text = drawCircleNumber(circle);
        stack.getChildren().addAll(circle, text);
        return stack;
    }
}
