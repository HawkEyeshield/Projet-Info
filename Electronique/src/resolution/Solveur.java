
package resolution;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;

/**
 * @author Raphaël
 */
public class Solveur 
{
	/* ========================= */
	/* Déclaration des attributs */
	/* ========================= */
	
    /**tableaux de controle des variables.[type][i][j][determined?,value]*/
    private double[][][][] variables;

    /**equations : tableau des equations connues*/
    private Equation[] equations;
    //Ordre d'apparition des variables I U Y

    //grandeurs relatives aux parametres : nombre de noeurs, d'equations, et d'inconnues.
    private int nbNodes;
    private int nbEq;
    private int nbUnknown;

    //private boolean[][] currentSubstituated;

    private double[][] powerCurrents;
    
    /* =========================== */
    /* Déclaration du constructeur */
    /* =========================== */
    
    public Solveur(double[][][] volt, double[][][] curr, double[][][] adm, double[][] cg, Equation[] eq) {


        nbNodes = volt.length;

        variables = new double[][][][]{curr, volt, adm};
        equations = eq;
        nbEq = eq.length;

        powerCurrents = cg;
        printVariables();

        //currentsSubstituated = new boolean[nbNodes][nbNodes];

        for (int k = 0; k < 3; k++)
            for (int i = 0; i < nbNodes; i++)
                for (int j = 0; j < nbNodes; j++)
                    if (variables[k][i][j][0] == 1)
                        setVariableValue(new int[]{k, i, j}, variables[k][i][j][1], true);

        //###############################################################################################################tester la validit� des donn�es en entr�e : taille des tableaux, format des admittances, symetrie des courants tension, etc..

        //symetrisation des  tableaux de variables.
        makeSymetries();

        //remplacement de toutes les variables connues dans les equations
        for (int x = 0; x < 3; x++)
            for (int i = 0; i < nbNodes; i++)
                for (int j = 0; j < nbNodes; j++)
                    if (variables[x][i][j][0] == 1)
                        replaceAll(new int[]{x, i, j}, -1, variables[x][i][j][1]);

        //Remplacement des courants generateurs
        for (double[] d : powerCurrents)
            if (d[0] == 1)
                for (int ind = 0; ind < nbEq; ind++)
                    equations[ind].replace(-1, -1, 0, d[1]);

        //init du nombre d'inconnuess
        updateNumberUnknown();
        logn("parameters saved");

    }
    
    /* ======================== */
    /* Déclaration des méthodes */
    /* ======================== */

    //getter des courants generateurs
    public double[][] currGenerator() {
        return powerCurrents;
    }

    //getter des variables (SANS les doubles de determination : on obtient var[type][i][j] = valeur). Pour APRES la resolution.
    public double[][][] variables() {
        double[][][] ret = new double[3][nbNodes][nbNodes];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < nbNodes; j++)
                for (int k = 0; k < nbNodes; k++)
                    ret[i][j][k] = variables[i][j][k][1];
        return ret;
    }

    /**fonction de resolution du systeme à proprement parler*/
    public boolean resolution() {
        while (!updateNumberUnknown()) {
            logn("UNKNOWN" + nbUnknown);
            //1 : Determiner toutes les variables calculables, et recommencer tant que l'on en determine plus.

            printVariables();
            while (calculateVariables() != 0) {}
            //On s'arrette si on a tout trouvé
            if (updateNumberUnknown()) break;//
            printVariables();

            //Si on a pas tout determiné, on injecte une eqution
            if (!substituateVariable()) {
                //si on a plus de variables à injecter, le systeme n'est pas solvable
                logn("not solvable");
                return false;
            }
            printEquations();
        }
        //on est sorti -> on a resolu le systeme.
        logn("System solved");
        return true;
    }

     /*fonction de calcul des variables determinables (genre a*Xij=b)*/
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
        int[3] id donne les coordonnees de la variable à remplacer dans le tableau des variables
        //nb donne l'indice de l'equation d'oigine de la variable, pour qu'elle ne soit pas elle aussi modifiée (ce serait malpropre dans le log)
        */

        //remplacement dans le tableau de valeurs
        setVariableValue(id, value, false);

        //div : est ce qu'on va pouvoir faire un switch du courant (I -> U*Y)
        boolean div = false;
        //toVoltage : si on doit switcher, est ce qu'on doit faire apparaitre une tension
        boolean toVoltage = false;

        //determination des deux variables precedentes : si on trouve une tension ou une admittance, on va pouvoir switcher
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
                equations[x].replace(id[0], id[1], id[2], value);//on remplace
                if (div) {//et on switch le courant si necessaire
                    equations[x].eliminateCurrent(toVoltage, id[1], id[2], value);
                }
            }
        }
        return true;
    }

    /**fonction d'ajout d'une valeur dans le tableau des valeurs*/
    private boolean setVariableValue(int[] id, double value, boolean fill) {
        //id : coordonnées de la variable à ajouter
        //value : la nouvelle valeur
        //fill : vrai si la valeur est deja connue : au debut du programme, on doit s'assurer que les variables sont dans un etat stable (genre (U,Y) connu -> I connu
        //pour cela, on re-set les variables une par une, et si fill est à false, la fonction retournera une erreur si la variable à remlacer est deja connue.


        //modif des courants generateurs
        if ((id[0] == -1) && (id[1] == -1)) {
            if (powerCurrents[id[2]][0] == 0) powerCurrents[id[2]] = new double[]{1, value};//cas ou la valeur n'est pas determinee
            else if ((powerCurrents[id[2]][1] == value)&&(fill))return false;//cas ou la valeur est determinee
            return true;
        }
        //modif des vrariables regulieres
        if (variables[id[0]][id[1]][id[2]][0] == 1) {// if the value is already determined
            if ((variables[id[0]][id[1]][id[2]][1] != value)||(fill)) return false;
        }
        if (id[0] == 0) {//Courant trouve
            if ((variables[1][id[1]][id[2]][0] == 0) && (variables[2][id[1]][id[2]][0] == 1) && (variables[2][id[1]][id[2]][1] != 0)) {//Tension inconnue - Admittance connue et PAS à 0
                variables[1][id[1]][id[2]] = new double[]{1, value / variables[2][id[1]][id[2]][1]};//Calcul U=I/Y
            }
            if ((variables[1][id[1]][id[2]][0] == 1) && (variables[2][id[1]][id[2]][0] == 0) && (variables[1][id[1]][id[2]][1] != 0)) {//Tension connue et PAS à 0 - Admittance inconnue
                variables[1][id[1]][id[2]] = new double[]{1, value / variables[1][id[1]][id[2]][1]};//Calcul Y = I/U
            }
        }
        if (id[0] == 1) {//if we have found a voltage
            if ((variables[0][id[1]][id[2]][0] == 0) && (variables[2][id[1]][id[2]][0] == 1)) {//Courant inconnu - admittance connue
                variables[0][id[1]][id[2]] = new double[]{1, value * variables[2][id[1]][id[2]][1]};//Calcul I = Y*U
            }
            if ((variables[0][id[1]][id[2]][0] == 1) && (variables[2][id[1]][id[2]][0] == 0) && (value != 0)) {//Courant connu - Admittance inconnue ET Tension PAS à 0
                variables[2][id[1]][id[2]] = new double[]{1, variables[0][id[1]][id[2]][1] / value};//Calcul Y = I/U
            }
        }
        if (id[0] == 2) {//if we have found an admittance
            if ((variables[1][id[1]][id[2]][0] == 0) && (variables[0][id[1]][id[2]][0] == 1) && (value != 0)) {//Tension inconnue - Courant connu ET Admittance PAS à 0
                variables[1][id[1]][id[2]] = new double[]{1, variables[2][id[1]][id[2]][1] / value};//Calcul U = I/Y
            }
            if ((variables[1][id[1]][id[2]][0] == 1) && (variables[0][id[1]][id[2]][0] == 0)) {//Tension connue - Courant Inconnu
                variables[0][id[1]][id[2]] = new double[]{1, variables[1][id[1]][id[2]][1] * value};//Calcul I = U*Y
            }
        }
        logn("replaced");
        if (!fill) {//Ajout de la variable
            variables[id[0]][id[1]][id[2]] = new double[]{1, value};
            logn("Symetries made : "+makeSymetries());
        }
        return true;
    }

    private boolean makeSymetries() {
        for (int x = 0; x < 3; x++) {
            for (int i = 0; i < nbNodes; i++) {
                for (int j = 0; j <= i; j++) {
                    if (i == j) {
                        if (variables[x][i][j][1] != 0) {
                            logn("bad case");return false;////////exception parametre pas � 0
                        }
                    } else {
                        if ((variables[x][i][j][0] == 1) && (variables[x][j][i][0] == 0)) {//if only [i,j] is known
                            if (x == 2) variables[x][j][i] = new double[]{1, variables[x][i][j][1]};
                            else variables[x][j][i] = new double[]{1, -1 * variables[x][i][j][1]};
                            continue;
                        }

                        if ((variables[x][j][i][0] == 1) && (variables[x][i][j][0] == 0)) {//if only [j,i] is known
                            if (x == 2) variables[x][i][j] = new double[]{1, variables[x][j][i][1]};
                            else variables[x][i][j] = new double[]{1, -1 * variables[x][j][i][1]};
                            continue;
                        }
                        if ((variables[x][j][i][0] == 1) && (variables[x][i][j][0] == 1)) {
                            if ((x == 2) && (variables[2][i][j][1] != variables[2][j][i][1])) {
                                return false;
                            }
                            if ((x != 2) && (variables[x][i][j][1] != -1 * variables[x][j][i][1])) {
                                logn("bad case : "+x+" " +i+" "+j);
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**Fonction de substitution de variables*/
    private boolean substituateVariable() {
        //recuperation d'une equation substituable
        int ind = getSubstituableEquation();
        if (ind == -1) return false;//erreur, pas d'equation substituable
        logn("Injection de l'equation " + ind);

        Equation eq = equations[ind];
        int[] id = eq.getFirstVariable();
        double[][][] equivalent = eq.getEquivalent(id);//recuperation de l'equivalent
        double cst = eq.getEqConstant(id);//constante
        double[] pwcur = eq.getEqPowerCurrents(id);//courantsAlim
        eq.used = true;//marquage de l'equation comme utilisée

        //substitution
        for (int i = 0; i < nbEq; i++)
            if (i != ind)
                equations[i].substituate(id[0], id[1], id[2], equivalent, cst, pwcur);
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
    private boolean updateNumberUnknown() {//returns if all variables are found
        int nb = 0;
        for (double[] d : powerCurrents)
            if (d[0] == 0) nb++;
        for (int k = 0; k < 3; k++)
            for (int i = 0; i < nbNodes; i++)
                for (int j = 0; j < nbNodes; j++)
                    if (variables[k][i][j][0] == 0) nb++;
        nbUnknown = nb;
        return nb == 0;
    }

    /**Affiche les equations*/
    public void printEquations() {
        logn("\nEquations\n");
        for (int i = 0; i < nbEq; i++) logn(equations[i]);

    }

    /**Affichage des variables*/
    public void printVariables() {
        NumberFormat nf = new DecimalFormat("0.00###");
        char[] aff = equations[0].names;
        for (int t = 0; t < 3; t++) {
            log("\n" + aff[t] + "\n");
            for (int i = 0; i < nbNodes; i++) {
                for (int j = 0; j < nbNodes; j++) {
                    if (variables[t][i][j][0] == 1) {
                        log(" " + nf.format(variables[t][i][j][1]) + " ");
                    } else log(" x ");
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
    }

    private void logn(Object s) {
        log(s);
        log("\n");
    }

}
