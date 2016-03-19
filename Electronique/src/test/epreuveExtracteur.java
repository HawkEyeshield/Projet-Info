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

        //Partie expermentation
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(1);
        Vertex v3 = new Vertex(2);

        g.add_vertex(v0);
        //g.add_vertex(v1);
        g.add_vertex(v2);
        g.add_vertex(v3);

        g.add_component(v0, v2, new VoltageGenerator("E0", 0, 2, 10));
        g.add_component(v2, v3, new VoltageGenerator("E1", 2, 3, 10));
        //g.add_component(v0, v1, new Admittance("Y0", 0, 1, 1));
        //g.add_component(v1, v2, new Admittance("Y1", 1, 2, 1));
        g.add_component(v0, v2, new Admittance("Y1", 1, 2, 1));
        g.add_component(v2, v3, new Admittance("Y1", 2, 3, 1));
        System.out.println("ducon");
        Extracteur e = new Extracteur(g);

        e.printVariables();
    }
}

