package graphStructure;

/**
 * Classe définissant les sommets utilisés dans le graphe, couplés avec les bords(edges)
 * @author Raphaël
 */
public class Vertex 
{

/* ========================= */
/* Déclaration des attributs */
/* ========================= */
	
    private int number;

/* ============================= */
/* Déclaration des constructeurs */
/* ============================= */
    
    public Vertex() 
    {
        number = 0;
    }

    public Vertex(int i) 
    {
        number = i;
    }

/* ======================== */
/* Déclaration des méthodes */
/* ======================== */
    
    public void set (int i) throws IllegalArgumentException 
    {
        if (i>=0) 
        {
            number = i;
        }
        else throw new IllegalArgumentException("Indice négatif");
    }

    public int get() 
    {
        return number;
    }
}
