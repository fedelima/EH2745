package assignment2;

public class Sample {
	static final int HIGH_LOAD = 0;
	static final int SHUT_DOWN = 1;
	static final int LOW_LOAD = 2;
	static final int DISCONNECT = 3;
	
	String time;
	Double[] attribute;	
	int state;
	
	public Sample(String time, Double[] attribute) {
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
		}
		return condition;
	}
}
