package com.amplio.rdict.review;

import java.text.SimpleDateFormat;
import java.util.Date;

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
	}
	
	public ObjectSet loadCardsByScheduledDate(String scheduledDate) {
		Query query= db.query();
		query.constrain(Card.class);
		query.descend("date_scheduled").constrain(scheduledDate);
		ObjectSet cards = query.execute();
		return cards;
	}

	public ObjectSet loadCardsScheduledForToday() {
        String todaysDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		return this.loadCardsByScheduledDate(todaysDate);
	}

	public ObjectSet loadCardsLookedupToday() {
		String todaysDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		
		Query query= db.query();
		query.constrain(Card.class);
		query.descend("date_lookedup").constrain(todaysDate);
		ObjectSet cards = query.execute();
		return cards;
	}

	public Object[] loadTopNHardestCards(int n) {
		Query query= db.query();
		query.constrain(Card.class);
		query.descend("easiness").orderAscending();
		ObjectSet cards = query.execute();
		
		if(cards.size() < 20)
			return cards.toArray();
		else
			return cards.subList(0, 20).toArray();
	}


}
