package com.amplio.rdict.tests.review;

import junit.framework.TestCase;

import com.amplio.rdict.review.ScoreHistory;

public class ScoreHistoryTest extends TestCase{

	public void testEasinessHistoryTest() {
		int size = 4;
		
		ScoreHistory eh = new ScoreHistory(size);
		
		int scoreOnMonday = 1;
		int scoreOnTuesday = 2;
		int scoreOnWednesday = 3;
		int scoreOnThursday = 4;
		
		eh.add(scoreOnMonday);
		eh.add(scoreOnTuesday);
		eh.add(scoreOnWednesday);
		eh.add(scoreOnThursday);
		
		assertEquals("4,3,2,1", eh.toString());
	}
	
	public void testCreateFromString() {
		ScoreHistory eh = ScoreHistory.createFromString("1,2,3,4");
		
		assertEquals(4, eh.size());
		assertEquals(1, eh.get(0));
		assertEquals(2, eh.get(1));
		assertEquals(3, eh.get(2));
		assertEquals(4, eh.get(3));
	}
	
	public void testCalculateAverageScore() {
		ScoreHistory shPerfect = ScoreHistory.createFromString("4,4,4");
		ScoreHistory shFailEverything = ScoreHistory.createFromString("0,0,0");
		ScoreHistory sh50Percent = ScoreHistory.createFromString("0,2,4");
		
		assertEquals(4.0, shPerfect.calcAvg());
		assertEquals(0.0, shFailEverything.calcAvg());
		assertEquals(2.0, sh50Percent.calcAvg());
	}
	
	public void testAddingMoreRecordsThanSizeOverwritesOldHistory(){
		ScoreHistory eh = ScoreHistory.createFromString("4,3,2,1");
		
		assertEquals(2, eh.get(2));
		assertEquals(1, eh.get(3));
		
		eh.add(5);
		
		assertEquals("5,4,3,2", eh.toString());
		
		eh.add(3);
		
		assertEquals("3,5,4,3", eh.toString());
	}
	
}
