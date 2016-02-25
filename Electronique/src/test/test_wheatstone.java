
package test;

import Resolution.Equation;
import Resolution.Solveur;

public class test_wheatstone {

    public test_wheatstone() {
        char[] st = new char[]{'I', 'U', 'Y'};

        /*double[][] t11 = new double[][]{{1, 0}, {0, 0}};
        double[][] t12 = new double[][]{{0, 0}, {0, 0}};
        double[][] t13 = new double[][]{{0, 0}, {0, 0}};*/

        double[][] t11 = new double[][]{{0,1,1,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
        double[][] t12 = new double[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
        double[][] t13 = new double[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};

        Equation eq1 = new Equation(t11, t12, t13, 0, st,-1);
        System.out.println(eq1);

        double[][] t21 = new double[][]{{0,-1,0,0},{0,0,1,1},{0,0,0,0},{0,0,0,0}};
        double[][] t22 = new double[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
        double[][] t23 = new double[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};

        Equation eq2 = new Equation(t21, t22, t23, 0, st,0);
        System.out.println(eq2);

        double[][] t31 = new double[][]{{0,0,0,0},{0,0,0,0},{1,1,0,1},{0,0,0,0}};
        double[][] t32 = new double[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
        double[][] t33 = new double[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};

        Equation eq3 = new Equation(t31, t32, t33, 0, st,0);
        System.out.println(eq3);

        double[][] t41 = new double[][]{{0,0,0,0},{0,0,0,-1},{0,0,0,-1},{0,0,0,0}};
        double[][] t42 = new double[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
        double[][] t43 = new double[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};

        Equation eq4 = new Equation(t41, t42, t43, 0, st,1);
        System.out.println(eq4);

        double[][] t51 = new double[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
        double[][] t52 = new double[][]{{0,1,-1,0},{0,0,0,1},{0,0,0,-1},{0,0,0,0}};
        double[][] t53 = new double[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};

        Equation eq5 = new Equation(t51, t52, t53, 0, st,0);
        System.out.println(eq5);

        double[][] t61 = new double[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
        double[][] t62 = new double[][]{{0,1,0,0},{0,0,0,1},{0,0,0,0},{0,0,0,0}};
        double[][] t63 = new double[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};

        Equation eq6 = new Equation(t61, t62, t63, 10, st,0);
        System.out.println(eq6);

        double[][] t71 = new double[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
        double[][] t72 = new double[][]{{0,1,-1,0},{0,0,1,0},{0,0,0,0},{0,0,0,0}};
        double[][] t73 = new double[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};

        Equation eq7 = new Equation(t71, t72, t73, 0, st,0);
        System.out.println(eq7);


        double[][][] y = new double[][][]  {{{1,0},{1,2},{1,2},{1,0}},
                                            {{0,0},{1,0},{1,2},{1,2}},
                                            {{0,0},{0,0},{1,0},{1,2}},
                                            {{0,0},{0,0},{0,0},{1,0}}};

        double[][][] u = new double[][][] {{{1,0},{0,0},{0,0},{1,10}},
                                            {{0,0},{1,0},{0,0},{0,0}},
                                            {{0,0},{0,0},{1,0},{0,0}},
                                            {{0,0},{0,0},{0,0},{1,0}}};

        double[][][] i = new double[][][]  {{{1,0},{0,0},{0,0},{1,0}},
                                            {{0,0},{1,0},{0,0},{0,0}},
                                            {{0,0},{0,0},{1,0},{0,0}},
                                            {{0,0},{0,0},{0,0},{1,0}}};



        Equation[] eq = new Equation[]{eq1,eq2,eq3,eq4,eq5,eq6,eq7};
        @SuppressWarnings("unused")
		Solveur solveur = new Solveur(u,i,y,new double[]{0,0},eq);

    }
}
