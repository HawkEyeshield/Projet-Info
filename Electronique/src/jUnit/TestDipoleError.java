package jUnit;

import components.Admittance;
import components.CurrentGenerator;
import components.VoltageGenerator;
import exceptions.AdmittanceError;
import exceptions.CurrentGeneratorError;
import exceptions.VoltageGeneratorError;

/**
 * Classe testant les exceptions concernant les dipoles et leurs contraintes électriques
 * @author François
 */
public class TestDipoleError extends AbstractUnit
{

	private VoltageGenerator voltage;
	private CurrentGenerator current;
	private Admittance firstAdmittance;
	private Admittance secondAdmittance;
	
	@Override
	public void setUp() 
	{
		voltage = new VoltageGenerator("V", null, null,10);
		current = new CurrentGenerator("C", null, null, 10);
		firstAdmittance = new Admittance("R1", null, null, 1);
		secondAdmittance = new Admittance("R2", null, null, 1);
	}

	@Override
	public void test() 
	{
		try
		{
			voltage.setCurrent(10);
		}
		catch(VoltageGeneratorError e)
		{
			System.out.println("Exception bien attrapée : " + e.getMessage());
		}
		
		try
		{
			current.setVoltage(10);
		}
		catch(CurrentGeneratorError e)
		{
			System.out.println("Exception bien attrapée : " + e.getMessage());
		}
		
		try
		{
			firstAdmittance.setCurrent(10);
			firstAdmittance.setVoltage(10);
		}
		catch(AdmittanceError e)
		{
			System.out.println("Exception bien attrapée : " + e.getMessage());
		}
		
		try
		{
			secondAdmittance.setVoltage(10);
			secondAdmittance.setCurrent(10);
		}
		catch(AdmittanceError e)
		{
			System.out.println("Exception bien attrapée : " + e.getMessage());
		}
	}

	@Override
	public void after() 
	{
		System.out.println("Test terminé !");
	}

}
