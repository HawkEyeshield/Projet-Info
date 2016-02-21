package main;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.graph.Multigraph;

import components.AbstractDipole;
import components.VoltageGenerator;

/**
 * Created by Rapha�l on 19/02/2016.
 */
public class CircuitGraph {

    public DirectedMultigraph<Integer, AbstractDipole> graph;

    public CircuitGraph() {

        graph = new DirectedMultigraph<Integer, AbstractDipole>(AbstractDipole.class);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        for (int i = 0;i<3;i++) {
            graph.addEdge(1, 2, new VoltageGenerator("ducon"+i, 1, 2, 2.0));

        }


        System.out.println(graph.edgeSet());
    }

    public void add_component(int vertex1,int vertex2, AbstractDipole composant) {
        graph.addEdge(vertex1,vertex2,composant);
    }

    public void add_vertex(int new_vertex) {
        graph.addVertex(new_vertex);
    }

    public static void main(String args[]) {
        new CircuitGraph();
    }
}
