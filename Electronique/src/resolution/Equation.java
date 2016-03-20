package resolution;

/**
 * @author Rapĥaël
 */

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

    private double[] powerCurrents;

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

    public Equation (double[][] tens,double[][] cour,double[][] admit, double cst,char[] v, double[] courantAlim)
    {
        size = tens.length;
        constante = cst;
        names = v;
        powerCurrents = courantAlim;

        used = false;
        solved = false;
        t = new double[][][]{symetrise(true,cour),symetrise(true,tens),symetrise(false,admit)};
        update();


        //return true;
    }



    double[][] symetrise(boolean opposition, double[][] source) 
    {
        double[][] ret = new double[source.length][source[0].length];
        for (int k = 0;k<3;k++)
        {
            for (int i=0;i<size;i++) 
            {
                for (int j=0;j<i;j++) 
                {
                    if (opposition) 
                    {
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

    boolean substituate(int type, int i, int j, double[][][] mod, double cst, double[] curr)
    {   //AMODIFIER
        //mod_x (x in [|1;3|] donne l'equivalent de la variable substituée dans la base des autres parametres ohmiques
        //cst est la constante à ajouter

        //type : type de la variable à modifier -1:courantAlim/0:Courant/1:Tension/2:Admittance
        //i et j sont les coordonnées dans la matrice de variables, dans le cas ou c'est un courantAim, seul j est utilisé (tableau simple)
        //multi est le coeff multiplicateur (eg : pour 2(einstein) = 3(cauchy) +4(dakhly) +5(l�o lagrange), multi = 2
        //curr est le nouveau tableau des courantsAlim


        double coeff;
        if (type!=-1) 
        {
            if (mod[type][i][j] != 0)
                return false; //###################################################################### d�clencher une exception
            //##############################################################################################################verifier que la substitution ne fait pas intervenir le parametre qu'elle substitue
            coeff = t[type][i][j];
        }
        else coeff = powerCurrents[j];

        //substitution dans les parametres réguliers
        double[][] m;
        double[][] cur;
        if (coeff !=0) {
            for (int t=0;t<3;t++)
            {//modifying each array of variable
                //all we have to do is to add the array coefficients by coefficients (one multiplied by #coeff
                cur = this.t[t];
                m = mod[t];
                for(int x=0;x<size;x++)
                {//row
                    for(int y=0;y<size;y++)
                    {//column
                        cur[x][y] += coeff * m[x][y];//summing
                    }
                }
            }
        }

        //substitution dans la constante
        constante-=coeff*cst;

        //substitution dans les courantAlim
        for (int ca=0;ca< powerCurrents.length;ca++) powerCurrents[ca] += coeff * curr[ca];

        //mise à 0 du parametre substitué
        if (type != -1) t[type][i][j] = 0;//parametre regulier
        else  powerCurrents[j] = 0;//courantAlim

        update();
        return true;
    }

    public void eliminateCurrent(boolean voltageUnknown, int i, int j, double value)
    {
        double coeff = t[0][i][j];
        if (coeff != 0) 
        {
            if (voltageUnknown)
            {
                t[1][i][j] += value*coeff;
                t[0][i][j] = 0;
            }
            else {
                t[2][i][j] += value*coeff;
                t[0][i][j] = 0;
            }
        }
    }

    public boolean replace(int type, int i, int j, double value)
    {
        if ((type == -1)&&(i == -1))//si on doit remplacer un courantAlim (j joue toujours le role d'indicatif).
        {
            constante-=value* powerCurrents[j];
            powerCurrents[j] = 0;
            return true;
        }

        double coeff = t[type][i][j];
        constante -= coeff*value;
        t[type][i][j] = 0;
        update();
        return true;
    }

    private void update() 
    {
        updateNunk();
        stable =  !((nunk==0)&&(constante != 0));
        trivial = (nunk==0)&&(constante == 0);
    }

    private void updateNunk()
    {
        int cpt = 0;

        //courantsAlim
        for (double a: powerCurrents)
            if (a!=0) cpt++;

        //Parametres  reguliers
        for(int x=0;x<3;x++)
            for(int i=0;i<size;i++)
                for (int j = 0; j < size; j++)
                    if (t[x][i][j] != 0)
                        cpt++;
        nunk = cpt;
    }

    public int[] getFirstVariable()
    {
        for(int x=0;x<3;x++)
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    if (t[x][i][j] != 0) {
                        used = true;
                        return new int[]{x, i, j};
                    }
        for (int j=0;j< powerCurrents.length;j++)
            if (powerCurrents[j] != 0) return new int[]{-1,-1,j};

        return new int[]{-1,-1,-1};/////////////////////////////////////////////////////////////////////////////////////retourner une erreur au lieu d'un tableau pourri
    }

    public double getEqConstant(int[] id)
    {
        System.out.println(id[0]+" "+id[1]+" "+id[2]);
        double coeff;
        if (id[0] == -1)
            return constante/ powerCurrents[id[2]];
        else {
            coeff = t[id[0]][id[1]][id[2]];
            return constante/coeff;
        }
    }

    public double[] getEqPowerCurrents(int[] id) {
        double coeff;
        double[] ret = new double[powerCurrents.length];

        boolean b = (id[0] == -1);//on utilise ce truc 2 fois.

        //coeff
        if (b) coeff=powerCurrents[id[2]];
        else coeff = t[id[0]][id[1]][id[2]];

        //ajout des lignes
        for (int i=0;i<powerCurrents.length;i++) ret[i] = -powerCurrents[i]/coeff;

        if (b) ret[id[2]] = 0;//si on a equivalenté un corantAlim, on met son coeff à 0 (logique...)
        return ret;
    }
    public double[][][] getEquivalent(int[] id)
    {
        double[][][] ret = new double[3][size][size];
        double coeff;
        if((id[0] == -1)) coeff = powerCurrents[id[2]];
        else coeff = t[id[0]][id[1]][id[2]];

        for(int x=0;x<3;x++)
        {
            for(int i=0;i<size;i++) 
            {
                for(int j=0;j<size;j++) 
                {
                    if ((x == id[0])&&(i==id[1])&&(j==id[2]))
                    {
                        ret[x][i][j] = 0;
                    }
                    else ret[x][i][j] = -t[x][i][j]/coeff;
                }
            }
        }
        return ret;
    }

    //FONCTION AYANT CONNAISSANCE DE LA POSITION DU COURANT
    public boolean isCurrentPresent()
    {
        for (int i=0;i<size;i++) 
        {
            for (int j= 0; j<size; j++) 
            {
                if (t[0][i][j] != 0) return true;
            }
        }
        return false;
    }

    @Override
    public String toString() 
    {
        String str = "";
        boolean sum=false;
        for(int typ=0;typ<3;typ++) 
        {
            double[][] c = t[typ];
            for(int i = 0;i<size;i++)
                for (int j = 0; j < size; j++)
                    if (c[i][j] != 0) {
                        if (sum) str += " + ";
                        else sum = true;
                        if (c[i][j] != 0) str += c[i][j] + "*" + names[typ] + "(" + i + "," + j + ")";
                    }
        }
        for (int k=0;k<powerCurrents.length;k++)
            if (powerCurrents[k] != 0)
                str += " + " + powerCurrents[k] + "*Ip"+k+" ";


        str+= " = " + constante;//+" . Nb inconnues : "+nunk+", Utilise : "+used+", resolue : "+solved;


        return str;
    }




}
//TESTER NUNK
//TESTER STABLE
//TESTER REPLACE