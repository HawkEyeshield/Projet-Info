package components;

/**
 * Created by Raphaël on 19/04/2016.
 */
public class Couple {

    public boolean found;
    public double value;

    public Couple(boolean b, double val) {
        found = b;
        value = val;
    }

    public void set(boolean b,double val) {
        found = b;
        value = val;
    }
}
