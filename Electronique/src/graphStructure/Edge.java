package graphStructure;

import components.Admittance;
import components.AbstractDipole;
import components.AbstractGenerator;

import java.util.ArrayList;

/**
 * EDGE is the edge class in the graph. Its utility is to avoid the orientation problem, as we work in an undirected graph
 * To do this, it's gonna keep as attributes its two source and destination vertices vertices
 * @author Raphaël
 */
public class Edge 
{
	/* ========================= */
	/* Déclaration des attributs */
	/* ========================= */
	
    /**sommet de depart*/
    private Vertex vDep;
    
    /** sommet d'arrivée*/
    private Vertex vArr;

    /**l'unique generateur de l'arrete*/
    private AbstractGenerator generator;

    /**variable d'orientation directe du generateur (initialisée à true car lorsque l'arrete est construite pour le generateur, elle est cree dans son sens direct). modifié dans setGenerator*/
    private boolean genAtDep = true;

    /**le tableau des admittances débutant par vDep (dans le sens direct)*/
    public ArrayList<Admittance> directAdmittances;

    /**le tableau d'admittances terminant par vDep (dans le sens indirect)*/
    public ArrayList<Admittance> indirectAdmittances;
    
    /* ============================= */
    /* Déclaration des constructeurs */
    /* ============================= */
    
    /**
     * Constructeur de la classe Edge
     * @param vd : sommet de départ
     * @param va : sommet d'arrivée
     * @param gen : générateur de l'arrête
     * @param pAdmit : tableau des admittances débutant par vDep
     * @param nAdmit : tableau d'admittances terminant par vDep
     */
    public Edge(Vertex vd, Vertex va, AbstractGenerator gen, ArrayList<Admittance> pAdmit, ArrayList<Admittance> nAdmit) 
    {
        this.vDep = vd;
        this.vArr = va;
        this.generator = gen;
        this.directAdmittances = pAdmit;
        this.indirectAdmittances = nAdmit;
    }
    
    /**
     * Constructeur de la classe Edge
     * @param vd : sommet de départ
     * @param va : sommet d'arrivée
     * @param admit : admittance débutant par vd
     */
    public Edge(Vertex vd, Vertex va, Admittance admit) 
    {
        this.vDep = vd;
        this.vArr = va;
        this.generator = null;
        this.directAdmittances = new ArrayList<Admittance>();
        this.directAdmittances.add(admit);
        this.indirectAdmittances = new ArrayList<Admittance>();
    }

    /**
     * Constructeur de la classe Edge
     * @param vd : sommet de départ
     * @param va : sommet d'arrivée
     * @param gen : générateur de l'arrête
     */
    public Edge(Vertex vd, Vertex va, AbstractGenerator gen) 
    {
        this.vDep = vd;
        this.vArr = va;
        this.generator = gen;
        this.directAdmittances = new ArrayList<>();
        this.indirectAdmittances = new ArrayList<>();
    }
    
    /* ======================== */
    /* Déclaration des méthodes */
    /* ======================== */
    
    //getter Generator
    public AbstractGenerator generator() 
    {
        return this.generator;
    }

    //setter Generator
    public void setGenerator(Vertex originVertex, AbstractGenerator g) 
    {
        if (originVertex == this.vDep) this.generator = g;
        else if (originVertex == vArr) 
        {
            this.generator = g;
            this.genAtDep = false;
        }
    }

    /**Ajout d'admittances : determination du sens du composant et ajout de celui ci dans le tableau approprié*/
    public void addAdmittance(Vertex originVertex, Admittance a) 
    {
        if (originVertex == vDep) this.directAdmittances.add(a);
        else if (originVertex == vArr) this.indirectAdmittances.add(a);
        /////////////////////////////////faire une erreur, le cas else n'est pas normal.
    }

    /**retourne le nombre total d'admittances dans l'arrete*/
    public int AdmittancesNb() 
    {
        return this.directAdmittances.size() + this.indirectAdmittances.size();
    }

    /**Retourne les composants debutant par le sommet src*/
    public ArrayList<AbstractDipole> componentsFrom(Vertex src) 
    {
        ArrayList<AbstractDipole> a;
        if (src == vDep)//si src est le sommet de depart, on retourne les admittances dans le sens direct, et eventuellement le generateur
        {
            a = new ArrayList<>(directAdmittances);
            if ((genAtDep) && (generator != null)) 
            {
                a.add(generator);
            }
        } 
        else 
        {//sinon, les admittances dans le sens indirect, et eventuellement le generateur
            a = new ArrayList<>(indirectAdmittances);
            if ((!genAtDep) && (generator != null)) 
            {
                a.add(generator);
            }
        }
        return a;
    }

    /**Retourne les composants debutant par le sommet src*/
    public ArrayList<AbstractDipole> componentsTo(Vertex dst) 
    {
        ArrayList<AbstractDipole> a;
        if (dst == vArr) 
        {//si src est le sommet de depart, on retourne les admittances dans le sens direct, et eventuellement le generateur
            a = new ArrayList<>(directAdmittances);
            if ((genAtDep) && (generator != null)) 
            {
                a.add(generator);
            }
        } 
        else 
        {//sinon, les admittances dans le sens indirect, et eventuellement le generateur
            a = new ArrayList<>(indirectAdmittances);
            if ((!genAtDep) && (generator != null)) 
            {
                a.add(generator);
            }
        }
        return a;
    }

    /**retourne "L'arrete commence par v"*/
    public boolean beginsWith(Vertex v) 
    {
        return vDep == v;
    }

    /**retourne "L'arrete finit par v"*/
    public boolean endsWith(Vertex v) 
    {
        return vArr == v;
    }

    //getters des sommets de l'arrete
    public Vertex beginVertex() 
    {
        return vDep;
    }

    public Vertex endVertex() 
    {
        return vArr;
    }
}
