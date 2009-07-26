package com.amplio.rdict.review;

import com.db4o.ObjectContainer;

public class ReviewManager {
	public static final int EXERCISES_SCHEDULED_TODAY = 0;
	public static final int EXERCISES_OTHER = 1;
	public static final int EXERCISES_CARD_DB_IS_EMPTY = 2;
	public static final int EXERCISES_CARDS_LOOKEDUP_TODAY = 3;
	public static final int EXERCISES_CARDS_TOP_N_HARDEST = 4;
	
	public int availableExercises = 0;
	
	public CardSetManager cardsMgr = null;
	
	public boolean isAvailableTodaysScheduledExercise = false;
	public boolean isAvailableLookedupTodayExercise = false;
	public boolean isAvailableTOPNExercise = false;
	
	public ReviewManager(ObjectContainer db){
		this.cardsMgr = new CardSetManager(db);
	}
	
	public void checkAvailableExercises() {
		this.isAvailableTodaysScheduledExercise = ( 0 != this.cardsMgr.loadCardsScheduledForToday().size());
		this.isAvailableLookedupTodayExercise = ( 0 != this.cardsMgr.loadCardsLookedupToday().size());
		this.isAvailableTOPNExercise = ( 0 != this.cardsMgr.loadTopNHardestCards(20).size());
	}

	public void checkStudyAvailableStudyModes() {
		this.checkAvailableExercises();
		
		if(this.isAvailableTodaysScheduledExercise){
			this.availableExercises = EXERCISES_SCHEDULED_TODAY;
		}
		else {
			if(this.isAvailableLookedupTodayExercise || this.isAvailableTOPNExercise)
				this.availableExercises = EXERCISES_OTHER;
			else
				this.availableExercises = EXERCISES_CARD_DB_IS_EMPTY;
		}
	}
}
