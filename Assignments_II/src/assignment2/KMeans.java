package assignment2;

import java.util.ArrayList;

public class KMeans {
	//*** K-CLUSTERING ROUTINE ***
	public static void Cluster(ArrayList<Sample> learnSet) {	
		int K = 4; //Number of centroids.
		int N = learnSet.get(0).attribute.length; //Number of attributes per sample.
		double[][] centroids = InitCentroids(K,N);		
		int M = learnSet.size(); //Number of samples.

		double dsq = 0.0;
		double epsilon = 0.001;
		double max_delta = epsilon + 1; //initialize max_delta greater than epsilon
		double[][] distance = new double[M][K]; //Distance from sample "m" to centroid "k"
		double[][] delta = new double[K][N]; //Array to change centroid's position
		Sample sample = null;

		while (max_delta > epsilon) {
			//Cluster samples with centroids.
			for (int m = 0; m < M; m++) {
				sample = learnSet.get(m);
				for (int k = 0; k < K; k++) {				
					for (int n = 0; n < N; n++) {
						dsq += Math.pow(centroids[k][n] - sample.attribute[n], 2);					
					}				
					distance[m][k] = Math.sqrt(dsq); 
					dsq = 0.0; //reset squared distance.
				}
				sample.cluster = mindex(distance[m]);
				learnSet.set(m, sample); //update sample in learn set.
			}
			
			//Calculate centroids' displacement.
			for (int m = 0; m < M; m++) {
				sample = learnSet.get(m);
				switch (sample.cluster) {
					case 0 : 
						for (int n = 0; n < N; n++) {
							delta[0][n] += (sample.attribute[n]-centroids[0][n])/frequency(learnSet,0);
						}					
						break;
					case 1 : 
						for (int n = 0; n < N; n++) {
							delta[1][n] += (sample.attribute[n]-centroids[1][n])/frequency(learnSet,1);
						}					
						break;
					case 2 : 
						for (int n = 0; n < N; n++) {
							delta[2][n] += (sample.attribute[n]-centroids[2][n])/frequency(learnSet,2);
						}					
						break;
					case 3 :
						for (int n = 0; n < N; n++) {
							delta[3][n] += (sample.attribute[n]-centroids[3][n])/frequency(learnSet,3);
						}					
						break;		
				}			
			}			
			
			//Update centroids' position.
			max_delta = 0.0;
			for (int k=0; k < K; k++) {
				for (int n=0; n < N; n++) {
					centroids[k][n] += delta[k][n];
					if (max_delta < delta[k][n]) max_delta = delta[k][n];
					delta[k][n] = 0.0;
				}
			}
		}
	}
	
	//*** FREQUENCY OF STATE ***
	private static double frequency(ArrayList<Sample> set, int state) {
		int count = 0;
		for (Sample sample : set) {
			if (state == sample.cluster) {
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
	private static double[][] InitCentroids(int K, int N) {
		double[][] centroids = new double[K][N];
		for (int k=0; k < K; k++) {
			for (int n=0; n < N; n++) {
				centroids[k][n] = Math.random();
			}
		}
		return centroids;
	}
}
