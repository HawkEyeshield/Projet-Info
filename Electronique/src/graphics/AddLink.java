package graphics;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;


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
     * Permet de creer un lien entre deux images
     * @param premiereImageDuLien
     * @param secondeImageDuLien
     * @param anchorPane2
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
            anchorPane2.getChildren().add(line1);

            line2 = new Line(centreXLinkArea2, centreYLinkArea2, (centreXLinkArea1 + centreXLinkArea2) / 2, centreYLinkArea2);
            anchorPane2.getChildren().add(line2);

            line3 = new Line((centreXLinkArea1 + centreXLinkArea2) / 2, centreYLinkArea1, (centreXLinkArea1 + centreXLinkArea2) / 2, centreYLinkArea2);
            anchorPane2.getChildren().add(line3);
        }

         else if (premiereImageDuLien.orientation == 'h' && secondeImageDuLien.orientation == 'h') {

            line1 = new Line(centreXLinkArea1, centreYLinkArea1, (centreXLinkArea1 + centreXLinkArea2) / 2, centreYLinkArea1);
            anchorPane2.getChildren().add(line1);

            line2 = new Line(centreXLinkArea2, centreYLinkArea2, (centreXLinkArea1 + centreXLinkArea2) / 2, centreYLinkArea2);
            anchorPane2.getChildren().add(line2);

            line3 = new Line((centreXLinkArea1 + centreXLinkArea2) / 2, centreYLinkArea1, (centreXLinkArea1 + centreXLinkArea2) / 2, centreYLinkArea2);
            anchorPane2.getChildren().add(line3);
        }

        else if (premiereImageDuLien.orientation == 'v' && secondeImageDuLien.orientation == 'v') {
            line1 = new Line(centreXLinkArea1, centreYLinkArea1, centreXLinkArea1, (centreYLinkArea1 + centreYLinkArea2) / 2);
            anchorPane2.getChildren().add(line1);

            line2 = new Line(centreXLinkArea2, centreYLinkArea2, centreXLinkArea2, (centreYLinkArea1 + centreYLinkArea2) / 2);
            anchorPane2.getChildren().add(line2);

            line3 = new Line(centreXLinkArea1, (centreYLinkArea1 + centreYLinkArea2) / 2, centreXLinkArea2, (centreYLinkArea1 + centreYLinkArea2) / 2);
            anchorPane2.getChildren().add(line3);
        }

        else if ((premiereImageDuLien.orientation == 'h' && secondeImageDuLien.orientation == 'v') || (premiereImageDuLien.orientation == 't' && secondeImageDuLien.orientation == 'v') || (premiereImageDuLien.orientation == 'h' && secondeImageDuLien.orientation == 't')) {
            line1 = new Line(centreXLinkArea1, centreYLinkArea1, centreXLinkArea2, centreYLinkArea1);
            anchorPane2.getChildren().add(line1);
            line2 = new Line(centreXLinkArea2, centreYLinkArea2, centreXLinkArea2, centreYLinkArea1);
            anchorPane2.getChildren().add(line2);
        }
        else if ((premiereImageDuLien.orientation == 'v' && secondeImageDuLien.orientation == 'h') || (premiereImageDuLien.orientation == 't' && secondeImageDuLien.orientation == 'h') || (premiereImageDuLien.orientation == 'v' && secondeImageDuLien.orientation == 't')) {
            line1 = new Line(centreXLinkArea2, centreYLinkArea2, centreXLinkArea1, centreYLinkArea2);
            anchorPane2.getChildren().add(line1);

            line2 = new Line(centreXLinkArea1, centreYLinkArea1, centreXLinkArea1, centreYLinkArea2);
            anchorPane2.getChildren().add(line2);
        }
        else if (premiereImageDuLien.orientation == 't' && secondeImageDuLien.orientation == 't') {
            line1 = new Line(centreXLinkArea1, centreYLinkArea1, centreXLinkArea2, centreYLinkArea2);
            anchorPane2.getChildren().add(line1);
        }

        if (k == -1){ //Si on ajoute un nouveau lien k = -1
            GraphicalFunctions.boardOfLink[GraphicalFunctions.nombreDeLien] = new link(premiereImageDuLien, secondeImageDuLien, linkAreaUsed1, linkAreaUsed2, line1, line2, line3);
            System.out.println("on devrait rajouter un truc a la case "+ GraphicalFunctions.nombreDeLien);
            GraphicalFunctions.nombreDeLien += 1;
            //TODO ici mettre la fonction qui ajoute le lien dans la breadboard
        }
        else{ //Sinon c'est que c'est qu'il faut juste actualisé les liens
            GraphicalFunctions.boardOfLink[k].lien1 = line1;
            GraphicalFunctions.boardOfLink[k].lien2 = line2;
            GraphicalFunctions.boardOfLink[k].lien3 = line3;
            //TODO mettre la fonction qui actualise le lien dans la breadboard
        }

    }
    public static void deleteViewOfLink(ImageView premiereImageDuLien, ImageView secondeImageDuLien, char orientationImage1, char orientationImage2){

    }

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