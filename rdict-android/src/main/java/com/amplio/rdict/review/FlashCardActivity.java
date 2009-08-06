package com.amplio.rdict.review;

import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amplio.rdict.R;
import com.amplio.rdict.RDictActivity;

public class FlashCardActivity extends Activity {
	TextView progress_label = null;
	
	LinearLayout m_flashcardLayout = null;
	LinearLayout m_buttonLayout = null;
	
	FlashcardFrontViewWrapper m_flashcardFrontViewWrapper = null;
	FlashcardBackViewWrapper m_flashcardBackViewWrapper = null;
	
	ReviewExerciseFrontButtonsViewWrapper m_reviewExerciseButtonsFront = null;
	ReviewExerciseBackButtonsViewWrapper m_reviewExerciseButtonsBack = null;
	
	public static ReviewExerciseManager m_exerciseMgr = null;
	
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.review_exercise);
    	
    	this.progress_label = (TextView) findViewById(R.id.progress_label);
    	
		this.m_flashcardLayout = (LinearLayout)findViewById(R.id.flashcard_layout);
		this.m_buttonLayout = (LinearLayout)findViewById(R.id.button_layout);
		
		this.m_flashcardFrontViewWrapper = new FlashcardFrontViewWrapper(this.getApplicationContext());
		this.m_flashcardBackViewWrapper = new FlashcardBackViewWrapper(this.getApplicationContext());
		
		this.m_reviewExerciseButtonsFront = new ReviewExerciseFrontButtonsViewWrapper(this.getApplicationContext(), this);
		this.m_reviewExerciseButtonsBack = new ReviewExerciseBackButtonsViewWrapper(this.getApplicationContext(), this);
		
		System.out.println("FlashCard - created");
		
		FlashCardActivity.m_exerciseMgr = new ReviewExerciseManager(this.loadCardSet());
		
		this.drawDisplay();
	}
	
	public void onResume(View v){
		super.onResume();
		
		if(ReviewExerciseManager.STATE_EXERCISE_FINISHED != FlashCardActivity.m_exerciseMgr.getState()) {
			this.drawDisplay();
		}
		else{
			RDictActivity.c_statisticsManager.saveOrUpdateCardStackStatistics();
			this.finish();
		}
	}
	
	public void drawDisplay() {
		this.progress_label.setText("Card " + (FlashCardActivity.m_exerciseMgr.getCardIndex() + 1) 
				+ " of "+  (FlashCardActivity.m_exerciseMgr.getCardCount()) );
		
		this.m_flashcardLayout.removeAllViews();
		this.m_flashcardLayout.addView(getFlashcardView());
		
		this.m_buttonLayout.removeAllViews();
		this.m_buttonLayout.addView(getButtonsView());		
	}

	private View getFlashcardView() {
		Card c = FlashCardActivity.m_exerciseMgr.getCard();
		
		if(ReviewExerciseManager.STATE_IS_SHOWING_CARD_FRONT == FlashCardActivity.m_exerciseMgr.getState()) {
			this.m_flashcardFrontViewWrapper.setWord(c.question);
			return this.m_flashcardFrontViewWrapper.getView();
		}
		else {
			this.m_flashcardBackViewWrapper.setWordAndDef(c.question, c.answer.replace("%20", " "));
			return this.m_flashcardBackViewWrapper.getView();
		}
    }
	
	private View getButtonsView() {
		if(ReviewExerciseManager.STATE_IS_SHOWING_CARD_FRONT == FlashCardActivity.m_exerciseMgr.getState())
			return this.m_reviewExerciseButtonsFront.getView();
		else
			return this.m_reviewExerciseButtonsBack.getView();
    }
	
	private Vector<Card> loadCardSet() {
	    switch(ReviewActivity.reviewMode){
			case ReviewManager.EXERCISES_SCHEDULED_TODAY: 
				return RDictActivity.c_cardSetManager.loadCardsScheduledForToday();
			case ReviewManager.EXERCISES_CARDS_LOOKEDUP_TODAY:
				return RDictActivity.c_cardSetManager.loadCardsLookedupToday();
			case ReviewManager.EXERCISES_CARDS_TOP_N_HARDEST:
				return RDictActivity.c_cardSetManager.loadTopNHardestCards(CardExerciseWrapper.N);
			default:
				throw new IllegalArgumentException("Bad argument in FlashCardActivity");
		}
    }	
}
