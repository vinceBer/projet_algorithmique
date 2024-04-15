package exo2_sacMax;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MatchTailleVal implements Comparable<MatchTailleVal>{
	public static void main(String[] args) {
		//dans le main, ce sont simplement des teste, il ne servent à rien pour l'exo sacMax
		List<MatchTailleVal> tableau = new ArrayList<MatchTailleVal>();
		MatchTailleVal toto30 = new MatchTailleVal(10,30);
		MatchTailleVal toto3 = new MatchTailleVal(100,3);
		MatchTailleVal toto10 = new MatchTailleVal(5,50);
		
		tableau.add(toto30);
		tableau.add(toto3);
		tableau.add(toto10);
		
//		Collections.sort(tableau);
		tableau.sort(Comparator.comparing(MatchTailleVal::getValeur));
		tableau.sort(Comparator.comparing(MatchTailleVal::getTaille));
		tableau.sort(Comparator.comparing(MatchTailleVal::getRatio));
		
	}
	private int aTaille = 0;//attribut taille pour chaque objet du sac
	private int aValeur = 0;//attribut valeur pour chaque objet du sac

	  // other getters and setters omitted

	public MatchTailleVal(int pTaille, int pValeur) {//constructeur de la class MatchTailleVal
		this.setTaille(pTaille);//modifie la taille de l'objet
		this.setValeur(pValeur);//modifie la valeur de l'objet
	}
	
	public int getValeur() {//retourn la valeur de l'objet
		return this.aValeur;
    }
	
	public int getTaille() {//retourne la taille de l'objet
		return this.aTaille;
    }
	
	public float getRatio() {//retourne le ratio (densite) de la valeur et la taille
		return (float)this.aValeur/this.aTaille;
    }

	public void setValeur(int pValeur) {//modifie la valeur de l'objet
		this.aValeur = pValeur;
	}
	
	public void setTaille(int pTaille) {//modifie la taille de l'objet
		this.aTaille = pTaille;
	}
	  
	@Override public int compareTo(MatchTailleVal object) {//permet de comparer des objet MAtchTailleVal entre eux
		if (this.getValeur() == 0 || object.getValeur() == 0) {
			return 0;
		  }
		return this.getValeur() - object.getValeur();
	  }
}
