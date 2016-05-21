package exceptions;

/**
 * Exception pour l'extracteur si aucun générateur n'est présent
 * @author François
 */
public class PowerSupplyException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public PowerSupplyException(String m)
	{
		super(m);
	}

}
