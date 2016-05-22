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
	private Admittance thirdAdmittance;
	
	@Override
	public void setUp() 
	{
		System.out.println("Ce test doit détecter des sur-contraintes sur les dipoles.");
		System.out.println("La première porte sur un générateur de tension dont on impose le courant.");
		System.out.println("La seconde porte sur un générateur de courant sont on fixe la tension.");
		System.out.println("Les deux suivantes vérifient qu'on ne puisse imposer à la fois courant et tension d'une résistance.");
		System.out.println("La dernière vérifie qu'on puisse imposer un courant et une tension aux bornes d'une résistance sans valeur. \n");
		voltage = new VoltageGenerator("V", null, null,10);
		current = new CurrentGenerator("C", null, null, 10);
		firstAdmittance = new Admittance("R1", null, null, 1);
		secondAdmittance = new Admittance("R2", null, null, 1);
		thirdAdmittance = new Admittance("R3", null, null);
	}

	@Override
	public void test() 
	{
		System.out.println("Début de test : \n");
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
		
		try
		{
			thirdAdmittance.setCurrent(10);
			thirdAdmittance.setVoltage(10);
			System.out.println("Aucune sur-contrainte détectée dans le dernier test, tout est OK !");
		}
		catch(Exception e)
		{
			System.out.println("Exception détectée anormale : " + e.getMessage());
		}
	}

	@Override
	public void after() 
	{
		System.out.println("\nTest terminé !");
	}

}
