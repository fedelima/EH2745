package assignment2;

import java.util.ArrayList;

public class KMeans {
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
						dsq += Math.pow(centroid[k][n] - sample.attribute[n], 2);					
					}				
					distance[m][k] = Math.sqrt(dsq); 
					dsq = 0.0; //reset squared distance.
				}
				clusterWith = mindex(distance[m]);
				switch (clusterWith) {
					case Sample.HIGH_LOAD : sample.state = Sample.HIGH_LOAD; break;
					case Sample.SHUT_DOWN : sample.state = Sample.SHUT_DOWN; break;
					case Sample.LOW_LOAD : sample.state = Sample.LOW_LOAD; break;
					case Sample.DISCONNECT : sample.state = Sample.DISCONNECT; break;				
				}
				learnSet.set(m, sample); //update sample in learn set.
			}
			
			//Calculate centroids' displacement.
			for (int m = 0; m < M; m++) {
				sample = learnSet.get(m);
				switch (sample.state) {
					case Sample.HIGH_LOAD : 
						for (int n = 0; n < N; n++) {
							delta[Sample.HIGH_LOAD][n] += (centroid[Sample.HIGH_LOAD][n]-sample.attribute[n])/frequency(learnSet,Sample.HIGH_LOAD);
						}					
						break;
					case Sample.SHUT_DOWN : 
						for (int n = 0; n < N; n++) {
							delta[Sample.SHUT_DOWN][n] += (centroid[Sample.SHUT_DOWN][n]-sample.attribute[n])/frequency(learnSet,Sample.SHUT_DOWN);
						} 
						break;
					case Sample.LOW_LOAD : 
						for (int n = 0; n < N; n++) {
							delta[Sample.LOW_LOAD][n] += (centroid[Sample.LOW_LOAD][n]-sample.attribute[n])/frequency(learnSet,Sample.LOW_LOAD);
						} 
						break;
					case Sample.DISCONNECT :
						for (int n = 0; n < N; n++) {
							delta[Sample.DISCONNECT][n] += (centroid[Sample.DISCONNECT][n]-sample.attribute[n])/frequency(learnSet,Sample.DISCONNECT);
						} 
						break;			
				}			
			}			
			
			//Update centroids' position.
			max_delta = 0.0;
			for (int k=0; k < K; k++) {
				for (int n=0; n < N; n++) {
					centroid[k][n] += delta[k][n];
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
}
