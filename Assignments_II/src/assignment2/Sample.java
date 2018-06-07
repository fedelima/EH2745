package assignment2;

public class Sample {
	static final int HIGH_LOAD = 0;
	static final int SHUT_DOWN = 1;
	static final int LOW_LOAD = 2;
	static final int DISCONNECT = 3;
	
	int id, cluster, state;
	double[] attribute;
	
	//*** CONSTRUCTOR ***
	public Sample(int id, double[] attribute, int cluster) {
		this.id = id;
		this.attribute = attribute;
		this.cluster = cluster;
	}

	//*** GET STATE ***
	public String GetState() {
		String condition = null;
		switch (this.state) {
			case HIGH_LOAD : condition = "HIGH LOAD"; break;
			case SHUT_DOWN : condition = "SHUT_DOWN"; break;
			case LOW_LOAD : condition = "LOW_LOAD"; break;
			case DISCONNECT : condition = "DISCONNECT"; break;
			default : condition = "UNASSIGNED"; break;
		}
		return condition;
	}
}
