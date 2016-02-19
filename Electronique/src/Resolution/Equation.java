package Resolution;

import java.util.ArrayList;


public class Equation {

    public int size;
    //Each Equation contains at most 3 variables, groupped in arrays {t0,t1,t2}, arrays stored in t (when we will have to index those, so put them in an array is necessry)
    //Each line in those arrays is structures as follows : {double coefficient, int line, int column} (the values are found in a square matrix
    //The constant is the sum of all known parameters.
    private double[][][] t;
    /*old
    private double[][] t2;
    private double[][] t3;
    */
    public double constante;

    private double coeff_courant_alim;

    //for more ease to manipulate each Equation, we define nunk as the number of variables still unknown at this moment (actualised after each operation)
    public int nunk;

    //It is important to know if the Equation has already been used for a simplification, so comes the boolean "used"
    public boolean used;

    //after a modification, it is possible to have all variables resolved, and a non null constant : a bad operation has been done, and the Equation is not stable anymore
    public boolean stable;

    //a boolean to check if this equation is resolved
    public boolean solved;

    public boolean trivial;

    //variables name for print
    public char[] names;

    double[][] symetrise(boolean opposition, double[][] source) {
        double[][] ret = new double[source.length][source[0].length];
        for (int i_t = 0;i_t<3;i_t++) {
            for (int i=0;i<size;i++) {
                for (int j=0;j<i;j++) {
                    if (opposition) {
                        ret[i][j] = source[i][j] - source[j][i];
                        ret[j][i] = 0;
                    }
                    else {
                        ret[i][j] = source[i][j]+source[j][i];
                        ret[j][i] = 0;
                    }
                }
            }
        }
        return ret;
    }

    public Equation (double[][] tens,double[][] cour,double[][] admit, double cst,char[] v, double courant) {
        //if ((tt1.length == tt2.length)&&(tt1.length== tt3.length)) return false;//######################################exceptionner
        size = tens.length;

        t = new double[][][]{symetrise(true,tens),symetrise(true,cour),symetrise(false,admit)};
        constante = cst;
        used = false;
        names = v;
        solved = false;
        update();
        coeff_courant_alim = courant;

        //return true;
        }

    boolean substituate(int type, int i, int j, double[][][] mod, double cst, double curr) {//AMODIFIER
        //type gives the type of the variable substituated (1,2 or 3)
        //i and j give the coordinate of the variable (matrix)
        //multi is the variable multiplicator (eg : for 2(einstein) = 3(cauchy) +4(dakhly) +5(léo lagrange) multi will be 2
        //curr is the coefficient of the generator's current

        //mod_x (x in [|1;3|] give the equivalent of the substituated variable (what we will replace it with)
        //cst is the value to add to the constant

        double coeff;
        if (type!=-1) {
            if (mod[type][i][j] != 0)
                return false; //###################################################################### déclencher une exception
            //##############################################################################################################verifier que la substitution ne fait pas intervenir le parametre qu'elle substitue
            coeff = t[type][i][j];
        }
        else coeff = coeff_courant_alim;

        double[][] m;
        double[][] cur;
        if (coeff !=0) {
            for (int i_tab=0;i_tab<3;i_tab++) {//modifying each array of variable
                //all we have to do is to add the array coefficients by coefficients (one multiplied by #coeff
                cur = t[i_tab];
                m = mod[i_tab];
                for(int c_i=0;c_i<size;c_i++) {//row
                    for(int c_j=0;c_j<size;c_j++) {//column
                        cur[c_i][c_j] += coeff * m[c_i][c_j];//summing
                    }
                }
            }
        }
        constante-=coeff*cst;
        if (type != -1) {
            coeff_courant_alim += coeff * curr;
            t[type][i][j] = 0;//deleting the substituated variable
        }

        else coeff_courant_alim = 0;

        update();
        return true;
    }

    public void eliminate_current(boolean voltage_unknown,int i, int j, double value) {
        double coeff = t[0][i][j];
        if (coeff != 0) {
            if (voltage_unknown) {
                t[1][i][j] += value*coeff;
                t[0][i][j] = 0;
            }
            else {
                t[2][i][j] += value*coeff;
                t[0][i][j] = 0;
            }
        }
    }

    public boolean replace(int type, int i, int j, double value) {
        if ((type == -1)&&(i == -1)&&(j == 0)) {
            constante-=value*coeff_courant_alim;
            coeff_courant_alim = 0;
            return true;
        }

        double coeff = t[type][i][j];
        constante -= coeff*value;
        t[type][i][j] = 0;
        update();
        return true;
    }

    private void update() {
        update_nunk();
        stable =  !((nunk==0)&&(constante != 0));
        trivial = (nunk==0)&&(constante == 0);
    }

    private void update_nunk () {
        int cpt = 0;
        if (coeff_courant_alim != 0) cpt++;
        for(int i_t=0;i_t<3;i_t++) {
            for(int i=0;i<size;i++) {
                for (int j = 0; j < size; j++) {
                    if (t[i_t][i][j] != 0) {
                        cpt++;
                    }
                }
            }
        }
        nunk = cpt;
    }

    public int[] get_first_variable() {
        for(int i_t=0;i_t<3;i_t++) {
            for(int i=0;i<size;i++) {
                for (int j = 0; j < size; j++) {
                    if (t[i_t][i][j] != 0) {
                        used = true;
                        return new int[]{i_t,i,j};
                    }
                }
            }
        }
        if (coeff_courant_alim!= 0) return new int[]{-1,-1,0};

        return new int[]{-1,-1,0};/////////////////////////////////////////////////////////////////////////////////////retourner une erreur au lieu d'un tableau pourri
    }

    public double[] get_value(int[] id) {
        double coeff;
        if ((id[0] == -1)&&(id[2]==0)) return new double[]{constante/coeff_courant_alim,0};
        else {
            coeff = t[id[0]][id[1]][id[2]];
            return new double[]{constante/coeff,-coeff_courant_alim/coeff};
        }
    }

    public double[][][] get_equivalent(int[] x) {
        double[][][] ret = new double[3][size][size];
        double coeff;
        if((x[0] == -1)&&(x[1] == -1)&&(x[2]==0)) coeff = coeff_courant_alim;
        else coeff = t[x[0]][x[1]][x[2]];

        for(int i_t=0;i_t<3;i_t++) {
            for(int i=0;i<size;i++) {
                for(int j=0;j<size;j++) {
                    if ((i_t == x[0])&&(i==x[1])&&(j==x[2])) {
                        ret[i_t][i][j] = 0;
                    }
                    else ret[i_t][i][j] = -t[i_t][i][j]/coeff;
                }
            }
        }
        return ret;
    }

    //FONCTION AYANT CONNAISSANCE DE LA POSITION DU COURANT
    public boolean is_current_present() {
        for (int i=0;i<size;i++) {
            for (int j= 0; j<size; j++) {
                if (t[0][i][j] != 0) return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String str = "";
        boolean sum=false;
        for(int typ=0;typ<3;typ++) {
            double[][] c = t[typ];
            for(int i = 0;i<size;i++) {
                for(int j = 0;j<size;j++) {
                    if (c[i][j] != 0) {
                        if (sum) str += " + ";
                        else sum = true;
                        if (c[i][j] != 0) str += c[i][j] + "*" + names[typ] + "(" + i + "," + j + ")";
                    }
                }
            }
        }
        if (coeff_courant_alim != 0) {
            str+= " + " + coeff_courant_alim + "*I ";
        }

        str+= " = " + constante;//+" . Nb inconnues : "+nunk+", Utilise : "+used+", resolue : "+solved;


        return str;
    }


}
//TESTER NUNK
//TESTER STABLE
//TESTER REPLACE