package Components;

import Exceptions.AbstractDipoleError;

/**
 * Created by Raphaël on 21/02/2016.
 */
public class Generator extends AbstractDipole {

    private boolean is_active = true;

    public Generator(String name, Type t, int first_link, int second_link) {
        super(name, t, first_link, second_link);
    }

    public void turn_on() {
        is_active = true;
    }

    public void turn_off() {
        is_active = false;
    }

    public boolean is_active() {
        return is_active;
    }




}
