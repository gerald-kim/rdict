package com.amplio.rdict.review;

import java.util.Vector;

public class EasinessHistory {

	public static EasinessHistory createFromString(String string) {
		String[] vals = string.split(",");
		
		EasinessHistory eh = new EasinessHistory(vals.length);
		
		for(int i = vals.length - 1; i >= 0; i--)
			eh.add(new Double(vals[i]).doubleValue());
		
		return eh;
	}
	
	int size = 0;
	Vector<String> efs = new Vector<String>();
	
	public EasinessHistory(int size) {
		this.size = size;
	}
	
	public void add(double easinessMonday) {
		if(efs.size() == size)
			efs.remove(efs.size() - 1);
			
		efs.insertElementAt(new Double(easinessMonday).toString(), 0);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		for(int i = 0; i < efs.size(); i ++){
			sb.append(efs.get(i));
			
			if( i < efs.size() - 1)
				sb.append(", ");
		}
		
		return sb.toString();
	}

	public double get(int i) {
		return new Double(efs.get(i)).doubleValue();
	}
	
	public double calcAvg() {
		double avg = 0;
		
		for(int i = 0; i < this.efs.size(); i++)
			avg +=  new Double(this.efs.get(i)).doubleValue();
		
		return avg / this.efs.size();
	}

	public int size() {
		return this.size;
	}
}
