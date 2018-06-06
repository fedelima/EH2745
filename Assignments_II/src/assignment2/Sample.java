package assignment2;

public class Sample {
	static final int HIGH_LOAD = 1;
	static final int SHUT_DOWN = 2;
	static final int LOW_LOAD = 3;
	static final int DISCONNECT = 4;
	
	String time;
	double[] attribute;
	int cluster;
	int state;
	
	public Sample(String time, double[] attribute) {
		this.time = time;
		this.attribute = attribute;
	}

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
