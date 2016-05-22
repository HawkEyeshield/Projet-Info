package graphics;

import components.Type;
import javafx.scene.image.ImageView;

/** Classe qui définit un composant pour l'interface graphique
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
     * Contructeur d'un composant
     * @param object Image du composant en lui même
     * @param square1 Première zone de lien
     * @param square2 Seconde zone de lien
     * @param square3 Troisième zone de lien
     * @param square4 Quatrième zone de lien
     * @param orientation Définit si l'image est horizontale ou verticale
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
        if(type == Type.NODE) {
            this.indexation = GraphicalFunctions.potentielCommun;
            GraphicalFunctions.potentielCommun += 1;
        }
        else{
            this.indexation = -1;
        }
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
        if(type == Type.NODE) {
            this.indexation = GraphicalFunctions.potentielCommun;
            GraphicalFunctions.potentielCommun += 1;
        }
        else{
            this.indexation = -1;
        }
    }
    
    /**
     * Constructeur amoindri pour afficher les résultats
     * @param name nom du composant
     * @param value valeur caractéristique du composant
     * @param voltage tension aux bornes du composant
     * @param current courant traversant le composant
     */
    public GraphicalComponent(String name, double value, double voltage, double current,ImageView object,char orientation)
    {
    	this.name=name;
    	this.value=value;
    	this.courant=current;
    	this.voltage=voltage;
    	this.object=object;
    	this.orientation=orientation;
    }
    
    //Quelques fonctions pour acceder à différente valeurs
    public String getCname (){
        return this.name;
    }
    public double getCvalue (){
        return this.value;
    }
    public Type getCtype (){
        return this.type;
    }
    public char getCorientation(){return this.orientation;}
    public ImageView getCimage(){return this.object;}
    public  int getCindexation() {return this.indexation;}
    public void setCindexation(int a) {this.indexation =a;}
    }
