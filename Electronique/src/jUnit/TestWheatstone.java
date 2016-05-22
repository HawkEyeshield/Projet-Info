package jUnit;

import components.Admittance;
import components.VoltageGenerator;
import exceptions.PowerSupplyException;
import graphStructure.CircuitGraph;
import graphStructure.Vertex;
import resolution.Extractor;

/**
 * Classe définissant un test pour le pont de wheatstone
 * @author Raphaël
 */
public class TestWheatstone extends AbstractUnit
{
	CircuitGraph g;
	Extractor e;

	@Override
	public void setUp() 
	{
		System.out.println("Ce test doit vérifier la condition d'équilibre du pont de Wheatstone.");
		System.out.println("Le test doit donc affcher un courant et une tension nulle entre les sommets 1 et 2.\n");
		g = new CircuitGraph();
		//les sommets du graphe sont definis ici.
		Vertex v0 = new Vertex(0);
		Vertex v1 = new Vertex(1);
		Vertex v2 = new Vertex(2);
		Vertex v3 = new Vertex(3);

		//Les sommets sont ici ajoutes aux graphe
		g.addVertex(v0);
		g.addVertex(v1);
		g.addVertex(v2);
		g.addVertex(v3);

		//les divers composants sont ici ajoutés au graphe.
		//Admittance a;

		g.addComponent(v0, v3, new VoltageGenerator("E0", v0, v3, 10));

		g.addComponent(v0, v1, new Admittance("Y0", v0, v1, 1));
		g.addComponent(v0, v2, new Admittance("Y0", v0, v2, 1));
		g.addComponent(v1, v2, new Admittance("Y0", v1, v2, 10000));
		g.addComponent(v1, v3, new Admittance("Y0", v1, v3, 2));
		g.addComponent(v2, v3, new Admittance("Y0", v2, v3, 2));

		//création de l'extracteur
		e = new Extractor(g);

	}
	
	@Override
	public void test()
	{
		System.out.println("Début de test : \n");
		long t = System.currentTimeMillis();

		//resolution du circuit
		try
		{
			e.extraction(false);
		}
		catch(PowerSupplyException e)
		{
			System.out.println(e.getMessage());
		}
		
		System.out.println("temps d'execution : "+(System.currentTimeMillis()-t)+"ms");
	}

	@Override
	public void after() 
	{
		//affichage du résultat
		e.printVariables();
		
		System.out.println("\nTest terminé !");
	}

}
