package GraphStructure;

import Components.*;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Raphaël on 19/02/2016.
 */
public class CircuitGraph {

    public SimpleGraph<Vertex, Edge> graph;

    public CircuitGraph() {
        graph = new SimpleGraph<Vertex, Edge>(Edge.class);
    }

    //Procédure d'ajout de sommet
    public void add_vertex(Vertex new_vertex) {
        graph.addVertex(new_vertex);
    }

    //Procédure d'ajout de composant
    public void add_component(Vertex src,Vertex dst, AbstractDipole composant) {
        boolean b = isComponentBetween(src,dst);
        if (!b) {//if we need to create an edge
            switch(composant.type()) {
                case ADMITTANCE:
                    graph.addEdge(src, dst, new Edge(src,dst,(Admittance)composant));break;
                case CURRENT_GENERATOR:
                    graph.addEdge(src, dst, new Edge(src,dst,(Generator)composant));break;
                case VOLTAGE_GENERATOR:
                    graph.addEdge(src, dst, new Edge(src,dst,(Generator)composant));break;
                default:break;
            }
        }
        else {
            Edge e = getEdge(src,dst);//getting the current edge
            switch(composant.type()) {
                case ADMITTANCE:
                    e.addAdmittance(src,(Admittance)composant);break;//add the new admittance according to the source vertex
                case CURRENT_GENERATOR:
                    e.setGenerator(src, (Generator) composant);break;//Modify the generator according to the source vertex
                case VOLTAGE_GENERATOR:
                    e.setGenerator(src, (Generator) composant);break;//same thing
                default:break;
            }
        }
    }

    public Edge getEdge(Vertex src,Vertex dst) {
        return graph.getEdge(src,dst);
    }

    public boolean isComponentBetween(Vertex src,Vertex dst) {
        if ((graph.getEdge(src,dst) != null)) return true;
        return false;
    }

    public Vertex[] get_all_vertices() {
        Set<Vertex> set = graph.vertexSet();
        return set.toArray(new Vertex[set.size()]);
    }
    
    //Fonction de récupération des générateurs
    public ArrayList<Generator> get_all_generators() {
        ArrayList<Generator> s = new ArrayList<>();
        Set<Edge> set = graph.edgeSet();
        Edge[] d = set.toArray(new Edge[set.size()]);
        for (int i = 0; i < d.length; i++) {
            Generator g;
            if ((g = d[i].generator()) != null) {
                s.add(g);
            }
        }
        return s;
    }

    private Edge[] edgesOf(Vertex v) {
        Set<Edge> set = graph.edgesOf(v);
        return set.toArray(new Edge[set.size()]);

    }

    public boolean multipleAdmittances(Vertex v0,Vertex v1) {
        Edge e = graph.getEdge(v0,v1);
        if (e==null) return false;
        if (e.AdmittancesNb()>1) return true;
        return false;
    }
    public ArrayList<componentMap> getConnectedComponents(Vertex v) {
        ArrayList<componentMap> maps = new ArrayList<componentMap>();
        //First : get all Edges.
        Edge[] edges = edgesOf(v);

        //temp vars
        Edge e_tmp; //a temp var for the current edge
        Vertex v_tmp;
        ArrayList<AbstractDipole> a_tmp;//a temp var for the componements

        //For all Edges :
        for (int i = 0;i<edges.length;i++) {
            e_tmp = edges[i];
            if (e_tmp.beginsWith(v)) v_tmp = e_tmp.endVertex();
            else v_tmp = e_tmp.beginVertex();

            //get the edges components begining with this vertex
            a_tmp = e_tmp.componentsFrom(v);
            for (int j=0;j<a_tmp.size();j++) {
                maps.add(new componentMap(a_tmp.get(j),v_tmp,true));
            }
            //get edges ending with this vertex
            a_tmp = e_tmp.componentsTo(v);
            for (int j=0;j<a_tmp.size();j++) {
                maps.add(new componentMap(a_tmp.get(j),v_tmp,false));
            }
        }
        return maps;
    }
}
