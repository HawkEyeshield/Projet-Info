package components;

/** Classe abstraite des générateurs
 * @author Raphaël
 */
public abstract class Generator extends AbstractDipole 
{

/* ========================= */
/* Déclaration des attributs */
/* ========================= */
	
	/** Etat du générateur, éteint ou allumé */
    protected boolean isActive = true;
    
    protected boolean determination;

/* =========================== */
/* Déclaration du constructeur */
/* =========================== */
    
    public Generator(String name, Type t, int first_link, int second_link) 
    {
        super(name, t, first_link, second_link);
    }

/* ======================== */
/* Déclaration des méthodes */
/* ======================== */
    
    /** Modifie l'état du générateur à allumé*/
    public void turnOn() 
    {
        isActive = true;
    }

    /** Modifie l'état du générateur à éteint*/
    public void turnOff() 
    {
        isActive = false;
    }
    
    /** Renseigne l'état du générateur*/
    public boolean isActive() 
    {
        return isActive;
    }




}
