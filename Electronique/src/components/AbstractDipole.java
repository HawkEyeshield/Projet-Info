package components;

import exceptions.AbstractDipoleError;
import graphStructure.Vertex;

/**
 * Classe abstraite définisant les dipôles 
 * @author François, Sterenn
 */
public abstract class AbstractDipole 
{
	/* ========================= */
	/* Déclaration des attributs */
	/* ========================= */
	
	/** Nom donnée au composant pour affichage user-friendly*/
	protected String name;
	
	/** Nature du composant */
	protected Type type;
	
	/**Sommet auquel est relié la première patte */
	protected Vertex firstLink;

	/**Sommet auquel est remié la seconde patte */
	protected Vertex secondLink;

	/**Indice du dipole dans l'arrete ou il sera mis */
	public int index;

	/**Courant par défaut traversant les composants*/
	protected final double DEFAULT_CURRENT=0;

	/**Courant parcourant les dipôles*/
	protected double current;//=DEFAULT_CURRENT;

	/**double indiquant la différence de potentiel*/
	protected double voltage;

	/**value caractéristique du composant, aussi bien la valeur d'une admittance que le courant ou la tension d'un générateur*/
	protected double value;
	

	/* =========================== */
	/* Déclaration du constructeur */
	/* =========================== */

	/**
	 * Constructeur de cette classe, les classes filles préciseront les autres paramètres dans leurs constructeurs respectifs
	 * @param name : nom à donner au composant 
	 * @param firstLink : Sommet auquel est relié la première patte
	 * @param secondLink : Sommet auquel est relié la première patte
	 */
	public AbstractDipole(String name, Type type, Vertex firstLink, Vertex secondLink)
	{
		this.name=name;
		this.type=type;
		this.firstLink = firstLink;
		this.secondLink = secondLink;
	}

	
	/* ======================== */
	/* Déclaration des méthodes */
	/* ======================== */
	
	//Getters et Setters de name
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name=name;
	}


	//Getters de type : il n'y a pas de mutabilité du type donc pas de setter
	public Type type(){
		return(this.type);
	}

	// Getters de links
	public Vertex getFirstLink()
	{
		return this.firstLink;
	}
	public Vertex getSecondLink()
	{
		return this.secondLink;
	}


	// Setters de links, ne permet qu'une seule valeur
	public void setFirstLink(Vertex l)
	{
		if((this.firstLink==null))
		{
			this.firstLink =l;
		}
	}
	
	public void setSecondLink(Vertex l)
	{
		if(this.secondLink==null)
		{
			this.secondLink=l;
		}
	}



	//fonctions de récuperation des valeurs du composant
	//Elles vont etre redéfines dans les classes filles. On ne fait ici que les définir.

	//courant
	public double getCurrent()	{return 0;}

	//tension
	public double getVoltage() {return 0;}

	//value
	public double getValue(){return 0;}

	public abstract double[][] getParameters();

	//fonctions de paramétrage des composants
	//pourront renvoyer des erreurs propres au composant héritant de la classe AbstractdipoleError

	//courant
	public void setCurrent(double c) throws AbstractDipoleError{}

	//tension
	public void setVoltage(double v) throws AbstractDipoleError{}

	//value
	public void setValue(double valeur) throws AbstractDipoleError{}



	@Override
	public String toString() 
	{
		return name+" "+type;
	}
}


