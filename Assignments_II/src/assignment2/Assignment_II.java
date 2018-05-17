package assignment2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

public class Assignment_II {
	//*** VARIABLE DEFINITION ***
	static ArrayList<Sample> learnSet = new ArrayList<Sample>();
	static ArrayList<Sample> testSet = new ArrayList<Sample>();
	static final int HIGH_LOAD = 0;
	static final int MEDIUM_LOAD = 1;
	static final int LOW_LOAD = 2;
	static final int NO_LOAD = 3;
	
	// *** MAIN ROUTINE ***
	public static void main(String[] args) {
		String user = "root";
		String psswd = "xxxx";
		createSets(user,psswd); //Build learning set.
		kClustering();
		kNN();
	}
	
	//*** BUILD LEARNING AND TEST SET FROM SQL DATA ***
	public static void createSets(String user, String psswd) {
		try {
			//Connect to database.			
			String jdbcString = "jdbc:mysql://localhost:3306/assignment_2?useSSL=false";
			Connection conn = DriverManager.getConnection(jdbcString, user, psswd);
			Statement query = conn.createStatement();
			
			//Initialize variables.		
			int N = 9; //Number of buses in the system.
			int i = 0; //Initialize counter i.
			int j = 0; //Initialize counter j.
			String[] rdfid = new String[N];
			String[] name = new String[N];
			String[] time = new String[N];
			String[] sub_rdfid = new String[N];
			Double[] value = new Double[N];

			//Create learning set.
			ResultSet measurementsTable = query.executeQuery("SELECT * FROM measurements ORDER BY time");
			while(measurementsTable.next()) {				
				if (i < 2*N){					
					if ((i%2 == 0) || (i==0)){
						rdfid[j] = measurementsTable.getString("rdfid");
						name[j] = measurementsTable.getString("name");	
						time[j] = measurementsTable.getString("time");
						value[j] = Double.parseDouble(measurementsTable.getString("value"));
						sub_rdfid[j] = measurementsTable.getString("sub_rdfid");
						j++;
					}
					i++;
				}else {
					learnSet.add(new Sample(rdfid,name,time,value,sub_rdfid));
					i=0;
					j=0;
				}
			}
			
			//Create test set.
			ResultSet analogValuesTable = query.executeQuery("SELECT * FROM analog_values ORDER BY time");
			while(analogValuesTable.next()) {				
				if (i < 2*N){					
					if ((i%2 == 0) || (i==0)){
						rdfid[j] = analogValuesTable.getString("rdfid");
						name[j] = analogValuesTable.getString("name");	
						time[j] = analogValuesTable.getString("time");
						value[j] = Double.parseDouble(analogValuesTable.getString("value"));
						sub_rdfid[j] = analogValuesTable.getString("sub_rdfid");
						j++;
					}
					i++;
				}else {
					testSet.add(new Sample(rdfid,name,time,value,sub_rdfid));
					i=0;
					j=0;
				}
			}
			
			//Close database objects.			
			query.close(); //Close query.
			conn.close(); //Close connection.
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//*** K-CLUSTERING ROUTINE ***
	public static void kClustering() {
		int M = learnSet.size(); //Sample index.
		int N = 9; //Attribute index.
		int K = 4; //Centroid index.
		double dsq = 0;
		Sample sample = null;
		double[][] delta = new double[K][N];		
		int clusterWith;	
		double[][] distance = new double[M][K];
		double epsilon = 0.01;
		double[][] centroid = { {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
								{1.1, 1.1, 1.2, 0.6, 1.2, 1.1, 1.2, 1.4, 1.2},
								{1.3, 0.5, 0.6, 1.0, 1.2, 1.1, 0.6, 1.2, 0.9},
								{1.0, 1.1, 1.2, 1.3, 0.5, 1.1, 1.2, 0.7, 1.0} };	
		
		while (max(delta) > epsilon) {
			//Cluster samples with centroids.
			for (int m = 0; m < learnSet.size(); m++) {
				sample = learnSet.get(m);
				for (int k = 0; k < K; k++) {				
					for (int n = 0; n < N; n++) {
						dsq += Math.pow(centroid[k][n] - sample.value[n], 2);					
					}				
					distance[m][k] = Math.sqrt(dsq); 
				}
				clusterWith = mindex(distance[m]);
				switch (clusterWith) {
					case 0 : sample.state = HIGH_LOAD; break;
					case 1 : sample.state = MEDIUM_LOAD; break;
					case 2 : sample.state = LOW_LOAD; break;
					case 3 : sample.state = NO_LOAD; break;				
				}
				learnSet.set(m, sample);
			}
			
			//Calculate centroids' displacement.
			for (int m = 0; m < learnSet.size(); m++) {
				sample = learnSet.get(m);
				switch (sample.state) {
					case HIGH_LOAD : 
						for (int n = 0; n < N; n++) {
							delta[0][n] += (centroid[m][n]-sample.value[n])/frequency(HIGH_LOAD);
						}					
						break;
					case MEDIUM_LOAD : 
						for (int n = 0; n < N; n++) {
							delta[0][n] += (centroid[m][n]-sample.value[n])/frequency(MEDIUM_LOAD);
						} 
						break;
					case LOW_LOAD : 
						for (int n = 0; n < N; n++) {
							delta[0][n] += (centroid[m][n]-sample.value[n])/frequency(LOW_LOAD);
						} 
						break;
					case NO_LOAD :
						for (int n = 0; n < N; n++) {
							delta[0][n] += (centroid[m][n]-sample.value[n])/frequency(NO_LOAD);
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
	
	//*** KNN SAMPLE CLASSIFICATION ***
	public static void kNN() {
		Sample sampleTest = null;
		Sample sampleLearn = null;
		double dsq = 0.00;
		int M = learnSet.size(); //Sample index.
		int I = testSet.size(); //Sample index.
		int N = 9; //Number of attributes.
		int K = 5; //K-Number.
		double[][] a = new double[M][2];

		for (int i=0; i < I; i++) {		
			sampleTest = testSet.get(i);
			for (int m=0; m < M; m++) {
				sampleLearn = learnSet.get(m);
				for (int n = 0; n < N; n++) {
					dsq += Math.pow(sampleTest.value[n] - sampleLearn.value[n], 2);					
				}
				a[m][0] = Math.sqrt(dsq);
				a[m][1] = sampleLearn.state; 
			}
			Arrays.sort(a[0]);
			for (int k=0; k < K; k++) {
				sampleTest.state = (int) a[k][1];
				testSet.set(i, sampleTest);
			}			
		}
	}
	
	//*** FREQUENCY OF STATE ***
	public static double frequency(int type) {
		int count = 0;
		for (Sample sample : learnSet) {
			if (type == sample.state) {
				count++;
			}
		}		
		return count;		
	}
	
	//*** DETERMINE MINIMUM VALUE INDEX WITHIN ARRAY ***
	public static int mindex(double[] arr) {
		double min = 1000.0;
		int mindex = 0;
		for (int i=0; i < arr.length; i++) {
			if (arr[i] < min) {
				min = arr[i];
				mindex = i;
			}
		}
		return mindex;
	}
	
	//*** DETERMINE MAX WITHIN MATRIX ***
	public static double max(double[][] matrix) {
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
