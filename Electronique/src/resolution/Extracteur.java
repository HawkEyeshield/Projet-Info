package resolution;

import components.Admittance;
import components.Generator;
import components.Type;
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

/* ========================= */
/* Déclaration des attributs */
/* ========================= */
	
    private CircuitGraph graph;
    private int nbNodes;
    private int nbGenerators;

    private double[] resultCurrents;
    private double[][][] resultVariables;
    private double[][][][] varFix;

    private char[] st;

    private boolean solved;

    private int[][] genVertices;

/* =========================== */
/* Déclaration du constructeur */
/* =========================== */
    
    public Extracteur(CircuitGraph g) 
    {
        graph = g;
        solved = false;
    }

/* ======================== */
/* Déclaration des méthodes */
/* ======================== */
    
    private void logn(Object s) 
    {
        System.out.println(s);
    }

    private void log(Object s) 
    {
        log(s);log("\n");
    }

    public boolean extraction(boolean resetting) 
    {
        //In this part we will assume that there ca be no parallel admittances aka there is at most an unique admitance between two vertices.7
        //A future version will allow that case, in introducing more variables in the resolution algorythm, but for instance, I am limited to this.

        Vertex[] vertices = graph.getAllVertices();
        nbNodes = vertices.length;

        if (!resetting) 
        {
            logn("Le reparametrage desactive, je ne toucherai pas aux sommets.\n");
        } else {
            logn("Parametrage des sommets...");
            //Step 0 :setting vertices : giving to each a number from 0 to [nb_vertices]
            for (int i = 0; i < nbNodes; i++) 
            {
                vertices[i].set(i);
            }
            logn("\t Fait\n");
        }

        //Etape 0 : verification qu'il n'y a pas d'admittances paralleles
        logn("Verification des composants paralleles...");
        for (int i = 0; i < nbNodes; i++) 
        {
            for (int j = 0; j < i; j++) 
            {
                if (graph.multipleAdmittances(vertices[i], vertices[j])) 
                {
                    logn("Deux Admittances sont connectees en parallele, je ne peux résoudre ce systeme pour l'instant.");
                    return false;
                }
            }
        }
        logn("Fait\n");

        logn("Tout a l'air correct.\n\nRecherche des generateurs ... ");

        //Step 1 : recuperation des generateurs
        ArrayList<Generator> generateurs = graph.getAllGenerators();
        nbGenerators = generateurs.size();
        switch (nbGenerators) 
        {
            case 0:
                logn("Vous me prenez pour un idiot? Il n'y a pas de generateur. Arret...\n");
                break;
            case 1:
                logn("1 generateur trouvé.\n");
                break;
            default:
                logn("Disjonction des cas pour des " + nbGenerators + " generateur(s).\n");
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
        Vertex dep, arr;
        for (Edge e : graph.getAllEdges()) 
        {
            dep = e.beginVertex();
            arr = e.endVertex();
            for (Admittance a : e.directAdmittances) 
            {
                double[][] det = a.getParameters();
                for (int d = 0; d < 3; d++) 
                {
                    s = 1;
                    if (det[d][0] == 1) {
                        varFix[d][dep.get()][arr.get()][0] = 1;
                        varFix[d][arr.get()][dep.get()][0] = 1;

                        varFix[d][dep.get()][arr.get()][1] = s * det[d][1];
                        if (d == 2) s = -1;//on inverse l'inversion...
                        varFix[d][arr.get()][dep.get()][1] = -s * det[d][1];
                    }
                }
            }
            for (Admittance a : e.indirectAdmittances) 
            {
                double[][] det = a.getParameters();
                for (int d = 0; d < 3; d++) 
                {
                    s = -1;
                    if (det[d][0] == 1) {
                        varFix[d][dep.get()][arr.get()][0] = 1;
                        varFix[d][arr.get()][dep.get()][0] = 1;

                        varFix[d][arr.get()][dep.get()][1] = s * det[d][1];
                        if (d == 2) s = 1;//on inverse l'inversion
                        varFix[d][dep.get()][arr.get()][1] = -s * det[d][1];


                    }
                }
            }
        }

        //rentrée des valeurs dans resultVariables
        for (int i = 0; i < 3; i++) 
        {
            for (int j = 0; j < nbNodes; j++) 
            {
                for (int k = 0; k < nbNodes; k++) 
                {
                    if (varFix[i][j][k][0] == 1)
                        resultVariables[i][j][k] = varFix[i][j][k][1];
                }
            }
        }

        //Variables temporaires, pour la boucle de résolution

        ArrayList<Equation> equations;//La liste des equations;
        int signe;//La variable de coefficients
        double[][] eqCurrents;//La matrice de courants pour les equations
        double[][] powerCurrents;
        ArrayList<Generator> genOrder;


        //the variables matrices
        double[][][] voltages;
        double[][][] admittances;
        double[][][] currents;
        double[][][][] vartab;
        genVertices = new int[nbGenerators][2];

        double[] equationCurrents;
        int pci;
        int pg;

        //Etape 2 : calcul des parametres du circuit pour chaque générateur.

        logn("Activation des Generateur");
        //Step 3 : extinction de tous les générateurs et allumage du concerné.
        for (Generator gen : generateurs) gen.turnOn();

        equations = new ArrayList<>();
        voltages = new double[nbNodes][nbNodes][2];//pas besoin d'initialiser, car les tensions existent sans dipole.
        for (double[][] x : voltages) 
        {
            for (double[] y : x) y[0] = 0;
        }

        currents = new double[nbNodes][nbNodes][2];//There we need to initialise, as current do not exist without a dipole
        for (double[][] x : currents) 
        {
            for (double[] y : x) y[0] = 1;
        }

        admittances = new double[nbNodes][nbNodes][2];
        for (double[][] x : admittances) {
            for (double[] y : x) y[0] = 1;
        }

        vartab = new double[][][][]{currents, voltages, admittances};

        for (int tp = 0; tp < nbNodes; tp++) 
        {
            for (int dd = 0; dd < 3; dd++) {
                vartab[dd][tp][tp] = new double[]{1, 0};
            }
        }

        powerCurrents = new double[nbGenerators][2];
        genOrder = new ArrayList<>();
        pci = 0;

        //Etape 4 : récupération des equations aux noeuds

        logn("Equation aux noeuds ...");
        for (Vertex vertice : vertices) 
        {
            eqCurrents = new double[nbNodes][nbNodes];
            equationCurrents = new double[nbGenerators];
            System.out.println("new Vertex");
            ArrayList<ComponentMap> connections = graph.getConnectedComponents(vertice);//Getting all components connected to this vertex

            for (ComponentMap m : connections) 
            {//for each component connected
                System.out.println(m.vertex().get()+" "+m.component().hashCode()+" "+m.incoming());
                //Setting the signe of the coefficient, because components are oriented, as we set their current and voltage.
                if (m.incoming()) signe = 1;
                else signe = -1;
                Generator gen = null;
                Type type = m.component().type();

                //Ajout du generateur dans les tableaux
                if ((type == Type.VOLTAGEGENERATOR)||(type == Type.CURRENTGENERATOR)) 
                {
                    gen = (Generator) m.component();
                    pg = genOrder.indexOf(gen);//on regarde si on a pas déja le générateur
                    if (pg == -1) 
                    {
                        if ((type== Type.CURRENTGENERATOR)&&(gen.isActive())) powerCurrents[pci] = new double[]{1, signe * gen.getCurrent()};
                        genOrder.add(gen);
                        if (m.incoming()) genVertices[pci] = new int[]{vertice.get(),m.vertex().get()};
                        else genVertices[pci] = new int[]{m.vertex().get(),vertice.get()};
                        pg = pci;
                        //si le generateur est actif, on remplace sa valeur dans le tableau des courants.
                        pci++;
                    }
                    equationCurrents[pg] = signe;

                }
                switch (m.component().type()) 
                {

                    //writing coefficients in equation arrays;
                    //TODO Faire la verif des valeurs
                    case ADMITTANCE:
                        //there is an admittance between the two vertices => a current (non generator-generated) can exist and an admittance too.
                        currents[vertice.get()][m.vertex().get()][0] = 0;
                        admittances[vertice.get()][m.vertex().get()][0] = 0;
                        eqCurrents[vertice.get()][m.vertex().get()] = 1;
                        break;
                    //Cette partie ne REMPLACE PAS LES VALEURS, ce travail est laissé au solveur.


                    //Todo : verifier si les valeurs des generateurs sont bien remplac�es dans cette partie ET dans la partie recup des valeurs, et si c'est le cas, simplifier.
                    case VOLTAGEGENERATOR://A Voltage generator determines the voltage between the points;
                        voltages[vertice.get()][m.vertex().get()][0] = 1;
                        if (gen.isActive()) 
                        {//pas d'erreur, en effet, on a traité le cas du generateur plus haut et on a initialisé gen à ce moment la.
                            voltages[vertice.get()][m.vertex().get()][1] = signe * gen.getVoltage();//If the generator is not active, the voltage will be set to 0
                        }
                        break;

                    case CURRENTGENERATOR://A current Generator determines the current through a component
                        currents[vertice.get()][m.vertex().get()][0] = 0;
                        if (gen.isActive()) 
                        {//pas d'erreur, en effet, on a traité le cas du generateur plus haut et on a initialisé gen à ce moment la.
                            currents[vertice.get()][m.vertex().get()][1] = signe * gen.getCurrent();//If the generator is not active, the current will be set to 0
                        }
                        break;
                }

                //writing values in variables arrays, so that they can be replaced in the solver.
                double[][] det = m.component().getParameters();
                for (int d = 0; d < 3; d++) 
                {
                    if (det[d][0] == 1) 
                    {
                        vartab[d][vertice.get()][m.vertex().get()][0] = 1;
                        if (d == 2) signe = 1;
                        else System.out.println(vertice.get()+" "+m.vertex().get()+" "+signe * det[d][1]);
                        vartab[d][vertice.get()][m.vertex().get()][1] = signe * det[d][1];
                    }
                }
            }
            Equation eq = new Equation(new double[nbNodes][nbNodes], eqCurrents, new double[nbNodes][nbNodes], 0, st, equationCurrents);
            equations.add(eq);
        }
        logn("Fait. \n\nEquations aux mailles ...");
        //Lois des mailles :
        double[][] volt;
        for (int p = 0; p < nbNodes - 2; p++) 
        {//Premiers point des cycle;
            for (int f = p + 2; f < nbNodes; f++) 
            {//points de fin des mailles
                volt = new double[nbNodes][nbNodes];
                for (int r = 0; r <= f - p; r++) {
                    volt[r + p][p + (r + 1) % (f - p + 1)] = 1;
                }
                Equation eq = new Equation(volt, new double[nbNodes][nbNodes], new double[nbNodes][nbNodes], 0, st, new double[nbGenerators]);
                equations.add(eq);
            }

        }

        logn("Fait.\n\nResolution....");
        for (Equation eq : equations) System.out.println(eq);
        logn("");
        Equation[] eq = equations.toArray(new Equation[equations.size()]);
        solveur = new Solveur(voltages, currents, admittances, powerCurrents, eq);
        if (solveur.resolution()) 
        {
            for (int i=0;i<nbGenerators;i++) resultCurrents[i] = solveur.currGenerator()[i][1];
            //ajout du tableau actuel aux reusltats
            ajout(solveur.variables());
            System.out.println("Systeme resolu.");
            solved = true;
            return true;
        } else {
            logn("Systeme inrésolvable. Arret...");
            return false;
        }


    }

    //fonction de somme de tableaux : ajoute AU PREMIER TABLEAU le second (ie la somme est faite sur le premier, le second est juste lu.
    private void ajout(double[][][] b) 
    {
        for (int i=0;i<3;i++)
            for (int j=0;j<nbNodes;j++)
                for (int k=0;k<nbNodes;k++)
                    if (varFix[i][j][k][0] == 0)
                        resultVariables[i][j][k] = resultVariables[i][j][k] + b[i][j][k];
    }


    public void printVariables()
    {
        if (!solved) 
        {
            System.out.println("Le circuit n'est pas encore résolu, ou inrésolvable, aucun résultat à afficher");
            return;
        }
        NumberFormat nf = new DecimalFormat("0.00###");
        for(int t=0;t<3;t++) 
        {
            System.out.print("\n"+st[t]+"\n");
            for (int i = 0;i< nbNodes;i++) 
            {
                for (int j = 0;j< nbNodes;j++) 
                {
                    System.out.print(" "+ nf.format(resultVariables[t][i][j]) + " ");
                }
                System.out.println("");
            }
        }
        System.out.println("");
        System.out.println("Courants Generateurs :");
        for (int i=0;i<nbGenerators;i++) 
        {
            System.out.println("Generateur "+ genVertices[i][0]+""+ genVertices[i][1]+" : "+ resultCurrents[i]);
        }

    }


}
