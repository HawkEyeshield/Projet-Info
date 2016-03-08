package components;

/**
 * Enum listant les types de composant
 * Ne répertorie pas de fil (wire) pour que la résolution soit possible
 * @author François
 */
public enum Type {
	ADMITTANCE,
	VOLTAGE_GENERATOR,
	CURRENT_GENERATOR,
	NULL;
}
