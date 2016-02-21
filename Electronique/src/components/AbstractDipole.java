package components;

import exceptions.AbstractDipoleError;

/**
 * Classe abstraite définisant les dipôles 
 * @author CF, Briztou, Tanguy
 */
public abstract class AbstractDipole 
{
	
	/** Nom donnée au composant pour affichage user-friendly*/
	protected String name;
	
	/**Type de composant*/
	private Type type;
	
	/**Entier indiquant à quels autres fils est relié le premier lien*/
	protected int first_link;

	/**Entier indiquant à quels autres fils est relié le second lien*/
	protected int second_link;

	/**Courant par défaut traversant les composants*/
	protected final double DEFAULT_CURRENT=0;

	/**Courant parcourant le dipôle*/
	protected double current;  //=DEFAULT_CURRENT

	/**Indique la différence de potentiel*/
	protected double voltage;

	/**value caractéristique du composant*/
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

	/**Getter de name
	 * @return le nom du composant */
	public String getName()
	{
		return this.name;
	}
	
	/**Setter de name
	 * @param name le nom que l'on souhaite donner au composant
	 */
	public void setName(String name)
	{
		this.name=name;
	}


	/**
	 * Getter de type
	 * @return le type du composant 
	 */
	 // il n'y a pas de mutabilité du type donc pas de setter
	public Type getType()
	{
		return this.type;
	}

	/**
	 * Getter de firstlink
	 * @return l'entier indiquant les liaisons communes
	 */
	public int getFirstLink()
	{
		return this.first_link;
	}
	
	/** Getter de secondLink
	 * @return l'entier indiquant les liaisons communes
	 */
	public int getSecondLink()
	{
		return this.second_link;
	}


	/**
	 * Setter de firstLink
	 * @param l l'entier à renseigner pour indiquer les liaisons communes
	 */
	public void setFirstLink(int l)
	{
		this.first_link=l;
	}
	
	/**
	 * Setter de secondLink
	 * @param l l'entier à renseigner pour indiquer les liaisons communes
	 */
	public void setSecondLink(int l)
	{
		this.second_link=l;
	}



	/*
	fonctions de récuperation des valeurs du composant
	Elles vont être redéfinies dans les classes filles. On ne fait ici que les définir.
	*/

	/**
	 * Getter de courant
	 * @return le courant traversant le dipôle
	 */
	public abstract double getCurrent();

	/**
	 * Getter de voltage
	 * @return la différence de potentiel aux bornes du dipôle
	 */
	public abstract double getVoltage();

	/**
	 * Getter de value
	 * @return la valeur caractéristique du composant
	 */
	public abstract double getValue();

	/*
	fonctions de paramétrage des composants
	pourront renvoyer des erreurs propres au composant héritant de la classe AbstractDipoleError
	*/

	/**
	 * Setter de current
	 * @param c le courant que l'on applique à travers le dipôle
	 * @throws AbstractDipoleError 
	 */
	public abstract void setCurrent(double c) throws AbstractDipoleError;

	/**
	 * Setter de voltage
	 * @param v la différence de potentiel aux bornes du dipôle
	 * @throws AbstractDipoleError
	 */
	public abstract void setVoltage(double v) throws AbstractDipoleError;

	/**
	 * Setter de value
	 * @param valeur valeur caractéristique que l'on souhaite donner au dipôle
	 * @throws AbstractDipoleError
	 */
	public abstract void setValue(double valeur) throws AbstractDipoleError;


	/**
	 * Méthode générant le texte par défaut pour l'affichage system.out.print
	 */
	@Override
	public String toString() 
	{
		return name+" "+type;
	}
}


