package assignment2;

import java.util.ArrayList;

public class Assignment_II {
	//*** VARIABLE DEFINITION ***
	static ArrayList<Sample> learnSet = new ArrayList<Sample>();
	static ArrayList<Sample> testSet = new ArrayList<Sample>();
	static int N = 0;
	
	// *** MAIN ROUTINE ***
	public static void main(String[] args) {
		String user = "root";
		String psswd = "xxxx";
		N = Database.createSets(user,psswd,learnSet,testSet); //Build learning and test set.
		KMeans.Cluster(learnSet, N); //Cluster samples
		KNN.Classify(learnSet, testSet, N); //Classify new samples
	}
}