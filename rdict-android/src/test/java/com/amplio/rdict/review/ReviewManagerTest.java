package com.amplio.rdict.review;

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;

public class ReviewManagerTest extends TestCase {
	
	public static final String DB_TEST_FILE = "test.db";
	
	ODB db = null;

	public void setUp() {
		db = ODBFactory.open( DB_TEST_FILE );
	}

	public void tearDown() {
		db.rollback();
		db.close();
	}
	
	public void testDetermineStudyModeIfNoCards() {
		ReviewManager mgr = new ReviewManager(db);
		
		mgr.checkAvailableExercises();
		
		assertTrue(! mgr.isAvailableTodaysScheduledExercise);
		assertTrue(! mgr.isAvailableLookedupTodayExercise);
		assertTrue(! mgr.isAvailableTOPNExercise);
	}
	
	public void testCheckExercisesIfCardsScheduledForToday() {
        Card cardScheduledForToday = new Card("today", "the answer");
        cardScheduledForToday.date_lookedup = "19700101";
        cardScheduledForToday.date_scheduled = new SimpleDateFormat("yyyyMMdd").format(new Date());
		
		db.store(cardScheduledForToday);
		
		ReviewManager mgr = new ReviewManager(db);
		
		mgr.checkAvailableExercises();
		
		assertTrue(mgr.isAvailableTodaysScheduledExercise);
		assertTrue(! mgr.isAvailableLookedupTodayExercise);
		assertTrue(mgr.isAvailableTOPNExercise);
	}
	
	public void testCheckExercisesIfCardsLookedUpToday() {
        Card c = new Card("today", "the answer");
        db.store(c);
		
		ReviewManager mgr = new ReviewManager(db);
		
		mgr.checkAvailableExercises();
		
		assertTrue(! mgr.isAvailableTodaysScheduledExercise);
		assertTrue(mgr.isAvailableLookedupTodayExercise);
		assertTrue(mgr.isAvailableTOPNExercise);
	}
	
	public void testCheckExercisesIfNotLookedUpTodayAndNotScheduledForToday() {
        Card c = new Card("today", "the answer");
        c.date_lookedup = "19700101";
        c.date_scheduled = "19700101";
        
        db.store(c);
		
		ReviewManager mgr = new ReviewManager(db);
		
		mgr.checkAvailableExercises();
		
		assertTrue( mgr.isAvailableTodaysScheduledExercise );
		assertTrue( !mgr.isAvailableLookedupTodayExercise );
		assertTrue( mgr.isAvailableTOPNExercise );
	}
	
	
	public void testDetermineAvailableExercises(){
		ReviewManager mgr = new ReviewManager(db);
		
		mgr.checkStudyAvailableStudyModes();
		
		assertEquals(ReviewManager.EXERCISES_CARD_DB_IS_EMPTY, mgr.availableExercises);
	}
	
	
	public void testCheckStudyModeIfCardsScheduledForToday() {
		Card cardLookedupToday = new Card("lookeduptoday","an answer");
        cardLookedupToday.schedule();
        
        Card cardScheduledForToday = new Card("today", "the answer");
        cardScheduledForToday.date_scheduled = new SimpleDateFormat("yyyyMMdd").format(new Date());
		
		db.store(cardScheduledForToday);
		db.store(cardLookedupToday);
		
		ReviewManager mgr = new ReviewManager(db);
		
		mgr.checkStudyAvailableStudyModes();
		
		assertEquals(ReviewManager.EXERCISES_SCHEDULED_TODAY, mgr.availableExercises);
	}
	
	public void testCheckStudyModeIfCardLookedupToday() {
		Card cardLookedupToday = new Card("lookeduptoday","an answer");
        cardLookedupToday.schedule();
       
		db.store(cardLookedupToday);
		
		ReviewManager mgr = new ReviewManager(db);
		
		mgr.checkStudyAvailableStudyModes();
		
		assertEquals(ReviewManager.EXERCISES_OTHER, mgr.availableExercises);
	}
	
	public void testCheckStudyModeIfNoScheduledCardAndNoLookedupCardButCardsExist() {
		Card cardLookedupIn1970AndNotScheduledForToday = new Card("question","an answer");
		cardLookedupIn1970AndNotScheduledForToday.date_lookedup = "19700101";
		cardLookedupIn1970AndNotScheduledForToday.schedule();
       
		db.store(cardLookedupIn1970AndNotScheduledForToday);
		
		ReviewManager mgr = new ReviewManager(db);
		
		mgr.checkStudyAvailableStudyModes();
		
		assertEquals(ReviewManager.EXERCISES_OTHER, mgr.availableExercises);
	}

}
