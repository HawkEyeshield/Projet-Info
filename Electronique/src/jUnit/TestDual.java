package jUnit;


import components.Admittance;
import components.VoltageGenerator;
import graphStructure.CircuitGraph;
import graphStructure.Vertex;
import resolution.Extracteur;

/**
 * Classe définissant un test pour le pont de wheatstone
 * @author Raphaël
 */
public class TestDual extends AbstractUnit
{
	CircuitGraph g;
	Extracteur e;

	@Override
	public void setUp() {
		
		g = new CircuitGraph();
		//les sommets du graphe sont definis ici.
		Vertex v0 = new Vertex(0);
		Vertex v1 = new Vertex(1);


		//Les sommets sont ici ajoutes aux graphe
		g.addVertex(v0);
		g.addVertex(v1);


		//les divers composants sont ici ajoutés au graphe.
		//Admittance a;

		g.addComponent(v0, v1, new VoltageGenerator("E0", 0, 2, 10));

		g.addComponent(v0, v1, new Admittance("Y0", 0, 1, 1));


		//création de l'extracteur
		e = new Extracteur(g);

	}
	
	@Override
	public void test()
	{
		long t = System.currentTimeMillis();

		//resolution du circuit
		e.extraction(false);
		
		System.out.println("temps d'execution : "+(System.currentTimeMillis()-t)+"ms");
	}

	@Override
	public void after() 
	{
		//affichage du résultat
		e.printVariables();
	}

}
