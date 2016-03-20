package test;

import components.Admittance;
import components.VoltageGenerator;
import exceptions.AdmittanceError;
import graphStructure.CircuitGraph;
import graphStructure.Vertex;
import resolution.Extracteur;

/**
 * @author RaphaÃ«l
 */
public class epreuveExtracteur {

    CircuitGraph g;

    public epreuveExtracteur() {
        g = new CircuitGraph();

        //les sommets du graphe sont dï¿½finis ici.
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);


        //Les sommets sont ici ajoutï¿½s aux graphe
        g.addVertex(v0);
        g.addVertex(v1);
        g.addVertex(v2);


        //les divers composants sont ici ajoutés au graphe.
        Admittance a;

        g.addComponent(v0, v2, new VoltageGenerator("E0", 0, 2, 10));
        
        a = new Admittance("Y0", 0, 1);
        try {a.setCurrent(5.0);}
        catch (AdmittanceError e) {e.printStackTrace();return;}
        g.addComponent(v0, v1, a);

        a = new Admittance("Y0", 0, 1);
        try {a.setVoltage(5.0);}
        catch (AdmittanceError e) {e.printStackTrace();return;}
        g.addComponent(v1, v2, a);
        //g.addComponent(v1, v2, new Admittance("Y0", 0, 1, 1));



        //crï¿½ation de l'extracteur
        Extracteur e = new Extracteur(g);

        long t = System.currentTimeMillis();
        //resolution du circuit
        e.extraction(false);
        System.out.println("temps d'execution : "+(System.currentTimeMillis()-t)+"ms");
        //affichage du résultat

        e.printVariables();


    }
}

