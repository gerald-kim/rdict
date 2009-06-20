package com.amplio.rdict.tests;

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import com.amplio.rdict.Card;
import com.amplio.rdict.ReviewManager;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class ReviewManagerTest extends TestCase {
	
public static final String DB_TEST_FILE = "db4o_test.db";
	
	ObjectContainer db = null;
	
	public void setUp() {
		db = Db4o.openFile(DB_TEST_FILE);
	}
	
	public void tearDown() {
		ObjectSet set = db.query(Card.class);
		
		for(int i = 0; i < set.size(); i++)
			db.delete(set.get(i));
		
		db.close();
	}
	
	public void testCheckStudyModeIfExistCardsScheduledForToday() {
		Card cardLookedupToday = new Card("lookeduptoday","an answer");
        cardLookedupToday.schedule();
        
        Card cardScheduledForToday = new Card("today", "the answer");
        cardScheduledForToday.date_scheduled = new SimpleDateFormat("yyyyMMdd").format(new Date());
		
		db.store(cardScheduledForToday);
		db.store(cardLookedupToday);
		
		ReviewManager mgr = new ReviewManager(db);
		
		mgr.checkAvailableStudyModes();
		
		assertEquals(ReviewManager.PRACTICE_SCHEDULED_TODAY, mgr.studyMode);
	}
	
	public void testCheckStudyModeIfNoCardsScheduledForToday() {
		Card cardLookedupToday = new Card("lookeduptoday","an answer");
        cardLookedupToday.schedule();
       
		db.store(cardLookedupToday);
		
		ReviewManager mgr = new ReviewManager(db);
		
		mgr.checkAvailableStudyModes();
		
		assertEquals(ReviewManager.PRACTICE_OTHER, mgr.studyMode);
	}
	
	public void testInitialGreetingIfThereAreCardsScheduledForToday() {
		Card cardLookedupToday = new Card("lookeduptoday","an answer");
        cardLookedupToday.schedule();
        
        Card cardScheduledForToday = new Card("today", "the answer");
        cardScheduledForToday.date_scheduled = new SimpleDateFormat("yyyyMMdd").format(new Date());
		
		db.store(cardScheduledForToday);
		db.store(cardLookedupToday);
		
		ReviewManager mgr = new ReviewManager(db);
		
		mgr.checkAvailableStudyModes();
		
		assertEquals("You have cards to practice today.", mgr.getReviewHomeGreeting());
	}
	
	public void testInitialGreetingIfThereNoCardsScheduledForToday() {
		Card cardLookedupToday = new Card("lookeduptoday","an answer");
        cardLookedupToday.schedule();
        
		db.store(cardLookedupToday);
		
		ReviewManager mgr = new ReviewManager(db);
		
		mgr.checkAvailableStudyModes();
		
		assertEquals("You have finished studying for today.", mgr.getReviewHomeGreeting());
	}

}
