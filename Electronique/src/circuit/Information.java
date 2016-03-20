package circuit;

import components.Type;

/**
 * Classe qui represente les informations utilisées pour le calcul.
 * @author tanguy
 */
public class Information 
{ 
	//Placé de base comme étant un interrupteur ouvert
	boolean tensionConnue = false;
	boolean courantConnue = false;
	boolean impedanceConnue = false;
	float courant = 0;
	float tension = 0;
	Type typeComposant = Type.NULL;
	float valeurComposant = 0; //Admitance si c'est une résistance !
	int premierNoeud = -1;
	int secondNoeud = -1;
	
	public Information (float courant,float t, int pN,int sN,Type tC,String nom,float vC,boolean tensionConnue, boolean courantConnue, boolean impedanceConnue)
	{
		this.tensionConnue = tensionConnue;
		this.courantConnue = courantConnue;
		this.impedanceConnue = impedanceConnue;
		this.courant = courant;
		this.tension = t;
		this.premierNoeud = pN;
		this.secondNoeud = sN;
		this.typeComposant = tC;
		this.valeurComposant = vC;
	}
}
