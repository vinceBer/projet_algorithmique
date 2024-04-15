package exo2_sacMax;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

public class exo2_sacMax {

	public static void main(String[] args) {
		//int[] V = {2,1,3,8,4}; 
		//int[] T = {1,1,2,4,3};
		int nbRuns = 5000;//algo très long et on récupère NaN de temps en temps 
		evalStat(nbRuns);
	}
	
	static float[] calculerRelative(int vNbCases) {
		float[]D = new float[2];
		Random vRandom = new Random();
		//initTabT crée un tableau un tableau de valeurs aléatoire pour le Tableaudes tailles
		//initTabV crée un tableau un tableau de valeurs aléatoire pour le Tableaudes valeurs
		//on aurait pu juste créé la fonction initTab et l'appeler 2 fois aulieu d'en créé 2
		//mais ceci était plus simple pour se repérer
		int[] T = initTabT(vNbCases,100);
		int[] V = initTabV(vNbCases,100);
		
		int n = V.length;
		//int tailleSac = 18;
		int tailleSac = vRandom.nextInt(100);;
		
		int[][] M = calculerM(V,T,tailleSac); //fonction qui calcul le tableau M
		int valOptimale = M[V.length][tailleSac];//on récupère la valeur optimale "finale"
		int maxGloutVal = gloutonVal(T,V,tailleSac);//on récupère la valeur max en utilisant la méthode gloutonne par valeur
		int maxGloutDens = gloutonDens(T,V,tailleSac);//on récupère la valeur max en utilisant la méthode gloutonne par densite
		//afficher(M);
		
		//System.out.println("V = " + Arrays.toString(V));
		//System.out.println("T = " + Arrays.toString(T));
		//System.out.printf("M[%d][%d] = %d\n",V.length, C,M[V.length][C]);
		
		if (valOptimale != 0) {
		//System.out.printf("valOptimale = %d, maxGloutVal = %d, maxGloutDens = %d ",valOptimale, maxGloutVal,maxGloutDens);
			//on range la valeur de la distance relative avec la gloutonne par valeur à l'indice 0
			//on range la valeur de la distance relative avec la gloutonne par densite à l'indice 1
			D[0] = (float)(valOptimale - maxGloutVal)/valOptimale;
			D[1] = (float)(valOptimale - maxGloutDens)/valOptimale;
		}
		else {
			//en effet on ne peut pas diviser par 0 donc on renvoie 0
			D[0]=0;
			D[1]=0;
		}
		return D;
	}
	
	static float[][] evalDistRelative(int pNbRuns) {
		Random vRandom = new Random();
		 float [] vDistanceRes = new float[2];
		 //float [] vDistanceDens = new float [pNbRuns];
		 float[][] R = new float[2][pNbRuns];
		 int vNbVals = 0;
	    	for (int i=0; i<pNbRuns; i++){
	    		//on fait 5000 runs pour compléter le tableaudes distances relatives
	    		vNbVals = vRandom.nextInt(2,1000);
	    		vDistanceRes = calculerRelative(vNbVals);//on recoit un tableau avec les distances relatives par valeur et densite
	    		R[0][i] = vDistanceRes[0];//distance relative avec méthode des valeurs
	    		R[1][i] = vDistanceRes[1];//distance relative avec méthode des densites

	            //System.out.printf("distance relative Valeur: %f\n",vDistanceVal[i]);
	            //System.out.printf("distance relative Densité: %f\n",vDistanceVal[i]);
	        }

	    return R;
	}
	
	static void evalStat(int pNbRuns) {
		double[] DistDens = new double[pNbRuns];
		double[] DistVal = new double[pNbRuns];
		Arrays.sort(DistDens);//plus simple pour avoir la médiane
		Arrays.sort(DistVal);//plus simple pour avoir la médiane
		float moyDistVal = 0;
		float moyDistDens = 0;
		float medianeVal = 0;
		float medianeDens = 0;
		float varianceVal = 0;
		float varianceDens = 0;
		float ecartTypeVal = 0;
		float ecartTypeDens = 0;
		float[][] tabRelatives = evalDistRelative(pNbRuns);
		for(int j =0; j < pNbRuns; j++) {
			DistVal[j] = tabRelatives[0][j];//evalDistRelative(pNbRuns)[0][j];	
			DistDens[j] = tabRelatives[1][j];//evalDistRelative(pNbRuns)[1][j];
		}
		for(int i = 0; i < pNbRuns; i++) {
			moyDistVal += DistVal[i];
			moyDistDens += DistDens[i];
		}
		moyDistVal = moyDistVal/pNbRuns;//calcul de la Moyenne du tableau de la distance relative par valeurs
		moyDistDens = moyDistDens/pNbRuns;//calcul de la Moyenne du tableau de la distance relative par densite
		
		for(int i=0; i< pNbRuns;i++) {
			varianceVal += (DistVal[i] - moyDistVal)*(DistVal[i] - moyDistVal);
			varianceDens += (DistDens[i] - moyDistDens)*(DistDens[i] - moyDistDens);
    	}
		ecartTypeDens = (float) Math.sqrt(varianceDens);//ecat-type densite
		ecartTypeVal = (float) Math.sqrt(varianceVal);//ecart-type valeur
		
		if(pNbRuns%2 == 0) {//tableau si nb cases imapaire
			medianeDens = (float)(DistDens[pNbRuns/2] + DistDens[pNbRuns/2 +1])/2;
			medianeVal = (float)(DistVal[pNbRuns/2] + DistVal[pNbRuns/2 +1])/2;
    	}
    	else {//tableau si nb cases paire
    		medianeDens = (float)DistDens[pNbRuns/2];
    		medianeVal = (float)DistVal[pNbRuns/2];
    	}
		System.out.printf("moyDistVal : %f, medianeVal : %f, ecartTypeVal : %f\n", moyDistVal, medianeVal, ecartTypeVal);
		System.out.printf("moyDistDens : %f, medianeDens : %f, ecartTypeDens : %f\n", moyDistDens, medianeDens, ecartTypeDens);
		
		//permet d'avoir l'Histogram
		String lFileNameDens = "C:/Users/vince/eclipse-workspace/histograms/histogramSacMaxDens.png";
		String lFileNameVal = "C:/Users/vince/eclipse-workspace/histograms/histogramSacMaxVal.png";
        String lPlotTitle = "Histogram";
        String lxAxis = "Bins";
        String lyAxis = "Values";
        int lNbBins = 50;
        saveChart(DistDens, lNbBins, lPlotTitle, lxAxis, lyAxis, lFileNameDens);
        saveChart(DistVal, lNbBins, lPlotTitle, lxAxis, lyAxis, lFileNameVal);
	}
	
	static int[] initTabV(int pNbValeurs, int vMax){//initialise de manière aléatoire un tableau de pNbValeurs éléments
		Random vRandom = new Random();
		int[]V = new int[pNbValeurs];
		int nbRandom = 0;
        for (int i=0;i<pNbValeurs; i++){
        	nbRandom = vRandom.nextInt(2,vMax);
            V[i]=nbRandom; 
        }
        if(!checkvalid(pNbValeurs, V)) {
        	initTabT(pNbValeurs, vMax);
        }
        return V;
    }
	
	static int[] initTabT(int pNbValeurs, int vMax){//initialise de manière aléatoire un tableau de pNbValeurs éléments
		Random vRandom = new Random();
		int[]T = new int[pNbValeurs];
		int nbRandom = 0;
        for (int i=0;i<pNbValeurs; i++){
        	nbRandom = vRandom.nextInt(2,vMax);
            T[i]=nbRandom; 
        }
        if(!checkvalid(pNbValeurs, T)) {
        	initTabT(pNbValeurs, vMax);
        }
        return T;
    }
	
	static void afficher(int[][] M){ int n = M.length; // affichage du tableau M.
	 	System.out.println("\t[");
	 	for (int i = n-1; i>=0; i--) 
	 		System.out.println("\t\t" + Arrays.toString(M[i]));
		System.out.println("\t]");
	}
	
	static int somme(int[] T){
		//calcule la somme des valeurs d'un tableau
		int s = 0; 
		for (int i = 0; i<T.length; i++) 
			s = s+T[i]; 
		return s;
	}
	
	static int[][] calculerM(int[] V, int[] T, int C){int n = V.length;
	// Retourne M[0:n+1][0:C+1], de terme gÃ©nÃ©ral M[k][c] = m(k,c)
		int[][] M = new int[n+1][C+1];
		// Base : m(0,c) = 0 pour toute contenance c, 0 â‰¤ c < C+1
		for (int c = 0; c < C+1; c++) M[0][c] = 0;
		// Cas gÃ©nÃ©ral, pour tous k et c, 1 â‰¤ k < n+1, 0 â‰¤ c < C+1,
		// m(k,c) = max(M[k-1][c], V[k-1] + M[k-1][c-T[k-1]])
		for (int k = 1; k < n+1; k++)
			for (int c = 0; c < C+1; c++) // calcul et mÃ©morisation de m(k,c)
				if (c-T[k-1] < 0) // le k-Ã¨me objet est trop gros pour entrer dans le sac 
					M[k][c] = M[k-1][c];
				else  
					M[k][c] = max(M[k-1][c], V[k-1]+M[k-1][c-T[k-1]]);
		return M;
	}	
	
	static int max(int x, int y){ //calcul le max de 2 valeurs
		if (x >= y) 
			return x; 
		return y; 
	}
	
	/*comme précisé dans le rapport nous avons décidé d'utiliser des objets
	 * car plus simple de trier les tableaux même si on aurait pu également réimplanter 
	 * les algorithmes de tris vus en cours (on voulait tester autre chose)*/
	
	static int gloutonVal(int[]T, int []V, int tailleSac) {
		int valeurMax =0;//on initiale la valeur maxiamale obtenue à 0
		//on crée une liste de Type MatchTailleVal (classe décrite sur une autre page)
		List<MatchTailleVal> tableau = new ArrayList<MatchTailleVal>();
		for(int i = 0; i < T.length; i++) {
			//de cette manière on associe chaque Taille de l'objet avec sa valeur 
			MatchTailleVal objetSac = new MatchTailleVal(T[i],V[i]);// liste de la taille de chaque object et de la valeur de chaque object
			tableau.add(objetSac);//puis on ajoute cet objet dans la liste
		}
		//Collections.sort(tableau);
		//de la manière suivante on tris la liste par valeurs d'objets en gardant associé sa taille 
		tableau.sort(Comparator.comparing(MatchTailleVal::getValeur));
		//System.out.printf("gloutonVal : tailleSac : %d, valeurMax : %d\n",tailleSac,valeurMax);
		
		for(int i = 0; i < T.length; i++) {
			/*si l'objet peut rentrer dans le sac, on l'ajoute
			 donc on retire sa taille à la taille qu'il reste dans le sac
			 et on ajoute la valeur de l'objet à la valeur totale du sac*/
			if(tailleSac - tableau.get(T.length - 1-i).getTaille() > 0) {	
				tailleSac -= tableau.get(T.length - 1-i).getTaille();
				valeurMax += tableau.get(T.length - 1-i).getValeur();
				//System.out.printf("gloutonVal : tailleSac : %d, valeurMax : %d\n",tailleSac,valeurMax);
			}
		}
//		System.out.printf("resultat gloutonVal: tailleSacRestant : %d, valeurMax : %d\n",tailleSac,valeurMax);
		return valeurMax;
		
	}
	
	static int gloutonDens(int[]T, int []V, int tailleSac) {
		//on initiale la valeur maxiamale obtenue à 0
		int valeurMax =0;
		//on crée une liste de Type MatchTailleVal (classe décrite sur une autre page)
		List<MatchTailleVal> tableau = new ArrayList<MatchTailleVal>();
		//de cette manière on associe chaque Taille de l'objet avec sa valeur 
		for(int i = 0; i < T.length; i++) {
			// liste de la taille de chaque object et de la valeur de chaque object
			MatchTailleVal objetSac = new MatchTailleVal(T[i],V[i]);
			tableau.add(objetSac);// on ajoute cet objet dans la liste
		}
		//Collections.sort(tableau);
		//permet de trier le tableau par densite de valeur tout en gardant la taille et la valeur d'un objet
		tableau.sort(Comparator.comparing(MatchTailleVal::getRatio));
		//System.out.printf("gloutonDens: tailleSac : %d, valeurMax : %d\n",tailleSac,valeurMax);
		for(int i = 0; i < T.length; i++) {
			//si l'ojet peut rentrer dans le sac, on retire sa taille à la taille restante dans le sac
			//et on ajoute sa valeur à la valeur du sac 
			if(tailleSac - tableau.get(T.length - 1-i).getTaille() > 0) {		
				tailleSac -= tableau.get(T.length - 1-i).getTaille();
				valeurMax += tableau.get(T.length - 1-i).getValeur();
				//System.out.printf("gloutonDens: tailleSac : %d, valeurMax : %d\n",tailleSac,valeurMax);
			}
		}
//		System.out.printf("resultat gloutonDens: tailleSacRest : %d, valeurMax : %d\n",tailleSac,valeurMax);
		return valeurMax;
		
	}
	
	static boolean checkvalid(int pNbVal, int[]tab) {//vérifie qu'il y ait au mimimum 1 valeur différente de 0 dans le tableau
		
    	for(int i=0; i < pNbVal; i++) {
    		if (tab[i] != 0) {
    			return true;
    		}
    	}
    	return false;
    }
	
	static void asm(int[][] M, int[] V, int[] T, int k, int c){
	// affichage d'un sac svm(k,c), sac de valeur maximum, de contenance c, contenant un 
	// un sous-ensemble de [0:k]. Appel principal : asm(M,V,T,n,C).
		if (k == 0) // svm(0,c) est vide. Sans rien faire, il a Ã©tÃ© affichÃ©.
			return; // svm(0,c) a Ã©tÃ© affichÃ©.
		// ici : k > 0
		if (M[k][c] == M[k-1][c]) // le k-Ã¨me objet n'est pas dans svm(k,c), 
		// donc svm(k,c) = svm(k-1,c). 
			asm(M, V, T, k-1, c) ; // svm(k-1,c) a Ã©tÃ© affichÃ©, donc svm(k,c) a Ã©tÃ© affichÃ©
		else {// le k-Ã¨me objet est dans le sac. Donc svm(k,c) = svm(k-1,c-t(k-1)) union {k-1}
			asm(M,V,T,k-1,c-T[k-1]); // svm(k-1,c-t(k-1)) a Ã©tÃ© affichÃ©
			System.out.printf("objet, valeur, taille = %d, %d, %d\n",
				k-1, V[k-1], T[k-1]); // Le k-Ã¨me objet Ã©tÃ© affichÃ©
			// svm(k-1,c-t(k-1)) union {k-1} a Ã©tÃ© affichÃ©, donc svm(k,c) a Ã©tÃ© affichÃ©.
		}
	}
	
	//le code suivant à été trouvé sur internet, il permet de faire les Histograms
	static void saveChart(double[] pvalues, int pNbBins, String pTitle, String pAxisX, String pAxisY, String pFileName) {
    	HistogramDataset dataset = new HistogramDataset();
    	dataset.setType(HistogramType.FREQUENCY);
    	dataset.addSeries("Hist", pvalues, 50); // Number of bins is 50
    	PlotOrientation orientation = PlotOrientation.VERTICAL;

    	boolean show = false;
    	boolean toolTips = false;
    	boolean urls = false;

    	JFreeChart chart = ChartFactory.createHistogram(pTitle, pAxisX, pAxisY, dataset, orientation, show, toolTips, urls);
    	chart.setBackgroundPaint(Color.white);

    	try {
    		ChartUtilities.saveChartAsPNG(new File(pFileName), chart, 600, 400);
    	}

    	catch (Exception e) {
    		System.out.println("Exception");            

    	}
    }
}
