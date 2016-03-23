package jUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Classe abstraite pour les tests unitaires
 * @author francois
 */
public abstract class AbstractUnit 
{
	@Before
	public abstract void setUp();
	
	@Test
	public abstract void test();
	
	@After
	public abstract void after();
}
