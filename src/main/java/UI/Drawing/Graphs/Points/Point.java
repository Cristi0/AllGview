package UI.Drawing.Graphs.Points;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public interface Point {

    /**
     * Deseneaza varful unui graf
     * @param x, valoarea de pe coordonata x
     * @param y, valoarea de pe coordonata y
     * @return StackPane, un obiect format din mai multe figuri suprapuse
     */
    public StackPane draw(double x, double y);
}
