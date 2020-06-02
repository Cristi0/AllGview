package UI.controller;

import Eroare.UnexpectedException;
import Service.MainService;
import UI.Controlls.MouseGestures;
import UI.Drawing.Graphs.Connection.StraightLine;
import UI.Drawing.Graphs.Points.GraphCircle;
import UI.Drawing.Graphs.Points.Point;
import UI.controller.Template.GraphTemplateCode;
import Utils.Compile;
import com.sun.javafx.collections.ObservableIntegerArrayImpl;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class GraphController extends Controller {
    public AnchorPane currentpane;
    public static List<Node> drawable = new ArrayList<>();  //.size numarul de noduri in ordine de la 0
    public static List<Pair<Integer, Integer>> conections = new ArrayList<>();  // conexiunile intre noduri

    public Button StartAnimation;

    private List<Integer> order;

    public TextArea codeArea;

    public Label SpeedValue;
    public Slider SpeedIndicator;

    boolean isDrawable = true;
    boolean isCircle = false;
    boolean isLine = false;

    boolean isLineConnected = false;


    String className = "javademo";
    MainService srv = new MainService();

    private Integer template = -1;

    //interface for diferent styles
    private Point point = new GraphCircle();
    private UI.Drawing.Graphs.Connection.Line line;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        StartAnimation.setDisable(true);

        SpeedValue.textProperty().bind(SpeedIndicator.valueProperty().divide(20).asString("%.2f"));


//        "import java.util.ArrayList;\n" +
//                "import Service.MainService;\n\n" +
//                "public class " + className + " {\n\n" +
//                "   public static int abc(){\n" +
//                "       return 231;\n" +
//                "   }\n\n" +
//                "   public static ArrayList<Integer> run() {\n" +
//                "       ArrayList<Integer> a =new ArrayList<>();\n" +
//                "       a.add(1);\n" +
//                "       a.add(2);\n" +
//                "       a.add(abc());\n" +
//                "       MainService s =new MainService();\n" +
//                "       a.add(s.srv());\n" +
//                "       return a;\n" +
//                "    }\n" +
//                "}"
        Platform.runLater(() -> {

            try {
                codeArea.setText(GraphTemplateCode.getBy(template));
            } catch (UnexpectedException e) {
                throw new RuntimeException(e);
            }

            line = new StraightLine(currentpane);
            ContextMenu menu = new ContextMenu();
            menu.getItems().addAll(Menu.createDefaultMenuItems(codeArea));
            MenuItem execute = new MenuItem("Execute");

            execute.setOnAction((event) -> {
                MainController mc = (MainController) Creator.Creator;
                mc.ProgressBar.setVisible(true);
                mc.ProgressBar.progressProperty().bind(Compile.CompileProgressProperty.divide(Compile.NumberOfCompileSteps));
                mc.TextStatus.setText("Compiling");
                Platform.runLater(() -> {
                    srv.run(codeArea.getText()).whenComplete((x, y) -> {
                        order = x;
                        StartAnimation.setDisable(false);
                        Platform.runLater(() -> {
                            mc.ProgressBar.setVisible(false);
                            mc.TextStatus.setText("Done");
                        });

                    });

                });

            });
            menu.getItems().add(menu.getItems().size() - 2, execute);
            menu.getItems().add(execute);
            codeArea.setContextMenu(menu);

            //^^^ todo: de facut validare pt codul sursa + separare pe functii
            showMenu();

        });

    }

    public void draw(MouseEvent mouseEvent) {   //todo: de facut o validare prin care daca se face aceeasi linie atunci sa nu se repete in lista

        if (isDrawable && mouseEvent.getButton() == MouseButton.PRIMARY) {
            if (isCircle) {
                StackPane stack = drawWithGesture(mouseEvent.getX(), mouseEvent.getY());
                stack.setId(String.valueOf(drawable.size()));   //setam id-ul acelasi cu pozitia in lista pentru a putea fi gasit usor obiect
                drawable.add(stack);
                stack.setOnMouseClicked(event -> {
                    if ((isLine || isLineConnected) && event.getButton() == MouseButton.PRIMARY) {
                        StackPane source = (StackPane) event.getSource();
                        Shape shape = line.draw(source);
                        if (shape != null) {
                            AnchorPane pane = (AnchorPane) mouseEvent.getSource();
                            pane.getChildren().add(shape);
                            conections.add(new Pair<Integer, Integer>(Integer.valueOf(stack.getId()), -1));  //-1 pentru nu stim a doua pozitie
                            isLineConnected = true;
                        } else {
                            Pair<Integer, Integer> p = conections.remove(conections.size() - 1);
                            conections.add(new Pair<>(p.getKey(), Integer.valueOf(stack.getId())));  // salvam perechea completa
                            isLineConnected = false;
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

    private void showMenu() {
        final ContextMenu contextMenu = new ContextMenu();
        MenuItem circle = new MenuItem("Circle");
        MenuItem line = new MenuItem("Line");
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

    public void setTemplate(Integer template) {
        this.template = template;
    }

    private Thread animation = null;

    public void startAnimation(ActionEvent event) {
        Platform.runLater(() -> {
            if (animation != null) {
                animation.interrupt();
            }
            animation = new Thread(() -> {
                drawable.forEach(x -> ((Circle) ((StackPane) x).getChildren().get(0)).setFill(Color.WHITE));
                long speed = ((Double) (SpeedIndicator.getValue() * 50)).longValue();
                try {
                    Circle previous = null;
                    if (order.size() > 0) {
                        previous = ((Circle) ((StackPane) drawable.get(order.get(0))).getChildren().get(0));
                        previous.setFill(Color.color(0.6, 0.8, 0.6));
                        //Thread.sleep(speed);
                    }
                    for (int i = 0; i < order.size(); i++) {
                        previous.setFill(Color.color(0.7, 0.7, 0.9));
                        previous = ((Circle) ((StackPane) drawable.get(order.get(i))).getChildren().get(0));
                        previous.setFill(Color.color(0.6, 0.8, 0.6));
                        Thread.sleep(speed);
                    }
                    previous.setFill(Color.color(0.7, 0.7, 0.9));
                } catch (InterruptedException e) {
                    System.out.println("Thread intrerupt de user"); //todo: logs
                }
                animation = null;
            });
            animation.start();


        });
    }

    public void SaveFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("AllGView files (*.agv)", "*.agv");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            new Thread(() -> {
                List nodes = drawable.stream().map(x -> new Pair(x.getTranslateX(), x.getTranslateY())).collect(Collectors.toList());
                srv.saveLocalFile(nodes, conections, codeArea.getText(), file);
            }).start();

        }
    }

    public void LoadFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("AllGView files (*.agv)", "*.agv");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showOpenDialog(primaryStage);

        if (file != null) {
            //new Thread(() -> {
            Object[] data = srv.loadLocalFile(file);
            currentpane.getChildren().clear();
            drawable.clear();
            conections.clear();
            for (Pair<Double, Double> pair : ((List<Pair<Double, Double>>) data[0])) {
                StackPane stack = drawWithGesture(pair.getKey(), pair.getValue());
                stack.setId(String.valueOf(drawable.size()));   //setam id-ul acelasi cu pozitia in lista pentru a putea fi gasit usor obiect
                drawable.add(stack);
                stack.setOnMouseClicked(ev -> {
                    if ((isLine || isLineConnected) && ev.getButton() == MouseButton.PRIMARY) {
                        StackPane source = (StackPane) ev.getSource();
                        Shape shape = line.draw(source);
                        if (shape != null) {
                            AnchorPane pane = currentpane;
                            pane.getChildren().add(shape);
                            conections.add(new Pair<Integer, Integer>(Integer.valueOf(stack.getId()), -1));  //-1 pentru nu stim a doua pozitie
                            isLineConnected = true;
                        } else {
                            Pair<Integer, Integer> p = conections.remove(conections.size() - 1);
                            conections.add(new Pair<>(p.getKey(), Integer.valueOf(stack.getId())));  // salvam perechea completa
                            isLineConnected = false;
                        }
                    }
                });
                AnchorPane pane = currentpane;
                pane.getChildren().add(stack);
            }
            Platform.runLater(()->{
                for (Pair<Integer, Integer> pair : ((List<Pair<Integer, Integer>>) data[1])) {
                    StackPane stack = (StackPane) drawable.get(pair.getKey());
                    StackPane source = (StackPane) drawable.get(pair.getValue());
                    Shape shape = line.draw(stack);
                    line.draw(source);

                    AnchorPane pane = currentpane;
                    pane.getChildren().add(shape);
                    conections.add(new Pair<Integer, Integer>(Integer.valueOf(stack.getId()), Integer.valueOf(source.getId())));  // salvam perechea completa

                }
            });
            codeArea.setText((String) data[2]);


            //  }).start();

        }
    }
}
