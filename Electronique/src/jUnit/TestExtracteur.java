package jUnit;

import components.Admittance;
import components.VoltageGenerator;
import exceptions.AdmittanceError;
import exceptions.PowerSupplyException;
import graphStructure.CircuitGraph;
import graphStructure.Vertex;
import resolution.Extractor;

/**
 * Classe de test de l'extracteur
 * @author Raphaël
 */
public class TestExtracteur extends AbstractUnit
{
	
	CircuitGraph g;
	
	//création de l'extracteur
    Extractor e;
    
	@Override
	public void setUp() 
	{
		System.out.println("Ce test réalise une simulation de deux admittances en série d'un générateur de tension.");
		System.out.println("Le générateur délivrant 10 volts entre les sommets 0 et 2.");
		System.out.println("La première résistance est contrainte avec un courant de 5 ampères, dont l'admittance est à déterminer par le solveur.");
		System.out.println("La seconde résistance est contrainte avec une tension de 4 volts, dont l'admittance est à déterminer par le solveur.");
		System.out.println("On s'attend à trouver dans les tableaux de résultats une tension de 10 volts entre les sommets 0 et 2.");
		System.out.println("Il devrait également y avoir 6 volts entre les sommets 0 et 1, ainsi que 4 volts entre les sommets 1 et 2.");
		System.out.println("Un courant générateur absolu de 5 ampères doit parcourir le générateur et les deux résistances.");
		System.out.println("Enfin, les résistances calibrées devraient être égales à 1,2 et 0,8 ohms. \n");
		g = new CircuitGraph();

		//Les sommets du graphe sont definis ici.
		Vertex v0 = new Vertex(0);
		Vertex v1 = new Vertex(1);
		Vertex v2 = new Vertex(2);

		//Les sommets sont ici ajoutes aux graphe
		g.addVertex(v0);
		g.addVertex(v1);
		g.addVertex(v2);

		//Ajout des generateurs
		g.addComponent(v0, v2, new VoltageGenerator("E0", v0, v2, 10));

		//Ajout des composants
		Admittance a;//init de la variable temporaire

		try {
            /*procedure d'ajout d'un composant parametré :
            a = new Admittance("Y0", 0, 1) :  creation d'un nouveau composant
            a.setCurrent(5.0) : parametrage du courant (existe aussi en  tension, et admittance)
            g.addComponent(v0, v1, a) : ajout du composant au graphe

            Vous pouvez aussi utiliser cette commande si vous voulez directement parametrer directement l'admittance sans vous embetter :
            g.addComponent(v0, v1, new Admittance("Y0", 0, 1,5)) //l'admittance parametree ets de 5 Mho
            */

			a = new Admittance("Y0", v0, v1);
			a.setCurrent(5.0);
			g.addComponent(v0, v1, a);

			a = new Admittance("Y0", v1, v2);
			a.setVoltage(4.0);
			g.addComponent(v1, v2, a);

		} catch (AdmittanceError e) {
			e.printStackTrace();
			return;
		}
		
		e = new Extractor(g);


	}

	@Override
	public void test() 
	{
		System.out.println("Début de test : \n");
		long t = System.currentTimeMillis();
		
        //resolution du circuit
		try
		{
			e.extraction(false);
		}
		catch(PowerSupplyException e)
		{
			System.out.println(e.getMessage());
		}
        
        System.out.println("temps d'execution : "+(System.currentTimeMillis()-t)+"ms");
		
	}

	@Override
	public void after() 
	{	
		//affichage du résultat
        e.printVariables();
        
        System.out.println("\nTest terminé !");
	}

	
}
