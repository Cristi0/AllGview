package UI.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class Controller implements Initializable {
    protected Stage primaryStage;       // fereastra curenta
    protected Controller Creator;       // Controllerul care a creat acest controller
    protected Controller Created;       // Ultimul controller creat de acest controller

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setCreator(Controller creator) {
        this.Creator = creator;
    }

    public abstract void load();

    /**
     * Schimba continutul unui fxml care are la baza: BorderPane, anume schimba centrul acestuia.
     * Se seteaza controllerul care il creeaza pentru controllerul noului fxml creat
     * Se seteaza pentru propriul controllor ultimul controller creat
     * @param fxmlFileName
     * @throws IOException
     */
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
