package com.amplio.vcbl8r.review;

import java.util.Calendar;
import java.util.Date;

public class Card {
	public static final int MAX_GRADE = 4;
	
	public String question;
	public String answer;
	public double easiness;
	public int repsSinceLapse;
	public int interval;
	public int grade;
	public int finalGrade;
	public Date lookedup;
	public Date scheduled;
	public Date searched;
	public Date studied;
	
	public ScoreHistory sh;

	public Card(String question, String answer) {
		this.question = question;
		this.answer = answer;
        this.sh = new ScoreHistory(3);

        repsSinceLapse = 0;
        easiness = 2.5;
        interval = 1;
        grade = 0;
        finalGrade = 0;

        lookedup = new Date();
        studied = null;
        searched = new Date();

        Calendar c = Calendar.getInstance();
        c.add( Calendar.DAY_OF_MONTH, 1 );
        scheduled = c.getTime();
        
        sh = new ScoreHistory( 3 );
        sh.add( 0 ); sh.add(0); sh.add(0);
	}

	public void scheduleByGrade( int grade ) {
		this.grade = grade;
		if ( grade < 3 ) {
			repsSinceLapse = 0;
		} else {
			repsSinceLapse ++;
			double newEasiness = easiness + (0.1 - (5 - grade) * (0.08 + (5 - grade) * 0.02));
			easiness = Math.min( 1.3, newEasiness );
		}
		
		if ( repsSinceLapse == 0 ) {
			interval = 1;
		} else if ( repsSinceLapse == 1 ) {
			interval = 6;
		} else {
			interval = (int) (interval * easiness);
		}
		
		Calendar c = Calendar.getInstance();
		c.add( Calendar.DAY_OF_MONTH, interval );
		scheduled = c.getTime();
		studied = new Date();
		
		sh.add( grade );
    }

	public ScoreHistory getScoreHistory() {
		return this.sh;
	}

	@Override
	public String toString() {
		return "Card [question=" + question + ", answer=" + answer
				+ ", searched=" + searched + ", date_scheduled="
				+ scheduled + ", easiness=" + easiness + ", interval="
				+ interval + ", repsSinceLapse=" + repsSinceLapse + ", sh="
				+ sh + "]";
	}

	public static String getAbbreviatedAnswer(String answer, int maxLength) {
		String abbrv = null;
		
		if(answer.length() <= maxLength) {
			abbrv = answer;
		}
		else {
			int lastValidChar = answer.charAt( maxLength - 1);
			int firstInvalidChar = answer.charAt( maxLength );
			 
			//space, letter
			if(' ' == lastValidChar && ' ' != firstInvalidChar)
				abbrv = answer.substring(0, maxLength).trim() + "...";
			// if letter, space
			else if(' ' != lastValidChar && ' ' == firstInvalidChar)
				abbrv = answer.substring(0, maxLength) + "...";
			// if letter, letter
			else if(' ' != lastValidChar && ' ' != firstInvalidChar) {
				int lastSpaceBeforeBreak = answer.lastIndexOf(' ', maxLength);
				if(-1 != lastSpaceBeforeBreak)
					abbrv = answer.substring(0, lastSpaceBeforeBreak) + "...";
				else
					abbrv = answer.substring(0, maxLength) + "...";
			}
			// if space, space
			else if(' ' == lastValidChar && ' ' == firstInvalidChar)
				abbrv = answer.substring(0, maxLength).trim() + "...";
		}
		
		return abbrv;
    }


}
