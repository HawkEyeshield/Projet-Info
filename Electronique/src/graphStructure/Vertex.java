package graphStructure;

/**
 * @author Raphaël
 */
public class Vertex 
{
    /*Cette classe définit les sommets du CircuitGraph. Chaque sommet (vertex - vertices au pluriel)
            possede un numero entier, destiné à l'identifier durant la phase de resolution. Comme les indices des
            sommets doivent etre consecutifs pour la resolution, le plus simple est de les parametrer uniquement avant de resoudre,
            et d'identifier chaque vertex par sa reference lors de la conception du circuit.
 */

    //le numero du vertex (changeable)
    private int number;

    public Vertex() 
    {
        number = 0;
    }

    public Vertex(int i) 
    {
        number = i;
    }

    //setter du number
    public void set (int i) throws IllegalArgumentException 
    {
        if (i>=0) 
        {
            number = i;
        }
        else throw new IllegalArgumentException("Indice négatif");
    }

    //Getter du number
    public int get() 
    {
        return number;
    }
}
