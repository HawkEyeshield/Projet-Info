package circuit;

import components.*;
import exceptions.PowerSupplyException;
import graphStructure.CircuitGraph;
import graphStructure.Vertex;
import graphics.GraphicalComponent;
import graphics.GraphicalFunctions;
import graphics.Link;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import resolution.Extractor;

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
	private Extractor extractor;
	
	/** liste des sommets déjà ajoutés au graphe*/
	private ArrayList<Vertex> verticesUsed = new ArrayList<>();
	
	/** indice pour les admittances*/
	private int index=0;

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
	
	/** Méthode faisant appel au solveur pour la résolution via console
	 * @throws IllegalArgumentException exception levée si un problème de graphe est repéré par le solveur, doit être catch par l'interface graphique*/

    public void computeConsole() throws IllegalArgumentException
	{	
    	CircuitGraph gConsole = new CircuitGraph();
    	for(int i=0; i<GraphicalFunctions.listOfLink.size();i++)
    	{
    		Vertex v=new Vertex(i);
    		setDipoleLinks(GraphicalFunctions.listOfLink.get(i), v);
    	}

    	for(AbstractDipole a:components)
    	{
    		Vertex first = a.getFirstLink();
    		Vertex second = a.getSecondLink();

    		if(isAlreadyUsed(first))
    		{
    			a.setFirstLink(vertexToUse(first));
    			gConsole.addVertex(vertexToUse(first));
    		}
    		else
    		{
    			gConsole.addVertex(first);
    			verticesUsed.add(first);
    		}
    		if(isAlreadyUsed(second))
    		{
    			a.setSecondLink(vertexToUse(second));
    			gConsole.addVertex(vertexToUse(second));
    		}
    		else
    		{
    			gConsole.addVertex(second);
    			verticesUsed.add(second);
    		}
    		gConsole.addComponent(a.getFirstLink(), a.getSecondLink(), a);
    	}
    	extractor = new Extractor(gConsole);

        try
        {
        	extractor.extraction(false);
        }
        catch(IndexOutOfBoundsException ex)
        {
        	System.out.println("Problème de graphe : " + ex);
        }
        catch(PowerSupplyException p)
        {
        	System.out.println(p.getMessage());
        }
		extractor.printVariables();
	}
    
    /** Méthode faisant appel au solveur pour la résolution via l'interface 
	 * @throws IllegalArgumentException exception levée si un problème de graphe est repéré par le solveur, doit être catch par l'interface graphique*/

    public void computeInterface(AnchorPane anchorPane3, Text programmeLaunch, AnchorPane anchorPane4, Button Run) throws IllegalArgumentException, IndexOutOfBoundsException, PowerSupplyException
    {	
    	CircuitGraph gConsole = new CircuitGraph();
    	for(int i=0; i<GraphicalFunctions.listOfLink.size();i++)
    	{
    		Vertex v=new Vertex(i);
    		setDipoleLinks(GraphicalFunctions.listOfLink.get(i), v);
    	}

    	for(AbstractDipole a:components)
    	{
    		Vertex first = a.getFirstLink();
    		Vertex second = a.getSecondLink();

    		if(isAlreadyUsed(first))
    		{
    			a.setFirstLink(vertexToUse(first));
    			gConsole.addVertex(vertexToUse(first));
    		}
    		else
    		{
    			gConsole.addVertex(first);
    			verticesUsed.add(first);
    		}
    		if(isAlreadyUsed(second))
    		{
    			a.setSecondLink(vertexToUse(second));
    			gConsole.addVertex(vertexToUse(second));
    		}
    		else
    		{
    			gConsole.addVertex(second);
    			verticesUsed.add(second);
    		}
    		gConsole.addComponent(a.getFirstLink(), a.getSecondLink(), a);
    	}
    	extractor = new Extractor(gConsole);

        extractor.extraction(false);
       
		extractor.printVariables();
		
		ArrayList<GraphicalComponent> result = new ArrayList<>();
		for(AbstractDipole a : components)
		{
			int k = a.getIndex()-1;
			int i = a.getFirstLink().getIndex();
			int j = a.getSecondLink().getIndex();

			if(a.getType().equals(Type.ADMITTANCE))
			{
				a=(Admittance) a;
				result.add(new GraphicalComponent(a.getName(), extractor.getAdmittance(i, j, k), extractor.getVoltage(i,j), extractor.getCurrent(i, j, k),getRelatedImage(a.getName()),getRelatedOrientation(a.getName())));
			}
			else if(a.getType().equals(Type.CURRENTGENERATOR))
			{
				a=(CurrentGenerator)a;
				result.add(new GraphicalComponent(a.getName(), a.getValue(), extractor.getVoltage(i, j), extractor.getGeneratorCurrent(k+1),getRelatedImage(a.getName()),getRelatedOrientation(a.getName())));
			}
			else if(a.getType().equals(Type.VOLTAGEGENERATOR))
			{
				a=(VoltageGenerator)a;
				result.add(new GraphicalComponent(a.getName(), a.getValue(), extractor.getVoltage(i, j), extractor.getGeneratorCurrent(k+1),getRelatedImage(a.getName()),getRelatedOrientation(a.getName())));
			}
		}
		GraphicalFunctions.showResult(anchorPane3,programmeLaunch,anchorPane4, result,Run);
	}
    
    /**
     * Méthode renvoyant l'orientation d'un composant graphique existant avec le nom donné
     * @param name le nom
     */
    public char getRelatedOrientation(String name)
    {
    	for(GraphicalComponent a : GraphicalFunctions.graphicalComponentsList)
    	{
    		if(a.getCname().equals(name))
    		{
    			return a.getCorientation();
    		}
    	}
    	return 'v';
    }
    
    /**
     * Méthode renvoyant l'imageView d'un composant graphique existant avec le nom donné
     * @param name le nom
     */
    public ImageView getRelatedImage(String name)
    {
    	for(GraphicalComponent a : GraphicalFunctions.graphicalComponentsList)
    	{
    		if(a.getCname().equals(name))
    		{
    			return a.getCimage();
    		}
    	}
    	return null;
    }
    
    /**
     * Méthode déterminant à quels composants concernés par le lien donné en argument associer le vertex donné en argument
     * @param link le lien
     * @param vertex le vertex
     */
    public void setDipoleLinks(Link link, Vertex vertex)
    {
    	boolean firstNode = link.getImage1().getCtype().equals(Type.NODE);
    	boolean secondNode = link.getImage2().getCtype().equals(Type.NODE);
    	
    	if( ! (firstNode || secondNode))
    	{
    		decideFirstDipoleArea(link, vertex);
    		decideSecondDipoleArea(link, vertex);
    		if(!verticesUsed.contains(vertex))
    		{
    			verticesUsed.add(vertex);
    		}
    	}
    	
    	else if(firstNode)
    	{
    		Vertex firstTemp = new Vertex(link.getImage1().getCindexation());
    		decideSecondDipoleArea(link, firstTemp);
    		if(!verticesUsed.contains(firstTemp))
    		{
    			verticesUsed.add(firstTemp);
    		}
    	}
    	else if(secondNode)
    	{
    		Vertex secondTemp = new Vertex(link.getImage2().getCindexation());
    		decideFirstDipoleArea(link, secondTemp);
    		if(!verticesUsed.contains(secondTemp))
    		{
    			verticesUsed.add(secondTemp);
    		}
    	}
    }
    
    /**
     * Méthode déterminant à quelle patte du premier composant du lien fixer un sommet
     * @param link le lien
     * @param vertex le sommet à lier au composant physique
     */
    public void decideFirstDipoleArea(Link link, Vertex vertex)
    {
    	if(link.getFirstArea()==1 || link.getFirstArea()==2)
		{
			getDipole(link.getImage1()).setFirstLink(vertex);
		}
		else
		{
			getDipole(link.getImage1()).setSecondLink(vertex);
		}
    }
    
    /**
     * Méthode déterminant si l'indice du vertex en argument a déjà été utilisé
     * @param vertex le sommet à tester
     */
    public boolean isAlreadyUsed(Vertex vertex)
    {
    	for(Vertex v : verticesUsed)
    	{
    		if(vertex.getIndex()==v.getIndex())
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Méthode renvoyant le vertex contenu dans verticesUsed de même indice que celui en argument 
     * @param vertex argument
     * @return le vertex unique à utiliser
     */
    public Vertex vertexToUse(Vertex vertex)
    {
    	for(Vertex v : verticesUsed)
    	{
    		if(vertex.getIndex()==v.getIndex())
    		{
    			return v;
    		}
    	}
    	return null;
    }
    
    /**
     * Méthode déterminant à quelle patte du second composant du lien fixer un sommet
     * @param link le lien
     * @param vertex le sommet à lier au composant physique
     */
    public void decideSecondDipoleArea(Link link, Vertex vertex)
    {
    	if(link.getSecondArea()==1 || link.getSecondArea()==2)
		{
			getDipole(link.getImage2()).setFirstLink(vertex);
		}
		else
		{
			getDipole(link.getImage2()).setSecondLink(vertex);
		}
    }
	
	/** Méthode ajoutant un composant physique dans la breadboard à partir de l'homologue graphique
	 *  @param graphical le composant que l'on souhaite ajouter 
	 *  */
	public void addComponent(GraphicalComponent graphical)
	{
		if (graphical.getCtype().equals(Type.ADMITTANCE)) 
		{
			Admittance a = new Admittance(graphical.getCname(),null,null);
            components.add(a);
        }
       else if (graphical.getCtype().equals(Type.CURRENTGENERATOR))
        {
            components.add(new CurrentGenerator(graphical.getCname(), null, null, graphical.getCvalue(),index));
            index++;
        }
        else if (graphical.getCtype().equals(Type.VOLTAGEGENERATOR))
        {
            components.add(new VoltageGenerator(graphical.getCname(), null, null, graphical.getCvalue(),index));
            index++;
        }
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
