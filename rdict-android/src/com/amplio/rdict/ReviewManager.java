package com.amplio.rdict;

import com.db4o.ObjectContainer;

public class ReviewManager {
	public static final int PRACTICE_SCHEDULED_TODAY = 0;
	public static final int PRACTICE_OTHER = 1;
	
	public int studyMode = 0;
	
	public CardSetManager cardsMgr = null;
	
	public ReviewManager(ObjectContainer db){
		this.cardsMgr = new CardSetManager(db);
	}

	public void checkAvailableStudyModes() {
		int cardsScheduledForToday = this.cardsMgr.loadCardsScheduledForToday().size();
		
		if(cardsScheduledForToday != 0)
			this.studyMode = PRACTICE_SCHEDULED_TODAY;
		else
			this.studyMode = PRACTICE_OTHER;
	}
	
	public String getReviewHomeGreeting() {
		if(PRACTICE_SCHEDULED_TODAY == this.studyMode)
			return "You have cards to practice today.";
		else
			return "You have finished studying for today.";
	}
}
