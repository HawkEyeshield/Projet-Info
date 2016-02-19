package main;
import java.util.ArrayList;


/**
 * Ce graphe est la représentation du circuit éléctrique
 * @author tanguy
 *
 */
/*
public class Graphe {
	
	
	private AbstractDipole[][][] matrice;
	int nbreNoeud;
	*/
	/**
	 * On le représente par une matrice de dipole, on met aussi le nombre de noeud pour plus de clarté
	 * @param nombreDeNoeud
	 */
/*
	public Graphe (int nombreDeNoeud){
		matrice = new AbstractDipole [nombreDeNoeud][nombreDeNoeud][];
		nbreNoeud = nombreDeNoeud;
	}
	*/
	//TODO fonction qui permet de remplir le graphe en prennant en argument sans doute un tableau dégeu ( à voir avec l'ingterface graphique)
	
	//TODO faire une autre fonction qui permet d'ajouter et d'enlever un noeud(risque d'être chiant...)
	/**
	 * créer un AbstractDipole en placant un composant
	 * @param name nom du composant
	 * @param first_link entier indiquant les liaions communes avec le premier AbstractDipole
	 * @param second_link entier indiquant les liaisons communes avec le second AbstractDipole
	 * @param valeur dépend du composant
	 */

/*
	public void add_link(int first_link, int second_link, String type, String name, float valeur) throws IllegalArgumentException {
		
		if (first_link<0 || first_link >= nbreNoeud|| second_link<0 || second_link >= nbreNoeud)
			throw new IllegalArgumentException("Invalid node identifer");
		//"va te faire" si un de tes noeuds n'existent pas
		int taille = matrice[first_link][second_link].length;
		AbstractDipole [] a = new AbstractDipole [taille + 1];
		for (int i = 0 ; i < taille ; i++){
			a[i] = matrice[first_link][second_link][i];
			// flemme d'utiliser une array list, du coup on recréer le tableau des AbstractDipoles
		}
		if (type == "resistance"){
			a[taille] = new Generator(name,Type.GENERATORTENSION,first_link,second_link,valeur);
		}
		if (type == "generateurDeCourant"){
			a[taille] = new Resistor(name,Type.RESISTOR, first_link,second_link,valeur);
		}
		*/
		//TODO rajouter les générateurs de tension/courant
		/*
		if (type == 'n'){
			a[taille] = new fil();
		}
		*/

/*
		matrice[first_link][second_link] = a;
		
	}
	*/
	//TODO 3 fonctions qui seront utliser par raph, la première en cours
	//TODO fonction qui améliore les donner du graphe en regardant chaque composant
	// par exemple pour un generateur de tension, fixer la tension a ces bornes ainsi que noter que la tension est connue
	// on peut faire aussi de même pour la masse
	
	
	/**
	 * Fonction qui rend tous les liens existants entre i et j sous forme de tableau de Information
	 * Ce tableau doit contenir toute les infos pour le calcul
	 * @param i
	 * @param j
	 * @return
	 * @throws IllegalStateException si i ou j sont pas des bonnes valeurs
	 */

	/*
	public Information[] getInfoNode(int i, int j) throws IllegalStateException{
		
		if (i<0 || i >= nbreNoeud|| j<0 || j >= nbreNoeud) throw new IllegalStateException("une des deux valeurs n'est pas dans le graphe");
		Information [] a = new Information [matrice[i][j].length];
		for (int k = 0; k < matrice[i][j].length; k++){
			float courant = matrice[i][j][k].current; //courant de i vers j
			float t = matrice[i][j][k].getPotential();
			//TODO creer des fonctions qui déterminer ces booleans.
			boolean tensionConnue = ;
			boolean courantConnue = ;
			boolean impedanceConnue = ;
			int pN = i;
			int sN = j;
			Type tC = matrice[i][j][k].getType();
			String nom = matrice[i][j][k].getName();
			float vC = matrice[i][j][k].getValue();
			a[k] = new Information (courant,t,pN,sN,tC,nom,vC,tensionConnue,courantConnue,impedanceConnue);
		}
		return(a);
	}

	
}
*/