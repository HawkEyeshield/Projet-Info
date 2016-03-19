package components;

/** Classe abstraite des générateurs
 * @author Raphaël
 */
public abstract class Generator extends AbstractDipole {

    protected boolean isActive = true;
    protected boolean determination;

    public Generator(String name, Type t, int first_link, int second_link) {
        super(name, t, first_link, second_link);
    }

    public void turnOn() {
        isActive = true;
    }

    public void turnOff() {
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }




}
