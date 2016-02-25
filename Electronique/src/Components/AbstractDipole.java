package Components;

import Exceptions.AbstractDipoleError;

/**
 * Classe abstraite définisant les dipôles 
 * @author CF, Sterenn
 */
public abstract class AbstractDipole 
{
	/**
	 * Nom donnée au composant pour affichage user-friendly
	 */
	protected String name;
	
	//Type de composant

	private Type type;
	
	//Entier indiquant à quels autres fils est relié le premier lien
	protected int first_link;

	//Entier indiquant à quels autres fils est relié le second lien
	protected int second_link;

	//Courant par défaut traversant les composants
	protected final double DEFAULT_CURRENT=0;

	//Courant parcourant les dipôles
	protected double current;//=DEFAULT_CURRENT;

	//double indiquant la différence de potentielle
	protected double voltage;

	//value caractéristique du composant
	protected double value;
	


	/**
	 * Constructeur de cette classe, les classes filles préciseront les autres paramètres dans leurs constructeurs respectifs
	 * @param name Nom à donner au composant 
	 * @param first_link Entier à renseigner pour savoir à quels autres liens est connecté le premier fil
	 * @param second_link Entier à renseigner pour savoir à quels autres liens est connecté le second fil
	 */
	public AbstractDipole(String name, Type type, int first_link, int second_link)
	{
		this.name=name;
		this.type=type;
		this.first_link=first_link;
		this.second_link=second_link;
	}

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

	/**
	 * Getters de link
	 * @return l'entier indiquant les liaisons communes
	 */
	public int getFirstLink()
	{
		return this.first_link;
	}
	public int getSecondLink()
	{
		return this.second_link;
	}


	/**
	 * Setters de link
	 * @param l entier à renseigner pour indiquer les liaisons communes
	 */

	public void setFirstLink(int l)
	{
		this.first_link=l;
	}
	public void setSecondLink(int l)
	{
		this.second_link=l;
	}



	//fonctions de récuperation des valeurs du composant
	//Elles vont etre redéfines dans les classes filles. On ne fait ici que les définir.

	//courant
	public double getCurrent()	{return 0;}

	//tension
	public double getVoltage() {return 0;}

	//value
	public double getValue(){return 0;}

	//fonctions de paramétrage des composants
	//pourront renvoyer des erreurs propres au composant héritant de la classe AbstractdipoleError

	//courant
	public void setCurrent(double c) throws AbstractDipoleError{}

	//tension
	public void setVoltage(double v) throws AbstractDipoleError{}

	//value
	public void setValue(double valeur) throws AbstractDipoleError{}



	@Override
	public String toString() {
		return name+" "+type;
	}
}


