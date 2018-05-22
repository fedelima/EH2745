package assignment2;

import java.util.ArrayList;

public class KMeans {
	//*** VARIABLE DEFINITION ***
	static final int HIGH_LOAD = 0;
	static final int SHUT_DOWN = 1;
	static final int LOW_LOAD = 2;
	static final int DISCONNECT = 3;
	
	//*** K-CLUSTERING ROUTINE ***
	public static void Cluster(ArrayList<Sample> learnSet) {
		double[][] centroid = { {1.00, 1.01, 1.02, 0.89, 0.87, 0.95, 0.90, 0.93, 0.80},
								{1.00, 0.99, 1.00, 0.90, 0.85, 0.95, 0.90, 0.94, 0.82},
								{1.00, 0.97, 0.98, 0.88, 0.84, 0.95, 0.90, 0.93, 0.79},
								{1.00, 0.99, 1.00, 0.90, 0.85, 0.95, 0.90, 0.94, 0.82} }; //Centroids' initial position
		int N = 9; //Number of buses in the system.
		int M = learnSet.size(); //Number of samples.
		int K = 4; //Number of centroids.
		int clusterWith; //Sample "m" belongs to which centroid? 
		double dsq = 0.0;
		double epsilon = 0.01;
		double max_delta = 1000;
		double[][] distance = new double[M][K]; //Distance from sample "m" to centroid "k"
		double[][] delta = new double[K][N]; //Array to change centroid's position
		Sample sample = null;

		while (max_delta > epsilon) {
			//Cluster samples with centroids.
			for (int m = 0; m < M; m++) {
				sample = learnSet.get(m);
				for (int k = 0; k < K; k++) {				
					for (int n = 0; n < N; n++) {
						dsq += Math.pow(centroid[k][n] - sample.attribute[n], 2);					
					}				
					distance[m][k] = Math.sqrt(dsq); 
					dsq = 0.0; //reset squared distance.
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
			
			//Update maximum movement of centroid.
			max_delta = max(delta);
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
