package com.amplio.rdict.review;

import java.util.Date;

import junit.framework.TestCase;

import org.joda.time.DateTime;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;

public class ReviewManagerTest extends TestCase {

	public static final String DB_TEST_FILE = "test.db";

	private ODB m_db;
	private CardSetManager m_cardSetManager;

	private ReviewManager m_mgr;

	public void setUp() {
		m_db = ODBFactory.open( DB_TEST_FILE );
		m_cardSetManager = new CardSetManager( m_db );
		m_mgr = new ReviewManager( m_db, m_cardSetManager );
	}

	public void tearDown() {
		m_db.rollback();
		m_db.close();
	}

	public void testDetermineStudyModeIfNoCards() {

		m_mgr.checkAvailableExercises();

		assertTrue( !m_mgr.isAvailableTodaysScheduledExercise );
		assertTrue( !m_mgr.isAvailableLookedupTodayExercise );
	}

	public void testCheckExercisesIfCardsScheduledForToday() {
		Card cardScheduledForToday = new Card( "today", "the answer" );
		cardScheduledForToday.lookedup = new DateTime().minusDays( 1 ).toDate();
		cardScheduledForToday.scheduled = new Date();

		m_db.store( cardScheduledForToday );

		m_mgr.checkAvailableExercises();

		assertTrue( m_mgr.isAvailableTodaysScheduledExercise );
		assertTrue( !m_mgr.isAvailableLookedupTodayExercise );
	}

	public void testCheckExercisesIfCardsLookedUpToday() {
		Card c = new Card( "today", "the answer" );
		m_db.store( c );

		m_mgr.checkAvailableExercises();

		assertTrue( !m_mgr.isAvailableTodaysScheduledExercise );
		assertTrue( m_mgr.isAvailableLookedupTodayExercise );
	}

	public void testCheckExercisesIfNotLookedUpTodayAndNotScheduledForToday() {
		Card c = new Card( "today", "the answer" );
		c.lookedup = new DateTime().minusDays( 10).toDate();
		c.scheduled = new DateTime().plusDays( 10).toDate();

		m_db.store( c );

		m_mgr.checkAvailableExercises();

		assertTrue( !m_mgr.isAvailableTodaysScheduledExercise );
		assertTrue( !m_mgr.isAvailableLookedupTodayExercise );
	}

	public void testDetermineAvailableExercises() {

		m_mgr.checkStudyAvailableStudyModes();

		assertEquals( ReviewManager.EXERCISES_CARD_DB_IS_EMPTY, m_mgr.availableExercises );
	}

	public void testCheckStudyModeIfCardsScheduledForToday() {
		Card cardLookedupToday = new Card( "lookeduptoday", "an answer" );
//		cardLookedupToday.s();

		Card cardScheduledForToday = new Card( "today", "the answer" );
		cardScheduledForToday.scheduled = new Date();
		
		m_db.store( cardScheduledForToday );
		m_db.store( cardLookedupToday );

		m_mgr.checkStudyAvailableStudyModes();

		assertEquals( ReviewManager.EXERCISES_SCHEDULED_TODAY, m_mgr.availableExercises );
	}

	public void testCheckStudyModeIfCardLookedupToday() {
		Card cardLookedupToday = new Card( "lookeduptoday", "an answer" );
		cardLookedupToday.study( 3 );

		m_db.store( cardLookedupToday );

		m_mgr.checkStudyAvailableStudyModes();

		assertEquals( ReviewManager.EXERCISES_OTHER, m_mgr.availableExercises );
	}

}
