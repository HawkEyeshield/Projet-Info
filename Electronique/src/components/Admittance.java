package components;

import exceptions.AdmittanceError;
import graphStructure.Vertex;

/**
 * Classe pour les résistances
 * @author François, Raphaël
 */
public class Admittance extends AbstractDipole
{	
	/* ========================= */
	/* Déclaration des attributs */
	/* ========================= */
	
	/**tableau de fixation des parametres : false = non determiné, true = determiné.
	 * ordre des parametres : {Courant,Tension,Valeur}*/
	private boolean[] determination;

	/* ============================= */
	/* Déclaration des constructeurs */
	/* ============================= */
	
	/**
	 * Constructeur de resistances de valeur nulle
	 * @param name : nom du la resistance
	 * @param firstLink : entier désignant les liaisons communes avec le premier lien
	 * @param secondLink : entier désigant les liaisons communes avec le second lien
	 */
	public Admittance(String name, Vertex firstLink, Vertex secondLink)
	{
		super(name, Type.ADMITTANCE, firstLink, secondLink);
		this.determination = new boolean[]{false,false,false};
		this.value = 0;
	}
	
	/**
	 * Constructeur de resistances avec choix de sa valeur
	 * @param name : nom du la resistance
	 * @param firstLink : sommet désignant les liaisons communes avec le premier lien
	 * @param secondLink : sommet désigant les liaisons communes avec le second lien
	 * @param v : valeur de l'admittance
	 */
	public Admittance(String name, Vertex firstLink, Vertex secondLink, double v)
	{
		super(name, Type.ADMITTANCE, firstLink, secondLink);
		this.determination = new boolean[]{false,false,true};
		this.value = v;
	}
	
	/* ======================== */
	/* Déclaration des méthodes */
	/* ======================== */

	//getter de determination
	public boolean[] determination() 
	{
		return determination;
	}

	//fonctions de récuperation des valeurs du composant

	//courant
	@Override
	public double getCurrent()	
	{
		if (determination[0])
			return this.current;
		else return 0;
	}

	//tension
	@Override
	public double getVoltage() 
	{
		if (determination[1])
			return this.voltage;
		else return 0;
	}

	//valeur
	@Override
	public double getValue()
	{
		if (determination[2])
			return this.value;
		else return 0;
	}

	public double[][] getParameters() 
	{
		double [][] ret = new double[3][2];
		for (int i=0;i<3;i++) 
		{
			if (determination[i]) 
			{
				ret[i][0] = 1;
				switch(i) 
				{
					case 0:
						ret[i][1] = current;
						break;
					case 1:
						ret[i][1] = voltage;
						break;
					case 2:
						ret[i][1] = value;
						break;
				}
			}
			else {
				ret[i][0] = 0;
				ret[i][1] = 0;
			}
		}
		return ret;
	}

	//fonctions de paramétrage des composants

	//courant
	@Override
	public void setCurrent(double c) throws AdmittanceError
	{
		if (!(determination[1]&&determination[2])) 
		{
			this.current = c;
			determination[0] = true;
		}
		else throw new AdmittanceError("Sur contrainte : la tension et l'admitance sont déja fixés");
	}

	//tension
	@Override
	public void setVoltage(double v) throws AdmittanceError
	{
		if (!(determination[0]&&determination[2])) 
		{
			this.voltage = v;
			determination[1] = true;
		}
		else throw new AdmittanceError("Sur contrainte : le courant et l'admitance sont déja fixés");
	}

	//valeur
	@Override
	public void setValue(double valeur) throws AdmittanceError
	{
		if (!(determination[0]&&determination[1])) 
		{
			this.value = valeur;
			determination[2] = true;
		}
		else throw new AdmittanceError(" Sur contrainte : le courant et la tension sont déja fixés");
	}

}
