package UI.controller;

import Service.MainService;
import UI.configuration.Animations;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.css.Style;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

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

    public FontAwesomeIconView menuUserOpener;
    public Label HintIndicator;

    public static Label Hint =null;
    public static final String HintString = "Hint: Hover on an object in order to tell what it can do.";
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
        Hint=HintIndicator;
        HintIndicator.setWrapText(true);
    }

    /**
     * Deschide sau inchide meniul din lateral
     * @param mouseEvent
     */
    public void open_close(MouseEvent mouseEvent) {
        animation.slideOpenOrClosePanel(left_panel, menu_opener, 150, 40);
    }

    @Override
    public void load() {

    }


    /**
     * Modifica continului din centru aplicatie la dashboard.
     * @param mouseEvent
     */
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

    private ContextMenu cn;

    /**
     * Afiseaza meniul userului
     * @param mouseEvent
     */
    public void showUserMenu(MouseEvent mouseEvent) {
        if (cn == null) {
            MenuItem account = new MenuItem("Account");
            MenuItem settings = new MenuItem("Settings");
            MenuItem signOut = new MenuItem("Sign Out");
            String style="-fx-font: serif; -fx-font-size: 12";
            account.setStyle(style);
            settings.setStyle(style);
            signOut.setStyle(style);
            ContextMenu menu = new ContextMenu();
            menu.getItems().addAll(account, settings,new SeparatorMenuItem(), signOut);
            menu.show(menuUserOpener, mouseEvent.getScreenX() - mouseEvent.getX(), mouseEvent.getScreenY() - mouseEvent.getY());
            menu.hide();
            cn = menu;
        }
        if (!cn.isShowing()) {
            cn.show(menuUserOpener, mouseEvent.getScreenX() - mouseEvent.getX()-cn.getWidth()+10, mouseEvent.getScreenY() - mouseEvent.getY()+10);

        }
    }

    public void goToSettings(MouseEvent event) {
        try {
            changeContent("Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToContactSupport(MouseEvent event) {
        try {
            changeContent("ContactInfo");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToProjects(MouseEvent event) {
        try {
            changeContent("Projects");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
