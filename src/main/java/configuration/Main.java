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
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        System.out.println("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");

        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(576);
        try {
            System.out.println(getClass().getResource("/fxml/Main.fxml").getPath());
            //Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));
            FXMLLoader rootfxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
            Parent root = rootfxmlLoader.load();
            FXMLLoader subfxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
            ((BorderPane)root).setCenter(subfxmlLoader.load());

            DashboardController controller = subfxmlLoader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.load();

            Scene scene =new Scene(root,1024,576);

            primaryStage.setTitle("AllGview");
            primaryStage.setScene(scene);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
