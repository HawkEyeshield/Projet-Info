package exceptions;

/** Exception pour les admittances
 * @author Raphaël
 */

public class AdmittanceError extends AbstractDipoleError{
    
	private static final long serialVersionUID = 1L;

	public AdmittanceError(String m) 
	{
        super(m);
    }
}