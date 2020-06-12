package UI.configuration;

import UI.Alert.Dialog;
import UI.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Pornirea programului principal
     *
     * @param primaryStage scnea principala
     */

    public static boolean hasHints = true;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(576);
        try {
            FXMLLoader rootfxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
            Parent root = rootfxmlLoader.load();

            MainController ctrl = rootfxmlLoader.getController();
            ctrl.setPrimaryStage(primaryStage);
            ctrl.load();

            Scene scene = new Scene(root, 1024, 576);

            primaryStage.setTitle("AllGview");
            primaryStage.setScene(scene);
            ctrl.changeContent("Dashboard");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
