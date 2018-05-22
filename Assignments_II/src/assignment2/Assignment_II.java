package assignment2;

import java.util.ArrayList;

public class Assignment_II {
	//*** VARIABLE DEFINITION ***
	static ArrayList<Sample> learnSet = new ArrayList<Sample>();
	static ArrayList<Sample> testSet = new ArrayList<Sample>();
	
	// *** MAIN ROUTINE ***
	public static void main(String[] args) {
		String user = "root";
		String psswd = "xxxx";
		Database.buildSet(user,psswd,learnSet, "measurements"); //Build learning set from database.
		Database.buildSet(user,psswd,testSet,"analog_values"); //Build test set from database.
		KMeans.Cluster(learnSet); //Cluster samples
		KNN.Classify(learnSet, testSet); //Classify new samples
		Database.populateSet(user,psswd,learnSet,"learn_set"); //Populate learn set into database.
		Database.populateSet(user,psswd,testSet,"test_set"); //Populate test set into database.
	}
}