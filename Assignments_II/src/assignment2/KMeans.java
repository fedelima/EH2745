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
		double epsilon = 0.001;
		double max_delta = epsilon + 1; //initialize max_delta greater than epsilon.
		double[][] distance = new double[M][K]; //distance from sample "m" to centroid "k".
		double[][] delta = new double[K][N]; //array to change centroid's position.
		
		InitCentroids(learnSet, centroids, K); //randomly initialize K centroids.
		while (max_delta > epsilon) {
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
	
	//*** RANDOMLY INITIALIZE CENTROIDS ***
	private static void InitCentroids(ArrayList<Sample> learnSet, ArrayList<Sample> centroids, int K) {
		int N = learnSet.get(0).attribute.length;
		double[][] attribute = new double[K][N];
		int min=0, max=199;
		Random random = new Random();
		for (int k=0; k < K; k++) {		
			int rnd = random.nextInt((max - min) + 1) + min;
			attribute[k] = learnSet.get(rnd).attribute;
			centroids.add(new Sample(k+1, attribute[k], k));
		}
	}
}
