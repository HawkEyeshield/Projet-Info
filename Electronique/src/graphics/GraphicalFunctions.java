package graphics;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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
	public static ImageView premiereImageDuLien = new ImageView(); // Premiere image du lien que l'on va creer
	public static char orientationImage = 'r'; // orientation de la premiere image du lien que l'on va creer
	public static int idVotalgeGenerator = 0; //Compte le nombre de generateur de tension
	public static int idCourantgeGenerator = 0;// Compte le nombre de generateur de courant
	public static int idNode = 0; // Compte  le nombre de noeud
	public static AddLink.link [] boardOfLink = new AddLink.link[maxLien]; //tableau qui liste les liens
	public static int nombreDeLien = 0; //Compte le nombre de lien

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
			System.out.println("Un generateur de tension devrait apparaitre");
			ImageView tensionGenerator = new ImageView();
			Image image = new Image("file:image/Generateur de tension h.png");
			tensionGenerator.setImage(image);
			tensionGenerator.setX(100);
			tensionGenerator.setY(100);
			anchorPane2.getChildren().add(tensionGenerator);
			System.out.println("coucou");

			ImageView linkArea1 = new ImageView();
			Image square1 = new Image("file:image/LinkArea.png");
			linkArea1.setImage(square1);
			linkArea1.setFitHeight(10);
			linkArea1.setFitWidth(10);
			linkArea1.setX(100);
			linkArea1.setY(100 + image.getHeight()/2);
			anchorPane2.getChildren().add(linkArea1);

			ImageView linkArea2 = new ImageView();
			Image square2 = new Image("file:image/LinkArea.png");
			linkArea2.setImage(square2);
			linkArea2.setFitHeight(10);
			linkArea2.setFitWidth(10);
			linkArea2.setX(100 + image.getWidth());
			linkArea2.setY(100 + image.getHeight()/2);
			anchorPane2.getChildren().add(linkArea2);

			tensionGenerator.setLayoutX(idVotalgeGenerator); // Donne un identifiant unique au generateur de tension
			idVotalgeGenerator += 1;


			//Permet de creer un lien
			tensionGenerator.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					System.out.println("On a cliqué sur le second objet");
					AddLink.addLink(premiereImageDuLien,tensionGenerator,orientationImage,'h', anchorPane2);
					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					System.out.println("On a cliqué sur le premier objet");
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					orientationImage = 'h'; // h comme Horizontale pour savoir ou sont les fils du composant
					premiereImageDuLien = tensionGenerator; // On enregistre l'image
				}
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
					//System.out.println(x);

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

					tensionGenerator.relocate(x - imagx / 2, y - imagy / 2);
					tensionGenerator.setX(x - imagx / 2);
					tensionGenerator.setY(y - imagy / 2);
					linkArea1.relocate(tensionGenerator.getX() + image.getWidth(),tensionGenerator.getY()+ image.getHeight()/2);
					linkArea1.setX(tensionGenerator.getX() + image.getWidth());
					linkArea1.setY(tensionGenerator.getY()+ image.getHeight()/2);
					linkArea2.relocate(tensionGenerator.getX(),tensionGenerator.getY()+ image.getHeight()/2);
					linkArea2.setX(tensionGenerator.getX());
					linkArea2.setY(tensionGenerator.getY()+ image.getHeight()/2);
					//System.out.println(tensionGenerator.getX());
					//System.out.println(tensionGenerator.getY());
					//AddLink.addLink(,anchorPane2);
					//AddLink.deleteLink(premiereImageDuLien,a.get,orientationImage1,orientationImage2)
					//System.out.println(tensionGenerator.getRotate());
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

			ImageView linkArea1 = new ImageView();
			Image square1 = new Image("file:image/LinkArea.png");
			linkArea1.setImage(square1);
			linkArea1.setFitHeight(10);
			linkArea1.setFitWidth(10);
			linkArea1.setX(200 + image.getWidth()/2);
			linkArea1.setY(100);
			anchorPane2.getChildren().add(linkArea1);

			ImageView linkArea2 = new ImageView();
			Image square2 = new Image("file:image/LinkArea.png");
			linkArea2.setImage(square2);
			linkArea2.setFitHeight(10);
			linkArea2.setFitWidth(10);
			System.out.println(image.getHeight());
			System.out.println(image.getWidth());
			linkArea2.setX(200 + image.getWidth()/2);
			linkArea2.setY(100 + image.getHeight());
			anchorPane2.getChildren().add(linkArea2);

			courantGenerator.setLayoutX(idCourantgeGenerator);
			idCourantgeGenerator += 1;

			//Permet de creer un lien
			courantGenerator.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					System.out.println("On a cliqué sur le second objet");
					AddLink.addLink(premiereImageDuLien,courantGenerator,orientationImage,'v', anchorPane2);

					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					System.out.println("On a cliqué sur le premier objet");
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					orientationImage = 'v'; // h comme Horizontale pour savoir ou sont les fils du composant
					premiereImageDuLien = courantGenerator; // On enregistre l'image
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
					System.out.println(courantGenerator.getId());
					linkArea1.relocate(courantGenerator.getX() + image.getWidth()/2,courantGenerator.getY());
					linkArea1.setX(courantGenerator.getX() + image.getWidth()/2);
					linkArea1.setY(courantGenerator.getY());
					linkArea2.relocate(courantGenerator.getX() + image.getWidth()/2,courantGenerator.getY() + image.getHeight());
					linkArea2.setX(courantGenerator.getX() + image.getWidth()/2);
					linkArea2.setY(courantGenerator.getY() + image.getHeight());
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

			ImageView linkArea1 = new ImageView();
			Image square1 = new Image("file:image/LinkArea.png");
			linkArea1.setImage(square1);
			linkArea1.setFitHeight(10);
			linkArea1.setFitWidth(10);
			linkArea1.setX(300 + image.getWidth());
			linkArea1.setY(100  +image.getHeight()/2);
			anchorPane2.getChildren().add(linkArea1);

			ImageView linkArea2 = new ImageView();
			Image square2 = new Image("file:image/LinkArea.png");
			linkArea2.setImage(square2);
			linkArea2.setFitHeight(10);
			linkArea2.setFitWidth(10);
			linkArea2.setX(300);
			linkArea2.setY(100 + image.getHeight()/2);
			anchorPane2.getChildren().add(linkArea2);

			ImageView linkArea3 = new ImageView();
			Image square3 = new Image("file:image/LinkArea.png");
			linkArea3.setImage(square3);
			linkArea3.setFitHeight(10);
			linkArea3.setFitWidth(10);
			linkArea3.setX(300 + image.getWidth()/2);
			linkArea3.setY(100);
			anchorPane2.getChildren().add(linkArea3);

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


			//Permet de creer un lien
			node.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){ // lance la lien si c'est le deuxième objet sur lequel on a cliqué
					System.out.println("On a cliqué sur le second objet");
					AddLink.addLink(premiereImageDuLien,node,orientationImage,'t', anchorPane2);

					etat = "l1"; // On repasse dans l'etat 1 car le lien a ete creer
				}
				else if (etat == "l1"){ // ajoute un objet
					System.out.println("On a cliqué sur le premier objet");
					etat = "l2"; // On change l'etat pour indiquer que l'utilisateur a bien cliqué sur une image
					orientationImage = 't'; // h comme Horizontale pour savoir ou sont les fils du composant
					premiereImageDuLien = node; // On enregistre l'image
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

					linkArea2.relocate(node.getX(),node.getY() + image.getHeight()/2);
					linkArea2.setX(node.getX());
					linkArea2.setY(node.getY() + image.getHeight()/2);
					linkArea1.relocate(node.getX() + image.getWidth(),node.getY() + image.getHeight()/2);
					linkArea1.setX(node.getX() + image.getWidth());
					linkArea1.setY(node.getY() + image.getHeight()/2);
					linkArea3.relocate(node.getX() + image.getWidth()/2,node.getY());
					linkArea3.setX(node.getX() + image.getWidth()/2);
					linkArea3.setY(node.getY());
					linkArea4.relocate(node.getX() + image.getWidth()/2,node.getY() + image.getHeight());
					linkArea4.setX(node.getX() + image.getWidth()/2);
					linkArea4.setY(node.getY() + image.getHeight());
				}
			});
		});
	}
}