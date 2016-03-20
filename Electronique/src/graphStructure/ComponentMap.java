package graphStructure;

import components.AbstractDipole;

/**
 * @author Raphaël
 */
public class ComponentMap
{

/* ========================= */
/* Déclaration des attributs */
/* ========================= */

    private AbstractDipole component;
    private boolean incoming;
    private Vertex vertex;
    
/* =========================== */
/* Déclaration du constructeur */
/* =========================== */
    
    public ComponentMap(AbstractDipole a, Vertex v, boolean incom)
    {
        component = a;
        vertex = v;
        incoming = incom;
    }
    
/* ======================== */
/* Déclaration des méthodes */
/* ======================== */

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
