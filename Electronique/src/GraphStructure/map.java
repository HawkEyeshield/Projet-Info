package GraphStructure;

import Components.AbstractDipole;

/**
 * Created by Rapha�l on 22/02/2016.
 */
public class map {

    private AbstractDipole component;
    private boolean incoming;
    private Vertex vertex;

    public map(AbstractDipole a, Vertex v, boolean incom) {
        component = a;
        vertex = v;
        incoming = incom;
    }

    public AbstractDipole component() {
        return component;
    }

    public Vertex Vertex() {
        return vertex;
    }

    public boolean incoming() {
        return incoming;
    }
}