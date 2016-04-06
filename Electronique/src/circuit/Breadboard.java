package circuit;

import java.util.ArrayList;

import components.AbstractDipole;
import components.Admittance;
import components.CurrentGenerator;
import components.VoltageGenerator;
import resolution.Solveur;

/**
 * Classe pour une breadboard. Endroit fictif où le circuit est réalisé puis le traduit sous forme de graphe pour la résolution
 * @author François
 */
public class Breadboard
{

/* ========================= */
/* Déclaration des attributs */
/* ========================= */
	
	/** liste des composants du circuit */
	private ArrayList<AbstractDipole> components;

/* =========================== */
/* Déclaration du constructeur */
/* =========================== */
	
	public Breadboard(ArrayList<AbstractDipole> components)
	{
		this.components=components;
	}

/* ======================== */
/* Déclaration des méthodes */
/* ======================== */
	
	/** Méthode faisant appel au solveur pour la résolution */
	public void compute()
	{
		// TODO
	}
	
	/** Méthode ajoutant des composants
	 * @param component : le composant à ajouter */
	public void addComponent(AbstractDipole component)
	{
		// TODO
		if(component instanceof CurrentGenerator)
		{
			components.add(new CurrentGenerator(component.getName(), component.getFirstLink(),component.getSecondLink()));
		}
		else if(component instanceof VoltageGenerator)
		{
			components.add(new VoltageGenerator(component.getName(), component.getFirstLink(), component.getSecondLink()));
		}
		else if(component instanceof Admittance)
		{
			components.add(new Admittance(component.getName(), component.getFirstLink(), component.getSecondLink()));
		}
	}
	
	/** Méthode ajoutant des liens entre composants
	 * @param a : premier composant
	 * @param b : second composant */
	public void addLink(AbstractDipole a, AbstractDipole b)
	{
		// TODO
	}
	
	public String toString()
	{
		// TODO
		return "";
	}
}
