package com.amplio.rdict.review;

import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amplio.rdict.R;
import com.amplio.rdict.RDictActivity;

public class FlashCardActivity extends Activity implements OnClickListener{
	TextView progress_label = null;
	
	LinearLayout m_flashcardLayout = null;
	LinearLayout m_buttonLayout = null;
	
	View m_flashcardFrontView = null;
	TextView m_frontWordLabel = null;
	
	View m_flashcardBackView = null;
	TextView m_backWordLabel = null;
	TextView m_defLabel = null;
	
	View m_reviewExerciseButtonsFront = null;
	View m_reviewExerciseButtonsBack = null;
	
	Button view_answer_button = null;
	
	Button easy_button = null;
	Button not_bad_button = null;
	Button hard_button = null;
	Button i_forgot_button = null;
	
	private int cardSetIndex = 0;
	private Vector<Card> cardSet = null;
	
	private Card m_card = null;
	
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.review_exercise);
    	
    	this.progress_label = (TextView) findViewById(R.id.progress_label);
		this.m_flashcardLayout = (LinearLayout)findViewById(R.id.flashcard_layout);
		this.m_buttonLayout = (LinearLayout)findViewById(R.id.button_layout);
		
		this.m_flashcardFrontView = View.inflate( this.getApplicationContext(), R.layout.flashcard_front, null);
		this.m_frontWordLabel = (TextView) this.m_flashcardFrontView.findViewById(R.id.front_headword_label);
		
		this.m_flashcardBackView = View.inflate( this.getApplicationContext(), R.layout.flashcard_back, null);
		this.m_backWordLabel = (TextView) this.m_flashcardBackView.findViewById(R.id.back_headword_label);
		this.m_defLabel = (TextView) this.m_flashcardBackView.findViewById(R.id.definition_label);
		
		this.m_reviewExerciseButtonsFront = View.inflate( this.getApplicationContext(), R.layout.review_exercise_buttons_front, null);
		this.view_answer_button = (Button) this.m_reviewExerciseButtonsFront.findViewById(R.id.view_answer_button);
		this.view_answer_button.setOnClickListener(this);
		
		this.m_reviewExerciseButtonsBack = View.inflate( this.getApplicationContext(), R.layout.review_exercise_buttons_back, null);
		this.easy_button = (Button) this.m_reviewExerciseButtonsBack.findViewById(R.id.easy_button);
		this.easy_button.setOnClickListener(this);
		this.not_bad_button = (Button) this.m_reviewExerciseButtonsBack.findViewById(R.id.not_bad_button);
		this.not_bad_button.setOnClickListener(this);
		this.hard_button = (Button) this.m_reviewExerciseButtonsBack.findViewById(R.id.hard_button);
		this.hard_button.setOnClickListener(this);
		this.i_forgot_button = (Button) this.m_reviewExerciseButtonsBack.findViewById(R.id.i_forgot_button);
		this.i_forgot_button.setOnClickListener(this);
		
		System.out.println("FlashCard - created");
		
		this.initializeCardActivity();
	}
	
	public void onStart(View v){
		System.out.println("Flashcard - started");
		this.initializeCardActivity();
		super.onStart();
	}
	
	public void onResume(View v){
		System.out.println("FlashCard - resumed");
		this.initializeCardActivity();
		
		super.onResume();
	}
	
	public void onPause(View v){
		super.onPause();
	}
	
	public void initializeCardActivity(){
		switch(ReviewActivity.reviewMode){
			case ReviewManager.EXERCISES_SCHEDULED_TODAY: 
				this.cardSet = RDictActivity.c_cardSetManager.loadCardsScheduledForToday();
				break;
			case ReviewManager.EXERCISES_CARDS_LOOKEDUP_TODAY:
				this.cardSet = RDictActivity.c_cardSetManager.loadCardsLookedupToday();
				break;
			case ReviewManager.EXERCISES_CARDS_TOP_N_HARDEST:
				this.cardSet = RDictActivity.c_cardSetManager.loadTopNHardestCards(CardExerciseWrapper.N);
				break;
			default:
				System.out.println("Bad argument in FlashCardActivity");
		}
		
		this.progress_label.setText("Card " + (cardSetIndex + 1) + " of "+  (this.cardSet.size()) );
		
		this.m_card = cardSet.elementAt(cardSetIndex);
		
		this.m_flashcardLayout.removeAllViews();
		this.m_flashcardLayout.addView(this.m_flashcardFrontView);
		
		this.m_buttonLayout.removeAllViews();
		this.m_buttonLayout.addView(this.m_reviewExerciseButtonsFront);
		
		this.m_frontWordLabel.setText(this.m_card.question);
	}
	
	public void onClick(View v) {
		if(cardSetIndex < cardSet.size()){
			if(R.id.view_answer_button == v.getId()){
				this.m_flashcardLayout.removeAllViews();
				this.m_flashcardLayout.addView(this.m_flashcardBackView);
				this.m_backWordLabel.setText(this.m_card.question);
				this.m_defLabel.setText(this.m_card.answer.replace("%20", " "));
				
				this.m_buttonLayout.removeAllViews();
				this.m_buttonLayout.addView(this.m_reviewExerciseButtonsBack);
			}
			else {
				Card c = cardSet.elementAt(cardSetIndex);
				c.adjustEasinessByGrade(getGradeByButton(v));
				c.schedule();
				
				RDictActivity.c_cardSetManager.save(c);
				
				cardSetIndex++;
				
				if(cardSetIndex < cardSet.size()){
					this.progress_label.setText("Card " + (cardSetIndex + 1) + " of "+  (this.cardSet.size()) );
					
					this.m_card = cardSet.elementAt(cardSetIndex);
					this.m_flashcardLayout.removeAllViews();
					this.m_flashcardLayout.addView(this.m_flashcardFrontView);
					this.m_frontWordLabel.setText(this.m_card.question);
					
					this.m_buttonLayout.removeAllViews();
					this.m_buttonLayout.addView(this.m_reviewExerciseButtonsFront);
				}
				else{
					RDictActivity.c_statisticsManager.saveOrUpdateCardStackStatistics();
					this.finish();
					System.out.println("Finished reviewing");
				}
			}
		}
	}

	private int getGradeByButton(View view) {
		if(view.equals(this.easy_button)) return 4;
		else if (view.equals(this.not_bad_button)) return 3;
		else if (view.equals(this.hard_button)) return 2;
		else return 1; // i forgot button
	}
	
}
