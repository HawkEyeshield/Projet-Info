package graphStructure;

import components.Admittance;
import components.AbstractDipole;
import components.Generator;

import java.util.ArrayList;

/**
 * @author Raphaël
 *
 * EDGE is the edge class in the graph. Its utility is to avoid the orientation problem, as we work in an undirected graph
 * To do this, it's gonna keep as attributes its two source and destination vertices vertices
 */
public class Edge 
{
    private Vertex vDep;
    private Vertex vArr;

    private Generator generator;
    private boolean genAtDep = true;

    public ArrayList<Admittance> directAdmittances;
    public ArrayList<Admittance> indirectAdmittances;

    public Edge(Vertex vd,Vertex va,Generator gen,ArrayList<Admittance> pAdmit,ArrayList<Admittance> nAdmit)
    {
        this.vDep = vd;
        this.vArr = va;
        this.generator = gen;
        this.directAdmittances = pAdmit;
        this.indirectAdmittances = nAdmit;
    }

    public Edge(Vertex vd,Vertex va,Admittance admit) 
    {
        this.vDep = vd;
        this.vArr = va;
        this.generator = null;
        this.directAdmittances = new ArrayList<Admittance>();
        this.directAdmittances.add(admit);
        this.indirectAdmittances = new ArrayList<Admittance>();
    }

    public Edge(Vertex vd,Vertex va,Generator gen) 
    {
        this.vDep = vd;
        this.vArr = va;
        this.generator = gen;
        this.directAdmittances = new ArrayList<>();
        this.indirectAdmittances = new ArrayList<>();
    }

    public Generator generator()
    {
        return this.generator;
    }

    public void setGenerator(Vertex originVertex,Generator g) 
    {
        if (originVertex == this.vDep) this.generator = g;
        else if (originVertex == vArr) {this.generator = g;this.genAtDep = false;}
    }

    public void addAdmittance(Vertex originVertex,Admittance a)
    {
        if (originVertex == vDep) this.directAdmittances.add(a);
        else if (originVertex == vArr) this.indirectAdmittances.add(a);
        /////////////////////////////////faire une erreur, le cas else n'est pas normal.
    }

    public int AdmittancesNb() 
    {
        return this.directAdmittances.size()+this.indirectAdmittances.size();
    }

    //The couple of functions that will return admittances depending on their orientation
    public ArrayList<AbstractDipole> componentsFrom(Vertex src) 
    {
        ArrayList<AbstractDipole> a;
        if (src == vDep)
        {
            a = new ArrayList<AbstractDipole>(directAdmittances);
            if ((genAtDep)&&(generator!=null))
            {
            	a.add(generator);
            }
        }
        else 
        {
            a = new ArrayList<AbstractDipole>(directAdmittances);
            if ((!genAtDep)&&(generator!=null))
            {
            	a.add(generator);
            }
        }
        return a;
    }

    public ArrayList<AbstractDipole> componentsTo(Vertex dst) 
    {
        ArrayList<AbstractDipole> a;
        if (dst == vArr)
        {
            a = new ArrayList<AbstractDipole>(directAdmittances);
            if ((genAtDep)&&(generator!=null))
            {
            	a.add(generator);
            }
        }
        else 
        {
            a = new ArrayList<AbstractDipole>(directAdmittances);
            if ((!genAtDep)&&(generator!=null))
            {
            	a.add(generator);
            }
        }
        return a;
    }

    public boolean beginsWith(Vertex v) 
    {
        if (vDep == v) return true;
        return false;
    }

    public boolean endsWith(Vertex v) 
    {
        if (vArr == v) return true;
        return false;
    }

    public Vertex beginVertex() 
    {
        return vDep;
    }

    public Vertex endVertex() 
    {
        return vArr;
    }

    //The couple o
}
