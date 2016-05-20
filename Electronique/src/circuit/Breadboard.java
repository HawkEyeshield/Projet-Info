package circuit;

import components.*;
import graphStructure.CircuitGraph;
import graphics.GraphicalComponent;
import graphics.Link;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
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

	/** Indique les noeuds deja utilises pour mettre tous les potentiels au mêmes points, empehce de boucler indefiniment*/
	public static ArrayList<GraphicalComponent> nodeAlreadyUsed = new ArrayList<GraphicalComponent>();

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
	 * @throws IllegalArgumentException exception levée si un problème de graphe est repéré par le solveur, doit être catch par l'interface graphique*/
	public void compute(ArrayList<Link> links) throws IllegalArgumentException
	{	
		// TODO Pour Sterenn : faire en sorte que la résolution se passe bien, catch des exceptions issues du solveur, renvoie des résultats à l'interface
		// Création du graphe pour la résolution
		CircuitGraph g = new CircuitGraph();
		
		// Création des liens entre composants
		//this.addLink(links);
		
		// Ajout des sommets et composants du graphe
		for(int i=0;i<components.size();i++)
		{
			// Attention ! Il faut d'abord ajouter les vertices au graphe, sinon il renvoie IllegalArgumentException lors de l'ajout du composant !
			g.addVertex(components.get(i).getFirstLink());
			g.addVertex(components.get(i).getSecondLink());
			
			// Si l'ajout de composant renvoie IllegalArgumentException, l'interface graphique doit le catch pour faire apparaître un message d'erreur
			g.addComponent(components.get(i).getFirstLink(), components.get(i).getSecondLink(), components.get(i));
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
	
	/**
	 * Méthode de traduction du circuit graphique vers le solveur
	 * @param links Liste des liens
     */
	public void breadboardLaunch(ArrayList<Link> links, AnchorPane anchorPane3, Text programmeLaunch, AnchorPane anchorPane4, Button Run){
		//La premiere partie permet de faire un parcourt de graphe et de mettre tous les fils relie au meme potentiel
		for(int i = 0 ; i < links.size() ; i++ ){//On regarde la liste des liens
			if(links.get(i).getImage1().getCtype() == Type.NULL && isNeverUsed(links.get(i).getImage1(),nodeAlreadyUsed)){
				//Quand on tombe sur un noeud pas encore considerer on met le lien au potentiel du noeud et on indique qu on a regarde ce noeud
				nodeAlreadyUsed.add(links.get(i).getImage1());
				links.get(i).setPotentielLink(links.get(i).getImage1().getCindexation());
				findLink(links,links.get(i).getImage1(),links.get(i).getImage1().getCindexation());//On cherche les liens autour
			}
			else if(links.get(i).getImage2().getCtype() == Type.NULL && isNeverUsed(links.get(i).getImage2(),nodeAlreadyUsed)){
				//Pareil avec l autre image
				nodeAlreadyUsed.add(links.get(i).getImage2());
				links.get(i).setPotentielLink(links.get(i).getImage2().getCindexation());
				findLink(links,links.get(i).getImage2(),links.get(i).getImage2().getCindexation());
			}
		}
		//TODO suite du programme main de la breadboard ici
	}

	/**
	 * Trouve tous les liens et les noeuds qui ont le même potentiel pour les mettre au même potentiel
	 * @param links Liste des liens
	 * @param node noeud considerer
     * @param i potentiel du noeud considerer
     */
	public void findLink(ArrayList<Link> links, GraphicalComponent node, int i){
		//On regarde la liste des liens
		for(int j = 0 ; j < links.size() ; j++ ) {
			if (links.get(j).getImage1() == node) {//Lorsqu'on trouve le noeud qui nous interesse
				if (links.get(j).getImage2().getCtype() != Type.NULL) {//si c'est pas un noeud on change le potentile du lien
					links.get(j).setPotentielLink(i);
				}
				else if (links.get(j).getImage2().getCtype() == Type.NULL && isNeverUsed(links.get(j).getImage2(),nodeAlreadyUsed)) {
					//si c'est un noeud on effectue la recherche sur se noeud
					links.get(j).setPotentielLink(i);//On met le lien au meme potentiel
					nodeAlreadyUsed.add(links.get(j).getImage2()); //on ajout a la liste des noeud deja utilise
					links.get(j).getImage2().setCindexation(i); // On met le nom au meme potentiel
					findLink(links, links.get(j).getImage2(),i); //On effectue recursivement la recherche
				}
			}
			else if (links.get(j).getImage2() == node) {
				if (links.get(j).getImage1().getCtype() != Type.NULL) {
					links.get(j).setPotentielLink(i);
				}
				else if (links.get(j).getImage1().getCtype() == Type.NULL && isNeverUsed(links.get(j).getImage1(),nodeAlreadyUsed)) {
					links.get(j).setPotentielLink(i);
					nodeAlreadyUsed.add(links.get(j).getImage1());
					links.get(j).getImage2().setCindexation(i);
					findLink(links, links.get(j).getImage1(),i);
				}
			}
		}
	}

	/**
	 * Indique si le composant est dans la liste donner
	 * @param node composant a utiliser
	 * @param nodeAlreadyUsed liste des composants
     * @return
     */
	public boolean isNeverUsed(GraphicalComponent node,ArrayList<GraphicalComponent> nodeAlreadyUsed){
		for(int i = 0 ; i < nodeAlreadyUsed.size(); i ++){
			if(nodeAlreadyUsed.get(i) == node){
				return(false);
			}
		}
		return true;
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
