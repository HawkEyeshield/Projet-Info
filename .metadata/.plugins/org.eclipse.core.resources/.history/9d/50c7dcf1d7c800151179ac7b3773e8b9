package main;

/**
 * Classe abstraite définisant les dippôles 
 * @author CF
 */
public abstract class AbstractDipole 
{
	/**
	 * Nom donnée au composant pour affichage user-friendly
	 */
	protected String name;

	/**
	 * Entier indiquant à quels autres fils est relié le premier lien
	 */
	protected int first_link;

	/**
	 * Entier indiquant à quels autres fils est relié le second lien
	 */
	protected int second_link;

	/**
	 * Potentiel par défaut fixé pour les composants passifs
	 */
	protected final float DEFAULT_POTENTIAL=0;
	
	/**
	 * Flottant indiquant le potentiel du premier lien
	 */
	protected float first_potential=DEFAULT_POTENTIAL;

	/**
	 * Flottant indiquant le potentiel du second lien
	 */
	protected float second_potential=DEFAULT_POTENTIAL;


	/**
	 * Courant par défaut traversant les composants
	 */
	protected final float DEFAULT_CURRENT=0;

	/**
	 * Courant parcourant les dipôles
	 */
	protected float current=DEFAULT_CURRENT;


	/**
	 * Constructeur de cette classe, les classes filles préciseront les autres paramètres dans leurs constructeurs respectifs
	 * @param name Nom à donner au composant 
	 * @param first_link Entier à renseigner pour savoir à quels autres liens est connecté le premier fil
	 * @param second_link Entier à renseigner pour savoir à quels autres liens est connecté le second fil
	 */
	public AbstractDipole(String name, int first_link, int second_link)
	{
		this.name=name;
		this.first_link=first_link;
		this.second_link=second_link;
	}

	/**
	 * Getteur de name
	 * @return le nom du composant
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Setteur de name
	 * @param name le nom que l'on souhaite donner au composant
	 */
	public void setName(String name)
	{
		this.name=name;
	}
	
	/**
	 * Getteur de first_link
	 * @return l'entier indiquant les liaisons communes
	 */
	public int getFirstLink()
	{
		return this.first_link;
	}
	
	/**
	 * Setteur de first_link
	 * @param first_link entier à renseigner pour indiquer les liaisons communes
	 */
	public void setFirstLink(int first_link)
	{
		this.first_link=first_link;
	}
	
	/**
	 * Getteur de second_link
	 * @return l'entier indiquant les liaisons communes
	 */
	public int getSecondLink()
	{
		return this.second_link;
	}
	
	/**
	 * Setteur de second_link
	 * @param second_link entier à renseigner pour indiquer les liaisons communes
	 */
	public void setSecondLink(int second_link)
	{
		this.second_link=second_link;
	}
	
	/**
	 * Getteur de first_potential
	 * @return le potentiel du premier fil
	 */
	public float getFirstPotential()
	{
		return this.first_potential;
	}
	
	/**
	 * Setteur de first_potential
	 * @param first_potential potentiel à imposer au premier lien
	 */
	public void setFirstPotential(float first_potential)
	{
		this.first_potential=first_potential;
	}
	
	/**
	 * Getteur de second_potential
	 * @return le potentiel du second fil
	 */
	public float getSecondPotential()
	{
		return this.second_potential;
	}
	
	/**
	 * Setteur de second_potential
	 * @param second_potential potentiel à imposer au second lien
	 */
	public void setSecondPotential(float second_potential)
	{
		this.second_potential=second_potential;
	}
	
	/**
	 * Getteur de current
	 * @return le courant traversant le dipole
	 */
	public float getCurrent()
	{
		return this.current;
	}
	
	/**
	 * Setteur de current
	 * @param current le courant traversant le dipole
	 */
	public void setCurrent(float current)
	{
		this.current=current;
	}
}
