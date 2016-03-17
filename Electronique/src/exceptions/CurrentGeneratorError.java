package exceptions;

/** Exception concernant les erreurs sur les générateurs de courant
 * @author Raphaël
 */
public class CurrentGeneratorError extends AbstractDipoleError {
    
	private static final long serialVersionUID = 1L;

	public CurrentGeneratorError(String m) 
	{
        super(m);
    }
}
