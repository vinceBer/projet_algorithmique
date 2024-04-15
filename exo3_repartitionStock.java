package exo3_repartitionStock;
import java.util.Arrays;
import java.util.Random;

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


public class exo3_repartitionStock {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		evalStat();
	}
	
	static int[][] estimations(int n, int H){ // retourne E[0:n][0:H+1] de terme g√©n√©ral
		// E[i][h] = e(i,h). Les estimations sont al√©atoires, croissantes selon h.
			int[][] E = new int[n][H+1];
			Random rand = new Random(); // pour g√©n√©ration al√©atoire des notes estim√©es.
			for (int i = 0; i < n; i++) E[i][0] = 6 + rand.nextInt(5);
			for (int i = 0; i < n; i++)
				for (int h = 1; h < H+1; h++)
					E[i][h] = min( E[i][h-1] + (1+rand.nextInt(5)), 100) ;
			return E;
		}

/* Exercice 3 : r√©partition optimale d'un stock S sur n entrep√¥ts
	m(k,s) : gain d'une r√©partition optimale d'un stock s sur le sous-ensemble
	des k premiers entrep√¥ts. */
	static int[][][] calculerMA(int[][] G){ // G[0:n][0:S+1] de terme g√©n√©ral 
	// G[i][s] = gain d'une livraison d'un stock s √  l'entrep√¥t i.
	// Calcule : M[0:n+1][0:S+1] de tg M[k][s] = m(k,s) et A = arg M.
	// Retourne : int[][][] MA = {M,A}.
		int n = G.length; int S = G[0].length - 1;
		int[][] M = new int[n+1][S+1], A =  new int[n+1][S+1];
		// Base : m(0,S) 0 et pour tout 0 =< s < S+1
		//init inutilie en java

		// cas g√©n√©ral: m(k,s)=max(m(k-1,s-s') + g(k-1,s')) 
		for (int k = 1; k < n+1; k++) {
			for (int s = 0; s < S+1; s++) {
				M[k][s] = Integer.MIN_VALUE;
				for (int sp = 0; sp < s+1; sp++) {
					int mkssp = M[k-1][s-sp] + G[k-1][sp];
					if(mkssp > M[k][s]) {
						M[k][s]=mkssp;
						A[k][s] = sp;
					}
				}
			}
		}
		return new int[][][] {M,A};
	}
	
	static int[] glouton(int[][] G){//recoit en parametre un tableau ‡ 2 dimensions (le prix des stocks et les diffÈrents entrepots)
		System.out.printf("on est au glouton\n");
		int nbEntrepot =G.length;
		int nbStocks = G[0].length;
		int h = 0;
		int k = 0;
		int[] listeNbStockParEntrepot = new int[nbEntrepot];
		int[] listeGainStockParEntrepot = new int[nbEntrepot];
		int diffMax = 0;
		int indiceDiffMax = 0;
		int diffEntrepot = 0;
		while(h < nbStocks-1) {//on parcours entrepotsMax (le nombre de stocks)
			indiceDiffMax = 0;
			diffMax = 0;
			//on veut augmenter les bÈnÈfices de maniËre gloutonne
			//donc on cherche simplement ‡ augmenter la marge faite par le vendeur stocks aprËs stocks
			//on veut donc avoir la diffÈrence de prix qu'on a entre vendre k et k+1 stocks 
			for(int i = 0; i < nbEntrepot; i++) {//parcours tous les entrepots
				//on recherche quel est la meilleur augmentation de profit et ‡ quel entrepot
				//on retient cette diffÈrence max(diffMax) et ‡ quel endroit (indiceDiffMax)
				
				if(listeNbStockParEntrepot[i] == 0 ) {
					diffEntrepot = G[i][listeNbStockParEntrepot[i]];
				}
				else {
					diffEntrepot = G[i][listeNbStockParEntrepot[i]] - G[i][listeNbStockParEntrepot[i]-1];
				}
				
				if(diffEntrepot > diffMax) {
					diffMax = diffEntrepot;
					indiceDiffMax = i;
				}
			}
			//System.out.printf("diffMax = %d, indiceDiffMax = %d\n",diffMax, indiceDiffMax);
			listeNbStockParEntrepot[indiceDiffMax] += 1;//une fois qu'on est sur de l'endroit o˘ il y a la meilleur augmentation,on le note
			h++;//puis on passe au stock suivant
		}
		//afficheTab(listeNbStockParEntrepot, "listeNbStockParEntrepot");
		System.out.printf("\n");
		while(k < nbEntrepot) {
			//on fait un tableau dans lequel il y a les diffÈrents gains effetues dans chacuns des entrepots
			listeNbStockParEntrepot[k] += -1;
			if(listeNbStockParEntrepot[k] == -1) {
				listeGainStockParEntrepot[k] = 0;
				k++;
			}
			else{
				listeGainStockParEntrepot[k] = G[k][listeNbStockParEntrepot[k]];
				k++;
			}
		}
		/*for(int j = 0; j < nbEntrepot; j++) {
			System.out.printf("listeGainStockParEntrepot[%d] = %d\n",j,listeGainStockParEntrepot[j]);
		}*/
		return listeGainStockParEntrepot;//on renvoie ce tableau des diffÈrents gains
	}
	
	static void aro(int[][] A, int k, int s) {
		if(k == 0) return;
		
		int aks = A[k][s];
		aro(A,k-1,s-aks);
		System.out.printf("n∞%d entrepot <--> %d stocks\n", k-1,aks);
	}
	
	static void afficheTab(int[] pTab, String pStr) {
		for(int i=0; i < pTab.length; i++) {
			System.out.printf(pStr+"[%d] = %d\n", i, pTab[i]);
		}
	}
	
	//la fonction suivante est appellÈe ‡ chaques tours 
	//elle appel elle mÍme la mÈthode gloutonne et la mÈthode optimale 
	//et renvoie la comparaison 
	static float chaqueTour(int nbENtrepot, int prixMaxStock, int numeroTour) {
		Random rand = new Random();
		//int nbENtrepot = rand.nextInt(1,100);
		//int prixMaxStock = rand.nextInt(1,100);
		int[][]G = estimations(nbENtrepot,prixMaxStock);
		int K = G.length, S = G[0].length - 1;
		//System.out.println("tableau des gain : g(k,s) = gain obtenu en livrant s √  k");
		//afficher(G);
		int[][][] MA = calculerMA(G);
		int[][] M = MA[0], A = MA[1];
		/*System.out.printf("gain total maximum : %d\n", M[K][S]);
		System.out.println("tableau M des gains maximum :");
		afficher(M); 
		System.out.println("une affectation optimale :");
		//aro(M,A,G,K,S);
		aro(A,K,S);*/
		
		int gainOptimal =  M[M.length-1][M[0].length-1];
		int[] listeGain = glouton(G);
		//afficheTab(listeGain,"Gain par entrepot ");
		int gainGlouton = somme(listeGain);
		float distRelative = (float)(gainOptimal - gainGlouton)/gainOptimal;
		
		System.out.printf("n∞%d :gainOptimal = %d, somme gainGlouton = %d, distRelative = %f\n",numeroTour, M[M.length-1][M[0].length-1], gainGlouton, distRelative);
		return distRelative;
	}
	
	static double[] tabDistRelative() {//renvoie un tableau de doubles (plus simples pour les histogrames)
		double[]DistanceRelative = new double[5000];//5000
		Random vRandom = new Random();
		int nbENtrepot = 0;
		int prixMaxStock = 0;
		int Vmax = 100;
		for(int i = 0; i < DistanceRelative.length; i++) {
			//on fixe un prix minimum d'un stock et prend un valeur aleatoire infÈrieur ‡ Vmax
			//on fixe un nb d'entrepots minimum  et prend un valeur aleatoire infÈrieur ‡ Vmax
			prixMaxStock = vRandom.nextInt(2,Vmax);
			nbENtrepot = vRandom.nextInt(2,Vmax);
			//nbENtrepot = 5; 
			DistanceRelative[i] = chaqueTour(nbENtrepot,prixMaxStock,i);//retourne une distance relative			
		}
		return DistanceRelative;//tableau de toutes les distances relatives 
	}
	
	static void evalStat() {//affiche la mÈdiane, la moyenne, l'Ècart-type et fait l'histogram
		double[]DistanceRelative = tabDistRelative();
		int n =DistanceRelative.length;
		float moyenneRelative = 0;
		float vMediane =0;
		float variance = 0;
		float vEcartType = 0;
		for(int i = 0; i < n; i++) {
			moyenneRelative += DistanceRelative[i];
		}
		moyenneRelative = moyenneRelative/n;
		
		Arrays.sort(DistanceRelative);
		
		if(n%2 == 0) {
    		vMediane = (float)(DistanceRelative[n/2] + DistanceRelative[n/2 +1])/2;
    	}
    	else {
    		vMediane = (float)DistanceRelative[n/2];
    	}
		
		for(int i=0; i< n;i++) {
    		variance += (DistanceRelative[i] - moyenneRelative)*(DistanceRelative[i] - moyenneRelative);
    	}
    	variance = variance/n;
    	vEcartType = (float)Math.sqrt(variance);
    	
    	System.out.printf("moyenneRelative = %f, MedianeRelative = %f, EcartTypeRelatif = %f\n", moyenneRelative, vMediane, vEcartType);
    	
    	String lFileName = "C:/Users/vince/eclipse-workspace/histograms/histogramStock.png";
        String lPlotTitle = "Histogram";
        String lxAxis = "Bins";
        String lyAxis = "Values";
        int lNbBins = 50;
        saveChart(DistanceRelative, lNbBins, lPlotTitle, lxAxis, lyAxis, lFileName);
	}

	/* fonctions annexe */
	static int max(int x, int y){ if (x >= y) return x; return y;}
	static int max(int x, int y, int z){ if (x >= max(y,z)) return x; 
		if (y >= z) return y; 
		return z;
	}	
	static int min(int x, int y){ if (x <= y) return x; return y;}
	static int min(int x, int y, int z){ if (x <= min(y,z)) return x; 
		if (y <= z) return y; 
		return z;
	}
	static int somme(int[] T){ int n = T.length;
		int s = 0; 
		for (int i = 0; i < n; i++) s = s + T[i];
		return s;
	}
	static void afficher(int[][] T){int n = T.length;
		for (int i = n-1; i >= 0; i--)
			System.out.printf("%d : " + Arrays.toString(T[i])+"\n",i);
	}
	
	//le code suivant ‡ ÈtÈ trouvÈ sur internet, il permet de faire les Histograms
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
} // end classe

/*
Exercice 3 : r√©partition optimale d'un stock
tableau des gain : g(k,s) = gain obtenu en livrant s √  k
[0, 12, 12, 14, 14, 15, 15, 15, 17, 17, 17]
[0, 10, 12, 12, 16, 16, 16, 16, 16, 16, 16]
[0, 10, 14, 14, 14, 14, 14, 14, 14, 16, 16]
[0, 14, 14, 14, 16, 16, 16, 16, 16, 16, 16]
[0, 10, 10, 12, 12, 13, 13, 14, 15, 16, 16]
[0, 8, 10, 10, 10, 12, 12, 14, 14, 14, 14]
[0, 5, 5, 7, 7, 10, 10, 12, 12, 13, 13]
gain total maximum : 77
tableau M des gains maximum :
[0, 14, 26, 36, 46, 56, 64, 69, 73, 75, 77]
[0, 14, 24, 34, 44, 52, 57, 61, 63, 65, 67]
[0, 14, 24, 34, 42, 47, 51, 53, 53, 55, 56]
[0, 14, 24, 32, 37, 39, 39, 41, 42, 44, 44]
[0, 10, 18, 23, 25, 25, 27, 28, 30, 30, 32]
[0, 8, 13, 15, 15, 17, 18, 20, 20, 22, 22]
[0, 5, 5, 7, 7, 10, 10, 12, 12, 13, 13]
[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
une affectation optimale :
entrep√¥t  0 : stock livr√© = 1, gain = 5
entrep√¥t  1 : stock livr√© = 2, gain = 10
entrep√¥t  2 : stock livr√© = 1, gain = 10
entrep√¥t  3 : stock livr√© = 1, gain = 14
entrep√¥t  4 : stock livr√© = 2, gain = 14
entrep√¥t  5 : stock livr√© = 2, gain = 12
entrep√¥t  6 : stock livr√© = 1, gain = 12
*/
