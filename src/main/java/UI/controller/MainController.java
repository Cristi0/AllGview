package UI.controller;

import Service.MainService;
import UI.configuration.Animations;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController extends Controller {
    public FontAwesomeIconView menu_opener;
    public VBox left_panel;

    public Label TextStatus;
    public ProgressBar ProgressBar;
    // public Button menu_opener;
   // public AnchorPane left_panel;
    private Animations animation = new Animations();

    private List<Node> drawable = new ArrayList<>();

    public MainController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ProgressBar.setVisible(false);
        TextStatus.setText("");
    }

    public void open_close(MouseEvent mouseEvent) {
       animation.slideOpenOrClosePanel(left_panel, menu_opener,150,40);
    }

    @Override
    public void load() {

    }


    public void goToDashboard(MouseEvent mouseEvent) {
        try {
            changeContent("Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void service(MouseEvent mouseEvent) {
        System.out.println("Easter egg!");
    }
}
