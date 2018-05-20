package assignment2;

import java.util.ArrayList;

public class KMeans {
	//*** VARIABLE DEFINITION ***
	static final int HIGH_LOAD = 0;
	static final int SHUT_DOWN = 1;
	static final int LOW_LOAD = 2;
	static final int DISCONNECT = 3;
	
	//*** K-CLUSTERING ROUTINE ***
	public static void Cluster(ArrayList<Sample> learnSet, int N) {
		Sample sample = null;
		int M = learnSet.size(); //Sample index.
		int K = 4; //Centroid index.
		int clusterWith; //Sample "m" belongs to which centroid? 
		double dsq = 0.0;
		double epsilon = 0.01;
		double[][] distance = new double[M][K]; //Distance from sample "m" to centroid "k"
		double[][] delta = new double[K][N]; //Array to change centroid's position								
		double[][] centroid = { {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
								{1.1, 1.1, 1.2, 0.6, 1.2, 1.1, 1.2, 1.4, 1.2},
								{1.3, 0.5, 0.6, 1.0, 1.2, 1.1, 0.6, 1.2, 0.9},
								{1.0, 1.1, 1.2, 1.3, 0.5, 1.1, 1.2, 0.7, 1.0} }; //Centroids' initial position	
		
		while (max(delta) > epsilon) {
			//Cluster samples with centroids.
			for (int m = 0; m < M; m++) {
				sample = learnSet.get(m);
				for (int k = 0; k < K; k++) {				
					for (int n = 0; n < N; n++) {
						dsq += Math.pow(centroid[k][n] - sample.attribute[n], 2);					
					}				
					distance[m][k] = Math.sqrt(dsq); 
				}
				clusterWith = mindex(distance[m]);
				switch (clusterWith) {
					case HIGH_LOAD : sample.state = HIGH_LOAD; break;
					case SHUT_DOWN : sample.state = SHUT_DOWN; break;
					case LOW_LOAD : sample.state = LOW_LOAD; break;
					case DISCONNECT : sample.state = DISCONNECT; break;				
				}
				learnSet.set(m, sample); //update sample in learn set.
			}
			
			//Calculate centroids' displacement.
			for (int m = 0; m < M; m++) {
				sample = learnSet.get(m);
				switch (sample.state) {
					case HIGH_LOAD : 
						for (int n = 0; n < N; n++) {
							delta[HIGH_LOAD][n] += (centroid[HIGH_LOAD][n]-sample.attribute[n])/frequency(learnSet,HIGH_LOAD);
						}					
						break;
					case SHUT_DOWN : 
						for (int n = 0; n < N; n++) {
							delta[SHUT_DOWN][n] += (centroid[SHUT_DOWN][n]-sample.attribute[n])/frequency(learnSet,SHUT_DOWN);
						} 
						break;
					case LOW_LOAD : 
						for (int n = 0; n < N; n++) {
							delta[LOW_LOAD][n] += (centroid[LOW_LOAD][n]-sample.attribute[n])/frequency(learnSet,LOW_LOAD);
						} 
						break;
					case DISCONNECT :
						for (int n = 0; n < N; n++) {
							delta[DISCONNECT][n] += (centroid[DISCONNECT][n]-sample.attribute[n])/frequency(learnSet,DISCONNECT);
						} 
						break;			
				}			
			}			
			
			//Update centroids' position.
			for (int k=0; k < K; k++) {
				for (int n=0; n < N; n++) {
					centroid[k][n] += delta[k][n];
				}
			}			
		}
	}
	
	//*** FREQUENCY OF STATE ***
	private static double frequency(ArrayList<Sample> set, int state) {
		int count = 0;
		for (Sample sample : set) {
			if (state == sample.state) {
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
	
	//*** DETERMINE MAX WITHIN MATRIX ***
	private static double max(double[][] matrix) {
		double max = 0.0;
		int K = matrix.length;
		int N = matrix[0].length;		
		for (int k=0; k < K; k++) {
			for (int n=0; n < N; n++) {
				if (matrix[k][n] > max) {
					max = matrix[k][n];
				}
			}
		}		
		return max;
	}
}
