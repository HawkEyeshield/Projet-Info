package resolution;

import components.*;
import exceptions.PowerSupplyException;
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
public class Extractor 
{
	/* ========================= */
	/* Déclaration des attributs */
	/* ========================= */
	
    private CircuitGraph graph;
    private int nbNodes;
    private int nbGenerators;

    private double[] resultCurrents;
    private Tableau<Double>[] resultVariables;
    private Tableau<Couple>[] varFix;

    private char[] st;

    private boolean solved;

    private int[][] genVertices;
    
    /* =========================== */
    /* Déclaration du constructeur */
    /* =========================== */
    
    public Extractor(CircuitGraph g) {
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

    /**
     * @param resetting determine si les vertices vont etre reparametres (proscrit si vous concevez vous meme votre circuit)
     * @throws IndexOutOfBoundsException si le solveur a un problème de tableau dans la méthode printVariables
     * @throws PowerSupplyException si aucun générateur de tension ou courant n'est détecté
     */
    @SuppressWarnings("unchecked")
	public boolean extraction(boolean resetting) throws IndexOutOfBoundsException, PowerSupplyException{
        //Pour le moment, les admittances paralleles ne sont pas acceptées. Une version future les incluera, je n'ai pas le temps pour l'instant

        Vertex[] vertices = graph.getAllVertices();
        nbNodes = vertices.length;

        if (!resetting) {
            //logn("Le reparametrage desactive, je ne toucherai pas aux sommets.\n");
        } else {
            logn("Parametrage des sommets...");
            //Etape -1 : parametrage des vertices, on leur attribue des entiers consecutifs
            for (int i = 0; i < nbNodes; i++) vertices[i].set(i);
            //logn("\t Fait\n");
        }

        logn("Fait\n");

        logn("Tout a l'air correct.\n\nRecherche des generateurs ... ");

        //Etape 1 : recuperation des generateurs
        ArrayList<AbstractGenerator> generateurs = graph.getAllGenerators();
        nbGenerators = generateurs.size();
        switch (nbGenerators) {
            case 0:
                logn("Il n'y a pas de generateur. Arret...");
                throw new PowerSupplyException("Aucune source d'énergie détectée !");
            case 1:
                logn("1 generateur trouvé.");
                break;
            default:
                logn(nbGenerators + " generateurs trouvés.");
        }

        ArrayList<Integer> indexes = new ArrayList<>();
        if (!resetting) {
            logn("Verifcation des indices des generateurs");
            for (AbstractGenerator g:generateurs) {
                if (indexes.contains(g.index)) {
                    logn("Deux generateurs ont le meme indice, Arret.");
                    return false;
                }
                indexes.add(g.index);
            }
        }
        else {
            logn("Reparametrage des indices des generateurs");
            for (int g = 0;g<generateurs.size();g++) {
                generateurs.get(g).index = g;
            }
        }
        logn("tout est clean.");

        logn("Activation ...");
        //Step 3 : extinction de tous les générateurs et allumage du concerné.
        for (AbstractGenerator gen : generateurs) {
            gen.turnOn();
        }
        logn("fait!\n");


        //Variables pour l'extraction :

        //chars de print
        st = new char[]{'I', 'U', 'Y'};

        //le solveur
        Solver solveur;

        //tailles des tableaux à manipuler
        int[][] sizes = new int[nbNodes][nbNodes];
        for (int i = 0;i<nbNodes;i++)
            for (int j = 0;j<nbNodes;j++)
                sizes[i][j] = 1;

        //tableaux de données
        resultCurrents = new double[nbGenerators];
        resultVariables = (Tableau<Double>[]) new Tableau[3];
        varFix = (Tableau<Couple>[]) new Tableau[3];//TODO voir si on peut pas supprier VarFix vu qu'on fait tout d'un coup


        int b,l,n;
        for (Edge e : graph.getAllEdges()) {
            b = e.beginVertex().getIndex();
            l = e.endVertex().getIndex();
            n = e.AdmittancesNb()+1;//add the generator
            sizes[b][l] = n;
            sizes[l][b] = n;
        }

        //init
        for (int i = 0;i<3;i++) {
            resultVariables[i] = new Tableau<Double>(nbNodes, sizes, ()->0.0);//TODO virer le nbNodes en pram qui fait doublon avec sizes
            varFix[i] = new Tableau<Couple>(nbNodes, sizes, ()->new Couple(false,0.0));
        }
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
                        varFix[d].get(dep.getIndex(),arr.getIndex(),a.index).found = true;
                        varFix[d].get(arr.getIndex(),dep.getIndex(),a.index).found = true;
                        //on ajoute la valeur fixe en direct
                        varFix[d].get(dep.getIndex(),arr.getIndex(),a.index).value = s * det[d][1];
                        if (d == 2) s = -1;//on inverse l'inversion dans le cas de l'admittance qui est symetrique.
                        //on ajoute la valeur fixe inverse, en opposant la valeur
                        varFix[d].get(arr.getIndex(),dep.getIndex(),a.index).value = -s * det[d][1];
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
                        varFix[d].get(dep.getIndex(),arr.getIndex(),a.index).found = true;
                        varFix[d].get(arr.getIndex(),dep.getIndex(),a.index).found = true;
                        //on ajoute la valeur fixe en inverse
                        varFix[d].get(arr.getIndex(),dep.getIndex(),a.index).value = s * det[d][1];
                        if (d == 2) s = 1;////on inverse l'opposition dans le cas de l'admittance qui est symetrique.
                        //on ajoute la valeur fixe inverse, en opposant la valeur
                        varFix[d].get(dep.getIndex(),arr.getIndex(),a.index).value = -s * det[d][1];
                    }
                }
            }
        }

        //rentrée des valeurs dans resultVariables
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < nbNodes; j++) {
                for (int k = 0; k < nbNodes; k++) {
                    for (int ll = 0; ll < sizes[j][k]; ll++) {
                        if (varFix[i].get(j, k, ll).found)
                            resultVariables[i].get(j, k).set(ll, varFix[i].get(j, k, ll).value);
                    }
                }
            }
        }
        //Variables temporaires, pour la boucle de résolution

        ArrayList<Equation> equations;//La liste des equations;
        int signe;//La variable de coefficients
        Tableau<Double> eqCurrents;//La matrice de courants pour les equations
        double[][] powerCurrents;




        //Etape 2 : Initialisation des tableaux de parametres
        equations = new ArrayList<>();


        //Tableaux de Variables, plus initialisation
        Tableau<Couple> voltages = new Tableau<>(nbNodes, ()->(new Couple(false,0.0)));//pas besoin d'initialiser, car les tensions existent sans dipole.

        Tableau<Couple> currents = new Tableau<>(nbNodes, sizes, ()->(new Couple(true,0.0)));//There we need to initialise, as current does not exist without a dipole

        Tableau<Couple> admittances = new Tableau<>(nbNodes, sizes, ()->(new Couple(true,0.0)));

        Tableau<Couple>[] vartab = (Tableau<Couple>[])new Tableau[]{currents, voltages, admittances};


        //Vartab : [current, voltage, admittance]
        genVertices = new int[nbGenerators][2];

        for (int c = 0;c<3;c++)
            for (int i = 0;i<nbNodes;i++)
                voltages.get(i,i,0).found = true;


        powerCurrents = new double[nbGenerators][2];
        ArrayList<Integer> addedGens = new ArrayList<>();



        //tableau du courant pour les equations aux noeuds
        double[] equationCurrents;
        //compteur d'ajout des generateurs
        int iGen;
        //ArrayList de ComponentMaps
        ArrayList<ComponentMap> connections;

        //ETAPE 3 : recuperation des equations aux noeuds
        logn("Equation aux noeuds ...");
        for (Vertex vertice : vertices) {
            logn("Vertex "+ vertice.getIndex());

            //initialisation
            eqCurrents = new Tableau<Double>(nbNodes, sizes, () -> 0.0);
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

                int x = vertice.getIndex();
                int y = m.vertex().getIndex();

                //Ajout du generateur dans les tableaux
                if ((type == Type.VOLTAGEGENERATOR) || (type == Type.CURRENTGENERATOR)) {
                    gen = (AbstractGenerator) m.component();
                    iGen = gen.index;
                    if (addedGens.contains(iGen)) {//Si on ne l'a pas ajouté (pas trouvé dans l'arraylist des generateurs)
                        if (type == Type.CURRENTGENERATOR)//Si c'est un generateur de courant, on ajoute le courant donné dans le tableau des generateurs de courant
                            powerCurrents[iGen] = new double[]{1, signe * gen.getCurrent()};
                        addedGens.add(iGen);//on ajoute le generateur

                        //on map le generateur, pour avoir la correspondance indice - (sommetdepart/sommetarrivée)
                        if (m.incoming()) genVertices[iGen] = new int[]{x, y};
                        else genVertices[iGen] = new int[]{y, x};

                    }
                    equationCurrents[iGen] = signe;
                }

                //disonction des types du composant
                switch (m.component().type()) {
                    case ADMITTANCE:
                        //Il y a un composant regulier entre les deux sommets => un courant et une admittance peuvent exister
                        //todo : initialiser l'arrayList jusqu'à l'index donné par le composant pour pouvoir le remplir (--')
                        int index = m.component().index;//decarer avant la boucle
                        //todo on initialise a (true, 0)
                        currents.get(x,y,index).found = false;
                        admittances.get(x,y,index).found = false;
                        eqCurrents.get(x,y).set(m.component().index, 1.0);//ajout du courant traversant le composant concerné à l'equation
                        /*Remarque
                            pas besoin de se preoccuper du signe ici, car on a pas de parametre fixé.
                            On a juste ajouté le courant ORIENTE allant de vertice a m.vertex donc par definition de ecoefficient 1.
                         */
                        break;
                    //Cette partie ne REMPLACE PAS LES VALEURS, ce travail est laissé au solveur.

                    case VOLTAGEGENERATOR://Un generateur de tension determine la tension entre deux sommets;
                        voltages.get(x,y,0).found = true;
                        if (gen.isActive()) {//pas d'erreur, en effet, on a traité le cas du generateur plus haut et on a initialisé gen à ce moment la.
                            voltages.get(x,y,0).value = signe * gen.getVoltage();//Si le generateur n'est pas actif, le voltage sera parametre à 0;
                        }
                        break;

                    case CURRENTGENERATOR://Un generateur de courant determine le courant passant entre deux points
                        currents.get(x,y,0).found = true;
                        //TODO probleme avant on initialisait a false => soit j'ai fait de la merde, soit je vais faire de la merde
                        //TODO probleme avant on initialisait a false => soit j'ai fait de la merde, soit je vais faire de la merde
                        if (gen.isActive()) {//pas d'erreur, en effet, on a traité le cas du generateur plus haut et on a initialisé gen à ce moment la.
                            currents.get(x,y,0).value  = signe * gen.getCurrent();//Si le generateur n'est pas actif, le courant sera parametre à 0;
                        }
                        break;
				default:
					break;
                }

                //Partie mise en memoire des parametres; La partie precedente s'occupait juste des coefficients dans les equations
                double[][] det = m.component().getParameters();
                int indice;
                for (int d = 0; d < 3; d++) {//pour chaque parametre determinable
                    if (d==1) indice = 0;
                    else indice = m.component().index;
                    if (det[d][0] == 1) {//si il est effectivement determiné
                        vartab[d].get(x,y,indice).found = true;//mise à 1 de la determination dans vartab//TODO variabiliser m.component
                        if (d == 2) signe = 1;//si le parametre est une admittance, la valeur ne depend pas du sens
                        //sinon, on met le parametre en memoire, en coherence avec l'orientation du composant
                        vartab[d].get(x,y,indice).value = signe * det[d][1];
                    }
                }
            }

            //ajout de l'equation
            Tableau<Double> t0 = new Tableau<>(nbNodes, () -> 0.0);
            Tableau<Double> t1 = new Tableau<>(nbNodes, sizes, () -> 0.0);

            Equation eq = new Equation(t0, eqCurrents, t1, 0, st, equationCurrents);
            equations.add(eq);
        }

        //ETAPE 5 : Equations aux mailles
        logn("Fait. \n\nEquations aux mailles ...");
        Tableau<Double> volt;
        for (int p = 0; p < nbNodes - 2; p++) {//Premiers point des cycle;
            for (int f = p + 2; f < nbNodes; f++) {//points de fin des mailles
                volt = new Tableau<Double>(nbNodes, ()->0.0);
                for (int r = 0; r <= f - p; r++)
                    volt.get(r + p,p + (r + 1) % (f - p + 1)).set(0,1.0);
                /*remarque :
                    Je sais que la formule est vomitive, mais pinaillez pas... elle est bonne...
                    vous pouvez toujours essayer de la redemontrer si vous voulez, moi j'ai la flemme de le faire en commentaire...
                 */

                //ajout de l'equation
                Tableau<Double> t0 = new Tableau<>(nbNodes, sizes, () -> 0.0);
                Tableau<Double> t1 = new Tableau<>(nbNodes, sizes, () -> 0.0);

                Equation eq = new Equation(volt, t0, t1, 0, st, new double[nbGenerators]);
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


        solveur = new Solver(voltages, currents, admittances, powerCurrents, eq);
        
        System.out.println("Variables initiales");
        
        solveur.printVariables();
        if (solveur.resolution()) {
            for (int i = 0; i < nbGenerators; i++) resultCurrents[i] = solveur.currGenerator()[i][1];
            //rapatriement resultats
            resultVariables = solveur.variables();
            logn("Systeme resolu.\n");
            solved = true;
            return true;
        } else {
            logn("Systeme inrésolvable. Arret...\n");
            return false;
        }

    }

    public double getVoltage(int i,int j) {
        if ((!solved)||(i>=nbNodes)||(i<0)||(j>=nbNodes)||(j<0)) return 0;
        return resultVariables[1].get(i,j,0);
    }

    public double getCurrent(int i, int j, int k){
        if ((!solved)||(i>=nbNodes)||(i<0)||(j>=nbNodes)||(j<0)||(k>=resultVariables[0].size(i,j))||(k<0)) return 0;
        return resultVariables[0].get(i,j,k);
    }

    public double getAdmittance(int i, int j, int k){
        if ((!solved)||(i>=nbNodes)||(i<0)||(j>=nbNodes)||(j<0)||(k>=resultVariables[2].size(i, j))||(k<0)) return 0;
        return resultVariables[2].get(i,j,k);
    }

    public double getGeneratorCurrent(int i){
        if ((!solved)||(i>=nbGenerators)||(i<0)) return 0;
        return resultCurrents[i];
    }




    public void printVariables() {
        if (!solved) {
            System.out.println("Le circuit n'est pas encore résolu, ou inrésolvable, aucun résultat à afficher");
            return;
        }
        NumberFormat nf = new DecimalFormat("0.00###");
        for (int i = 0; i < 3; i++) {
            System.out.print("\n" + st[i] + "\n");
            for (int j = 0; j < nbNodes; j++) {
                for (int k = 0; k < nbNodes; k++) {
                    System.out.print("(");
                    for (int l = 0; l < resultVariables[i].size(j,k); l++) {
                        System.out.print(" " + nf.format(resultVariables[i].get(j, k, l)) + " ");
                    }
                    System.out.print(")");
                }
                System.out.println("");
            }
        }
        System.out.println("");
        System.out.println("Courants Generateurs :");
        for (int i = 0; i < nbGenerators; i++) {
            System.out.println("Generateur " + i + " : " + resultCurrents[i]);
        }

    }


}