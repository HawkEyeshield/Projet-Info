package circuit;

import components.*;
import graphStructure.CircuitGraph;
import graphStructure.Vertex;
import graphics.GraphicalComponent;
import graphics.Link;
import resolution.Extracteur;
import java.util.ArrayList;

/**
 * Classe pour une breadboard
 * Endroit fictif où le circuit physique est réalisé d'après l'interface graphique, puis le traduit sous forme de graphe pour la résolution
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
	
	/** indice pour la création de nouveau vertices*/
	int vertexIndex=0;

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
	
	/** Méthode faisant appel au solveur pour la résolution 
	 * @param links liste des liens entre composants graphiques pour les traduire en liens physiques
	 * @throws IllegalArgumentException exception levée si un problème de graphe est repéré par le solveur*/
	public void compute(ArrayList<Link> links) throws IllegalArgumentException
	{	
		// TODO Pour Sterenn : faire en sorte que la résolution se passe bien, catch des exceptions issues du solveur, renvoie des résultats à l'interface
		CircuitGraph g = new CircuitGraph();
		
		for(int i=0;i<links.size();i++)
		{
			addLink(links.get(i));
		}
		
		for(int i=0;i<components.size();i++)
		{
			// Attention ! Il faut d'abord ajouter les vertices au graphe, sinon il renvoie IllegalArgumentException lors de l'ajout du composant !
			g.addVertex(components.get(i).getFirstLink());
			g.addVertex(components.get(i).getSecondLink());
			try
			{
				g.addComponent(components.get(i).getFirstLink(), components.get(i).getSecondLink(), components.get(i));
			}
			catch(IllegalArgumentException e)
			{
				System.out.println("Le circuit à un problème de sommet ! Veuillez le vérifier !");
				return;
			}
		}
		// La "clé" devient l'indice du composant dans la liste "components". En bijection avec les noms, donc.
		//Ne fonctionne que si on travaille avec une seule liste - un seul type ?

		Extracteur e = new Extracteur(g);
		e.extraction(false);
		e.printVariables();
	}
	
	/** Méthode ajoutant un composant physique dans la breadboard à partir de l'homologue graphique
	 *  @param graphical le composant que l'on souhaite ajouter 
	 *  */
	public void addComponent(GraphicalComponent graphical)
	{
		if (graphical.getCtype().equals(Type.ADMITTANCE)) 
		{
            components.add(new Admittance(graphical.getCname(), null, null, 1));
        }
       else if (graphical.getCtype().equals(Type.CURRENTGENERATOR))
        {
            components.add(new CurrentGenerator(graphical.getCname(), null, null, graphical.getCvalue()));
        }
        else if (graphical.getCtype().equals(Type.VOLTAGEGENERATOR))
        {
            components.add(new VoltageGenerator(graphical.getCname(), null, null, graphical.getCvalue()));
        }
	}


	/** Méthode liant deux composants à partir du lien donné en paramètre
	 * @param l : le lien
	 */
    public void addLink(Link l)
    {
        GraphicalComponent A = l.getImage1();
        GraphicalComponent B = l.getImage2();
        Vertex v = new Vertex(vertexIndex);
        vertexIndex++;
        
        
        //Else : exception = les composants ne sont pas encore arrivés/enregistés dans la breadboard/ont été supprimés.
          // et sinon on a supposé que personne n'a le même nom
    }

    /**
     * Méthode supprimant un composant physique à partir de son homologue graphique
     * @param graphical le composant que l'on souhaite supprimer
     */
    public void deleteComponent(GraphicalComponent graphical)
	{
        this.components.remove(this.getDipole(graphical));
	}
	
	/**
	 * Getter de components
	 * @return la liste des composants de la breadboard
	 */
	public ArrayList<AbstractDipole> getComponents()
	{
		return this.components;
	}
	
	/**
	 * Méthode renvoyant un composant physique à partir de son homologue graphique
	 * @param graphical le composant que l'on souhaite récupérer
	 * @return le composant physique correspondant à graphical
	 * @throws NullPointerException si aucun composant ne correspond
	 */
	public AbstractDipole getDipole(GraphicalComponent graphical) throws NullPointerException
	{
		for(int i=0;i<this.components.size();i++)
		{
			if(components.get(i).getName().equals(graphical.getCname()))
			{
				return components.get(i);
			}
		}
		throw new NullPointerException();
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
