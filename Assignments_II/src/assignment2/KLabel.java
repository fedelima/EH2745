package assignment2;

import java.util.ArrayList;

public class KLabel {
	
	//*** LABEL CENTROIDS ***
	public static void LabelCentroids(ArrayList<Sample> centroids) {
		for (Sample centroid : centroids) {
			isHighLoad(centroid);
			isShutDown(centroid);
			isLowLoad(centroid);			
			isDisconnect(centroid);
			centroids.set(centroid.cluster, centroid);
		}
	}
	
	//*** DETERMINE IF CENTROID IS HIGH LOAD ***
	private static void isHighLoad(Sample centroid) {
		int Nbus = centroid.attribute.length/2;
		double[] v = new double[Nbus];
		
		for (int n=0; n < Nbus; n++) {
			v[n] = centroid.attribute[2*n];
		}
		
		if (v[4] + v[6] + v[8] < 2.93) {
			//buses 5, 7 and 9 have low voltages
			centroid.state = Sample.HIGH_LOAD;
		}		
	}
	
	//*** DETERMINE IF CENTROID IS SHUT DOWN ***
	private static void isShutDown(Sample centroid) {
		int Nbus = centroid.attribute.length/2;
		double[] o = new double[Nbus];
		
		for (int n=0; n < Nbus; n++) {
			o[n] = centroid.attribute[2*n+1]*Math.PI/180;
		}
		
		double pmin = 0.02; //minimum power to assume generator is online
		double p14 = Math.abs(o[0]-o[3]); //flow through line 1-4
		double p28 = Math.abs(o[1]-o[7]); //flow through line 2-8
		double p36 = Math.abs(o[2]-o[5]); //flow through line 3-6
		
		if (p14 < pmin || p28 < pmin || p36 < pmin) {
			//if one of these flows is too low, it means that the generator is offline
			centroid.state = Sample.SHUT_DOWN;
		}		
	}
	
	//*** DETERMINE IF CENTROID IS LOW LOAD ***
	private static void isLowLoad(Sample centroid) {
		int Nbus = centroid.attribute.length/2;
		double[] v = new double[Nbus];
		
		for (int n=0; n < Nbus; n++) {
			v[n] = centroid.attribute[2*n];
		}
		
		if (v[5] + v[6] + v[7] > 3.02) {
			//buses 6, 7 and 8 have high voltages
			centroid.state = Sample.LOW_LOAD;
		}		
	}
	
	//*** DETERMINE IF CENTROID IS DISCONNECT ***
	private static void isDisconnect(Sample centroid) {
		int Nbus = centroid.attribute.length/2;
		double[] v = new double[Nbus];
		
		for (int n=0; n < Nbus; n++) {
			v[n] = centroid.attribute[2*n];
		}
		
		for (int i=0; i < Nbus; i++) {
			if (v[i] < 0.85) {
				//if one of the voltages is too low, it is because of line disconnection
				centroid.state = Sample.DISCONNECT;
				break;
			}
		}
	}
	
	//*** LABEL SAMPLES BASED ON CENTROIDS ***
	public static void LabelSamples(ArrayList<Sample> centroids, ArrayList<Sample> samples) {
		for (int m=0; m < samples.size(); m++) {
			Sample sample = samples.get(m);
			for (Sample centroid : centroids) {
				if (sample.cluster == centroid.cluster) {
					sample.state = centroid.state;
				}
			}
			samples.set(m, sample);
		}
	}
}


