package circuit;

import components.*;
import graphStructure.CircuitGraph;
import graphics.Component;
import graphics.Link;
import resolution.Extracteur;

import java.util.ArrayList;

/**
 * Classe pour une breadboard
 * Endroit fictif où le circuit est réalisé d'après l'interface graphique, puis traduit sous forme de graphe pour la résolution
 * @author François, Sterenn
 */

//TODO remarque de tanguy, le mieux serait que chaque composant soit mis avec son ImageView ce qui me permettra de rendre les valeurs en les affichant

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
		this.components = components;
	}

/* ======================== */
/* Déclaration des méthodes */
/* ======================== */
	
	/** Méthode faisant appel au solveur pour la résolution */
	public void compute()
	{	// TODO Pour Sterenn : faire en sorte que la résolution se passe bien, catch des exceptions issues du solveur, renvoie des résultats à l'interface
		CircuitGraph g = new CircuitGraph();
		for(int i=0;i<components.size();i++)
		{
			g.addComponent(components.get(i).getFirstLink(), components.get(i).getSecondLink(), components.get(i));
			g.addVertex(components.get(i).getFirstLink());
			g.addVertex(components.get(i).getSecondLink());
		}
		// La "clé" devient l'indice du composant dans la liste "components". En bijection avec les noms, donc.
		//Ne fonctionne que si on travaille avec une seule liste - un seul type ?

		Extracteur e = new Extracteur(g);
		e.extraction(false);
		e.printVariables();
	}
	
	/** Méthode ajoutant des composants (dans la breadboard) */
	public void addComponent(Component c)
	{
		// TODO Pour Sterenn : mettre en place la méthode d'ajout de composant, pour la breadboard et au sein de l'interface graphique

		if (c.getCtype().equals(Type.ADMITTANCE)) 
		{
            components.add(new Admittance(c.getCname(), null, null, c.getCvalue()));
        }
       else if (c.getCtype().equals(Type.CURRENTGENERATOR))
        {
            components.add(new CurrentGenerator(c.getCname(), null, null, c.getCvalue()));
        }
        else if (c.getCtype().equals(Type.VOLTAGEGENERATOR))
        {
            components.add(new VoltageGenerator(c.getCname(), null, null, c.getCvalue()));
        }
        else {
            throw new IllegalArgumentException("Composant inconnu");
        }
	}

    // TODO Pour Sterenn : mettre en place les liens entre composants,
        // voir si un ré-indexage des vertex serait nécessaire pour le solveur
	/** Méthode liant deux composants à partir du lien donné en paramètre
	 * @param l : le lien
	 */
    public void addLink(Link l)
    {
        Component A = l.getImage1();
        Component B = l.getImage2();
        
        // Parcours des composants connus pour récupérer les deux que l'on souhaite lier
        for (int i=0;i<components.size();i++)
        {
            AbstractDipole C1 = components.get(i);
            if (A.getCname().equals(C1.getName()))
            {
                for (int j=0;j<components.size();j++)
                {
                    AbstractDipole C2 = components.get(j);
                    if (B.getCname().equals(C2.getName()))
                    {
                        LinkAB(C1, C2);
                    }
                }
            }
        }
        //Else : exception = les composants ne sont pas encore arrivés/enregistés dans la breadboard/ont été supprimés.
          // et sinon on a supposé que personne n'a le même nom
    }
    
    /** Ajoute un lien entre deux composants donnés
     * @param a : premier composant
     * @param b : second composant
     */
    public void LinkAB(AbstractDipole a, AbstractDipole b)
	{
        b.setFirstLink(a.getSecondLink());
	}


    /**
     * Méthode supprimant un composant
     * @param c
     */
    public void deleteComponent(AbstractDipole c)
	{
		//TODO supprimer un composant c.
        for (int i=0;i<components.size();i++)
        {
            AbstractDipole C1 = components.get(i);
            if (C1.getName().equals(c.getName()))
            {
                components.remove(i); 
                //on a supprimé le composant abstractdipole dans la liste mais pas le composant Component...
                // Mais du coup comme ça on ne peut plus le relier à rien.
            }
        }
	}

	/**
	 * Méthode supprimmant des liens
     */

	public void deleteLink(Link l)
	{
        //Ne sert à rien si on met les liens et les composants à la fin : à partir du bouton "run"
        // (comme ça on tient compte seulement de la liste des liens ?)
	}

	/**
	 * Méthode d'affichage console de la breadboard
	 * @return le texte que l'on souhaite afficher
     */
	public String toString()
	{
		String string = "Connaissance actuelle du circuit : \n";
		//for(AbstractDipole a : components)
		{
			//string+=a.toString() + "\n";
		}
		return string;
	}
}
