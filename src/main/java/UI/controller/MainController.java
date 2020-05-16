package UI.controller;

import UI.configuration.Animations;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController extends Controller {
    public FontAwesomeIconView menu_opener;
    public VBox left_panel;
   // public Button menu_opener;
   // public AnchorPane left_panel;
    private Animations animation = new Animations();

    private List<Node> drawable = new ArrayList<>();

    public MainController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
}