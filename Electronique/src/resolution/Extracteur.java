package resolution;

import components.Admittance;
import components.AbstractGenerator;
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
public class Extracteur 
{
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
    
    public Extracteur(CircuitGraph g) {
        graph = g;
        solved = false;
    }
    
    /* ======================== */
    /* Déclaration des méthodes */
    /* ======================== */
    
    private void log(Object s) {
        System.out.print(s.toString());
    }

    private void logn(Object s) {
        log(s);
        log("\n");
    }

    public boolean extraction(boolean resetting) {//resetting determine si les vertices vont etre reparametres (proscrit si vous concevez vous meme votre circuit)
        //Pour le moment, les admittances paralleles ne sont pas acceptées. Une version future les incluera, je n'ai pas le temps pour l'instant

        Vertex[] vertices = graph.getAllVertices();
        nbNodes = vertices.length;

        if (!resetting) {
            logn("Le reparametrage desactive, je ne toucherai pas aux sommets.\n");
        } else {
            logn("Parametrage des sommets...");
            //Etape -1 : parametrage des vertices, on leur attribue des entiers consecutifs
            for (int i = 0; i < nbNodes; i++) vertices[i].set(i);
            logn("\t Fait\n");
        }

        //Etape 0 : verification qu'il n'y ait pas d'admittances paralleles
        logn("Verification des composants paralleles...");
        for (int i = 0; i < nbNodes; i++) {
            for (int j = 0; j < i; j++) {
                if (graph.existMultiAdmittances(vertices[i], vertices[j])) {
                    logn("Deux Admittances sont connectees en parallele, je ne peux résoudre ce systeme pour l'instant.");
                    return false;
                }
            }
        }
        logn("Fait\n");

        logn("Tout a l'air correct.\n\nRecherche des generateurs ... ");

        //Etape 1 : recuperation des generateurs
        ArrayList<AbstractGenerator> generateurs = graph.getAllGenerators();
        nbGenerators = generateurs.size();
        switch (nbGenerators) {
            case 0:
                logn("Vous me prenez pour un idiot? Il n'y a pas de generateur. Arret...");
                break;
            case 1:
                logn("1 generateur trouvé.");
                break;
            default:
                logn(nbGenerators + " generateurs trouvés.");
        }

        logn("Activation ...");
        //Step 3 : extinction de tous les générateurs et allumage du concerné.
        for (AbstractGenerator gen : generateurs) gen.turnOn();
        logn("fait!\n");


        //Variables pour l'extraction :

        //chars de print
        st = new char[]{'I', 'U', 'Y'};

        //le solveur
        Solveur solveur;

        //initialisation des tableaux de resultats :
        resultCurrents = new double[nbGenerators];
        resultVariables = new double[3][nbNodes][nbNodes];

        //Tableaux des variables fixees
        varFix = new double[3][nbNodes][nbNodes][2];
        int s;


        //Recuperation des variables fixees
        Vertex dep, arr;
        //pour chaque arrete
        for (Edge e : graph.getAllEdges()) {
            dep = e.beginVertex();
            arr = e.endVertex();
            //Recuperation des valeurs des composants directs
            for (Admittance a : e.directAdmittances) {
                double[][] det = a.getParameters();
                for (int d = 0; d < 3; d++) {
                    s = 1;//les composants sont orientés en direct -> le signe vaut 1
                    if (det[d][0] == 1) {//pour chaque parametre fixe
                        //marquage de la determination du parametre
                        varFix[d][dep.get()][arr.get()][0] = 1;
                        varFix[d][arr.get()][dep.get()][0] = 1;
                        //on ajoute la valeur fixe en direct
                        varFix[d][dep.get()][arr.get()][1] = s * det[d][1];
                        if (d == 2) s = -1;//on inverse l'inversion dans le cas de l'admittance qui est symetrique.
                        //on ajoute la valeur fixe inverse, en opposant la valeur
                        varFix[d][arr.get()][dep.get()][1] = -s * det[d][1];
                    }
                }
            }
            //Recuperation des valeurs des composants indirects
            for (Admittance a : e.indirectAdmittances) {
                double[][] det = a.getParameters();
                for (int d = 0; d < 3; d++) {
                    s = -1;//les composants sont orientés en indirect -> le signe vaut -1
                    if (det[d][0] == 1) {//pour chaque parametre fixe
                        //marquage de la determination du parametre
                        varFix[d][dep.get()][arr.get()][0] = 1;
                        varFix[d][arr.get()][dep.get()][0] = 1;
                        //on ajoute la valeur fixe en inverse
                        varFix[d][arr.get()][dep.get()][1] = s * det[d][1];
                        if (d == 2) s = 1;////on inverse l'opposition dans le cas de l'admittance qui est symetrique.
                        //on ajoute la valeur fixe inverse, en opposant la valeur
                        varFix[d][dep.get()][arr.get()][1] = -s * det[d][1];
                    }
                }
            }
        }

        //rentrée des valeurs dans resultVariables
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < nbNodes; j++) {
                for (int k = 0; k < nbNodes; k++) {
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
        ArrayList<AbstractGenerator> genOrder;


        //Les tableaux de variables
        double[][][] voltages;
        double[][][] admittances;
        double[][][] currents;


        //Etape 2 : Initialisation des tableaux de parametres

        equations = new ArrayList<>();
        voltages = new double[nbNodes][nbNodes][2];//pas besoin d'initialiser, car les tensions existent sans dipole.
        for (double[][] x : voltages) {
            for (double[] y : x) y[0] = 0;
        }

        currents = new double[nbNodes][nbNodes][2];//There we need to initialise, as current do not exist without a dipole
        for (double[][] x : currents) {
            for (double[] y : x) y[0] = 1;
        }

        admittances = new double[nbNodes][nbNodes][2];
        for (double[][] x : admittances) {
            for (double[] y : x) y[0] = 1;
        }

        double[][][][] vartab = new double[][][][]{currents, voltages, admittances};


        //Vartab : [current, voltage, admittance]
        genVertices = new int[nbGenerators][2];

        for (int tp = 0; tp < nbNodes; tp++) {
            for (int dd = 0; dd < 3; dd++) {
                vartab[dd][tp][tp] = new double[]{1, 0};
            }
        }

        powerCurrents = new double[nbGenerators][2];
        genOrder = new ArrayList<>();

        //tableau du courant pour les equations aux noeuds
        double[] equationCurrents;
        //compteur d'ajout des generateurs
        int iGen = 0;
        //variable d'index des generateurs déja ajoutée (on l'initialisera avec un ArrayList.indexOf(generateur))
        int previousIndex;
        //ArrayList de ComponentMaps
        ArrayList<ComponentMap> connections;

        //ETAPE 3 : recuperation des equations aux noeuds
        logn("Equation aux noeuds ...");
        for (Vertex vertice : vertices) {
            logn("Vertex "+ vertice.get());

            //initialisation
            eqCurrents = new double[nbNodes][nbNodes];
            equationCurrents = new double[nbGenerators];
            //recuperation de tous les composants connectes à vertice
            connections = graph.getConnectedComponents(vertice);

            for (ComponentMap m : connections) {//pour chaque connection
                //init
                AbstractGenerator gen = null;
                Type type = m.component().type();
                //setting du signe de chaque composant (les composants sont orientés, dans la mesure ou on parametre une tension et un courant A LEURS BORNES
                if (m.incoming()) signe = 1;
                else signe = -1;


                //Ajout du generateur dans les tableaux
                if ((type == Type.VOLTAGEGENERATOR) || (type == Type.CURRENTGENERATOR)) {
                    gen = (AbstractGenerator) m.component();
                    previousIndex = genOrder.indexOf(gen);//on regarde si on a pas déja le générateur
                    if (previousIndex == -1) {//Si on ne l'a pas ajouté (pas trouvé dans l'arraylist des generateurs)
                        if (type == Type.CURRENTGENERATOR)//Si c'est un generateur de courant, on ajoute le courant donné dans le tableau des generateurs de courant
                            powerCurrents[iGen] = new double[]{1, signe * gen.getCurrent()};
                        genOrder.add(gen);//on ajoute le generateur

                        //on map le generateur, pour avoir la correspondance indice - (sommetdepart/sommetarrivée)
                        if (m.incoming()) genVertices[iGen] = new int[]{vertice.get(), m.vertex().get()};
                        else genVertices[iGen] = new int[]{m.vertex().get(), vertice.get()};

                        previousIndex = iGen;//la variable est utilisée juste après
                        iGen++;
                    }
                    equationCurrents[previousIndex] = signe;
                }

                //disonction des types du composant
                switch (m.component().type()) {
                    case ADMITTANCE:
                        //Il y a un composant regulier entre les deux sommets => un courant et une admittance peuvent exister
                        currents[vertice.get()][m.vertex().get()][0] = 0;
                        admittances[vertice.get()][m.vertex().get()][0] = 0;
                        eqCurrents[vertice.get()][m.vertex().get()] = 1;//ajout du courant traversant le composant concerné à l'equation
                        /*Remarque
                            pas besoin de se preoccuper du signe ici, car on a pas de parametre fixé.
                            On a juste ajouté le courant ORIENTE allant de vertice a m.vertex donc par definition de ecoefficient 1.
                         */
                        break;
                    //Cette partie ne REMPLACE PAS LES VALEURS, ce travail est laissé au solveur.

                    //Todo : verifier si les valeurs des generateurs sont bien remplac�es dans cette partie ET dans la partie recup des valeurs, et si c'est le cas, simplifier.
                    case VOLTAGEGENERATOR://Un generateur de tension determine la tension entre deux sommets;
                        voltages[vertice.get()][m.vertex().get()][0] = 1;
                        if (gen.isActive()) {//pas d'erreur, en effet, on a traité le cas du generateur plus haut et on a initialisé gen à ce moment la.
                            voltages[vertice.get()][m.vertex().get()][1] = signe * gen.getVoltage();//Si le generateur n'est pas actif, le voltage sera parametre à 0;
                        }
                        break;

                    case CURRENTGENERATOR://Un generateur de courant determine le courant passant entre deux points
                        currents[vertice.get()][m.vertex().get()][0] = 0;
                        if (gen.isActive()) {//pas d'erreur, en effet, on a traité le cas du generateur plus haut et on a initialisé gen à ce moment la.
                            currents[vertice.get()][m.vertex().get()][1] = signe * gen.getCurrent();//Si le generateur n'est pas actif, le courant sera parametre à 0;
                        }
                        break;
                }

                //Partie mise en memoire des parametres; La partie precedente s'occupait juste des coefficients dans les equations
                double[][] det = m.component().getParameters();
                for (int d = 0; d < 3; d++) {//pour chaque parametre determinable
                    if (det[d][0] == 1) {//si il est effectivement determiné
                        vartab[d][vertice.get()][m.vertex().get()][0] = 1;//mise à 1 de la determination dans vartab

                        if (d == 2) signe = 1;//si le parametre est une admittance, la valeur ne depend pas du sens

                        //sinon, on met le parametre en memoire, en coherence avec l'orientation du composant
                        vartab[d][vertice.get()][m.vertex().get()][1] = signe * det[d][1];
                    }
                }
            }

            //ajout de l'equation
            Equation eq = new Equation(new double[nbNodes][nbNodes], eqCurrents, new double[nbNodes][nbNodes], 0, st, equationCurrents);
            equations.add(eq);
        }

        //ETAPE 5 : Equations aux mailles
        logn("Fait. \n\nEquations aux mailles ...");
        double[][] volt;
        for (int p = 0; p < nbNodes - 2; p++) {//Premiers point des cycle;
            for (int f = p + 2; f < nbNodes; f++) {//points de fin des mailles
                volt = new double[nbNodes][nbNodes];
                for (int r = 0; r <= f - p; r++)
                    volt[r + p][p + (r + 1) % (f - p + 1)] = 1;
                /*remarque :
                    Je sais que la formule est vomitive, mais pinaillez pas... elle est bonne...
                    vous pouvez toujours essayer de la redemontrer si vous voulez, moi j'ai la flemme de le faire en commentaire...
                 */

                Equation eq = new Equation(volt, new double[nbNodes][nbNodes], new double[nbNodes][nbNodes], 0, st, new double[nbGenerators]);
                equations.add(eq);
            }
        }

        //Resolution itself
        logn("Fait.\n\nEquations obtenues");

        //print des equations
        for (Equation eq : equations) logn(eq);
        logn("");
        logn("Resolution....");
        //conversion en tableau d'equations
        Equation[] eq = equations.toArray(new Equation[equations.size()]);

        //creation du solveir
        solveur = new Solveur(voltages, currents, admittances, powerCurrents, eq);
        if (solveur.resolution()) {
            for (int i = 0; i < nbGenerators; i++) resultCurrents[i] = solveur.currGenerator()[i][1];
            //rapatriement resultats
            ajout(solveur.variables());
            logn("Systeme resolu.\n");
            solved = true;
            return true;
        } else {
            logn("Systeme inrésolvable. Arret...\n");
            return false;
        }

    }

    /**fonction de somme de tableaux : ajoute AU PREMIER TABLEAU le second (ie la somme est faite sur le premier, le second est juste lu.*/
    private void ajout(double[][][] b) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < nbNodes; j++)
                for (int k = 0; k < nbNodes; k++)
                    if (varFix[i][j][k][0] == 0)
                        resultVariables[i][j][k] = resultVariables[i][j][k] + b[i][j][k];
    }


    public void printVariables() {
        if (!solved) {
            System.out.println("Le circuit n'est pas encore résolu, ou inrésolvable, aucun résultat à afficher");
            return;
        }
        NumberFormat nf = new DecimalFormat("0.00###");
        for (int t = 0; t < 3; t++) {
            System.out.print("\n" + st[t] + "\n");
            for (int i = 0; i < nbNodes; i++) {
                for (int j = 0; j < nbNodes; j++) {
                    System.out.print(" " + nf.format(resultVariables[t][i][j]) + " ");
                }
                System.out.println("");
            }
        }
        System.out.println("");
        System.out.println("Courants Generateurs :");
        for (int i = 0; i < nbGenerators; i++) {
            System.out.println("Generateur " + genVertices[i][0] + "" + genVertices[i][1] + " : " + resultCurrents[i]);
        }

    }


}