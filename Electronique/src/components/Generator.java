package components;

import exceptions.AbstractDipoleError;

/** Classe abstraite des générateurs
 * @author Raphaël
 */
public abstract class Generator extends AbstractDipole {

    protected boolean is_active = true;
    protected boolean determination;

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
