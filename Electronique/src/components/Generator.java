package components;

/** Classe abstraite des générateurs
 * @author Raphaël
 */
public abstract class Generator extends AbstractDipole {

    //etat du generateur
    protected boolean isActive = true;

    //etat de determination du parametre
    protected boolean determination;

    public Generator(String name, Type t, int first_link, int second_link) {
        super(name, t, first_link, second_link);
    }

    //foncion d'allumage du générateur
    public void turnOn() {
        isActive = true;
    }

    //fonction d'extinction du generateur
    public void turnOff() {
        isActive = false;
    }

    //getter d'etat du generateur
    public boolean isActive() {
        return isActive;
    }




}
