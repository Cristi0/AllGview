package UI.controller;

import Eroare.UnexpectedException;
import Service.MainService;
import UI.Controlls.MouseGestures;
import UI.Drawing.Graphs.Connection.StraightLine;
import UI.Drawing.Graphs.Points.GraphCircle;
import UI.Drawing.Graphs.Points.Point;
import UI.controller.Template.GraphTemplateCode;
import Utils.Compile;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.stream.Collectors;

public class GraphController extends Controller {
    public AnchorPane currentpane;
    public static List<Node> drawable = new ArrayList<>();  //.size numarul de noduri in ordine de la 0
    public static List<Node> lines = new ArrayList<>();
    public static List<Pair<Integer, Integer>> conections = new ArrayList<>();  // conexiunile intre noduri


    public Button StartAnimation;

    private List<Integer> order;

    public TextArea codeArea;

    public Label SpeedValue;
    public Slider SpeedIndicator;

    boolean isDrawable = true;
    boolean isCircle = false;
    boolean isLine = false;
    boolean isDelete = false;

    boolean isLineConnected = false;


    String className = "javademo";
    MainService srv = new MainService();

    private Integer template = -1;

    //interface for diferent styles
    private Point point = new GraphCircle();
    private UI.Drawing.Graphs.Connection.Line line;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        drawable.clear();
        conections.clear();
        lines.clear();
        currentpane.getChildren().clear();
        StartAnimation.setDisable(true);

        SpeedValue.textProperty().bind(SpeedIndicator.valueProperty().divide(20).asString("%.2f"));

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
                Compile.nodes.clear();
                Compile.edges.clear();
                for (int i = 0; i < drawable.size(); i++) {
                    Compile.nodes.add(i);
                }
                conections = transformLine();
                for (int i = 0; i < conections.size(); i++) {
                    ArrayList<Integer> arr = new ArrayList<>();
                    arr.add(conections.get(i).getKey());
                    arr.add(conections.get(i).getValue());
                    Compile.edges.add(arr);
                }
                System.out.println(Compile.nodes);
                System.out.println(Compile.edges);
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
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {

            drawCircles(mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getSource());

        }

    }

    private void drawCircles(double pozX, double pozY, Object Source) {
        if (isDrawable) {
            if (isCircle) {
                StackPane stack = drawWithGesture(pozX, pozY);
                stack.setId(String.valueOf(drawable.size()));   //setam id-ul acelasi cu pozitia in lista pentru a putea fi gasit usor obiect
                drawable.add(stack);
                stack.setOnMouseClicked(event -> {
                    if (isDelete) {
                        StackPane node = (StackPane) event.getSource();
                        drawable.remove(node);
                        lines.removeIf(x -> {
                            String[] s = x.getId().split(",");
                            Integer node1 = Integer.valueOf(s[0]);
                            Integer node2 = Integer.valueOf(s[1]);
                            if (node1.equals(Integer.valueOf(node.getId())) || node2.equals(Integer.valueOf(node.getId()))) {
                                Platform.runLater(() -> {
                                    currentpane.getChildren().remove(x);
                                });
                                return true;
                            }
                            return false;
                        });
                        Platform.runLater(() -> {
                            for (int i = Integer.valueOf(node.getId()); i < drawable.size(); i++) {
                                System.out.println(drawable.get(i).getId());
                                drawable.get(i).setId(String.valueOf(i));
                                System.out.println(drawable.get(i).getId());
                                ((Text) ((StackPane) drawable.get(i)).getChildren().get(1)).setText(String.valueOf(i + 1));
                                int finalI = i;
                                lines.stream().parallel().forEach(x -> x.setId(x.getId().replace(String.valueOf(finalI + 1), String.valueOf(finalI))));
                            }
                            currentpane.getChildren().remove(node);
                        });

                    } else {
                        drawLines(event.getSource(), event.getButton(), currentpane, stack);
                    }
                });
                AnchorPane pane = (AnchorPane) Source;
                pane.getChildren().add(stack);
            }
        }
    }

    private void drawLines(Object Source, MouseButton button, Object whereToDraw, StackPane stack) {


        if ((isLine || isLineConnected) && button == MouseButton.PRIMARY) {
            StackPane source = (StackPane) Source;
            Shape shape = line.draw(source);
            System.out.println(shape);
            if (shape != null) {
                AnchorPane pane = (AnchorPane) whereToDraw;
                pane.getChildren().add(shape);
                isLineConnected = true;
                shape.setId(stack.getId());
                System.out.println(shape.getId());
                lines.add(shape);
                shape.setOnMouseClicked(ev -> {
                    if (isDelete) {
                        Shape node = (Shape) ev.getSource();
                        lines.remove(node);
                        Platform.runLater(() -> {
                            currentpane.getChildren().remove(node);
                        });

                    }
                });
            } else {
                shape = (Shape) lines.get(lines.size() - 1);
                shape.setId(shape.getId() + "," + stack.getId());
                isLineConnected = false;
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
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().addAll(circle, line, new SeparatorMenuItem(), delete);
        circle.setOnAction(event -> {
            isCircle = !isCircle;
            isDelete = false;
        });
        line.setOnAction(event -> {
            isLine = !isLine;
            isDelete = false;
        });
        delete.setOnAction(x -> {
            isDelete = !isDelete;
            isLine = false;
            isCircle = false;
        });
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
                srv.saveLocalFile(nodes, transformLine(), codeArea.getText(), file);
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
            lines.clear();

            isDelete = false;
            isDrawable = true;
            isCircle = true;
            isLine = false;
            for (Pair<Double, Double> pair : ((List<Pair<Double, Double>>) data[0])) {
                drawCircles(pair.getKey(), pair.getValue(), currentpane);
            }
            isCircle = false;

            codeArea.setText((String) data[2]);
            isLine = false;

            Platform.runLater(() -> {
                isDrawable = true;
                isLine = true;
                for (Pair<Integer, Integer> pair : ((List<Pair<Integer, Integer>>) data[1])) {
                    System.out.println(drawable);
                    StackPane stack = (StackPane) drawable.get(pair.getKey());
                    StackPane source = (StackPane) drawable.get(pair.getValue());
                    drawLines(source, MouseButton.PRIMARY, currentpane, stack);
                    drawLines(stack, MouseButton.PRIMARY, currentpane, source);
                }
                isLine = false;
            });

        }
    }

    private List<Pair<Integer, Integer>> transformLine() {
        List<Pair<Integer, Integer>> connections = lines.stream().map(x -> {
            String[] s = x.getId().split(",");
            Integer first = Integer.valueOf(s[0]);
            Integer second = Integer.valueOf(s[1]);
            return new Pair<Integer, Integer>(first, second);
        }).collect(Collectors.toList());
        return connections;
    }
}
