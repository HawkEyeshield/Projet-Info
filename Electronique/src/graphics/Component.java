package graphics;

import javafx.scene.image.ImageView;

/**
 * Created by tanguy on 15/04/16.
 */
public class Component {
    ImageView object;
    ImageView square1 = null;
    ImageView square2 = null;
    ImageView square3 = null;
    ImageView square4 = null;
    char orientation;
    public Component(ImageView object,ImageView square1, ImageView square2,ImageView square3, ImageView square4, char orientation){
        this.object = object;
        this.square1 = square1;
        this.square2 = square2;
        this.square3 = square3;
        this.square4 = square4;
        this.orientation = orientation;
    }
}
