package components;

import exceptions.CurrentGeneratorError;
import exceptions.VoltageGeneratorError;

/**
 * Classe pour les générateurs de courant
 * @author François, Raphaël
 */
public class CurrentGenerator extends Generator
{
	
/* ============================= */
/* Déclaration des constructeurs */
/* ============================= */
	
	/**
	 * Constructeur de générateurs de tension
	 * @param name nom du générateur
	 * @param firstLink entier indiquant les liaions communes avec le premier lien
	 * @param secondLink entier indiquant les liaisons communes avec le second lien
	 */
	public CurrentGenerator(String name, int firstLink, int secondLink) 
	{
		super(name, Type.CURRENTGENERATOR, firstLink, secondLink);
		determination = false;
	}

	public CurrentGenerator(String name, int firstLink, int secondLink, double v) 
	{
		super(name, Type.CURRENTGENERATOR, firstLink, secondLink);
		determination = true;
		this.current = v;
	}
	
/* ======================== */
/* Déclaration des méthodes */
/* ======================== */

	public double[][] getParameters() 
	{
		double[][] ret = new double[3][2];
		if (determination) {
			ret[0] = new double[]{1,current};
		}
		return ret;
	}

	//Récuperation du courant
	@Override
	public double getCurrent() 
	{
		return this.current;
	}


	//fonctions de paramétrage des composants
	//tension

	@Override
	public void setVoltage(double v) throws CurrentGeneratorError
	{
		throw new CurrentGeneratorError("Sur contrainte : la tension ne peut être fixée dans un générateur de courant");
	}

	//courant
	@Override
	public void setCurrent(double c) throws VoltageGeneratorError
	{
		this.current = c;
		determination = true;
	}

	//valeur caractéristique

	//TODO Eventuellement paramétrer la valeur caractéristique comme le courant délivré par le générateur, en discuter avec l'équipe.
	@Override
	public void setValue(double valeur) throws VoltageGeneratorError
	{
		throw new VoltageGeneratorError("Un générateur de courant n'a pas de valeur caracteristique");
	}

	@Override
	public double getVoltage() 
	{
		return this.voltage;
	}

	@Override
	public double getValue() 
	{
		return this.current;
	}

}

