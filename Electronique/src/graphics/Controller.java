package graphics;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements javafx.fxml.Initializable
{


    /** cette méthode avec l'implements permet de lancer une méthode au lancement du controller */
    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        //Juste pour bien voir que le programme ce lance

        System.out.println("L'interface graphique est lancée");

        // On génère toute les images à la suite en les positionnant au bon endroit

        anchorPane3.setPrefHeight(4000);
        anchorPane3.setPrefWidth(2000);

        // On charge les images des composants dans la zone de droite
        GraphicalFunctions.addCourantGenerator(anchorPane2, anchorPane4, scrollPane);
        GraphicalFunctions.addVoltageGenerator(anchorPane2, anchorPane4, scrollPane);
        GraphicalFunctions.addNode(anchorPane2, anchorPane4, scrollPane, CreeUnLien);

        CreeUnLien.setOnMouseClicked(event -> {
            GraphicalFunctions.etat = "l1";
            System.out.println("On commence a creer un lien");
        });
    }

    @FXML
    private MenuItem Delete;

    @FXML
    private Menu Help;


    @FXML
    private ImageView image;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button Run;

    @FXML
    private Menu Edit;

    @FXML
    private AnchorPane anchorPane2;

    @FXML
    private Button Debugge;

    @FXML
    private AnchorPane anchorPane3;

    @FXML
    private MenuItem About;

    @FXML
    private AnchorPane anchorPane4;

    @FXML
    private Button ValeurADeterminer;

    @FXML
    private MenuBar menuBar;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Menu file;

    @FXML
    private SplitPane splitPane;

    @FXML
    private Button CreeUnLien;

    @FXML
    private Button TensionAImposer;

    @FXML
    private MenuItem Close;

}
