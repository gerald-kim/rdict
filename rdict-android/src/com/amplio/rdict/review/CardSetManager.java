package com.amplio.rdict.review;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

public class CardSetManager {
	public ObjectContainer db = null;
	
	public CardSetManager (ObjectContainer db) {
		this.db = db;
	}
	
	public void save(Card card) {
		db.store(card);
		db.commit();
	}
	
	public void deleteCard(Card card) {
		db.delete(card);
		db.commit();
	}
	
	public Vector<Card> loadCardsByScheduledDate(String scheduledDate) {
		Vector<Card> v = new Vector<Card>();
		
		Query query= db.query();
		query.constrain(Card.class);
		query.descend("date_scheduled").constrain(scheduledDate);
		ObjectSet cards = query.execute();
		
		for(int i = 0; i < cards.size(); i++)
			v.add((Card)cards.get(i));
		
		return v;
	}

	public Vector<Card> loadCardsScheduledForToday() {
        String todaysDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		return this.loadCardsByScheduledDate(todaysDate);
	}

	public Vector<Card> loadCardsLookedupToday() {
		Vector<Card> v = new Vector<Card>();
		
		String todaysDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		
		Query query = db.query();
		query.constrain(Card.class);
		query.descend("date_lookedup").constrain(todaysDate);
		ObjectSet cards = query.execute();
		
		for(int i = 0; i < cards.size(); i++)
			v.add((Card)cards.get(i));
		
		return v;
	}

	public Vector<Card> loadTopNHardestCards(int n) {
		Vector<Card> v = new Vector<Card>();
		
		Query query= db.query();
		query.constrain(Card.class);
		query.descend("easiness").orderAscending();
		ObjectSet cards = query.execute();
		
		for(int i = 0; i < 20 && i < cards.size(); i++)
			v.add((Card)cards.get(i));
		
		return v;
	}

	public Vector<Card> loadCardsByPrefix(String prefix) {
		Vector<Card> v = new Vector<Card>();
		
		Query query= db.query();
		query.constrain(Card.class);
		
		if(! "".equals(prefix))
			query.descend("question").constrain(prefix).startsWith(true);
		
		query.descend("question").orderAscending();
		
		ObjectSet cards = query.execute();
		
		for(int i = 0; i < 20 && i < cards.size(); i++)
			v.add((Card)cards.get(i));
		
		return v;
	}

	public Card loadCardByHeadword(String headword) {
		Query query = db.query();
		query.constrain(Card.class);
		query.descend("question").constrain(headword);
		ObjectSet cards = query.execute();
		
		if(1 < cards.size())
			throw new IllegalStateException("Duplicates cards exist in the database.");
		else if (1 == cards.size())
			return (Card) cards.get(0);
		else
			return null;
	}
}
