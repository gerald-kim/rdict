package com.amplio.rdict.review;

import java.util.Date;

import junit.framework.TestCase;

import org.joda.time.DateTime;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;

public class ReviewManagerTest extends TestCase {

	public static final String DB_TEST_FILE = "test.db";

	private ODB db;
	private CardSetManager cardSetManager;

	private ReviewManager mgr;

	public void setUp() {
		db = ODBFactory.open( DB_TEST_FILE );
		cardSetManager = new CardSetManager( db );
		mgr = new ReviewManager( db, cardSetManager );
	}

	public void tearDown() {
		db.rollback();
		db.close();
	}

	public void testDetermineStudyModeIfNoCards() {

		mgr.checkAvailableExercises();

		assertTrue( !mgr.isAvailableTodaysScheduledExercise );
		assertTrue( !mgr.isAvailableLookedupTodayExercise );
	}

	public void testCheckExercisesIfCardsScheduledForToday() {
		Card cardScheduledForToday = new Card( "today", "the answer" );
		cardScheduledForToday.lookedup = new DateTime().minusDays( 1 ).toDate();
		cardScheduledForToday.scheduled = new Date();

		db.store( cardScheduledForToday );

		mgr.checkAvailableExercises();

		assertTrue( mgr.isAvailableTodaysScheduledExercise );
		assertTrue( !mgr.isAvailableLookedupTodayExercise );
	}

	public void testCheckExercisesIfCardsLookedUpToday() {
		Card c = new Card( "today", "the answer" );
		db.store( c );

		mgr.checkAvailableExercises();

		assertTrue( !mgr.isAvailableTodaysScheduledExercise );
		assertTrue( mgr.isAvailableLookedupTodayExercise );
	}

	public void testCheckExercisesIfNotLookedUpTodayAndNotScheduledForToday() {
		Card c = new Card( "today", "the answer" );
		c.lookedup = new DateTime().minusDays( 10).toDate();
		c.scheduled = new DateTime().plusDays( 10).toDate();

		db.store( c );

		mgr.checkAvailableExercises();

		assertTrue( !mgr.isAvailableTodaysScheduledExercise );
		assertTrue( !mgr.isAvailableLookedupTodayExercise );
	}

	public void testDetermineAvailableExercises() {

		mgr.checkStudyAvailableStudyModes();

		assertEquals( ReviewManager.EXERCISES_CARD_DB_IS_EMPTY, mgr.availableExercises );
	}

	public void testCheckStudyModeIfCardsScheduledForToday() {
		Card cardLookedupToday = new Card( "lookeduptoday", "an answer" );
//		cardLookedupToday.s();

		Card cardScheduledForToday = new Card( "today", "the answer" );
		cardScheduledForToday.scheduled = new Date();
		
		db.store( cardScheduledForToday );
		db.store( cardLookedupToday );

		mgr.checkStudyAvailableStudyModes();

		assertEquals( ReviewManager.EXERCISES_SCHEDULED_TODAY, mgr.availableExercises );
	}

	public void testCheckStudyModeIfCardLookedupToday() {
		Card cardLookedupToday = new Card( "lookeduptoday", "an answer" );
		cardLookedupToday.scheduleByGrade( 3 );

		db.store( cardLookedupToday );

		mgr.checkStudyAvailableStudyModes();

		assertEquals( ReviewManager.EXERCISES_CARD_DB_IS_EMPTY, mgr.availableExercises );
	}

}
