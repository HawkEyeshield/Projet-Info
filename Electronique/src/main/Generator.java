package main;

/**
 * Classe pour les générateurs
 * @author CF
 */
public class Generator extends AbstractDipole
{
	/**
	 * Constructeur de générateurs de tension
	 * @param name nom du générateur
	 * @param first_link entier indiquant les liaions communes avec le premier lien
	 * @param second_link entier indiquant les liaisons communes avec le second lien
	 * @param p1 potentiel imposé par le générateur entre first_link et second_link
	 */
	public Generator(String name,Type type, int first_link, int second_link, float value)
	{
		super(name,Type.GENERATORTENSION, first_link, second_link,value);
		this.difference_potential=value;
	}
	
	
}
