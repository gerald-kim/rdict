package com.amplio.rdict.review;

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
	
	private int m_state = -1;
	private Vector<Card> m_cards = null;
	private int m_cardIndex = 0;
	
	public ReviewExerciseManager(Vector<Card> cards) {
		this.m_state = STATE_USER_STARTED_EXERCISE;
		this.m_cards = cards;
	}
	
	public void userPressedViewAnswerButton() {
		this.m_state = STATE_USER_PRESSED_VIEW_ANSWER;    
    }

	public void userPressedAnEasinessButton(int grade) {
		this.m_state = STATE_USER_PRESSED_EASINESS_BUTTON;
		
		Card c = this.m_cards.elementAt(this.m_cardIndex);
		c.study( grade );
    }
	
	public void next() {
		this.m_cardIndex++;
			
		if(this.m_cardIndex < this.m_cards.size())
			this.m_state = STATE_LOADED_NEXT_CARD;
		else
			this.m_state = STATE_USER_FINISHED_EXERCISE;
	}
	
	public int getState() {
	    return m_state;
    }

	public int getCardIndex() {
	    return m_cardIndex ;
    }

	public Card getCard() {
	    return this.m_cards.get(this.m_cardIndex);
    }

	public int getCardCount() {
		return this.m_cards.size();
    }

}
