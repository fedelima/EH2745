package assignment2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class KMeans {
	//*** K-CLUSTERING ROUTINE ***
	public static void Cluster(ArrayList<Sample> learnSet, String csvFile) {
		double[][] centroids = csv2array(csvFile); //centroid's initial position	
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
						dsq += Math.pow(centroids[k][n] - sample.attribute[n], 2);					
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
							delta[Sample.HIGH_LOAD][n] += (centroids[Sample.HIGH_LOAD][n]-sample.attribute[n])/frequency(learnSet,Sample.HIGH_LOAD);
						}					
						break;
					case Sample.SHUT_DOWN : 
						for (int n = 0; n < N; n++) {
							delta[Sample.SHUT_DOWN][n] += (centroids[Sample.SHUT_DOWN][n]-sample.attribute[n])/frequency(learnSet,Sample.SHUT_DOWN);
						} 
						break;
					case Sample.LOW_LOAD : 
						for (int n = 0; n < N; n++) {
							delta[Sample.LOW_LOAD][n] += (centroids[Sample.LOW_LOAD][n]-sample.attribute[n])/frequency(learnSet,Sample.LOW_LOAD);
						} 
						break;
					case Sample.DISCONNECT :
						for (int n = 0; n < N; n++) {
							delta[Sample.DISCONNECT][n] += (centroids[Sample.DISCONNECT][n]-sample.attribute[n])/frequency(learnSet,Sample.DISCONNECT);
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
	
	//*** READ CSV FILE INTO ARRAY ***
	public static double[][] csv2array(String centroidFile) {		
		ArrayList<String> db = new ArrayList<String>();
		
		try {
			FileReader fr = new FileReader(centroidFile);
			BufferedReader br = new BufferedReader(fr);		
			db = new ArrayList<String>();
			String line = null;
			while ((line = br.readLine()) != null) {db.add(line);}
			br.close();
		} catch (IOException e){
			e.printStackTrace();
		}
		
		int K = db.size();
		int N = db.get(0).split(",").length;		
		String[] attributes = new String[N];
		double[][] centroids = new double[K][N];		
		
		for (int k = 0; k < K; k++) {
			attributes = db.get(k).split(",");
			for (int n = 0; n < N; n++) {
				centroids[k][n] = Double.parseDouble(attributes[n]);
			}			
		}		
		
		return centroids;
	}
}
