package graphics;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;


/**
 * @author Tanguy
 * Permet de créer des liens entre les composants
 */
public class AddLink {

    private class link {
        ImageView image1;
        ImageView image2;
        char orientationImage1;
        char orientationImage2;
        Line line1; //Line 1 est relie a l'image 1
        Line line2; //Line 2 est relié a l'image 2
        Line line3; //Line 3 fait le lien entre le lien 1 et 2

        public void link(ImageView image1, ImageView image2, Line line1, Line line2, Line line3,char orientationImage1, char orientationImage2) {
            this.image1 = image1;
            this.image2 = image2;
            this.line1 = line1;
            this.line2 = line2;
            this.line3 = line3;
            this.orientationImage1 = orientationImage1;
            this.orientationImage2 = orientationImage2;
        }
    }

    public static void addLink(ImageView premiereImageDuLien, ImageView secondeImageDuLien, char orientationImage1, char orientationImage2, AnchorPane anchorPane2) {
        double tailleXImage1 = premiereImageDuLien.getImage().getHeight() + 8;
        double tailleXImage2 = secondeImageDuLien.getImage().getHeight();
        double tailleYImage1 = premiereImageDuLien.getImage().getWidth();
        double tailleYImage2 = secondeImageDuLien.getImage().getWidth();
        double positionXImage1 = premiereImageDuLien.getX();
        double positionXImage2 = secondeImageDuLien.getX();
        double positionYImage1 = premiereImageDuLien.getY();
        double positionYImage2 = secondeImageDuLien.getY();
        if(positionXImage1 < positionXImage2 && orientationImage1 == 'h' && orientationImage2 == 'h'){
            double a = positionXImage1 + tailleXImage1;
            System.out.println(tailleXImage1);
            System.out.println(positionXImage1);
            System.out.println(a);
            double b = positionYImage1 + tailleYImage1/2;
            System.out.println();
            Line line1 = new Line(a,b,a + (positionXImage2-a)/2,b);
            anchorPane2.getChildren().add(line1);

        }
        //link Link = new link(premiereImageDuLien, secondeImageDuLien, line1, line2, line3, orientationImage1, orientationImage1);
    }


}