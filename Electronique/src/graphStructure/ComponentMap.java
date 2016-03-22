package graphStructure;

import components.AbstractDipole;

/**
 * @author Raphaël
 */
public class ComponentMap {
    /*
    cette classe permet de transmettre l'orientation d'un composant par rapport à un point
     */

    //composant concerné
    private AbstractDipole component;

    //orientation du dipole
    private boolean incoming;

    //sommet opposé du dipole
    private Vertex vertex;

    public ComponentMap(AbstractDipole a, Vertex v, boolean incom) {
        component = a;
        vertex = v;
        incoming = incom;
    }

    public AbstractDipole component() {
        return component;
    }

    public Vertex vertex() {
        return vertex;
    }

    public boolean incoming() {
        return incoming;
    }
}
