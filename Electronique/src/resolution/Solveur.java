
package resolution;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author Raphaël
 */
public class Solveur 
{

/* ========================= */
/* Déclaration des variables */
/* ========================= */
	
    /**tableaux de controle des variables*/
    private double[][][][] variables;

    //equations : [equations given]

    private Equation[] equations;
    /*Variables appearance order : I U Y
    */
    //grandeurs relatives aux parametres
    private int nbNodes;
    private int nbEq;
    private int nbUnknown;

    private boolean[][] currentsSubstituated;

    private double[][] currGenerator;

    public Solveur(double[][][] volt, double[][][] curr, double[][][] adm, double[][] cg, Equation[] eq) 
    {
        //adm - volt - curr: matrixes of [isDetermined,value]

        //variables array : [IUY]
       /*Variables appearance order :
            I U Y Igenerateur

        */

        nbNodes = volt.length;

        variables = new double[][][][]{curr, volt, adm};
        equations = eq;
        nbEq = eq.length;

        currGenerator = cg;
        printVariables();

        currentsSubstituated = new boolean[nbNodes][nbNodes];

        for (int k = 0; k < 3; k++) 
        {
            for (int i = 0; i < nbNodes; i++) 
            {
                for (int j = 0; j < nbNodes; j++) 
                {
                    if (variables[k][i][j][0] == 1)
                        setVariableValue(new int[]{k, i, j}, variables[k][i][j][1], true);
                }
            }
        }
        //###############################################################################################################tester la validit� des donn�es en entr�e : taille des tableaux, format des admittances, symetrie des courants tension, etc..


        makeSymetries();
        //replacing all found variables (correct initialisation)
        for (int x = 0; x < 3; x++)
            for (int i = 0; i < nbNodes; i++)
                for (int j = 0; j < nbNodes; j++)
                    if (variables[x][i][j][0] == 1)
                        replace(new int[]{x, i, j}, 0, -1, variables[x][i][j][1]);

        //Replacing the generator current if known
        for (double[] d : currGenerator)
            if (d[0] == 1)
                for (int ind = 0; ind < nbEq; ind++)
                    equations[ind].replace(-1, -1, 0, d[1]);


        updateNumberUnknown();
        logn("parameters saved");

    }

    public double[][] currGenerator() 
    {
        return currGenerator;
    }

    public double[][][] variables() 
    {
        double[][][] ret = new double[3][nbNodes][nbNodes];
        for (int i = 0; i < 3; i++) 
        {
            for (int j = 0; j < nbNodes; j++) 
            {
                for (int k = 0; k < nbNodes; k++) 
                {
                    ret[i][j][k] = variables[i][j][k][1];
                }
            }
        }
        return ret;
    }

    public boolean resolution() 
    {
        while (!updateNumberUnknown()) 
        {
            logn("UNKNOWN" + nbUnknown);
            //Step 1 : determining all calculable values : we use the method calculateVariables while it doesn't returns 0;
            int a;
            printVariables();
            while ((calculateVariables() != 0)&&(!updateNumberUnknown())) 
            {


            }
            if (updateNumberUnknown()) break;//if all variables were found : job done .

            printVariables();
            //Step 2 : inject an equation
            if (!substituateVariable()) 
            {
                logn("not solvable");
                return false;
            }
            printEquations();
        }
        logn("System solved");
        return true;
    }

    private int calculateVariables() 
    {
        int cpt = 0;
        for (int ind = 0; ind < nbEq; ind++) 
        {
            Equation eq = equations[ind];
            if ((eq.nunk == 1) && (!eq.solved)) 
            {
                int[] indices = eq.getFirstVariable();//on trouve la seule variable restante.
                double value = eq.getEqConstant(indices);//coeff,constante
                logn("remplacement d'une variables : " + indices[0]+" "+indices[1]+" "+indices[2]);
                replace(indices, 0, ind, value);//on injecte dans toutes les equations
                eq.solved = true;
                cpt++;
                printVariables();
                printEquations();


            }
        }
        return cpt;
    }

    private boolean replace(int[] id, int cat, int nb, double value) 
    {//cat and nb give the original equation, so that we do not modify it.
        //##############################################################################################################verifier qu'on a pas d�ja d�termin� cette valeur
        //replacing the value in all equations
        setVariableValue(id, value, false);
        boolean div = false;
        boolean voltageUnknown = false;
        if (id[0] == 1) 
        {
            div = true;
            voltageUnknown = false;
        }
        if (id[0] == 2) {
            div = true;
            voltageUnknown = true;
        }

        for (int x = 0; x < nbEq; x++) 
        {
            if ((cat != 0) || (nb != x)) 
            {
                equations[x].replace(id[0], id[1], id[2], value);
                if (div) 
                {
                    equations[x].eliminateCurrent(voltageUnknown, id[1], id[2], value);
                }
            }
        }
        return true;
    }

    private boolean setVariableValue(int[] id, double value, boolean remplissage) 
    {/////////////////remplacer les valeurs dans les equations
        //id : coordinates of value in variables
        //value : the new value
        //remplissage : is true if the value is already known. At the beginning, we need to make sure that the variable board is in safe state (eg : if U and I are known, Y is too)

        //modifying the value in the variable array

        if ((id[0] == -1) && (id[1] == -1)) 
        {
            if (currGenerator[id[2]][0] == 0) 
            {
                currGenerator[id[2]] = new double[]{1, value};
            } else {
                if (currGenerator[id[2]][1] != value)
                    return false;///////////////////////////////////////////////////////////////////////equation equivalentes erreur
            }
            return true;
        }

        if (variables[id[0]][id[1]][id[2]][0] == 1) 
        {// if the value is already determined
            if (variables[id[0]][id[1]][id[2]][1] == value) 
            {
                if (!remplissage)
                    ;// true;////////////////////////////////////////////////////////retourner une erreur d'�quations equivalentes
            } else return false;
        }
        if (id[0] == 0) {//if we have found a current
            if ((variables[1][id[1]][id[2]][0] == 0) && (variables[2][id[1]][id[2]][0] == 1) && (variables[2][id[1]][id[2]][1] != 0)) 
            {//voltage not known - admittance known AND not zero
                variables[1][id[1]][id[2]] = new double[]{1, value / variables[2][id[1]][id[2]][1]};//determining the voltage U=I/Y
            }
            if ((variables[1][id[1]][id[2]][0] == 1) && (variables[2][id[1]][id[2]][0] == 0) && (variables[1][id[1]][id[2]][1] != 0)) 
            {//voltage known - admittance not known AND voltage not zero
                variables[1][id[1]][id[2]] = new double[]{1, value / variables[1][id[1]][id[2]][1]};//determining the admittance Y = I/U
            }
        }
        if (id[0] == 1) {//if we have found a voltage
            if ((variables[0][id[1]][id[2]][0] == 0) && (variables[2][id[1]][id[2]][0] == 1)) 
            {//current not known - admittance known
                variables[0][id[1]][id[2]] = new double[]{1, value * variables[2][id[1]][id[2]][1]};//determining the current I = Y*U
            }
            if ((variables[0][id[1]][id[2]][0] == 1) && (variables[2][id[1]][id[2]][0] == 0) && (value != 0)) 
            {//current known - admittance not known AND voltage not zero
                variables[2][id[1]][id[2]] = new double[]{1, variables[0][id[1]][id[2]][1] / value};//determining the admittance Y = I/U
            }
        }
        if (id[0] == 2) {//if we have found an admittance
            if ((variables[1][id[1]][id[2]][0] == 0) && (variables[0][id[1]][id[2]][0] == 1) && (value != 0)) 
            {//voltage not known - current known AND admittance NOT zero
                variables[1][id[1]][id[2]] = new double[]{1, variables[2][id[1]][id[2]][1] / value};//determining the voltage U = I/Y
            }
            if ((variables[1][id[1]][id[2]][0] == 1) && (variables[0][id[1]][id[2]][0] == 0)) 
            {//Voltage known - Current not known
                variables[0][id[1]][id[2]] = new double[]{1, variables[1][id[1]][id[2]][1] * value};//determining current I = U*Y
            }
        }
        System.out.println("replaced");
        if (!remplissage) 
        {
            variables[id[0]][id[1]][id[2]] = new double[]{1, value};

            System.out.println("Symetries made : "+makeSymetries());
        }

        return true;
    }

    private boolean makeSymetries() 
    {
        for (int x = 0; x < 3; x++) 
        {
            for (int i = 0; i < nbNodes; i++) 
            {
                for (int j = 0; j <= i; j++) 
                {
                    if (i == j) {
                        if (variables[x][i][j][1] != 0) {
                            System.out.println("bad case");return false;/////////////////////////////////////////exception parametre pas � 0
                        }
                        continue;
                    } else {
                        if ((variables[x][i][j][0] == 1) && (variables[x][j][i][0] == 0)) 
                        {//if only [i,j] is known
                            if (x == 2) variables[x][j][i] = new double[]{1, variables[x][i][j][1]};
                            else variables[x][j][i] = new double[]{1, -1 * variables[x][i][j][1]};
                            continue;
                        }

                        if ((variables[x][j][i][0] == 1) && (variables[x][i][j][0] == 0)) 
                        {//if only [j,i] is known
                            if (x == 2) variables[x][i][j] = new double[]{1, variables[x][j][i][1]};
                            else variables[x][i][j] = new double[]{1, -1 * variables[x][j][i][1]};
                            continue;
                        }
                        if ((variables[x][j][i][0] == 1) && (variables[x][i][j][0] == 1)) 
                        {
                            if ((x == 2) && (variables[2][i][j][1] != variables[2][j][i][1])) 
                            {
                                return false;
                            }

                            if ((x != 2) && (variables[x][i][j][1] != -1 * variables[x][j][i][1])) 
                            {
                                System.out.println("bad case : "+x+" " +i+" "+j);

                                return false;
                            }
                            continue;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean substituateVariable() 
    {
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

        logn("parametres " + cst + " " + pwcur);

        //substitution
        for (int i = 0; i < nbEq; i++)
            if (i != ind)
                equations[i].substituate(id[0], id[1], id[2], equivalent, cst, pwcur);
        if (id[0] == 0) currentsSubstituated[id[1]][id[2]] = true;
        return true;

    }

    private int getSubstituableEquation() 
    {
        int i = -1;
        boolean cur = isCurrentUnknown();

        for (int cpt = 0; cpt < nbEq; cpt++) 
        {
            Equation eq = equations[cpt];
            if (!eq.used) 
            {
                if (eq.stable) 
                {
                    if (!eq.trivial) 
                    {
                        if ((eq.isCurrentPresent()) || (!cur)) 
                        {//if a current is substituable or if we have determined or substituated all of them
                            i = cpt;
                            break;
                        }
                    }
                }
            }
        }
        return i;
    }

    private boolean isCurrentUnknown() 
    {
        boolean ret = false;
        for (int i = 0; i < nbEq; i++) 
        {
            if (equations[i].isCurrentPresent()) 
            {
                ret = true;
                break;
            }
        }
        return ret;
    }

    private boolean updateNumberUnknown() 
    {//returns if all variables are found
        int nb = 0;
        for (double[] d : currGenerator)
            if (d[0] == 0) nb++;
        for (int k = 0; k < 3; k++)
            for (int i = 0; i < nbNodes; i++)
                for (int j = 0; j < nbNodes; j++)
                    if (variables[k][i][j][0] == 0) nb++;


        nbUnknown = nb;
        return nb == 0;
    }

    public void printEquations() 
    {
        logn("\nEquations\n");
        for (int i = 0; i < nbEq; i++) 
        {
            logn(equations[i]);
        }
    }

    public void printVariables() 
    {
        NumberFormat nf = new DecimalFormat("0.00###");
        char[] aff = equations[0].names;
        for (int t = 0; t < 3; t++) 
        {
            System.out.print("\n" + aff[t] + "\n");
            for (int i = 0; i < nbNodes; i++) 
            {
                for (int j = 0; j < nbNodes; j++) 
                {
                    if (variables[t][i][j][0] == 1) 
                    {
                        System.out.print(" " + nf.format(variables[t][i][j][1]) + " ");
                    } else System.out.print(" x ");
                }
                System.out.println("");
            }
        }
        System.out.println("");
        System.out.println("Courants Generateurs :");
        for (int i = 0; i < currGenerator.length; i++) {
            System.out.println("Generateur " + currGenerator[i][0] + " : " + currGenerator[i][1]);
        }
    }

    private void log(Object s) {
        System.out.print(s);
    }

    private void logn(Object s) 
    {
        log(s);
        log("\n");
    }

}
