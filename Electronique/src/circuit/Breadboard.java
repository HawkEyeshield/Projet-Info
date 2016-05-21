package circuit;

import components.*;
import exceptions.PowerSupplyException;
import graphStructure.CircuitGraph;
import graphStructure.Vertex;
import graphics.*;
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
	
	/**Extracteur de la breadboard*/
	private Extracteur extractor;
	
	/** iste des sommets déjà instanciés*/
	private ArrayList<Vertex> verticesInstancied = new ArrayList<>();

	/** Indique les noeuds deja utilises pour mettre tous les potentiels au mêmes points*/
	//private ArrayList<GraphicalComponent> nodeAlreadyUsed = new ArrayList<GraphicalComponent>();

    
    private int nbComponents = GraphicalFunctions.graphicalComponentsList.size();
    
    /**Prégraphe*/
    private int[][] pregraphe = new int[nbComponents][4] ;


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
	 * @param console booléen indiquant si la résolution est purement via la console, le cas contraire étant avec l'interface graphique
	 * @throws IllegalArgumentException exception levée si un problème de graphe est repéré par le solveur, doit être catch par l'interface graphique*/

    public void compute(boolean console) throws IllegalArgumentException
	{	
//    	if(!console)
//    	{
//    		try
//    		{
//    			CircuitGraph g = buildGraph();
//    			extractor = new Extracteur(g);
//    		}
//    		catch(Exception e)
//    		{
//    			System.out.println("Impossible de résoudre : ");
//    			e.printStackTrace();
//    		}
//    	}
    	//else
    	{
    		CircuitGraph gConsole = new CircuitGraph();
    		for(int i=0; i<GraphicalFunctions.listOfLink.size();i++)
			{
    			Vertex v=new Vertex(i);
				getDipole(GraphicalFunctions.listOfLink.get(i).getImage1()).setFirstLink(v);
				getDipole(GraphicalFunctions.listOfLink.get(i).getImage2()).setSecondLink(v);
			}
    		
    		for(AbstractDipole a:components)
    		{
    			Vertex first = a.getFirstLink();
    			Vertex second = a.getSecondLink();
    			
    			if(!verticesInstancied.contains(first))
    			{
    				gConsole.addVertex(a.getFirstLink());
    				verticesInstancied.add(first);
    			}
    			if(!verticesInstancied.contains(second))
    			{
    				gConsole.addVertex(a.getSecondLink());
    				verticesInstancied.add(second);
    			}
    			gConsole.addComponent(a.getFirstLink(), a.getSecondLink(), a);
    		}
    		extractor = new Extracteur(gConsole);
    	}
        try
        {
        	extractor.extraction(false);
        }
        catch(IndexOutOfBoundsException ex)
        {
        	System.out.println("Le graphe est vide : " + ex);
        }
        catch(PowerSupplyException p)
        {
        	System.out.println(p.getMessage());
        }
		extractor.printVariables();
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
	
//    /** Méthode faisant correspondre un abstractDipole à un composant graphique
//     *
//     */
//    public AbstractDipole getAbstract(GraphicalComponent graphical) throws NullPointerException
//    {
//        if (graphical.getCtype().equals(Type.ADMITTANCE)) {
//            return new Admittance(graphical.getCname(), null, null, 1);
//        } else if (graphical.getCtype().equals(Type.CURRENTGENERATOR)) {
//            return new CurrentGenerator(graphical.getCname(), null, null, graphical.getCvalue());
//        } else if (graphical.getCtype().equals(Type.VOLTAGEGENERATOR)) {
//            return (new VoltageGenerator(graphical.getCname(), null, null, graphical.getCvalue()));
//        }
//
//        throw new NullPointerException();
//    }
//
//	
//	/**
//	 * Méthode de traduction du circuit graphique vers le solveur
//     * Pregraphe : tableau des arêtes  (une ligne par composant, les deux valeurs non nulles sont les vertex)
//     */
//
//	public int Intermediaire()
//	{
//		
//        int k = 1;
//        for (int i = 0; i < GraphicalFunctions.graphicalComponentsList.size(); i++ )
//        {
//            GraphicalComponent Composant = GraphicalFunctions.graphicalComponentsList.get(i);
//            /*On choisit on composant, on cherche ses voisins. 
//             * Le composant est reliés à deux vertex, les liens parcourus seront tous appartenant à l'un ou l'autre des vertex */
//
//
//            for (int a=0; a < GraphicalFunctions.listOfLink.size(); a++){
//                if (Composant.equals( GraphicalFunctions.listOfLink.get(a).getImage1()) ){
//                    if (this.pregraphe[i][GraphicalFunctions.listOfLink.get(a).getFirstArea()-1] == 0){
//                        findVertex(GraphicalFunctions.listOfLink.get(a).getImage2() , GraphicalFunctions.listOfLink.get(a).getSecondArea(), k);
//                        this.pregraphe[i][GraphicalFunctions.listOfLink.get(a).getFirstArea()-1] =k;
//                        k++;
//                    }
//                }
//                else if (Composant.equals( GraphicalFunctions.listOfLink.get(a).getImage2() )){
//                    if (this.pregraphe[i][GraphicalFunctions.listOfLink.get(a).getSecondArea()-1] == 0){
//                        findVertex(GraphicalFunctions.listOfLink.get(a).getImage1() , GraphicalFunctions.listOfLink.get(a).getFirstArea(), k);
//                        this.pregraphe[i][GraphicalFunctions.listOfLink.get(a).getSecondArea()-1] =k;
//                        k++;
//                    }
//                }
//            }
//        }
//        return k-1;
//     }
//
//    /** Pour Intermédiaire
//     * Méthode récursive pour remplir Pregraphe
//     * @param Image
//     * @param LinkArea
//     * @param k
//     */
//    public void findVertex (GraphicalComponent Image, int LinkArea, int k ) {
//        if (Image.getCtype() != Type.NULL) {
//            // On considère Type(Noeud graphique) = NULL
//            this.pregraphe[IndiceImage(Image)][LinkArea - 1] = k;
//        } else {
//            for (int a = 0; a < GraphicalFunctions.listOfLink.size(); a++) {
//                if (Image.equals( GraphicalFunctions.listOfLink.get(a).getImage1())) {
//                    if (GraphicalFunctions.listOfLink.get(a).getFirstArea() != LinkArea) {
//                        findVertex(GraphicalFunctions.listOfLink.get(a).getImage2(), GraphicalFunctions.listOfLink.get(a).getSecondArea(), k);
//                    }
//                } else if (Image.equals(GraphicalFunctions.listOfLink.get(a).getImage2())) {
//                    if (GraphicalFunctions.listOfLink.get(a).getSecondArea() != LinkArea) {
//                        findVertex(GraphicalFunctions.listOfLink.get(a).getImage1(), GraphicalFunctions.listOfLink.get(a).getFirstArea(), k);
//                    }
//                }            // On tombe sur un noeud. On trouve tous les composants reliés à ce noeud
//                // findVertex for composants in trouvés if Image.linkarea != LinkArea
//            }
//        }
//    }
//
//    public int IndiceImage (GraphicalComponent Image){
//        for (int l=0; l<GraphicalFunctions.graphicalComponentsList.size(); l++){
//            if (GraphicalFunctions.graphicalComponentsList.get(l).equals(Image) ){
//                return  l ;
//            }
//        }
//        return 0 ;
//    }
//
//    /** Construction du graphe équivalent à celui de l'interface graphique*/
//    public CircuitGraph buildGraph()
//    {
//		initPregraphe();
//        CircuitGraph g = new CircuitGraph();
//        int k = Intermediaire();
//        Vertex[] v = new Vertex[k];
//        for (int i =0; i<k; i++){
//            v[i] = new Vertex(i);
//            g.addVertex(v[i]);
//        }
//        for (int i=0; i<this.pregraphe.length; i++){
//            int u = 0;
//            int w = 0 ;
//            for (int j=0; j<4; j++){
//                if (this.pregraphe[i][j] != 0) {
//                    if (u==0){
//                        u = this.pregraphe[i][j];
//                    }
//                    else  {
//						w = this.pregraphe[i][j];
//                    }
//                }
//            }
//            g.addComponent ( v[u], v[w], getAbstract(GraphicalFunctions.graphicalComponentsList.get(i)));
//        }
//        return g;
//    }
//    
//    /**
//     * Initialise le tableau pregraphe avec des 0
//     */
//    public void initPregraphe() 
//    {
//		for (int i = 0; i < nbComponents; i++) 
//		{
//			for (int j = 0; j < 4; j++) 
//			{
//				this.pregraphe[i][j] = 0;
//			}
//		}
//	}

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
