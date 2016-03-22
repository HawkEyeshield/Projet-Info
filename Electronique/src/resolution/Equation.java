package resolution;

/**
 * @author Rapĥaël
 */

public class Equation {

    public int size;

    //Chaque equation contient au plus trois types de variables (I,U et Y), une constante, et des courants particuliers appelés courants generateurs n'obeissant pas à la loi d'ohm.
    //Ces variables sont stockées dans le tableau t[I,U,Y] pour plus de simplicité dans la manipulation.
    //Ces tableaux sont bidimentionnels, tels que (double) X[i][j] est le coefficient (tension, courant ou admittance dependant de X) de la variable Xij.
    //Ces matrices sont triangulaires inerieures strictes, pour plus de clarté dans les equations (on aura dont Xij avec j<i)

    private double[][][] t;

    //La constante est la somme des parametres connus
    public double constante;

    //powerCurrents contient les coefficients des courants generateurs
    private double[] powerCurrents;

    //pour plus de facilité, on inteoduit le nombre d'inconnues de l'equation
    public int nunk;

    //Il est important de savoir si l'equation a déjà été utilisée pour une substitution, d'ou la variable used
    public boolean used;

    //Si des mauvaises equations sont introduites on peut arriver à k=0 avec k!=0, ce qui passera à false la variable stable
    public boolean stable;

    //Une variable pour savoir si l'equation est resolue (Xij=k, k un reel)
    public boolean solved;

    //Une variable pour differencier les equations du type 0=0
    public boolean trivial;

    //Les noms à afficher, souvent [I,U,Y]
    public char[] names;

    public Equation(double[][] tens, double[][] cour, double[][] admit, double cst, char[] v, double[] courantAlim) {
        size = tens.length;
        constante = cst;
        names = v;
        powerCurrents = courantAlim;

        used = false;
        solved = false;
        t = new double[][][]{symetrise(true, cour), symetrise(true, tens), symetrise(false, admit)};
        update();
    }

    //fonction de triangularisation des matrices : passage des coefficients sur le triangle inferieur strict.
    double[][] symetrise(boolean opposition, double[][] source) {
        double[][] ret = new double[source.length][source[0].length];
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < i; j++) {
                    if (opposition) {//si on doit symetriser
                        ret[i][j] = source[i][j] - source[j][i];
                        ret[j][i] = 0;
                    } else {
                        ret[i][j] = source[i][j] + source[j][i];
                        ret[j][i] = 0;
                    }
                }
            }
        }
        return ret;
    }

    boolean substituate(int type, int i, int j, double[][][] mod, double cst, double[] curr) {
        //mod_x (x in [|1;3|] donne l'equivalent de la variable substituée dans la base des autres parametres ohmiques
        //cst est la constante à ajouter

        //type : type de la variable à modifier -1:courantAlim/0:Courant/1:Tension/2:Admittance
        //i et j sont les coordonnées dans la matrice de variables, dans le cas ou c'est un courantAim, seul j est utilisé (tableau simple)
        //multi est le coeff multiplicateur (eg : pour 2(einstein) = 3(cauchy) +4(dakhly) +5(l�o lagrange), multi = 2
        //curr est le nouveau tableau des courantsAlim


        double coeff;
        if (type != -1) {
            if (mod[type][i][j] != 0)
                return false; //###################################################################### d�clencher une exception
            //##############################################################################################################verifier que la substitution ne fait pas intervenir le parametre qu'elle substitue
            coeff = t[type][i][j];
        } else coeff = powerCurrents[j];

        //substitution dans les parametres réguliers
        double[][] m;
        double[][] cur;
        if (coeff != 0) {
            for (int t = 0; t < 3; t++) {//modification de chaque tableau de variables
                //ajout terme a terme des tableaux
                cur = this.t[t];
                m = mod[t];
                //
                for (int x = 0; x < size; x++)
                    for (int y = 0; y < size; y++) //par colonne
                        cur[x][y] += coeff * m[x][y];//somme des coefficients
            }
        }

        //substitution dans la constante
        constante -= coeff * cst;

        //substitution dans les courantAlim
        for (int ca = 0; ca < powerCurrents.length; ca++) powerCurrents[ca] += coeff * curr[ca];

        //mise à 0 du parametre substitué
        if (type != -1) t[type][i][j] = 0;//parametre regulier
        else powerCurrents[j] = 0;//courantAlim

        update();
        return true;
    }

    //fonction d'elimination du courant (changement en U ou en Y dependant de  I = U*Y et de la connaissance de la tension ou de l'admittance asociée
    public void eliminateCurrent(boolean replaceByVoltage, int i, int j, double value) {
        double coeff = t[0][i][j];
        if (coeff != 0) {
            if (replaceByVoltage) {//Si on doit faire apparaitre une tension
                t[1][i][j] += value * coeff;
                t[0][i][j] = 0;
            } else {//Si on doit faire apparaitre une admittance
                t[2][i][j] += value * coeff;
                t[0][i][j] = 0;
            }
        }
    }

    //fonction de remplacement d'une variable par sa valeur determinée
    public boolean replace(int type, int i, int j, double value) {
        //type : -1 powercurrent; 0 : cournat; 1 : tension; 2 : admittance
        if ((type == -1) && (i == -1)) {//si on doit remplacer un courantAlim (j joue toujours le role d'indicatif).
            constante -= value * powerCurrents[j];
            powerCurrents[j] = 0;
            return true;
        }

        //Si on doit plutot remplacer une variable reguliere
        double coeff = t[type][i][j];
        constante -= coeff * value;
        t[type][i][j] = 0;
        update();
        return true;
    }

    //fonction de mise à jour des booleens de l'equation
    private void update() {
        updateNunk();
        stable = !((nunk == 0) && (constante != 0));
        trivial = (nunk == 0) && (constante == 0);
    }

    //mise à jour du nombre d'inconnues
    private void updateNunk() {
        int cpt = 0;

        //courantsAlim
        for (double a : powerCurrents)
            if (a != 0) cpt++;

        //Parametres  reguliers
        for (int x = 0; x < 3; x++)
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    if (t[x][i][j] != 0)
                        cpt++;
        nunk = cpt;
    }

    //fonction de recuperation de la premiere variable disponnible (le courant passe avant)
    public int[] getFirstVariable() {
        //d'abord les variables regulieres
        int[] ret = new int[]{-1, -1, -1};

        for (int x = 0; x < 3; x++)
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    if (t[x][i][j] != 0) //trouvé
                        //on marque l'equation comme utilisée
                        ret = new int[]{x, i, j};

        // et si on a pas trouvé, les courants generateurs
        for (int j = 0; j < powerCurrents.length; j++)
            if (powerCurrents[j] != 0) //trouvé
                ret = new int[]{-1, -1, j};

        used = true;
        return ret;
    }

    //fonction de recuperation de la constante equivalents de la variable indicée par id
    public double getEqConstant(int[] id) {
        double coeff;//le coefficient de ladite variable

        if (id[0] == -1) coeff = powerCurrents[id[2]];
        else coeff = t[id[0]][id[1]][id[2]];

        return constante / coeff;
    }

    //fonction de recuperation des courants generateurs equivalents à une variable
    public double[] getEqPowerCurrents(int[] id) {
        //init des variables
        double coeff;
        double[] ret = new double[powerCurrents.length];

        boolean b = (id[0] == -1);//on utilise ce truc 2 fois.

        //coeff
        if (b) coeff = powerCurrents[id[2]];
        else coeff = t[id[0]][id[1]][id[2]];

        //ajout des lignes
        for (int i = 0; i < powerCurrents.length; i++) ret[i] = -powerCurrents[i] / coeff;

        if (b) ret[id[2]] = 0;//si on a equivalenté un corantAlim, on met son coeff à 0 (logique...)
        return ret;
    }

    //fonction de recuperation des variables regulieres equivalentes à une variable (reguliere ou non)
    public double[][][] getEquivalent(int[] id) {
        //init des variables
        double[][][] ret = new double[3][size][size];
        double coeff;

        //determination du coeff;
        if ((id[0] == -1)) coeff = powerCurrents[id[2]];
        else coeff = t[id[0]][id[1]][id[2]];

        //completion du tableau de retour
        for (int x = 0; x < 3; x++)
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    if ((x == id[0]) && (i == id[1]) && (j == id[2]))//si on est à la variable à equivalenter
                        ret[x][i][j] = 0;
                    else ret[x][i][j] = -t[x][i][j] / coeff;
        return ret;
    }

    //permet de savoir si un courant regulier est présent dans les equations
    public boolean isCurrentPresent() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) //si on arrive à un coeff non nul, on retourne true
                if (t[0][i][j] != 0) return true;
        return false;
    }

    @Override
    public String toString() {
        String str = "";
        boolean sum = false;
        for (int typ = 0; typ < 3; typ++) {
            double[][] c = t[typ];
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    if (c[i][j] != 0) {
                        if (sum) str += " + ";
                        else sum = true;
                        if (c[i][j] != 0) str += c[i][j] + "*" + names[typ] + "(" + i + "," + j + ")";
                    }
        }
        for (int k = 0; k < powerCurrents.length; k++)
            if (powerCurrents[k] != 0)
                str += " + " + powerCurrents[k] + "*Ip" + k + " ";


        str += " = " + constante;//+" . Nb inconnues : "+nunk+", Utilise : "+used+", resolue : "+solved;


        return str;
    }
}