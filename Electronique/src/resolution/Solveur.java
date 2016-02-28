
package Resolution;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author Briztou
 */
public class Solveur 
{
    //tableaux de controle des variables.
    private double[][][][] variables;

    //equations : [equations given]

    private Equation[] equations;
    /*Variables appearance order : I U Y
    */
    //grandeurs relatives aux parametres
    private int nb_nodes;
    private int nb_eq;
    private int nb_unknown;

    private boolean current_unknown;

    private boolean[][] currents_substituated;

    double[] curr_generator;

    public Solveur(double[][][] volt, double[][][] curr, double[][][] adm,double[] cg, Equation[] eq) 
    {
        //adm - volt - curr: matrixes of [is_determined,value]

        //variables array : [IUY]
       /*Variables appearance order :
            I U Y Igenerateur

        */
        nb_nodes = volt.length;

        variables = new double[][][][]{curr, volt, adm};
        equations = eq;
        nb_eq = eq.length;

        curr_generator = cg;

        currents_substituated = new boolean[nb_nodes][nb_nodes];

        for (int i_t = 0; i_t < 3; i_t++) 
        {
            for (int i = 0; i < nb_nodes; i++) 
            {
                for (int j = 0; j < nb_nodes; j++) 
                {
                    if (variables[i_t][i][j][0] == 1)
                        set_variable_value(new int[]{i_t, i, j}, variables[i_t][i][j][1], true);
                }
            }
        }
        //###############################################################################################################tester la validit� des donn�es en entr�e : taille des tableaux, format des admittances, symetrie des courants tension, etc..


        make_symetries();
        print_variables();

        //replacing all found variables (correct initialisation)
        for (int i_t = 0; i_t < 3; i_t++) 
        {
            for (int i = 0; i < nb_nodes; i++) 
            {
                for (int j = 0; j < nb_nodes; j++) 
                {
                    if (variables[i_t][i][j][0] == 1) 
                    {
                        replace(new int[]{i_t, i, j}, 0, -1, variables[i_t][i][j][1]);
                       /*for(int ind =0;ind<nb_eq;ind++) {
                           equations[ind].replace(i_t,i,j,variables[i_t][i][j][1]);
                       }*/
                    }
                }
            }
        }

        //Replacing the generator current if known
        if (curr_generator[0] == 1) 
        {
            for (int ind = 0; ind < nb_eq; ind++) 
            {
                equations[ind].replace(-1, -1, -1, curr_generator[1]);
            }
        }

        update_number_unknown();
        System.out.println("parameters saved");
        print_equations();
        print_variables();
        resolution();
    }

    public boolean resolution() 
    {
        while (!update_number_unknown()) 
        {
            System.out.println("UNKNOWN" + nb_unknown);
            //Step 1 : determining all calculable values : we use the method calculate_variables while it doesn't returns 0;
            int a;
            while ((a = calculate_variables()) != 0) 
            {

            }
            update_number_unknown();
            if (update_number_unknown()) break;//if all variables were found : job done .


            //Step 2 : inject an equation
            if (!substituate_variable()) 
            {
                System.out.println("not solvable");
                return false;
            }
            print_equations();
        }
        System.out.println("System solved");
        print_variables();
        return true;
    }

    private int calculate_variables() 
    {	
        int cpt = 0;
        for(int ind =0;ind<nb_eq;ind++) 
        {
            if ((equations[ind].nunk == 1)&&(!equations[ind].solved)) 
            {
                int[] indices = equations[ind].get_first_variable();//on trouve la seule variable restante.
                double value = equations[ind].get_value(indices)[0];//coeff,constante
                replace(indices, 0, ind, value);//on injecte dans toutes les equations
                equations[ind].solved = true;
                cpt++;
                System.out.println("remplacement d'une variables");
                print_equations();


            }
        }
        return cpt;
    }

    private boolean replace(int[] id,int cat, int nb, double value) 
    {//cat and nb give the original equation, so that we do not modify it.
        //##############################################################################################################verifier qu'on a pas d�ja d�termin� cette valeur

        //replacing the value in all equations
        set_variable_value(id,value,false);
        boolean div = false;
        boolean voltage_unknown = false;
        if (id[0] == 1) 
        {
            div = true;
            voltage_unknown = false;
        }
        if (id[0]== 2) 
        {
            div = true;
            voltage_unknown = true;
        }

        for (int i_e=0;i_e<nb_eq;i_e++) 
        {
            if ((cat != 0)||(nb != i_e)) 
            {
                equations[i_e].replace(id[0],id[1],id[2],value);
                if (div) {
                    equations[i_e].eliminate_current(voltage_unknown, id[1],id[2],value);
                }
            }
        }
        return true;
    }

    public boolean set_variable_value(int[] id, double value,boolean remplissage) 
    {/////////////////remplacer les valeurs dans les equations
        //id : coordinates of value in variables
        //value : the new value
        //remplissage : is true if the value is already known. At the beginning, we need to make sure that the variable board is in safe state (eg : if U and I are known, Y is too)

        //modifying the value in the variable array

        if ((id[0] == -1)&&(id[1] == -1)&&(id[2] == 0)) 
        {
            if (curr_generator[0] == 0) 
            {
                curr_generator = new double[]{1,value};
            } else {
                if (curr_generator[1] != value)
                    return false;///////////////////////////////////////////////////////////////////////equation equivalentes erreur
            }
            return true;
        }

        if (variables[id[0]][id[1]][id[2]][0] == 1) 
        {// if the value is already determined
            if (variables[id[0]][id[1]][id[2]][1] == value) 
            {
                if (!remplissage);// true;////////////////////////////////////////////////////////retourner une erreur d'�quations equivalentes
            }
            else return false;
        }
        if (id[0] == 0) {//if we have found a current
            if ((variables[1][id[1]][id[2]][0] == 0)&&(variables[2][id[1]][id[2]][0] == 1)&&(variables[2][id[1]][id[2]][1] !=0)) 
            {//voltage not known - admittance known AND not zero
                variables[1][id[1]][id[2]] = new double[]{1,value/variables[2][id[1]][id[2]][1]};//determining the voltage U=I/Y
            }
            if ((variables[1][id[1]][id[2]][0] == 1)&&(variables[2][id[1]][id[2]][0] == 0)&&(variables[1][id[1]][id[2]][1] !=0)) 
            {//voltage known - admittance not known AND voltage not zero
                variables[1][id[1]][id[2]] = new double[]{1,value/variables[1][id[1]][id[2]][1]};//determining the admittance Y = I/U
            }
        }
        if (id[0] == 1) {//if we have found a voltage
            if ((variables[0][id[1]][id[2]][0] == 0)&&(variables[2][id[1]][id[2]][0] == 1)) 
            {//current not known - admittance known
                variables[0][id[1]][id[2]] = new double[]{1,value*variables[2][id[1]][id[2]][1]};//determining the current I = Y*U
            }
            if ((variables[0][id[1]][id[2]][0] == 1)&&(variables[2][id[1]][id[2]][0] == 0)&&(value!=0)) 
            {//current known - admittance not known AND voltage not zero
                variables[2][id[1]][id[2]] = new double[]{1,variables[0][id[1]][id[2]][1]/value};//determining the admittance Y = I/U
            }
        }
        if (id[0] == 2) {//if we have found an admittance
            if ((variables[1][id[1]][id[2]][0] == 0)&&(variables[0][id[1]][id[2]][0] == 1)&&(value!=0)) 
            {//voltage not known - current known AND admittance NOT zero
                variables[1][id[1]][id[2]] = new double[]{1,variables[2][id[1]][id[2]][1]/value};//determining the voltage U = I/Y
            }
            if ((variables[1][id[1]][id[2]][0] == 1)&&(variables[0][id[1]][id[2]][0] == 0)) 
            {//Voltage known - Current not known
                variables[0][id[1]][id[2]] = new double[]{1,variables[1][id[1]][id[2]][1]*value};//determining current I = U*Y
                System.out.println(id[1]+" "+ id[2] +"replaced");
            }
        }
        if (!remplissage) 
        {
            variables[id[0]][id[1]][id[2]] = new double[] {1,value};
            make_symetries();
        }

        return true;
    }

    private boolean make_symetries() 
    {
        for (int i_t=0;i_t<3;i_t++)
        {
            for (int i=0;i<nb_nodes;i++) 
            {
                for(int j=0;j<=i;j++) 
                {
                    if (i == j) 
                    {
                        if (variables[i_t][i][j][1] != 0) return false;/////////////////////////////////////////exception parametre pas � 0
                        continue;
                    }
                    else {
                        if ((variables[i_t][i][j][0] == 1) && (variables[i_t][j][i][0] == 0)) 
                        {//if only [i,j] is known
                            if (i_t == 2) variables[i_t][j][i] = new double[]{1, variables[i_t][i][j][1]};
                            else variables[i_t][j][i] = new double[]{1, -1*variables[i_t][i][j][1]};
                            continue;
                        }

                        if ((variables[i_t][j][i][0] == 1) && (variables[i_t][i][j][0] == 0)) 
                        {//if only [j,i] is known
                            if (i_t == 2) variables[i_t][i][j] = new double[]{1, variables[i_t][j][i][1]};
                            else variables[i_t][i][j] = new double[]{1, -1*variables[i_t][j][i][1]};
                            continue;
                        }
                        if ((variables[i_t][j][i][0] == 1) && (variables[i_t][i][j][0] == 1)) 
                        {
                            if ((i_t == 2) && (variables[2][i][j][1] != variables[2][j][i][1])) 
                            {
                                return false;
                            }
                            if ((i_t != 2) && (variables[i_t][i][j][1] != -1*variables[i_t][j][i][1])) 
                            {
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

    private boolean substituate_variable() 
    {
        int ind = get_substituable_equation();
        if (ind==-1) return false;//if no equation is substituable, error

        System.out.println("Injection de l'equation "+ind);
        int[] indice = equations[ind].get_first_variable();
        double[][][] equivalent = equations[ind].get_equivalent(indice);//recuperation of equivalents
        double[] param = equations[ind].get_value(indice);//constante, coefficient du courant g�n�rateur

        System.out.println("parametres " + param[0]+" "+param[1]);



        equations[ind].used = true;//marking that the equation has now been used.

        for (int i=0;i<nb_eq;i++) 
        {
            if (i!=ind) {
                equations[i].substituate(indice[0], indice[1], indice[2], equivalent, param[0],param[1]);
            }
        }
        if (indice[0]== 0) currents_substituated[indice[1]][indice[2]] = true;
        return true;

    }

    private int get_substituable_equation() 
    {
        int i = -1;
        boolean cur = isCurrent_unknown();

        for (int cpt=0;cpt<nb_eq;cpt++) 
        {
            if (!equations[cpt].used) 
            {
                if (equations[cpt].stable) 
                {
                    if (!equations[cpt].trivial) 
                    {
                        if ((equations[cpt].is_current_present()) || (!cur)) 
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

    private boolean isCurrent_unknown() 
    {
        boolean ret = false;
        for (int i=0;i<nb_eq;i++) 
        {
            if (equations[i].is_current_present()) 
            {
                ret = true;
                break;
            }
        }
        current_unknown = ret;
        return ret;
    }//////////prendre en compte les injetions

    public boolean update_number_unknown() 
    {//returns if all variables are found
        int nb = 0;
        if (curr_generator[0] == 0) nb++;
        for (int i_t=0;i_t<3;i_t++) 
        {
            for (int i = 0;i<nb_nodes;i++) 
            {
                for (int j=0; j < nb_nodes; j++) 
                {
                    if (variables[i_t][i][j][0]==0) nb++;
                }
            }
        }
        nb_unknown = nb;
        return nb==0;
    }

    public void print_equations() 
    {
        System.out.println("\nEquations\n");
        for (int i = 0;i<nb_eq;i++) 
        {
            System.out.println(equations[i]);
        }
    }

    public void print_variables() 
    {
        NumberFormat nf = new DecimalFormat("0.00###");
        char[] aff = equations[0].names;
        for(int i_t=0;i_t<3;i_t++) 
        {
            System.out.print("\n"+aff[i_t]+"\n");
            for (int i = 0;i<nb_nodes;i++) 
            {
                for (int j = 0;j<nb_nodes;j++) 
                {
                    if (variables[i_t][i][j][0] == 1) 
                    {
                        System.out.print(" "+ nf.format(variables[i_t][i][j][1]) + " ");
                    }
                    else System.out.print(" x ");
                }
                System.out.println("");
            }
        }
        System.out.println("courant Generateur : "+ curr_generator[1]);
    }

}
