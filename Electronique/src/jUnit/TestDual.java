package jUnit;


import components.Admittance;
import components.VoltageGenerator;
import exceptions.PowerSupplyException;
import graphStructure.CircuitGraph;
import graphStructure.Vertex;
import resolution.Extracteur;

/**
 * Classe définissant un test pour l'utilisation de deux générateurs
 * @author Raphaël
 */
public class TestDual extends AbstractUnit
{
	CircuitGraph g;
	Extracteur e;

	@Override
	public void setUp() 
	{
		System.out.println("Ce test vérifie la capacité du logiciel à traiter des circuits avec plusieurs sources d'alimentation.");
		System.out.println("Un générateur de tension est crée entre les sommets 0 et 1, le second entre les sommets 0 et 2.");
		System.out.println("Une rsistance est placée entre les sommets 0 et 2, la seconde entre les sommets 1 et 2.");
		System.out.println("Elles valent respectivement 1/3 et 1 ohms.");
		System.out.println("On s'attend à récupérer les tensions des générateurs entre leurs sommets respectifs et des courants absolus de 20 et 40 ampères.");
		System.out.println("On devrait également obtenir un courant absolu de 60 et 20 ampères pour les résistances. \n");
		g = new CircuitGraph();
		//les sommets du graphe sont definis ici.
		Vertex v0 = new Vertex(0);
		Vertex v1 = new Vertex(1);
		Vertex v2 = new Vertex(2);


		//Les sommets sont ici ajoutes aux graphe
		g.addVertex(v0);
		g.addVertex(v1);
		g.addVertex(v2);


		//les divers composants sont ici ajoutés au graphe.
		//Admittance a;
		VoltageGenerator v = new VoltageGenerator("E0", v0, v1, 40);
		v.index = 0;
		g.addComponent(v0, v1, v);
		v = new VoltageGenerator("E1", v0, v2, 20);
		v.index = 1;
		g.addComponent(v0, v2, v);



		g.addComponent(v0, v2, new Admittance("Y1", v0, v2, 3));
		g.addComponent(v2, v1, new Admittance("Y2", v1, v2, 1));


		//création de l'extracteur
		e = new Extracteur(g);

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
