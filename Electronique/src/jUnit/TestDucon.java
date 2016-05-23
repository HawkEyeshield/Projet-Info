package jUnit;


import components.Admittance;
import components.CurrentGenerator;
import components.VoltageGenerator;
import exceptions.AdmittanceError;
import exceptions.PowerSupplyException;
import graphStructure.CircuitGraph;
import graphStructure.Vertex;
import resolution.Extractor;

/**
 * Classe définissant un test pour l'utilisation de deux générateurs
 * @author Raphaël
 */
public class TestDucon extends AbstractUnit
{
	CircuitGraph g;
	Extractor e;

	@Override
	public void setUp() 
	{
		Vertex v0 = new Vertex(0);
		Vertex v1 = new Vertex(1);
		g = new CircuitGraph();

		//Les sommets sont ici ajoutes aux graphe
		g.addVertex(v0);
		g.addVertex(v1);


		//les divers composants sont ici ajoutés au graphe.
		//Admittance a;
		CurrentGenerator v = new CurrentGenerator("E0", v0, v1, 40);
		v.index = 0;
		g.addComponent(v0, v1, v);

		Admittance a = new Admittance("Y1", v0, v1);
		try {
			a.setVoltage(10);
		} catch (AdmittanceError admittanceError) {
			admittanceError.printStackTrace();
		}
		g.addComponent(v0, v1, a);


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
