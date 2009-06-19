package com.amplio.rdict;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

public class CardSetManager {
	
	public static final int CARD_SET_TODAYS_SCHEDULED = 0;
	public static final int CARD_SET_TOP_N_HARDEST = 1;
	public static final int CARD_SET_WORDS_SEARCHED_TODAY = 2;
	
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
		return cards.subList(0, 20).toArray();
	}


}
