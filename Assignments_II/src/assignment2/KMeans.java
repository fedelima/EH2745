package assignment2;

import java.util.ArrayList;
import java.util.Random;

public class KMeans {
	//*** K-CLUSTERING ROUTINE ***
	public static ArrayList<Sample> Cluster(ArrayList<Sample> learnSet, ArrayList<Sample> centroids) {	
		int K = 4; //default number of centroids.
		int N = learnSet.get(0).attribute.length; //number of attributes per sample.				
		int M = learnSet.size(); //number of samples.
		double dsq = 0.0;
		double epsilon = 0.00000000000001;
		double max_iter= 1000;
		double iter=0;
		double max_delta = epsilon + 1; //initialize max_delta greater than epsilon.
		double[][] distance = new double[M][K]; //distance from sample "m" to centroid "k".
		
		Forgy(learnSet, centroids, K); //initialize K centroids using Forgy's algorithm.
		while ((max_delta > epsilon) && (iter < max_iter)) {
			//Cluster samples with centroids.
			for (int m = 0; m < M; m++) {
				Sample sample = learnSet.get(m);
				for (int k = 0; k < K; k++) {				
					Sample centroid = centroids.get(k);
					for (int n = 0; n < N; n++) {
						dsq += Math.pow(centroid.attribute[n] - sample.attribute[n], 2);					
					}				
					distance[m][k] = Math.sqrt(dsq); 
					dsq = 0.0; //reset squared distance.
				}
				sample.cluster = mindex(distance[m]); //cluster with nearest centroid.
				learnSet.set(m, sample); //update sample in learn set.
			}
			
			//Calculate centroids' displacement.
			double[][] delta = new double[K][N]; //array to change centroid's position.
			for (int m = 0; m < M; m++) {
				Sample sample = learnSet.get(m);
				Sample centroid = centroids.get(sample.cluster);
				for (int n = 0; n < N; n++) {
					delta[centroid.cluster][n] += (sample.attribute[n]-centroid.attribute[n])/frequency(learnSet,centroid.cluster);
				}			
			}			
			
			//Update centroids' position.
			max_delta = 0.0;
			for (int k=0; k < K; k++) {
				Sample centroid = centroids.get(k);
				for (int n=0; n < N; n++) {
					centroid.attribute[n] += delta[k][n];					
					if (max_delta < delta[k][n]) max_delta = delta[k][n];
					delta[k][n] = 0.0;
				}
				centroids.set(k, centroid);
			}
			iter++;
		}
		return centroids;
	}
	
	//*** FREQUENCY OF CLUSTER ***
	private static double frequency(ArrayList<Sample> set, int k) {
		int count = 0;
		for (Sample sample : set) {
			if (sample.cluster == k) {
				count++;
			}
		}		
		return count;		
	}
	
	//*** DETERMINE MINIMUM VALUE INDEX WITHIN ARRAY ***
	private static int mindex(double[] x) {
		double min = 1000.0; //very high initial value
		int mindex = 0;
		for (int i=0; i < x.length; i++) {
			if (x[i] < min) {
				min = x[i]; //update current minimum value
				mindex = i; //obtain minimum value index
			}
		}
		return mindex;
	}
	
	//*** FORGY INITIALIZATION ALGORITHM ***
	private static void Forgy(ArrayList<Sample> learnSet, ArrayList<Sample> centroids, int K) {
		int N = learnSet.get(0).attribute.length;
		double[][] attribute = new double[K][N];
		int min = 0, max = 199;
		boolean NoGoodC=true;
		boolean temp, temp0, temp1, temp2, temp3;
		Random random = new Random();
		while(NoGoodC) {
			centroids.clear();
			temp0=false; temp1=false; temp2=false; temp3=false; temp=true;
			for (int k=0; k < K; k++) {		
				int rnd = random.nextInt((max - min) + 1) + min;
				attribute[k] = learnSet.get(rnd).attribute;
				centroids.add(new Sample(k+1, attribute[k], k));
			}
			KLabel.LabelCentroids(centroids); //label centroids according to heuristics.
			for (int k=0; k < K; k++) {	
				switch (centroids.get(k).state) {
					case Sample.HIGH_LOAD : temp0 = true; break;
					case Sample.SHUT_DOWN : temp1 = true; break;
					case Sample.LOW_LOAD : temp2 = true; break;
					case Sample.DISCONNECT : temp3 = true; break;
				}
			}
			if (temp0 && temp1 && temp2 && temp3 && temp) NoGoodC=false;
		}
				
	}
}
