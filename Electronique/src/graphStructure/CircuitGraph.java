package graphStructure;

import components.*;
import components.AbstractDipole;
import components.Generator;

import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.Set;

/**
 * @author Raphaël
 */
public class CircuitGraph 
{

    public SimpleGraph<Vertex, Edge> graph;

    public CircuitGraph() 
    {
        graph = new SimpleGraph<Vertex, Edge>(Edge.class);
    }

    //Procédure d'ajout de sommet
    public void addVertex(Vertex newVertex)
    {
        graph.addVertex(newVertex);
    }

    //Procédure d'ajout de composant
    public void addComponent(Vertex src, Vertex dst, AbstractDipole composant)
    {
        boolean b = isComponentBetween(src,dst);
        if (!b) 
        {//if we need to create an edge
            switch(composant.type()) 
            {
                case ADMITTANCE:
                    graph.addEdge(src, dst, new Edge(src,dst,(Admittance)composant));break;
                case CURRENTGENERATOR:
                    graph.addEdge(src, dst, new Edge(src,dst,(Generator)composant));break;
                case VOLTAGEGENERATOR:
                    graph.addEdge(src, dst, new Edge(src,dst,(Generator)composant));break;
                default:break;
            }
        }
        else 
        {
            Edge e = getEdge(src,dst);//getting the current edge
            switch(composant.type()) 
            {
                case ADMITTANCE:
                    e.addAdmittance(src,(Admittance)composant);break;//add the new admittance according to the source vertex
                case CURRENTGENERATOR:
                    e.setGenerator(src, (Generator) composant);break;//Modify the generator according to the source vertex
                case VOLTAGEGENERATOR:
                    e.setGenerator(src, (Generator) composant);break;//same thing
                default:break;
            }
        }
    }

    public Edge getEdge(Vertex src,Vertex dst) 
    {
        return graph.getEdge(src,dst);
    }

    public boolean isComponentBetween(Vertex src,Vertex dst) 
    {
        if ((graph.getEdge(src,dst) != null)) return true;
        return false;
    }

    public Vertex[] getAllVertices()
    {
        Set<Vertex> set = graph.vertexSet();
        return set.toArray(new Vertex[set.size()]);
    }

    public Edge[] getAllEdges()
    {
        Set<Edge> set = graph.edgeSet();
    return set.toArray(new Edge[set.size()]);
}

    //Fonction de récupération des générateurs
    public ArrayList<Generator> getAllGenerators()
    {
        ArrayList<Generator> s = new ArrayList<>();
        Set<Edge> set = graph.edgeSet();
        Edge[] d = set.toArray(new Edge[set.size()]);
        for (int i = 0; i < d.length; i++) 
        {
            Generator g;
            if ((g = d[i].generator()) != null) 
            {
                s.add(g);
            }
        }
        return s;
    }

    private Edge[] edgesOf(Vertex v) 
    {
        Set<Edge> set = graph.edgesOf(v);
        return set.toArray(new Edge[set.size()]);

    }

    public boolean multipleAdmittances(Vertex v0,Vertex v1) 
    {
        Edge e = graph.getEdge(v0,v1);
        if (e==null) return false;
        return e.AdmittancesNb() > 1;
    }
    public ArrayList<ComponentMap> getConnectedComponents(Vertex v)
    {
        ArrayList<ComponentMap> maps = new ArrayList<ComponentMap>();
        //First : get all Edges.
        Edge[] edges = edgesOf(v);

        //temp vars
        Edge tE; //a temp var for the current edge
        Vertex tV;
        ArrayList<AbstractDipole> tA;//a temp var for the componements

        //For all Edges :
        for (int i = 0;i<edges.length;i++) 
        {
            tE = edges[i];
            if (tE.beginsWith(v)) tV = tE.endVertex();
            else tV = tE.beginVertex();

            //get the edges components begining with this vertex
            tA = tE.componentsFrom(v);
            for (int j=0;j<tA.size();j++)
            {
                maps.add(new ComponentMap(tA.get(j),tV,true));
            }
            //get edges ending with this vertex
            tA = tE.componentsTo(v);
            for (int j=0;j<tA.size();j++)
            {
                maps.add(new ComponentMap(tA.get(j),tV,false));
            }
        }
        return maps;
    }
}
