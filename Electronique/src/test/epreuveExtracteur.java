package test;

import components.Admittance;
import components.VoltageGenerator;
import graphStructure.CircuitGraph;
import graphStructure.Vertex;
import resolution.Extracteur;

/**
 * @author Raphaël
 */
public class epreuveExtracteur {

    CircuitGraph g;

    public epreuveExtracteur() {
        g = new CircuitGraph();

        //les sommets du graphe sont d�finis ici.
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);


        //Les sommets sont ici ajout�s aux graphe
        g.addVertex(v0);
        g.addVertex(v1);
        g.addVertex(v2);

        //les divers composants sont ici ajout�s au graphe.
        g.addComponent(v0, v1, new VoltageGenerator("E0", 0, 2, 10));
        g.addComponent(v1, v2, new VoltageGenerator("E1", 2, 3, 10));
        g.addComponent(v0, v1, new Admittance("Y0", 0, 1, 1));
        g.addComponent(v0, v2, new Admittance("Y0", 0, 1, 1));

        //cr�ation de l'extracteur
        Extracteur e = new Extracteur(g);

        //resolution du circuit
        e.extraction(false);

        //affichage du r�sultat
        e.printVariables();


    }
}

