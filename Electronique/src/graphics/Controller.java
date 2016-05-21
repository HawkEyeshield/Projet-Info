package graphics;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements javafx.fxml.Initializable//implement très utile qui permet de lancer la fonction d'initialisation
{
    /** cette méthode avec l'implements "javafx.fxml.Initializable" permet de lancer une méthode au lancement du controller */
    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {

        // On génère toutes les images à la suite en les positionnant au bon endroit

        anchorPane3.setPrefHeight(3000);
        anchorPane3.setPrefWidth(3000);

        // On charge les images des composants dans la zone de droite
        GraphicalFunctions.addCurrentGenerator(anchorPane3, anchorPane4, scrollPane, ValeurADeterminer, TensionAImposer, CourantAImposer);
        GraphicalFunctions.addVoltageGenerator(anchorPane3, anchorPane4, scrollPane, anchorPane, ValeurADeterminer, TensionAImposer, CourantAImposer);
        GraphicalFunctions.addNode(anchorPane3, anchorPane4, scrollPane,ValeurADeterminer, TensionAImposer, CourantAImposer);
        GraphicalFunctions.addResistance(anchorPane3, anchorPane4, scrollPane, anchorPane,ValeurADeterminer, TensionAImposer, CourantAImposer);


        //permet d'exécuter le programme
        Run.setOnMouseClicked(event ->{
            //On regarde si l'utilisateur n'a pas déjà appuyé sur le bouton Run
            if(!GraphicalFunctions.isProgramRunning) {
                //On échange de nom à chaque clic
                if(Run.getText().equals("Run")) {
                    Run.setText("StopRun");
                    Text programmeLaunch = new Text("Le programme tourne, veuillez patienter...");//On change le texte du bouton
                    double [] scrollPosition = GraphicalFunctions.positionRelative(anchorPane3,scrollPane); // Donne la position relative de la fenêtre
                    programmeLaunch.setX(5 + scrollPosition[0]);//On définit la zone
                    programmeLaunch.setY(20 + scrollPosition[1]);
                    anchorPane3.getChildren().add(programmeLaunch);//et on affiche

                    //TODO Mettre la fonction qui lance le programme de raphaël
                    //Attention il faut prendre en argument AnchorPane anchorPane3, Text programmeLaunch, AnchorPane anchorPane4,boutton Run


                    GraphicalFunctions.isProgramRunning = true;//on indique que le programme est lancé
                    anchorPane3.getChildren().remove(GraphicalFunctions.bug);

                    //TODO il me faudrait un tableau compose d'element du type GraphicalComponent pour que je puisse tout affiche
                    try
                    {
                        
                    }
                    catch(IllegalArgumentException e)
                    {
                        //TODO faire apparaître une fenêtre avec un message d'erreur
                        Text erreur = new Text("Erreur, le programme ne peut être resolu" + e);//On change le texte du bouton
                        double [] scrollPosition2 = GraphicalFunctions.positionRelative(anchorPane3,scrollPane); // Donne la position relative de la fenêtre
                        erreur.setX(550 + scrollPosition2[0]);//On définit la zone
                        erreur.setY(20 + scrollPosition2[1]);
                        anchorPane3.getChildren().add(erreur);//et on affiche
                        System.out.println("Problème de sommets !");
                        GraphicalFunctions.erreur = erreur;
                    }
                }
                else{
                    anchorPane4.getChildren().remove(GraphicalFunctions.informationsList.get(0));//On met ici le premier, car il est dans anchorPane4
                    Run.setText("Run");
                    for (int i = 1; i < GraphicalFunctions.informationsList.size(); i++) {//On supprime les infos quand l'utilisateur clic sur StopRun
                        anchorPane3.getChildren().remove(GraphicalFunctions.informationsList.get(i));
                    }
                    GraphicalFunctions.informationsList = new ArrayList<Text>();//On supprime toutes les valeurs présentes car elles ont été supprimées
                }
            }
            else{
                System.out.println("Arreter de spammer la touche run !");
                anchorPane3.getChildren().remove(GraphicalFunctions.bug);
                anchorPane3.getChildren().remove(GraphicalFunctions.erreur);
            }
        });

        TensionAImposer.setOnMouseClicked(event -> {
            if(TensionAImposer.getText().endsWith("Tension à imposer")) {//Evite qu'on puisse définir deux fois une tension
                TensionAImposer.setText("Choisir un composant");//On change le texte du bouton et on indique ce que doit faire l'utilisateur
                GraphicalFunctions.state = "tai";//Ce changement d'état va permettre d'activer la sélection dans GraphicalFonction
            }
        });

        CourantAImposer.setOnMouseClicked(event -> {
            if(CourantAImposer.getText().endsWith("Courant à imposer")) {//Evite qu on puisse definir deux fois une tension
                CourantAImposer.setText("Choisir un composant");//On change le texte du bouton et on indique ce que doit faire l'utilisateur
                GraphicalFunctions.state = "cai";//Ce changement d'etat va permettre d'activer la sélection dans GraphicalFonction
            }
        });

        ValeurADeterminer.setOnMouseClicked(event -> {//Permet à l'utilisateur de donner un composant, la tension et le courant lui seront rendus à la fin du calcul
            if(ValeurADeterminer.getText().endsWith("Valeur à déterminer")) {//Evite qu on puisse définir deux fois une valeur à determiner
                GraphicalFunctions.state = "v";//Ce changement d'état va permettre d'activer la sélection dans GraphicalFonction
                ValeurADeterminer.setText("Choisir un composant");//On change le texte du bouton et on indique ce que doit faire l'utilisateur
            }
        });

        Debugge.setOnMouseClicked(event -> {
            //Fonction qui détermine si le solveur va pouvoir donner un résultat ou si il existe des problèmes dans le schéma électrique
            Text bug = new Text("Le programme devrait pouvoir se lancer correctement");
            double [] scrollPosition = GraphicalFunctions.positionRelative(anchorPane3,scrollPane);
            bug.setX(2 + scrollPosition[0]);
            bug.setY(20 + scrollPosition[1]);
            anchorPane3.getChildren().add(bug);
            //TODO si des gens veulent faire des vérifications, c'est ici qu'il faut appeler leurs fonctions
            GraphicalFunctions.bug = bug;
        });

        CreerUnLien.setOnMouseClicked(event -> {
            //Permet de créer un lien entre deux composants
            if(GraphicalFunctions.state == "l1" || GraphicalFunctions.state =="l2") {//On regarde si l'utilisateur veut arrêter de créer des liens
                GraphicalFunctions.state = "d";//Si il était entrain de créer des liens, c'est qu'il veut arrêter, donc on repasse en Drag and Drop
                CreerUnLien.setText("Creer un lien"); //On remet le texte d'origine
            }
            else{
                GraphicalFunctions.state = "l1";
                CreerUnLien.setText("Arreter de creer des liens");
            }
        });
    }

    //Liste créée automatiquement par SceneBuilder des objets graphiques

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
    private Button CourantAImposer;

    @FXML
    private MenuItem Close;

}
