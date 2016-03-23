package exceptions;

/** Exception pour les erreurs concernant les dipôles en général
 * @author Raphaël
 */
public class AbstractDipoleError extends Exception 
{
	private static final long serialVersionUID = 1L;

	public AbstractDipoleError(String m) 
	{
        super(m);
    }
}
