package graphics;

import components.Type;
import javafx.scene.image.ImageView;

/** Composant avec ces zones de liens et les valeurs necessaire et les noms
 * Created by tanguy on 15/04/16.
 */
public class Component {
    ImageView object;
    ImageView square1 = null;
    ImageView square2 = null;
    ImageView square3 = null;
    ImageView square4 = null;
    Type type;
    char orientation;
    String name;
    Double value;
    public Component(ImageView object,ImageView square1, ImageView square2,ImageView square3, ImageView square4, char orientation,String name,double value, Type type){
        this.object = object;
        this.square1 = square1;
        this.square2 = square2;
        this.square3 = square3;
        this.square4 = square4;
        this.orientation = orientation;
        this.name = name;
        this.value = value;
        this.type = type;
    }
    public String getCname (Component c){
        return c.name;
    }
    public double getCvalue (Component c){
        return c.value;
    }
    public Type getCtype (Component c){
        return c.type;
    }

    }
