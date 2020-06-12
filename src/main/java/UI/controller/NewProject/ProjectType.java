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

    /**
     * Initializarea datelor.
     * Se seteaza butonul previous ca invizibil.
     *                    next ca sa nu se poata selecta
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (PreviousButton != null) {
            PreviousButton.setVisible(false);
        }
        if (NextButton != null) {
            NextButton.setDisable(true);
        }

    }

    /**
     * Parcuge in continuare wizardul de creare a unui nou proiect.
     * Se seteaza butoanele accesibile si invizibile la un pas urmator iar la final se trimit datele dupa care se inchide wizardul
     * @param actionEvent
     * @throws UnexpectedException
     */
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

    /**
     * Returnarea la meniul anterior.
     * Se seteaza butoanele accesibile si vizibile pentru pasul respectiv
     * @param event
     * @throws UnexpectedException
     */
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

    /**
     * Schimba continutul wizardului la un anumit pas
     * @param filename numele fisierului fxml care are continului acestui wizard
     */
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
