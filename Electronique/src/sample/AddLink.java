package sample;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;


/**
 * Created by tanguy on 09/03/16.
 * Permet de créer des liens entre les composants
 */
public class AddLink {

    private class link{
        ImageView image1;
        char zone1;
        ImageView image2;
        char zone2;
        Line line1; //Line 1 est relie a l'image 1
        Line line2; //Line 2 est relié a l'image 2

        public void Link(ImageView image1,ImageView image2, Line line1, Line line2, char zone1, char zone2) {
            this.image1 = image1;
            this.image2 = image2;
            this.line1 = line1;
            this.line2 = line2;
            this.zone1 = zone1;
            this.zone2 = zone2;
        }
    }
}
