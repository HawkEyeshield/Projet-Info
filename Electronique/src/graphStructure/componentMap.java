package graphStructure;

import components.AbstractDipole;

/**
 * @author Raphaël
 */
public class componentMap 
{

    private AbstractDipole component;
    private boolean incoming;
    private Vertex vertex;

    public componentMap(AbstractDipole a, Vertex v, boolean incom) 
    {
        component = a;
        vertex = v;
        incoming = incom;
    }

    public AbstractDipole component() 
    {
        return component;
    }

    public Vertex Vertex() 
    {
        return vertex;
    }

    public boolean incoming() 
    {
        return incoming;
    }
}
