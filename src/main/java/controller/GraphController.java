package controller;

import Controlls.MouseGestures;
import javafx.beans.property.Property;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GraphController extends Controller {
    public AnchorPane currentpane;
    private List<Node> drawable = new ArrayList<>();

    boolean isDrawable =true;
    boolean isCircle = false;
    boolean isLine =false;

    boolean lineIscConnect=false;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void draw(MouseEvent mouseEvent) {
       if(mouseEvent.getButton()==MouseButton.SECONDARY){
          showMenu(mouseEvent);
       }
       if(isDrawable && mouseEvent.getButton() == MouseButton.PRIMARY ){
            if(isCircle){
                StackPane stack = drawCircle(mouseEvent);
                AnchorPane pane = (AnchorPane) mouseEvent.getSource();
                pane.getChildren().add(stack);
            }
       }

    }

    private void drawLineFromSource(MouseEvent event,AnchorPane pane) {
        Line line = new Line();
        Circle circle =(Circle) event.getSource();
        StackPane stack = (StackPane) circle.getParent();
        System.out.println("circle.getTranslateX() = " + stack.getTranslateX());
   //     System.out.println("circle.getCenterX() = " + stack.getCenterX());
        System.out.println("circle.getLayoutX() = " + stack.getLayoutX());
        System.out.println("circle.getScaleX() = " + stack.getScaleX());
        System.out.println("event.getX() = " + event.getX());
        System.out.println("event.getSceneX() = " + event.getSceneX());
        System.out.println("event.getScreenX() = " + event.getScreenX());
        line.setStartX(stack.getTranslateX()+stack.getWidth()/2);
        line.setStartY(stack.getTranslateY()+stack.getHeight()/2);

        line.startXProperty().bind( stack.translateXProperty().add(stack.getWidth()/2));
        line.startYProperty().bind( stack.translateYProperty().add(stack.getHeight()/2));
        line.setEndX(100);
        line.setEndY(100);
        line.setViewOrder(10);

        pane.getChildren().add(line);
       // stack.getChildren().add(0,line);
//        currentpane.setOnMouseMoved(event ->{
//            line.setEndX(event.getX());
//            line.setEndY(event.getY());
//            System.out.println("line??");
//        });
    }

    private void showMenu(MouseEvent mouseEvent) {
        final ContextMenu contextMenu = new ContextMenu();
        MenuItem circle = new MenuItem("Circle");
        MenuItem line = new MenuItem("Line");
        //MenuItem paste = new MenuItem("Paste");
        contextMenu.getItems().addAll(circle, line);
        circle.setOnAction(event -> isCircle=!isCircle);
        line.setOnAction(event -> isLine=!isLine);
        currentpane.setOnContextMenuRequested(event -> {
            contextMenu.show(currentpane,event.getScreenX(), event.getScreenY());
        });
    }

    public StackPane drawCircle(MouseEvent mouseEvent){
        int radius =10;
        StackPane stack = new StackPane();
        stack.setTranslateX(mouseEvent.getX());
        stack.setTranslateY(mouseEvent.getY());
        stack.setCenterShape(true);
        stack.setViewOrder(9);

        Circle circle = new Circle();
        circle.setCenterX(0.0);
        circle.setCenterY(0.0);

        circle.setStroke(Color.valueOf("#485460"));
        circle.setStrokeWidth(2);
        circle.setViewOrder(8);

        //circle.setFill(new LinearGradient(0,0,1,1,true, CycleMethod.NO_CYCLE,new Stop[]{new Stop(0,Color.BLACK),new Stop(1,Color.BLUE)}));
        circle.setFill(Color.WHITE);
        circle.setRadius(radius);
        circle.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {
            //((Node)event.getSource()).getOnMousePressed()
            System.out.println("click");
            ((Circle) event.getSource()).setFill(new Color(1,0,0,0.5));
            if(event.getButton()== MouseButton.SECONDARY){
                System.out.println("right click");
            }
            if(isLine){
                if(lineIscConnect){

                }else{
                    System.out.println("apel");
                    if(event.getSource() instanceof Circle){
                        System.out.println("apel circle");
                        drawLineFromSource(event,(AnchorPane) mouseEvent.getSource());
                    }

                }
            }
        });

        Text text = drawCircleNumber(circle);
        stack.getChildren().addAll(circle,text);//todo:
        drawable.add(stack);
        MouseGestures gestures =new MouseGestures();
        gestures.makeDraggable(stack);
        return stack;
    }
    public Text drawCircleNumber(Circle circle){
        Text text = new Text((drawable.size()+1)+"");
        text.translateXProperty().bindBidirectional(circle.translateXProperty());
        text.translateYProperty().bindBidirectional(circle.translateYProperty());
//        text.setOnMouseClicked(event -> {
//            System.out.println("click text");
//           // ((Circle) event.getSource()).setFill(new Color(1,0,0,0.5));
//            if(event.getButton()== MouseButton.SECONDARY){
//                System.out.println("right click");
//            }
//        });
        text.setViewOrder(7);
        return text;
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
