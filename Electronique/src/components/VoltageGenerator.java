package components;

import exceptions.VoltageGeneratorError;

/**
 * Classe pour les générateurs de tension
 * @author François, Raphaël
 */
public class VoltageGenerator extends Generator
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
	public VoltageGenerator(String name, int firstLink, int secondLink) {
		super(name, Type.VOLTAGEGENERATOR, firstLink, secondLink);
	}

	public VoltageGenerator(String name, int firstLink, int secondLink, double v) {
		super(name, Type.VOLTAGEGENERATOR, firstLink, secondLink);
		this.voltage = v;
	}

/* ======================== */
/* Déclaration des méthodes */
/* ======================== */

	//Récupération de la tension
	@Override
	public double getVoltage() 
	{
		return this.voltage;
	}

	public double[][] getParameters() 
	{
		double[][] ret = new double[3][2];
		if (determination) 
		{
			ret[1] = new double[]{1,voltage};
		}
		return ret;
	}

	//fonctions de paramétrage des composants
	//courant
	@Override
	public void setCurrent(double c) throws VoltageGeneratorError
	{
		throw new VoltageGeneratorError("Sur contrainte : Le courant ne peut etre fixé dans un générateur de tension");
	}

	//tension
	@Override
	public void setVoltage(double v) throws VoltageGeneratorError
	{
		this.voltage = v;
	}

	//valeur caractéristique

	//TODO Eventuellement paramétrer la valeur caracteristique comme la tension délivrée par le générateur, en discuter avec l'équipe.
	@Override
	public void setValue(double valeur) throws VoltageGeneratorError
	{
		throw new VoltageGeneratorError("Un générateur de tension n'a pas de valeur caracteristique");
	}

	@Override
	public double getCurrent() 
	{
		return this.current;
	}

	@Override
	public double getValue() 
	{
		return this.value;
	}

}

