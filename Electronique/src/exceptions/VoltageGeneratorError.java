package exceptions;

/** Exceptions pour les erreurs sur les générateurs de tension
 * @author Raphaël
 */
public class VoltageGeneratorError extends AbstractDipoleError {
   
	private static final long serialVersionUID = 1L;

	public VoltageGeneratorError(String m) 
	{
        super(m);
    }
}
