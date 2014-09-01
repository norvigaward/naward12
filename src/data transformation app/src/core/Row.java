package core;

import java.util.ArrayList;

public class Row implements Comparable{
	
	public String domain;
	public String website;
	public ArrayList<String> categories;
	public ArrayList<Double> scores;
	
	public Row(String line) {
		String[] fields = line.split("\t");
		this.domain = fields[0];
		this.website = fields[1];
		
		this.categories = new ArrayList<String>();
		this.scores = new ArrayList<Double>();
		for(int i = 2; i < fields.length; i++){
			this.categories.add(fields[i].split(":")[0]);
			this.scores.add(Double.parseDouble(fields[i].split(":")[1]));
		}
	}
	
	public Row(String domain, String website) {
		this.domain =domain;
		this.website = website;
		
		this.categories = new ArrayList<String>();
		this.scores = new ArrayList<Double>();
	}

	@Override
	public int compareTo(Object o) {
		return this.domain.compareTo(((Row)o).domain);
	}

	@Override
	public String toString() {
		return "Row [domain=" + domain + ", website=" + website
				+ ", categories=" + categories + ", scores=" + scores + "]";
	}

}
