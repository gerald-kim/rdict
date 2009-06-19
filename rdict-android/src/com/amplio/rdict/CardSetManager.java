package com.amplio.rdict;

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
		query.descend("scheduled").constrain(scheduledDate);
		ObjectSet cards = query.execute();
		return cards;
	}


}
