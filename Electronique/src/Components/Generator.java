package Components;

import Exceptions.AbstractDipoleError;

/**
 * Created by Raphaël on 21/02/2016.
 */
public class Generator extends AbstractDipole {
    public Generator(String name, Type t, int first_link, int second_link) {
        super(name, t, first_link, second_link);
    }
}
