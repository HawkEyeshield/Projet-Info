package main;

//import java.util.ArrayList;

/*
 * Classe qui liste les différents composants et trouve leurs potentiel et courants
 * @author Olivier de Carglass
 */

/*
public class Breadboard
{
	private ArrayList<Admittance> listr;
	
	private ArrayList<Generator> listg;
	
	public Breadboard(ArrayList<Admittance> list, ArrayList<Generator> listg)
	{
		this.listr=list;
		this.listg=listg;
	}
	
	/**
	 * Etablit les potentiels
	 */
/*
	public void setPotentials()
	{
		for (int i=0;i<this.listg.size();i++)
		{
			for (int j=0;j<this.listr.size();j++)
			{
				if (this.listr.get(j).getFirstLink() == this.listg.get(i).getFirstLink())
				{
					this.listr.get(j).setFirstPotential(this.listg.get(i).getFirstPotential());
				}
				if (this.listr.get(j).getSecondLink() == this.listg.get(i).getSecondLink())
				{
					this.listr.get(j).setSecondPotential(this.listg.get(i).getSecondPotential());
				}
				if (this.listr.get(j).getFirstLink() == this.listg.get(i).getSecondLink())
				{
					this.listr.get(j).setFirstPotential(this.listg.get(i).getSecondPotential());
				}
				if (this.listr.get(j).getSecondLink() == this.listg.get(i).getFirstLink())
				{
					this.listr.get(j).setSecondPotential(this.listg.get(i).getFirstPotential());
				}
			}
		}
	}
	
	/**
	 * Méthode calculant les courants après avoir trouvé les potentiels
	 */

/*
	public void setCurrents()
	{
		for (int i=0; i<this.listr.size();i++)
		{
			this.listr.get(i).computeCurrent();
		}
	}
	
	public String toString()
	{
		String str="";
		
		for (int i=0; i<this.listg.size();i++)
		{
			str+= "Premier potentiel du générateur " + this.listg.get(i).getName() + " : ";
			str+= this.listg.get(i).getFirstPotential() + " V \n";
			str+= "Second potentiel du générateur  " + this.listg.get(i).getName() + " : ";
			str+=this.listg.get(i).getSecondPotential() + " V \n";
		}
		for (int i=0;i<listr.size();i++)
		{
			str+= "Premier potentiel de la résistance " + this.listr.get(i).getName() + " : ";
			str+= this.listr.get(i).getFirstPotential() + " V \n";
			str+= "Second potentiel de la résistance  "  +this.listr.get(i).getName() + " : ";
			str+= this.listr.get(i).getSecondPotential()+ " V \n";
			str+="Courant traversant la résistance : " + this.listr.get(i).getCurrent() + " A \n";
		}
		return str;
	}
}
*/