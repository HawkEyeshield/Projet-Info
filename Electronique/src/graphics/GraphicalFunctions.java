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
	public static String etat = "d";
	public static ImageView premiereImageDuLien = new ImageView();
	public static char orientationImage = 'r';

	/**
	 * Permet de faire apparaitre un generateur de tension
	 * @param anchorPane2 Zone a l interieur du scrollPane
	 * @param anchorPane4 Zone de depart des images
	 * @param scrollPane Zone pour mettre le circuit
	 */
	public static  void addVoltageGenerator (AnchorPane anchorPane2, AnchorPane anchorPane4, ScrollPane scrollPane)
	{
		ImageView firstVoltageGenerator = new ImageView(); // On créer un object de type ImageView
		Image image1 = new Image("file:image/Generateur de tension.png"); // On va la cherche au bonne endroit
		firstVoltageGenerator.setImage(image1);
		firstVoltageGenerator.setX(90);
		firstVoltageGenerator.setY(80); // On définit la position
		anchorPane4.getChildren().add(firstVoltageGenerator); // On affiche dans la zone voulu (anchorPane4)


		
        // Permet de faire apparaitre le generateur de tension dans la zone centrale en cliquant dans la zone des composants
		firstVoltageGenerator.setOnMouseClicked(event -> 
		{
			System.out.println("Un generateur de tension devrait apparaitre");
			ImageView tensionGenerator = new ImageView();
			Image image = new Image("file:image/Generateur de tension.png");
			tensionGenerator.setImage(image);
			tensionGenerator.setX(100);
			tensionGenerator.setY(100);
			anchorPane2.getChildren().add(tensionGenerator);

			//Permet de creer un lien
			tensionGenerator.setOnMouseClicked(event2 ->
			{
				if(etat == "l2"){
					System.out.println("On a cliqué sur le second objet");
					AddLink.addLink(premiereImageDuLien,tensionGenerator,orientationImage,'h', anchorPane2);
				}
				if (etat == "l1"){
					System.out.println("On a cliqué sur le premier objet");
					etat = "l2";
					orientationImage = 'h';
					premiereImageDuLien = tensionGenerator;
				}
			});


           // Permet de deplacer le generateur de tension d'un point a un autre
			tensionGenerator.setOnMouseDragged(event1 -> 
			{
				
                // position de l'image
				double imagx = image.getWidth();
				double imagy = image.getHeight();

				//System.out.println(imagx);
                // on récupère la taille de la fenêtre
				double x=event1.getSceneX();
				double y=event1.getSceneY();
				//System.out.println(x);
				
                // Permet de ne pas sortir du cadre
				if(x < (imagx/2)) {x = (imagx/2);} // on evite de sortir du cadre a gauche
				if(y < (65 + imagy/2)) {y = (65 + imagy/2);} // on evite de sortir du cadre en haut

				double mx = anchorPane2.getWidth();

				double my = anchorPane2.getHeight();
				if(x + imagx/2 > mx - 20) {x = mx - imagx/2 - 20;} // on evite de sortir du cadre a droite

				/* Ici on prend la taille de l'écran, on lui enlève la scrollbare et on regarde si le bord de l'image
				 * c est a dire le centre x plus la largeur de l image divise par 2 */
				if(y + imagy/2 > my - 20) {y = my - imagy/2 - 20;} // on evite de sortir du cadre en bas

				tensionGenerator.relocate(x-imagx/2, y-imagy/2);
				tensionGenerator.setX(x-imagx/2);
				tensionGenerator.setY(y-imagy/2);
			});

		});

	}
	
	/**
	 * Permet de faire apparaitre un generateur de courant
	 * @param anchorPane3 Zone a l interieur du scrollPane
	 * @param anchorPane4 Zone de depart des images
	 * @param scrollPane Zone pour mettre le circuit
	 */
	public static void addCourantGenerator (AnchorPane anchorPane3, AnchorPane anchorPane4, ScrollPane scrollPane)
	{
		ImageView firstCourantGenerator = new ImageView();
		Image image2 = new Image("file:image/Generateur de courant.png");
		firstCourantGenerator.setImage(image2);
		firstCourantGenerator.setX(10);
		firstCourantGenerator.setY(80);
		anchorPane4.getChildren().add(firstCourantGenerator);

		
        //Permet de faire apparaitre le generateur de courant
		firstCourantGenerator.setOnMouseClicked(event -> {
			System.out.println("Un generateur de courant devrait apparaitre");
			ImageView courantGenerator = new ImageView();
			Image image = new Image("file:image/Generateur de courant.png");
			courantGenerator.setImage(image);
			courantGenerator.setX(200);
			courantGenerator.setY(100);
			anchorPane3.getChildren().add(courantGenerator);

			/*
			Permet de faire appaitre un lien entre deux objets
			 */


			/*
            Permet de deplacer le generateur de courant d'un point a un autre
			 */

			courantGenerator.setOnMouseDragged(event1 -> 
			{
				
                // taille de l'image
				double imagx = image.getWidth();
				double imagy = image.getHeight();

				
                //on récupère la taille de la fenêtre
				double x=event1.getSceneX();
				double y=event1.getSceneY();
				
                //Permet de ne pas sortir du cadre
				if(x < (imagx/2)) {x = (imagx/2);} // on evite de sortir du cadre a gauche
				if(y < (65 + imagy/2)) {y = (65 + imagy/2);} // on evite de sortir du cadre en haut

				double mx = anchorPane3.getWidth();

				double my = anchorPane3.getHeight();
				if(x + imagx/2 > mx - 20) {x = mx - imagx/2 - 20;} // on evite de sortir du cadre a droite
				
				/* Ici on prend la taille de l'écran, on lui enlève la scrollbare et on regarde si le bord de l'image
				 * c est a dire le centre x plus la largeur de l image divise par 2*/
				if(y + imagy/2 > my - 20) {y = my - imagy/2 - 20;} // on evite de sortir du cadre en bas

				courantGenerator.relocate(x-imagx/2, y-imagy/2);
				courantGenerator.setX(x-imagx/2);
				courantGenerator.setY(y-imagy/2);
			});
		});
	}

	/**
	 * Permet de faire apparaitre un noeud
	 * @param anchorPane3 Zone a l interieur du scrollPane
	 * @param anchorPane4 Zone de depart des images
	 * @param scrollPane Zone pour mettre le circuit
	 */
	public static void addNode (AnchorPane anchorPane3, AnchorPane anchorPane4, ScrollPane scrollPane, Button CreeUnLien)
	{
		ImageView firstNode = new ImageView();
		Image image2 = new Image("file:image/point.gif");
		firstNode.setImage(image2);
		firstNode.setX(10);
		firstNode.setY(170);
		anchorPane4.getChildren().add(firstNode);


		
        //Permet de faire apparaitre un noeud

		firstNode.setOnMouseClicked(event -> 
		{
			System.out.println("Un generateur de courant devrait apparaitre");
			ImageView node = new ImageView();
			Image image = new Image("file:image/point.gif");
			node.setImage(image);
			node.setX(300);
			node.setY(100);
			anchorPane3.getChildren().add(node);

			
            //Permet de deplacer le noeud d'un point a un autre
			node.setOnMouseDragged(event1 -> 
			{
				
                //position de la souris 
				double imagx = image.getWidth();
				double imagy = image.getHeight();

				
                //on récupère la taille de la fenêtre
				double x=event1.getSceneX();
				double y=event1.getSceneY();
				
                //Permet de ne pas sortir du cadre
				if(x < (imagx/2)) {x = (imagx/2);} // on evite de sortir du cadre a gauche
				if(y < (65 + imagy/2)) {y = (65 + imagy/2);} // on evite de sortir du cadre en haut

				double mx = anchorPane3.getWidth();

				double my = anchorPane3.getHeight();
				if(x + imagx/2 > mx - 20) {x = mx - imagx/2 - 20;} // on evite de sortir du cadre a droite
				
				/* Ici on prend la taille de l'écran, on lui enlève la scrollbare et on regarde si le bord de l'image
				 * c est a dire le centre x plus la largeur de l image divise par 2*/

				if(y + imagy/2 > my - 20) {y = my - imagy/2 - 20;} // on evite de sortir du cadre en bas

				node.relocate(x-imagx/2, y-imagy/2);
				node.setX(x-imagx/2);
				node.setY(y-imagy/2);
			});
		});
	}
}