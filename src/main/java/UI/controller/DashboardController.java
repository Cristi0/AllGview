package UI.controller;

import Eroare.UnexpectedException;
import UI.controller.NewProject.ProjectType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController extends Controller {


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void createNewProject(ActionEvent actionEvent) throws IOException {  //todo: Deal with exception
//        try {
//            createProjectFromData(1,3);
//        } catch (UnexpectedException e) {
//            e.printStackTrace();
//        }
        //todo:De DECOMENTAT
        FXMLLoader rootfxmlLoader = new FXMLLoader(getClass().getResource("/fxml/NewProject/ProjectType.fxml"));
        Parent root = rootfxmlLoader.load();

        ProjectType ctrl = rootfxmlLoader.getController();

        Scene secondScene = new Scene(root, 600, 450);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Create new project");
        newWindow.setScene(secondScene);
        newWindow.setMinWidth(600);
        newWindow.setMinHeight(400);

        // Set position of second window, related to primary window.
        newWindow.setX(primaryStage.getX() + 200);
        newWindow.setY(primaryStage.getY() + 100);
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(primaryStage);

        ctrl.setPrimaryStage(newWindow);
        ctrl.setCreator(this);
        ctrl.changeContent("NewProject/Types");
        newWindow.show();
    }

    public void createProjectFromData(Integer type, Integer template) throws UnexpectedException {
        try {
            switch (type){
                case 1:
                    changeContent("Work");
                    GraphController gc = (GraphController) Created;
                    gc.setTemplate(template);
                    break;
                default:
                    throw new UnexpectedException("Type not found for accessing new window");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load() {

    }
}
