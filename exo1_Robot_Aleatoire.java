import java.util.Arrays;
import java.util.Random;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

public class Robot1_Aleatoire {

	public static void main(String[] args) {
			evalStat();	
		}
	
		static int Nruns = 5000;//pour 5000 la moyenne et l'ecarttype affichent NaN

		static float init() {
			//int L = 5;
			//int C = 7;
			Random vRandom = new Random();
			int L = vRandom.nextInt(1000); //on créé un nombre de lignes aléatoire à chaque tours
			int C = vRandom.nextInt(1000); //on crée un ombre de colonnes aléatoire à chaque tours
			while (L < 3) {
				L = vRandom.nextInt(1000);/*pour que l'algorithme ait un peu plus de sens
				on s'arrange pour un damier d'un minimum de cases*/ 
			}
			while (C < 3) {
				C = vRandom.nextInt(1000);
			}
			/*tableau qui définiele cout pour aller au Nord,
			  à l'Est et au Nord-Est à chauqe case*/
			int[][] coutN = new int[L][C];  
			int[][] coutNE = new int[L][C];
			int[][] coutE = new int[L][C];
			//les valurs dans les différents tableaux sont aléatoires
			for(int i = 0; i < L; i++) {
				for(int j = 0; j< C; j++) {
					coutN[i][j]= vRandom.nextInt(100);
					coutNE[i][j]= vRandom.nextInt(100);
					coutE[i][j]= vRandom.nextInt(100);
				}
			}
			
			System.out.printf("L = %d, C = %d",L,C);
			//on appelle calculerM pour obtenir le tableau optimal 
			int[][] M = calculerM(L, C, coutN, coutNE, coutE);   
			//afficher(M);
			int sommeOptimale = M[L-1][C-1];//la case où se situe l'arrivé du robot (donc son prix total)
			//System.out.printf("Cout minimum :%d", M[L-1][C-1]);
			accm(M ,L-1, C-1, L, C, coutN,coutNE,coutE);//affichage chmin cout minimum
			//System.out.println();
			int sommeGloutonne = glouton(L, C, coutN, coutNE, coutE);//appel de la méthode gloutonne
			System.out.printf("sommeOptimale : %d, sommeGloutonne : %d",sommeOptimale,sommeGloutonne);
			return (float)Math.abs(sommeOptimale - sommeGloutonne)/sommeOptimale;
		}
		
		
		static int[][]calculerM(int L, int C, int[][] coutN, int[][] coutNE, int[][] coutE){
			int[][]M = new int[L][C];
			M[0][0] = 0;
			for (int c = 1; c < C; c++) {
				//on a modifié légèrement pour avoir un cout de déplacement aléatoire pour E
				M[0][c] = M[0][c-1] + e(0, c-1, L, C, coutE);//e[0][c-1]; 
			}
			for (int l = 1; l < L; l++) {
				//on a modifié légèrement pour avoir un cout de déplacement aléatoire pour N
				M[l][0] = M[l-1][0] + n(l-1, 0, L, C, coutN);//n[l-1][0];
			}
			for (int l = 1; l < L; l++) {
				for(int c = 1; c < C; c++) {
					//on a modifié légèrement pour avoir un cout de déplacement aléatoire pour NE
					M[l][c] = min(M[l-1][c] + n(l-1, c, L, C, coutN), M[l][c-1] + e(l, c-1, L, C, coutE), M[l-1][c-1] + ne(l-1, c-1, L, C, coutNE));//n[l-1][c], M[l][c-1] + e[l][c-1], M[l-1][c-1] + ne[l][c-1]);
				}
			}
			return M;
		}
		
		static void accm(int[][] M, int l, int c, int L, int C,  int[][] coutN, int[][] coutNE, int[][] coutE) {
			if(l == 0 && c ==0) {
				System.out.printf("(l:%d,c:%d)\n", l,c);
				return;
			}
			if(l == 0) {
				accm(M, l, c-1, L, C, coutN, coutNE, coutE);
				//System.out.printf("%d, (%d,%d)\n",e(l,c-1,L,C), l, c);
				return;
			}
			if(c == 0) {
				accm(M, l-1, c, L, C, coutN, coutNE, coutE);
				//System.out.printf("%d, (%d,%d)\n",n(l-1,c,L,C),l,c);
				return;
			}
			int m1 = M[l][c-1] + e(l, c-1, L, C, coutE);
			int m2 = M[l-1][c-1] + ne(l-1, c-1, L, C, coutNE);
			int m3 = M[l-1][c] + n(l-1, c, L, C, coutN);
			
			if(m1 == min(m1,m2,m3)) {
				accm(M, l, c-1, L, C, coutN, coutNE, coutE);
				//System.out.printf("%d (%d,%d)\n",e(l,c-1,L,C), l,c);
				return;
			}
			
			else if(m2 == min(m1,m2,m3)) {
				accm(M,l-1,c-1,L,C, coutN, coutNE, coutE);
				//System.out.printf("%d (%d,%d)\n",ne(l-1,c-1,L,C), l,c);
				return;
			}
			else {
				accm(M, l-1, c, L, C, coutN, coutNE, coutE);
				//System.out.printf("%d (%d,%d)\n",n(l-1,c,L,C), l,c);
				return;
			}
		}
		
		static void afficher(int[][]M) {
			//permet d'afficher un tableau à 2 dimensions 
			int L = M.length;
			for(int l = 0; l< L; l++) {
				for(int c = 0; c < M[1].length; c++ ) {
					System.out.println(""+M[l][c]+"");
				}
			}
		}
			
		static int min( int x, int y, int z) {
			//fonction qui permet d'avoir le minimum de 3 valeurs
			int min = x;
			if(min > y) {
				min = y;
			}
			if(min > z) {
				min = z;
			}
			return min;
		}
			
		static int glouton(int L, int C, int[][] coutN, int[][] coutNE, int[][] coutE) {
			//int[][]M = new int [L][C];//toutes les vals sont à 0
			int m1 = 0;//ne(l, c, L, C, coutNE);
			int m2 = 0;//e(l, c, L, C, coutE);
			int m3 = 0;//n(l, c, L, C, coutN);
			int minimum = 0;
			int sommeGloutonne = 0;
			int c = 0;
			int l = 0;
			//System.out.printf("(%d,%d)",l,c);
			/*on veux aller à la destination du Robot qui est C-1 et L-1
			 pour cela on compare le prix des différents coup que le Robot
			 peut faire. 
			 On veut qu'il ait un cout minimal donc on appel la fonction min
			 qui permet d'avoir ce cout minimal.
			 Ensuite, on regarde à quel direction correspond ce cout
			 On privilégie la diagonale car il aura moins de couts à faire pour a la case C-1 et L-1*/
			while (c != C-1 || l != L-1) {
				m1 = ne(l, c, L, C, coutNE);
				m2 = e(l, c, L, C, coutE);
				m3 =  n(l, c, L, C, coutN);
				minimum = min(m1,m2,m3);
				
				if (m1 == minimum) {
					/*si on va dans la diagonale, pour "déplacer" le robot dans la suivante
					 il faut ajouter 1 à l et 1 à c*/
					sommeGloutonne += minimum;
					l++;
					c++;
					//System.out.printf("-> %d, (%d,%d)",minimum,l,c);
				}
				else if (m2 == minimum) {
					/*si on va à l'est, il faut ajouter 1 dans les colonnes
					 *  pour aller à la case suivante*/
					sommeGloutonne += minimum;
					c++;
					//System.out.printf("-> %d, (%d,%d)",minimum,l,c);
				}
				else {
					/*sinon on va au nord, il faut ajouter 1 dans les lignes
					 *  pour aller à la case suivante*/
					sommeGloutonne += minimum;
					l++;
					//System.out.printf("-> %d, (%d,%d)",minimum,l,c);
				}
			}
			System.out.println();
			return sommeGloutonne;
		}
		
	    static void sortMoyMedEcart(double[] D) {
	    	//permet de trier le tableau por que ce soit plus simple pour avoir la médiane
	    	Arrays.sort(D);
	    	int n =D.length;
			float vMoyenne = 0;
			float vMediane = 0;
			float vEcartType = 0;
	    	float somme =0;
	    	float variance = 0;
	    	for(int i=0; i< n;i++) {
	    		 //System.out.printf("D[%d] = %f\n",i,D[i]);
	    		somme += D[i];
	    	}
	    	vMoyenne = somme/n;//calcul de la moyenne
	    	if(n%2 == 0) {
	    		vMediane = (float)(D[n/2] + D[n/2 +1])/2;//calcul médaine si le nombre de case est paire
	    	}
	    	else {
	    		vMediane = (float)D[n/2];//si le nombre de cases est imapaire
	    	}
	    	for(int i=0; i< n;i++) {
	    		variance += (D[i] - vMoyenne)*(D[i] - vMoyenne);
	    		//calcul de la variance pour en déduire l'Ecart-Type
	    	}
	    	variance = variance/n;
	    	vEcartType = (float)Math.sqrt(variance);
	    	System.out.printf("moyenne : %f, mediane : %f, EcartType : %f", vMoyenne, vMediane, vEcartType);
	    }
		
		static void evalStat() {
			double [] vDistance = new double [Nruns];
			//tableau qui regroupe toutes les distances relatives des 5000 runs 
	    	for (int i=0; i<Nruns; i++){
	            vDistance[i]= (double)init();/*appel de la fonction init 5000 fois 
	            (elle renvoie la distance relative
	             entre la méthode gloutonne et la méthode optimale)*/
	            System.out.printf("distance relative : %f\n",vDistance[i]);
	        }
	    	//afficheHisto(vDistance,Nruns);
	    	sortMoyMedEcart(vDistance);//affiche médiane, moyenne et écart-type
	    	
	    	//le code suivant permet de faire apparaitre l'Histogram
	    	String lFileName = "C:/Users/vince/eclipse-workspace/histograms/histogramRobot1_Aleatoire.png";
	        String lPlotTitle = "Histogram";
	        String lxAxis = "Bins";
	        String lyAxis = "Values";
	        int lNbBins = 50;
	        saveChart(vDistance, lNbBins, lPlotTitle, lxAxis, lyAxis, lFileName);
	    }
		
		static int n(int l, int c, int L, int C, int[][] coutN){
			//fonction qui renvoie le cout de déplacement vers le Nord 
			if (l==L-1) return Integer.MAX_VALUE;
			return coutN[l][c];
		}
		static int ne(int l, int c, int L, int C, int[][] coutNE){
			//fonction qui renvoie le cout de déplacement pour aller au Nord-Est
			if (l == L-1 || c == C-1) return Integer.MAX_VALUE;
			return coutNE[l][c];
		}
		static int e(int l, int c, int L, int C, int[][] coutE){
			if (c == C-1) return Integer.MAX_VALUE;
			return coutE[l][c];
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

