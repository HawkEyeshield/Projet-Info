package components;

import exceptions.CurrentGeneratorError;
import exceptions.VoltageGeneratorError;

/**
 * Classe pour les générateurs
 * @author CF, Briztou
 */
public class CurrentGenerator extends Generator
{
	/**
	 * Constructeur de générateurs de tension
	 * @param name nom du générateur
	 * @param first_link entier indiquant les liaions communes avec le premier lien
	 * @param second_link entier indiquant les liaisons communes avec le second lien
	 */
	public CurrentGenerator(String name, int first_link, int second_link, double value) 
	{
		super(name, Type.CURRENT_GENERATOR, first_link, second_link);
		this.voltage = value;
	}


	/**
	 * Getter de current
	 * @return le courant traversant le générateur
	 */
	public double getCurrent() 
	{
		return this.current;
	}


	//fonctions de paramétrage des composants
	
	/**
	 * Setter de voltage
	 * @param v la différence de potentiel que l'on impose aux bornes du générateur
	 * @throws CurrentGeneratorError
	 */
	public void setVoltage(double v) throws CurrentGeneratorError
	{
		throw new CurrentGeneratorError("Sur contrainte : La tension ne peut etre fixé dans un générateur de courant");
	}

	/**
	 * Setter de current
	 * @param c le courant que l'on impose à travers le générateur
	 * @throws VoltageGeneratorError
	 */
	public void setCurrent(double c) throws VoltageGeneratorError
	{
		this.current = c;
	}

	//valeur caracteristique

	//TODO Eventuellement parametrer la valeur caracteristique comme le courant délivré par le générateur, en discuter avec l'equipe.
	/**
	 * Setter de value
	 * @param valeur la valeur caractéristique du générateur
	 * @throws VoltageGeneratorError
	 */
	public void setValue(double valeur) throws VoltageGeneratorError
	{
		throw new VoltageGeneratorError("Un générateur de courant n'a pas de valeur caracteristique");
	}

}

