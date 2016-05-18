package graphics;

import javafx.scene.shape.Line;

/**
 * Classe définissant un lien graphique
 * @author Tanguy
 */
public class Link 
{
    GraphicalComponent image1 = null;
    GraphicalComponent image2 = null;
    Line lien1 = null;
    Line lien2 = null;
    Line lien3 = null;
    int linkAreaUsed1 = 0;
    int linkAreaUsed2 = 0;
    int potentielLink = 0;

    /**
     * Constructeur d'un lien pour l'interface graphique entre deux composants
     * @param image1 Image du premier composant relié
     * @param image2 Image du second composant relié
     * @param linkAreaUsed1 Première zone (carré noir) utilisée
     * @param linkAreaUsed2 Seconde zone (carré noir) utilisée
     * @param lien1 Lien graphique
     * @param lien2 Lien graphique
     * @param lien3 Lien graphique
     */
    public Link(GraphicalComponent image1, GraphicalComponent image2, int linkAreaUsed1, int linkAreaUsed2, Line lien1, Line lien2, Line lien3)
    {
        this.image1 = image1;
        this.image2 = image2;
        this.linkAreaUsed1 = linkAreaUsed1;
        this.linkAreaUsed2 = linkAreaUsed2;
        this.lien1 = lien1;
        this.lien2 = lien2;
        this.lien3 = lien3;
        this.potentielLink = GraphicalFunctions.potentielCommun;
        GraphicalFunctions.potentielCommun += 1;
    }
    //Quelques fonctions qui permettent d'acceder à differentes valeurs
    public GraphicalComponent getImage1 (){
        return this.image1;
    }
    public GraphicalComponent getImage2 (){
        return this.image2;
    }
    
    public int getFirstArea()
    {
    	return this.linkAreaUsed1;
    }
    
    public int getSecondArea()
    {
    	return this.linkAreaUsed2;
    }

    public  int getPotentielLink() {return this.potentielLink;}

    public void setPotentielLink(int a) {this.potentielLink =a;}
}
