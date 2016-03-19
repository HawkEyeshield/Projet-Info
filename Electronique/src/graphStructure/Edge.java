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
    private Vertex v_dep;
    private Vertex v_arr;

    private Generator generator;
    private boolean gen_at_dep = true;

    public ArrayList<Admittance> directAdmittances;
    public ArrayList<Admittance> indirectAdmittances;

    public Edge(Vertex vd,Vertex va,Generator gen,ArrayList<Admittance> p_admit,ArrayList<Admittance> n_admit) 
    {
        this.v_dep = vd;
        this.v_arr = va;
        this.generator = gen;
        this.directAdmittances = p_admit;
        this.indirectAdmittances = n_admit;
    }

    public Edge(Vertex vd,Vertex va,Admittance admit) 
    {
        this.v_dep = vd;
        this.v_arr = va;
        this.generator = null;
        this.directAdmittances = new ArrayList<Admittance>();
        this.directAdmittances.add(admit);
        this.indirectAdmittances = new ArrayList<Admittance>();
    }

    public Edge(Vertex vd,Vertex va,Generator gen) 
    {
        this.v_dep = vd;
        this.v_arr = va;
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
        if (originVertex == this.v_dep) this.generator = g;
        else if (originVertex == v_arr) {this.generator = g;this.gen_at_dep = false;}
    }

    public void addAdmittance(Vertex origin_vertex,Admittance a) 
    {
        if (origin_vertex == v_dep) this.directAdmittances.add(a);
        else if (origin_vertex == v_arr) this.indirectAdmittances.add(a);
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
        if (src == v_dep) 
        {
            a = new ArrayList<AbstractDipole>(directAdmittances);
            if ((gen_at_dep)&&(generator!=null)) 
            {
            	a.add(generator);
            }
        }
        else 
        {
            a = new ArrayList<AbstractDipole>(directAdmittances);
            if ((!gen_at_dep)&&(generator!=null)) 
            {
            	a.add(generator);
            }
        }
        return a;
    }

    public ArrayList<AbstractDipole> componentsTo(Vertex dst) 
    {
        ArrayList<AbstractDipole> a;
        if (dst == v_arr) 
        {
            a = new ArrayList<AbstractDipole>(directAdmittances);
            if ((gen_at_dep)&&(generator!=null)) 
            {
            	a.add(generator);
            }
        }
        else 
        {
            a = new ArrayList<AbstractDipole>(directAdmittances);
            if ((!gen_at_dep)&&(generator!=null)) 
            {
            	a.add(generator);
            }
        }
        return a;
    }

    public boolean beginsWith(Vertex v) 
    {
        if (v_dep == v) return true;
        return false;
    }

    public boolean endsWith(Vertex v) 
    {
        if (v_arr == v) return true;
        return false;
    }

    public Vertex beginVertex() 
    {
        return v_dep;
    }

    public Vertex endVertex() 
    {
        return v_arr;
    }

    //The couple o
}
