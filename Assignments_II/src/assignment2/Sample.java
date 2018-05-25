package assignment2;

public class Sample {
	int state;
	Double[] attribute;
	String[] rdfid, name, sub_rdfid, time;
	
	public Sample(String[] rdfid, String[] name, String[] time, Double[] attribute, String[] sub_rdfid) {
		this.rdfid = rdfid;
		this.name = name;
		this.time = time;
		this.attribute = attribute;
		this.sub_rdfid = sub_rdfid;
	}
}
