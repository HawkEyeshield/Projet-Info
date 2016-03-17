package graphStructure;

/**
 * @author Raphaël
 */
public class Vertex 
{
    private int number;

    public Vertex() 
    {
        number = 0;
    }

    public Vertex(int i) 
    {
        number = i;
    }

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
