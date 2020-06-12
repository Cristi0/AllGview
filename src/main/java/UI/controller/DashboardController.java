package UI.controller;

import Eroare.UnexpectedException;
import UI.controller.NewProject.ProjectType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController extends Controller {


    public Button CreateProjectButton;
    public Button ProjectsButton;

    @Override
    public void load() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void createNewProject(ActionEvent event) {
        try {
            ProjectsController ctrl = new ProjectsController();
            ctrl.primaryStage=this.primaryStage;
            ctrl.Creator=this.Creator;
            ctrl.createNewProject(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToProjects(ActionEvent event) {
        try {
            Creator.changeContent("Projects");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
