package Resolution;


public class test_basic {
    public test_basic() {
        char[] st = new char[]{'I', 'U', 'Y'};


        double[][] t11 = new double[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        double[][] t12 = new double[][]{{0, 1, 0}, {0, 0, 1}, {1, 0, 0}};
        double[][] t13 = new double[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};

        Equation eq1 = new Equation(t11, t12, t13, 0, st, 0);
        System.out.println();

        double[][] t21 = new double[][]{{0, 1, 1}, {0, 0, 0}, {0, 0, 0}};
        double[][] t22 = new double[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        double[][] t23 = new double[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};

        Equation eq2 = new Equation(t21, t22, t23, 0, st, 1);
        System.out.println(eq2);

        double[][] t31 = new double[][]{{0, 0, 0}, {1, 0, 1}, {0, 0, 0}};
        double[][] t32 = new double[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        double[][] t33 = new double[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};

        Equation eq3 = new Equation(t31, t32, t33, 0, st, 0);
        System.out.println(eq3);

        double[][] t41 = new double[][]{{0, 0, 0}, {0, 0, 0}, {1, 1, 0}};
        double[][] t42 = new double[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        double[][] t43 = new double[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};

        Equation eq4 = new Equation(t41, t42, t43, 0, st, -1);
        System.out.println(eq4);

        double[][][] y = new double[][][]{{{1, 0}, {1, 4}, {1, 3}},
                {{0, 0}, {1, 0}, {1, 2}},
                {{0, 0}, {0, 0}, {1, 0}}};

        double[][][] u = new double[][][]{{{1, 0}, {0, 0}, {1, 3}},
                {{0, 0}, {1, 0}, {0, 0}},
                {{0, 0}, {0, 0}, {1, 0}}};

        double[][][] i = new double[][][]{{{1, 0}, {0, 0}, {0, 0}},
                {{0, 0}, {1, 0}, {0, 0}},
                {{0, 0}, {0, 0}, {1, 0}}};


        Equation[] eq = new Equation[]{eq1, eq2, eq3, eq4};
        Solveur solveur = new Solveur(u, i, y, new double[]{0, 0}, eq);
    }


}
