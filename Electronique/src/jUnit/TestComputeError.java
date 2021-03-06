package jUnit;

import java.util.ArrayList;
import circuit.Breadboard;
import components.AbstractDipole;
import components.Admittance;
import components.VoltageGenerator;
import graphStructure.Vertex;

/**
 * Test unitaire pour vérifier que les problèmes de sommets sont bien détectés
 * On doit détecter un sommet null et des boucles
 * @author François
 */
public class TestComputeError extends AbstractUnit
{
	private Breadboard b;
	ArrayList<AbstractDipole> list = new ArrayList<AbstractDipole>();
	
	@Override
	public void setUp() 
	{
		System.out.println("Ce test doit récupérer deux erreurs relatives à la contruction du graphe.");
		System.out.println("La première concerne la présence d'une boucle dans le graphe.");
		System.out.println("La seconde concerne la présence d'un sommet initialisé à null.\n");
		Vertex v0 = new Vertex(0);
		Vertex v1 = new Vertex(1);
		list.add(new VoltageGenerator("G1",v0,v0, 15));
		list.add(new Admittance("R1", v0, v1, 10));
		b=new Breadboard(list);	
	}

	@Override
	public void test() 
	{
		System.out.println("Début de test : \n");
		try
		{
			b.computeConsole();
		}
		catch(IllegalArgumentException e)
		{
			System.out.println("Exception bien détectée : " + e.getMessage());
		}
		try
		{
			list.clear();
			list.add(new Admittance("R2", new Vertex(0), null));
			b.computeConsole();
		}
		catch(NullPointerException e)
		{
			System.out.println("Exception bien détectée : " + e.getMessage());
		}
	}

	@Override
	public void after() 
	{	
		System.out.println("\nTest terminé !");
	}

}
