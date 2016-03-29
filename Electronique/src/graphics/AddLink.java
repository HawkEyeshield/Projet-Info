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
        char orientationImage1;
        char orientationImage2;

        public link(ImageView image1, ImageView image2, char orientationImage1, char orientationImage2) {
            this.image1 = image1;
            this.image2 = image2;
            this.orientationImage1 = orientationImage1;
            this.orientationImage2 = orientationImage2;
        }
    }

    /**
     * Permet de creer un lien entre deux images
     * @param premiereImageDuLien
     * @param secondeImageDuLien
     * @param orientationImage1
     * @param orientationImage2
     * @param anchorPane2
     */
    public static void addLink(ImageView premiereImageDuLien, ImageView secondeImageDuLien, char orientationImage1, char orientationImage2, AnchorPane anchorPane2) {
        double tailleXImage1 = premiereImageDuLien.getImage().getHeight();
        double tailleXImage2 = secondeImageDuLien.getImage().getHeight();
        double tailleYImage1 = premiereImageDuLien.getImage().getWidth();
        double tailleYImage2 = secondeImageDuLien.getImage().getWidth();
        double positionXImage1 = premiereImageDuLien.getX();
        double positionXImage2 = secondeImageDuLien.getX();
        double positionYImage1 = premiereImageDuLien.getY();
        double positionYImage2 = secondeImageDuLien.getY();
        double centreXImage1 = positionXImage1 + tailleXImage1/2 ;
        double centreYImage1 = positionYImage1 + tailleYImage1/2;
        double centreXImage2 = positionXImage2 + tailleXImage2/2;
        double centreYImage2 = positionYImage2 + tailleYImage2/2;
        if( orientationImage1 == 'h' && orientationImage2 == 'h'){
            Line line1 = new Line(centreXImage1,centreYImage1,( centreXImage1 + centreXImage2 )/2,centreYImage1);
            anchorPane2.getChildren().add(line1);

            Line line2 = new Line(centreXImage2,centreYImage2,(centreXImage1 + centreXImage2)/2,centreYImage2);
            anchorPane2.getChildren().add(line2);

            Line line3 = new Line((centreXImage1 + centreXImage2)/2,centreYImage1,(centreXImage1 + centreXImage2)/2, centreYImage2);
            anchorPane2.getChildren().add(line3);
        }
        if( orientationImage1 == 'v' && orientationImage2 == 'v'){
            Line line1 = new Line(centreXImage1,centreYImage1,centreXImage1,(centreYImage1 + centreYImage2)/2);
            anchorPane2.getChildren().add(line1);

            Line line2 = new Line(centreXImage2,centreYImage2,centreXImage2,(centreYImage1 + centreYImage2)/2);
            anchorPane2.getChildren().add(line2);

            Line line3 = new Line(centreXImage1,(centreYImage1 + centreYImage2)/2,centreXImage2,(centreYImage1 + centreYImage2)/2);
            anchorPane2.getChildren().add(line3);
        }
        if((orientationImage1 == 'h' && orientationImage2 == 'v') || (orientationImage1 == 't' && orientationImage2 == 'v') || (orientationImage1 == 'h' && orientationImage2 == 't')){
            Line line1 = new Line(centreXImage1,centreYImage1,centreXImage2,centreYImage1);
            anchorPane2.getChildren().add(line1);

            Line line2 = new Line(centreXImage2,centreYImage2,centreXImage2,centreYImage1);
            anchorPane2.getChildren().add(line2);
        }
        if((orientationImage1 == 'v' && orientationImage2 == 'h') || (orientationImage1 == 't' && orientationImage2 == 'h') || (orientationImage1 == 'v' && orientationImage2 == 't')){
            Line line1 = new Line(centreXImage2,centreYImage2,centreXImage1,centreYImage2);
            anchorPane2.getChildren().add(line1);

            Line line2 = new Line(centreXImage1,centreYImage1,centreXImage1,centreYImage2);
            anchorPane2.getChildren().add(line2);
        }
        if(orientationImage1 == 't' && orientationImage2 == 't'){
            Line line1 = new Line(centreXImage1,centreYImage1,centreXImage2,centreYImage2);
            anchorPane2.getChildren().add(line1);
        }
    }
    public static void deleteLink(ImageView premiereImageDuLien, ImageView secondeImageDuLien, char orientationImage1, char orientationImage2, AnchorPane anchorPane2) {
    }


}