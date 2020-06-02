package UI.controller.NewProject;

import Eroare.UnexpectedException;
import UI.controller.Controller;
import UI.controller.DashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProjectType extends Controller {

    public Button PreviousButton;
    public Button NextButton;

    public Integer type = -1; //Selecteaza tipul de grafic de la 1 la n
    public Integer template = -1; //Selecteaza template de grafic de la 1 la n
    public Integer pozition = 0; // pozitia la care se afla in procesul de creare a unui nou proiect

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (PreviousButton != null) {
            PreviousButton.setVisible(false);
        }
        if (NextButton != null) {
            NextButton.setDisable(true);
        }

    }

    public void goForword(ActionEvent actionEvent) throws UnexpectedException {
        switch (pozition) {
            case 0:
                pozition++;
                changeTo("NewProject/Templates");
                PreviousButton.setVisible(true);
                NextButton.setText("Done");
                NextButton.setDisable(true);
                break;
            case 1:
                DashboardController dc=(DashboardController) Creator;
                dc.createProjectFromData(type,template);
                primaryStage.close();
                break;
            default:
                throw new UnexpectedException("Counter gresit");
        }

    }

    public void goBack(ActionEvent event) throws UnexpectedException {
        switch (pozition) {
            case 1:
                pozition--;
                changeTo("NewProject/Types");
                PreviousButton.setVisible(false);
                NextButton.setDisable(true);
                NextButton.setText("Next");
                break;
            default:
                throw new UnexpectedException("Counter gresit");
        }

    }

    private void changeTo(String filename) {
        try {
            changeContent(filename);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load() {

    }
}
