package jUnit;

import circuit.Breadboard;
import components.AbstractDipole;
import components.Admittance;
import components.VoltageGenerator;
import graphStructure.Vertex;

import java.util.ArrayList;

/**
 * Test unitaire pour l'appel du solveur par la breadboard
 * @author François
 */
public class TestBreadboard extends AbstractUnit
{
	private Breadboard b;
	
	@Override
	public void setUp() 
	{
		System.out.println("Ce test réalise une simulation de deux admittances en parallèles d'un générateur de tension.");
		System.out.println("Elles valent respectivement 0.1 et 0.2 ohms, le générateur délivrant 10 volts.");
		System.out.println("On s'attend à trouver dans les tableaux de résultats une tension de 10 volts entre les sommets 0 et 1.");
		System.out.println("Ainsi qu'un courant générateur absolu de 150 ampères, et un courant respectif absolu de 100 et 50 ampères pour les résistances.");
		ArrayList<AbstractDipole> list = new ArrayList<AbstractDipole>();
		Vertex v0 = new Vertex(0);
		Vertex v1 = new Vertex(1);
		list.add(new VoltageGenerator("G1",v0,v1, 10));
		list.add(new Admittance("R1", v0, v1, 10));
		list.add(new Admittance("R2", v0, v1, 5));
		b=new Breadboard(list);	
	}

	@Override
	public void test() 
	{
		System.out.println("Composants de la breadboard : \n");
		System.out.println(b.getComponents());
		try
		{
			b.compute(true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void after() 
	{
		System.out.println("Test terminé !");
	}

}
