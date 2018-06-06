package assignment2;

import java.util.ArrayList;

public class KNN {
	//*** KNN SAMPLE CLASSIFICATION ***
	public static void Classify(ArrayList<Sample> learnSet,ArrayList<Sample> testSet) {
		Sample sampleTest = null;
		Sample sampleLearn = null;
		double dsq = 0.0;
		int N = learnSet.get(0).attribute.length; //Number of buses in the system.
		int M = learnSet.size(); //Number of learning samples.
		int I = testSet.size(); //Number of testing samples.
		int K = 5; //K-Number.
		int[] cluster = new int[K];
		int[] count = new int[4];
		double[][] distance = new double[M][2];

		for (int i=0; i < I; i++) {		
			sampleTest = testSet.get(i);
			for (int m=0; m < M; m++) {
				sampleLearn = learnSet.get(m);
				for (int n = 0; n < N; n++) {
					dsq += Math.pow(sampleTest.attribute[n] - sampleLearn.attribute[n], 2);					
				}
				distance[m][0] = Math.sqrt(dsq);
				distance[m][1] = sampleLearn.state; 
				dsq = 0.0; //reset squared distance.
			}
			cluster = sort(distance,K); //array with the K nearest neighbors classes
			for (int k=0; k < K; k++) {
				count[cluster[k]]++;
			}	
			sampleTest.state = maxdex(count);
			testSet.set(i, sampleTest);			
		}
	}
	
	//*** SORT FIRST K ELEMENTS IN ARRAY *** 
	private static int[] sort(double[][] matrix,int K) {
		int M = matrix.length;
		int mindex = 0;
		int[] state = new int[K];
		double min = 1000.0;
		for (int k=0; k < K; k++) {
			for (int m=0; m < M; m++) {
				if (matrix[m][0] < min) {
					min = matrix[m][0]; //update current minimum value
					mindex = m; //obtain minimum value index
				}
			}
			state[k]=(int) matrix[mindex][1];
			matrix[mindex][0] = 1000; //reset minimum value to a high value (avoid include more than once).
			min = 1000.0; //restart for the following nearest neighbor
		}
		return state;
	}
	
	//*** DETERMINE MAXIMUM VALUE INDEX WITHIN ARRAY ***
	private static int maxdex(int[] x) {
		int max = 0; //very high initial value
		int maxdex = 0;
		for (int i=0; i < x.length; i++) {
			if (x[i] > max) {
				max = x[i]; //update current minimum value
				maxdex = i; //obtain minimum value index
			}
		}
		return maxdex;
	}
}
