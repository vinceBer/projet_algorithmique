package exo4_tempTravail;
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

public class exo4_tempTravail {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		evalStat();
	}
	
	static int[][][] calculerMA(int[][] E){	// E : tableau des notes estimées.
		// E[0:n][0:H+1] est de terme général E[i][h] = e(i,h).
		// Retourne M et A : M[0:n+1][0:H+1] de terme général M[k][h] = m(k,h), somme maximum
		// des notes d'une répartition de h heures sur le sous-ensemble des k premières unités.
		int n = E.length, H = E[0].length - 1;
		int[][] M = new int[n+1][H+1], A = new int[n+1][H+1];
		// base, k = 0.
		int s0 = 0; // somme des notes pour 0 heure travaillée
		for (int i = 0; i < n; i++) 
			s0 = s0 + E[i][0];
		// Base : m(0,h) = s0 pour tout h, 0 ≤ h < H+1
		for (int h = 0; h < H+1; h++)
			M[0][h] = s0;
		// Cas général, 1 ≤ k < n+1 pour tout h, h, 0 ≤ h < H+1 :
		// m(k,h) = ( Max m(k-1, h - h_k) + e(k-1,h_k) sur h_k, 0 ≤ h_k < h+1 ) - e(k-1,0)
		// Calcul des valeur m(k,h) par k croissants et mémorisation dans le tableau M.
		// Calcul �  la volée des a(k,h) = arg m(k,h) et mémorisation dans le tableau A.
		for (int k = 1; k < n+1; k++) // par tailles k croissantes
			for (int h = 0; h < H+1; h++){ // calcul des valeurs m(k,h), 0 ≤� h < H+1
				// Calcul de M[k][h] = 
				// ( Max M[k-1][h-h_k] + e(k-1,h_k), h_k, 0 ≤ h_k < h+1 ) - e(k-1,0)  
				M[k][h] = -1; 
				for (int h_k = 0; h_k < h+1; h_k++){
					int mkhh_k = M[k-1][h - h_k] + E[k-1][h_k]; 
					if (mkhh_k > M[k][h]){
						M[k][h] = mkhh_k;
						A[k][h] = h_k; 
					}		
				}	
				// M[k][h] = (max M[k-1][h-h_k] + e(k-1,h_k), h_k, 0 ≤ h_k < h+1)
				M[k][h] = M[k][h] - E[k-1][0];  // M[k][h] = m(k,h)
			}
		return new int[][][] {M, A};
	} // complexité Theta(n x H^2).
	
	static int[] glouton(int[][] E){//est le m�me pricipe que pour l'exo pr�c�dent
		//on cherche � avoir la meilleur augmentation de moyenne 
		//en ayant la meilleur augmentation de moyenne entre le fait d'avoir travaille k heures et k+1
		//et on compare cette diff�rence avec celle qu'on peut faire dans chaque mati�re
		//et on en d�duit l� o� il faut mieux mettre � disposition cette heure de travail
		//pour avoir la meilleur augmentation de moyenne possible
		int n =E.length;
		int Hmax = E[0].length;
		int h = 0;
		int k = 0;
		int[] heurParUnite = new int[n];
		int[] listeNotes = new int[n];
		int diffMax = 0;
		int indiceDiffMax = 0;
		while(h < Hmax-1) {
			indiceDiffMax = 0;
			diffMax = 0;
			for(int i = 0; i < n; i++) {
				if(diffMax < E[i][1+heurParUnite[i]]- E[i][heurParUnite[i]]) {
					diffMax = E[i][1+heurParUnite[i]]- E[i][heurParUnite[i]];
					indiceDiffMax = i;
				}
			}
			//System.out.printf("diffMax = %d, indiceDiffMax = %d\n",diffMax, indiceDiffMax);
			heurParUnite[indiceDiffMax] += 1;
			h++;
		}
		//afficheTab(heurParUnite, "HeureParUnitee");
		while(k < n) {
			listeNotes[k] = E[k][heurParUnite[k]];
			k++;
		}
		/*for(int j = 0; j < n; j++) {
			System.out.printf("listeNotes[%d] = %d\n",j,listeNotes[j]);
		}*/
		return listeNotes;
	}
	
	static void aro(int[][] A, int[][] E, int k, int h){
		// affiche ro(k,h) : répartition optimale de h heures sur les k premières unités.
			if (k == 0) return; // sans rien faire, ro(0,h) a été affichée.
			// ici : k > 0
			// ro(k,h) = ro(k-1,h-a(k,h)) union {"k-1 <-- a(k,h)"}
			int akh = A[k][h]; // nombre d'heures allouées �  la k-ème unité dans ro(k,h)
			aro(A,E,k-1,h-akh); // ro(k-1,h-akh) a été affichée
			System.out.printf("unité %d, <-- %d heures, note estimée %d\n", 
					k-1, akh, E[k-1][akh]); 
			// le nombre d'heures allouées �  la kème unité a été affiché
			// Ainsi : 
			// 1) La répartition optimale ro(k-1,h-akh) a été affichée,
			// 2) "k-1 <-- akh" a été affichée,
			// 3) donc ro(k,h) = ro(k-1,h-akh) union {"k-1 <-- akh"}
			// a été affichée.
		} // Complexité Theta(n).

	static int[][] estimations(int n, int H){ // retourne E[0:n][0:H+1] de terme général
		// E[i][h] = e(i,h). Les estimations sont aléatoires, croissantes selon h.
			int[][] E = new int[n][H+1];
			Random rand = new Random(); // pour génération aléatoire des notes estimées.
			for (int i = 0; i < n; i++) E[i][0] = 6 + rand.nextInt(5);
			for (int i = 0; i < n; i++)
				for (int h = 1; h < H+1; h++)
					E[i][h] = min( E[i][h-1] + (1+rand.nextInt(5)), 20) ;
			return E;
		}
	
	static int[][] estimationsRestreintes(int[][] E, int H){ int n = E.length;
	// E[0:n][0:Hmax+1]. Cette fonction retourne E[0:n][0:H+1]
		int[][] E_H = new int[n][H+1];
		for (int i = 0; i < n; i++) 
			for (int h = 0; h < H+1; h++)
				E_H[i][h] = E[i][h]; 
		return E_H;
	}
	
	static void afficher(int[][] E){ int n = E.length, H = E[0].length - 1;
	// E[0:n][0:H+1] est de terme général E[i][h] = e(i,h), note estimée pour h heures
	// de révision de l'unité i. Les lignes se terminent par une suite de 20.
	// Le premier 20 est affiché. Puis ", ...]"
	// Exemple : [12, 15, 20, 20, 20] --> [12, 15, 20, ...]
		System.out.println("[");
		for (int i = 0; i < n; i++) {
			// recherche du 1er "20"
			int h = 0;
			while (h < H+1 && E[i][h] < 20) h++;
			// E[h:n] = [20, 20, ...]
			if (h == H+1)
				System.out.printf("unité %d %s\n",i,Arrays.toString(E[i]));
			else {
				int[] Ei = Arrays.copyOfRange(E[i],0,h+1);
				String Si = Arrays.toString(Ei);
				int li = Si.length();
				Si = Si.substring(0,li-1) + ", ...]";
				System.out.printf("i = %d %s\n",i,Si);
			}
		}
		System.out.println("]");
	}	
	
	static void afficheE(int[][]E) {
		int n = E.length;
		int Hmax = E[0].length;
		for (int i=0; i<n;i++) {
			for(int j =0; j< Hmax; j++) {
				System.out.printf("E[%d][0+heurParUnite[%d]] = %d\n",i,j,E[i][j]);
			}
		}
	}
	
	static int min(int x, int y){
		if (x<=y) return x;
		return y;
	}
	
	static void afficheTab(int[] pTab, String pStr) {
		for(int i=0; i < pTab.length; i++) {
			System.out.printf(pStr+"[%d] = %d\n", i, pTab[i]);
		}
	}
	
	static float calcDistanceRelative(int[]listeNotesGlouton, int[][]M, int n, int Hmax) {
		float moyenneMaximumOptimale = (float) M[n][Hmax]/n;
		String strDouble = String.format("%.2f", moyenneMaximumOptimale);
		System.out.printf("Moyenne maximum optimale: %s/20\n", strDouble);
		float moyenneMaximumGloutonne = 0;
		for(int i = 0; i < n; i++) {
			moyenneMaximumGloutonne += listeNotesGlouton[i];
		}
		moyenneMaximumGloutonne = moyenneMaximumGloutonne/n;
		System.out.printf("Moyenne maximum gloutonne : %.2f\n", moyenneMaximumGloutonne);
		float distanceRelative = (moyenneMaximumOptimale - moyenneMaximumGloutonne)/moyenneMaximumOptimale;
		System.out.printf("distanceRelative = %.2f\n",distanceRelative);
		return distanceRelative; //renvoie la distance relative entre le glouton et l'optimal
	}
	
	static float chaqueTour(int pNbUnites, int pNbHeures, int pNbTours) {//
		int[][] E = estimations(pNbUnites,pNbHeures); 
		//afficher(E);		
		//afficheE(E);
		int[] listeNotes = glouton(E);
		//afficheTab(listeNotes,"Notes par unites ");		
		int[][] E_H = estimationsRestreintes(E,pNbHeures); // notes estimées pour 0 ≤ h < H+1 
		int[][][] MA = calculerMA(E_H);
		int[][] M = MA[0], A = MA[1];
		float DistanceRelative = calcDistanceRelative(listeNotes, M,pNbUnites,pNbHeures);
		//System.out.printf("gain total maximum : %d\n", M[K][S]);
		/*System.out.println("tableau M des gains maximum :");
		afficher(M); 
		System.out.println("une affectation optimale :");
		System.out.printf("DistanceRelative[%d] = %f\n",pNbTours, DistanceRelative);*/
		return DistanceRelative;//renvoie la distance relative apr�s avoir appel� calcDistanceRelative()
	}
	
	static double[] tabDistRelative() {//fait 5000 runs et renvoie un tableau de distances relatives
		//en appelant 5000 fois chaqueTour()
		double[]DistanceRelative = new double[5000];//5000
		Random vRandom = new Random();
		int nbHeureTravail = 0;
		int nbUnitees = 0;
		int Vmax = 100;
		for(int i = 0; i < DistanceRelative.length; i++) {
			nbHeureTravail = vRandom.nextInt(2,Vmax);
			nbUnitees = vRandom.nextInt(2,Vmax);
			DistanceRelative[i] = chaqueTour(nbUnitees,nbHeureTravail,i);			
		}
		return DistanceRelative;
	}
	
	static void evalStat() {//affiche la mediane, la moyenne et l'�cart-type avec l'histogram
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
    	
    	String lFileName = "C:/Users/vince/eclipse-workspace/histograms/histogramTravail.png";
        String lPlotTitle = "Histogram";
        String lxAxis = "Bins";
        String lyAxis = "Values";
        int lNbBins = 50;
        saveChart(DistanceRelative, lNbBins, lPlotTitle, lxAxis, lyAxis, lFileName);
	}
	
	//le code suivant � �t� trouv� sur internet, il permet de faire les Histograms
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