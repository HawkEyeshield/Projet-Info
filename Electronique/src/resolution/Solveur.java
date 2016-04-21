
package resolution;

import components.Couple;
import components.Tableau;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author Raphaël
 */
public class Solveur 
{
	/* ========================= */
	/* Déclaration des attributs */
	/* ========================= */
	
    /**tableaux de controle des vars.[type][i][j][determined?,value]*/
    private Tableau<Couple>[] vars;

    /**equations : tableau des equations connues*/
    private Equation[] equations;
    //Ordre d'apparition des vars I U Y

    //grandeurs relatives aux parametres : nombre de noeurs, d'equations, et d'inconnues.
    private int nbNodes;
    private int nbEq;
    private int nbUnknown;

    //private boolean[][] currentSubstituated;

    private double[][] powerCurrents;
    
    /* =========================== */
    /* Déclaration du constructeur */
    /* =========================== */
    
    public Solveur(Tableau<Couple> volt, Tableau<Couple> curr, Tableau<Couple> adm, double[][] cg, Equation[] eq) {
        //currents : matrice de tableaux contenant les courants passant à travers les composants entre i et j -> double[nV][nV][][2]
        //voltages : matrice de tableaux contenant la tension entre i et j (tj la meme) -> double[nV][nV][1][2]
        //admittances : matrice de tableaux contenant l'admittance des composants entre i et j -> double[nV][nV][][2]


        nbNodes = volt.size();

        vars = (Tableau<Couple>[]) new Tableau[]{curr, volt, adm};
        equations = eq;
        nbEq = eq.length;

        powerCurrents = cg;

        //TODO faire une verification de la taille egale des tableaux symetriques!!

        //determination de toutes les valeurs par la loi d'ohm
        for (int c = 0; c < 3; c++)
            for (int i = 0; i < nbNodes; i++)
                for (int j = 0; j < nbNodes; j++)
                    for (int k = 0; k < vars[c].size(i, j); k++)
                        if (vars[c].get(i, j, k).found) {
                            setVariableValue(new int[]{c, i, j, k}, vars[c].get(i, j, k).value, true);
                        }


        //symetrisation des  tableaux de vars.
        makeSymetries();

        System.out.println("ducon");
        printVariables();


        //remplacement de toutes les vars connues dans les equations
        for (int c = 0; c < 3; c++) {
            for (int i = 0; i < nbNodes; i++)
                for (int j = 0; j < nbNodes; j++)
                    for (int k = 0; k < vars[c].size(i, j); k++)
                        if (vars[c].get(i, j, k).found)
                            replaceAll(new int[]{c, i, j, k}, -1, vars[c].get(i, j, k).value);
        }



        //Remplacement des courants generateurs
        for (double[] d : powerCurrents)
            if (d[0] == 1)
                for (int ind = 0; ind < nbEq; ind++)
                    equations[ind].replace(-1, -1, 0, -1, d[1]);//TODO adapter  : passer un tableau d'indices


        //init du nombre d'inconnuess
        updateNumberUnknown();

        logn("parameters saved "+nbUnknown);

        System.out.println("ducon");
        printVariables();




    }
    
    /* ======================== */
    /* Déclaration des méthodes */
    /* ======================== */

    //getter des courants generateurs
    public double[][] currGenerator() {
        return powerCurrents;
    }

    //getter des vars
    public Tableau<Double>[] variables() {
        Tableau<Double>[] ret = (Tableau<Double>[])new Tableau[3];
        for (int c = 0; c < 3; c++) {
            ret[c] = new Tableau<>(nbNodes);
            for (int i = 0; i < nbNodes; i++)
                for (int j = 0; j < nbNodes; j++)
                    for (int k = 0; k< vars[c].size(i,j); k++)
                        if (vars[c].get(i, j, k).found)
                            ret[c].get(i,j).add(vars[c].get(i, j, k).value);
        }
        return ret;
    }

    /**fonction de resolution du systeme à proprement parler*/
    public boolean resolution() {
        System.out.println(nbUnknown);
        while (!updateNumberUnknown()) {
            logn("UNKNOWN" + nbUnknown);
            //1 : Determiner toutes les vars calculables, et recommencer tant que l'on en determine plus.

            while (calculateVariables() != 0) {}
            //On s'arrette si on a tout trouvé
            if (updateNumberUnknown()) break;//
            printVariables();

            //Si on a pas tout determiné, on injecte une eqution
            if (!substituateVariable()) {
                //si on a plus de vars à injecter, le systeme n'est pas solvable
                logn("not solvable");
                printEquations();
                return false;
            }
            printEquations();
        }
        //on est sorti -> on a resolu le systeme.
        logn("System solved");
        return true;
    }

     /*fonction de calcul des vars determinables (genre a*Xij=b)*/
    private int calculateVariables() {
        int cpt = 0;
        if (!updateNumberUnknown()) {
            for (int ind = 0; ind < nbEq; ind++) {//pour chaque equation
                Equation eq = equations[ind];
                if ((eq.nunk == 1) && (!eq.solved) && (!eq.trivial)) {//si elle peut etre resolue (plus qu'une variable non constante
                    int[] indices = eq.getFirstVariable();//on recupere les coordonnées de Xij
                    double value = eq.getEqConstant(indices);//on recupere b/a
                    replaceAll(indices, ind, value);//on injecte dans toutes les equations
                    eq.solved = true;//marquage de l'equation comme resolue
                    cpt++;
                    makeSymetries();

                    logn("Variable " + indices[0] + " " + indices[1] + " " + indices[2] + " remplacee");
                    printVariables();
                    printEquations();
                }
            }
        }
        return cpt;

    }

    /**fonction de remplacement d'un variable dans toutes les equations*/
    private boolean replaceAll(int[] id, int nb, double value) {
        /*
        int[3] id donne les coordonnees de la variable à remplacer dans le tableau des vars
        //nb donne l'indice de l'equation d'oigine de la variable, pour qu'elle ne soit pas elle aussi modifiée (ce serait malpropre dans le log)
        */

        //remplacement dans le tableau de valeurs
        setVariableValue(id, value, false);

        //div : est ce qu'on va pouvoir faire un switch du courant (I -> U*Y)
        boolean div = false;
        //toVoltage : si on doit switcher, est ce qu'on doit faire apparaitre une tension
        boolean toVoltage = false;

        //determination des deux vars precedentes : si on trouve une tension ou une admittance, on va pouvoir switcher
        if (id[0] == 1) {//tension determinée
            div = true;
            toVoltage = false;//tension connue -> on fait apparaitre une admittance
        }
        if (id[0] == 2) {//admittance determinée
            div = true;
            toVoltage = true;//tension non connue à priori : on fait apparaitre une  tension
        }
        /* Remarque :
            On ne fera pas reapparaitre une tension ou une admittance au préalable substituée,
            car on passe uniquement de I à U ou Y, et on substitue EN PRIORITE les courants. de cette maniere,
            un switch pourra etre fait UNIQUEMENT si aucune Tension ou Admittance n'a été substituée.
        */

        for (int x = 0; x < nbEq; x++) {//pour chaque equation
            if (nb != x) {//si on est pas sur l'equation source de la variable
                equations[x].replace(id[0], id[1], id[2], id[3], value);//on remplace
                if (div) {//et on switch le courant si necessaire
                    equations[x].eliminateCurrent(toVoltage, id[1], id[2], id[3], value);
                }
            }
        }
        return true;
    }

    /**fonction d'ajout d'une valeur dans le tableau des valeurs*/
    private boolean setVariableValue(int[] id, double value, boolean fill) {
        //id : coordonnées de la variable à ajouter
        //value : la nouvelle valeur
        //fill : vrai si la valeur est deja connue : au debut du programme, on doit s'assurer que les vars sont dans un etat stable (genre (U,Y) connu -> I connu
        //pour cela, on re-set les vars une par une, et si fill est à false, la fonction retournera une erreur si la variable à remlacer est deja connue.


        //modif des courants generateurs
        if ((id[0] == -1) && (id[1] == -1)) {
            if (powerCurrents[id[2]][0] == 0)
                powerCurrents[id[2]] = new double[]{1, value};//cas ou la valeur n'est pas determinee
            else if ((powerCurrents[id[2]][1] == value) && (fill)) return false;//cas ou la valeur est determinee
            return true;
        }
        //modif des vrariables regulieres
        if (vars[id[0]].get(id[1], id[2], id[3]).found) {// if the value is already determined//TODO REFAIRE LES ID!!!!!

            if ((vars[id[0]].get(id[1], id[2], id[3]).value != value) || (!fill)) return false;
        }

        Tableau<Couple> I = vars[0];
        Tableau<Couple> U = vars[1];
        Tableau<Couple> Y = vars[2];

        int[] idU = new int[]{id[1], id[2], 0};//ID pour les tensions
        int[] idO = new int[]{id[1], id[2], id[3]};//id pour Other( admittances, tensions);

        if (id[0] == 0) {//Courant trouve
            if ((!U.get(idU).found) && (Y.get(idO).found) && (Y.get(idO).value != 0)) {//Tension inconnue - Admittance connue et PAS à 0
                U.get(idU).set(true, value / Y.get(idO).value);//Calcul U=I/Y
            }
            if ((U.get(idU).found) && (U.get(idU).value != 0) && (!Y.get(idO).found)) {//Tension connue et PAS à 0 - Admittance inconnue
                Y.get(idO).set(true, value / U.get(idU).value);//Calcul Y = I/U
            }
        } else if (id[0] == 1) {//if we have found a voltage
            for (int l = 0; l < I.size(id[1], id[2]); l++) {//pour chaque composant parallele entre id[1] et id[2]
                idO[2] = l;
                if ((!I.get(idO).found) && (Y.get(idO).found)) {//Courant inconnu - admittance connue
                    I.get(idO).set(true, value * Y.get(idO).value);//Calcul I = Y*U
                }
                if ((I.get(idO).found) && (!Y.get(idO).found) && (value != 0)) {//Courant connu - Admittance inconnue ET Tension PAS à 0
                    Y.get(idO).set(true, I.get(idO).value / value);//Calcul Y = I/U
                }
            }
        } else if (id[0] == 2) {//if we have found an admittance
            if ((!U.get(idU).found) && (I.get(idO).found) && (value != 0)) {//Tension inconnue - Courant connu ET Admittance PAS à 0
                U.get(idU).set(true, Y.get(idO).value / value);//Calcul U = I/Y
            }
            if ((U.get(idU).found) && (!I.get(idO).found)) {//Tension connue - Courant Inconnu
                I.get(idO).set(true, U.get(idU).value * value);//Calcul I = U*Y
            }
        }
        if (!fill) {//Ajout de la variable
            vars[id[0]].get(id[1], id[2], id[3]).set(true, value);
        }
        return true;
    }

    private boolean makeSymetries() {
        for (int x = 0; x < 3; x++) {
            for (int i = 0; i < nbNodes; i++) {
                for (int j = 0; j <= i; j++) {
                    if (i == j) {
                        if (vars[x].size(i,j) != 1) {//si il y a un composant entre i et i :
                            logn("bad case");return false;////////exception parametre pas � 0
                        }
                    } else {
                        for (int l = 0;l<vars[x].size(i,j);l++){

                            if ((vars[x].get(i,j,l).found) && (!vars[x].get(j,i,l).found)) {//if only [i,j] is known
                                if (x == 2) vars[2].get(j, i, l).set(true,vars[2].get(i, j, l).value);
                                else vars[x].get(j,i,l).set(true,-1*vars[x].get(i,j,l).value);
                            }

                            else if ((!vars[x].get(i,j,l).found) && (vars[x].get(j,i,l).found)) {//if only [j,i] is known
                                if (x == 2) vars[2].get(i,j,l).set(true,vars[2].get(j,i,l).value);
                                else vars[x].get(i,j,l).set(true,-vars[x].get(j,i,l).value);
                            }
                            else if ((vars[x].get(i,j,l).found) && (vars[x].get(j,i,l).found)) {
                                if ((x == 2) && (vars[2].get(i,j,l).value != vars[2].get(j,i,l).value)) {
                                    return false;
                                }
                                if ((x != 2) && (vars[x].get(i,j,l).value != -1*vars[x].get(j,i,l).value)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**Fonction de substitution de vars*/
    private boolean substituateVariable() {
        //recuperation d'une equation substituable
        int ind = getSubstituableEquation();
        if (ind == -1) return false;//erreur, pas d'equation substituable
        logn("Injection de l'equation " + ind);

        Equation eq = equations[ind];
        int[] id = eq.getFirstVariable();
        Tableau<Double>[] equivalent = eq.getEquivalent(id);//recuperation de l'equivalent
        double cst = eq.getEqConstant(id);//constante
        double[] pwcur = eq.getEqPowerCurrents(id);//courantsAlim
        eq.used = true;//marquage de l'equation comme utilisée

        //substitution
        for (int i = 0; i < nbEq; i++)
            if (i != ind)
                equations[i].substituate(id[0], id[1], id[2], id[3], equivalent, cst, pwcur);
        return true;

    }

    /**Fonction de recuperation d'une variable substituable*/
    private int getSubstituableEquation() {
        int i = -1;
        boolean cur = isCurrentUnknown();//est ce qu'un courant est substituable

        for (int cpt = 0; cpt < nbEq; cpt++) {
            Equation eq = equations[cpt];
            if ((!eq.used)&&(eq.stable)&&(!eq.trivial))
                if ((eq.isCurrentPresent()) || (!cur)) {//Si on a trouvé un courant, ou une autre variable et que tous les courants ont été substitues
                    i = cpt;
                    break;
                }
        }
        return i;
    }

    /**retourne "est ce qu'il reste du courant à substituer"*/
    private boolean isCurrentUnknown() {
        boolean ret = false;
        for (int i = 0; i < nbEq; i++) {//Pour chaque equation
            //Si l'equation n'est ni utilisée ni resolue, et qu'elle contient du courant
            if ((equations[i].isCurrentPresent())&&(!equations[i].used)&&(!equations[i].solved)) {
                ret = true;
                break;
            }
        }
        return ret;
    }
    
    /**Mise à jour de NbUnknown, retourne true si NbUnknown==0*/
    private boolean updateNumberUnknown() {//returns if all vars are found
        int nb = 0;
        for (double[] d : powerCurrents)
            if (d[0] == 0) nb++;
        for (int c = 0; c < 3; c++)
            for (int i = 0; i < nbNodes; i++)
                for (int j = 0; j < nbNodes; j++)
                    for (int k = 0;k<vars[k].size(i, j);k++)
                        if (!vars[c].get(i, j, k).found) nb++;
        nbUnknown = nb;
        return nb == 0;
    }

    /**Affiche les equations*/
    public void printEquations() {
        logn("\nEquations\n");
        for (int i = 0; i < nbEq; i++) logn(equations[i]);

    }

    /**Affichage des vars*/
    public void printVariables() {
        NumberFormat nf = new DecimalFormat("0.00###");
        char[] aff = equations[0].names;
        for (int t = 0; t < 3; t++) {
            log("\n" + aff[t] + "\n");
            for (int i = 0; i < nbNodes; i++) {
                for (int j = 0; j < nbNodes; j++) {
                    log("(");
                    for (int l = 0; l < vars[t].size(i, j); l++)
                        if (vars[t].get(i,j,l).found) {
                            log(" " + nf.format(vars[t].get(i,j,l).value) + " ");
                        } else log(" x ");
                    log(")");
                }
                logn("");
            }
        }
        logn("");
        logn("Courants Generateurs :");
        for (double[] gen : powerCurrents) {
            logn("Generateur " + gen[0] + " : " + gen[1]);
        }
    }


    private void log(Object s) {
        System.out.print(s.toString());
    }

    private void logn(Object s) {
        log(s);
        log("\n");
    }

}
