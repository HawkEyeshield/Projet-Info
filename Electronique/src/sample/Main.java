package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @FXML
    private ImageView imageView;

    /**
     * start permet de lancer l'interface graphique
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        URL location = getClass().getResource("sample.fxml");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

        Parent root = (Parent) fxmlLoader.load(location.openStream());

        primaryStage.setTitle("Circuit Electrique");
        primaryStage.setScene(new Scene(root, 1100, 690));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}