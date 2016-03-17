package test;

import components.*;
import resolution.Extracteur;
import graphStructure.*;

import java.util.ArrayList;

/**
 * @author Raphaël
 */
public class test_extracteur {

    CircuitGraph g;

    public test_extracteur()
    {
        g = new CircuitGraph();

        //Partie expermentation
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);

        g.add_vertex(v0);
        g.add_vertex(v1);
        g.add_vertex(v2);
        g.add_vertex(v3);

        g.add_component(v0, v3, new VoltageGenerator("E", 0, 3, 10));
        g.add_component(v0, v1, new Admittance("Y0", 0, 1,1));
        g.add_component(v0, v2, new Admittance("Y1", 0, 2,1));
        g.add_component(v1, v2, new Admittance("Y2", 1, 2,1000000000));
        g.add_component(v1, v3, new Admittance("Y3", 1, 3,1));
        g.add_component(v2, v3, new Admittance("Y4", 2, 3,1));
        System.out.println("ducon");
       new Extracteur(g);
    }
}
