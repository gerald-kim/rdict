package com.amplio.vcbl8r.review;

import java.util.Vector;

public class ScoreHistory {
	public static ScoreHistory createFromString(String string) {
		String[] vals = string.split(",");
		
		ScoreHistory sh = new ScoreHistory(vals.length);
		
		for(int i = vals.length - 1; i >= 0; i--)
			sh.add(new Integer(vals[i].trim()).intValue());
		
		return sh;
	}
	
	int size = 0;
	Vector<String> scoreValues = new Vector<String>();
	
	public ScoreHistory(int size) {
		this.size = size;
	}
	
	public void add(int score) {
		if(scoreValues.size() == size)
			scoreValues.remove(scoreValues.size() - 1);
			
		scoreValues.insertElementAt(new Integer(score).toString(), 0);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		for(int i = 0; i < scoreValues.size(); i ++){
			sb.append(scoreValues.get(i));
			
			if( i < scoreValues.size() - 1)
				sb.append(",");
		}
		
		return sb.toString();
	}

	public int get(int i) {
		return new Integer(scoreValues.get(i)).intValue();
	}
	
	public double calcAvg() {
		int total = 0;
		
		for(int i = 0; i < this.scoreValues.size(); i++)
			total +=  this.get(i);
		
		return total / this.scoreValues.size();
	}

	public int size() {
		return this.size;
	}
}
