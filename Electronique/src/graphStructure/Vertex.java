package graphStructure;

/**
 * Cette classe définit les sommets du CircuitGraph. Chaque sommet (vertex - vertices au pluriel)
 * possede un numero entier, destiné à l'identifier durant la phase de resolution. Comme les indices des
 * sommets doivent etre consecutifs pour la resolution, le plus simple est de les parametrer uniquement avant de resoudre,
 * et d'identifier chaque vertex par sa reference lors de la conception du circuit.
 * @author Raphaël
 */
public class Vertex 
{
	/* ========================= */
	/* Déclaration des attributs */
	/* ========================= */
	
	/**le numero du vertex (changeable)*/
    private int index;

    /* ============================ */
    /* Déclaration des contructeurs */
    /* ============================ */
    
    /**
     * Constructeur de vertex d'indice nul
     */
    public Vertex() 
    {
        index = 0;
    }
    
    /**
     * Constructeur de vertex avec choix de son numéro
     * @param i : l'entier le définissant
     */
    public Vertex(int i) 
    {
        index = i;
    }
    
    /* ======================== */
    /* Déclaration des méthodes */
    /* ======================== */
    
    //setter du number
    public void set (int i) throws IllegalArgumentException 
    {
        if (i>=0) 
        {
            index = i;
        }
        else throw new IllegalArgumentException("Indice négatif");
    }

    //Getter du number
    public int getIndex() 
    {
        return index;
    }
}
