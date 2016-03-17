package components;

import exceptions.VoltageGeneratorError;

/**
 * Classe pour les générateurs de tension
 * @author François, Raphaël
 */
public class VoltageGenerator extends Generator
{
	/**
	 * Constructeur de générateurs de tension
	 * @param name nom du générateur
	 * @param first_link entier indiquant les liaions communes avec le premier lien
	 * @param second_link entier indiquant les liaisons communes avec le second lien
	 */
	public VoltageGenerator(String name, int first_link, int second_link) {
		super(name, Type.VOLTAGE_GENERATOR, first_link, second_link);
	}

	public VoltageGenerator(String name, int first_link, int second_link, double v) {
		super(name, Type.VOLTAGE_GENERATOR, first_link, second_link);
		this.voltage = v;
	}


	//Recuperation de la tension
	@Override
	public double getVoltage() {
		return this.voltage;
	}

	public double[][] getParameters() {
		double[][] ret = new double[3][2];
		if (determination) {
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

	//valeur caracteristique

	//TODO Eventuellement parametrer la valeur caracteristique comme la tension délivrée par le générateur, en discuter avec l'equipe.
	@Override
	public void setValue(double valeur) throws VoltageGeneratorError
	{
		throw new VoltageGeneratorError("Un générateur de tension n'a pas de valeur caracteristique");
	}

}

