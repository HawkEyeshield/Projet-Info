package Resolution;

import Components.*;
import GraphStructure.CircuitGraph;
import GraphStructure.Vertex;
import GraphStructure.componentMap;

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
        } else {
            log("Setting the vertices numbers...");
            //Step 0 :setting vertices : giving to each a number from 0 to [nb_vertices]
            for (int i = 0; i < len; i++) {
                vertices[i].set(i);
            }
            log("Vertices set\n");
        }

        //Step 1 : checking that there are no parallel admittances
        log("Checking the components...");
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < i; j++) {
                if (graph.multipleAdmittances(vertices[i], vertices[j])) {
                    log("There are two components connected to the same two vertices, I can't solve this problem for instance.");
                    return false;
                }
            }
        }

        log("Looks like everything is allright\n");

        //Step 1 : get all generators
        ArrayList<Generator> generateurs = graph.get_all_generators();
        int l = generateurs.size();
        log("Found " + l + " generators.\n");

        //Variables for the extraction :
        char[] st = new char[]{'I', 'U', 'Y'};

        ArrayList<Equation> equations;//The equation list;
        int signe;//The coefficient variable
        double[][] cour;//The current matrix for equation
        double coeff_cur_gen; //The generator current coefficient
        boolean cur_gen_det;
        double cur_gen_value;

        //the variables matrices
        double[][][] voltages;
        double[][][] admittances;
        double[][][] currents;
        double[][][][] vartab;
        boolean generator_found;

        //Step 2 : for each generator, calculate the circuit parameters

        //Generate the variables a
        for (int i = 0; i < l; i++) {
            log("Generator " + i + " : ");
            //Step 3 : turning on each generator and disabling all others
            for (int j = 0; j < l; j++) {
                if (j == i) generateurs.get(j).turn_on();
                else generateurs.get(j).turn_off();
            }

            equations = new ArrayList<>();
            voltages = new double[len][len][2];//pas besoin d'initialiser, car toutes les tensions sont connues.
            for (double[][] x:voltages) {
                for (double[] y:x) y[0] = 0;
            }

            currents = new double[len][len][2];//There we need to initialise, as current do not exist without a dipole
            for (double[][] x:currents) {
                for (double[] y:x) y[0] = 1;
            }

            admittances = new double[len][len][2];
            for (double[][] x:admittances) {
                for (double[] y:x) y[0] = 1;
            }

            vartab = new double[][][][]{currents, voltages, admittances};

            for (int tp=0;tp<len;tp++) {
                for (int dd=0;dd<3;dd++) {
                    vartab[dd][tp][tp] = new double[]{1,0};
                }
            }

            cur_gen_value=0;
            cur_gen_det = false;
//TODO reorganiser le graphe lorsque l'on coupe les generteurs : creer un nouveau graphe a chaque fois car des aretes disparaissent et des noeuds sont fusionnés.
            //Step 4 : Get all nodes equations
            for (Vertex vertice : vertices) {
                log("Vertex " + vertice.get());
                componentMap map;
                coeff_cur_gen = 0;
                generator_found = false;
                cour = new double[len][len];

                ArrayList<componentMap> connections = graph.getConnectedComponents(vertice);//Getting all components connected to this vertex
                for (componentMap m : connections) {//for each component connected
                    //Setting the signe of the coefficient, because components are oriented, as we set their current and voltage.
                    if (m.incoming()) signe = 1;
                    else signe = -1;
                    //TODO : FAIRE LE GENERATEUR DE COURANT DANS LE RESOLVEUR
                    Generator gen;
                    switch (m.component().type()) {
                        //writing coefficients in equation arrays;
                        //TODO Faire la verif des valeurs
                        case ADMITTANCE:
                            //there is an admittance between the two vertices => a current (non generator-generated) can exist and an admittance too.
                            currents[vertice.get()][m.Vertex().get()][0] = 0;
                            admittances[vertice.get()][m.Vertex().get()][0] = 0;
                            cour[vertice.get()][m.Vertex().get()] = 1;
                            break;
                        //This part doesnt replace values, this job is for the solver.


                        //Todo : verifier si les valeurs des generateurs sont bien remplacées dans cette partie ET dans la partie recup des valeurs, et si c'est le cas, simplifier.
                        case VOLTAGE_GENERATOR://A Voltage generator determines the voltage between the points;
                            gen = (Generator) m.component();
                            //TODO modifier cette partie apres la reorganisation des graphes : si on detecte deux generateurs erreur.
                            voltages[vertice.get()][m.Vertex().get()][0] = 1;
                            if (gen.is_active()) {
                                voltages[vertice.get()][m.Vertex().get()][1] = signe*gen.getVoltage();//If the generator is not active, the voltage will be set to 0
                                //if coeff_cur_gen has already been modified, we know that almost two generators are active : ERROR
                                if (generator_found) {
                                    log("failed to turn off all generators : two are still active");
                                    return false;
                                } else coeff_cur_gen = signe;
                                generator_found = true;//a generator has been found

                            }
                            break;
                        case CURRENT_GENERATOR://A current Generator determines the current through a component
                            System.out.println("\n\n\nBIPBIP\n\n\n");
                            gen = (Generator) m.component();
                            currents[vertice.get()][m.Vertex().get()][0] = 0;
                            if (gen.is_active()) {
                                currents[vertice.get()][m.Vertex().get()][1] = signe*gen.getCurrent();//If the generator is not active, the current will be set to 0
                                //if coeff_cur_gen has already been modified, we know that almost two generators are active : ERROR
                                if (generator_found) {
                                    log("failed to turn off all generators : two are still active");
                                    return false;
                                }
                                else {
                                    coeff_cur_gen = signe;//TODO verifier si la constante est necessaire, et si on ne peut pas s'en passer, genre si elle n'est pas saisie dans la partie suivante
                                    cur_gen_det = true;
                                    cur_gen_value=signe*gen.getCurrent();
                                }
                                generator_found = true;//a generator has been found
                            }
                            break;
                    }

                    //writing values in variables arrays, so that they can be replaced in the solver.
                    double[][] det = m.component().getParameters();
                    for (int d = 0; d < 3; d++) {
                        if (det[d][0] == 1) {
                            vartab[d][vertice.get()][m.Vertex().get()][0] = 1;
                            vartab[d][vertice.get()][m.Vertex().get()][1] = signe*det[d][1];
                        }
                    }
                    //Todo tester si le signe est correctement mis dans les valeurs (aka l'orientation est bien faite)
                }
                Equation eq = new Equation(new double[len][len], cour, new double[len][len], 0, st, coeff_cur_gen);
                log(eq.toString());
                equations.add(eq);
            }
            double[][] volt;
            //Lois des mailles :
            for (int p=0;p<len-2;p++) {//Premiers point des cycle;
                for (int f=p+2;f<len;f++) {//points de fin des mailles
                    volt = new double[len][len];
                    for (int r=0;r<=f-p;r++) {
                        System.out.print((r+p)+":");
                        System.out.print((p+(r+1)%(f-p+1))+" ");
                        volt[r+p][p+(r+1)%(f-p+1)] = 1;
                    }
                    System.out.println();
                    Equation eq = new Equation(volt,new double[len][len], new double[len][len], 0, st,0);

                    equations.add(eq);
                }

            }
            for(Equation eq:equations) System.out.println(eq);
            double[] cg;
            if (cur_gen_det) cg = new double[]{1, cur_gen_value};
            else cg = new double[]{0, 0};
            Equation[] eq = equations.toArray(new Equation[equations.size()]);
            new Solveur(voltages, currents, admittances, cg, eq);
        }
        return true;

    }

}
