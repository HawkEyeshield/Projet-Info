package Resolution;

import Components.AbstractDipole;
import Components.Generator;
import org.jgrapht.graph.Multigraph;

import java.util.ArrayList;

/**
 * Created by Raphaël on 19/02/2016.
 */
public class Extracteur {

    public Multigraph<Integer, AbstractDipole> graph;

    public Extracteur(Multigraph<Integer, AbstractDipole> g) {
        graph = g;

        extraction();
    }

    private void extraction() {
        //First Step : get all generators
        //ArrayList<Generator> generateurs = graph.get_all_generators();

    }

}
