package components;

import exceptions.VoltageGeneratorError;

/**
 * Classe pour les générateurs de tension
 * @author CF, Briztou
 */
public class VoltageGenerator extends Generator
{
	/**
	 * Constructeur de générateurs de tension
	 * @param name nom du générateur
	 * @param first_link entier indiquant les liaions communes avec le premier lien
	 * @param second_link entier indiquant les liaisons communes avec le second lien
	 */
	public VoltageGenerator(String name, int first_link, int second_link, double value) {
		super(name, Type.VOLTAGE_GENERATOR, first_link, second_link);
		this.voltage = value;
	}


	/**
	 * Getter de volage
	 * @return la différence de potentiel aux bornes du générateur
	 */
	public double getVoltage() 
	{
		return this.voltage;
	}


	//fonctions de paramétrage des composants
	
	/**
	 * Setter de current
	 * @param c le courant traversant le générateur
	 * @throws VoltageGeneratorError
	 */
	public void setCurrent(double c) throws VoltageGeneratorError
	{
		throw new VoltageGeneratorError("Sur contrainte : Le courant ne peut être fixé dans un générateur de tension");
	}

	/**
	 * Setter de voltage
	 * @param v la différence de potentiel que l'on impose aux bornes du générateur
	 * @throws VoltageGeneratorError
	 */
	public void setVoltage(double v) throws VoltageGeneratorError
	{
		this.voltage = v;
	}

	//valeur caracteristique

	//TODO Eventuellement parametrer la valeur caracteristique comme la tension délivrée par le générateur, en discuter avec l'equipe.
	/**
	 * Setter de value
	 * @param valeur a valeur caractéritstique du générateur
	 * @throws VoltageGeneratorError
	 */
	public void setValue(double valeur) throws VoltageGeneratorError
	{
		throw new VoltageGeneratorError("Un générateur de tension n'a pas de valeur caracteristique");
	}

}

