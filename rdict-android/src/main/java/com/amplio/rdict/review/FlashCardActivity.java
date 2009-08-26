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
	
	LinearLayout buttonLayout = null;
	
	FlashcardFrontViewWrapper flashcardFrontViewWrapper = null;
	FlashcardBackViewWrapper flashcardBackViewWrapper = null;
	
	ReviewExerciseFrontButtonsViewWrapper reviewExerciseButtonsFront = null;
	ReviewExerciseBackButtonsViewWrapper reviewExerciseButtonsBack = null;
	
	public static ReviewExerciseManager exerciseMgr = null;
	
	public ViewFlipper flashcardMover = null;
	
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.review_exercise);
    	
    	this.progress_label = (TextView) findViewById(R.id.progress_label);
    	this.flashcardMover = (ViewFlipper)findViewById(R.id.flashcard_mover);
		this.buttonLayout = (LinearLayout)findViewById(R.id.button_layout);
		
		this.flashcardFrontViewWrapper = new FlashcardFrontViewWrapper(this.getApplicationContext());
		this.flashcardBackViewWrapper = new FlashcardBackViewWrapper(this.getApplicationContext());
		
		this.reviewExerciseButtonsFront = new ReviewExerciseFrontButtonsViewWrapper(this.getApplicationContext(), this);
		this.reviewExerciseButtonsBack = new ReviewExerciseBackButtonsViewWrapper(this.getApplicationContext(), this);
		
		FlashCardActivity.exerciseMgr = new ReviewExerciseManager(this.loadCardSet());
		
		if(null != savedInstanceState && savedInstanceState.containsKey(SAVE_TAG_STATE)) {
			FlashCardActivity.exerciseMgr.setState(savedInstanceState.getInt(SAVE_TAG_STATE));
		}
				
		this.drawDisplay();
	}
	
	public void onResume(View v){
		super.onResume();
		
		if(ReviewExerciseManager.STATE_USER_FINISHED_EXERCISE != FlashCardActivity.exerciseMgr.getState()) {
			this.drawDisplay();
		}
		else{
			RDictActivity.c_statisticsManager.saveOrUpdateCardStackStatistics();
			this.finish();
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(SAVE_TAG_STATE, FlashCardActivity.exerciseMgr.getState());
		super.onSaveInstanceState(outState);
	}
	
	public void drawDisplay() {
		this.setProgressText();
		this.setFlashcardTextViews(FlashCardActivity.exerciseMgr.getCard());
		
		this.buttonLayout.removeAllViews();
		this.buttonLayout.addView(getButtonsView());
		
		this.flashcardMover.setInAnimation(AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.push_left_in));
		this.flashcardMover.setOutAnimation(AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.push_left_out));
		this.flashcardMover.addView(getAFlashcardSide());
		this.flashcardMover.showNext();
		
		if(1 < this.flashcardMover.getChildCount())
			this.flashcardMover.removeViewAt(0);
	}

	private View getAFlashcardSide() {
		if(ReviewExerciseManager.STATE_USER_STARTED_EXERCISE == FlashCardActivity.exerciseMgr.getState()
				|| ReviewExerciseManager.STATE_LOADED_NEXT_CARD == FlashCardActivity.exerciseMgr.getState()) {
			return this.flashcardFrontViewWrapper.getView();
		}
		else if(ReviewExerciseManager.STATE_USER_PRESSED_VIEW_ANSWER == FlashCardActivity.exerciseMgr.getState()) {
			return this.flashcardBackViewWrapper.getView();
		}
		else
			return null;
    }
	
	private View getButtonsView() {
		if(ReviewExerciseManager.STATE_USER_STARTED_EXERCISE == FlashCardActivity.exerciseMgr.getState()
				|| ReviewExerciseManager.STATE_LOADED_NEXT_CARD == FlashCardActivity.exerciseMgr.getState())
			return this.reviewExerciseButtonsFront.getView();
		else
			return this.reviewExerciseButtonsBack.getView();
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
	    int cardsLeft = FlashCardActivity.exerciseMgr.getCardCount() 
						- (FlashCardActivity.exerciseMgr.getCardIndex() + 1);
		if (cardsLeft > 0)
			this.progress_label.setText(cardsLeft   + " more " + ">");
		else
			this.progress_label.setText("Last Card!");
    }
	
	public void setFlashcardTextViews(Card c) {
		if(ReviewExerciseManager.STATE_USER_STARTED_EXERCISE == FlashCardActivity.exerciseMgr.getState()
				|| ReviewExerciseManager.STATE_LOADED_NEXT_CARD == FlashCardActivity.exerciseMgr.getState()) {
			this.flashcardFrontViewWrapper.setWord(c.question);
		}
		else if(ReviewExerciseManager.STATE_USER_PRESSED_VIEW_ANSWER == FlashCardActivity.exerciseMgr.getState()) {
			this.flashcardBackViewWrapper.setWordAndDef(c.question, c.answer);
		}
	}
}
