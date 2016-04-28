package jUnit;


import components.Admittance;
import components.CurrentGenerator;
import components.VoltageGenerator;
import exceptions.AdmittanceError;
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
		Vertex v2 = new Vertex(2);


		//Les sommets sont ici ajoutes aux graphe
		g.addVertex(v0);
		g.addVertex(v1);
		g.addVertex(v2);


		//les divers composants sont ici ajoutés au graphe.
		//Admittance a;

		g.addComponent(v0, v2, new CurrentGenerator("E0", v0, v2, 20));
		Admittance a;
		try {

			a = new Admittance("Y0", v0, v1);
			a.setVoltage(-20);
			g.addComponent(v0, v1, a);

		} catch (Exception f) {f.printStackTrace();}


		g.addComponent(v1, v2, new Admittance("Y1", v0, v2, 1));


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
