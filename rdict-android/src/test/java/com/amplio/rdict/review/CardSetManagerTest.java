package com.amplio.rdict.review;

import java.util.Vector;

import junit.framework.TestCase;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.Objects;

public class CardSetManagerTest extends TestCase {

	public static final String DB_TEST_FILE = "test.db";

	ODB db = null;

	private CardSetManager m_cardSetManager;

	public void setUp() {
		db = ODBFactory.open( DB_TEST_FILE );
		m_cardSetManager = new CardSetManager( db );
	}

	public void tearDown() {
		db.rollback();
		db.close();
	}

	
	public void testSaveCard() {
		Card cardForToday = new Card( "today", "the answer" );

		m_cardSetManager.save( cardForToday );

		Objects<Card> cards = db.getObjects( Card.class );

		assertEquals( 1, cards.size() );
		assertEquals( "today", (cards.getFirst()).question );
	}

	public void testSaveDuplicatedCard() {
		m_cardSetManager.create( "question", "answer1" );
		m_cardSetManager.create( "question", "answer2" );
		
		Card actual = m_cardSetManager.loadCardByHeadword( "question" );
		assertEquals( "answer1\n-------\nanswer2", actual.answer );
	}

	public void testLoadScheduledForToday() {
		MutableDateTime dateTime = new MutableDateTime();
		
		Card cardForToday = new Card( "today", "the answer" );
		cardForToday.scheduled = dateTime.toDate();
		
		Card cardForTomorrow = new Card( "1970 baby yeah!", "the answer" );
		dateTime.addDays( 1 );
		cardForTomorrow.scheduled = dateTime.toDate(); 
		Card cardForTomorrowAfter = new Card( "1970 second day", "the answer" );
		dateTime.addDays( 1 );
		cardForTomorrowAfter.scheduled = dateTime.toDate(); 
		
		m_cardSetManager.save( cardForToday );
		m_cardSetManager.save( cardForTomorrow  );
		m_cardSetManager.save( cardForTomorrowAfter );

		Vector<Card> cards = m_cardSetManager.loadCardsScheduledForToday();

		assertEquals( 1, cards.size() );

		Card card = (Card) cards.get( 0 );

		assertEquals( cardForToday.question, card.question );
	}

	public void testLoadCardsLookedupToday() {
		Card cardLookedupToday = new Card( "lookeduptoday", "an answer" );
		Card cardLookedupIn1970 = new Card( "today", "the answer" );
		DateTime dateTime = new DateTime();
		cardLookedupIn1970.lookedup = dateTime.minusDays( 100 ).toDate();

		m_cardSetManager.save( cardLookedupToday );
		m_cardSetManager.save( cardLookedupIn1970 );

		Vector<Card> cards = m_cardSetManager.loadCardsLookedupToday();

		assertEquals( 1, cards.size() );

		Card card = (Card) cards.get( 0 );
		assertEquals( "lookeduptoday", card.question );
	}

	public void testLoadCardsLookedupTodaySkipStudied() {
		Card cardLookedupToday = new Card( "lookeduptoday", "an answer" );
		cardLookedupToday.scheduleByGrade( 5 );
		Card cardLookedupIn1970 = new Card( "today", "the answer" );
		DateTime dateTime = new DateTime();
		cardLookedupIn1970.lookedup = dateTime.minusDays( 100 ).toDate();
		
		m_cardSetManager.save( cardLookedupToday );
		m_cardSetManager.save( cardLookedupIn1970 );
		
		
		Vector<Card> cards = m_cardSetManager.loadCardsLookedupToday();
		
		assertEquals( 0, cards.size() );
	}
	
	public void testLoadByPrefixIfNoneGiven() {

		Card aCard = new Card( "apple", "the answer" );
		Card bCard = new Card( "banana", "the answer" );
		Card cCard = new Card( "coconunt", "the answer" );

		m_cardSetManager.save( bCard );
		m_cardSetManager.save( aCard );
		m_cardSetManager.save( cCard );

		Vector<Card> cards = m_cardSetManager.loadCardsByPrefix( "" );

		assertEquals( 3, cards.size() );
		assertEquals( aCard.question, ((Card) cards.get( 0 )).question );
		assertEquals( bCard.question, ((Card) cards.get( 1 )).question );
		assertEquals( cCard.question, ((Card) cards.get( 2 )).question );
	}

	public void testLoadByPrefix() {

		Card aCard = new Card( "apple", "the answer" );
		Card bCard = new Card( "banana", "the answer" );
		Card cCard = new Card( "coconunta", "the answer" );

		m_cardSetManager.save( aCard );
		m_cardSetManager.save( bCard );
		m_cardSetManager.save( cCard );

		Vector<Card> cards = m_cardSetManager.loadCardsByPrefix( "a" );

		assertEquals( 1, cards.size() );
		assertEquals( aCard.question, cards.get( 0 ).question );
	}

	public void testDeleteCard() {

		Card aCard = new Card( "apple", "the answer" );
		Card bCard = new Card( "banana", "the answer" );
		Card cCard = new Card( "coconunt", "the answer" );

		m_cardSetManager.save( aCard );
		m_cardSetManager.save( bCard );
		m_cardSetManager.save( cCard );

		Vector<Card> cards = m_cardSetManager.loadCardsByPrefix( "a" );

		assertEquals( 1, cards.size() );
		assertEquals( 3, db.getObjects( ScoreHistory.class ).size() );
		
		m_cardSetManager.deleteCard( aCard );

		cards = m_cardSetManager.loadCardsByPrefix( "a" );

		assertEquals( 0, cards.size() );
		assertEquals( 2, db.getObjects( ScoreHistory.class ).size() );
		
	}

	public void testLoadCardByHeadword() {

		Card aCard = new Card( "apple", "the answer" );
		Card bCard = new Card( "banana", "the answer" );
		Card cCard = new Card( "coconunt", "the answer" );

		m_cardSetManager.save( aCard );
		m_cardSetManager.save( bCard );
		m_cardSetManager.save( cCard );

		Card c = m_cardSetManager.loadCardByHeadword( "banana" );

		assertEquals( "banana", c.question );
	}

	public void testLoadCardByHeadwordIfDuplicateExists() {

		Card bCard1 = new Card( "banana", "the answer" );
		Card bCard2 = new Card( "banana", "the answer2" );
		Card cCard = new Card( "coconunt", "the answer" );

		m_cardSetManager.save( bCard1 );
		m_cardSetManager.save( bCard2 );
		m_cardSetManager.save( cCard );

		try {
			m_cardSetManager.loadCardByHeadword( "banana" );
			fail();
		} catch( IllegalStateException ignore ) {
		}
	}

	public void testLoadCardByHeadwordIfNoSuchCardExists() {

		Card bCard1 = new Card( "banana", "the answer" );

		m_cardSetManager.save( bCard1 );

		Card c = m_cardSetManager.loadCardByHeadword( "fish" );

		assertEquals( null, c );
	}

	public void testLoadingCardPreservesEasinessHistory() {

		Card bCard1 = new Card( "banana", "the answer" );
		bCard1.scheduleByGrade( 1 );

		assertEquals( "1,0,0", bCard1.getScoreHistory().toString() );

		m_cardSetManager.save( bCard1 );

		Card c = m_cardSetManager.loadCardByHeadword( "banana" );

		assertEquals( "banana", c.question );
		assertEquals( "1,0,0", c.getScoreHistory().toString() );
	}
}
