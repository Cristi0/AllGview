package UI.controller;

import Eroare.CompileException;
import Eroare.UnexpectedException;
import Service.MainService;
import UI.Controlls.MouseGestures;
import UI.Drawing.Graphs.Connection.StraightLine;
import UI.Drawing.Graphs.Points.GraphCircle;
import UI.Drawing.Graphs.Points.Point;
import UI.Drawing.Graphs.Weights.Weight;
import UI.controller.Template.GraphTemplateCode;
import Utils.Compile;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class GraphController extends Controller {
    public AnchorPane currentpane;
    public static List<Node> drawable = new ArrayList<>();  //.size numarul de noduri in ordine de la 0
    public static List<Node> lines = new ArrayList<>();
    public static List<Node> weight = new ArrayList<>(); //pondere

    public static List<Pair<Integer, Integer>> conections = new ArrayList<>();  // conexiunile intre noduri


    public Button StartAnimation;
    public Button SaveAsButton;
    public Button LoadFromButton;

    private List<Integer> order;

    public TextArea codeArea;
    public TextArea Console;

    public Label SpeedValue;
    public Slider SpeedIndicator;

    boolean isDrawable = true;
    Property<Boolean> isCircle = new SimpleBooleanProperty(false);
    Property<Boolean> isLine = new SimpleBooleanProperty(false);
    Property<Boolean> isDelete = new SimpleBooleanProperty(false);
    Property<Boolean> hasWeight = new SimpleBooleanProperty(false);

    boolean isLineConnected = false;


    MainService srv = MainService.getMainService();

    private Integer template = -1;

    //interface for diferent styles
    private Point point = new GraphCircle();
    private UI.Drawing.Graphs.Connection.Line line;

    /**
     * Curata poti parametrii
     */
    private void clearData() {
        Console.setText("");
        drawable.clear();
        conections.clear();
        lines.clear();
        weight.clear();
        currentpane.getChildren().clear();
        StartAnimation.setDisable(true);
    }

    private void hintButton(Button button, String text){
        button.setOnMouseEntered((event)->{
            MainController.Hint.setText("Hint: "+text);
        });
        button.setOnMouseExited(event -> {
            MainController.Hint.setText(MainController.HintString);
        });
    }
    /**
     * Initializeaza datele si seteaza meniurile pentru zonele din aplicatie
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clearData();

        SpeedValue.textProperty().bind(SpeedIndicator.valueProperty().divide(20).asString("%.2f"));

        hintButton(StartAnimation,"View the vizual result of the code execution at the set speed.");
        hintButton(SaveAsButton,"Save all code, nodes, connections and weights in a local file.");
        hintButton(LoadFromButton,"Load code, nodes, connections and weights from a local file.");



        Platform.runLater(() -> {

            try {
                codeArea.setText(GraphTemplateCode.getBy(template));
            } catch (UnexpectedException e) {
                throw new RuntimeException(e);  //todo: manage error into dialog
            }

            line = new StraightLine(currentpane);
            ContextMenu menu = new ContextMenu();
            menu.getItems().addAll(Menu.createDefaultMenuItems(codeArea));
            MenuItem execute = new MenuItem("Execute");

            execute.setOnAction((event) -> {
                executeCode();
            });

            menu.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, e -> {
                Menu.getHintsForDefaultMenuItems(e.getY());
                if (185 < e.getY() && e.getY() < 211) {
                    Platform.runLater(() -> {
                        MainController.Hint.setText("Hint: Execute source code. If it has compile errors those will be shown in \"Console\".");
                    });
                }
            });
            menu.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, e -> {
                MainController.Hint.setText(MainController.HintString);
            });
            menu.getItems().add(execute);
            codeArea.setContextMenu(menu);



            ContextMenu menu1 = new ContextMenu();      //codeAreaMenu
            menu1.getItems().addAll(Menu.createDefaultMenuItems(Console));

            menu1.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, e -> {
                Menu.getHintsForDefaultMenuItems(e.getY());
            });
            menu1.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, e -> {
                MainController.Hint.setText(MainController.HintString);
            });
            Console.setContextMenu(menu1);


            showMenu();

        });

    }

    /**
     * In cazul in care se apasa pe Execute se va compila codul si se va afisa progresul
     */
    public void executeCode() {
        addGraphParametersForRuntimeExecution();
        setProgessBarAndStatus();
    }

    /**
     * Adauga parametrii necesari pentru executia codului sursa
     */
    public void addGraphParametersForRuntimeExecution() {
        Compile.nodes.clear();
        Compile.edges.clear();
        Compile.weight.clear();
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
        if (hasWeight.getValue()) {
            for (int i = 0; i < weight.size(); i++) {
                Compile.weight.add(Double.parseDouble(((Text) weight.get(i)).getText()));
            }
        } else {
            Compile.weight.clear();
        }
    }

    private CompletableFuture futureCompile = null; //necesar pentru oprirea vechiului proces

    /**
     * Porneste compilarea codului si seteaza o afisare a progresului.
     */
    public void setProgessBarAndStatus() {
        MainController mc = (MainController) Creator.Creator;
        mc.ProgressBar.setVisible(true);
        mc.ProgressBar.progressProperty().bind(Compile.CompileProgressProperty.divide(Compile.NumberOfCompileSteps));
        mc.TextStatus.setText("Compiling");
        Platform.runLater(() -> {
            if (futureCompile != null) {
                System.err.println("CANCEL");
                futureCompile.completeOnTimeout(null, 0, TimeUnit.SECONDS);
            }

            futureCompile = CompletableFuture.supplyAsync(() -> {
                try {
                    List<Integer> l = Compile.runCode(codeArea.getText());
                    Console.setText(Compile.getWarnings() + "\nSucces");
                    return l;
                } catch (CompileException e) {
                    Console.setText(e.getMessage());
                }
                return null;
            }).whenComplete((x, y) -> {
                order = x;
                StartAnimation.setDisable(false);
                Platform.runLater(() -> {
                    mc.ProgressBar.setVisible(false);
                    mc.TextStatus.setText("Done");
                });
                futureCompile = null;
            });
        });
    }

    /**
     * Deseneaza noduri la pozitia mouse-ului
     *
     * @param mouseEvent clickul pe un pane al unui mouse
     */
    public void draw(MouseEvent mouseEvent) {   //todo: de facut o validare prin care daca se face aceeasi linie atunci sa nu se repete in lista
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            drawCircles(mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getSource());
        }
    }

    /**
     * Deseneaza nodurile la pozitiile x si y intr-un pane
     *
     * @param pozX   pozitie pe orizontala
     * @param pozY   pozitia pe verticala
     * @param Source Pane
     */
    private void drawCircles(double pozX, double pozY, Object Source) {
        if (isDrawable) {
            if (isCircle.getValue()) {
                StackPane stack = drawWithGesture(pozX, pozY);
                stack.setId(String.valueOf(drawable.size()));   //setam id-ul acelasi cu pozitia in lista pentru a putea fi gasit usor obiect
                drawable.add(stack);
                stack.setOnMouseClicked(event -> {
                    setCircleEvent(event, stack);
                });
                AnchorPane pane = (AnchorPane) Source;
                pane.getChildren().add(stack);
            }
        }
    }

    /**
     * In cazul unui clcik pe nod se va alege actiunea necesara in functie de optiuniule alese
     * Daca isDelete este true atunci se va sterege nodul
     * Altfel se va crea un nod nou
     *
     * @param event      clickul care a provocat acest eveniment
     * @param thisCircle nodul pentru care se executa actiunea
     */
    private void setCircleEvent(MouseEvent event, StackPane thisCircle) {
        if (isDelete.getValue()) {
            deleteNodeAction((StackPane) event.getSource());

        } else {
            drawLines(event.getSource(), event.getButton(), currentpane, thisCircle.getId());
        }
    }

    /**
     * Sterge nodul curent.
     * Daca nodul are conexiuni cu alte noduri, acestea vor fi sterse
     *
     * @param node nodul ce urmeaza a fi sters
     */
    private void deleteNodeAction(StackPane node) {
        drawable.remove(node);      //remove from list
        removeAllLinesAndWeightForNode(node);
        Platform.runLater(() -> {       //remove visualy node and all connected lines to it
            StartAnimation.setDisable(true);
            for (int i = Integer.valueOf(node.getId()); i < drawable.size(); i++) {
                drawable.get(i).setId(String.valueOf(i));
                ((Text) ((StackPane) drawable.get(i)).getChildren().get(1)).setText(String.valueOf(i + 1));
                int finalI = i;
                lines.stream().forEach(x -> x.setId(x.getId().replace(String.valueOf(finalI + 1), String.valueOf(finalI))));
            }
            currentpane.getChildren().remove(node);
        });
    }


    /**
     * Sterge toate liniile asociate unui nod si ponderile asociate acestor linii
     *
     * @param node, nodul
     */
    private void removeAllLinesAndWeightForNode(Node node) {
        for (int i = lines.size() - 1; i >= 0; i--) {
            String[] s = lines.get(i).getId().split(",");
            Integer node1 = Integer.valueOf(s[0]);
            Integer node2 = Integer.valueOf(s[1]);
            if (node1.equals(Integer.valueOf(node.getId())) || node2.equals(Integer.valueOf(node.getId()))) {
                if (weight.size() > i) {
                    Node n = GraphController.weight.remove(i);
                    if (hasWeight.getValue()) {
                        Platform.runLater(() -> {
                            currentpane.getChildren().remove(n);
                        });
                    }
                }
                Node lineNode = lines.get(i);
                lines.remove(i);
                Platform.runLater(() -> {
                    currentpane.getChildren().remove(lineNode);
                });
            }
        }
    }

    /**
     * Deseneaz o linie care trece de la un nod la pozitia mouse-ului.
     * Daca a fost deja selecta un nod inainte actunci sfarsitul liniei va fa la nodul curent.
     *
     * @param Source      Stackpane-ul de la care se deseneaza linia
     * @param button      tipul de buton, de la mouse, apasat
     * @param whereToDraw Pane, locatia unde se va desena
     * @param idSource    id-ul nodului sursa
     */
    private void drawLines(Object Source, MouseButton button, Object whereToDraw, String idSource) {
        if ((isLine.getValue() || isLineConnected) && button == MouseButton.PRIMARY) {
            StackPane source = (StackPane) Source;
            Shape shape = line.draw(source);
            if (shape != null) {
                AnchorPane pane = (AnchorPane) whereToDraw;
                pane.getChildren().add(shape);
                isLineConnected = true;
                shape.setId(idSource);
                lines.add(shape);
                if (hasWeight.getValue()) {
                    Text t = new Weight().createWeight((Line) shape, currentpane);
                    GraphController.weight.add(t);
                    Platform.runLater(() -> {
                        currentpane.getChildren().add(t);
                    });

                }
                shape.setOnMouseClicked(ev -> {
                    if (isDelete.getValue()) {
                        Platform.runLater(() -> {
                            StartAnimation.setDisable(true);
                        });
                        Shape node = (Shape) ev.getSource();
                        int idx = lines.indexOf(node);
                        lines.remove(node);
                        if (weight.size() > idx) {
                            Node weight = GraphController.weight.remove(idx);
                            if (hasWeight.getValue()) {
                                Platform.runLater(() -> {
                                    currentpane.getChildren().remove(weight);
                                });
                            }
                        }
                        Platform.runLater(() -> {
                            currentpane.getChildren().remove(node);
                        });

                    }
                });
            } else {
                shape = (Shape) lines.get(lines.size() - 1);
                shape.setId(shape.getId() + "," + idSource);
                isLineConnected = false;
            }
        }

    }

    /**
     * Deseneaza un nod, avand proprietatea de a se putea misca dand click pe el, tinand apasat si miscand mouse-ul
     *
     * @param x pozitia pe verticala pentru crearea nodului
     * @param y pozitia pe orizontala
     * @return nodul creat
     */
    private StackPane drawWithGesture(double x, double y) {
        StackPane stack = point.draw(x, y);
        MouseGestures gestures = new MouseGestures();
        gestures.makeDraggable(stack);
        return stack;
    }

    /**
     * Creaza meniul pentru a seta parametrii pentru crearea unui nod sau linie, respectiv stergerea acestora si afisarea ponderilor
     */
    private void showMenu() {
        final ContextMenu contextMenu = new ContextMenu();
        CheckMenuItem circle = new CheckMenuItem("Circle");
        CheckMenuItem line = new CheckMenuItem("Line");
        CheckMenuItem delete = new CheckMenuItem("Delete");
        CheckMenuItem weight = new CheckMenuItem("Show Weights");

        contextMenu.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, e -> {
            if (0 < e.getY() && e.getY() < 33) {
                Platform.runLater(() -> {
                    MainController.Hint.setText("Hint: Create new nodes on click. Press it again to stop it.");
                });
            }
            if (32 < e.getY() && e.getY() < 60) {
                Platform.runLater(() -> {
                    MainController.Hint.setText("Hint: Create new connections between nodes when clicked on node. To finish line click on another node. Press it again to stop it.");
                });
            }
            if (60 < e.getY() && e.getY() < 88) {
                Platform.runLater(() -> {
                    MainController.Hint.setText("Hint: Delete nodes or connections when clicked on them. Press it again to stop it. Note: When you delete a node, all his connections are also deleted. ");
                });
            }
            if (87 < e.getY() && e.getY() < 120) {
                Platform.runLater(() -> {
                    MainController.Hint.setText("Hint: Show weights on connections. Press again to hide them.");
                });
            }
        });
        contextMenu.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, e -> {
            Platform.runLater(() -> {
                MainController.Hint.setText(MainController.HintString);
            });
        });
        contextMenu.getItems().addAll(circle, line, new SeparatorMenuItem(), delete, new SeparatorMenuItem(), weight);
        circle.setOnAction(event -> {
            isCircle.setValue(!isCircle.getValue());
            isDelete.setValue(false);
            Platform.runLater(() -> {
                circle.setSelected(isCircle.getValue());
                delete.setSelected(isDelete.getValue());
            });

        });
        line.setOnAction(event -> {
            isLine.setValue(!isLine.getValue());
            isDelete.setValue(false);
            Platform.runLater(() -> {
                line.setSelected(isLine.getValue());
                delete.setSelected(isDelete.getValue());
            });
        });
        delete.setOnAction(x -> {
            isDelete.setValue(!isDelete.getValue());
            isLine.setValue(false);
            isCircle.setValue(false);
            Platform.runLater(() -> {
                circle.setSelected(isCircle.getValue());
                line.setSelected(isLine.getValue());
                delete.setSelected(isDelete.getValue());
            });
        });

        weight.setOnAction(x -> {
            hasWeight.setValue(weight.isSelected());
            if (hasWeight.getValue()) {
                Weight w = new Weight();
                for (int i = GraphController.weight.size(); i < GraphController.lines.size(); i++) {
                    Text t = w.createWeight((Line) GraphController.lines.get(i), currentpane);
                    GraphController.weight.add(t);
                }
                currentpane.getChildren().addAll(GraphController.weight);
            } else {
                currentpane.getChildren().removeAll(GraphController.weight);
            }
        });
        currentpane.setOnContextMenuRequested(event -> {
            contextMenu.show(currentpane, event.getScreenX(), event.getScreenY());
        });
    }


    /**
     * La modificarea dimensiunii aplicatiei, se modifica si continutul obiectelor desenat la aceleasi rapoarte.
     * Nu se scaleaza
     */
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

    /**
     * Setarea sablonului
     *
     * @param template
     */
    public void setTemplate(Integer template) {
        this.template = template;
    }

    private Thread animation = null;

    /**
     * Pornirea animatiei de parcurgere a nodorilor
     * In caz de apelare multipla se opreste ultimul apel
     *
     * @param event
     */
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

    /**
     * Salveaza in fisier datele despre locatia nodurilo, conexiunile intre noduri, ponderile daca exista si a codului sursa.
     * Se deschide fileManagaer al sistemului de operarea.
     *
     * @param event
     */
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
                srv.saveLocalFile(nodes, transformLine(), weight.stream().map(x -> Double.valueOf(((Text) x).getText())).collect(Collectors.toList()), codeArea.getText(), file);
            }).start();

        }
    }

    /**
     * Incarca din fisier datele despre locatia nodurilo, conexiunile intre noduri, ponderile daca exista si a codului sursa.
     * Se deschide fileManagaer al sistemului de operarea.
     *
     * @param event
     */
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
            weight.clear();

            isDelete.setValue(false);
            isDrawable = true;
            isCircle.setValue(true);
            isLine.setValue(false);
            for (Pair<Double, Double> pair : ((List<Pair<Double, Double>>) data[0])) {
                drawCircles(pair.getKey(), pair.getValue(), currentpane);
            }
            isCircle.setValue(false);

            codeArea.setText((String) data[3]);
            isLine.setValue(false);

            Platform.runLater(() -> {
                isDrawable = true;
                isLine.setValue(true);
                hasWeight.setValue(false);
                for (Pair<Integer, Integer> pair : ((List<Pair<Integer, Integer>>) data[1])) {
                    StackPane stack = (StackPane) drawable.get(pair.getKey());
                    StackPane source = (StackPane) drawable.get(pair.getValue());
                    drawLines(source, MouseButton.PRIMARY, currentpane, source.getId());
                    drawLines(stack, MouseButton.PRIMARY, currentpane, stack.getId());
                }
                // Platform.runLater();
                ((List<Double>) data[2]).size();
                for (int i = 0; i < ((List<Double>) data[2]).size(); i++) {
                    Text t = new Weight().createWeight((Line) lines.get(i), currentpane);
                    t.setText(String.valueOf(((List<Double>) data[2]).get(i)));
                    GraphController.weight.add(t);
                    // System.out.println(GraphController.weight);
                }

                isLine.setValue(false);
            });

        }
    }

    /**
     * Transorma din Linii in lista de conexiuni intre noduri
     *
     * @return lista cu conexiunilr intre noduri
     */
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
