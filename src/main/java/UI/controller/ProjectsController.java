package UI.controller;

import Eroare.UnexpectedException;
import UI.controller.NewProject.ProjectType;
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

public class ProjectsController extends Controller {
    public Button CreateProjectButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hintButton(CreateProjectButton,"Create a new project.");
    }

    /**
     * Creeaza o noua fereastra unde se va afisar wizad-ul pentru crearea unui nou proiect
     * @param actionEvent
     * @throws IOException
     */
    public void createNewProject(ActionEvent actionEvent) throws IOException {
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

    /**
     * Schimba continutul ferestrei in functie de parametrii dati
     * @param type tipul de vizualizare
     * @param template sablonul ales
     * @throws UnexpectedException
     */
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
