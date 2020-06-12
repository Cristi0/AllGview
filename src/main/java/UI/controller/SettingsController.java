package UI.controller;

import UI.configuration.Main;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController extends Controller {
    public CheckBox ShowHints;

    public void showHintsPressed(ActionEvent event) {
        Main.hasHints=!Main.hasHints;
        ((MainController)Creator).HintIndicator.setVisible(Main.hasHints);
    }

    @Override
    public void load() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ShowHints.setSelected(Main.hasHints);
    }
}
