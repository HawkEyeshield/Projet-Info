package graphStructure;

import components.AbstractDipole;

/**
 * Cette classe permet de transmettre l'orientation d'un composant par rapport à un point
 * @author Raphaël
 */
public class ComponentMap 
{
	/* ========================= */
	/* Déclaration des attributs */
	/* ========================= */
	
    /**composant concerné*/
    private AbstractDipole component;

    /**orientation du dipole*/
    private boolean incoming;

    /**sommet opposé du dipole*/
    private Vertex vertex;

    /* =========================== */
    /* Déclaration du constructeur */
    /* =========================== */
    
    /**
     * Constructeur de cette classe
     * @param a : le dipole concerné
     * @param v : sommet opposé au dipôle
     * @param incom : orientation du dipôle
     */
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
