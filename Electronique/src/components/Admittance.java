package Components;

import Exceptions.AdmittanceError;

/**
 * Classe pour les résistances
 * @author CF  LOL!!!!#JE_RIS
 */
public class Admittance extends AbstractDipole
{	
	//tableau de fixation des parametres : false = non determiné, true = determiné.
	//ordre des parametres : {Courant,Tension,Valeur}
	private boolean[] determination;

	/**
	 * Constructeur de resistances
	 * @param name nom du la resistance
	 * @param first_link entier désignant les liaisons communes avec le premier lien
	 * @param second_link entier désigant les liaisons communes avec le second lien 
	 */
	public Admittance(String name, int first_link, int second_link)
	{
		super(name, Type.ADMITTANCE, first_link, second_link);
		this.determination = new boolean[]{false,false,false};
		this.value = 0;
	}

	public Admittance(String name, int first_link, int second_link, double v)
	{
		super(name, Type.ADMITTANCE, first_link, second_link);
		this.determination = new boolean[]{false,false,true};
		this.value = v;
	}

	//getter de determination
	public boolean[] determination() {
		return determination;
	}

	//fonctions de récuperation des valeurs du composant

	//courant
	@Override
	public double getCurrent()	{
		if (determination[0])
			return this.current;
		else return 0;
	}

	//tension
	@Override
	public double getVoltage() {
		if (determination[1])
			return this.voltage;
		else return 0;
	}

	//valeur
	@Override
	public double getValue(){
		if (determination[2])
			return this.value;
		else return 0;
	}

	public double[][] getParameters() {
		double [][] ret = new double[3][2];
		for (int i=0;i<3;i++) {
			if (determination[i]) {
				System.out.println("hint");
				ret[i][0] = 1;
				switch(i) {
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
		System.out.println(ret[2][0]);
		return ret;
	}

	//fonctions de paramétrage des composants

	//courant
	@Override
	public void setCurrent(double c) throws AdmittanceError
	{
		if (!(determination[1]&&determination[2])) {
			this.voltage = c;
			determination[0] = true;
		}
		else throw new AdmittanceError(" Sur contrainte : la tension et l'admitance sont déja fixés");
	}

	//tension
	@Override
	public void setVoltage(double v) throws AdmittanceError
	{
		if (!(determination[0]&&determination[2])) {
			this.voltage = v;
			determination[1] = true;
		}
		else throw new AdmittanceError(" Sur contrainte : le courant et l'admitance sont déja fixés");
	}

	//valeur
	@Override
	public void setValue(double valeur) throws AdmittanceError
	{
		if (!(determination[0]&&determination[1])) {
			this.value = valeur;
			determination[2] = true;
		}
		else throw new AdmittanceError(" Sur contrainte : le courant et la tension sont déja fixés");
	}

}
