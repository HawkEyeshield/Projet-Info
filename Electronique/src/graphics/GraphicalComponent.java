package graphics;

import components.Type;
import javafx.scene.image.ImageView;

/** Classe qui definie un composant pour l interface graphique
 * @author Tanguy
 */
public class GraphicalComponent {
    ImageView object;
    ImageView square1 = null;
    ImageView square2 = null;
    ImageView square3 = null;
    ImageView square4 = null;
    Type type;
    char orientation;
    String name;
    Double value;
    Double courant;
    Double voltage;
    int indexation;

    /**
     * Contructeur d un composant
     * @param object Image du composant en lui meme
     * @param square1 Premiere zone de lien
     * @param square2 Seconde zone de lien
     * @param square3 Troisieme zone de lien
     * @param square4 Quatrieme zone de lien
     * @param orientation Definie si l image est horizontale ou verticale
     * @param name Nom du composant
     * @param value valeur du composant
     * @param type Type du composant
     */
    public GraphicalComponent(ImageView object, ImageView square1, ImageView square2, ImageView square3, ImageView square4, char orientation, String name, double value, Type type){
        this.object = object;
        this.square1 = square1;
        this.square2 = square2;
        this.square3 = square3;
        this.square4 = square4;
        this.orientation = orientation;
        this.name = name;
        this.value = value;
        this.type = type;
        this.indexation = -1;
        if(type == Type.VOLTAGEGENERATOR){
            this.voltage = value;
        }
        if(type == Type.CURRENTGENERATOR){
            this.courant = value;
        }
    }

    public GraphicalComponent(ImageView object, ImageView square1, ImageView square2, ImageView square3, ImageView square4, char orientation, String name, Type type){
        this.object = object;
        this.square1 = square1;
        this.square2 = square2;
        this.square3 = square3;
        this.square4 = square4;
        this.orientation = orientation;
        this.name = name;
        this.type = type;
        indexation = -1;
    }
    //Quelques fonctions pour acceder a differente valeur
    public String getCname (){
        return this.name;
    }
    public double getCvalue (){
        return this.value;
    }
    public Type getCtype (){
        return this.type;
    }

    }
