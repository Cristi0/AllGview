package UI.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class Controller implements Initializable {
    protected Stage primaryStage;
    protected Controller Creator;
    protected Controller Created;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setCreator(Controller creator) {
        this.Creator = creator;
    }

    public abstract void load();

    public void changeContent(String fxmlFileName) throws IOException {
        FXMLLoader subfxmlLoader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlFileName + ".fxml"));
        BorderPane borderPane = ((BorderPane) primaryStage.getScene().rootProperty().get());
        borderPane.setCenter(subfxmlLoader.load());
        Controller ctrl = subfxmlLoader.getController();
        ctrl.setPrimaryStage(primaryStage);
        Created = ctrl;
        ctrl.Creator = this;
        ctrl.load();
    }
}
