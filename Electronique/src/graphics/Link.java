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

    /**
     * Constructeur d un lien pour l interface graphique entre deux composants
     * @param image1 Image du premier composant relie
     * @param image2 Image du second composant relie
     * @param linkAreaUsed1 Premiere zone (carre noir) utilisee
     * @param linkAreaUsed2 Seconde zone (carre noir) utilisee
     * @param lien1 Lien graphique
     * @param lien2 Lien graphique
     * @param lien3 Lien graphique
     */
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
    //Quelques fonctions qui permettent d'acceder a differente valeur
    public Component getImage1 (){
        return this.image1;
    }
    public Component getImage2 (){
        return this.image2;
    }
}
