package main;
import test.epreuveExtracteur;
import test.test_extracteur;


/** Classe test_wheatstone du projet, boucle principale
 * @author François 
 */

public class Main 
{

	public static void main(String[] args) 
	{
		new epreuveExtracteur();
		/*
		Admittance r1 = new Admittance("r1", 1, 0,100);
		Admittance r2 = new Admittance("r2", 1, 0,200);
		Admittance r3 = new Admittance("r3", 2, 0,100);
		Generator g1 = new Generator("g1", 1, 0,15,0);
		Generator g2 = new Generator("g2", 2, 0, 10, 0);
		
		ArrayList<Admittance> l1 = new ArrayList<Admittance>();
		l1.add(r1);
		l1.add(r2);
		l1.add(r3);
		
		ArrayList<Generator> l2 = new ArrayList<Generator>();
		l2.add(g1);
		l2.add(g2);
		
		Breadboard b = new Breadboard(l1, l2);
		b.setPotentials();
		b.setCurrents();
		System.out.println(b);
		*/
	}

}
