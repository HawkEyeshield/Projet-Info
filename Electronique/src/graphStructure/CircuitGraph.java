package graphStructure;

import components.*;
import components.AbstractDipole;
import components.AbstractGenerator;

import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.Set;

/**
 * Classe définissant la structure de graphe utilisée pour représenter le circuit
 * @author Raphaël
 */
public class CircuitGraph 
{
	/* ========================= */
	/* Déclaration des attributs */
	/* ========================= */
	
    /**le graphe de base de la classe*/
    public SimpleGraph<Vertex, Edge> graph;
    
    /* ========================== */
    /* Définition du constructeur */
    /* ========================== */
    
    /**
     * Constructeur de la classe
     */
    public CircuitGraph() 
    {//init du graphe
        graph = new SimpleGraph<Vertex, Edge>(Edge.class);
    }
    
    /* ======================== */
    /* Déclaration des méthodes */
    /* ======================== */
    
    /**Ajout simple d'un sommet, sans verification d'existence préalable dans le graphe*/
    public void addVertex(Vertex newVertex) 
    {
        graph.addVertex(newVertex);
    }

    /**
     * Procédure d'ajout de composant
     * @param src : sommet source
     * @param dst : sommet destination
     * @param composant : composant entre ces sommets
     */
    public void addComponent(Vertex src, Vertex dst, AbstractDipole composant) 
    {
        //On commence par rechercher si une arete existe déjà entre les deux points
        boolean b = isComponentBetween(src, dst);
        if (!b) 
        {//si on doit creer une arrete
        	switch (composant.type()) 
        	{//disjoinction du type de composant à creer
                case ADMITTANCE:
                    graph.addEdge(src, dst, new Edge(src, dst, (Admittance) composant));
                    break;
                case CURRENTGENERATOR:
                    graph.addEdge(src, dst, new Edge(src, dst, (AbstractGenerator) composant));
                    break;
                case VOLTAGEGENERATOR:
                    graph.addEdge(src, dst, new Edge(src, dst, (AbstractGenerator) composant));
                    break;
                default:
                    break;
            }
        } 
        else 
        {//si une arrete existe deja
            Edge e = getEdge(src, dst);//on la recupere
            switch (composant.type()) 
            {
                case ADMITTANCE://on ajoute l'admittance en donnant en parametre le sommet de depart du composant (pour que le composant soit correctement orienté
                    e.addAdmittance(src, (Admittance) composant);
                    break;
                case CURRENTGENERATOR:
                    e.setGenerator(src, (AbstractGenerator) composant);
                    break;//On modifie le generateur, en coherence avec le sommet de départ du nouveau generateur
                case VOLTAGEGENERATOR:
                    e.setGenerator(src, (AbstractGenerator) composant);
                    break;//meme combat
                default:
                    break;
            }
        }
    }

    /**Fonction de recuperation de l'arrete reliant deux points (null si il n'en existe pas)*/
    public Edge getEdge(Vertex src, Vertex dst) 
    {
        return graph.getEdge(src, dst);
    }

    /**verificattion de l'existence d'une arrete entre deux sommets*/
    public boolean isComponentBetween(Vertex src, Vertex dst) 
    {
        if ((graph.getEdge(src, dst) != null)) return true;
        return false;
    }

    /**retourne tous les sommets du graphe*/
    public Vertex[] getAllVertices() 
    {
        Set<Vertex> set = graph.vertexSet();
        return set.toArray(new Vertex[set.size()]);
    }

    /**retourne toutes les arretes du graphe*/
    public Edge[] getAllEdges() 
    {
        Set<Edge> set = graph.edgeSet();
        return set.toArray(new Edge[set.size()]);
    }

    /**retourne tous les generteurs tous les générateurs*/
    public ArrayList<AbstractGenerator> getAllGenerators() 
    {
        ArrayList<AbstractGenerator> ret = new ArrayList<>();
        AbstractGenerator g;
        //recup de toutes les arretes
        Edge[] d = getAllEdges();
        //pour chaque arrete
        for (Edge e:d)
            if ((g = e.generator()) != null) //ajout du generateur eventuel
                ret.add(g);
        return ret;
    }

    /**retourne toutes les arretes branchées sur un noeud (qu'il soit de depart ou d'arrivee)*/
    private Edge[] edgesOf(Vertex v) 
    {
        Set<Edge> set = graph.edgesOf(v);
        return set.toArray(new Edge[set.size()]);

    }

    /**retourne l'existence d'admittance multiple entre les deux sommets v0 et v1*/
    public boolean existMultiAdmittances(Vertex v0, Vertex v1)
    {
        Edge e = graph.getEdge(v0,v1);
        if (e == null) return false;
        return e.AdmittancesNb() > 1;
    }

    /**getCOnnectedComponents :
    Renvoie tous les composants connectée à un sommets v sous la forme
    ComponentMap(autre sommet du composant, composant, orientation)
    orientation est un booleen representant "Le sommet v est le sommet de depart du composant"
    */
    public ArrayList<ComponentMap> getConnectedComponents(Vertex v) 
    {
        ArrayList<ComponentMap> maps = new ArrayList<ComponentMap>();
        //avant tout recuperer toutes les arretes
        Edge[] edges = edgesOf(v);

        //temp vars
        Vertex tV;
        ArrayList<AbstractDipole> tA;//a temp var for the componements

        //pour chaque arrete
        for (Edge edge : edges) 
        {
            //determiner le sommet opposé à v dans l'arrete
            if (edge.beginsWith(v)) tV = edge.endVertex();
            else tV = edge.beginVertex();

            //1 : recuperation de tous les composant de l'arrete commencant par v
            tA = edge.componentsFrom(v);
            for (AbstractDipole dipole : tA) 
            {
                maps.add(new ComponentMap(dipole, tV, true));
            }
            //2 : recuperation de tous les composants de l'arrete finissant par v
            tA = edge.componentsTo(v);
            for (AbstractDipole dipole : tA) 
            {
                maps.add(new ComponentMap(dipole, tV, false));
            }
        }
        return maps;
    }
}
