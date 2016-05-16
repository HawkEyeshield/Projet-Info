package graphics;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application 
{

    @FXML
    private ImageView imageView;

    /** start permet de lancer l'interface graphique */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        URL location = getClass().getResource("sample.fxml");//Permet de trouver le fichier sample

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

        Parent root = (Parent) fxmlLoader.load(location.openStream());

        primaryStage.setTitle("Circuit Electrique"); //permet de donner un titre à la fenêtre
        primaryStage.setScene(new Scene(root, 1100, 690)); //définit la taille
        primaryStage.show(); //affiche l'interface
    }


    public static void main(String[] args) 
    {
        launch(args);
    }
}
