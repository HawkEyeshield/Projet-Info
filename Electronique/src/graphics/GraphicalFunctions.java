package graphics;
import circuit.Breadboard;
import components.AbstractDipole;
import components.Type;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.ArrayList;

//import java.awt.event.ActionEvent;

/** Classe donnant les différentes fonctions utilisées dans l'interface graphique.
 * Fait appel à la breadboard pour faire le lien avec le solveur
 * @author Tanguy, Sterenn
 */
public class GraphicalFunctions
{

	/** Permet de définir si on peut faire du drag and drop ou si on est entrain de créer des liens*/
	public static String etat = "d";
	
	/** Premiere image du lien que l'on va creer*/
	public static GraphicalComponent premiereImageDuLien;
	
	/**Compte le nombre de generateur de tension*/
	public static int idVoltageGenerator = 0;
	
	/** Compte le nombre de generateur de courant*/
	public static int idCourantgeGenerator = 0;

	/** Compte le nombre de generateur de courant*/
	public static int idResistance = 0;

	/**Compte  le nombre de noeud*/
	public static int idNode = 0; 
	
	/**liste qui liste les liens*/
	public  static ArrayList<Link> arrayListOfLink = new ArrayList<Link>();

	/**Permet de retenir quel est la valeur a afficher*/
	public static GraphicalComponent valueToShow = null;

	/** Boolean qui permet de savoir si le programme tourne ou non*/
	public static boolean launch = false;

	/**Va servir pour garder en memoire la premiere zone de lien utilise*/
	public static int linkArea;
	
	/** Breadboard qui traduira le schéma interface en graphe pour le solveur.
	 * Permet également de donner à chaque composant les potentiels à ses pattes et les courants après résolution*/
    private static Breadboard breadboard = new Breadboard(new ArrayList<AbstractDipole>());
	
	/** Entier indiquant le numéro d'un sommet pour la création de composant*/
	private static int vertexIndex=0;

	/** Permet de savoir si la rotation d'une image est possible, ou si il existe déjà des liens*/
	public static boolean rotationPossible;

	/** String qui sont utilise pour affiche le faite qu'on ne connaisse pas encore une valeur*/
	public  static  String componentCourantToShow;
	public  static  String componentVoltageToShow;
	public  static  String componentValueToShow;

	/** Permet de retenir le texte qui indique si le programme peut se lancer ou non*/
	public static Text bug;

	/** Permet de retenir les informations que l'on affiche a la fin du programme pour pouvoir les supprimer*/
	public  static ArrayList<Text> informationsList = new ArrayList<Text>();

	/**
     * Permet d'ajouter ou d'actualiser un lien aussi bien visuellement que dans la breadboard
     * @param premiereImageDuLien Premier composant a relier
     * @param secondeImageDuLien Second composant a relier
     * @param linkAreaUsed1 Premiere zone a relier(1 pour a gauche, 2 pour en haut, 3 a droite et 4 en bas)
     * @param linkAreaUsed2 Seconde zone a relier
     * @param k Permet de savoir si il faut l'ajouter ou juste actualiser
     * @param anchorPane2 Zone de travail
     */
    public static void addLink(GraphicalComponent premiereImageDuLien, GraphicalComponent secondeImageDuLien, int linkAreaUsed1, int linkAreaUsed2, int k, AnchorPane anchorPane2) {
		//On initialise des variables qui vont nous simplifier la compréhension
        double centreXLinkArea1 = 0;
        double centreYLinkArea1 = 0;
        double centreXLinkArea2 = 0;
        double centreYLinkArea2 = 0;
		//Les variables prennent les valeurs des zones utilise
        if (linkAreaUsed1 == 1) {
            centreXLinkArea1 = premiereImageDuLien.square1.getX() + 5;
            centreYLinkArea1 = premiereImageDuLien.square1.getY() + 5;
        } else if (linkAreaUsed1 == 2) {
            centreXLinkArea1 = premiereImageDuLien.square2.getX() + 5;
            centreYLinkArea1 = premiereImageDuLien.square2.getY() + 5;
        } else if (linkAreaUsed1 == 3) {
            centreXLinkArea1 = premiereImageDuLien.square3.getX() + 5;
            centreYLinkArea1 = premiereImageDuLien.square3.getY() + 5;
        } else if (linkAreaUsed1 == 4) {
            centreXLinkArea1 = premiereImageDuLien.square4.getX() + 5;
            centreYLinkArea1 = premiereImageDuLien.square4.getY() + 5;
        }
        if (linkAreaUsed2 == 1) {
            centreXLinkArea2 = secondeImageDuLien.square1.getX() + 5;
            centreYLinkArea2 = secondeImageDuLien.square1.getY() + 5;
        } else if (linkAreaUsed2 == 2) {
            centreXLinkArea2 = secondeImageDuLien.square2.getX() + 5;
            centreYLinkArea2 = secondeImageDuLien.square2.getY() + 5;
        } else if (linkAreaUsed2 == 3) {
            centreXLinkArea2 = secondeImageDuLien.square3.getX() + 5;
            centreYLinkArea2 = secondeImageDuLien.square3.getY() + 5;
        } else if (linkAreaUsed2 == 4) {
            centreXLinkArea2 = secondeImageDuLien.square4.getX() + 5;
            centreYLinkArea2 = secondeImageDuLien.square4.getY() + 5;
        }

		//On initialise les lignescar elles se seront pas toujours toutes utilisees, mais toujours toutes affichees
        Line line1 = new Line(0, 0, 0, 0);
        Line line2 = new Line(0, 0, 0, 0);
        Line line3 = new Line(0, 0, 0, 0);

		/**Tout les if suivent le même schema, on regarde les zones utilise et on agit en consequence
		 * On part toujours d un cote de la zone utiliser pour le premier composant et de l autre pour le second composant
		 * On relie ces deux traits avec un dernier
		 * Ceci a pour unique but de clarifier visuellement la schema, en evitant les traits droit qui serait illisible
		*/
        if((linkAreaUsed1 == 3 && linkAreaUsed2 == 1) || (linkAreaUsed1 == 1 && linkAreaUsed2 == 3)){
            line1 = new Line(centreXLinkArea1, centreYLinkArea1, (centreXLinkArea1 + centreXLinkArea2) / 2, centreYLinkArea1);

            line2 = new Line(centreXLinkArea2, centreYLinkArea2, (centreXLinkArea1 + centreXLinkArea2) / 2, centreYLinkArea2);

            line3 = new Line((centreXLinkArea1 + centreXLinkArea2) / 2, centreYLinkArea1, (centreXLinkArea1 + centreXLinkArea2) / 2, centreYLinkArea2);
        }

		else if((linkAreaUsed1 == 2 && linkAreaUsed2 == 4) || (linkAreaUsed1 == 4 && linkAreaUsed2 == 2)){

			line1 = new Line(centreXLinkArea1,centreYLinkArea1,centreXLinkArea1,(centreYLinkArea1+centreYLinkArea2)/2);

			line2 = new Line(centreXLinkArea2,centreYLinkArea2,centreXLinkArea2,(centreYLinkArea2 + centreYLinkArea1)/2);

			line3 = new Line(centreXLinkArea2,(centreYLinkArea2 + centreYLinkArea1)/2,centreXLinkArea1,(centreYLinkArea1+centreYLinkArea2)/2);
		}

		else if((linkAreaUsed1 == 1 && linkAreaUsed2 == 2) || (linkAreaUsed1 == 3 && linkAreaUsed2 == 2) ){
			line1 = new Line(centreXLinkArea1, centreYLinkArea1, centreXLinkArea2, centreYLinkArea1);
			line2 = new Line(centreXLinkArea2, centreYLinkArea2, centreXLinkArea2, centreYLinkArea1);
		}

		else if((linkAreaUsed1 == 2 && linkAreaUsed2 == 1) || (linkAreaUsed1 == 2 && linkAreaUsed2 == 3)){
			line1 = new Line(centreXLinkArea2, centreYLinkArea2, centreXLinkArea1, centreYLinkArea2);
			line2 = new Line(centreXLinkArea1, centreYLinkArea1, centreXLinkArea1, centreYLinkArea2);
		}

		else if(linkAreaUsed1 == 1 && linkAreaUsed2 == 1){
			if(centreXLinkArea1 < centreXLinkArea2){
				line1 = new Line(centreXLinkArea1,centreYLinkArea1,centreXLinkArea1 - 30,centreYLinkArea1);
				line2 = new Line(centreXLinkArea1 - 30, centreYLinkArea1,centreXLinkArea1 - 30,centreYLinkArea2);
				line3 = new Line(centreXLinkArea2,centreYLinkArea2,centreXLinkArea1 - 30,centreYLinkArea2);
			}
			else{
				line1 = new Line(centreXLinkArea2,centreYLinkArea2,centreXLinkArea2 - 30,centreYLinkArea2);
				line2 = new Line(centreXLinkArea2 - 30, centreYLinkArea2,centreXLinkArea2 - 30,centreYLinkArea1);
				line3 = new Line(centreXLinkArea1,centreYLinkArea1,centreXLinkArea2 - 30,centreYLinkArea1);
			}
		}

		else if(linkAreaUsed1 == 2 && linkAreaUsed2 == 2){
			if(centreYLinkArea1 < centreYLinkArea2){
				line1 = new Line(centreXLinkArea1,centreYLinkArea1,centreXLinkArea1,centreYLinkArea1 - 30);
				line2 = new Line(centreXLinkArea1, centreYLinkArea1 - 30,centreXLinkArea2,centreYLinkArea1 - 30);
				line3 = new Line(centreXLinkArea2,centreYLinkArea2,centreXLinkArea2,centreYLinkArea1 - 30);
			}
			else{
				line1 = new Line(centreXLinkArea2,centreYLinkArea2,centreXLinkArea2,centreYLinkArea2 - 30);
				line2 = new Line(centreXLinkArea2, centreYLinkArea2 - 30,centreXLinkArea1,centreYLinkArea2 - 30);
				line3 = new Line(centreXLinkArea1,centreYLinkArea1,centreXLinkArea1,centreYLinkArea2 - 30);
			}
		}

		else if(linkAreaUsed1 == 3 && linkAreaUsed2 == 3){
			if(centreXLinkArea1 > centreXLinkArea2){
				line1 = new Line(centreXLinkArea1,centreYLinkArea1,centreXLinkArea1 + 30,centreYLinkArea1);
				line2 = new Line(centreXLinkArea1 + 30, centreYLinkArea1,centreXLinkArea1 + 30,centreYLinkArea2);
				line3 = new Line(centreXLinkArea2,centreYLinkArea2,centreXLinkArea1 + 30,centreYLinkArea2);
			}
			else{
				line1 = new Line(centreXLinkArea2,centreYLinkArea2,centreXLinkArea2 + 30,centreYLinkArea2);
				line2 = new Line(centreXLinkArea2 + 30, centreYLinkArea2,centreXLinkArea2 + 30,centreYLinkArea1);
				line3 = new Line(centreXLinkArea1,centreYLinkArea1,centreXLinkArea2 + 30,centreYLinkArea1);
			}
		}

		else if(linkAreaUsed1 == 4 && linkAreaUsed2 == 4){
			if(centreYLinkArea1 > centreYLinkArea2){
				line1 = new Line(centreXLinkArea1,centreYLinkArea1,centreXLinkArea1,centreYLinkArea1 + 30);
				line2 = new Line(centreXLinkArea1, centreYLinkArea1 + 30,centreXLinkArea2,centreYLinkArea1 + 30);
				line3 = new Line(centreXLinkArea2,centreYLinkArea2,centreXLinkArea2,centreYLinkArea1 + 30);
			}
			else{
				line1 = new Line(centreXLinkArea2,centreYLinkArea2,centreXLinkArea2,centreYLinkArea2 + 30);
				line2 = new Line(centreXLinkArea2, centreYLinkArea2 + 30,centreXLinkArea1,centreYLinkArea2 + 30);
				line3 = new Line(centreXLinkArea1,centreYLinkArea1,centreXLinkArea1,centreYLinkArea2 + 30);
			}
		}
		else if((linkAreaUsed1 == 3  && linkAreaUsed2 == 4)||(linkAreaUsed1 == 4 && linkAreaUsed2 == 3)){
			line1 = new Line(centreXLinkArea1,centreYLinkArea1,centreXLinkArea1,centreYLinkArea2);
			line2 = new Line(0,0,0,0);
			line3 = new Line(centreXLinkArea2,centreYLinkArea2,centreXLinkArea1,centreYLinkArea2);

		}

		else if((linkAreaUsed1 == 1 && linkAreaUsed2 == 4) || (linkAreaUsed1 == 4 && linkAreaUsed2 == 1)) {
			if (centreYLinkArea1 < centreYLinkArea2) {

				if(centreXLinkArea1 > centreXLinkArea2){
					line1 = new Line(centreXLinkArea2, centreYLinkArea2, centreXLinkArea2, centreYLinkArea1);

					line2 = new Line(centreXLinkArea1, centreYLinkArea1, centreXLinkArea2, centreYLinkArea1);
				}
				else {
					line1 = new Line(centreXLinkArea2, centreYLinkArea2, centreXLinkArea1, centreYLinkArea2);

					line2 = new Line(centreXLinkArea1, centreYLinkArea1, centreXLinkArea1, centreYLinkArea2);
				}
			}
			else {
				if(centreXLinkArea1 > centreXLinkArea2) {
					line1 = new Line(centreXLinkArea2, centreYLinkArea2, centreXLinkArea2, centreYLinkArea1);

					line2 = new Line(centreXLinkArea1, centreYLinkArea1, centreXLinkArea2, centreYLinkArea1);
				}
				else {
					line1 = new Line(centreXLinkArea2, centreYLinkArea2, centreXLinkArea2, centreYLinkArea1);

					line2 = new Line(centreXLinkArea1, centreYLinkArea1, centreXLinkArea2, centreYLinkArea1);
				}
			}
		}

        if (k == -1) { //Si on ajoute un nouveau lien k = -1
            boolean a = true; //Permet de véfifier si il n'existe pas déjà un lien qui part de cet endroit la.
            for (int i = 0; i < arrayListOfLink.size(); i++) {
                if ((premiereImageDuLien == secondeImageDuLien) ||(premiereImageDuLien == arrayListOfLink.get(i).image1 && linkAreaUsed1 == arrayListOfLink.get(i).linkAreaUsed1) || (premiereImageDuLien == arrayListOfLink.get(i).image2 && linkAreaUsed1 == arrayListOfLink.get(i).linkAreaUsed2) || (secondeImageDuLien == arrayListOfLink.get(i).image1 && linkAreaUsed2 == arrayListOfLink.get(i).linkAreaUsed1) || (secondeImageDuLien == arrayListOfLink.get(i).image2 && linkAreaUsed2 == arrayListOfLink.get(i).linkAreaUsed2)) {
                    a = false;//On indique qu'il ne faut pas afficher le lien, ni l'enregistrer et on affiche un message d'erreur
                    Text informations = new Text("Mettre un noeud pour relier ces deux éléments");
                    informations.setLayoutX(5);
                    informations.setLayoutY(75);
                    anchorPane2.getChildren().add(informations);
                    informations.setOnMouseClicked(event ->{ //Permet d'enlever le message d'erreur si la personne clique dessus.
                        anchorPane2.getChildren().remove(informations);
                    });
                    break; //On quitte la boucle pour eviter d'ecrire deux message d'erreur
                }
            }
            if (a) {//Si on est dans le cas ou il n existe pas encore de lien, alors on affiche tout
                anchorPane2.getChildren().add(line1);
                anchorPane2.getChildren().add(line2);
                anchorPane2.getChildren().add(line3);
                arrayListOfLink.add(new Link(premiereImageDuLien, secondeImageDuLien, linkAreaUsed1, linkAreaUsed2, line1, line2, line3));
                //TODO Pour Sterenn : mettre la fonction qui ajoute le lien dans la breadboard
                //breadboard.addLink(arrayListOfLink.get(-1));Erreur ici
            }
        }
        else{ //Si k!= -1 c'est que c'est qu'il faut juste actualiser les liens
            anchorPane2.getChildren().add(line1);
            anchorPane2.getChildren().add(line2);
            anchorPane2.getChildren().add(line3);
			//Et on remet en memoire les nouveaux liens
			arrayListOfLink.get(k).lien1 = line1;
			arrayListOfLink.get(k).lien2 = line2;
			arrayListOfLink.get(k).lien3 = line3;
        }

    }

	/**
	 * Fonction qui permet de supprimer un lien, mais ne me parrait pas tres pertinent apres reflexion donc ne sera sans doute pas utiliser
	 * @param lien Lelien a surpprimer
	 * @param anchorPane2 Zone de travail dans lequel le lien apparait
     */
	static  public void deleteLink(Line lien, AnchorPane anchorPane2){
		//TODO enlever le lien qui relie deux composants
		for (int i = 0; i < arrayListOfLink.size();i++){//On retrouve les liens qui lui sont associé (ils vont par trois) et on les supprime
			if(arrayListOfLink.get(i).lien1 == lien || arrayListOfLink.get(i).lien2 == lien || arrayListOfLink.get(i).lien3 == lien){
				anchorPane2.getChildren().remove(arrayListOfLink.get(i).lien1);
				anchorPane2.getChildren().remove(arrayListOfLink.get(i).lien2);
				anchorPane2.getChildren().remove(arrayListOfLink.get(i).lien3);
				arrayListOfLink.remove(i);
			}
		}
	}

    /**
     * Actualise les liens entre les objets.
     * L'idée est de regarder tous les liens existant avec la boucle et si il y a un lien qui est relier d'un cote a l'image alors on
     * supprime les liens et on appel AddLink pour les remettre au bon endroit
     * @param image objet que l'on deplace
     * @param anchorPane2 zone ou l'on travail
     */
    public static void actualiseViewOfLink(GraphicalComponent image, AnchorPane anchorPane2) {
        for (int i = 0; i < arrayListOfLink.size();i++){
			//On trouve tous les liens associes a l image et on les supprime pour les reaffiches comme si n existait pas
			//(avec k != -1 donc pas d ajout dans le breadboard)
            if(arrayListOfLink.get(i).image1 == image || arrayListOfLink.get(i).image2 == image){
                anchorPane2.getChildren().remove(arrayListOfLink.get(i).lien1);
                anchorPane2.getChildren().remove(arrayListOfLink.get(i).lien2);
                anchorPane2.getChildren().remove(arrayListOfLink.get(i).lien3);
                addLink(arrayListOfLink.get(i).image1,arrayListOfLink.get(i).image2,arrayListOfLink.get(i).linkAreaUsed1,arrayListOfLink.get(i).linkAreaUsed2,i, anchorPane2);
            }
        }
    }

	/**
	 * Supprime le composant dans la Breadboard et dans l'interface graphique, avec les liens qui lui sont associé
	 * @param composant composant à supprimer.
	 * @param anchorPane2 zone de travail
     */
	public  static void deleteComponent(GraphicalComponent composant, AnchorPane anchorPane2){
		//TODO supprimer le composant de la breadboard et les liens qui existent avec lui !
		int[] a = new int[arrayListOfLink.size()]; //Va permettre de sauvgarder en memoire les composant relies a celui qui doit etre supprimer
		int b = 0; //Compte le nombre de composants relies avec celui qui doit etre supprimer
		for ( int i =0 ; i < arrayListOfLink.size(); i++){
			//On garde que les valeurs qui sont en lien avec le composant a supprimer
			if(arrayListOfLink.get(i).image1 == composant || arrayListOfLink.get(i).image2 == composant){
				//On supprime les liens
				anchorPane2.getChildren().removeAll(arrayListOfLink.get(i).lien1,arrayListOfLink.get(i).lien2,arrayListOfLink.get(i).lien3);
				a[b]=i;//On garde en memoire dans les premiers cases les valeurs qui correspondent au lien a supprimer
				b += 1;
			}
			else{a[i]=0;}
		}
		for(int j = 0; j < b;j++){
			//On regarde toutes les valeurs retenu en memoire
			arrayListOfLink.remove(a[j]);//On les supprime
			for(int k = j + 1 ; k < b;k++){//On enleve ensuite 1 au valeur de a[k] car le remove a decaler le arrayListOfLink de 1...
				a[k] -= 1;
			}
		}
		//Enfin on supprime le composant en lui meme
		anchorPane2.getChildren().removeAll(composant.object,composant.square1,composant.square2,composant.square3,composant.square4);
	}

	/**
	 * Permet de faire apparaitre un generateur de tension
	 * @param anchorPane2 Zone a l interieur du scrollPane
	 * @param anchorPane4 Zone de depart des images
	 * @param scrollPane Zone pour mettre le circuit
	 */
	public static  void addVoltageGenerator (AnchorPane anchorPane2, AnchorPane anchorPane4, ScrollPane scrollPane, AnchorPane anchorPane, Button valeurADeterminer,Button TensionAImposer, Button CourantAImposer)
	{
		ImageView firstVoltageGenerator = new ImageView(); // On créer un object de type ImageView
		Image image1 = new Image("file:image/Generateur de tension h.png"); // On va la cherche au bonne endroit
		firstVoltageGenerator.setImage(image1);
		firstVoltageGenerator.setX(120);
		firstVoltageGenerator.setY(80); // On définit la position
		anchorPane4.getChildren().add(firstVoltageGenerator); // On affiche dans la zone voulu (anchorPane4)

        // Permet de faire apparaitre le generateur de tension dans la zone centrale en cliquant dans la zone des composants
		firstVoltageGenerator.setOnMouseClicked(event ->
		{	//Fait apparaitre différente image

			//L'image principale du composant
			ImageView tensionGenerator = new ImageView();
			Image image = new Image("file:image/Generateur de tension h.png");
			tensionGenerator.setImage(image);
			/**Arnaque du lambda proposer par Intellij qui permet d'outre passer le faite qu'on ne puisse pas
			 * modifier des varaibles dans les events*/
			final double[][] positionFenetre = {positionRelative(anchorPane2, scrollPane)};
			tensionGenerator.setX(100 + positionFenetre[0][0]);//valeur par default d'apparition
			tensionGenerator.setY(100 + positionFenetre[0][1]);//valeur par default d'apparition
			anchorPane2.getChildren().add(tensionGenerator);

			//Les 4 carrés noir autour (affiche 2 par 2)
			ImageView linkArea1 = new ImageView();
			Image square1 = new Image("file:image/LinkArea.png");
			linkArea1.setImage(square1);
			linkArea1.setFitHeight(10);//On redimensionne
			linkArea1.setFitWidth(10);
			linkArea1.setX(tensionGenerator.getX()); //Placer en fonction des valeurs par default
			linkArea1.setY(tensionGenerator.getY() + image.getHeight()/2);
			anchorPane2.getChildren().add(linkArea1);

			ImageView linkArea2 = new ImageView();
			Image square2 = new Image("file:image/LinkArea.png");
			linkArea2.setImage(square2);
			linkArea2.setFitHeight(10);
			linkArea2.setFitWidth(10);
			//One definit pas immediatement la position, on attendre d effectuer une rotation

			ImageView linkArea4 = new ImageView();
			Image square4 = new Image("file:image/LinkArea.png");
			linkArea4.setImage(square4);
			linkArea4.setFitHeight(10);
			linkArea4.setFitWidth(10);
			//De même

			ImageView linkArea3 = new ImageView();
			Image square3 = new Image("file:image/LinkArea.png");
			linkArea3.setImage(square3);
			linkArea3.setFitHeight(10);
			linkArea3.setFitWidth(10);
			linkArea3.setX(tensionGenerator.getX() + image.getWidth());
			linkArea3.setY(tensionGenerator.getY() + image.getHeight()/2);
			anchorPane2.getChildren().add(linkArea3);



			tensionGenerator.setLayoutX(idVoltageGenerator); // Donne un identifiant unique au generateur de tension.
			// Pas très propre d'utiliser Layout mais permet de garder en mémoire en valeur, mais sans conséquence après affichage
			idVoltageGenerator += 1;
            //On creer l'objet
			GraphicalComponent componentVoltageGenerator = new GraphicalComponent(tensionGenerator,linkArea1,linkArea2,linkArea3,linkArea4,'h',"Generateur de tension " + idVoltageGenerator, 10, Type.VOLTAGEGENERATOR);
			
			// TODO Pour Sterenn : faire en sorte d'ajouter correctement un nouveau composant avec les vertex adéquats
			// breadboard.addComponent(new VoltageGenerator(componentVoltageGenerator.name,new Vertex(vertexIndex),new Vertex(vertexIndex+1)));
			// vertexIndex+=2;

			//Permet de voir le nom et la valeur du composant quand la souris entre dans l'image
            tensionGenerator.setOnMouseEntered(event3 -> {
				//On met les bonnes valeur dans les variables qu on va affiche, on affiche qu il faut determiner les parametre si ils n ont pas encore ete
				//donnee en argument
				if(componentVoltageGenerator.courant == null){
					componentCourantToShow = "à determiner";
				}
				else{
					componentCourantToShow = "" + componentVoltageGenerator.courant; // C'est un cast de double dans un String
				}
				if(componentVoltageGenerator.voltage == null){
					componentVoltageToShow = "à determiner";
				}
				else{
					componentVoltageToShow = "" + componentVoltageGenerator.voltage; // C'est un cast de double dans un String
				}
				if(componentVoltageGenerator.value == null){
					componentValueToShow = "à determiner";
				}
				else{
					componentValueToShow= "" + componentVoltageGenerator.value; // C'est un cast de double dans un String
				}
				//On cree deux messages en fonctions de l'orientation de l'image
				Text informationsh = new Text("Nom : " + componentVoltageGenerator.name + "\nValeur : " + componentValueToShow
						+ "\nValeur du courant : " + componentCourantToShow + "\nValeur de tension : " + componentVoltageToShow);
				Text informationsv = new Text("Nom : " + componentVoltageGenerator.name + "\nValeur : " + componentValueToShow
						+ "\nValeur du courant : " + componentCourantToShow + "\nValeur de tension : " + componentVoltageToShow);

				// On definie la zone des deux informations.
				informationsh.setX(tensionGenerator.getX());
				informationsh.setY(tensionGenerator.getY() + 60);

				informationsv.setX(tensionGenerator.getX() + 80);
				informationsv.setY(tensionGenerator.getY());

				//On affiche le bon en fonction de l orientation
				if(componentVoltageGenerator.orientation == 'v') {
					anchorPane2.getChildren().add(informationsv); //On affiche les informations
				}
				else{
					anchorPane2.getChildren().add(informationsh); //On affiche les informations

				}
				tensionGenerator.setOnMouseExited(event4 ->{ //On supprime les informations quand la souris sort de la zone
                    anchorPane2.getChildren().removeAll(informationsh,informationsv);

                });
            });

			//Permet de creer un lien en cliquant sur les deux zones
			linkArea1.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					addLink(premiereImageDuLien,componentVoltageGenerator,linkArea,1,-1,anchorPane2);
					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					premiereImageDuLien = componentVoltageGenerator; // On enregistre l'image
					linkArea = 1;//C est la premier zone qui a ete utilise
				}
			});
			linkArea2.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					addLink(premiereImageDuLien,componentVoltageGenerator,linkArea,2,-1,anchorPane2);
					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					premiereImageDuLien = componentVoltageGenerator; // On enregistre l'image
					linkArea = 2;
				}
			});
			linkArea3.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					addLink(premiereImageDuLien,componentVoltageGenerator,linkArea,3,-1,anchorPane2);
					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					premiereImageDuLien = componentVoltageGenerator; // On enregistre l'image
					linkArea = 3;
				}
			});
			linkArea4.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					addLink(premiereImageDuLien,componentVoltageGenerator,linkArea,4,-1,anchorPane2);
					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					premiereImageDuLien = componentVoltageGenerator; // On enregistre l'image
					linkArea = 4;
				}
			});


			//Permet de selectionner la valeur à déterminer.

			tensionGenerator.setOnMouseClicked(event1 -> {
				if(etat == "v"){
					valueToShow = componentVoltageGenerator;
					etat = "d";
					valeurADeterminer.setText("Choix pris en compte");
				}

				if(etat == "tai"){
					System.out.println("On va eviter de faire des betises...");
				}
				if(etat == "cai") {
					Text info = new Text("Par convention, fleche du courant est oriente \n de bas en haut ou de gauche a droite");
					info.setX(tensionGenerator.getX() + 100);
					info.setY(tensionGenerator.getY() + 60);
					anchorPane2.getChildren().add(info);

					TextField zonePourChangerValeur = new TextField("Entrer votre valeur");
					zonePourChangerValeur.setLayoutX(tensionGenerator.getX() + 110);
					zonePourChangerValeur.setLayoutY(tensionGenerator.getY() - 30);
					anchorPane2.getChildren().add(zonePourChangerValeur);
					zonePourChangerValeur.setOnAction(event8 -> {
						String a = zonePourChangerValeur.getText();
						double x;
						//On essaie de mettre la valeur rentrer dans un double
						//Ce qui permet d'avoir que des double de partout mais aussi de verifier qu on a bien un nombre et pas un mot
						//On suppose que l utilisateur n est pas idiot et ne va pas rentrer une valeur de resistance negative par exemple
						try {
							x = Double.parseDouble(a);//Si on arrive a cast alors on fait les actions qui suivent
							//TODO Actualiser la valeur dans la breadboard
							anchorPane2.getChildren().remove(zonePourChangerValeur);
							componentVoltageGenerator.courant = x;
							etat = "d";
							CourantAImposer.setText("Courant à imposer");
							anchorPane2.getChildren().remove(info);
						} catch (NumberFormatException erreur) {//Sinon on demande a l utilisateur de remettre une autre valeur
							zonePourChangerValeur.setText("Entrer une valeur correct");
						}
					});

				}
			});

			//Menu qui s'affiche quand on effectue un clic droit sur l'objet

			ContextMenu menu = new ContextMenu();
			MenuItem rotation = new MenuItem("Effectuer une rotation de +90°");
			MenuItem changeValue = new MenuItem("Changer de valeur");
			MenuItem delete = new MenuItem("Supprimer le composant");
			MenuItem changeName = new MenuItem("Changer le nom");

			delete.setOnAction(event1 -> {
				//Permet de supprimer le composant et les liens qui vont avec
				deleteComponent(componentVoltageGenerator,anchorPane2);
			});

			changeName.setOnAction(event1 -> {//Permet de changer le nom du composant

				//Zone de texte interractive
				TextField zonePourChangerName = new TextField("Entrer un nouveau nom");
				// positionFenetre contient les valeurs du coin en haut a gauche visible de l'anchorPane2
				positionFenetre[0] = positionRelative(anchorPane2,scrollPane);
				zonePourChangerName.setLayoutX(tensionGenerator.getX());
				zonePourChangerName.setLayoutY(tensionGenerator.getY() - 25);
				anchorPane2.getChildren().add(zonePourChangerName); //On affiche
				zonePourChangerName.setOnAction(event8 ->{
					String a = zonePourChangerName.getText();
					if (a.equals("Tanguy")) {//Ce teste permet de mettre en evidence l'importance de certain nom de composant
						System.out.println("Excellent choix !");
					}
					componentVoltageGenerator.name = a;
					//TODO Actualiser le nom dans la breadboard
					anchorPane2.getChildren().remove(zonePourChangerName);//on enleve la zone d'affichage du message

				});
			});

			changeValue.setOnAction(event1 ->{
				//Permet de changer la valeur du composant, les unités sont celles utilise habituellement en electronique
				TextField zonePourChangerValeur = new TextField("Entrer votre valeur");
				// positionFenetre contient les valeurs du coin en haut a gauche visible de l'anchorPane2
				positionFenetre[0] = positionRelative(anchorPane2,scrollPane);
				zonePourChangerValeur.setLayoutX(tensionGenerator.getX());
				zonePourChangerValeur.setLayoutY(tensionGenerator.getY()  - 30);
				anchorPane2.getChildren().add(zonePourChangerValeur);
				//Affiche des informations sur l orientation de la tension
				Text info = new Text("Par convention, fleche de la tension est orientee \n de bas en haut ou de gauche a droite");
				info.setX(tensionGenerator.getX() + 100);
				info.setY(tensionGenerator.getY() + 60);
				anchorPane2.getChildren().add(info);
				zonePourChangerValeur.setOnAction(event8 ->{
					String a = zonePourChangerValeur.getText();
					double x;

					//Petit differecne ici, on essaie de mettre la valeur rentrer dans un double
					//Ce qui permet d'avoir que des double de partout mais aussi de verifier qu on a bien un nombre et pas un mot
					try {x = Double.parseDouble(a);//Si on arrive a cast alors on fait les actions qui suivent
						componentVoltageGenerator.value = x;
						//TODO Actualiser la valeur dans la breadboard
						anchorPane2.getChildren().remove(zonePourChangerValeur);
						anchorPane2.getChildren().remove(info);
						componentVoltageGenerator.voltage = x;
					}
					catch(NumberFormatException erreur){//Sinon on demande a l utilisateur de remettre une autre valeur
						zonePourChangerValeur.setText("Entrer une valeur correct");
					}
				});
			});

			rotation.setOnAction(event1 -> {
				//Permet d'effectuer une rotation pour mettre le composant a la verticale ou a l horizontale
				rotationPossible = true; //impossible de faire une rotation si des liens existent deja
				for(int i = 0; i < arrayListOfLink.size(); i++){
					//On regarde si des liens existent deja, on pourrait limite faire un "break" pour quitter la boucle
					if(arrayListOfLink.get(i).image1.object == tensionGenerator || arrayListOfLink.get(i).image2.object == tensionGenerator){
						rotationPossible = false;
					}
				}

				if(rotationPossible) {
					if (tensionGenerator.getRotate() == 0) {
						rotation.setText("Effectuer une rotation de +90°");

						tensionGenerator.setRotate(90);//Fonction bien pratique qui retourne visuellement l'image
						//En revanche aucune de ces caracteristique a ete modifie.


						/*Les valeurs 50/25/75 proviennent rigoureusement de calcul sur la taille de l'image, mais en sachant que
						la hauteur vaut 50 et la longueur 100 on evite des longues formules
						C'est aussi ici qu'on definie la position des carres pour la premiere fois*/
						linkArea2.setX(tensionGenerator.getX() + 50);
						linkArea2.setY(tensionGenerator.getY() - 25);
						linkArea4.setX(tensionGenerator.getX() + 50);
						linkArea4.setY(tensionGenerator.getY() + 75);

						//On supprime visuellement les zones inutiles et on affiche les nouvelles
						anchorPane2.getChildren().removeAll(linkArea1, linkArea3);
						anchorPane2.getChildren().addAll(linkArea2, linkArea4);

						componentVoltageGenerator.orientation = 'v';//On retient qu'un rotation a ete effectue
					} else {
						//On fait de meme mais sans definir la position des carres
						rotation.setText("Effectuer une rotation de -90°");

						anchorPane2.getChildren().removeAll(linkArea2, linkArea4);
						anchorPane2.getChildren().addAll(linkArea3, linkArea1);
						tensionGenerator.setRotate(0);

						componentVoltageGenerator.orientation = 'h';
					}
				}
				else {System.out.println("Rotation impossible");} //Dans le cas ou des liens existent deja
			});


			menu.getItems().addAll(changeName,changeValue,rotation,delete);//Permet d'ajouter les different bouton au menu


			tensionGenerator.setOnContextMenuRequested(event11 ->{
				menu.show(tensionGenerator, Side.BOTTOM,0,0);//Fait apparaitre le menu qui disparrait automatiquement lorsqu on clique ailleurs
			});

           	// Permet de deplacer le generateur de tension d'un point a un autre
			tensionGenerator.setOnMouseDragged(event1 -> 
			{
				if (event1.isPrimaryButtonDown()) {
					if (etat == "d") { //En position Drag and Drop
						// position de l'image
						double imagx;
						double imagy;
						if(tensionGenerator.getRotate() == 0) {//Dans le bon sens
							imagx = image.getWidth();
							imagy = image.getHeight();
						}
						else{//rotation 90° donc on inverse la hauteur et la largeur
							imagy = image.getWidth();
							imagx = image.getHeight();
						}

						// positionFenetre contient les valeurs du coin en haut a gauche visible de l'anchorPane2
						positionFenetre[0] = positionRelative(anchorPane2,scrollPane);

						// on récupère la taille de la fenêtre
						double x = event1.getSceneX() + positionFenetre[0][0];
						double y = event1.getSceneY() + positionFenetre[0][1];

						//On effectue cet ajustement car les coordonées de la souris sont calculées a partir du cadre global
						//alors que celles du composant à partir de anchorPane2
						y = y - 60;

						// Permet de ne pas sortir du cadre
						if (x < (imagx / 2)) { // on evite de sortir du cadre a gauche
							x = (imagx / 2);
						}
						if (y < (imagy / 2)) { // on evite de sortir du cadre en haut
							y = (imagy / 2);
						}

						double mx = anchorPane2.getWidth();

						double my = anchorPane2.getHeight();

						if (x + imagx / 2 > mx ) {// on evite de sortir du cadre a droite
							x = mx - imagx / 2 ;
						}

						/* Ici on prend la taille de l'écran, on lui enlève la scrollbare et on regarde si le bord de l'image
						 * c est a dire le centre x plus la largeur de l image divise par 2 */
						if (y + imagy / 2 > my ) {// on evite de sortir du cadre en bas
							y = my - imagy / 2 ;
						}

						//Ici on repositionne l'image est les 4 potentiel carré noir autour
						tensionGenerator.setX(x - imagx / 2);
						tensionGenerator.setY(y - imagy / 2);

						linkArea3.setX(tensionGenerator.getX() + image.getWidth());
						linkArea3.setY(tensionGenerator.getY() + image.getHeight() / 2);
						linkArea1.setX(tensionGenerator.getX());
						linkArea1.setY(tensionGenerator.getY() + image.getHeight() / 2);
						linkArea2.setX(tensionGenerator.getX() + imagx);
						linkArea2.setY(tensionGenerator.getY() - 25);
						linkArea4.setX(tensionGenerator.getX() + imagx);
						linkArea4.setY(tensionGenerator.getY() + 75);

						actualiseViewOfLink(componentVoltageGenerator, anchorPane2); //On actualise les liens
					}
				}
			});

		});
	}
	
	/**
	 * Permet de faire apparaitre un generateur de courant
	 * @param anchorPane2 Zone a l interieur du scrollPane
	 * @param anchorPane4 Zone de depart des images
	 * @param scrollPane Zone pour mettre le circuit
	 */
	public static void addCurrentGenerator (AnchorPane anchorPane2, AnchorPane anchorPane4, ScrollPane scrollPane, Button valeurADeterminer, Button TensionAImposer,Button CourantAImposer)
	{
		ImageView firstCourantGenerator = new ImageView();
		Image image2 = new Image("file:image/Generateur de courant h.png");
		firstCourantGenerator.setImage(image2);
		firstCourantGenerator.setX(10);
		firstCourantGenerator.setY(80);
		anchorPane4.getChildren().add(firstCourantGenerator);

		
        //Permet de faire apparaitre le generateur de courant
		firstCourantGenerator.setOnMouseClicked(event -> {
			ImageView courantGenerator = new ImageView();
			Image image = new Image("file:image/Generateur de courant h.png");
			courantGenerator.setImage(image);
			final double[][] positionFenetre = {positionRelative(anchorPane2, scrollPane)};
			courantGenerator.setX(230 + positionFenetre[0][0]);//valeur par default d'apparition
			courantGenerator.setY(100 + positionFenetre[0][1]);//valeur par default d'apparition
			anchorPane2.getChildren().add(courantGenerator);

			ImageView linkArea1 = new ImageView();
			Image square1 = new Image("file:image/LinkArea.png");
			linkArea1.setImage(square1);
			linkArea1.setFitHeight(10);//On redimensionne
			linkArea1.setFitWidth(10);
			linkArea1.setX(courantGenerator.getX()); //Placer en fonction des valeurs par default
			linkArea1.setY(courantGenerator.getY() + image.getHeight()/2);
			anchorPane2.getChildren().add(linkArea1);

			ImageView linkArea2 = new ImageView();
			Image square2 = new Image("file:image/LinkArea.png");
			linkArea2.setImage(square2);
			linkArea2.setFitHeight(10);
			linkArea2.setFitWidth(10);


			ImageView linkArea4 = new ImageView();
			Image square4 = new Image("file:image/LinkArea.png");
			linkArea4.setImage(square4);
			linkArea4.setFitHeight(10);
			linkArea4.setFitWidth(10);


			ImageView linkArea3 = new ImageView();
			Image square3 = new Image("file:image/LinkArea.png");
			linkArea3.setImage(square3);
			linkArea3.setFitHeight(10);
			linkArea3.setFitWidth(10);
			linkArea3.setX(courantGenerator.getX() + image.getWidth());
			linkArea3.setY(courantGenerator.getY() + image.getHeight()/2);
			anchorPane2.getChildren().add(linkArea3);

			courantGenerator.setLayoutX(idCourantgeGenerator);
			idCourantgeGenerator += 1;
			GraphicalComponent componentCourantGenerator = new GraphicalComponent(courantGenerator,linkArea1,linkArea2,linkArea3,linkArea4,'h',"Generateur de courant " + idCourantgeGenerator,10, Type.CURRENTGENERATOR);

			// TODO Pour Sterenn : faire en sorte d'ajouter correctement un nouveau composant avec les vertex adéquats
			//breadboard.addComponent(new CurrentGenerator(componentCourantGenerator.name, new Vertex(vertexIndex), new Vertex(vertexIndex+1)));
			//vertexIndex+=2;

			//Permet de voir le nom et la valeur du composant quand la souris entre dans l'image
			courantGenerator.setOnMouseEntered(event3 -> {
				//On met les bonnes valeur dans les variables qu on va affiche, on affiche qu il faut determiner les parametre si ils n ont pas encore ete
				//donnee en argument
				if(componentCourantGenerator.courant == null){
					componentCourantToShow = "à determiner";
				}
				else{
					componentCourantToShow = "" + componentCourantGenerator.courant; // C'est un cast de double dans un String
				}
				if(componentCourantGenerator.voltage == null){
					componentVoltageToShow = "à determiner";
				}
				else{
					componentVoltageToShow = "" + componentCourantGenerator.voltage; // C'est un cast de double dans un String
				}
				if(componentCourantGenerator.value == null){
					componentValueToShow = "à determiner";
				}
				else{
					componentValueToShow= "" + componentCourantGenerator.value; // C'est un cast de double dans un String
				}
				//On cree deux messages en fonctions de l'orientation de l'image
				Text informationsh = new Text("Nom : " + componentCourantGenerator.name + "\nValeur : " + componentValueToShow
						+ "\nValeur du courant : " + componentCourantToShow + "\nValeur de tension : " + componentVoltageToShow);
				Text informationsv = new Text("Nom : " + componentCourantGenerator.name + "\nValeur : " + componentValueToShow
						+ "\nValeur du courant : " + componentCourantToShow + "\nValeur de tension : " + componentVoltageToShow);




				// On definie la zone des deux informations.
				informationsh.setX(courantGenerator.getX());
				informationsh.setY(courantGenerator.getY() + 60);

				informationsv.setX(courantGenerator.getX() + 80);
				informationsv.setY(courantGenerator.getY());

				//On affiche le bon en fonction de l orientation
				if(componentCourantGenerator.orientation == 'v') {
					anchorPane2.getChildren().add(informationsv); //On affiche les informations
				}
				else{
					anchorPane2.getChildren().add(informationsh); //On affiche les informations

				}
				courantGenerator.setOnMouseExited(event4 ->{ //On supprime les informations quand la souris sort de la zone
					anchorPane2.getChildren().removeAll(informationsh,informationsv);

				});
			});

			//Permet de creer un lien

			linkArea1.setOnMouseClicked(event1 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					addLink(premiereImageDuLien,componentCourantGenerator,linkArea,1,-1,anchorPane2);

					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					premiereImageDuLien = componentCourantGenerator; // On enregistre l'image
					linkArea = 1;
				}
			});

			linkArea2.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					addLink(premiereImageDuLien,componentCourantGenerator,linkArea,2,-1,anchorPane2);
					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					premiereImageDuLien = componentCourantGenerator; // On enregistre l'image
					linkArea = 2;
				}
			});

			linkArea3.setOnMouseClicked(event3 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					addLink(premiereImageDuLien,componentCourantGenerator,linkArea,3,-1,anchorPane2);

					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					premiereImageDuLien = componentCourantGenerator; // On enregistre l'image
					linkArea = 3;
				}
			});

			linkArea4.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					addLink(premiereImageDuLien,componentCourantGenerator,linkArea,4,-1,anchorPane2);

					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					premiereImageDuLien = componentCourantGenerator; // On enregistre l'image
					linkArea = 4;
				}
			});

			//Permet de selectionner la valeur à déterminer.

			courantGenerator.setOnMouseClicked(event1 -> {
				if(etat == "v"){
					valueToShow = componentCourantGenerator;
					etat = "d";
					valeurADeterminer.setText("Choix pris en compte");
				}

				if(etat == "tai"){
					Text info = new Text("Par convention, fleche de la tension est oriente \n de bas en haut ou de gauche a droite");
					info.setX(courantGenerator.getX() + 100);
					info.setY(courantGenerator.getY() + 60);
					anchorPane2.getChildren().add(info);

					TextField zonePourChangerValeur = new TextField("Entrer votre valeur");
					zonePourChangerValeur.setLayoutX(courantGenerator.getX() + 100);
					zonePourChangerValeur.setLayoutY(courantGenerator.getY());
					anchorPane2.getChildren().add(zonePourChangerValeur);
					zonePourChangerValeur.setOnAction(event8 ->{
						String a = zonePourChangerValeur.getText();
						double x;
						//On essaie de mettre la valeur rentrer dans un double
						//Ce qui permet d'avoir que des double de partout mais aussi de verifier qu on a bien un nombre et pas un mot
						//On suppose que l utilisateur n est pas idiot et ne va pas rentrer une valeur de resistance negative par exemple
						try {x = Double.parseDouble(a);//Si on arrive a cast alors on fait les actions qui suivent
							//TODO Actualiser la valeur dans la breadboard
							anchorPane2.getChildren().remove(zonePourChangerValeur);
							componentCourantGenerator.voltage = x;
							etat = "d";
							TensionAImposer.setText("Tension à imposer");
							anchorPane2.getChildren().remove(info);
						}
						catch(NumberFormatException erreur){//Sinon on demande a l utilisateur de remettre une autre valeur
							zonePourChangerValeur.setText("Entrer une valeur correct");
						}
					});

				}
			});


			//Menu qui s'affiche quand on effectue un clic droit sur l'objet

			ContextMenu menu = new ContextMenu();
			MenuItem rotation = new MenuItem("Effectuer une rotation de +90°");
			MenuItem changeValue = new MenuItem("Changer de valeur");
			MenuItem delete = new MenuItem("Supprimer le composant");
			MenuItem changeName = new MenuItem("Changer le nom");

			delete.setOnAction(event1 -> {
				deleteComponent(componentCourantGenerator,anchorPane2);
			});

			changeName.setOnAction(event1 -> {
				TextField zonePourChangerName = new TextField("Entrer un nouveau nom");
				zonePourChangerName.setLayoutX(courantGenerator.getX());
				zonePourChangerName.setLayoutY(courantGenerator.getY() - 25);
				anchorPane2.getChildren().add(zonePourChangerName);
				zonePourChangerName.setOnAction(event8 ->{
					String a = zonePourChangerName.getText();
					if (a.equals("Tanguy")) {
						System.out.println("Excellent choix !");
					}
					componentCourantGenerator.name = a;
					//TODO Actualiser le nom dans la breadboard
					anchorPane2.getChildren().remove(zonePourChangerName);

				});
			});

			changeValue.setOnAction(event1 ->{

				//On affiche les informations concernant l'orientation de la tension
				Text info = new Text("Par convention, fleche de la tension est oriente \n de bas en haut ou de gauche a droite");
				info.setX(courantGenerator.getX() + 100);
				info.setY(courantGenerator.getY() + 60);
				anchorPane2.getChildren().add(info);

				TextField zonePourChangerValeur = new TextField("Entrer une valeur");
				zonePourChangerValeur.setLayoutX(courantGenerator.getX());
				zonePourChangerValeur.setLayoutY(courantGenerator.getY() - 25);
				anchorPane2.getChildren().add(zonePourChangerValeur);
				zonePourChangerValeur.setOnAction(event8 ->{
					String a = zonePourChangerValeur.getText();
					double x;
					try {x = Double.parseDouble(a);
						componentCourantGenerator.value = x;
						//TODO Actualiser la valeur dans la breadboard
						anchorPane2.getChildren().remove(zonePourChangerValeur);
						anchorPane2.getChildren().remove(info);
						componentCourantGenerator.courant = x;
					}
					catch(NumberFormatException erreur){
						zonePourChangerValeur.setText("Entrer une valeur correct");
					}
				});
			});

			rotation.setOnAction(event1 -> {
				rotationPossible = true;
				for(int i = 0; i < arrayListOfLink.size(); i++){
					if(arrayListOfLink.get(i).image1.object == courantGenerator || arrayListOfLink.get(i).image2.object == courantGenerator){
						rotationPossible = false;
					}
				}

				if(rotationPossible) {
					if (courantGenerator.getRotate() == 0) {
						rotation.setText("Effectuer une rotation de +90°");

						courantGenerator.setRotate(90);

						linkArea2.setX(courantGenerator.getX() + 50);
						linkArea2.setY(courantGenerator.getY() - 25);
						linkArea4.setX(courantGenerator.getX() + 50);
						linkArea4.setY(courantGenerator.getY() + 75);

						anchorPane2.getChildren().removeAll(linkArea1, linkArea3);
						anchorPane2.getChildren().addAll(linkArea2, linkArea4);

						componentCourantGenerator.orientation = 'v';
					} else {
						anchorPane2.getChildren().removeAll(linkArea2, linkArea4);
						anchorPane2.getChildren().addAll(linkArea3, linkArea1);
						courantGenerator.setRotate(0);
						rotation.setText("Effectuer une rotation de -90°");

						componentCourantGenerator.orientation = 'h';
					}
				}
				else {System.out.println("Rotation impossible");}
			});


			menu.getItems().addAll(changeName,changeValue,rotation,delete);


			courantGenerator.setOnContextMenuRequested(event11 ->{
				menu.show(courantGenerator, Side.BOTTOM,0,0);
			});

			/*
            Permet de deplacer le generateur de courant d'un point a un autre
			 */

			courantGenerator.setOnMouseDragged(event1 -> 
			{
				if (etat == "d" && event1.isPrimaryButtonDown()) { //En position Drag and Drop
					// position de l'image
					double imagx;
					double imagy;
					if(courantGenerator.getRotate() == 0) {//Dans le bon sens
						imagx = image.getWidth();
						imagy = image.getHeight();
					}
					else{//rotation 90° donc on inverse la hauteur et la largeur
						imagy = image.getWidth();
						imagx = image.getHeight();
					}

					// positionFenetre contient les valeurs du coin en haut a gauche visible de l'anchorPane2
					positionFenetre[0] = positionRelative(anchorPane2,scrollPane);

					// on récupère la taille de la fenêtre
					double x = event1.getSceneX() + positionFenetre[0][0];
					double y = event1.getSceneY() + positionFenetre[0][1];

					//On effectue cet ajustement car les coordonées de la souris sont calculées a partir du cadre global
					//alors que celles du composant à partir de anchorPane2
					y = y - 60;

					// Permet de ne pas sortir du cadre
					if (x < (imagx / 2)) { // on evite de sortir du cadre a gauche
						x = (imagx / 2);
					}
					if (y < (imagy / 2)) { // on evite de sortir du cadre en haut
						y = (imagy / 2);
					}

					double mx = anchorPane2.getWidth();

					double my = anchorPane2.getHeight();

					if (x + imagx / 2 > mx ) {// on evite de sortir du cadre a droite
						x = mx - imagx / 2 ;
					}

						/* Ici on prend la taille de l'écran, on lui enlève la scrollbare et on regarde si le bord de l'image
						 * c est a dire le centre x plus la largeur de l image divise par 2 */
					if (y + imagy / 2 > my ) {// on evite de sortir du cadre en bas
						y = my - imagy / 2 ;
					}

					//Ici on repositionne l'image est les 4 potentiel carré noir autour
					courantGenerator.setX(x - imagx / 2);
					courantGenerator.setY(y - imagy / 2);

					linkArea3.setX(courantGenerator.getX() + image.getWidth());
					linkArea3.setY(courantGenerator.getY() + image.getHeight() / 2);
					linkArea1.setX(courantGenerator.getX());
					linkArea1.setY(courantGenerator.getY() + image.getHeight() / 2);
					linkArea2.setX(courantGenerator.getX() + imagx);
					linkArea2.setY(courantGenerator.getY() - 25);
					linkArea4.setX(courantGenerator.getX() + imagx);
					linkArea4.setY(courantGenerator.getY() + 75);

					actualiseViewOfLink(componentCourantGenerator,anchorPane2);

				}
			});
		});
	}

	/**
	 * Permet de faire apparaitre un noeud
	 * @param anchorPane2 Zone a l interieur du scrollPane
	 * @param anchorPane4 Zone de depart des images
	 * @param scrollPane Zone pour mettre le circuit
	 */
	public static void addNode (AnchorPane anchorPane2, AnchorPane anchorPane4, ScrollPane scrollPane, Button valeurADeterminer, Button TensionAImposer,Button CourantAImposer)
	{
		ImageView firstNode = new ImageView();
		Image image2 = new Image("file:image/Noeud.png");
		firstNode.setImage(image2);
		firstNode.setX(10);
		firstNode.setY(170);
		anchorPane4.getChildren().add(firstNode);

		// TODO Pour Sterenn : voir comment l'ajout d'un noeud influence la breadboard et ses composants avec les vertex
		
        //Permet de faire apparaitre un noeud

		firstNode.setOnMouseClicked(event -> 
		{
			ImageView node = new ImageView();
			Image image = new Image("file:image/Noeud.png");
			node.setImage(image);
			final double[][] positionFenetre = {positionRelative(anchorPane2, scrollPane)};
			node.setX(350 + positionFenetre[0][0]);//valeur par default d'apparition
			node.setY(100 + positionFenetre[0][1]);//valeur par default d'apparition
			anchorPane2.getChildren().add(node);

			ImageView linkArea3 = new ImageView();
			Image square3 = new Image("file:image/LinkArea.png");
			linkArea3.setImage(square3);
			linkArea3.setFitHeight(10);
			linkArea3.setFitWidth(10);
			linkArea3.setX(node.getX() + image.getWidth());
			linkArea3.setY(node.getY()  +image.getHeight()/2);
			anchorPane2.getChildren().add(linkArea3);

			ImageView linkArea1 = new ImageView();
			Image square1 = new Image("file:image/LinkArea.png");
			linkArea1.setImage(square1);
			linkArea1.setFitHeight(10);
			linkArea1.setFitWidth(10);
			linkArea1.setX(node.getX());
			linkArea1.setY(node.getY() + image.getHeight()/2);
			anchorPane2.getChildren().add(linkArea1);

			ImageView linkArea2 = new ImageView();
			Image square2 = new Image("file:image/LinkArea.png");
			linkArea2.setImage(square2);
			linkArea2.setFitHeight(10);
			linkArea2.setFitWidth(10);
			linkArea2.setX(node.getX() + image.getWidth()/2);
			linkArea2.setY(node.getY());
			anchorPane2.getChildren().add(linkArea2);

			ImageView linkArea4 = new ImageView();
			Image square4 = new Image("file:image/LinkArea.png");
			linkArea4.setImage(square4);
			linkArea4.setFitHeight(10);
			linkArea4.setFitWidth(10);
			linkArea4.setX(node.getX() + image.getWidth()/2);
			linkArea4.setY(node.getY() + image.getHeight());
			anchorPane2.getChildren().add(linkArea4);

			node.setLayoutX(idNode);
			idNode += 1;
			GraphicalComponent componentNode = new GraphicalComponent(node,linkArea1,linkArea2,linkArea3,linkArea4,'t',"Noeud " + idNode,0, Type.NULL);


			//Permet de creer un lien
			linkArea1.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					addLink(premiereImageDuLien,componentNode,linkArea,1,-1,anchorPane2);

					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					linkArea = 1;
					premiereImageDuLien = componentNode; // On enregistre l'image
				}
			});
			linkArea2.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					addLink(premiereImageDuLien,componentNode,linkArea,2,-1,anchorPane2);

					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					linkArea = 2;
					premiereImageDuLien = componentNode; // On enregistre l'image
				}
			});
			linkArea3.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					addLink(premiereImageDuLien,componentNode,linkArea,3,-1,anchorPane2);

					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					linkArea = 3;
					premiereImageDuLien = componentNode; // On enregistre l'image
				}
			});
			linkArea4.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					addLink(premiereImageDuLien,componentNode,linkArea,4,-1,anchorPane2);

					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					linkArea = 4;
					premiereImageDuLien = componentNode; // On enregistre l'image
				}
			});


            //Permet de deplacer le noeud d'un point a un autre
			node.setOnMouseDragged(event1 ->
			{
				if (event1.isPrimaryButtonDown()) {
					if (etat == "d") { //En position Drag and Drop
						// position de l'image
						double imagx;
						double imagy;
						if (node.getRotate() == 0) {//Dans le bon sens
							imagx = image.getWidth();
							imagy = image.getHeight();
						} else {//rotation 90° donc on inverse la hauteur et la largeur
							imagy = image.getWidth();
							imagx = image.getHeight();
						}

						// positionFenetre contient les valeurs du coin en haut a gauche visible de l'anchorPane2
						positionFenetre[0] = positionRelative(anchorPane2, scrollPane);

						// on récupère la taille de la fenêtre
						double x = event1.getSceneX() + positionFenetre[0][0];
						double y = event1.getSceneY() + positionFenetre[0][1];

						//On effectue cet ajustement car les coordonées de la souris sont calculées a partir du cadre global
						//alors que celles du composant à partir de anchorPane2
						y = y - 60;

						// Permet de ne pas sortir du cadre
						if (x < (imagx / 2)) { // on evite de sortir du cadre a gauche
							x = (imagx / 2);
						}
						if (y < (imagy / 2)) { // on evite de sortir du cadre en haut
							y = (imagy / 2);
						}

						double mx = anchorPane2.getWidth();

						double my = anchorPane2.getHeight();

						if (x + imagx / 2 > mx) {// on evite de sortir du cadre a droite
							x = mx - imagx / 2;
						}

						/* Ici on prend la taille de l'écran, on lui enlève la scrollbare et on regarde si le bord de l'image
						 * c est a dire le centre x plus la largeur de l image divise par 2 */
						if (y + imagy / 2 > my) {// on evite de sortir du cadre en bas
							y = my - imagy / 2;
						}

						//Ici on repositionne l'image est les 4 potentiel carré noir autour
						node.setX(x - imagx / 2);
						node.setY(y - imagy / 2);

						linkArea3.setX(node.getX() + image.getWidth());
						linkArea3.setY(node.getY() + image.getHeight() / 2);
						linkArea1.setX(node.getX());
						linkArea1.setY(node.getY() + image.getHeight() / 2);
						linkArea2.setX(node.getX() + image.getWidth() / 2);
						linkArea2.setY(node.getY());
						linkArea4.setX(node.getX() + image.getWidth() / 2);
						linkArea4.setY(node.getY() + image.getHeight());

						actualiseViewOfLink(componentNode, anchorPane2); //On actualise les liens
					}
				}
			});
		});
	}


	/**
	 * Permet de faire apparaitre une résistance
	 * @param anchorPane2 Zone a l'interieur du scrollPane
	 * @param anchorPane4 Zone de depart des images
	 * @param scrollPane Zone pour mettre le circuit
	 */
	public static  void addResistance (AnchorPane anchorPane2, AnchorPane anchorPane4, ScrollPane scrollPane, AnchorPane anchorPane, Button valeurADeterminer, Button TensionAImposer, Button CourantAImposer)
	{
		ImageView firstResistance = new ImageView(); // On créer un object de type ImageView
		Image image1 = new Image("file:image/resistance.png"); // On va la cherche au bonne endroit
		firstResistance.setImage(image1);
		firstResistance.setFitHeight(50);
		firstResistance.setFitWidth(100);
		firstResistance.setX(120);
		firstResistance.setY(180); // On définit la position
		anchorPane4.getChildren().add(firstResistance); // On affiche dans la zone voulu (anchorPane4)


		// Permet de faire apparaitre une resistance dans la zone centrale en cliquant dans la zone des composants
		firstResistance.setOnMouseClicked(event ->
		{
			//Fait apparaitre différente image
			ImageView resistance = new ImageView();
			Image image = new Image("file:image/resistance.png");
			resistance.setImage(image);
			resistance.setFitHeight(50);
			resistance.setFitWidth(100);
			final double[][] positionFenetre = {positionRelative(anchorPane2, scrollPane)};
			resistance.setX(450 + positionFenetre[0][0]);//valeur par default d'apparition
			resistance.setY(100 + positionFenetre[0][1]);//valeur par default d'apparition
			anchorPane2.getChildren().add(resistance);

			ImageView linkArea1 = new ImageView();
			Image square1 = new Image("file:image/LinkArea.png");
			linkArea1.setImage(square1);
			linkArea1.setFitHeight(10);//On redimensionne
			linkArea1.setFitWidth(10);
			linkArea1.setX(resistance.getX()); //Placer en fonction des valeurs par default
			linkArea1.setY(resistance.getY() + image.getHeight()/2);
			anchorPane2.getChildren().add(linkArea1);

			ImageView linkArea2 = new ImageView();
			Image square2 = new Image("file:image/LinkArea.png");
			linkArea2.setImage(square2);
			linkArea2.setFitHeight(10);
			linkArea2.setFitWidth(10);


			ImageView linkArea4 = new ImageView();
			Image square4 = new Image("file:image/LinkArea.png");
			linkArea4.setImage(square4);
			linkArea4.setFitHeight(10);
			linkArea4.setFitWidth(10);

			ImageView linkArea3 = new ImageView();
			Image square3 = new Image("file:image/LinkArea.png");
			linkArea3.setImage(square3);
			linkArea3.setFitHeight(10);
			linkArea3.setFitWidth(10);
			linkArea3.setX(resistance.getX() + image.getWidth());
			linkArea3.setY(resistance.getY() + image.getHeight()/2);
			anchorPane2.getChildren().add(linkArea3);



			resistance.setLayoutX(idResistance); // Donne un identifiant unique a la resistance
			idResistance += 1;
			//On creer l'objet
			GraphicalComponent componentResistance = new GraphicalComponent(resistance,linkArea1,linkArea2,linkArea3,linkArea4,'h',"Resistance " + idResistance, Type.RESISTANCE);

			// TODO Pour Sterenn : faire en sorte d'ajouter correctement un nouveau composant avec les vertex adéquats
			// breadboard.addComponent(new Resistance(componentResistance.name,new Vertex(vertexIndex),new Vertex(vertexIndex+1)));
			// vertexIndex+=2;



			//Permet de voir le nom et la valeur du composant quand la souris entre dans l'image
			resistance.setOnMouseEntered(event3 -> {
				//On met les bonnes valeur dans les variables qu on va affiche, on affiche qu il faut determiner les parametre si ils n ont pas encore ete
				//donnee en argument
				if(componentResistance.courant == null){
					componentCourantToShow = "à determiner";
				}
				else{
					componentCourantToShow = "" + componentResistance.courant; // C'est un cast de double dans un String
				}

				if(componentResistance.voltage == null){
					componentVoltageToShow = "à determiner";
				}
				else{
					componentVoltageToShow = "" + componentResistance.voltage; // C'est un cast de double dans un String
				}
				if(componentResistance.value == null){
					componentValueToShow = "à determiner";
				}
				else{
					componentValueToShow= "" + componentResistance.value; // C'est un cast de double dans un String
				}
				//On cree deux messages en fonctions de l'orientation de l'image
				Text informationsh = new Text("Nom : " + componentResistance.name + "\nValeur : " + componentValueToShow
						+ "\nValeur du courant : " + componentCourantToShow + "\nValeur de tension : " + componentVoltageToShow);
				Text informationsv = new Text("Nom : " + componentResistance.name + "\nValeur : " + componentValueToShow
						+ "\nValeur du courant : " + componentCourantToShow + "\nValeur de tension : " + componentVoltageToShow);



				// On definie la zone des deux informations.
				informationsh.setX(resistance.getX());
				informationsh.setY(resistance.getY() + 60);

				informationsv.setX(resistance.getX() + 80);
				informationsv.setY(resistance.getY());

				//On affiche le bon en fonction de l orientation
				if(componentResistance.orientation == 'v') {
					anchorPane2.getChildren().add(informationsv); //On affiche les informations
				}
				else{
					anchorPane2.getChildren().add(informationsh); //On affiche les informations

				}
				resistance.setOnMouseExited(event4 ->{ //On supprime les informations quand la souris sort de la zone
					anchorPane2.getChildren().removeAll(informationsh,informationsv);

				});
			});

			//Permet de creer un lien
			linkArea1.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					addLink(premiereImageDuLien,componentResistance,linkArea,1,-1,anchorPane2);
					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					premiereImageDuLien = componentResistance; // On enregistre l'image
					linkArea = 1;
				}
			});
			linkArea2.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					addLink(premiereImageDuLien,componentResistance,linkArea,2,-1,anchorPane2);
					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					premiereImageDuLien = componentResistance; // On enregistre l'image
					linkArea = 2;
				}
			});
			linkArea3.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					addLink(premiereImageDuLien,componentResistance,linkArea,3,-1,anchorPane2);
					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					premiereImageDuLien = componentResistance; // On enregistre l'image
					linkArea = 3;
				}
			});
			linkArea4.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					addLink(premiereImageDuLien,componentResistance,linkArea,4,-1,anchorPane2);
					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					premiereImageDuLien = componentResistance; // On enregistre l'image
					linkArea = 4;
				}
			});


			resistance.setOnMouseClicked(event1 -> {
				if(etat == "v"){
					valueToShow = componentResistance;
					etat = "d";
					valeurADeterminer.setText("Choix pris en compte");
				}

				if(etat == "tai") {
					Text info = new Text("Par convention, fleche de la tension est oriente \n de bas en haut ou de gauche a droite");
					info.setX(resistance.getX() + 100);
					info.setY(resistance.getY() + 60);
					anchorPane2.getChildren().add(info);

					TextField zonePourChangerValeur = new TextField("Entrer votre valeur");
					zonePourChangerValeur.setLayoutX(resistance.getX() + 110);
					zonePourChangerValeur.setLayoutY(resistance.getY());
					anchorPane2.getChildren().add(zonePourChangerValeur);
					zonePourChangerValeur.setOnAction(event8 -> {
						String a = zonePourChangerValeur.getText();
						double x;
						//On essaie de mettre la valeur rentrer dans un double
						//Ce qui permet d'avoir que des double de partout mais aussi de verifier qu on a bien un nombre et pas un mot
						//On suppose que l utilisateur n est pas idiot et ne va pas rentrer une valeur de resistance negative par exemple
						try {
							x = Double.parseDouble(a);//Si on arrive a cast alors on fait les actions qui suivent
							//TODO Actualiser la valeur dans la breadboard
							anchorPane2.getChildren().remove(zonePourChangerValeur);
							componentResistance.voltage = x;
							etat = "d";
							TensionAImposer.setText("Tension à imposer");
							anchorPane2.getChildren().remove(info);
						} catch (NumberFormatException erreur) {//Sinon on demande a l utilisateur de remettre une autre valeur
							zonePourChangerValeur.setText("Entrer une valeur correct");
						}

					});

				}
				if(etat == "cai") {
					Text info = new Text("Par convention, fleche du courant est oriente \n de bas en haut ou de gauche a droite");
					info.setX(resistance.getX() + 100);
					info.setY(resistance.getY() + 60);
					anchorPane2.getChildren().add(info);

					TextField zonePourChangerValeur = new TextField("Entrer votre valeur");
					zonePourChangerValeur.setLayoutX(resistance.getX() + 110);
					zonePourChangerValeur.setLayoutY(resistance.getY());
					anchorPane2.getChildren().add(zonePourChangerValeur);
					zonePourChangerValeur.setOnAction(event8 -> {
						String a = zonePourChangerValeur.getText();
						double x;
						//On essaie de mettre la valeur rentrer dans un double
						//Ce qui permet d'avoir que des double de partout mais aussi de verifier qu on a bien un nombre et pas un mot
						//On suppose que l utilisateur n est pas idiot et ne va pas rentrer une valeur de resistance negative par exemple
						try {
							x = Double.parseDouble(a);//Si on arrive a cast alors on fait les actions qui suivent
							//TODO Actualiser la valeur dans la breadboard
							anchorPane2.getChildren().remove(zonePourChangerValeur);
							componentResistance.courant = x;
							etat = "d";
							CourantAImposer.setText("Courant à imposer");
							anchorPane2.getChildren().remove(info);
						} catch (NumberFormatException erreur) {//Sinon on demande a l utilisateur de remettre une autre valeur
							zonePourChangerValeur.setText("Entrer une valeur correct");
						}

					});

				}
			});
			//Menu qui s'affiche quand on effectue un clic droit sur l'objet

			ContextMenu menu = new ContextMenu();
			MenuItem rotation = new MenuItem("Effectuer une rotation de +90°");
			MenuItem changeValue = new MenuItem("Changer de valeur");
			MenuItem delete = new MenuItem("Supprimer le composant");
			MenuItem changeName = new MenuItem("Changer le nom");

			delete.setOnAction(event1 -> {
				deleteComponent(componentResistance,anchorPane2);
			});

			changeName.setOnAction(event1 -> {
				TextField zonePourChangerName = new TextField("Entrer un nouveau nom");
				// positionFenetre contient les valeurs du coin en haut a gauche visible de l'anchorPane2
				positionFenetre[0] = positionRelative(anchorPane2,scrollPane);
				zonePourChangerName.setLayoutX(resistance.getX());
				zonePourChangerName.setLayoutY(resistance.getY() - 25);
				anchorPane2.getChildren().add(zonePourChangerName);
				zonePourChangerName.setOnAction(event8 ->{
					String a = zonePourChangerName.getText();
					if (a.equals("Tanguy")) {
						System.out.println("Excellent choix !");
					}
					componentResistance.name = a;
					//TODO Actualiser le nom dans la breadboard
					anchorPane2.getChildren().remove(zonePourChangerName);

				});
			});

			changeValue.setOnAction(event1 ->{
				TextField zonePourChangerValeur = new TextField("Entrer votre valeur");
				// positionFenetre contient les valeurs du coin en haut a gauche visible de l'anchorPane2
				positionFenetre[0] = positionRelative(anchorPane2,scrollPane);
				zonePourChangerValeur.setLayoutX(resistance.getX());
				zonePourChangerValeur.setLayoutY(resistance.getY() - 25);
				anchorPane2.getChildren().add(zonePourChangerValeur);

				//On affiche les informations concernant l'orientation de la tension
				Text info = new Text("Par convention, fleche de la tension est oriente \n de bas en haut ou de gauche a droite");
				info.setX(resistance.getX() + 100);
				info.setY(resistance.getY() + 60);
				anchorPane2.getChildren().add(info);

				zonePourChangerValeur.setOnAction(event8 ->{
					String a = zonePourChangerValeur.getText();
					double x;
					try {x = Double.parseDouble(a);
						componentResistance.value = x;
						//TODO Actualiser la valeur dans la breadboard
						anchorPane2.getChildren().remove(zonePourChangerValeur);
						anchorPane2.getChildren().remove(info);
					}
					catch(NumberFormatException erreur){
						zonePourChangerValeur.setText("Entrer une valeur correct");
					}
				});
			});

			rotation.setOnAction(event1 -> {
				rotationPossible = true;
				for(int i = 0; i < arrayListOfLink.size(); i++){
					if(arrayListOfLink.get(i).image1.object == resistance || arrayListOfLink.get(i).image2.object == resistance){
						rotationPossible = false;
					}
				}

				if(rotationPossible) {
					if (resistance.getRotate() == 0) {
						rotation.setText("Effectuer une rotation de +90°");

						resistance.setRotate(90);

						linkArea2.setX(resistance.getX() + 50);
						linkArea2.setY(resistance.getY() - 25);
						linkArea4.setX(resistance.getX() + 50);
						linkArea4.setY(resistance.getY() + 75);

						anchorPane2.getChildren().removeAll(linkArea1, linkArea3);
						anchorPane2.getChildren().addAll(linkArea2, linkArea4);

						componentResistance.orientation = 'v';
					} else {
						anchorPane2.getChildren().removeAll(linkArea2, linkArea4);
						anchorPane2.getChildren().addAll(linkArea3, linkArea1);
						resistance.setRotate(0);
						rotation.setText("Effectuer une rotation de -90°");

						componentResistance.orientation = 'h';
					}
				}
				else {System.out.println("Rotation impossible");}
			});


			menu.getItems().addAll(changeName,changeValue,rotation,delete);


			resistance.setOnContextMenuRequested(event11 ->{
				menu.show(resistance, Side.BOTTOM,0,0);
			});



			// Permet de deplacer la resistance d'un point a un autre
			resistance.setOnMouseDragged(event1 ->
			{
				if (event1.isPrimaryButtonDown()) {
					if (etat == "d") { //En position Drag and Drop
						// position de l'image
						double imagx;
						double imagy;
						if(resistance.getRotate() == 0) {//Dans le bon sens
							imagx = image.getWidth();
							imagy = image.getHeight();
						}
						else{//rotation 90° donc on inverse la hauteur et la largeur
							imagy = image.getWidth();
							imagx = image.getHeight();
						}

						// positionFenetre contient les valeurs du coin en haut a gauche visible de l'anchorPane2
						positionFenetre[0] = positionRelative(anchorPane2,scrollPane);

						// on récupère la taille de la fenêtre
						double x = event1.getSceneX() + positionFenetre[0][0];
						double y = event1.getSceneY() + positionFenetre[0][1];

						//On effectue cet ajustement car les coordonées de la souris sont calculées a partir du cadre global
						//alors que celles du composant à partir de anchorPane2
						y = y - 60;

						// Permet de ne pas sortir du cadre
						if (x < (imagx / 2)) { // on evite de sortir du cadre a gauche
							x = (imagx / 2);
						}
						if (y < (imagy / 2)) { // on evite de sortir du cadre en haut
							y = (imagy / 2);
						}

						double mx = anchorPane2.getWidth();

						double my = anchorPane2.getHeight();

						if (x + imagx / 2 > mx ) {// on evite de sortir du cadre a droite
							x = mx - imagx / 2 ;
						}

						/* Ici on prend la taille de l'écran, on lui enlève la scrollbare et on regarde si le bord de l'image
						 * c est a dire le centre x plus la largeur de l image divise par 2 */
						if (y + imagy / 2 > my ) {// on evite de sortir du cadre en bas
							y = my - imagy / 2 ;
						}

						//Ici on repositionne l'image est les 4 potentiel carré noir autour
						resistance.setX(x - imagx / 2);
						resistance.setY(y - imagy / 2);

						linkArea3.setX(resistance.getX() + image.getWidth());
						linkArea3.setY(resistance.getY() + image.getHeight() / 2);
						linkArea1.setX(resistance.getX());
						linkArea1.setY(resistance.getY() + image.getHeight() / 2);
						linkArea2.setX(resistance.getX() + imagx);
						linkArea2.setY(resistance.getY() - 25);
						linkArea4.setX(resistance.getX() + imagx);
						linkArea4.setY(resistance.getY() + 75);

						actualiseViewOfLink(componentResistance, anchorPane2); //On actualise les liens
					}
				}
			});

		});
	}

	/**
	 * Retourne la valeur de la position du coin en haut a droite de l'anchor pane 3
	 * @param anchorPane3
	 * @param scrollPane
     * @return
     */
	public static double[] positionRelative(AnchorPane anchorPane3,ScrollPane scrollPane){
		double relativeX = (anchorPane3.getWidth() - scrollPane.getWidth()) * scrollPane.getHvalue();
		double relativeY = (anchorPane3.getHeight() - scrollPane.getHeight()) * scrollPane.getVvalue();
		double[] a = new double[2];
		a[0] = relativeX;
		a[1] = relativeY;
		return(a);
	}

	public static void showResult(AnchorPane anchorPane3, Text programmeLaunch, AnchorPane anchorPane4, GraphicalComponent[] result, Button Run){
		//TODO il me faudrait un tableau compose d'element du type GraphicalComponent pour que je puisse tout affiche

		//Supprime le message
		anchorPane3.getChildren().remove(programmeLaunch);
		if(valueToShow !=null){//On affiche les valeurs qui interessent l utilisateur
			Text informations = new Text("Nom : " + valueToShow.name + "\nValeur : " + valueToShow.value
					+ "\nValeur du courant = " + valueToShow.courant + "\nValeur de tension = " + valueToShow.voltage);
			informations.setLayoutX(20);
			informations.setLayoutY(500);
			anchorPane4.getChildren().add(informations);
			informationsList.add(informations);
		}

		for(int i = 0; i < result.length ; i++){//On affiche aussi toutes les valeurs
			//On cree deux messages en fonctions de l'orientation de l'image
			Text informationsh = new Text("Nom : " + result[i].name + "\nValeur : " + result[i].value
					+ "\nValeur du courant = " + result[i].courant + "\nValeur de tension = " + result[i].voltage);
			Text informationsv = new Text("Nom : " + result[i].name + "\nValeur : " + result[i].value
					+ "\nValeur du courant = " + result[i].courant + "\nValeur de tension = " + result[i].voltage);


			// On definie la zone des deux informations.
			informationsh.setX(result[i].object.getX());
			informationsh.setY(result[i].object.getY() + 60);

			informationsv.setX(result[i].object.getX() + 80);
			informationsv.setY(result[i].object.getY());

			//On affiche le bon en fonction de l orientation
			if(result[i].orientation == 'v') {
				anchorPane3.getChildren().add(informationsv); //On affiche les informations
				informationsList.add(informationsv);
			}
			else{
				anchorPane3.getChildren().add(informationsh); //On affiche les informations
				informationsList.add(informationsh); //On ajoute les info a la liste pour pouvoir les supprimer plus tard
			}
		}
		GraphicalFunctions.launch = false;//On aurtorise a l utilisateur de relancer le programme

	}
}