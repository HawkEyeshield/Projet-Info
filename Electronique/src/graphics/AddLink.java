package graphics;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;


/**
 * @author Tanguy
 * Permet de créer des liens entre les composants
 */
public class AddLink {

    public static class link {
        Component image1;
        Component image2;
        Line lien1;
        Line lien2;
        Line lien3;
        int linkAreaUsed1;
        int linkAreaUsed2;

        public link(Component image1, Component image2,int linkAreaUsed1,int linkAreaUsed2, Line lien1, Line lien2,Line lien3)
        {
            this.image1 = image1;
            this.image2 = image2;
            this.linkAreaUsed1 = linkAreaUsed1;
            this.linkAreaUsed2 = linkAreaUsed2;
            this.lien1 = lien1;
            this.lien2 = lien2;
            this.lien3 = lien3;
        }
    }

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
            for (int i = 0; i < GraphicalFunctions.nombreDeLien; i++) {
                if ((premiereImageDuLien == GraphicalFunctions.boardOfLink[i].image1 && linkAreaUsed1 == GraphicalFunctions.boardOfLink[i].linkAreaUsed1) || (premiereImageDuLien == GraphicalFunctions.boardOfLink[i].image2 && linkAreaUsed1 == GraphicalFunctions.boardOfLink[i].linkAreaUsed2) || (secondeImageDuLien == GraphicalFunctions.boardOfLink[i].image1 && linkAreaUsed2 == GraphicalFunctions.boardOfLink[i].linkAreaUsed1) || (secondeImageDuLien == GraphicalFunctions.boardOfLink[i].image2 && linkAreaUsed2 == GraphicalFunctions.boardOfLink[i].linkAreaUsed2)) {
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
                GraphicalFunctions.boardOfLink[GraphicalFunctions.nombreDeLien] = new link(premiereImageDuLien, secondeImageDuLien, linkAreaUsed1, linkAreaUsed2, line1, line2, line3);
                System.out.println("on devrait rajouter un truc a la case " + GraphicalFunctions.nombreDeLien);
                GraphicalFunctions.nombreDeLien += 1;
                //TODO ici mettre la fonction qui ajoute le lien dans la breadboard
            }
        }
        else{ //Sinon c'est que c'est qu'il faut juste actualiser les liens
            anchorPane2.getChildren().add(line1);
            anchorPane2.getChildren().add(line2);
            anchorPane2.getChildren().add(line3);
            GraphicalFunctions.boardOfLink[k].lien1 = line1;
            GraphicalFunctions.boardOfLink[k].lien2 = line2;
            GraphicalFunctions.boardOfLink[k].lien3 = line3;
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
        for (int i = 0; i < GraphicalFunctions.nombreDeLien;i++){
            if(GraphicalFunctions.boardOfLink[i].image1 == image || GraphicalFunctions.boardOfLink[i].image2 == image){
                anchorPane2.getChildren().remove(GraphicalFunctions.boardOfLink[i].lien1);
                anchorPane2.getChildren().remove(GraphicalFunctions.boardOfLink[i].lien2);
                anchorPane2.getChildren().remove(GraphicalFunctions.boardOfLink[i].lien3);
                AddLink.addLink(GraphicalFunctions.boardOfLink[i].image1,GraphicalFunctions.boardOfLink[i].image2,GraphicalFunctions.boardOfLink[i].linkAreaUsed1,GraphicalFunctions.boardOfLink[i].linkAreaUsed2,i, anchorPane2);
            }
        }
    }

}