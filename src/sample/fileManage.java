package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class fileManage extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("sample.fxml")));
        primaryStage.setTitle("Hello World");
//        primaryStage.initStyle(StageStyle.UNDECORATED);

        Scene scene = new Scene(root, 800, 500);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("sample.css")).toExternalForm());
        primaryStage.setScene(scene);
//        primaryStage.setResizable(false);

        primaryStage.show();
    }
}
