package main;

import java.util.ArrayList;

/** Classe Main du projet, boucle principale  coucou
 * @author CF 
 */

public class Main {

	public static void main(String[] args) 
	{
		Resistor r1 = new Resistor("r1", 1, 0,100);
		Resistor r2 = new Resistor("r2", 1, 0,200);
		Resistor r3 = new Resistor("r3", 2, 0,100);
		Generator g1 = new Generator("g1", 1, 0,15,0);
		Generator g2 = new Generator("g2", 2, 0, 10, 0);
		
		ArrayList<Resistor> l1 = new ArrayList<Resistor>();
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
	}

}
