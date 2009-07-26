package com.amplio.rdict.review;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import com.amplio.rdict.review.Card;

public class CardTest extends TestCase {
	
	public void testConstructor() {
		Card c = new Card("How big is it?", "Very big.");
		
		assertEquals("How big is it?", c.question);
		assertEquals("Very big.", c.answer);
		assertEquals(2.5, c.easiness);
		assertEquals(0, c.repsSinceLapse);
		assertEquals(-1, c.interval);
		
		SimpleDateFormat dateformatYYYYMMDD = new SimpleDateFormat("yyyyMMdd");
		Date now  = Calendar.getInstance().getTime();
		
        assertEquals(dateformatYYYYMMDD.format(now), c.date_lookedup);
        assertEquals(null, c.date_scheduled);
        
        assertEquals("0,0,0", c.getScoreHistory().toString());
	}
	
	public void testRounding() {

		double twoPointTwo = 2.2;
		double twoPointSeven = 2.7;
		
		assertEquals(2, (int)twoPointTwo);
		assertEquals(2, (int)twoPointSeven);
		
		assertEquals(3, (int)Math.ceil(twoPointSeven));
	}
	
	public void testCalcInterval() {
		Card c = new Card("How big?", "Big.");
		
		assertEquals(0, c.repsSinceLapse);
		assertEquals(-1, c.interval);
		
		c.calcInterval();
		
		assertEquals(1, c.interval);
		
		c.repsSinceLapse += 1;
		c.calcInterval();
		
		assertEquals(6, c.interval);
		
		c.repsSinceLapse += 1;
		c.easiness = 2.2;
		
		int prevInterval = c.interval;
		
		c.calcInterval();
		
		assertEquals( (int) Math.ceil(prevInterval * c.easiness), c.interval);
	}
	
	public void testSchedule() {
		Card c = new Card("How big?", "Big.");
		
		c.schedule();
		
		Date oneDayLater = new Date();
		oneDayLater.setTime(oneDayLater.getTime() + 1000*60*60*24);
		
		SimpleDateFormat dateformatYYYYMMDD = new SimpleDateFormat("yyyyMMdd");
		
		String expectedScheduled = dateformatYYYYMMDD.format(oneDayLater);
		
		assertEquals(expectedScheduled, c.date_scheduled);
	}
	
	public void testDefaultEasiness() {
		Card c = new Card("How big?", "Big.");
		
		assertEquals(2.5, c.easiness);
	}
	
	public void testCalcEasinessNotAdjustedIfTooLow() {
		Card c = new Card("How big?", "Big.");
		
		int grade = 5;
		double prevEasiness = 0.1;
		double expectedEasiness = prevEasiness + (0.1 - (Card.MAX_GRADE - grade) * (0.08 + (Card.MAX_GRADE - grade) * 0.02));
		
		assertTrue( 1.3 > expectedEasiness);
		
		c.easiness = 0.1;
		c.adjustEasinessByGrade(grade);
		
		assertEquals(1.3, c.easiness);
	}
	
	public void testCalcEasinessByGrade() {
		Card c = new Card("How big?", "Big.");
		
		// EF':= EF + (0.1 - (5 - q) * (0.08 + (5 - q ) * 0.02))
		
		int grade = 4;
		double prevEasiness = 2.5;
		double expectedEasiness = prevEasiness + (0.1 - (Card.MAX_GRADE - grade) * (0.08 + (Card.MAX_GRADE - grade) * 0.02));
		
		assertTrue( 1.3 < expectedEasiness);
		
		c.adjustEasinessByGrade(grade);
		
		assertEquals(expectedEasiness, c.easiness);
	}
	
	public void testCalcEasinessByGradeLessThanThreeIgnoresEFAndResetsInterval() {
		// If grade is less than 3, don't change EF and reset reps and interval
		
		Card c = new Card("How big?", "Big.");
		
		int grade = 2;
		double prevEasiness = c.easiness;
		
		c.adjustEasinessByGrade(grade);
		
		assertEquals(prevEasiness, c.easiness);
		assertEquals(1, c.interval);
	}
	
	public void testAdjustingEasinessUpdatesScoreHistory() {
		Card c = new Card("How big?", "Big.");
		
		assertEquals("0,0,0", c.getScoreHistory().toString());
		
		c.adjustEasinessByGrade(1);
		
		assertEquals("1,0,0", c.getScoreHistory().toString());
		
		c.adjustEasinessByGrade(2);
		
		assertEquals("2,1,0", c.getScoreHistory().toString());
		
		c.adjustEasinessByGrade(3);
		
		assertEquals("3,2,1", c.getScoreHistory().toString());
	}
}
