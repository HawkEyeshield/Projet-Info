package Resolution;

import Components.*;
import GraphStructure.CircuitGraph;
import GraphStructure.Vertex;
import GraphStructure.map;
import org.jgrapht.graph.Multigraph;

import java.util.ArrayList;

/**
 * @author Briztou
 */
public class Extracteur {

    public CircuitGraph graph;

    public Extracteur(CircuitGraph g) {
        graph = g;
        extraction(false);
    }

    public void log(String s) {
        System.out.println(s);
    }

    private boolean extraction(boolean resetting) {
        //In this part we will assume that there ca be no parallel admittances aka there is at most an unique admitance between two vertices.7
        //A future version will allow that case, in introducing more variables in the resolution algorythm, but for instance, I am limited to this.

        Vertex[] vertices = graph.get_all_vertices();
        int len = vertices.length;

        if (!resetting) {
            log("Resetting is disabled, I will not modify vertices numbers.");
        }
        else {
            log("Setting the vertices numbers...");
            //Step 0 :setting vertices : giving to each a number from 0 to [nb_vertices]
            for (int i = 0; i < len; i++) {
                vertices[i].set(i);
            }
            log("Vertices set\n");
        }

        //Step 1 : checking that there are no parallel admittances
        log("Checking the components...");
        for (int i=0;i<len;i++) {
            for (int j=0;j<i;j++) {
                if (graph.get_all_admittances(vertices[i],vertices[j]).size() >1) {
                    log("There are two components connected to the same two vertices, I can't solve this problemfor instance.");
                    return false;
                }
            }
        }

        log("Looks like everything is allright\n");

        //Step 1 : get all generators
        ArrayList<Generator> generateurs = graph.get_all_generators();
        int l = generateurs.size();
        log("Found "+l+" generators.\n");

        //Step 2 : for each generator, calculate the circuit parameters

        char[] st = new char[]{'U', 'I', 'Y'};

        for (int i = 0;i<l;i++) {
            log("Generator "+i+" : ");
            //Step 3 : turning on each generator and disabling all others
            for (int j = 0;j<l;j++) {
                if (j==i) generateurs.get(j).turn_on();
                else generateurs.get(j).turn_off();
            }

            ArrayList<Equation> equations = new ArrayList<>();

            int signe;//The coefficient variable
            double[][] cour;//The current matrix for equation
            double cur_gen; //The generator current coefficient

            //Step 4 : Get all nodes equations
            for (int k=0;k<len;k++) {
                log("Vertex "+vertices[k].get());
                map m;
                cur_gen = 0;
                cour = new double[len][len];

                ArrayList<map> connections = graph.getConnectedComponents(vertices[k]);//Getting all components connected to this vertex
                for(int c = 0;c<connections.size();c++) {//for each component connected
                    m = connections.get(c);//for code comprehensibility
                    //Setting the signe of the coefficient, because components are oriented, as we set their current and voltage.
                    if (m.incoming()) signe = 1;
                    else signe  =-1;

                    //TODO : FAIRE LE GENERATEUR DE COURANT DANS LE RESOLVEUR
                    switch(m.component().type()) {
                        case ADMITTANCE:
                            cour[vertices[k].get()][m.Vertex().get()] = 1;
                            //TODO faire le cas ou le courant est fixé.
                            break;
                        case CURRENT_GENERATOR:
                            break;
                        case VOLTAGE_GENERATOR:
                            Generator gen = (Generator) m.component();
                            if (gen.is_active()) {
                                //if cur_gen has already been modified, we know that almost two generators are active : ERROR
                                if (cur_gen != 0) {
                                    log("failed to turn off all generators : two are still active");
                                    return false;
                                } else cur_gen = signe;
                            }
                            break;
                    }
                }
                Equation eq = new Equation(new double[len][len],cour,new double[len][len],0,st,cur_gen);
                log(eq.toString());
                equations.add(eq);
            }
        }
        return true;
    }

}
