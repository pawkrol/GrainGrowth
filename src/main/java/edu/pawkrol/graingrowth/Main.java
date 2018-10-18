package edu.pawkrol.graingrowth;

import edu.pawkrol.graingrowth.view.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL layoutUrl = Objects.requireNonNull(Main.class.getClassLoader().getResource("main.fxml"));
        FXMLLoader loader = new FXMLLoader(layoutUrl);
        Parent root = loader.load();
        MainController mainController = loader.getController();
        mainController.setStage(primaryStage);

        primaryStage.setTitle("Grain Growth");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
