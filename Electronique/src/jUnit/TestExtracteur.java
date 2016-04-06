package jUnit;

import components.Admittance;
import components.VoltageGenerator;
import exceptions.AdmittanceError;
import graphStructure.CircuitGraph;
import graphStructure.Vertex;
import resolution.Extracteur;

/**
 * Classe de test de l'extracteur
 * @author Raphaël
 */
public class TestExtracteur extends AbstractUnit
{
	
	CircuitGraph g;
	
	//création de l'extracteur
    Extracteur e;
    
	@Override
	public void setUp() {
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

			a = new Admittance("Y0", v0, v1);
			a.setVoltage(5.0);
			g.addComponent(v1, v2, a);

		} catch (AdmittanceError e) {
			e.printStackTrace();
			return;
		}
		
		e = new Extracteur(g);


	}

	@Override
	public void test() {
		
		long t = System.currentTimeMillis();
		
        //resolution du circuit
        e.extraction(false);
        
        System.out.println("temps d'execution : "+(System.currentTimeMillis()-t)+"ms");
		
	}

	@Override
	public void after() {
		
		//affichage du résultat
        e.printVariables();
		
	}

	
}
