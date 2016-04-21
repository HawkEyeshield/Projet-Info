package circuit;

//import components.AbstractDipole;
import components.*;

import graphStructure.CircuitGraph;
import resolution.Extracteur;

import java.util.ArrayList;
import graphics.*;

/**
 * Classe pour une breadboard
 * Endroit fictif où le circuit est réalisé d'après l'interface graphique, puis traduit sous forme de graphe pour la résolution
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
	
	public Breadboard(ArrayList<Component> components)
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
		Extracteur e = new Extracteur(g);
		e.extraction(false);
		e.printVariables();
	}
	
	/** Méthode ajoutant des composants (dans la breadboard) */
	public void addComponent(Component c)
	{
		// TODO Pour Sterenn : mettre en place la méthode d'ajout de composant, pour la breadboard et au sein de l'interface graphique

		String name = c.getCname();
		double value = c.getCvalue();
		if (c.getCtype() == Type.ADMITTANCE ) {
            Admittance Adm = new Admittance(name, null, null, value );
            components.add(Adm);
        }
       // else ;{
        // }
	}

	/** Méthodes ajoutant des liens entre deux composants
	 * @param a : premier composant
	 * @param b : second composant */
    // TODO Pour Sterenn : mettre en place les liens entre composants, voire si un ré-indexage des vertex serait nécessaire pour le solveur

    public void addLink(Link l)
    {
        Component A = l.getImage1();
        Component B = l.getImage2();
        for (int i=0;i<components.size();i++){
            AbstractDipole C1 = components.get(i);
            if (A.getCname() == C1.getName()){
                for (int j=0;j<components.size();j++){
                    AbstractDipole C2 = components.get(j);
                    if (A.getCname() == C2.getName()){ LinkAB(C1, C2)
                    }
                }
            }
        }
        //Else : exception = les composants ne sont pas encore arrivés. 
    }

    public void LinkAB(AbstractDipole a, AbstractDipole b)
	{
        b.setFirstLink(a.getSecondLink());
	}


    /**
     * Méthode supprimmant un composant
     * @param c
     */
    public void deleteComponent(AbstractDipole c)
	{
		//TODO .
	}
	
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
