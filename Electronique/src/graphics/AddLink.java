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
        ImageView image1;
        ImageView image2;
        ImageView zoneImage1;
        ImageView zoneImage2;
        Line lien1;
        Line lien2;
        Line lien3;
        char orientationImage1;
        char orientationImage2;

        public link(ImageView image1, ImageView image2,ImageView zoneImage1, ImageView zoneImage2, char orientationImage1, char orientationImage2, Line lien1, Line lien2,Line lien3) {
            this.image1 = image1;
            this.image2 = image2;
            this.zoneImage1 = zoneImage1;
            this.zoneImage2 = zoneImage2;
            this.orientationImage1 = orientationImage1;
            this.orientationImage2 = orientationImage2;
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
    public static void addLink(Component premiereImageDuLien,Component secondeImageDuLien, int linkAreaUsed1, int linkAreaUsed2, AnchorPane anchorPane2) {
        /*double tailleXImage1 = premiereImageDuLien.object.getImage().getHeight();
        double tailleXImage2 = secondeImageDuLien.object.getImage().getHeight();
        double tailleYImage1 = premiereImageDuLien.object.getImage().getWidth();
        double tailleYImage2 = secondeImageDuLien.object.getImage().getWidth();
        double positionXImage1 = premiereImageDuLien.object.getX();
        double positionXImage2 = secondeImageDuLien.object.getX();
        double positionYImage1 = premiereImageDuLien.object.getY();
        double positionYImage2 = secondeImageDuLien.object.getY();
        double centreXLinkArea1 = positionXImage1 + tailleXImage1/2 ;
        double centreYLinkArea1 = positionYImage1 + tailleYImage1/2;
        double centreXLinkArea2 = positionXImage2 + tailleXImage2/2;
        double centreYLinkArea2 = positionYImage2 + tailleYImage2/2;*/
        double centreXLinkArea1 = 0;
        double centreYLinkArea1 = 0;
        double centreXLinkArea2 = 0;
        double centreYLinkArea2 = 0;
        if (linkAreaUsed1 == 1){
            centreXLinkArea1 = premiereImageDuLien.square1.getX() + 5;
            centreYLinkArea1 = premiereImageDuLien.square1.getY() + 5;
        }
        else if (linkAreaUsed1 == 2){
            centreXLinkArea1 = premiereImageDuLien.square2.getX() + 5;
            centreYLinkArea1 = premiereImageDuLien.square2.getY() + 5;
        }
        else if (linkAreaUsed1 == 3){
            centreXLinkArea1 = premiereImageDuLien.square3.getX() + 5;
            centreYLinkArea1 = premiereImageDuLien.square3.getY() + 5;
        }
        else if (linkAreaUsed1 == 4){
            centreXLinkArea1 = premiereImageDuLien.square4.getX() + 5;
            centreYLinkArea1 = premiereImageDuLien.square4.getY() + 5;
        }
        if (linkAreaUsed2 == 1){
            centreXLinkArea2 = secondeImageDuLien.square1.getX() + 5;
            centreYLinkArea2 = secondeImageDuLien.square1.getY() + 5;
        }
        else if (linkAreaUsed2 == 2){
            centreXLinkArea2 = secondeImageDuLien.square2.getX() + 5;
            centreYLinkArea2 = secondeImageDuLien.square2.getY() + 5;
        }
        else if (linkAreaUsed2 == 3){
            centreXLinkArea2 = secondeImageDuLien.square3.getX() + 5;
            centreYLinkArea2 = secondeImageDuLien.square3.getY() + 5;
        }
        else if (linkAreaUsed2 == 4){
            centreXLinkArea2 = secondeImageDuLien.square4.getX() + 5;
            centreYLinkArea2 = secondeImageDuLien.square4.getY() + 5;
        }




        if( premiereImageDuLien.orientation == 'h' && secondeImageDuLien.orientation == 'h'){
            System.out.println("les deux h ca devrait faire quelques chose");
            System.out.println(centreXLinkArea1);
            System.out.println(centreXLinkArea2);
            System.out.println(centreYLinkArea1);
            System.out.println(centreYLinkArea2);
            Line line1 = new Line(centreXLinkArea1,centreYLinkArea1,( centreXLinkArea1 + centreXLinkArea2 )/2,centreYLinkArea1);
            anchorPane2.getChildren().add(line1);

            Line line2 = new Line(centreXLinkArea2,centreYLinkArea2,(centreXLinkArea1 + centreXLinkArea2)/2,centreYLinkArea2);
            anchorPane2.getChildren().add(line2);

            Line line3 = new Line((centreXLinkArea1 + centreXLinkArea2)/2,centreYLinkArea1,(centreXLinkArea1 + centreXLinkArea2)/2, centreYLinkArea2);
            anchorPane2.getChildren().add(line3);
        }
        if( premiereImageDuLien.orientation == 'v' && secondeImageDuLien.orientation == 'v'){
            Line line1 = new Line(centreXLinkArea1,centreYLinkArea1,centreXLinkArea1,(centreYLinkArea1 + centreYLinkArea1)/2);
            anchorPane2.getChildren().add(line1);

            Line line2 = new Line(centreXLinkArea2,centreYLinkArea2,centreXLinkArea2,(centreYLinkArea1 + centreYLinkArea2)/2);
            anchorPane2.getChildren().add(line2);

            Line line3 = new Line(centreXLinkArea1,(centreYLinkArea1 + centreYLinkArea2)/2,centreXLinkArea2,(centreYLinkArea1 + centreYLinkArea2)/2);
            anchorPane2.getChildren().add(line3);
        }
        if((premiereImageDuLien.orientation == 'h' && secondeImageDuLien.orientation == 'v') || (premiereImageDuLien.orientation == 't' && secondeImageDuLien.orientation == 'v') || (premiereImageDuLien.orientation == 'h' && secondeImageDuLien.orientation == 't')){
            Line line1 = new Line(centreXLinkArea1,centreYLinkArea1,centreXLinkArea2,centreYLinkArea1);
            anchorPane2.getChildren().add(line1);

            Line line2 = new Line(centreXLinkArea2,centreYLinkArea2,centreXLinkArea2,centreYLinkArea1);
            anchorPane2.getChildren().add(line2);
        }
        if((premiereImageDuLien.orientation == 'v' && secondeImageDuLien.orientation == 'h') || (premiereImageDuLien.orientation == 't' && secondeImageDuLien.orientation == 'h') || (premiereImageDuLien.orientation == 'v' && secondeImageDuLien.orientation == 't')){
            Line line1 = new Line(centreXLinkArea2,centreYLinkArea2,centreXLinkArea1,centreYLinkArea2);
            anchorPane2.getChildren().add(line1);

            Line line2 = new Line(centreXLinkArea1,centreYLinkArea1,centreXLinkArea1,centreYLinkArea2);
            anchorPane2.getChildren().add(line2);
            //System.out.println(centreXLinkArea1);
            //System.out.println(centreYLinkArea1);
            System.out.println(centreXLinkArea2);
            //System.out.println(centreYLinkArea2);
            //System.out.println(centreYLinkArea2);
            //System.out.println(tailleYImage2);
        }
        if(premiereImageDuLien.orientation == 't' && secondeImageDuLien.orientation == 't'){
            Line line1 = new Line(centreXLinkArea1,centreYLinkArea1,centreXLinkArea2,centreYLinkArea2);
            anchorPane2.getChildren().add(line1);
        }
        GraphicalFunctions.nombreDeLien += 1;
        //GraphicalFunctions.boardOfLink[nombreDeLien] = new link();
    }
    public static void deleteViewOfLink(ImageView premiereImageDuLien, ImageView secondeImageDuLien, char orientationImage1, char orientationImage2) {
    }


}