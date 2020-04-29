package controller;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController extends Controller {


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void createNewProject(ActionEvent actionEvent) {
        try {
            changeContent("Work");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load() {

    }
}
