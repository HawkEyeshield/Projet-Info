package Components;

import Exceptions.CurrentGeneratorError;
import Exceptions.VoltageGeneratorError;

/**
 * Classe pour les générateurs
 * @author CF
 */
public class CurrentGenerator extends Generator
{
	/**
	 * Constructeur de générateurs de tension
	 * @param name nom du générateur
	 * @param first_link entier indiquant les liaions communes avec le premier lien
	 * @param second_link entier indiquant les liaisons communes avec le second lien
	 */
	public CurrentGenerator(String name, int first_link, int second_link) {
		super(name, Type.VOLTAGEGENERATOR, first_link, second_link);
	}

	public CurrentGenerator(String name, int first_link, int second_link, double v) {
		super(name, Type.VOLTAGEGENERATOR, first_link, second_link);
		this.voltage = v;
	}


	//Recuperation deu courant
	@Override
	public double getCurrent() {
		return this.current;
	}


	//fonctions de paramétrage des composants
	//tension

	@Override
	public void setVoltage(double v) throws CurrentGeneratorError
	{
		throw new CurrentGeneratorError("Sur contrainte : La tension ne peut etre fixé dans un générateur de courant");
	}

	//tension
	@Override
	public void setCurrent(double c) throws VoltageGeneratorError
	{
		this.current = c;
	}

	//valeur caracteristique

	//TODO Eventuellement parametrer la valeur caracteristique comme le courant délivré par le générateur, en discuter avec l'equipe.
	@Override
	public void setValue(double valeur) throws VoltageGeneratorError
	{
		throw new VoltageGeneratorError("Un générateur de courant n'a pas de valeur caracteristique");
	}

}

