package components;

import graphStructure.Vertex;

/** Classe abstraite des générateurs, hérite de AbstractDipole
 * @author Raphaël
 */
public abstract class AbstractGenerator extends AbstractDipole 
{
	/* ========================= */
	/* Déclaration des attributs */
	/* ========================= */
	
    /**etat du generateur, actif ou éteint*/
    protected boolean isActive = true;

    /**etat de determination du parametre*/
    protected boolean determination;
    
    /* ========================== */
    /* Déclaration du construteur */
    /* ========================== */
    
    /**
     * Constructeur de la classe abstraite pour générateurs
     * @param name : nom du générateur
     * @param t : type de composant
     * @param first_link : Sommet auquel est relié la première patte
     * @param second_link : Sommet auquel est relié la première patte
     */
    public AbstractGenerator(String name, Type t, Vertex first_link, Vertex second_link) 
    {
        super(name, t, first_link, second_link);
    }

    /* ======================== */
    /* Déclaration des méthodes */
    /* ======================== */
    
    /**foncion d'allumage du générateur*/
    public void turnOn() 
    {
        isActive = true;
    }

    /**fonction d'extinction du generateur*/
    public void turnOff() 
    {
        isActive = false;
    }

    /**getter d'etat du generateur*/
    public boolean isActive() 
    {
        return isActive;
    }

}
