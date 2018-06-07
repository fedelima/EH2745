package assignment2;

import java.util.ArrayList;

public class Assignment_II {	
	// *** MAIN ROUTINE (EXECUTED FROM GUI) ***
	public static void execute(ArrayList<Sample> learnSet, ArrayList<Sample> testSet, ArrayList<Sample> centroids,
			String host, String database, String user, String psswd, 
			String lsname, String tsname) {
		Database.buildSet(host,database,user,psswd,learnSet,lsname); //build learning set from database.
		Database.buildSet(host,database,user,psswd,testSet,tsname); //build test set from database.
		KMeans.Cluster(learnSet, centroids); //cluster samples of the learning set.		
		KNN.Classify(learnSet, testSet, centroids); //classify samples of the test set.
		KLabel.LabelCentroids(centroids); //label centroids according to heuristics.
		KLabel.LabelSamples(centroids,learnSet); //label learn set samples.
		KLabel.LabelSamples(centroids,testSet); //label test set samples.
		Database.populateSet(host,database,user,psswd,learnSet,"learn_set"); //populate learn set into database.
		Database.populateSet(host,database,user,psswd,testSet,"test_set"); //populate test set into database.
		Database.populateSet(host,database,user,psswd,centroids,"centroids"); //populate centroids into database.
	}
}