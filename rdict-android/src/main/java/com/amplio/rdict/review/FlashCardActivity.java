package com.amplio.rdict.review;

import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.amplio.rdict.R;
import com.amplio.rdict.RDictActivity;

public class FlashCardActivity extends Activity {
	public static final String SAVE_TAG_STATE = "EX_MGR_STATE";

	TextView progress_label = null;
	
	LinearLayout m_buttonLayout = null;
	
	FlashcardFrontViewWrapper m_flashcardFrontViewWrapper = null;
	FlashcardBackViewWrapper m_flashcardBackViewWrapper = null;
	
	ReviewExerciseFrontButtonsViewWrapper m_reviewExerciseButtonsFront = null;
	ReviewExerciseBackButtonsViewWrapper m_reviewExerciseButtonsBack = null;
	
	public static ReviewExerciseManager m_exerciseMgr = null;
	
	public ViewFlipper m_flashcardMover = null;
	
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.review_exercise);
    	
    	this.progress_label = (TextView) findViewById(R.id.progress_label);
    	this.m_flashcardMover = (ViewFlipper)findViewById(R.id.flashcard_mover);
		this.m_buttonLayout = (LinearLayout)findViewById(R.id.button_layout);
		
		this.m_flashcardFrontViewWrapper = new FlashcardFrontViewWrapper(this.getApplicationContext());
		this.m_flashcardBackViewWrapper = new FlashcardBackViewWrapper(this.getApplicationContext());
		
		this.m_reviewExerciseButtonsFront = new ReviewExerciseFrontButtonsViewWrapper(this.getApplicationContext(), this);
		this.m_reviewExerciseButtonsBack = new ReviewExerciseBackButtonsViewWrapper(this.getApplicationContext(), this);
		
		FlashCardActivity.m_exerciseMgr = new ReviewExerciseManager(this.loadCardSet());
		
		if(null != savedInstanceState && savedInstanceState.containsKey(SAVE_TAG_STATE)) {
			FlashCardActivity.m_exerciseMgr.setState(savedInstanceState.getInt(SAVE_TAG_STATE));
		}
				
		this.drawDisplay();
	}
	
	public void onResume(View v){
		super.onResume();
		
		if(ReviewExerciseManager.STATE_USER_FINISHED_EXERCISE != FlashCardActivity.m_exerciseMgr.getState()) {
			this.drawDisplay();
		}
		else{
			RDictActivity.c_statisticsManager.saveOrUpdateCardStackStatistics();
			this.finish();
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(SAVE_TAG_STATE, FlashCardActivity.m_exerciseMgr.getState());
		super.onSaveInstanceState(outState);
	}
	
	public void drawDisplay() {
		this.setProgressText();
		this.setFlashcardTextViews(FlashCardActivity.m_exerciseMgr.getCard());
		
		this.m_buttonLayout.removeAllViews();
		this.m_buttonLayout.addView(getButtonsView());
		
		this.m_flashcardMover.setInAnimation(AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.push_left_in));
		this.m_flashcardMover.setOutAnimation(AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.push_left_out));
		this.m_flashcardMover.addView(getAFlashcardSide());
		this.m_flashcardMover.showNext();
		
		if(1 < this.m_flashcardMover.getChildCount())
			this.m_flashcardMover.removeViewAt(0);
	}

	private View getAFlashcardSide() {
		if(ReviewExerciseManager.STATE_USER_STARTED_EXERCISE == FlashCardActivity.m_exerciseMgr.getState()
				|| ReviewExerciseManager.STATE_LOADED_NEXT_CARD == FlashCardActivity.m_exerciseMgr.getState()) {
			return this.m_flashcardFrontViewWrapper.getView();
		}
		else if(ReviewExerciseManager.STATE_USER_PRESSED_VIEW_ANSWER == FlashCardActivity.m_exerciseMgr.getState()) {
			return this.m_flashcardBackViewWrapper.getView();
		}
		else
			return null;
    }
	
	private View getButtonsView() {
		if(ReviewExerciseManager.STATE_USER_STARTED_EXERCISE == FlashCardActivity.m_exerciseMgr.getState()
				|| ReviewExerciseManager.STATE_LOADED_NEXT_CARD == FlashCardActivity.m_exerciseMgr.getState())
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
			default:
				throw new IllegalArgumentException("Bad argument in FlashCardActivity");
		}
	}
	
	private void setProgressText() {
	    int cardsLeft = FlashCardActivity.m_exerciseMgr.getCardCount() 
						- (FlashCardActivity.m_exerciseMgr.getCardIndex() + 1);
		if (cardsLeft > 0)
			this.progress_label.setText(cardsLeft   + " more " + ">");
		else
			this.progress_label.setText("Last Card!");
    }
	
	public void setFlashcardTextViews(Card c) {
		if(ReviewExerciseManager.STATE_USER_STARTED_EXERCISE == FlashCardActivity.m_exerciseMgr.getState()
				|| ReviewExerciseManager.STATE_LOADED_NEXT_CARD == FlashCardActivity.m_exerciseMgr.getState()) {
			this.m_flashcardFrontViewWrapper.setWord(c.question);
		}
		else if(ReviewExerciseManager.STATE_USER_PRESSED_VIEW_ANSWER == FlashCardActivity.m_exerciseMgr.getState()) {
			this.m_flashcardBackViewWrapper.setWordAndDef(c.question, c.answer);
		}
	}
}
