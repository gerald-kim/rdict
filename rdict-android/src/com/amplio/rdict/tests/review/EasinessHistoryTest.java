package com.amplio.rdict.tests.review;

import junit.framework.TestCase;

import com.amplio.rdict.review.EasinessHistory;

public class EasinessHistoryTest extends TestCase{

	public void testEasinessHistoryTest() {
		int size = 4;
		
		EasinessHistory eh = new EasinessHistory(size);
		
		double easinessMonday = 1.2;
		double easinessTuesday = 2.2;
		double easinessWednesday = 3.2;
		double easinessThursday = 4.2;
		
		eh.add(easinessMonday);
		eh.add(easinessTuesday);
		eh.add(easinessWednesday);
		eh.add(easinessThursday);
		
		assertEquals("4.2, 3.2, 2.2, 1.2", eh.toString());
	}
	
	public void testCreateFromString() {
		EasinessHistory eh = EasinessHistory.createFromString("1.2,2.2,3.2,4.2");
		
		assertEquals(4, eh.size());
		assertEquals(1.2, eh.get(0));
		assertEquals(2.2, eh.get(1));
		assertEquals(3.2, eh.get(2));
		assertEquals(4.2, eh.get(3));
	}
	
	public void testCalculateAverageEF() {
		EasinessHistory ehPerfect = EasinessHistory.createFromString("4, 4, 4");
		EasinessHistory ehFailEverything = EasinessHistory.createFromString("0, 0, 0");
		EasinessHistory eh50Percent = EasinessHistory.createFromString("0, 2, 4");
		
		assertEquals(4.0, ehPerfect.calcAvg());
		assertEquals(0.0, ehFailEverything.calcAvg());
		assertEquals(2.0, eh50Percent.calcAvg());
	}
	
	public void testAddingMoreRecordsThanSizeOverwritesOldHistory(){
		EasinessHistory eh = EasinessHistory.createFromString("4.0, 3.0, 2.0, 1.0");
		
		assertEquals(2.0, eh.get(2));
		assertEquals(1.0, eh.get(3));
		
		eh.add(5);
		
		assertEquals("5.0, 4.0, 3.0, 2.0", eh.toString());
		
		eh.add(3);
		
		assertEquals("3.0, 5.0, 4.0, 3.0", eh.toString());
	}
	
}
