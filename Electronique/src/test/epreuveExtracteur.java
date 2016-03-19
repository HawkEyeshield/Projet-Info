package test;

import components.Admittance;
import components.VoltageGenerator;
import graphStructure.CircuitGraph;
import graphStructure.Vertex;
import resolution.Extracteur;

/**
 * Created by Raphaël on 17/03/2016.
 */
public class epreuveExtracteur {

    CircuitGraph g;

    public epreuveExtracteur() {
        g = new CircuitGraph();

        //les sommets du graphe sont définis ici.
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Vertex v4 = new Vertex(4);

        //Les sommets sont ici ajoutés aux graphe
        g.addVertex(v0);
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        
        //les divers composants sont ici ajoutés au graphe.
        g.addComponent(v0, v1, new VoltageGenerator("E0", 0, 2, 10));
        g.addComponent(v1, v2, new VoltageGenerator("E1", 2, 3, 10));
        g.addComponent(v0, v4, new Admittance("Y0", 0, 1, 1));
        g.addComponent(v4, v1, new Admittance("Y0", 0, 1, 1));
        g.addComponent(v0, v3, new Admittance("Y0", 0, 1, 1));
        g.addComponent(v3, v1, new Admittance("Y0", 0, 1, 1));
        g.addComponent(v1, v2, new Admittance("Y1", 1, 2, 1));

        //création de l'extracteur
        Extracteur e = new Extracteur(g);

        //resolution du circuit
        e.extraction(false);

        //affichage du résultat
        e.printVariables();


    }
}

