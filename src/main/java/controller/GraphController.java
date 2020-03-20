package controller;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GraphController extends Controller {
    public AnchorPane currentpane;
    private List<Node> drawable = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void drawCircle(MouseEvent mouseEvent) {
        Circle circle = new Circle();
        circle.setCenterX(0.0);
        circle.setCenterY(0.0);
        circle.setTranslateX(mouseEvent.getX());
        circle.setTranslateY(mouseEvent.getY());
        circle.setRadius(10);
        drawable.add(circle);
        AnchorPane pane = (AnchorPane) mouseEvent.getSource();

        pane.getChildren().add(circle);
    }

    public void resize(){
        currentpane.widthProperty().addListener((obs,oldVal,newVal)->{
            drawable.forEach(x->{
                x.setTranslateX(
                        x.getTranslateX()*newVal.doubleValue()/oldVal.doubleValue()
                );
               // x.setScaleX(x.getScaleX()*newVal.doubleValue()/oldVal.doubleValue());
            });
        });
        currentpane.heightProperty().addListener((obs,oldVal,newVal)->{
            drawable.forEach(x->{
                x.setTranslateY(
                        x.getTranslateY()*newVal.doubleValue()/oldVal.doubleValue()
                );
               // x.setScaleY(x.getScaleY()*newVal.doubleValue()/oldVal.doubleValue());
            });
        });
    }


    @Override
    public void load() {
        resize();
    }
}
