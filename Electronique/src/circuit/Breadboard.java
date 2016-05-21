package circuit;

import components.*;
import graphStructure.CircuitGraph;
import graphStructure.Vertex;
import graphics.*;
import graphics.Link;
import resolution.Extracteur;
import java.util.ArrayList;

/**
 * Classe pour une breadboard
 * Endroit fictif où le circuit physique est réalisé d'après l'interface graphique, puis le traduit sous forme de graphe pour la résolution
 * @author François, Sterenn
 */

//TODO remarque de Tanguy, le mieux serait que chaque composant soit mis avec son ImageView ce qui me permettra de rendre les valeurs en les affichant

public class Breadboard
{

/* ========================= */
/* Déclaration des attributs */
/* ========================= */

	/** liste des composants du circuit */
	private ArrayList<AbstractDipole> components;
	
	/** indice pour la création de nouveau vertices*/
	int vertexIndex=0;

	/** Indique les noeuds deja utilises pour mettre tous les potentiels au mêmes points*/
	public static ArrayList<GraphicalComponent> nodeAlreadyUsed = new ArrayList<GraphicalComponent>();

    /**Prégraphe*/
    public static int nbComposants = GraphicalFunctions.Graphics.size();
    public static int[][] Pregraphe = new int[nbComposants][4] ;

	public void makePregraphe() {
		for (int i = 0; i < nbComposants; i++) {
			for (int j = 0; j < 4; j++) {
				this.Pregraphe[i][j] = 0;
			}
		}
	}

    /** Graphe*/
    CircuitGraph g = new CircuitGraph();

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
    /** Méthode faisant correspondre un abstractDipole à un composant graphique
     *
     */
    public AbstractDipole getAbstract (GraphicalComponent graphical) throws NullPointerException
    {
        if (graphical.getCtype().equals(Type.ADMITTANCE)) {
            return new Admittance(graphical.getCname(), null, null, 1);
        } else if (graphical.getCtype().equals(Type.CURRENTGENERATOR)) {
            return new CurrentGenerator(graphical.getCname(), null, null, graphical.getCvalue());
        } else if (graphical.getCtype().equals(Type.VOLTAGEGENERATOR)) {
            return (new VoltageGenerator(graphical.getCname(), null, null, graphical.getCvalue()));
        }

        throw new NullPointerException();
    }

	
	/**
	 * Méthode de traduction du circuit graphique vers le solveur
     * Pregraphe : tableau des arêtes  (une ligne par composant, les deux valeurs non nulles sont les vertex)
     */

	public int Intermediaire ()
	{
        int k = 1;
        for (int i = 0; i < GraphicalFunctions.Graphics.size(); i++ )
        {
            GraphicalComponent Composant = GraphicalFunctions.Graphics.get(i);
//On choisit on composant, on cherche ses voisins. Le composant est reliés à deux vertex, les liens parcourus seront
            //tous appartenant à l'un ou l'autre des vertex.


            for (int a=0; a < GraphicalFunctions.listOfLink.size(); a++){
                if (Composant == GraphicalFunctions.listOfLink.get(a).getImage1() ){
                    if (this.Pregraphe[i][GraphicalFunctions.listOfLink.get(a).getFirstArea()-1] == 0){
                        findVertex(GraphicalFunctions.listOfLink.get(a).getImage2() , GraphicalFunctions.listOfLink.get(a).getSecondArea(), k);
                        this.Pregraphe[i][GraphicalFunctions.listOfLink.get(a).getFirstArea()-1] =k;
                        k++;
                    }
                }
                else if (Composant == GraphicalFunctions.listOfLink.get(a).getImage2() ){
                    if (this.Pregraphe[i][GraphicalFunctions.listOfLink.get(a).getSecondArea()-1] == 0){
                        findVertex(GraphicalFunctions.listOfLink.get(a).getImage1() , GraphicalFunctions.listOfLink.get(a).getFirstArea(), k);
                        this.Pregraphe[i][GraphicalFunctions.listOfLink.get(a).getSecondArea()-1] =k;
                        k++;
                    }
                }
            }
        }
        return k-1;
     }

    /** Pour Intermédiaire
     * Méthode récursive pour remplir Pregraphe
     * @param Image
     * @param LinkArea
     * @param k
     */
    public void findVertex (GraphicalComponent Image, int LinkArea, int k ) {
        if (Image.getCtype() != Type.NULL) {
            // On considère Type(Noeud graphique) = NULL
            this.Pregraphe[IndiceImage(Image)][LinkArea - 1] = k;
        } else {
            for (int a = 0; a < GraphicalFunctions.listOfLink.size(); a++) {
                if (Image == GraphicalFunctions.listOfLink.get(a).getImage1()) {
                    if (GraphicalFunctions.listOfLink.get(a).getFirstArea() != LinkArea) {
                        findVertex(GraphicalFunctions.listOfLink.get(a).getImage2(), GraphicalFunctions.listOfLink.get(a).getSecondArea(), k);
                    }
                } else if (Image == GraphicalFunctions.listOfLink.get(a).getImage2()) {
                    if (GraphicalFunctions.listOfLink.get(a).getSecondArea() != LinkArea) {
                        findVertex(GraphicalFunctions.listOfLink.get(a).getImage1(), GraphicalFunctions.listOfLink.get(a).getFirstArea(), k);
                    }
                }            // On tombe sur un noeud. On trouve tous les composants reliés à ce noeud
                // findVertex for composants in trouvés if Image.linkarea != LinkArea
            }
        }
    }
    
    public int IndiceImage (GraphicalComponent Image){
        for (int l=0; l<GraphicalFunctions.Graphics.size(); l++){
            if (GraphicalFunctions.Graphics.get(l).equals(Image) ){
                return  l ;
            }
        }
        return 0 ;
    }
    /** Construction du graphe (nouveau compute) */
    public void Construct (){
		makePregraphe();
        int k = Intermediaire();
        Vertex[] v = new Vertex[k];
        for (int i =0; i<k; i++){
            v[i] = new Vertex(i);
            g.addVertex(v[i]);
        }
        for (int i=0; i<this.Pregraphe.length; i++){
            int u = 0;
            int w = 0 ;
            for (int j=0; j<4; j++){
                if (this.Pregraphe[i][j] != 0) {
                    if (u==0){
                        u = this.Pregraphe[i][j];
                    }
                    else  {
						w = this.Pregraphe[i][j];
                    }
                }
            }
            g.addComponent ( v[u], v[w], getAbstract( GraphicalFunctions.Graphics.get(i) ));
        }
    }

	/**
	 * Trouve tous les liens et les noeuds qui ont le même potentiel pour les mettre au même potentiel
	 */
	public void findLink(ArrayList<Link> links, GraphicalComponent node, int i){
		for(int j = 0 ; j < links.size() ; j++ ) {
			if (links.get(j).getImage1() == node) {
				if (links.get(j).getImage2().getCtype() != Type.NULL) {
					System.out.println("il y a un lien relié a la node qui est un composant");
					links.get(j).setPotentielLink(i);
				}
				else if (links.get(j).getImage2().getCtype() == Type.NULL && isNeverUsed(links.get(j).getImage2(),nodeAlreadyUsed)) {
					System.out.println("il y a un lien relié a la node qui est un noeud, on le emt au potentiel " + i);
					links.get(j).setPotentielLink(i);
					nodeAlreadyUsed.add(links.get(j).getImage2());
					findLink(links, links.get(j).getImage2(),i);
				}
			}
			else if (links.get(j).getImage2() == node) {
				if (links.get(j).getImage1().getCtype() != Type.NULL) {
					System.out.println("il y a un lien relié a la node qui est un noeud, on le emt au potentiel " + i);
					links.get(j).setPotentielLink(i);
				}
				else if (links.get(j).getImage1().getCtype() == Type.NULL && isNeverUsed(links.get(j).getImage1(),nodeAlreadyUsed)) {
					links.get(j).setPotentielLink(i);
					System.out.println("il y a un lien relié a la node qui est un composant");
					nodeAlreadyUsed.add(links.get(j).getImage1());
					findLink(links, links.get(j).getImage1(),i);
				}
			}
		}
	}

	/**
	 * Indique si le composant est dans la liste donneé
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
