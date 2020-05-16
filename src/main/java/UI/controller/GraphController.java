package UI.controller;

import UI.Controlls.MouseGestures;
import UI.Drawing.Graphs.Connection.StraightLine;
import UI.Drawing.Graphs.Points.GraphCircle;
import UI.Drawing.Graphs.Points.Point;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Shape;
import javafx.util.Pair;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GraphController extends Controller {
    public AnchorPane currentpane;
    public static List<Node> drawable = new ArrayList<>();  //.size numarul de noduri in ordine de la 0
    public static List<Pair<Integer, Integer>> conections = new ArrayList<>();  // conexiunile intre noduri

    boolean isDrawable = true;
    boolean isCircle = false;
    boolean isLine = false;

    boolean isLineConnected = false;

    //interface for diferent styles
    private Point point = new GraphCircle();
    private UI.Drawing.Graphs.Connection.Line line;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        line=new StraightLine(currentpane);
    }

    public void draw(MouseEvent mouseEvent) {   //todo: de facut o validare prin care daca se face aceeasi linie atunci sa nu se repete in lista

        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            showMenu(mouseEvent);
        }
        if (isDrawable && mouseEvent.getButton() == MouseButton.PRIMARY) {
            if (isCircle) {
                StackPane stack = drawWithGesture(mouseEvent.getX(), mouseEvent.getY());
                stack.setId(String.valueOf(drawable.size()));   //setam id-ul acelasi cu pozitia in lista pentru a putea fi gasit usor obiect
                drawable.add(stack);
                stack.setOnMouseClicked(event -> {
                    if(isLine || isLineConnected) {
                        StackPane source = (StackPane) event.getSource();
                        Shape shape = line.draw(source);
                        if (shape != null) {
                            AnchorPane pane = (AnchorPane) mouseEvent.getSource();
                            pane.getChildren().add(shape);
                            conections.add(new Pair<Integer, Integer>(Integer.valueOf(stack.getId()),-1));  //-1 pentru nu stim a doua pozitie
                            isLineConnected=true;
                        }else{
                            Pair<Integer, Integer> p =conections.remove(conections.size()-1);
                            conections.add(new Pair<>(p.getKey(),Integer.valueOf(stack.getId())));  // salvam perechea completa
                            isLineConnected=false;
                        }
                    }
                });
                AnchorPane pane = (AnchorPane) mouseEvent.getSource();
                pane.getChildren().add(stack);
            }
        }

    }

    private StackPane drawWithGesture(double x, double y) {
        StackPane stack = point.draw(x, y);
        MouseGestures gestures = new MouseGestures();
        gestures.makeDraggable(stack);
        return stack;
    }



    private void showMenu(MouseEvent mouseEvent) {
        final ContextMenu contextMenu = new ContextMenu();
        MenuItem circle = new MenuItem("Circle");
        MenuItem line = new MenuItem("Line");
        //MenuItem paste = new MenuItem("Paste");
        contextMenu.getItems().addAll(circle, line);
        circle.setOnAction(event -> isCircle = !isCircle);
        line.setOnAction(event -> isLine = !isLine);
        currentpane.setOnContextMenuRequested(event -> {
            contextMenu.show(currentpane, event.getScreenX(), event.getScreenY());
        });
    }





    public void resize() {
        currentpane.widthProperty().addListener((obs, oldVal, newVal) -> {
            drawable.forEach(x -> {
                x.setTranslateX(
                        x.getTranslateX() * newVal.doubleValue() / oldVal.doubleValue()
                );
            });
        });
        currentpane.heightProperty().addListener((obs, oldVal, newVal) -> {
            drawable.forEach(x -> {
                x.setTranslateY(
                        x.getTranslateY() * newVal.doubleValue() / oldVal.doubleValue()
                );
            });
        });
    }


    @Override
    public void load() {
        resize();
    }
}
