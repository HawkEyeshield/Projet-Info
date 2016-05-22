package components;

import exceptions.CurrentGeneratorError;
import exceptions.VoltageGeneratorError;
import graphStructure.Vertex;

/**
 * Classe pour les générateurs de courant, hérite de AbstractGenerator
 * @author François, Raphaël
 */
public class CurrentGenerator extends AbstractGenerator
{
	/* ============================ */
	/* Déclaraion des constructeurs */
	/* ============================ */
	
	/**
	 * Constructeur de générateurs de tension
	 * @param name : nom du générateur
	 * @param firstLink : Sommet auquel est relié la première patte
	 * @param secondLink : Sommet auquel est relié la première patte
	 */
	public CurrentGenerator(String name, Vertex firstLink, Vertex secondLink) 
	{
		super(name, Type.CURRENTGENERATOR, firstLink, secondLink);
		determination = false;
	}

	public CurrentGenerator(String name, Vertex firstLink, Vertex secondLink, double v) 
	{
		super(name, Type.CURRENTGENERATOR, firstLink, secondLink);
		determination = true;
		this.current = v;
		this.value=v;
	}
	
	public CurrentGenerator(String name, Vertex firstLink, Vertex secondLink, double v,int index) 
	{
		super(name, Type.CURRENTGENERATOR, firstLink, secondLink);
		determination = true;
		this.current = v;
		this.value=v;
		this.index=index;
	}
	
	/* ======================== */
	/* Déclaration des méthodes */
	/* ======================== */
	
	//recuperation des parametres du generateur (seul le courant ets pertinent, donc fixé.
	public double[][] getParameters() 
	{
		double[][] ret = new double[3][2];
		if (determination) 
		{
			ret[0] = new double[]{1,current};
		}
		return ret;
	}

	//Recuperation du courant
	@Override
	public double getCurrent() 
	{
		return this.current;
	}
	
	public double getValue()
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

	//tension
	@Override
	public void setCurrent(double c) throws VoltageGeneratorError
	{
		this.current = c;
		this.value=c;
		determination = true;
	}
}