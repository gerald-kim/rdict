package com.amplio.rdict.review;

public class StatRecord {

	public int cardCount = 0;
	public int gradeInPercent = 0;
	public String record_date = null;
	
	public StatRecord(int cardCount, int gradeInPercent, String yyyyMMdd) {
		this.cardCount = cardCount;
		this.gradeInPercent = gradeInPercent;
		this.record_date = yyyyMMdd;
	}

}
