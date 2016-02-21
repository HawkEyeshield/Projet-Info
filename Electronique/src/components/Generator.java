package components;

import exceptions.AbstractDipoleError; 

/**
 * @author Briztou
 *
 */
public class Generator extends AbstractDipole
{
	public Generator(String name, Type type, int first_link, int second_link) 
	{
		super(name, type, first_link, second_link);
	}

}