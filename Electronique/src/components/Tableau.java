package components;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * Created by Raphaël on 19/04/2016.
 */
public class Tableau<T> {
    private int size;
    public ArrayList<T>[][] tab;

    public Tableau(int n) {
        size = n;
        tab = (ArrayList<T>[][]) new ArrayList[size][size];
        for (int i = 0;i<size;i++)
            for (int j = 0;j<size;j++)
                tab[i][j] = new ArrayList<>();
    }

    public Tableau(int n, int[][] sizes, Supplier<T> var) {
        this(n);
        for (int x = 0; x < n; x++)
            for (int y = 0; y < n; y++) {
                for (int l = 0; l < sizes[x][y]; l++)
                    tab[x][y].add(var.get());
            }
    }

    public Tableau(int n, Supplier<T> var) {
        this(n);
        for (int x = 0;x<n;x++)
            for (int y = 0;y<n;y++) {
                tab[x][y].add(var.get());
            }
    }

    public int[][] sizeArray() {
        int[][] ret = new int[size][size];
        for (int i = 0;i<size;i++)
            for (int j = 0;j<size;j++)
                ret[i][j] = tab[i][j].size();
        return ret;
    }

    public ArrayList<T> get(int i, int j) {
        return tab[i][j];
    }

    public T get(int i, int j, int k) {
        return tab[i][j].get(k);
    }

    public T get(int[] id) {
        if (id.length!=3) System.out.println("ERREUR DE TAILLE POUR LA RECUP DE COUPLE! Recue : "+id.length+" , attendue : 3");
        return tab[id[0]][id[1]].get(id[2]);
    }

    public ArrayList<T> getA(int[] id) {
        if (id.length!=2) System.out.println("ERREUR DE TAILLE POUR LA RECUP D'ARRAYLIST ! Recue : "+id.length+" , attendue : 2");
        return tab[id[0]][id[1]];
    }

    public void set(int i, int j, ArrayList<T> a) {
        tab[i][j] = a;
    }

    //todo faire un set(i,j,k,valeur);

    public int size(int i, int j) {
        return tab[i][j].size();
    }

    public int size() {return size;}
}
