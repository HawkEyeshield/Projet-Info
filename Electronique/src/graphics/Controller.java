package graphics;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements javafx.fxml.Initializable
{


    /** cette méthode avec l'implements "javafx.fxml.Initializable" permet de lancer une méthode au lancement du controller */
    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        //Juste pour bien voir que le programme ce lance

        System.out.println("L'interface graphique est lancée");

        // On génère toute les images à la suite en les positionnant au bon endroit

        anchorPane3.setPrefHeight(4000);
        anchorPane3.setPrefWidth(2000);

        // On charge les images des composants dans la zone de droite
        GraphicalFunctions.addCurrentGenerator(anchorPane2, anchorPane4, scrollPane, ValeurADeterminer);
        GraphicalFunctions.addVoltageGenerator(anchorPane2, anchorPane4, scrollPane, anchorPane, ValeurADeterminer);
        GraphicalFunctions.addNode(anchorPane2, anchorPane4, scrollPane,ValeurADeterminer);
        GraphicalFunctions.addResistance(anchorPane2, anchorPane4, scrollPane,  anchorPane,ValeurADeterminer);


        //permet d'executer le programme
        Run.setOnMouseClicked(event ->{
            //On regarde si l utilisateur n a pas deja appuier sur la bouton Run
            if(!GraphicalFunctions.launch) {
                System.out.println("Programme lancé");
                Text programmeLaunch = new Text("Le programme tourne, veuillez patienter...");
                programmeLaunch.setLayoutX(100);
                programmeLaunch.setLayoutX(100);
                programmeLaunch.setX(450);
                programmeLaunch.setY(50);
                anchorPane2.getChildren().add(programmeLaunch);
                //TODO Mettre la fonction qui lance le programme de raph
                //Attention il faut prendre en argument le programmeLaunch pour me le redonner par la suite (pour le supprimer)
                GraphicalFunctions.launch = true;
            }
            else{
                System.out.println("Arreter de spam la touche run !");
            }
        });

        ValeurADeterminer.setOnMouseClicked(event -> {
            GraphicalFunctions.etat = "v";
            ValeurADeterminer.setText("Choisir un composant");
        });

        Debugge.setOnMouseClicked(event -> {
            System.out.println("Le programme devrait pouvoir se lancer correctement");
            Text bug = new Text("Le programme devrait pouvoir se lancer correctement");
            bug.setLayoutX(100);
            bug.setLayoutX(100);
            bug.setX(-100);
            bug.setY(75);
            anchorPane2.getChildren().add(bug);
            //TODO si des gens veullent faire des vérifications, c'est ici qu'il faut appeler leurs fonctions

        });


        CreerUnLien.setOnMouseClicked(event -> {
            if(GraphicalFunctions.etat == "l1" || GraphicalFunctions.etat =="l2") {
                GraphicalFunctions.etat = "d";
                CreerUnLien.setText("Creer un lien");
                GraphicalFunctions.etat = "d";
            }
            else{
                GraphicalFunctions.etat = "l1";
                System.out.println("On commence a creer un lien");
                CreerUnLien.setText("Arreter de creer des liens");
            }
        });
    }


    //Liste creer automatiquement par SceneBuilder des objets graphique

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
    private Button CreerUnLien;

    @FXML
    private Button TensionAImposer;

    @FXML
    private MenuItem Close;

}
