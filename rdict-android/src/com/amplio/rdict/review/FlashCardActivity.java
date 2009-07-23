package com.amplio.rdict.review;

import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.amplio.rdict.R;
import com.amplio.rdict.RDictActivity;

public class FlashCardActivity extends Activity implements OnClickListener{
	TextView progress_label = null;
	TextView word_label = null;
	WebView def_label = null;
	
	Button view_answer_button = null;
	
	Button easy_button = null;
	Button not_bad_button = null;
	Button hard_button = null;
	Button i_forgot_button = null;
	
	private int cardSetIndex = 0;
	private Vector<Card> cardSet = null;
	
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);		
		setContentView(R.layout.flashcard);
		
		this.progress_label = (TextView)findViewById(R.id.progress_label);
		this.word_label = (TextView)findViewById(R.id.word_label);
		
		this.view_answer_button = (Button)findViewById(R.id.view_answer_button);
		this.view_answer_button.setOnClickListener(this);
		
		this.def_label = (WebView)findViewById(R.id.def_label);
		
		this.easy_button = (Button)findViewById(R.id.easy_button);
		this.easy_button.setOnClickListener(this);
		this.not_bad_button = (Button)findViewById(R.id.not_bad_button);
		this.not_bad_button.setOnClickListener(this);
		this.hard_button = (Button)findViewById(R.id.hard_button);
		this.hard_button.setOnClickListener(this);
		this.i_forgot_button = (Button)findViewById(R.id.i_forgot_button);
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
		this.def_label.setVisibility(View.INVISIBLE);
		this.easy_button.setVisibility(View.INVISIBLE);
		this.not_bad_button.setVisibility(View.INVISIBLE);
		this.hard_button.setVisibility(View.INVISIBLE);
		this.i_forgot_button.setVisibility(View.INVISIBLE);
		
		switch(ReviewActivity.reviewMode){
			case ReviewManager.EXERCISES_SCHEDULED_TODAY: 
				this.cardSet = ReviewActivity.reviewManager.cardsMgr.loadCardsScheduledForToday();
				break;
			case ReviewManager.EXERCISES_CARDS_LOOKEDUP_TODAY:
				this.cardSet = ReviewActivity.reviewManager.cardsMgr.loadCardsLookedupToday();
				break;
			case ReviewManager.EXERCISES_CARDS_TOP_N_HARDEST:
				this.cardSet = ReviewActivity.reviewManager.cardsMgr.loadTopNHardestCards(20);
				break;
			default:
				System.out.println("Bad argument here in FlashCardActivity");
		}
		
		this.progress_label.setText("Card " + (cardSetIndex + 1) + " of "+  (this.cardSet.size()) );
		
		Card c = cardSet.elementAt(cardSetIndex);
		
		System.out.println("Before: " + c.eh.toString());
		
		this.word_label.setText(c.question);
		this.def_label.loadData(c.answer, "text/html", "utf-8");
	}
	
	public void onClick(View v) {
		if(cardSetIndex < cardSet.size()){
			if(R.id.view_answer_button == v.getId()){
				this.view_answer_button.setVisibility(View.INVISIBLE);
				this.def_label.setVisibility(View.VISIBLE);
				this.easy_button.setVisibility(View.VISIBLE);
				this.not_bad_button.setVisibility(View.VISIBLE);
				this.hard_button.setVisibility(View.VISIBLE);
				this.i_forgot_button.setVisibility(View.VISIBLE);
			}
			else {
				Card c = cardSet.elementAt(cardSetIndex);
				c.adjustEasinessByGrade(getGradeByButton(v));
				c.schedule();
				
				ReviewActivity.reviewManager.cardsMgr.save(c);
				
				System.out.println("After: " + c.eh.toString());
				
				cardSetIndex++;
				
				if(cardSetIndex < cardSet.size()){
					this.progress_label.setText("Card " + (cardSetIndex + 1) + " of "+  (this.cardSet.size()) );
					
					this.word_label.setText(cardSet.elementAt(cardSetIndex).question);
					this.def_label.loadData(cardSet.elementAt(cardSetIndex).answer, "text/html", "utf-8");
					
					this.view_answer_button.setVisibility(View.VISIBLE);
					this.def_label.setVisibility(View.INVISIBLE);
					this.easy_button.setVisibility(View.INVISIBLE);
					this.not_bad_button.setVisibility(View.INVISIBLE);
					this.hard_button.setVisibility(View.INVISIBLE);
					this.i_forgot_button.setVisibility(View.INVISIBLE);
				}
				else{
					new StatisticsManager(RDictActivity.db).saveOrUpdateCardStackStatistics();
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
