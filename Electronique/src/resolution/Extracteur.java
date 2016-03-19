package resolution;

import components.Admittance;
import components.Generator;
import graphStructure.CircuitGraph;
import graphStructure.Edge;
import graphStructure.Vertex;
import graphStructure.ComponentMap;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * @author Raphaël
 */
public class Extracteur {

    public CircuitGraph graph;
    public int nbNodes;
    public int nbGenerators;

    public double[] resultCurrents;
    public double[][][] resultVariables;
    public double[][][][] varFix;

    public char[] st;

    public Extracteur(CircuitGraph g) {
        graph = g;
        extraction(false);
    }

    public void logn(Object s) {
        System.out.println(s);
    }

    public void log(Object s) {
        log(s);log("\n");
    }

    private boolean extraction(boolean resetting) {
        //In this part we will assume that there ca be no parallel admittances aka there is at most an unique admitance between two vertices.7
        //A future version will allow that case, in introducing more variables in the resolution algorythm, but for instance, I am limited to this.

        Vertex[] vertices = graph.getAllVertices();
       nbNodes = vertices.length;

        if (!resetting) {
            logn("Le reparametrage desactive, je ne toucherai pas aux sommets.\n");
        } else {
            logn("Parametrage des sommets...");
            //Step 0 :setting vertices : giving to each a number from 0 to [nb_vertices]
            for (int i = 0; i < nbNodes; i++) {
                vertices[i].set(i);
            }
            logn("\t Fait\n");
        }

        //Etape 0 : verification qu'il n'y a pas d'admittances paralleles
        logn("Verification des composants paralleles...");
        for (int i = 0; i < nbNodes; i++) {
            for (int j = 0; j < i; j++) {
                if (graph.multipleAdmittances(vertices[i], vertices[j])) {
                    logn("Deux Admittances sont connectees en parallele, je ne peux résoudre ce systeme pour l'instant.");
                    return false;
                }
            }
        }
        logn("Fait\n");

        logn("Tout a l'air correct.\n\nRecherche des generateurs ... ");

        //Step 1 : recuperation des generateurs
        ArrayList<Generator> generateurs = graph.get_all_generators();
        nbGenerators = generateurs.size();
        switch (nbGenerators) {
            case 0:logn("Vous me prenez pour un idiot? Il n'y a pas de generateur. Arret...\n");break;
            case 1:logn("1 generateur trouvé.\n");break;
            default:logn("Disjonction des cas pour des "+ nbGenerators + " generateur(s).\n");
        }


        //Variables for the extraction :
        st = new char[]{'I', 'U', 'Y'};

        //Variables de retour, pour stocker les resultats de la resolution
        //le solveur
        Solveur solveur;

        //initialisation des tableaux de resultats :
        resultCurrents = new double[nbGenerators];
        resultVariables = new double[3][nbNodes][nbNodes];

        //Tableaux de variables fixées
        varFix = new double[3][nbNodes][nbNodes][2];
        int s;
        //Recuperation des variables fixes
        Vertex dep,arr;
        for (Edge e:graph.getAllEdges()) {
            dep = e.beginVertex();arr = e.endVertex();
            for (Admittance a:e.directAdmittances) {
                double[][] det = a.getParameters();
                for (int d = 0; d < 3; d++) {
                    s=1;
                    if (det[d][0] == 1) {
                        varFix[d][dep.get()][arr.get()][0] = 1;
                        varFix[d][arr.get()][dep.get()][0] = 1;

                        varFix[d][dep.get()][arr.get()][1] = s*det[d][1];
                        if (d==2) s=-1;//on inverse l'inversion...
                        varFix[d][arr.get()][dep.get()][1] = -s*det[d][1];
                    }
                }
            }
            for (Admittance a:e.indirectAdmittances) {
                double[][] det = a.getParameters();
                for (int d = 0; d < 3; d++) {
                    s=-1;
                    if (det[d][0] == 1) {
                        varFix[d][dep.get()][arr.get()][0] = 1;
                        varFix[d][arr.get()][dep.get()][0] = 1;

                        varFix[d][arr.get()][dep.get()][1] = s*det[d][1];
                        if (d==2) s=1;//on inverse l'inversion
                        varFix[d][dep.get()][arr.get()][1] = -s*det[d][1];


                    }
                }
            }
        }

        //rentrée des valeurs dans resultVariables
        for (int i=0;i<3;i++) {
            for (int j=0;j<nbNodes;j++) {
                for (int k=0;k<nbNodes;k++) {
                    if (varFix[i][j][k][0] == 1)
                        resultVariables[i][j][k] = varFix[i][j][k][1];
                }
            }
        }

        //Variables temporaires, pour la boucle de résolution

        ArrayList<Equation> equations;//La liste des equations;
        int signe;//La variable de coefficients
        double[][] cour;//La matrice de courants pour les equations
        double coeffCurGen; //Le coefficient du courant générateur
        boolean curGenDet;
        double curGenValue;

        //the variables matrices
        double[][][] voltages;
        double[][][] admittances;
        double[][][] currents;
        double[][][][] vartab;
        boolean generatorFound;

        //Etape 2 : calcul des parametres du circuit pour chaque générateur.

        for (int i = 0; i < nbGenerators; i++) {
            logn(" - Generateur " + i + " : ");
            //Step 3 : extinction de tous les générateurs et allumage du concerné.
            for (int j = 0; j < nbGenerators; j++) {
                if (j == i) generateurs.get(j).turnOn();
                else generateurs.get(j).turnOff();
            }

            equations = new ArrayList<>();
            voltages = new double[nbNodes][nbNodes][2];//pas besoin d'initialiser, car toutes les tensions sont connues.
            for (double[][] x:voltages) {
                for (double[] y:x) y[0] = 0;
            }

            currents = new double[nbNodes][nbNodes][2];//There we need to initialise, as current do not exist without a dipole
            for (double[][] x:currents) {
                for (double[] y:x) y[0] = 1;
            }

            admittances = new double[nbNodes][nbNodes][2];
            for (double[][] x:admittances) {
                for (double[] y:x) y[0] = 1;
            }

            vartab = new double[][][][]{currents, voltages, admittances};

            for (int tp=0;tp<nbNodes;tp++) {
                for (int dd=0;dd<3;dd++) {
                    vartab[dd][tp][tp] = new double[]{1,0};
                }
            }

            curGenValue=0;
            curGenDet = false;
            //Etape 4 : récupération des equations aux noeuds
            
            logn("Equation aux noeuds ...");
            for (Vertex vertice : vertices) {
                ComponentMap map;
                coeffCurGen = 0;
                generatorFound = false;
                cour = new double[nbNodes][nbNodes];

                ArrayList<ComponentMap> connections = graph.getConnectedComponents(vertice);//Getting all components connected to this vertex
                for (ComponentMap m : connections) {//for each component connected
                    //Setting the signe of the coefficient, because components are oriented, as we set their current and voltage.
                    if (m.incoming()) signe = 1;
                    else signe = -1;
                    Generator gen;
                    switch (m.component().type()) {
                        //writing coefficients in equation arrays;
                        //TODO Faire la verif des valeurs
                        case ADMITTANCE:
                            //there is an admittance between the two vertices => a current (non generator-generated) can exist and an admittance too.
                            currents[vertice.get()][m.vertex().get()][0] = 0;
                            admittances[vertice.get()][m.vertex().get()][0] = 0;
                            cour[vertice.get()][m.vertex().get()] = 1;
                            break;
                        //Cette partie ne REMPLACE PAS LES VALEURS, ce travail est laissé au solveur.


                        //Todo : verifier si les valeurs des generateurs sont bien remplac�es dans cette partie ET dans la partie recup des valeurs, et si c'est le cas, simplifier.
                        case VOLTAGE_GENERATOR://A Voltage generator determines the voltage between the points;
                            gen = (Generator) m.component();
                            //TODO modifier cette partie apres la reorganisation des graphes : si on detecte deux generateurs erreur.
                            voltages[vertice.get()][m.vertex().get()][0] = 1;
                            if (gen.is_active()) {
                                voltages[vertice.get()][m.vertex().get()][1] = signe*gen.getVoltage();//If the generator is not active, the voltage will be set to 0
                                if (!gen.is_active()) {//si le generateur n'est pas actif, AUCUN COURANT NE LE TRAVERSE
                                    currents[vertice.get()][m.vertex().get()][0] = 1;
                                    currents[vertice.get()][m.vertex().get()][1] = 0;

                                }
                                //if coeffCurGen has already been modified, we know that almost two generators are active : ERROR
                                if (generatorFound) {
                                    logn("Echec d'extinction des generateurs, arret du programmme.");
                                    return false;
                                } else coeffCurGen = signe;
                                generatorFound = true;//a generator has been found

                            }
                            break;
                        case CURRENT_GENERATOR://A current Generator determines the current through a component
                            gen = (Generator) m.component();
                            currents[vertice.get()][m.vertex().get()][0] = 0;
                            if (gen.is_active()) {
                                currents[vertice.get()][m.vertex().get()][1] = signe*gen.getCurrent();//If the generator is not active, the current will be set to 0
                                //if coeffCurGen has already been modified, we know that almost two generators are active : ERROR
                                if (generatorFound) {
                                    logn("Echec d'extinction des generateurs, arret du programmme.");
                                    return false;
                                }
                                else {
                                    //TODO Rediger les conventions sur les generateurs (sens du courant et de la tension avec un dipole a->b
                                    coeffCurGen = signe;//TODO verifier si la constante est necessaire, et si on ne peut pas s'en passer, genre si elle n'est pas saisie dans la partie suivante
                                    curGenDet = true;
                                    curGenValue=signe*gen.getCurrent();
                                }
                                generatorFound = true;//a generator has been found
                            }
                            break;
                    }

                    //writing values in variables arrays, so that they can be replaced in the solver.
                    double[][] det = m.component().getParameters();
                    for (int d = 0; d < 3; d++) {
                        if (det[d][0] == 1) {
                            vartab[d][vertice.get()][m.vertex().get()][0] = 1;
                            if (d==2) signe=1;
                            vartab[d][vertice.get()][m.vertex().get()][1] = signe*det[d][1];
                        }
                    }
                    //Todo tester si le signe est correctement mis dans les valeurs (aka l'orientation est bien faite)

                    //TODO tester si les générateurs eteints imposent bien 0V à leurs bornes.
                }
                Equation eq = new Equation(new double[nbNodes][nbNodes], cour, new double[nbNodes][nbNodes], 0, st, coeffCurGen);
                equations.add(eq);
            }
            logn("Fait. \n\nEquations aux mailles ...");
            //Lois des mailles :
            double[][] volt;
            for (int p=0;p<nbNodes-2;p++) {//Premiers point des cycle;
                for (int f=p+2;f<nbNodes;f++) {//points de fin des mailles
                    volt = new double[nbNodes][nbNodes];
                    for (int r=0;r<=f-p;r++) {
                        volt[r+p][p+(r+1)%(f-p+1)] = 1;
                    }
                    Equation eq = new Equation(volt,new double[nbNodes][nbNodes], new double[nbNodes][nbNodes], 0, st,0);
                    equations.add(eq);
                }

            }

            logn("Fait.\n\nResolution....");
            for(Equation eq:equations) System.out.println(eq);
            logn("");
            double[] cg;
            if (curGenDet) cg = new double[]{1, curGenValue};
            else cg = new double[]{0, 0};
            Equation[] eq = equations.toArray(new Equation[equations.size()]);
            solveur = new Solveur(voltages, currents, admittances, cg, eq);
            if (solveur.resolution()) {
                resultCurrents[i] = solveur.currGenerator();

                //ajout du tableau actuel aux reusltats
                logn("Circuit résolu. Ajout du résultat au circuit global ... \n");
                ajout(solveur.variables());
            }
            else {
                logn("Systeme inrésolvable. Arret...");
            }
        }

        System.out.println("Systeme resolu.");
        return true;
    }

    //fonction de somme de tableaux : ajoute AU PREMIER TABLEAU le second (ie la somme est faite sur le premier, le second est juste lu.
    private void ajout(double[][][] b) {
        for (int i=0;i<3;i++) {
            for (int j=0;j<nbNodes;j++) {
                for (int k=0;k<nbNodes;k++) {
                    if (varFix[i][j][k][0] == 0)
                        resultVariables[i][j][k] = resultVariables[i][j][k] + b[i][j][k];
                }
            }
        }

    }


    public void printVariables()
    {
        NumberFormat nf = new DecimalFormat("0.00###");
        for(int i_t=0;i_t<3;i_t++) {
            System.out.print("\n"+st[i_t]+"\n");
            for (int i = 0;i< nbNodes;i++) {
                for (int j = 0;j< nbNodes;j++) {
                    System.out.print(" "+ nf.format(resultVariables[i_t][i][j]) + " ");
                }
                System.out.println("");
            }
        }
        System.out.println("");
        System.out.println("Courants Generateurs :");
        for (int i=0;i<nbGenerators;i++) {
            System.out.println(i+" : "+ resultCurrents[i]);
        }

    }


}
