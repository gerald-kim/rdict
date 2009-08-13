package com.amplio.rdict.review;

public class StatRecord {

	public int cardCount = 0;
	public int gradeInPercent = 0;
	public String recorded = null;
	
	public StatRecord(int cardCount, int gradeInPercent, String yyyyMMdd) {
		this.cardCount = cardCount;
		this.gradeInPercent = gradeInPercent;
		this.recorded = yyyyMMdd;
	}

}
