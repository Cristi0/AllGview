package configuration;

import controller.DashboardController;
import controller.GraphController;
import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
//        String javaVersion = System.getProperty("java.version");
//        String javafxVersion = System.getProperty("javafx.version");
//        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
//        Scene scene = new Scene(new StackPane(l), 640, 480);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//        String javafxVersion = System.getProperty("javafx.version");
//        String javaVersion = System.getProperty("java.version");
//        System.out.println("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");

        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(576);
        try {
            FXMLLoader rootfxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
            Parent root = rootfxmlLoader.load();

            MainController ctrl = rootfxmlLoader.getController();
            ctrl.setPrimaryStage(primaryStage);
            ctrl.load();

            Scene scene =new Scene(root,1024,576);

            primaryStage.setTitle("AllGview");
            primaryStage.setScene(scene);
            ctrl.changeContent("Dashboard");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
