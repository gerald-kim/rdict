package com.amplio.rdict.tests;

import junit.framework.TestCase;

import com.amplio.rdict.Card;
import com.amplio.rdict.CardSetManager;
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
		cardFor19700101.scheduled = "19700101";
		Card cardFor19700102 = new Card("1970 second day", "the answer");
		cardFor19700102.scheduled = "19700102";
		
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
}
