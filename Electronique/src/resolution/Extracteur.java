package resolution;

import org.jgrapht.graph.Multigraph;

import components.AbstractDipole;
import components.Generator;

import java.util.ArrayList;

/**
 * @author Briztou
 */
public class Extracteur 
{

    public Multigraph<Integer, AbstractDipole> graph;

    public Extracteur(Multigraph<Integer, AbstractDipole> g) 
    {
        graph = g;

        extraction();
    }

    private void extraction() 
    {
        //First Step : get all generators
        //ArrayList<Generator> generateurs = graph.get_all_generators();

    }

}
