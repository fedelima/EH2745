package assignment2;

import java.util.ArrayList;

public class Assignment_II {	
	// *** MAIN ROUTINE (EXECUTED FROM GUI) ***
	public static void execute(ArrayList<Sample> learnSet, ArrayList<Sample> testSet,
			String host, String database, String user, String psswd, 
			String lsname, String tsname, String csvFile) {
		Database.buildSet(host,database,user,psswd,learnSet,lsname); //Build learning set from database.
		Database.buildSet(host,database,user,psswd,testSet,tsname); //Build test set from database.
		KMeans.Cluster(learnSet, csvFile); //Cluster samples
		KNN.Classify(learnSet, testSet); //Classify new samples
		Database.populateSet(host,database,user,psswd,learnSet,"learn_set"); //Populate learn set into database.
		Database.populateSet(host,database,user,psswd,testSet,"test_set"); //Populate test set into database.
	}
}