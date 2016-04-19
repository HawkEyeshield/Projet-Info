package graphics;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

/** Interface donnant les différentes fonctions utilisées dans l'interface
 * @author Tanguy 
 */
public class GraphicalFunctions
{
	/*Permet de définir dans quel etat on est a tout moment, d pour Drag and Drop,
    l1 pour Lien et indique que aucun objet n'a été selectionner, l2 pour Lien et que un objet a déjà
    ete selectionner et qu'on attend le second
    */
	public static int maxLien = 1000;
	public static String etat = "d"; // Permet de définir si on peut faire du drag and drop ou si on est entrain de créer des liens
	public static Component premiereImageDuLien; // Premiere image du lien que l'on va creer
	public static int idVotalgeGenerator = 0; //Compte le nombre de generateur de tension
	public static int idCourantgeGenerator = 0;// Compte le nombre de generateur de courant
	public static int idNode = 0; // Compte  le nombre de noeud
	public static Link [] boardOfLink = new Link[maxLien]; //tableau qui liste les liens
	public static int nombreDeLien = 0; //Compte le nombre de lien
	public static int linkArea;


	/**
     * Permet d'ajouter ou d'actualiser un lien aussi bien visuellement que dans la breadboard
     * @param premiereImageDuLien Premier composant a relier
     * @param secondeImageDuLien Second composant a relier
     * @param linkAreaUsed1 Premiere zone a relier(1 pour a gauche, 2 pour en haut, 3 a droite et 4 en bas)
     * @param linkAreaUsed2 Seconde zone a relier
     * @param k Permet de savoir si il faut l'ajouter ou juste actualiser
     * @param anchorPane2 Zone de travail
     */
    public static void addLink(Component premiereImageDuLien,Component secondeImageDuLien, int linkAreaUsed1, int linkAreaUsed2, int k, AnchorPane anchorPane2) {

        double centreXLinkArea1 = 0;
        double centreYLinkArea1 = 0;
        double centreXLinkArea2 = 0;
        double centreYLinkArea2 = 0;
        if (linkAreaUsed1 == 1) {
            centreXLinkArea1 = premiereImageDuLien.square1.getX() + 5;
            centreYLinkArea1 = premiereImageDuLien.square1.getY() + 5;
        } else if (linkAreaUsed1 == 2) {
            centreXLinkArea1 = premiereImageDuLien.square2.getX() + 5;
            centreYLinkArea1 = premiereImageDuLien.square2.getY() + 5;
        } else if (linkAreaUsed1 == 3) {
            System.out.println(premiereImageDuLien.square3.getX());
            System.out.println(premiereImageDuLien.square3.getY());
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

        Line line1 = new Line(0, 0, 0, 0);
        Line line2 = new Line(0, 0, 0, 0);
        Line line3 = new Line(0, 0, 0, 0);


        //TODO il y aurait BEAUCOUP mieux a faire en résonnant sur les LinkArea1 ou 2


        if((linkAreaUsed1 == 3 && linkAreaUsed2 == 1) || (linkAreaUsed1 == 1 && linkAreaUsed2 == 3)){
            line1 = new Line(centreXLinkArea1, centreYLinkArea1, (centreXLinkArea1 + centreXLinkArea2) / 2, centreYLinkArea1);

            line2 = new Line(centreXLinkArea2, centreYLinkArea2, (centreXLinkArea1 + centreXLinkArea2) / 2, centreYLinkArea2);

            line3 = new Line((centreXLinkArea1 + centreXLinkArea2) / 2, centreYLinkArea1, (centreXLinkArea1 + centreXLinkArea2) / 2, centreYLinkArea2);
        }

         else if (premiereImageDuLien.orientation == 'h' && secondeImageDuLien.orientation == 'h') {

            line1 = new Line(centreXLinkArea1, centreYLinkArea1, (centreXLinkArea1 + centreXLinkArea2) / 2, centreYLinkArea1);

            line2 = new Line(centreXLinkArea2, centreYLinkArea2, (centreXLinkArea1 + centreXLinkArea2) / 2, centreYLinkArea2);

            line3 = new Line((centreXLinkArea1 + centreXLinkArea2) / 2, centreYLinkArea1, (centreXLinkArea1 + centreXLinkArea2) / 2, centreYLinkArea2);
        }

        else if (premiereImageDuLien.orientation == 'v' && secondeImageDuLien.orientation == 'v') {
            line1 = new Line(centreXLinkArea1, centreYLinkArea1, centreXLinkArea1, (centreYLinkArea1 + centreYLinkArea2) / 2);

            line2 = new Line(centreXLinkArea2, centreYLinkArea2, centreXLinkArea2, (centreYLinkArea1 + centreYLinkArea2) / 2);

            line3 = new Line(centreXLinkArea1, (centreYLinkArea1 + centreYLinkArea2) / 2, centreXLinkArea2, (centreYLinkArea1 + centreYLinkArea2) / 2);
        }

        else if ((premiereImageDuLien.orientation == 'h' && secondeImageDuLien.orientation == 'v') || (premiereImageDuLien.orientation == 't' && secondeImageDuLien.orientation == 'v') || (premiereImageDuLien.orientation == 'h' && secondeImageDuLien.orientation == 't')) {
            line1 = new Line(centreXLinkArea1, centreYLinkArea1, centreXLinkArea2, centreYLinkArea1);
            line2 = new Line(centreXLinkArea2, centreYLinkArea2, centreXLinkArea2, centreYLinkArea1);
        }
        else if ((premiereImageDuLien.orientation == 'v' && secondeImageDuLien.orientation == 'h') || (premiereImageDuLien.orientation == 't' && secondeImageDuLien.orientation == 'h') || (premiereImageDuLien.orientation == 'v' && secondeImageDuLien.orientation == 't')) {
            line1 = new Line(centreXLinkArea2, centreYLinkArea2, centreXLinkArea1, centreYLinkArea2);

            line2 = new Line(centreXLinkArea1, centreYLinkArea1, centreXLinkArea1, centreYLinkArea2);
        }
        else if (premiereImageDuLien.orientation == 't' && secondeImageDuLien.orientation == 't') {
            line1 = new Line(centreXLinkArea1, centreYLinkArea1, centreXLinkArea2, centreYLinkArea2);
        }

        if (k == -1) { //Si on ajoute un nouveau lien k = -1
            boolean a = true; //Permet de véfifier si il n'existe pas déjà un lien qui part de cet endroit la.
            for (int i = 0; i < nombreDeLien; i++) {
                if ((premiereImageDuLien == boardOfLink[i].image1 && linkAreaUsed1 == boardOfLink[i].linkAreaUsed1) || (premiereImageDuLien == boardOfLink[i].image2 && linkAreaUsed1 == boardOfLink[i].linkAreaUsed2) || (secondeImageDuLien == boardOfLink[i].image1 && linkAreaUsed2 == GraphicalFunctions.boardOfLink[i].linkAreaUsed1) || (secondeImageDuLien == boardOfLink[i].image2 && linkAreaUsed2 == boardOfLink[i].linkAreaUsed2)) {
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
            if (a) {
                anchorPane2.getChildren().add(line1);
                anchorPane2.getChildren().add(line2);
                anchorPane2.getChildren().add(line3);
                boardOfLink[nombreDeLien] = new Link(premiereImageDuLien, secondeImageDuLien, linkAreaUsed1, linkAreaUsed2, line1, line2, line3);
                System.out.println("on devrait rajouter un truc a la case " + nombreDeLien);
                nombreDeLien += 1;
                //TODO ici mettre la fonction qui ajoute le lien dans la breadboard
            }
        }
        else{ //Sinon c'est que c'est qu'il faut juste actualiser les liens
            anchorPane2.getChildren().add(line1);
            anchorPane2.getChildren().add(line2);
            anchorPane2.getChildren().add(line3);
            boardOfLink[k].lien1 = line1;
            boardOfLink[k].lien2 = line2;
            boardOfLink[k].lien3 = line3;
            //TODO mettre la fonction qui actualise le lien dans la breadboard
        }

    }

    /**
     * Actualise les liens entre les objets.
     * L'idée est de regarder tous les liens existant avec la boucle et si il y a un lien qui est relier d'un cote a l'image alors on
     * supprime les liens et on appel AddLink pour les remettre au bon endroit
     * @param image objet que l'on deplace
     * @param anchorPane2 zone ou l'on travail
     */
    public static void actualiseViewOfLink(Component image, AnchorPane anchorPane2) {
        for (int i = 0; i < nombreDeLien;i++){
            if(boardOfLink[i].image1 == image || boardOfLink[i].image2 == image){
                anchorPane2.getChildren().remove(boardOfLink[i].lien1);
                anchorPane2.getChildren().remove(boardOfLink[i].lien2);
                anchorPane2.getChildren().remove(boardOfLink[i].lien3);
                addLink(boardOfLink[i].image1,boardOfLink[i].image2,boardOfLink[i].linkAreaUsed1,boardOfLink[i].linkAreaUsed2,i, anchorPane2);
            }
        }
    }

	/**
	 * Permet de faire apparaitre un generateur de tension
	 * @param anchorPane2 Zone a l interieur du scrollPane
	 * @param anchorPane4 Zone de depart des images
	 * @param scrollPane Zone pour mettre le circuit
	 */
	public static  void addVoltageGenerator (AnchorPane anchorPane2, AnchorPane anchorPane4, ScrollPane scrollPane)
	{
		ImageView firstVoltageGenerator = new ImageView(); // On créer un object de type ImageView
		Image image1 = new Image("file:image/Generateur de tension h.png"); // On va la cherche au bonne endroit
		firstVoltageGenerator.setImage(image1);
		firstVoltageGenerator.setX(90);
		firstVoltageGenerator.setY(80); // On définit la position
		anchorPane4.getChildren().add(firstVoltageGenerator); // On affiche dans la zone voulu (anchorPane4)

		//firstVoltageGenerator.setOnMouseEntered();

        // Permet de faire apparaitre le generateur de tension dans la zone centrale en cliquant dans la zone des composants
		firstVoltageGenerator.setOnMouseClicked(event ->
		{
            //Fait apparaitre différente image
			System.out.println("Un generateur de tension devrait apparaitre");
			ImageView tensionGenerator = new ImageView();
			Image image = new Image("file:image/Generateur de tension h.png");
			tensionGenerator.setImage(image);
			tensionGenerator.setX(100);//valeur par default d'apparition
			tensionGenerator.setY(100);//valeur par default d'apparition
			anchorPane2.getChildren().add(tensionGenerator);

			ImageView linkArea1 = new ImageView();
			Image square1 = new Image("file:image/LinkArea.png");
			linkArea1.setImage(square1);
			linkArea1.setFitHeight(10);//On redimensionne
			linkArea1.setFitWidth(10);
			linkArea1.setX(100); //Placer en fonction des valeurs par default
			linkArea1.setY(100 + image.getHeight()/2);
			anchorPane2.getChildren().add(linkArea1);

			ImageView linkArea3 = new ImageView();
			Image square3 = new Image("file:image/LinkArea.png");
			linkArea3.setImage(square3);
			linkArea3.setFitHeight(10);
			linkArea3.setFitWidth(10);
			linkArea3.setX(100 + image.getWidth());
			linkArea3.setY(100 + image.getHeight()/2);
			anchorPane2.getChildren().add(linkArea3);

			tensionGenerator.setLayoutX(idVotalgeGenerator); // Donne un identifiant unique au generateur de tension
			idVotalgeGenerator += 1;
            //On creer l'objet mais on ne l'ajoute pas encore a la breadboard
			Component componentVoltageGenerator = new Component(tensionGenerator,linkArea1,null,linkArea3,null,'h',"Generateur de tension " + idVotalgeGenerator, 10);

			//Permet de voir le nom et la valeur
            tensionGenerator.setOnMouseEntered(event3 -> {
                Text informations = new Text("Nom : " + componentVoltageGenerator.name);
                informations.setLayoutX(tensionGenerator.getX());
                informations.setLayoutY(tensionGenerator.getY() + 65);
                anchorPane2.getChildren().add(informations);
                tensionGenerator.setOnMouseExited(event4 ->{ //On le supprime quand la souris sort de la zone
                    anchorPane2.getChildren().remove(informations);
                });
            });

			//Permet de creer un lien
			linkArea1.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					System.out.println("On a cliqué sur le second objet");
					addLink(premiereImageDuLien,componentVoltageGenerator,linkArea,1,-1,anchorPane2);
					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					System.out.println("On a cliqué sur le premier objet");
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					premiereImageDuLien = componentVoltageGenerator; // On enregistre l'image
					linkArea = 1;
				}
			});
			linkArea3.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					System.out.println("On a cliqué sur le second objet");
					addLink(premiereImageDuLien,componentVoltageGenerator,linkArea,3,-1,anchorPane2);
					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					System.out.println("On a cliqué sur le premier objet");
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					premiereImageDuLien = componentVoltageGenerator; // On enregistre l'image
					linkArea = 3;
				}
			});

            //Permet de faire apparaitre un menu avec le clique droit
            tensionGenerator.setOnMouseClicked(event4 -> {
                System.out.println("coucou");
            });

           // Permet de deplacer le generateur de tension d'un point a un autre
			tensionGenerator.setOnMouseDragged(event1 -> 
			{
				if (etat == "d") { //En position Drag and Drop
					// position de l'image
					double imagx = image.getWidth();
					double imagy = image.getHeight();

					//System.out.println(imagx);
					// on récupère la taille de la fenêtre
					double x = event1.getSceneX();
					double y = event1.getSceneY();

					// Permet de ne pas sortir du cadre
					if (x < (imagx / 2)) {
						x = (imagx / 2);
					} // on evite de sortir du cadre a gauche
					if (y < (65 + imagy / 2)) {
						y = (65 + imagy / 2);
					} // on evite de sortir du cadre en haut

					double mx = anchorPane2.getWidth();

					double my = anchorPane2.getHeight();
					if (x + imagx / 2 > mx - 20) {
						x = mx - imagx / 2 - 20;
					} // on evite de sortir du cadre a droite

					/* Ici on prend la taille de l'écran, on lui enlève la scrollbare et on regarde si le bord de l'image
					 * c est a dire le centre x plus la largeur de l image divise par 2 */
					if (y + imagy / 2 > my - 20) {
						y = my - imagy / 2 - 20;
					} // on evite de sortir du cadre en bas

                    //Ici on repositionne l'image est les 4 potentiel carré noir autour
					tensionGenerator.relocate(x - imagx / 2, y - imagy / 2);
					tensionGenerator.setX(x - imagx / 2);
					tensionGenerator.setY(y - imagy / 2);
					linkArea3.relocate(tensionGenerator.getX() + image.getWidth(),tensionGenerator.getY()+ image.getHeight()/2);
					linkArea3.setX(tensionGenerator.getX() + image.getWidth());
					linkArea3.setY(tensionGenerator.getY()+ image.getHeight()/2);
					linkArea1.relocate(tensionGenerator.getX(),tensionGenerator.getY()+ image.getHeight()/2);
					linkArea1.setX(tensionGenerator.getX());
					linkArea1.setY(tensionGenerator.getY()+ image.getHeight()/2);

					actualiseViewOfLink(componentVoltageGenerator,anchorPane2); //On actualise les liens
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
	public static void addCurrentGenerator (AnchorPane anchorPane2, AnchorPane anchorPane4, ScrollPane scrollPane)
	{
		ImageView firstCourantGenerator = new ImageView();
		Image image2 = new Image("file:image/Generateur de courant v.png");
		firstCourantGenerator.setImage(image2);
		firstCourantGenerator.setX(10);
		firstCourantGenerator.setY(80);
		anchorPane4.getChildren().add(firstCourantGenerator);

		
        //Permet de faire apparaitre le generateur de courant
		firstCourantGenerator.setOnMouseClicked(event -> {
			System.out.println("Un generateur de courant devrait apparaitre");
			ImageView courantGenerator = new ImageView();
			Image image = new Image("file:image/Generateur de courant v.png");
			courantGenerator.setImage(image);
			courantGenerator.setX(200);
			courantGenerator.setY(100);
			anchorPane2.getChildren().add(courantGenerator);

			ImageView linkArea2 = new ImageView();
			Image square2 = new Image("file:image/LinkArea.png");
			linkArea2.setImage(square2);
			linkArea2.setFitHeight(10);
			linkArea2.setFitWidth(10);
			linkArea2.setX(200 + image.getWidth()/2);
			linkArea2.setY(100);
			anchorPane2.getChildren().add(linkArea2);

			ImageView linkArea4 = new ImageView();
			Image square4 = new Image("file:image/LinkArea.png");
			linkArea4.setImage(square4);
			linkArea4.setFitHeight(10);
			linkArea4.setFitWidth(10);
			linkArea4.setX(200 + image.getWidth()/2);
			linkArea4.setY(100 + image.getHeight());
			anchorPane2.getChildren().add(linkArea4);

			courantGenerator.setLayoutX(idCourantgeGenerator);
			idCourantgeGenerator += 1;
			Component componentCourantGenerator = new Component(courantGenerator,null,linkArea2,null,linkArea4,'v',"Generateur de courant " + idCourantgeGenerator,10);


            courantGenerator.setOnMouseEntered(event3 -> {
                Text informations = new Text("Nom : " + componentCourantGenerator.name);
                informations.setLayoutX(courantGenerator.getX());
                informations.setLayoutY(courantGenerator.getY() + 120);
                anchorPane2.getChildren().add(informations);
                courantGenerator.setOnMouseExited(event4 ->{
                    anchorPane2.getChildren().remove(informations);
                });
            });

			//Permet de creer un lien
			linkArea2.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					System.out.println("On a cliqué sur le second objet");
					addLink(premiereImageDuLien,componentCourantGenerator,linkArea,2,-1,anchorPane2);

					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					System.out.println("On a cliqué sur le premier objet");
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					premiereImageDuLien = componentCourantGenerator; // On enregistre l'image
					linkArea = 2;
				}
			});
			linkArea4.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					System.out.println("On a cliqué sur le second objet");
					addLink(premiereImageDuLien,componentCourantGenerator,linkArea,4,-1,anchorPane2);

					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					System.out.println("On a cliqué sur le premier objet");
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					premiereImageDuLien = componentCourantGenerator; // On enregistre l'image
					linkArea = 4;
				}
			});

			/*
            Permet de deplacer le generateur de courant d'un point a un autre
			 */

			courantGenerator.setOnMouseDragged(event1 -> 
			{
				if (etat == "d") { //En position Drag and Drop
					// taille de l'image
					double imagx = image.getWidth();
					double imagy = image.getHeight();


					//on récupère la taille de la fenêtre
					double x = event1.getSceneX();
					double y = event1.getSceneY();

					//Permet de ne pas sortir du cadre
					if (x < (imagx / 2)) {
						x = (imagx / 2);
					} // on evite de sortir du cadre a gauche
					if (y < (65 + imagy / 2)) {
						y = (65 + imagy / 2);
					} // on evite de sortir du cadre en haut

					double mx = anchorPane2.getWidth();

					double my = anchorPane2.getHeight();
					if (x + imagx / 2 > mx - 20) {
						x = mx - imagx / 2 - 20;
					} // on evite de sortir du cadre a droite
				
					/* Ici on prend la taille de l'écran, on lui enlève la scrollbare et on regarde si le bord de l'image
				 	* c est a dire le centre x plus la largeur de l image divise par 2*/
					if (y + imagy / 2 > my - 20) {
						y = my - imagy / 2 - 20;
					} // on evite de sortir du cadre en bas

					courantGenerator.relocate(x - imagx / 2, y - imagy / 2);
					courantGenerator.setX(x - imagx / 2);
					courantGenerator.setY(y - imagy / 2);
					linkArea2.relocate(courantGenerator.getX() + image.getWidth()/2,courantGenerator.getY());
					linkArea2.setX(courantGenerator.getX() + image.getWidth()/2);
					linkArea2.setY(courantGenerator.getY());
					linkArea4.relocate(courantGenerator.getX() + image.getWidth()/2,courantGenerator.getY() + image.getHeight());
					linkArea4.setX(courantGenerator.getX() + image.getWidth()/2);
					linkArea4.setY(courantGenerator.getY() + image.getHeight());

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
	public static void addNode (AnchorPane anchorPane2, AnchorPane anchorPane4, ScrollPane scrollPane, Button CreeUnLien)
	{
		ImageView firstNode = new ImageView();
		Image image2 = new Image("file:image/Noeud.png");
		firstNode.setImage(image2);
		firstNode.setX(10);
		firstNode.setY(170);
		anchorPane4.getChildren().add(firstNode);


		
        //Permet de faire apparaitre un noeud

		firstNode.setOnMouseClicked(event -> 
		{
			System.out.println("Un generateur de courant devrait apparaitre");
			ImageView node = new ImageView();
			Image image = new Image("file:image/Noeud.png");
			node.setImage(image);
			node.setX(300);
			node.setY(100);
			anchorPane2.getChildren().add(node);

			ImageView linkArea3 = new ImageView();
			Image square3 = new Image("file:image/LinkArea.png");
			linkArea3.setImage(square3);
			linkArea3.setFitHeight(10);
			linkArea3.setFitWidth(10);
			linkArea3.setX(300 + image.getWidth());
			linkArea3.setY(100  +image.getHeight()/2);
			anchorPane2.getChildren().add(linkArea3);

			ImageView linkArea1 = new ImageView();
			Image square1 = new Image("file:image/LinkArea.png");
			linkArea1.setImage(square1);
			linkArea1.setFitHeight(10);
			linkArea1.setFitWidth(10);
			linkArea1.setX(300);
			linkArea1.setY(100 + image.getHeight()/2);
			anchorPane2.getChildren().add(linkArea1);

			ImageView linkArea2 = new ImageView();
			Image square2 = new Image("file:image/LinkArea.png");
			linkArea2.setImage(square2);
			linkArea2.setFitHeight(10);
			linkArea2.setFitWidth(10);
			linkArea2.setX(300 + image.getWidth()/2);
			linkArea2.setY(100);
			anchorPane2.getChildren().add(linkArea2);

			ImageView linkArea4 = new ImageView();
			Image square4 = new Image("file:image/LinkArea.png");
			linkArea4.setImage(square4);
			linkArea4.setFitHeight(10);
			linkArea4.setFitWidth(10);
			linkArea4.setX(300 + image.getWidth()/2);
			linkArea4.setY(100 + image.getHeight());
			anchorPane2.getChildren().add(linkArea4);

			node.setLayoutX(idNode);
			idNode += 1;
			Component componentNode = new Component(node,linkArea1,linkArea2,linkArea3,linkArea4,'t',"Noeud " + idNode,0);

			node.setOnMouseEntered(event3 -> {
                Text informations = new Text("Nom : " + componentNode.name);
                informations.setLayoutX(node.getX() + 40);
                informations.setLayoutY(node.getY() + 50);
                anchorPane2.getChildren().add(informations);
                node.setOnMouseExited(event4 ->{
                    anchorPane2.getChildren().remove(informations);
                });
            });


			//Permet de creer un lien
			linkArea1.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					System.out.println("On a cliqué sur le second objet qui est 1");
					addLink(premiereImageDuLien,componentNode,linkArea,1,-1,anchorPane2);

					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					System.out.println("On a cliqué sur le premier objet qui est 1");
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					linkArea = 1;
					premiereImageDuLien = componentNode; // On enregistre l'image
				}
			});
			linkArea2.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					System.out.println("On a cliqué sur le second objet qui est 2");
					addLink(premiereImageDuLien,componentNode,linkArea,2,-1,anchorPane2);

					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					System.out.println("On a cliqué sur le premier objet qui est 2");
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					linkArea = 2;
					premiereImageDuLien = componentNode; // On enregistre l'image
				}
			});
			linkArea3.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					System.out.println("On a cliqué sur le second objet qui est 3");
					addLink(premiereImageDuLien,componentNode,linkArea,3,-1,anchorPane2);

					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					System.out.println("On a cliqué sur le premier objet qui est 3");
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					linkArea = 3;
					premiereImageDuLien = componentNode; // On enregistre l'image
				}
			});
			linkArea4.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					System.out.println("On a cliqué sur le second objet qui est 4");
					addLink(premiereImageDuLien,componentNode,linkArea,4,-1,anchorPane2);

					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					System.out.println("On a cliqué sur le premier objet qui est 4	");
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					linkArea = 4;
					premiereImageDuLien = componentNode; // On enregistre l'image
				}
			});

			
            //Permet de deplacer le noeud d'un point a un autre
			node.setOnMouseDragged(event1 -> 
			{
				if (etat == "d") { //En position Drag and Drop
					//position de la souris
					double imagx = image.getWidth();
					double imagy = image.getHeight();


					//on récupère la taille de la fenêtre
					double x = event1.getSceneX();
					double y = event1.getSceneY();

					//Permet de ne pas sortir du cadre
					if (x < (imagx / 2)) {
						x = (imagx / 2);
					} // on evite de sortir du cadre a gauche
					if (y < (65 + imagy / 2)) {
						y = (65 + imagy / 2);
					} // on evite de sortir du cadre en haut

					double mx = anchorPane2.getWidth();

					double my = anchorPane2.getHeight();
					if (x + imagx / 2 > mx - 20) {
						x = mx - imagx / 2 - 20;
					} // on evite de sortir du cadre a droite
				
					/* Ici on prend la taille de l'écran, on lui enlève la scrollbare et on regarde si le bord de l'image
				 	* c est a dire le centre x plus la largeur de l image divise par 2*/

					if (y + imagy / 2 > my - 20) {
						y = my - imagy / 2 - 20;
					} // on evite de sortir du cadre en bas

					node.relocate(x - imagx / 2, y - imagy / 2);
					node.setX(x - imagx / 2);
					node.setY(y - imagy / 2);

					linkArea1.relocate(node.getX(),node.getY() + image.getHeight()/2);
					linkArea1.setX(node.getX());
					linkArea1.setY(node.getY() + image.getHeight()/2);
					linkArea3.relocate(node.getX() + image.getWidth(),node.getY() + image.getHeight()/2);
					linkArea3.setX(node.getX() + image.getWidth());
					linkArea3.setY(node.getY() + image.getHeight()/2);
					linkArea2.relocate(node.getX() + image.getWidth()/2,node.getY());
					linkArea2.setX(node.getX() + image.getWidth()/2);
					linkArea2.setY(node.getY());
					linkArea4.relocate(node.getX() + image.getWidth()/2,node.getY() + image.getHeight());
					linkArea4.setX(node.getX() + image.getWidth()/2);
					linkArea4.setY(node.getY() + image.getHeight());

					actualiseViewOfLink(componentNode,anchorPane2);

				}
			});
		});
	}
}