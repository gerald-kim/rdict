package com.amplio.rdict.review;

import org.neodatis.odb.ODB;

public class ReviewManager {
	public static final int EXERCISES_SCHEDULED_TODAY = 0;
	public static final int EXERCISES_OTHER = 1;
	public static final int EXERCISES_CARD_DB_IS_EMPTY = 2;
	public static final int EXERCISES_CARDS_LOOKEDUP_TODAY = 3;

	public int availableExercises = 0;

	private CardSetManager cardSetManager = null;

	public boolean isAvailableTodaysScheduledExercise = false;
	public boolean isAvailableLookedupTodayExercise = false;
	@SuppressWarnings( "unused" )
	private ODB odb;

	public ReviewManager( ODB db, CardSetManager cardSetManager ) {
		this.odb = db;
		this.cardSetManager = cardSetManager;
	}

	public void checkAvailableExercises() {
		this.isAvailableTodaysScheduledExercise = (0 != cardSetManager
		        .loadCardsScheduledForToday().size());
		this.isAvailableLookedupTodayExercise = (0 != cardSetManager.loadCardsLookedupToday()
		        .size());
	}

	public void checkStudyAvailableStudyModes() {
		this.checkAvailableExercises();

		if( this.isAvailableTodaysScheduledExercise ) {
			this.availableExercises = EXERCISES_SCHEDULED_TODAY;
		} else {
			if( this.isAvailableLookedupTodayExercise )
				this.availableExercises = EXERCISES_OTHER;
			else
				this.availableExercises = EXERCISES_CARD_DB_IS_EMPTY;
		}
	}
}
