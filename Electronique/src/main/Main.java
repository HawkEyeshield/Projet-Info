package main;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import jUnit.AbstractUnit;
import jUnit.TestBreadboard;
import jUnit.TestComputeError;
import jUnit.TestDipoleError;
import jUnit.TestDual;
import jUnit.TestExtractor;
import jUnit.TestWheatstone;

/**
 * Sélecteur de script pour les junit en .jar
 * aucun arg ou 0 => TestBreadboard
 * 1 => TestComputeError
 * 2 => TestDipoleError
 * 3 => TestDual
 * 4 => TestExtractor
 * 5 => TestWheatstone
 */
public class Main extends AbstractUnit {

    private static int value = 0;


    public static void main(String[] args) throws Exception 
    {
        System.out.println(args[0]);

        if (args.length != 0) 
        {
            Main.value = Integer.parseInt(args[0]);
        }
        JUnitCore.main("main.Main");
    }


    @Test
    public void test() 
    {
        if(value == 0)
        {
            TestBreadboard junit = new TestBreadboard();
            junit.setUp();
            junit.test();
            junit.after();
        }
        else if(value == 1)
        {
            TestComputeError junit = new TestComputeError();
            junit.setUp();
            junit.test();
            junit.after();
        }
        else if(value == 2)
        {
            TestDipoleError junit = new TestDipoleError();
            junit.setUp();
            junit.test();
            junit.after();
        }
        else if(value == 3)
        {
            TestDual junit = new TestDual();
            junit.setUp();
            junit.test();
            junit.after();
        }
        else if(value == 4)
        {
            TestExtractor junit = new TestExtractor();
            junit.setUp();
            junit.test();
            junit.after();
        }
        else if(value == 5)
        {
            TestWheatstone junit = new TestWheatstone();
            junit.setUp();
            junit.test();
            junit.after();
        }
        else
        {
            System.out.println("Mauvais argument donné, veuillez renseigner un entier entre 0 et 5");
        }
    }


	@Override
	public void setUp() {
		
	}


	@Override
	public void after() {
		
	}
}

