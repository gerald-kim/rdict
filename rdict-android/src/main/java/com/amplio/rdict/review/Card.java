package com.amplio.rdict.review;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Card {

	public static final int MAX_GRADE = 4;
	
	public String question;
	public String answer;
	public double easiness;
	public int repsSinceLapse;
	public int interval;
	public String date_scheduled;
	public String date_lookedup;
	
	public ScoreHistory sh;

	public Card(String question, String answer) {
		this.question = question;
		this.answer = answer;
		
		this.repsSinceLapse = 0;
		this.easiness = (float) 2.5;
		this.interval = -1;
		
		SimpleDateFormat dateformatYYYYMMDD = new SimpleDateFormat("yyyyMMdd");
		Date now  = Calendar.getInstance().getTime();
        this.date_lookedup = dateformatYYYYMMDD.format(now);
        
        this.date_scheduled = null;
        
        this.sh = new ScoreHistory(3);
        this.sh.add(0);
        this.sh.add(0);
        this.sh.add(0);
	}

	public void calcInterval() {
		if (this.repsSinceLapse == 0)
			this.interval = 1;
		else if (this.repsSinceLapse == 1)
			this.interval = 6;
		else
			this.interval = (int) Math.ceil(this.interval * this.easiness);		
	}

	public void adjustEasinessByGrade(int grade) {
		if(grade < 3){
			this.repsSinceLapse = 0;
			this.schedule();
		}
		else {
			double newEasiness = this.easiness + (0.1 - (MAX_GRADE - grade) * (0.08 + (MAX_GRADE - grade) * 0.02));
			this.easiness = Math.max(1.3, newEasiness);
		}
		
		this.sh.add(grade);
	}
	
	public void schedule() {
		// the difference between today and today + the interval
		this.calcInterval();
		
		SimpleDateFormat dateformatYYYYMMDD = new SimpleDateFormat("yyyyMMdd");
		
		String temp_date = this.date_scheduled;
		
		if(null == temp_date){
			temp_date = dateformatYYYYMMDD.format(new Date());
		}
		
		Date date = null;
		try {
			date = dateformatYYYYMMDD.parse(temp_date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Date newDate = new Date();
		newDate.setTime(date.getTime() + this.interval*1000*60*60*24);
		
		this.date_scheduled = dateformatYYYYMMDD.format(newDate);
	}

	public ScoreHistory getScoreHistory() {
		return this.sh;
	}

	@Override
	public String toString() {
		return "Card [question=" + question + ", answer=" + answer
				+ ", date_lookedup=" + date_lookedup + ", date_scheduled="
				+ date_scheduled + ", easiness=" + easiness + ", interval="
				+ interval + ", repsSinceLapse=" + repsSinceLapse + ", sh="
				+ sh + "]";
	}
	
	

}
