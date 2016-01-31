package main;

/**
 * Classe pour les résistances
 * @author CF
 */
public class Resistor extends AbstractDipole 
{	
	private final float DEFAULT_VALUE=0;
	/**
	 * Valeur du composant
	 */
	private float value=DEFAULT_VALUE;
	
	/**
	 * Constructeur de resistances
	 * @param name nom du la resistance
	 * @param first_link entier désignant les liaisons communes avec le premier lien
	 * @param second_link entier désigant les liaisons communes avec le second lien 
	 * @param value valeur de la résistance
	 */
	public Resistor(String name, int first_link, int second_link, float value)
	{
		super(name, first_link, second_link);
		this.value=value;
	}
	
	/**
	 * Trouve le courant traversant la résistance 
	 * Prend une convention de ddp : first potential - second potential 
	 * @return le courant
	 */
	public float computeCurrent()
	{
		float c=(this.first_potential - this.second_potential)/this.value;
		this.current=c;
		return c;
	}
	/**
	 * Getteur de value
	 * @return la valeur du composant
	 */
	public float getValue()
	{
		return this.value;
	}
	
	/**
	 * Setteur de value
	 * @param value la valeur du composant que l'on souhaite utiliser
	 */
	public void setValue(float value)
	{
		this.value=value;
	}
	

}
