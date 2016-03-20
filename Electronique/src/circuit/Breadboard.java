package circuit;

import java.util.ArrayList;

import components.AbstractDipole;
import resolution.Solveur;

/**
 * Classe pour une breadboard. Endroit fictif où le circuit est réalisé puis le traduit sous forme de graphe pour la résolution
 * @author François
 */
public class Breadboard
{

/* ========================= */
/* Déclaration des attributs */
/* ========================= */
	
	/** liste des composants du circuit */
	private ArrayList<AbstractDipole> components;
	
	/** solveur du circuit*/
	private Solveur solveur;

/* =========================== */
/* Déclaration du constructeur */
/* =========================== */
	
	public Breadboard(ArrayList<AbstractDipole> components)
	{
		this.components=components;
		// TODO : this.solveur=new Solveur(volt, curr, adm, cg, eq);
	}

/* ======================== */
/* Déclaration des méthodes */
/* ======================== */
	
	/** Méthode faisant appel au solveur pour la résolution */
	public void compute()
	{
		// TODO
	}
	
	public String toString()
	{
		// TODO
		return "";
	}
}
