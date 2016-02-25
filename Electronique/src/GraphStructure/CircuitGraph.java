package GraphStructure;

import Components.*;
import org.jgrapht.graph.DirectedMultigraph;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Raphaël on 19/02/2016.
 */
public class CircuitGraph {

    public DirectedMultigraph<Vertex, AbstractDipole> graph;

    public CircuitGraph() {
        graph = new DirectedMultigraph<Vertex, AbstractDipole>(AbstractDipole.class);
    }

    //Procédure d'ajout de sommet
    public void add_vertex(Vertex new_vertex) {
        graph.addVertex(new_vertex);
    }

    //Procédure d'ajout d'arrete
    public void add_component(Vertex vertex1,Vertex vertex2, AbstractDipole composant) {
        graph.addEdge(vertex1,vertex2,composant);
    }

    public Vertex[] get_all_vertices() {
        Set<Vertex> set = graph.vertexSet();
        return set.toArray(new Vertex[set.size()]);
    }
    
    //Fonction de récupération des générateurs
    public ArrayList<Generator> get_all_generators() {
        ArrayList<Generator> s = new ArrayList<>();
        Set<AbstractDipole> set = graph.edgeSet();
        AbstractDipole[] d = set.toArray(new AbstractDipole[set.size()]);
        for (int i = 0; i < d.length; i++) {
            if ((d[i].type() == Type.CURRENT_GENERATOR) || (d[i].type() == Type.VOLTAGE_GENERATOR)) {
                s.add((Generator) d[i]);
            }
        }
        return s;
    }

    public ArrayList<Admittance> get_all_admittances(Vertex s,Vertex d) {
        ArrayList<Admittance> ret = new ArrayList<>();
        Set<AbstractDipole> set = graph.getAllEdges(s,d);
        AbstractDipole[] tab = set.toArray(new AbstractDipole[set.size()]);
        for (int i = 0; i < tab.length; i++) {
            if (tab[i].type() == Type.ADMITTANCE) {
                ret.add((Admittance) tab[i]);
            }
        }
        return ret;
    }

    public AbstractDipole[] incomingComponentsOf(Vertex v) {
        Set<AbstractDipole> comp = graph.incomingEdgesOf(v);
        return comp.toArray(new AbstractDipole[comp.size()]);
    }

    public AbstractDipole[] outgoingComponentsOf(Vertex v) {
        Set<AbstractDipole> comp = graph.outgoingEdgesOf(v);
        return comp.toArray(new AbstractDipole[comp.size()]);
    }

    public ArrayList<map> getConnectedComponents(Vertex v) {
        ArrayList<map> maps = new ArrayList<map>();

        //first : get all incoming components
        AbstractDipole[] inc = incomingComponentsOf(v);
        // and put them in the map list, after determining their source vertex
        for (int i = 0;i<inc.length;i++) {
            maps.add(new map(inc[i], graph.getEdgeSource(inc[i]), true));
        }

        //second : get all outgoing components
        AbstractDipole[] out = outgoingComponentsOf(v);
        // and put them in the map list, after determining their target vertex
        for (int i = 0;i<out.length;i++) {
            maps.add(new map(out[i],graph.getEdgeTarget(out[i]),false));
        }

        return maps;
    }
}
