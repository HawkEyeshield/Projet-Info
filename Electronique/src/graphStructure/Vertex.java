package graphStructure;

/**
 * Created by Rapha�l on 21/02/2016.
 */
public class Vertex {
    private int number;

    public Vertex() {
        number = 0;
    }

    public Vertex(int i) {
        number = i;
    }

    public void set (int i) throws IllegalArgumentException {
        if (i>=0) {
            number = i;
        }
        else throw new IllegalArgumentException("Indice n�gatif");
    }

    public int get() {
        return number;
    }
}
