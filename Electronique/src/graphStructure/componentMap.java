package graphStructure;

import components.AbstractDipole;

/**
 * @author Raphaël
 */
public class ComponentMap
{

    private AbstractDipole component;
    private boolean incoming;
    private Vertex vertex;

    public ComponentMap(AbstractDipole a, Vertex v, boolean incom)
    {
        component = a;
        vertex = v;
        incoming = incom;
    }

    public AbstractDipole component() 
    {
        return component;
    }

    public Vertex vertex()
    {
        return vertex;
    }

    public boolean incoming() 
    {
        return incoming;
    }
}
