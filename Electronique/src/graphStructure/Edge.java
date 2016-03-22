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
public class Edge {
    //sommets de depart et d'arrivée
    private Vertex vDep;
    private Vertex vArr;

    //l'unique generateur de l'arrete
    private Generator generator;

    //variable d'orientation directe du generateur (initialisée à true car lorsque l'arrete est construite pour le generateur, elle est cree dans son sens direct). modifié dans setGenerator
    private boolean genAtDep = true;

    //le tableua des admittances débutant par vDep (dans le sens direct)
    public ArrayList<Admittance> directAdmittances;

    //le tableau d'admittances terminant par vDep (dans le sens indirect)
    public ArrayList<Admittance> indirectAdmittances;

    public Edge(Vertex vd, Vertex va, Generator gen, ArrayList<Admittance> pAdmit, ArrayList<Admittance> nAdmit) {
        this.vDep = vd;
        this.vArr = va;
        this.generator = gen;
        this.directAdmittances = pAdmit;
        this.indirectAdmittances = nAdmit;
    }

    public Edge(Vertex vd, Vertex va, Admittance admit) {
        this.vDep = vd;
        this.vArr = va;
        this.generator = null;
        this.directAdmittances = new ArrayList<Admittance>();
        this.directAdmittances.add(admit);
        this.indirectAdmittances = new ArrayList<Admittance>();
    }

    public Edge(Vertex vd, Vertex va, Generator gen) {
        this.vDep = vd;
        this.vArr = va;
        this.generator = gen;
        this.directAdmittances = new ArrayList<>();
        this.indirectAdmittances = new ArrayList<>();
    }

    //getter Generator
    public Generator generator() {
        return this.generator;
    }

    //setter Generator
    public void setGenerator(Vertex originVertex, Generator g) {
        if (originVertex == this.vDep) this.generator = g;
        else if (originVertex == vArr) {
            this.generator = g;
            this.genAtDep = false;
        }
    }

    //Ajout d'admittances : determination du sens du composant et ajout de celui ci dans le tableau approprié.
    public void addAdmittance(Vertex originVertex, Admittance a) {
        if (originVertex == vDep) this.directAdmittances.add(a);
        else if (originVertex == vArr) this.indirectAdmittances.add(a);
        /////////////////////////////////faire une erreur, le cas else n'est pas normal.
    }

    //retourne le nombre total d'admittances dans l'arrete.
    public int AdmittancesNb() {
        return this.directAdmittances.size() + this.indirectAdmittances.size();
    }

    //Retourne les composants debutant par le sommet src
    public ArrayList<AbstractDipole> componentsFrom(Vertex src) {
        ArrayList<AbstractDipole> a;
        if (src == vDep)//si src est le sommet de depart, on retourne les admittances dans le sens direct, et eventuellement le generateur
        {
            a = new ArrayList<>(directAdmittances);
            if ((genAtDep) && (generator != null)) {
                a.add(generator);
            }
        } else {//sinon, les admittances dans le sens indirect, et eventuellement le generateur
            a = new ArrayList<>(indirectAdmittances);
            if ((!genAtDep) && (generator != null)) {
                a.add(generator);
            }
        }
        return a;
    }

    //Retourne les composants debutant par le sommet src
    public ArrayList<AbstractDipole> componentsTo(Vertex dst) {
        ArrayList<AbstractDipole> a;
        if (dst == vArr) {//si src est le sommet de depart, on retourne les admittances dans le sens direct, et eventuellement le generateur
            a = new ArrayList<>(directAdmittances);
            if ((genAtDep) && (generator != null)) {
                a.add(generator);
            }
        } else {//sinon, les admittances dans le sens indirect, et eventuellement le generateur
            a = new ArrayList<>(indirectAdmittances);
            if ((!genAtDep) && (generator != null)) {
                a.add(generator);
            }
        }
        return a;
    }

    //retourne "L'arrete commence par v"
    public boolean beginsWith(Vertex v) {
        return vDep == v;
    }

    //retourne "L'arrete finit par v"
    public boolean endsWith(Vertex v) {
        return vArr == v;
    }

    //getters des sommets de l'arrete
    public Vertex beginVertex() {
        return vDep;
    }

    public Vertex endVertex() {
        return vArr;
    }
}
