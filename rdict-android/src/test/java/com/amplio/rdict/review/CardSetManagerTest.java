package com.amplio.rdict.review;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import junit.framework.TestCase;

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

	public void testLoadScheduledCards() {

		Card cardForToday = new Card( "today", "the answer" );
		Card cardFor19700101 = new Card( "1970 baby yeah!", "the answer" );
		cardFor19700101.date_scheduled = "19700101";
		Card cardFor19700102 = new Card( "1970 second day", "the answer" );
		cardFor19700102.date_scheduled = "19700102";

		m_cardSetManager.save( cardForToday );
		m_cardSetManager.save( cardFor19700101 );
		m_cardSetManager.save( cardFor19700102 );

		Vector<Card> cards = m_cardSetManager.loadCardsByScheduledDate( "19700101" );

		assertEquals( 1, cards.size() );

		Card card = (Card) cards.get( 0 );

		assertEquals( "1970 baby yeah!", card.question );

		cards = m_cardSetManager.loadCardsByScheduledDate( "19700102" );

		assertEquals( 2, cards.size() );
	}

	public void testLoadScheduledCardsButNoneScheduled() {
		Card cardForToday = new Card( "today", "the answer" );


		m_cardSetManager.save( cardForToday );

		Vector<Card> cardsScheduled = m_cardSetManager.loadCardsByScheduledDate( "19700101" );

		assertEquals( 0, cardsScheduled.size() );
	}

	public void testLoadCardsScheduledForToday() {
		Card cardLookedupToday = new Card( "lookeduptoday", "an answer" );
		cardLookedupToday.schedule();

		Card cardScheduledForToday = new Card( "today", "the answer" );
		cardScheduledForToday.date_scheduled = new SimpleDateFormat( "yyyyMMdd" )
		        .format( new Date() );

		m_cardSetManager.save( cardLookedupToday );
		m_cardSetManager.save( cardScheduledForToday );

		Vector<Card> cards = m_cardSetManager.loadCardsScheduledForToday();

		assertEquals( 1, cards.size() );

		Card card = (Card) cards.get( 0 );
		assertEquals( "today", card.question );
	}

	public void testLoadCardsLookedupToday() {
		Card cardLookedupToday = new Card( "lookeduptoday", "an answer" );
		Card cardLookedupIn1970 = new Card( "today", "the answer" );
		cardLookedupIn1970.date_lookedup = "19700101";

		m_cardSetManager.save( cardLookedupToday );
		m_cardSetManager.save( cardLookedupIn1970 );

		Vector<Card> cards = m_cardSetManager.loadCardsLookedupToday();

		assertEquals( 1, cards.size() );

		Card card = (Card) cards.get( 0 );
		assertEquals( "lookeduptoday", card.question );
	}

	public void testLoadTopNHardestCards() {
		int n = 20;
		int totalNumCards = n + 2;

		Card[] hardCards = new Card[totalNumCards];

		for( int i = 0; i < totalNumCards - 1; i++ ) {
			Card c = new Card( "hardCard", "answer" );
			c.easiness = 1;
			hardCards[i] = c;
		}

		Card easyCard = new Card( "easyCard", "answer" );
		easyCard.easiness = 4;
		hardCards[totalNumCards - 1] = easyCard;

		for( int i = 0; i < totalNumCards; i++ ) {
			m_cardSetManager.save( hardCards[i] );
		}

		Vector<Card> cards = m_cardSetManager.loadTopNHardestCards( n );

		assertEquals( n, cards.size() );

		for( int i = 0; i < n; i++ ) {
			Card card = cards.elementAt( i );
			assertEquals( 1.0, card.easiness );
		}
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
		bCard1.adjustEasinessByGrade( 1 );

		assertEquals( "1,0,0", bCard1.getScoreHistory().toString() );

		m_cardSetManager.save( bCard1 );

		Card c = m_cardSetManager.loadCardByHeadword( "banana" );

		assertEquals( "banana", c.question );
		assertEquals( "1,0,0", c.getScoreHistory().toString() );
	}
}
