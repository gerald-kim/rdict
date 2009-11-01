package com.amplio.vcbl8r.review;

import java.io.File;

import junit.framework.TestCase;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;

import com.amplio.vcbl8r.review.Card;
import com.amplio.vcbl8r.review.CardSetManager;

public class CardSetManagerTest extends TestCase {

	public static final String DB_TEST_FILE = "test.db";

	ODB db = null;

	private CardSetManager cardSetManager;

	public void setUp() {
		db = ODBFactory.open( DB_TEST_FILE );
		cardSetManager = new CardSetManager( db );
	}

	public void tearDown() {
		new File(DB_TEST_FILE).delete();
	}
	
//	public void testSaveCard() {
//		Card cardForToday = new Card( "today", "the answer" );
//
//		cardSetManager.save( cardForToday );
//
//		Objects<Card> cards = db.getObjects( Card.class );
//
//		assertEquals( 1, cards.size() );
//		assertEquals( "today", (cards.getFirst()).question );
//	}
//
//	public void testSaveDuplicatedCard() {
//		cardSetManager.create( "question", "answer1" );
//		cardSetManager.create( "question", "answer2" );
//		
//		Card actual = cardSetManager.loadCardByHeadword( "question" );
//		assertEquals( "answer1\n-------\nanswer2", actual.answer );
//	}
//
//	public void testLoadScheduledForToday() {
//		MutableDateTime dateTime = new MutableDateTime();
//		
//		Card cardForToday = new Card( "today", "the answer" );
//		cardForToday.scheduled = dateTime.toDate();
//		
//		Card cardForTomorrow = new Card( "1970 baby yeah!", "the answer" );
//		dateTime.addDays( 1 );
//		cardForTomorrow.scheduled = dateTime.toDate(); 
//		Card cardForTomorrowAfter = new Card( "1970 second day", "the answer" );
//		dateTime.addDays( 1 );
//		cardForTomorrowAfter.scheduled = dateTime.toDate(); 
//		
//		cardSetManager.save( cardForToday );
//		cardSetManager.save( cardForTomorrow  );
//		cardSetManager.save( cardForTomorrowAfter );
//
//		Vector<Card> cards = cardSetManager.loadCardsScheduledForToday();
//
//		assertEquals( 1, cards.size() );
//
//		Card card = (Card) cards.get( 0 );
//
//		assertEquals( cardForToday.question, card.question );
//	}
//
//	public void testLoadCardsLookedupToday() {
//		Card cardLookedupToday = new Card( "lookeduptoday", "an answer" );
//		Card cardLookedupIn1970 = new Card( "today", "the answer" );
//		DateTime dateTime = new DateTime();
//		cardLookedupIn1970.lookedup = dateTime.minusDays( 100 ).toDate();
//
//		cardSetManager.save( cardLookedupToday );
//		cardSetManager.save( cardLookedupIn1970 );
//
//		Vector<Card> cards = cardSetManager.loadCardsLookedupToday();
//
//		assertEquals( 1, cards.size() );
//
//		Card card = (Card) cards.get( 0 );
//		assertEquals( "lookeduptoday", card.question );
//	}
//
//	public void testLoadCardsLookedupTodaySkipStudied() {
//		Card cardLookedupToday = new Card( "lookeduptoday", "an answer" );
//		cardLookedupToday.scheduleByGrade( 5 );
//		Card cardLookedupIn1970 = new Card( "today", "the answer" );
//		DateTime dateTime = new DateTime();
//		cardLookedupIn1970.lookedup = dateTime.minusDays( 100 ).toDate();
//		
//		cardSetManager.save( cardLookedupToday );
//		cardSetManager.save( cardLookedupIn1970 );
//		
//		
//		Vector<Card> cards = cardSetManager.loadCardsLookedupToday();
//		
//		assertEquals( 0, cards.size() );
//	}
//	
//	public void testLoadByPrefixIfNoneGiven() {
//
//		Card aCard = new Card( "apple", "the answer" );
//		Card bCard = new Card( "banana", "the answer" );
//		Card cCard = new Card( "coconunt", "the answer" );
//
//		cardSetManager.save( bCard );
//		cardSetManager.save( aCard );
//		cardSetManager.save( cCard );
//
//		Vector<Card> cards = cardSetManager.loadCardsByPrefix( "" );
//
//		assertEquals( 3, cards.size() );
//		assertEquals( aCard.question, ((Card) cards.get( 0 )).question );
//		assertEquals( bCard.question, ((Card) cards.get( 1 )).question );
//		assertEquals( cCard.question, ((Card) cards.get( 2 )).question );
//	}
//
//	public void testLoadByPrefix() {
//
//		Card aCard = new Card( "apple", "the answer" );
//		Card bCard = new Card( "banana", "the answer" );
//		Card cCard = new Card( "coconunta", "the answer" );
//
//		cardSetManager.save( aCard );
//		cardSetManager.save( bCard );
//		cardSetManager.save( cCard );
//
//		Vector<Card> cards = cardSetManager.loadCardsByPrefix( "a" );
//
//		assertEquals( 1, cards.size() );
//		assertEquals( aCard.question, cards.get( 0 ).question );
//	}
//
//	public void testDeleteCard() {
//		Card aCard = new Card( "apple", "the answer" );
//		Card bCard = new Card( "banana", "the answer" );
//		Card cCard = new Card( "coconunt", "the answer" );
//
//		cardSetManager.save( aCard );
//		cardSetManager.save( bCard );
//		cardSetManager.save( cCard );
//
//		Vector<Card> cards = cardSetManager.loadCardsByPrefix( "a" );
//
//		assertEquals( 1, cards.size() );
//		assertEquals( 3, db.getObjects( ScoreHistory.class ).size() );
//		
//		cardSetManager.deleteCard( aCard );
//
//		cards = cardSetManager.loadCardsByPrefix( "a" );
//
//		assertEquals( 0, cards.size() );
//		assertEquals( 2, db.getObjects( ScoreHistory.class ).size() );
//		
//	}
//
//	public void testLoadCardByHeadword() {
//
//		Card aCard = new Card( "apple", "the answer" );
//		Card bCard = new Card( "banana", "the answer" );
//		Card cCard = new Card( "coconunt", "the answer" );
//
//		cardSetManager.save( aCard );
//		cardSetManager.save( bCard );
//		cardSetManager.save( cCard );
//
//		Card c = cardSetManager.loadCardByHeadword( "banana" );
//
//		assertEquals( "banana", c.question );
//	}
//
//	public void testLoadCardByHeadwordIfDuplicateExists() {
//
//		Card bCard1 = new Card( "banana", "the answer" );
//		Card bCard2 = new Card( "banana", "the answer2" );
//		Card cCard = new Card( "coconunt", "the answer" );
//
//		cardSetManager.save( bCard1 );
//		cardSetManager.save( bCard2 );
//		cardSetManager.save( cCard );
//
//		try {
//			cardSetManager.loadCardByHeadword( "banana" );
//			fail();
//		} catch( IllegalStateException ignore ) {
//		}
//	}
//
//	public void testLoadCardByHeadwordIfNoSuchCardExists() {
//
//		Card bCard1 = new Card( "banana", "the answer" );
//
//		cardSetManager.save( bCard1 );
//
//		Card c = cardSetManager.loadCardByHeadword( "fish" );
//
//		assertEquals( null, c );
//	}
//
//	public void testLoadingCardPreservesEasinessHistory() {
//
//		Card bCard1 = new Card( "banana", "the answer" );
//		bCard1.scheduleByGrade( 1 );
//
//		assertEquals( "1,0,0", bCard1.getScoreHistory().toString() );
//
//		cardSetManager.save( bCard1 );
//
//		Card c = cardSetManager.loadCardByHeadword( "banana" );
//
//		assertEquals( "banana", c.question );
//		assertEquals( "1,0,0", c.getScoreHistory().toString() );
//	}
	
	public void testFindCardIndexByWordPrefix () {
		Card card1 = new Card( "apple", "the answer" );		
		Card card2 = new Card( "application", "the answer" );
		Card card3 = new Card( "apply", "the answer" );
		
		cardSetManager.save(card1);
		cardSetManager.save(card2);
		cardSetManager.save(card3);
		
		cardSetManager.loadAllCards();
		
		assertEquals(3, cardSetManager.allCards.size());
		
		assertEquals(0, cardSetManager.findCardIndexByWordPrefix("appl"));
		assertEquals(0, cardSetManager.findCardIndexByWordPrefix("apple"));
		
		assertEquals(1, cardSetManager.findCardIndexByWordPrefix("appli"));
		
		assertEquals(2, cardSetManager.findCardIndexByWordPrefix("apply"));
		
		assertEquals(2, cardSetManager.findCardIndexByWordPrefix("jungle"));
	}
}
