package circuit;

import components.AbstractDipole;
import graphStructure.CircuitGraph;
import resolution.Extracteur;

import java.util.ArrayList;

/**
 * Classe pour une breadboard
 * Endroit fictif où le circuit est réalisé d'après l'interface graphique, puis le traduit sous forme de graphe pour la résolution
 * @author François
 */

//TODO remarque de tanguy, le mieux serait que chaque composant soit mis avec son ImageView ce qui me permettra de rendre les valeurs en les affichants

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
		// TODO Pour Sterenn : faire en sorte que la résolution se passe bien, catch des exceptions issues du solveur, renvoie des résultats à l'interface
		CircuitGraph g = new CircuitGraph();
		for(int i=0;i<components.size();i++)
		{
			g.addComponent(components.get(i).getFirstLink(), components.get(i).getSecondLink(), components.get(i));
			g.addVertex(components.get(i).getFirstLink());
			g.addVertex(components.get(i).getSecondLink());
		}
		Extracteur e = new Extracteur(g);
		e.extraction(false);
		e.printVariables();
	}
	
	/** Méthode ajoutant des composants */
	public void addComponent(AbstractDipole c)
	{
		// TODO Pour Sterenn : mettre en place la méthode d'ajout de composant, pour la breadboard et au sein de l'interface graphique
		components.add(c);
	}
	
	/** Méthode ajoutant des liens entre deux composants
	 * @param a : premier composant
	 * @param b : second composant */
	public void addLink(AbstractDipole a, AbstractDipole b)
	{
		// TODO Pour Sterenn : mettre en place les liens entre composants, voir si un ré-indexage des vertex serait nécessaire pour le solveur
		b.setFirstLink(a.getSecondLink());
	}
	
	public String toString()
	{
		String string = "Connaissance actuelle du circuit : \n";
		for(AbstractDipole a : components)
		{
			string+=a.toString() + "\n";
		}
		return string;
	}
}
