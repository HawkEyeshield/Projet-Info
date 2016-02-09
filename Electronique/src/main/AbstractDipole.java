package main;

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
	
	/**
	 * Type de composant
	 */
	private Type type;
	
	/**
	 * Entier indiquant à quels autres fils est relié le premier lien
	 */
	protected int first_link;

	/**
	 * Entier indiquant à quels autres fils est relié le second lien
	 */
	protected int second_link;

	/**
	 * Flottant indiquant la différence de potentielle
	 */
	protected float difference_potential;


	/**
	 * Courant par défaut traversant les composants
	 */
	protected final float DEFAULT_CURRENT=0;

	/**
	 * Courant parcourant les dipôles
	 */
	protected float current=DEFAULT_CURRENT;

	/**
	 * valeur caractéristique du composant
	 */
	private float valeur;
	


	/**
	 * Constructeur de cette classe, les classes filles préciseront les autres paramètres dans leurs constructeurs respectifs
	 * @param name Nom à donner au composant 
	 * @param first_link Entier à renseigner pour savoir à quels autres liens est connecté le premier fil
	 * @param second_link Entier à renseigner pour savoir à quels autres liens est connecté le second fil
	 */
	public AbstractDipole(String name, Type type, int first_link, int second_link, float valeur)
	{
		this.name=name;
		this.type=type;
		this.first_link=first_link;
		this.second_link=second_link;
		this.valeur = valeur;
	}
	
	
	
	
	/*
	public boolean tensionConnue(){
		return(this.)
	}
	
	*/
	/**
	 * Getteur du type
	 * @return le type du composant
	 */
	public Type getType(){
		return(this.type);
	}
	
	/**
	 * Getteur de la valeur caractéristique du composant
	 * @return la valeur caractéristique du composant
	 */
	public float getValue(){
		return(this.valeur);
	}
	
	/**
	 * Setteur du type
	 * @param le type du composant
	 */
	public void setType(Type type){
		this.type = type;
	}
	
	/**
	 * Setteur de la valeur caractéristique du composant
	 * @param la valeur caractéristique du composant
	 * @return 
	 */
	public void setValue(float valeur){
		this.valeur = valeur;
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
	 * Getteur de potential
	 * @return le potentiel du premier fil
	 */
	public float getPotential()
	{
		return this.difference_potential;
	}
	
	/**
	 * Setteur de first_potential
	 * @param potential potentiel à imposer au premier lien
	 */
	public void setFirstPotential(float potential)
	{
		this.difference_potential=potential;
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
