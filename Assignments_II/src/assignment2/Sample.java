package assignment2;

public class Sample {
	String[] rdfid, name, time, sub_rdfid;
	int state;
	Double[] value;
	
	public Sample(String[] rdfid, String[] name, String[] time, Double[] value, String[] sub_rdfid) {
		this.rdfid = rdfid;
		this.name = name;
		this.time = time;
		this.value = value;
		this.sub_rdfid = sub_rdfid;
	}
	
	public Sample(Double[] value, int state) {
		this.value = value;
		this.state = state;
	}
	
}
