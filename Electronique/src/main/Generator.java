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
	 * @param p1 potentiel imposé par le générateur sur son premier lien
	 * @param p2 potentiel imposé par le générateur sur son second lien
	 */
	public Generator(String name,int first_link, int second_link, float p1, float p2)
	{
		super(name, first_link, second_link);
		this.first_potential=p1;
		this.second_potential=p2;
	}
	
	
}
