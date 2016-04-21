package graphics;

import javafx.scene.shape.Line;

/**
 * Classe définissant un lien graphique
 * @author Tanguy
 */
public class Link 
{
    Component image1 = null;
    Component image2 = null;
    Line lien1 = null;
    Line lien2 = null;
    Line lien3 = null;
    int linkAreaUsed1 = 0;
    int linkAreaUsed2 = 0;

    public Link(Component image1, Component image2,int linkAreaUsed1,int linkAreaUsed2, Line lien1, Line lien2,Line lien3)
    {
        this.image1 = image1;
        this.image2 = image2;
        this.linkAreaUsed1 = linkAreaUsed1;
        this.linkAreaUsed2 = linkAreaUsed2;
        this.lien1 = lien1;
        this.lien2 = lien2;
        this.lien3 = lien3;
    }

    public  Link(){

    }
    public Component getImage1 (){
        return this.image1;
    }
    public Component getImage2 (){
        return this.image2;
    }
}
