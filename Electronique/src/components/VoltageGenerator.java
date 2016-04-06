package components;

import exceptions.VoltageGeneratorError;
import graphStructure.Vertex;

/**
 * Classe pour les générateurs de tension, hérite de AbstractGenerator
 * @author François, Raphaël
 */
public class VoltageGenerator extends AbstractGenerator
{
	/* ============================= */
	/* Déclaration des constructeurs */
	/* ============================= */
	
	/**
	 * Constructeur de générateurs de tension, par défaut nulle
	 * @param name : nom du générateur
	 * @param firstLink : Sommet auquel est relié la première patte
	 * @param secondLink : Sommet auquel est relié la première patte
	 */
	public VoltageGenerator(String name, Vertex firstLink, Vertex secondLink) 
	{
		super(name, Type.VOLTAGEGENERATOR, firstLink, secondLink);
	}
	
	/**
	 * Construxteur d'un générateur de tension de valeur fixée
	 * @param name : nom du générateur
	 * @param firstLink : entier indiquant les liaions communes avec le premier lien
	 * @param secondLink : entier indiquant les liaisons communes avec le second lien
	 * @param v : valeur de la tension
	 */
	public VoltageGenerator(String name, Vertex firstLink, Vertex secondLink, double v) 
	{
		super(name, Type.VOLTAGEGENERATOR, firstLink, secondLink);
		this.voltage = v;
	}
	
	/* ======================== */
	/* Déclaration des méthodes */
	/* ======================== */

	//Recuperation de la tension
	@Override
	public double getVoltage() 
	{
		return this.voltage;
	}


	//recuperation des parametres (seul la tension compte, et est donc fixble
	public double[][] getParameters() 
	{
		double[][] ret = new double[3][2];
		if (determination) 
		{
			ret[1] = new double[]{1, voltage};
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