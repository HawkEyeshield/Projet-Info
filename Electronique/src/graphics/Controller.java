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


        // On génère toute les images à la suite en les positionnant au bon endroit

        anchorPane3.setPrefHeight(3000);
        anchorPane3.setPrefWidth(3000);

        // On charge les images des composants dans la zone de droite
        GraphicalFunctions.addCurrentGenerator(anchorPane3, anchorPane4, scrollPane, ValeurADeterminer, TensionAImposer, CourantAImposer);
        GraphicalFunctions.addVoltageGenerator(anchorPane3, anchorPane4, scrollPane, anchorPane, ValeurADeterminer, TensionAImposer, CourantAImposer);
        GraphicalFunctions.addNode(anchorPane3, anchorPane4, scrollPane,ValeurADeterminer, TensionAImposer, CourantAImposer);
        GraphicalFunctions.addResistance(anchorPane3, anchorPane4, scrollPane, anchorPane,ValeurADeterminer, TensionAImposer, CourantAImposer);


        //permet d'executer le programme
        Run.setOnMouseClicked(event ->{
            //On regarde si l utilisateur n a pas deja appuyer sur le bouton Run
            if(!GraphicalFunctions.launch) {
                Text programmeLaunch = new Text("Le programme tourne, veuillez patienter...");//On change le texte du bouton
                double [] scrollPosition = GraphicalFunctions.positionRelative(anchorPane3,scrollPane); // Donne la position relaive de la fenetre
                programmeLaunch.setX(550 + scrollPosition[0]);//On definie la zone
                programmeLaunch.setY(20 + scrollPosition[1]);
                anchorPane3.getChildren().add(programmeLaunch);//et on affiche
                //TODO Mettre la fonction qui lance le programme de raph
                //Attention il faut prendre en argument AnchorPane anchorPane3, Text programmeLaunch, AnchorPane anchorPane4,boutton Run

                GraphicalFunctions.launch = true;//on indique que le programme est lance
                anchorPane3.getChildren().remove(GraphicalFunctions.bug);

                //Fonction de test pour voir si le renvoie de valeur fonctionne
                GraphicalComponent [] a = new GraphicalComponent[GraphicalFunctions.arrayListOfLink.size()];
                for(int i = 0; i < GraphicalFunctions.arrayListOfLink.size();i++){
                    a[i] = GraphicalFunctions.arrayListOfLink.get(i).image1;
                }
                GraphicalFunctions.showResult(anchorPane3,programmeLaunch,anchorPane4,a,Run);

                //On echange de nom a chaque clic
                if(Run.getText().equals("Run")) {
                    Run.setText("StopRun");
                }
                else{
                    Run.setText("Run");
                    for (int i = 0; i < GraphicalFunctions.informationsList.size(); i++) {//On supprime les infos quand l utilisateur clic sur StopRun
                        anchorPane3.getChildren().remove(GraphicalFunctions.informationsList.get(i));
                    }
                }
            }
            else{
                System.out.println("Arreter de spammer la touche run !");
                anchorPane3.getChildren().remove(GraphicalFunctions.bug);
            }
        });

        TensionAImposer.setOnMouseClicked(event -> {
            if(TensionAImposer.getText().endsWith("Tension à imposer")) {//Evite qu on puisse definir deux fois une tension
                TensionAImposer.setText("Choisir un composant");//On change le texte du bouton et on indique ce que dois faire l utilisateur
                GraphicalFunctions.etat = "tai";//Ce changement d etat va permettre d active la selection dans GraphicalFonction
            }
        });

        CourantAImposer.setOnMouseClicked(event -> {
            if(CourantAImposer.getText().endsWith("Courant à imposer")) {//Evite qu on puisse definir deux fois une tension
                CourantAImposer.setText("Choisir un composant");//On change le texte du bouton et on indique ce que dois faire l utilisateur
                GraphicalFunctions.etat = "cai";//Ce changement d etat va permettre d active la selection dans GraphicalFonction
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
            double [] scrollPosition = GraphicalFunctions.positionRelative(anchorPane3,scrollPane);
            bug.setX(2 + scrollPosition[0]);
            bug.setY(20 + scrollPosition[1]);
            anchorPane3.getChildren().add(bug);
            //TODO si des gens veullent faire des vérifications, c'est ici qu'il faut appeler leurs fonctions
            GraphicalFunctions.bug = bug;
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
    private Button CourantAImposer;

    @FXML
    private MenuItem Close;

}
