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

        anchorPane3.setPrefHeight(3000);
        anchorPane3.setPrefWidth(3000);

        // On charge les images des composants dans la zone de droite
        GraphicalFunctions.addCurrentGenerator(anchorPane3, anchorPane4, scrollPane, ValeurADeterminer, TensionAImposer);
        GraphicalFunctions.addVoltageGenerator(anchorPane3, anchorPane4, scrollPane, anchorPane, ValeurADeterminer, TensionAImposer);
        GraphicalFunctions.addNode(anchorPane3, anchorPane4, scrollPane,ValeurADeterminer, TensionAImposer);
        GraphicalFunctions.addResistance(anchorPane3, anchorPane4, scrollPane, anchorPane,ValeurADeterminer, TensionAImposer);


        //permet d'executer le programme
        Run.setOnMouseClicked(event ->{
            //On regarde si l utilisateur n a pas deja appuyer sur la bouton Run
            if(!GraphicalFunctions.launch) {
                Text programmeLaunch = new Text("Le programme tourne, veuillez patienter...");//On change le texte du bouton
                programmeLaunch.setX(550);//On definie la zone
                programmeLaunch.setY(20);
                System.out.println(scrollPane.getTranslateX());
                anchorPane3.getChildren().add(programmeLaunch);//et on affiche
                //TODO Mettre la fonction qui lance le programme de raph
                //Attention il faut prendre en argument le programmeLaunch pour me le redonner par la suite (pour le supprimer)
                GraphicalFunctions.launch = true;
            }
            else{
                System.out.println("Arreter de spammer la touche run !");
            }
        });

        TensionAImposer.setOnMouseClicked(event -> {
            if(TensionAImposer.getText().endsWith("Tension à imposer")) {//Evite qu on puisse definir deux fois une tension
                TensionAImposer.setText("Choisir un composant");//On change le texte du bouton et on indique ce que dois faire l utilisateur
                GraphicalFunctions.etat = "vd";//Ce changement d etat va permettre d active la selection dans GraphicalFonction
            }
        });

        ValeurADeterminer.setOnMouseClicked(event -> {//Permet a l utilisateur de donner un composant, la tension et le courant lui seront rendu a la fin du calcul
            if(ValeurADeterminer.getText().endsWith("Valeur à déterminer")) {//Evite qu on puisse definir deux fois une valeur a determiner
                GraphicalFunctions.etat = "v";//Ce changement d etat va permettre d active la selection dans GraphicalFonction
                ValeurADeterminer.setText("Choisir un composant");//On change le texte du bouton et on indique ce que dois faire l utilisateur
            }
        });

        Debugge.setOnMouseClicked(event -> {
            //Fonction qui determine si le solveur va pouvoir donner un resultat ou si il existe des problemes dans le schema electrique
            Text bug = new Text("Le programme devrait pouvoir se lancer correctement");
            bug.setX(2);
            bug.setY(20);
            anchorPane3.getChildren().add(bug);
            //TODO si des gens veullent faire des vérifications, c'est ici qu'il faut appeler leurs fonctions
        });

        CreerUnLien.setOnMouseClicked(event -> {
            //Permet de creer un lien entre deux composants
            if(GraphicalFunctions.etat == "l1" || GraphicalFunctions.etat =="l2") {//On regarde si l utilisateur veut arreter de creer des liens
                GraphicalFunctions.etat = "d";//Si il etait dans entrain de creer des liens, c est qu il veut arreter, donc on repasse en Drag and Drop
                CreerUnLien.setText("Creer un lien"); //On remet le texte d'origine
            }
            else{
                GraphicalFunctions.etat = "l1";
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
