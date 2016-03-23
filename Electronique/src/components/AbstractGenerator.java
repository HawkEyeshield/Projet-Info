package components;

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
     * @param first_link : premier lien
     * @param second_link : second lien
     */
    public AbstractGenerator(String name, Type t, int first_link, int second_link) 
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
