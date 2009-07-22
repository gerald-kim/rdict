package com.amplio.rdict.tests.review;

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import com.amplio.rdict.review.Card;
import com.amplio.rdict.review.CardSetManager;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class CardSetManagerTest extends TestCase {
	
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
	
	public void testSaveCardsWithDb4o(){
		Card c1 = new Card("Q", "A");
		
		db.store(c1);
		
		db.close();
		
		db = Db4o.openFile(DB_TEST_FILE);
		
		ObjectSet cards = db.query(Card.class);
		
		assertEquals(1, cards.size());
		assertEquals("Q", ((Card)cards.get(0)).question);
	}
	
	public void testSaveCard() {
		CardSetManager mgr = new CardSetManager(db);
		
		Card cardForToday = new Card("today" , "the answer");
		
		mgr.save(cardForToday);
		
		ObjectSet cards = db.query(Card.class);
		
		assertEquals(1, cards.size());
		assertEquals("today", ((Card)cards.get(0)).question);
	}
	
	public void testLoadScheduledCards() {
		CardSetManager mgr = new CardSetManager(db);
		
		Card cardForToday = new Card("today" , "the answer");
		Card cardFor19700101 = new Card("1970 baby yeah!", "the answer");
		cardFor19700101.date_scheduled = "19700101";
		Card cardFor19700102 = new Card("1970 second day", "the answer");
		cardFor19700102.date_scheduled = "19700102";
		
		mgr.save(cardForToday);
		mgr.save(cardFor19700101);
		mgr.save(cardFor19700102);
		
		ObjectSet cards = mgr.loadCardsByScheduledDate("19700101");
		
		assertEquals(1, cards.size());
		
		Card card = (Card) cards.get(0);
		
		assertEquals("1970 baby yeah!", card.question);

		cards = mgr.loadCardsByScheduledDate("19700102");
		
		assertEquals(1, cards.size());
		
		card = (Card) cards.get(0);
		
		assertEquals("1970 second day", card.question);
	}
	
	public void testLoadScheduledCardsButNoneScheduled() {	
		Card cardForToday = new Card("today", "the answer");
		
		CardSetManager mgr = new CardSetManager(db);
		
		mgr.save(cardForToday);
		
		ObjectSet cardsScheduled = mgr.loadCardsByScheduledDate("19700101");
		
		assertEquals(0, cardsScheduled.size());
	}
	
	public void testLoadCardsScheduledForToday() {
		Card cardLookedupToday = new Card("lookeduptoday","an answer");
        cardLookedupToday.schedule();
        
        Card cardScheduledForToday = new Card("today", "the answer");
        cardScheduledForToday.date_scheduled = new SimpleDateFormat("yyyyMMdd").format(new Date());
		
		CardSetManager mgr = new CardSetManager(db);
		mgr.save(cardLookedupToday);
		mgr.save(cardScheduledForToday);
		
		ObjectSet cards = mgr.loadCardsScheduledForToday();
		
		assertEquals(1, cards.size());

		Card card = (Card) cards.get(0);
		assertEquals("today", card.question);
	}
	
	public void testLoadCardsLookedupToday() {
		Card cardLookedupToday = new Card("lookeduptoday","an answer");
		Card cardLookedupIn1970 = new Card("today", "the answer");
		cardLookedupIn1970.date_lookedup = "19700101";
		
		CardSetManager mgr = new CardSetManager(db);
		mgr.save(cardLookedupToday);
		mgr.save(cardLookedupIn1970);
		
		ObjectSet cards = mgr.loadCardsLookedupToday();
		
		assertEquals(1, cards.size());

		Card card = (Card) cards.get(0);
		assertEquals("lookeduptoday", card.question);
	}
	
	public void testLoadTopNHardestCards() {
		int n = 20;
		int totalNumCards = n + 2;
		
		Card[] hardCards = new Card[totalNumCards];
		
		for(int i = 0; i < totalNumCards - 1; i++){
			Card c = new Card("hardCard", "answer");
			c.easiness = 1;
			hardCards[i] = c;
		}
		
		Card easyCard = new Card("easyCard", "answer");
		easyCard.easiness = 4;
		hardCards[totalNumCards - 1] = easyCard;

		CardSetManager mgr = new CardSetManager(db);
		for(int i = 0; i < totalNumCards; i++){
			mgr.save(hardCards[i]);
		}
		
		Object[] cards = mgr.loadTopNHardestCards(n);
		
		assertEquals(n, cards.length);

		for(int i = 0; i < n; i++){
			Card card = (Card) cards[i];
			assertEquals(1.0, card.easiness);
		}
	}
	
	public void testLoadByPrefixIfNoneGiven(){
		CardSetManager mgr = new CardSetManager(db);
		
		Card aCard = new Card("apple" , "the answer");
		Card bCard = new Card("banana", "the answer");
		Card cCard= new Card("coconunt", "the answer");

		mgr.save(bCard);
		mgr.save(aCard);
		mgr.save(cCard);
		
		ObjectSet cards = mgr.loadCardsByPrefix("");
		
		assertEquals(3, cards.size());
		assertEquals(aCard.question, ((Card) cards.get(0)).question);
		assertEquals(bCard.question, ((Card) cards.get(1)).question);
		assertEquals(cCard.question, ((Card) cards.get(2)).question);
	}
	
	public void testLoadByPrefix(){
		CardSetManager mgr = new CardSetManager(db);
		
		Card aCard = new Card("apple" , "the answer");
		Card bCard = new Card("banana", "the answer");
		Card cCard= new Card("coconunt", "the answer");

		mgr.save(aCard);
		mgr.save(bCard);
		mgr.save(cCard);
		
		ObjectSet cards = mgr.loadCardsByPrefix("a");
		
		assertEquals(1, cards.size());
		assertEquals(aCard.question, ((Card) cards.get(0)).question);
	}
	
	public void testDeleteCard() {
		CardSetManager mgr = new CardSetManager(db);
		
		Card aCard = new Card("apple" , "the answer");
		Card bCard = new Card("banana", "the answer");
		Card cCard= new Card("coconunt", "the answer");

		mgr.save(aCard);
		mgr.save(bCard);
		mgr.save(cCard);
		
		ObjectSet cards = mgr.loadCardsByPrefix("a");
		
		assertEquals(1, cards.size());
		
		mgr.deleteCard(aCard);
		
		cards = mgr.loadCardsByPrefix("a");
		
		assertEquals(0, cards.size());
	}
	
	public void testLoadCardByHeadword() {
		CardSetManager mgr = new CardSetManager(db);
		
		Card aCard = new Card("apple" , "the answer");
		Card bCard = new Card("banana", "the answer");
		Card cCard= new Card("coconunt", "the answer");

		mgr.save(aCard);
		mgr.save(bCard);
		mgr.save(cCard);
		
		Card c = mgr.loadCardByHeadword("banana");
		
		assertEquals("banana", c.question);
	}
	
	public void testLoadCardByHeadwordIfDuplicateExists() {
		CardSetManager mgr = new CardSetManager(db);
		
		Card bCard1 = new Card("banana", "the answer");
		Card bCard2 = new Card("banana", "the answer2");
		Card cCard= new Card("coconunt", "the answer");

		mgr.save(bCard1);
		mgr.save(bCard2);
		mgr.save(cCard);
		
		try {
			mgr.loadCardByHeadword("banana");
			fail();
		}
		catch(IllegalStateException ignore){}
	}
	
	public void testLoadCardByHeadwordIfNoSuchCardExists() {
		CardSetManager mgr = new CardSetManager(db);
		
		Card bCard1 = new Card("banana", "the answer");
		
		mgr.save(bCard1);
		
		Card c = mgr.loadCardByHeadword("fish");

		assertEquals(null, c);
	}
	
	public void testLoadingCardPreservesEasinessHistory() {
		CardSetManager mgr = new CardSetManager(db);
		
		Card bCard1 = new Card("banana", "the answer");
		bCard1.adjustEasinessByGrade(1);
		
		assertEquals("2.5, 2.5, 0.0", bCard1.getEasinessHistory().toString());
		
		mgr.save(bCard1);
		
		Card c = mgr.loadCardByHeadword("banana");

		assertEquals("banana", c.question);
		assertEquals("2.5, 2.5, 0.0", c.getEasinessHistory().toString());
	}
}
