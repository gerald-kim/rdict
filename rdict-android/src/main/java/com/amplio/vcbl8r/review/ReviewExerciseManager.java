package com.amplio.vcbl8r.review;

import java.util.Vector;

public class ReviewExerciseManager {
	public static final int GRADE_EASY = 4;
	public static final int GRADE_NOT_BAD = 3;
	public static final int GRADE_HARD = 2;
	public static final int GRADE_I_FORGOT = 1;

	public static final int STATE_USER_STARTED_EXERCISE = 0;
	public static final int STATE_USER_PRESSED_VIEW_ANSWER = 1;
	public static final int STATE_USER_PRESSED_EASINESS_BUTTON = 2;
	public static final int STATE_USER_FINISHED_EXERCISE = 3;
	public static final int STATE_LOADED_NEXT_CARD = 4;
	
	private int state = -1;
	private Vector<Card> cards = null;
	private int cardIndex = 0;
	
	public ReviewExerciseManager(Vector<Card> cards) {
		this.state = STATE_USER_STARTED_EXERCISE;
		this.cards = cards;
	}
	
	public void userPressedViewAnswerButton() {
		this.state = STATE_USER_PRESSED_VIEW_ANSWER;    
    }

	public void userPressedAnEasinessButton(int grade) {
		this.state = STATE_USER_PRESSED_EASINESS_BUTTON;
		
		Card c = this.cards.elementAt(this.cardIndex);
		c.scheduleByGrade(grade);
    }
	
	public void next() {
		this.cardIndex++;
			
		if(this.cardIndex < this.cards.size())
			this.state = STATE_LOADED_NEXT_CARD;
		else
			this.state = STATE_USER_FINISHED_EXERCISE;
	}
	
	public int getState() {
	    return state;
    }

	public int getCardIndex() {
	    return cardIndex ;
    }

	public Card getCard() {
	    return this.cards.get(this.cardIndex);
    }

	public int getCardCount() {
		return this.cards.size();
    }

	public void setState(int state) {
	    this.state = state;
    }

}
