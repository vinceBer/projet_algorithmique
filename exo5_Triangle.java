package triangle;

import java.util.Random;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import java.awt.Color;
import java.io.File;
import java.util.Arrays;


public class Triangle {


	
	public static void main(String[] args) {

		evalStat();
	}

	    // variables d'instance - remplacez l'exemple qui suit par le votre
	    static int vNbVal = 0;
	    static int vNbRangs = 0;
	    
	    static int Nruns = 5000;
	    static int Vmax = 100;
		static int Lmax = 500;
		static int Nmax = (Lmax*(Lmax + 1))/2;
		static int [] vTabRang = new int[Nmax];
		static int [] vTabValeurs = new int[Nmax];
        static Random vRandom = new Random();


	    static void initTabValeurs(int pNbValeurs){//initialise de mani�re al�atoire un tableau de pNbValeurs �l�ments
	        int vNbRandom;
	        
	        for (int i=0;i<pNbValeurs; i++){// boucle qui parcours le nombre d'�l�ments pr�sents dans le tableau
	            vNbRandom = vRandom.nextInt(Vmax);// permet de mettre de choisir des valeurs al�atoires
	            vTabValeurs[i]=vNbRandom; //associe une valeur al�atoire � chaque case du tableau
	        }
	    }
	    
	    static void listeValeursTab(){//permet de lister les valeurs du tableau (aide)
	        for (int i =0; i<vNbVal; i++){
	            System.out.printf("vTabValeurs[%d]= %d\n",i,vTabValeurs[i]);
	        }
	        System.out.printf("nb valeurs fois 2 avant la racine %d\n",2*vNbVal);// on va avoir 6 rangs
	        System.out.printf("nb valeurs fois 2 � la racine, pour avoir le nombre de rangs %d\n",(int)Math.sqrt(2*vNbVal));
	    }
	 
	    static void printTab(int []tab, int nbVal) {//permet de lister les valeurs du tableau (aide)
	        for (int i =0; i<nbVal; i++){
	            System.out.printf("tab[%d]= %d\n",i,tab[i]);
	        }
	    }
	    
	    static void initTabRang(){//permet d'�tablir un rang � chacun des indices du tableau()ne fonctionne pas si le triangle est incomplet
	        //pour un rang n, il y a n valeurs or tot = n(n+1)/2 => 2tot =~n^2
	        int i=0;
	        int rang=0;
	        while (i < Nmax) {// boucle sur le nombre de rangs 
	            for (int j=0; j < (rang +1); j++){// boucle sur le rang pour associer � chaque cases du tableau correspondantes � ce  rang (num�ro d'�tage )
	            	vTabRang[i+j]= rang;
	            }
	            rang += 1;
	            i += rang;
	        }
	    }
	    
	    static int g(int pIndice){//retourne l'indice gauche suivant (en dessous � gauche)
	        int a = pIndice + vTabRang[pIndice] + 1;// +11 car on d�mare du rang 0
	        //System.out.printf("indice de la val gauche %d\n",a);
	        return a;//nouvel indice (si est le max)
	    }
	    
	    static int d(int pIndice){// retourne l'indice droit suivant
	        int a = pIndice + vTabRang[pIndice] + 2;//+2 car on d�mare du rang 0
	        //System.out.printf("indice de la val droite %d\n",a);
	        return a;//nouvel indice (si est le max)
	    }
	    
	    static int maxGetD(int pIndice){//permet de regader quel est la valeur max parmis ces 2 l�
	        if (g(pIndice)<=vNbVal && d(pIndice)<=vNbVal){//on v�rifie que l'on ne d�passe pas le rang max
	            if(vTabValeurs[g(pIndice)]>=vTabValeurs[d(pIndice)]){// on regarde � quel indive la valeur sera la plus grande
	                //System.out.printf("le max est � gauche\n");
	                return g(pIndice);//l'indice qui correspond � lendroit o� la valeur est la plus grande des 2
	            }
	            else{
	                //System.out.printf("le max est � droite\n");
	                return d(pIndice);//l'indice qui correspond � lendroit o� la valeur est la plus grande des 2
	            }
	        }
	        return 0;
	    }
	    
	    static int glouton(int pIndice) {//On part de la velaur d'indice 0 (on pourrait choisir de commencer autre part)
	        int indice = 0;
	        int vSommeGloutonne=vTabValeurs[0];//On sait que au d�but la plus grande valeur est foc�ment la valeur d'o� on part 
	        for (int i=1; i<vNbRangs; i++) {// on a d�j� fait la valeur gloutonne au rang 0 donc on part pour cette boucle du rang 1 et on boucle sur tous les rangs
	            indice = maxGetD(pIndice);//on r�cup�re l'endroit o� se trouve la plus grande valeur entre droite et gauche
	            vSommeGloutonne += vTabValeurs[indice];//puis on ajoute la valeur r�cup�r�� � la somme glutonne �tant donn� que le chemin glouton passe par elle
	            //System.out.printf("cet indice est: %d, Somme gloutonne :%d\n",pIndice,vSommeGloutonne);
	            pIndice = indice;//on change d'indice car cette valeur est d�j� faite et on veut d�sormais aller voir le droite et gauche de cette nouvelle valeur
	        }        
	        return vSommeGloutonne;//on renvoie la somme totale du chemin 
	    }
	    
	    //(on part du bas et on remonte vers le haut du tiangle)
	    static int[] m(int []M,int []T, int [] rang, int nbMaxElements, int i){//le tableau M est celui que l'on va compl�ter (optimal), T est le tableau du triangle initial, 
	    	//rang est le tableau correspondant aux aux rang des diff�rentes cases, nbMaxElement est le dernier element du triangle et i 
	    	for (int k = nbMaxElements; k >= i; k--) {// boucle sur tous les �l�ments du triangle en commen�ant pas la fin
	    		if (rang[k] == rang[nbMaxElements]){//on sait que la valeur optimale sur le dernier niveau du triangle est elle m�me (cad c'est aussi le dernier niveau)
		            M[k]= T[k];//on recopie donc le dernier niveau du triagle dans le dernier niveau du tableau optimal
		        }
	    		else {// apr�s on remonte aux cases du niveau au dessus, on consid�re que les valeurs du niveau au dessus et on consid�re que � cet endroit
	    			// la valeur optimale est la valeur du triangle T a cet endroit l� + la valeur optiamle du max des cases situ�es en dessous � droite et � gauche
	    			M[k] = max(M[rang[k]+k+1],M[rang[k]+k+2]) + T[k];
	    		}
	    	}
			return M;//on revoie donc le tableau maximum
	    }
	    
	    static int m1(int []M,int []T, int [] rang, int rangMax, int i){//premi�re version trop couteuse en temps, r�cursive
//	        System.out.printf("indice : %d et rangMax :%d \n",i,rangMax);
	        if (rang[i] == rangMax){//pareil que pour la fonction m sauf qu'on revoir cases par cases
	            M[i]= T[i];//on met donc la valeur des indives de dernier rang de T dans la case correspondante du tableau M
	            return M[i];
	        }
	        else {//si on ne se situe pas sur la derni�re ligne
	            int gauche = m1(M,T,rang,rangMax,g(i));//on r�appelle la fonction mais cette fois sur l'indice i � droite et � gauche 
	            int droite = m1(M,T,rang,rangMax,d(i));
	            
	            if(gauche < droite){//on prend la plus grande valeur des 2 entre droite et gauche 
	            	//et la la met dans la case M[i] en ajoutant la valeur qui est pr�sente � cet indice du tableau T
	                M[i] = droite + T[i];//on fait pareil que pour la fonction m mais avec de la r�cursivit�
	                return M[i];
	            }
	            else{
	                M[i] = gauche + T[i];
	                return M[i];
	            }
	        }
	    }
	    
	    static int max(int val1, int val2) {//c'est une fonctin max, classsique qui renvoie le plus grand nombre entre 2 valeurs
	    	int max = val1;
	    	if(max < val2) {
	    		max = val2;
	    	}
	    	return max;
	    }
	    
	    static int [] calculerM(int []T){
	        int [] M =new int [T.length];
	        m(M,T,vTabRang,vNbVal-1,0);
	        return M;
	        /*m(0) = max(m(1),m(2)) + T[0]
	         * 
	         * m(j) = max(g(j),d(j)) + T[j]
	         * 
	         * au niveau de la ligne la plus basse du triangle, m(j) =T[j]
	         */
	    }
	    
	    static void acsm(int[] M, int[] T, int i, int n) {//fonction qui affiche le chemin pris par l'algorithme optimal
	    	int rangDepart = vTabRang[i];//on a un indice de d�part qu'on caract�rise par son rang
	    	int rangArrivee = vTabRang[n-1]; //un rang d'arriv�e
	    	int tailleChemin = rangArrivee - rangDepart + 1;//taille chemin arrivee-depart
		    int [] chemin = new int [tailleChemin];// on met tous les indices par lesquels on passe dansun tableau
		    int k = 0;
		    int gauche = 0;
		    int droite = 0;
		    chemin[0] = i;//on met l'indice de d�part � la 1�re case tu tableaau du chemin,
		    //en effet, on passe par la case d'o� on part
		    while (k < tailleChemin-1) {//boucle qui parcours les rangs par lequels on passe
		    	/* ici on effectue en quelques sortes l'algorithme glouton sur le tableau M
		    	  car on est forc�ment pass� par la case la plus grande qui se situe en dessous*/
		    	 
		    	gauche = g(i);
		    	droite = d(i);
		    	if (M[gauche]<= M[droite]) {//on prend donc la plus l'endroit o� il y a la plus grande valeur de droite et gauche
		    		i = droite;
		    	}
		    	else {
		    		i = gauche;
		    	}
		    	k++;//on peut donc passer � l'�tage suivant
		    	chemin[k] = i;//puis on met cet endroit par lequel on est pass� pr�c�demment (on le fait � ce moment l� car l'indice chemin[0] est d�j� remplis par la 1�re valeur)
		    }
		    for(k = 0; k < tailleChemin; k++) {//puis on affiche tous les endroit par lesquels on est pass�
		    	System.out.printf("case %d, T[%d],M[%d]\n", chemin[k], T[chemin[k]], M[chemin[k]]);
		    }
	    }
	    
	    static boolean checkvalid(int pNbVal) {//pour que cet algorithme fonctionne (pour l'�vauation statistique),
	    	//il faut �viter que toutes les valeurs du triangle soit 0
	    	for(int i=0; i < pNbVal; i++) {
	    		if (vTabValeurs[i] != 0) {
	    			return true;
	    		}
	    	}
	    	return false;
	    }
	    
	    static float CalculerSomme(){// permet de calculer la somme gloutonne puis la somme optimale
	        vNbRangs = vRandom.nextInt(1, Lmax + 1);
	       // vNbRangs = 5;
	    	vNbVal = vNbRangs*(vNbRangs + 1)/2;
	    	initTabValeurs(vNbVal);//initialise un tableau T al�atoire
	    	while (checkvalid(vNbVal) == false) {//on v�rifie qu'il exite aumoins une case diff�rente de 0 sinon on relance la proc�dure pr�c�dente
	    		initTabValeurs(vNbVal);
	    	}
/*	    	listeValeursTab();
	        printTab(vTabRang,vNbVal);
	        int a= g(5);
	        int b = d(5);
	        
	        int d = vNbRangs;
	        System.out.printf("resultat fct gloutonne %d\n",c);
	        System.out.printf(" %d\n,%d\n,%d\n",a,b,d);
*/
	    	int valGloutonMax = glouton(0);//on appelle la fonction gloutonne qui parcours le tableau T
	    	
	        int [] result = calculerM(vTabValeurs);//cette fonction construit le tableau M
	        //acsm(result,vTabValeurs,0,vNbVal);
	        int valOptimale = result[0];//la valeur optimale du tableau M se situe � l'indice 0 de celui-ci
//	        printTab(result,vNbVal);
	        System.out.printf("resultat optimal : %d, glouton : %d\n",valOptimale,valGloutonMax);
	        
	        return (float)(valOptimale - valGloutonMax)/valOptimale;//calcul de la distance relative
	    }
	    static void sortMoyMedEcart(double[] D) {//fonction qui permet de calculer la moyenne, la mediane et l'�cart-type
	    	Arrays.sort(D);
	    	float vMoyenne = 0;
	        float vMediane = 0;
	        float vEcartType = 0;
	    	int n =D.length;
	    	float somme =0;
	    	float variance = 0;
	    	for(int i=0; i< n;i++) {
	    		 //System.out.printf("D[%d] = %f\n",i,D[i]);
	    		somme += D[i];
	    	}
	    	vMoyenne = somme/n;
	    	if(n%2 == 0) {
	    		vMediane = (float)(D[n/2] + D[n/2 +1])/2;
	    	}
	    	else {
	    		vMediane = (float)D[n/2];
	    	}
	    	for(int i=0; i< n;i++) {
	    		variance += (D[i] - vMoyenne)*(D[i] - vMoyenne);
	    	}
	    	variance = variance/n;
	    	vEcartType = (float)Math.sqrt(variance);
	    	System.out.printf("moyenne : %f, mediane : %f, EcartType : %f", vMoyenne, vMediane, vEcartType);
	    }

	    
	    static void evalStat() {//fonction qui construit un tableau de distances relatives pour 5000 runs en appelant la fonction calculerSomme 5000 fois
	    	
		    double [] vDistance = new double [Nruns];
	    	initTabRang();
	    	for (int i=0; i<Nruns; i++){
	            vDistance[i]= CalculerSomme();
	            System.out.printf("distance relative : %f\n",vDistance[i]);
	        }
	    	//afficheHisto(vDistance,Nruns);
	    	sortMoyMedEcart(vDistance);
	    	
	    	String lFileName = "C:/Users/vince/eclipse-workspace/histograms/histogramTriangle.png";
	        String lPlotTitle = "Histogram";
	        String lxAxis = "Bins";
	        String lyAxis = "Values";
	        int lNbBins = 50;
	        saveChart(vDistance, lNbBins, lPlotTitle, lxAxis, lyAxis, lFileName);
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
