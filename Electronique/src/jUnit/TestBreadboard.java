package jUnit;

import java.util.ArrayList;

import circuit.Breadboard;
import components.AbstractDipole;
import components.Admittance;
import components.VoltageGenerator;
import graphStructure.Vertex;

public class TestBreadboard extends AbstractUnit
{
	private Breadboard b;
	
	@Override
	public void setUp() 
	{
		ArrayList<AbstractDipole> list = new ArrayList<AbstractDipole>();
		Vertex v0 = new Vertex(0);
		Vertex v1 = new Vertex(1);
		list.add(new VoltageGenerator("G1",v0,v1, 15));
		list.add(new Admittance("R1", v0, v1, 10));
		list.add(new Admittance("R2", v0, v1, 2));
		b=new Breadboard(list);	
	}

	@Override
	public void test() 
	{
		System.out.println("Composants de la breadboard : \n");
		System.out.println(b.getComponents());
		try
		{
			b.compute();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void after() 
	{
		
	}

}
