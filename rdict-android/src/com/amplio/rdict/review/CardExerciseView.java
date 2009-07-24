package com.amplio.rdict.review;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CardExerciseView extends View implements OnClickListener {
	
	public static CardExerciseView buildTopNCardExercise(Context context){
		CardExerciseView c = new CardExerciseView(context);
		c.greeting = "Practice your top 20 Hardest Cards:";
		c.reviewMode = ReviewManager.EXERCISES_CARDS_TOP_N_HARDEST;
		return c;
	}
	
	public static CardExerciseView buildScheduledCardExercise(Context context){
		CardExerciseView c = new CardExerciseView(context);
		c.greeting = "You have cards to practice today.";
		c.reviewMode = ReviewManager.EXERCISES_SCHEDULED_TODAY;
		return c;
	}
	
	public static CardExerciseView buildLookedupTodayCardExercise(Context context){
		CardExerciseView c = new CardExerciseView(context);
		c.greeting = "Practice the cards you've looked up today:";
		c.reviewMode = ReviewManager.EXERCISES_CARDS_LOOKEDUP_TODAY;
		return c;
	}
	
	public String greeting = null;
	public int reviewMode = -1;
	
	public TextView exerciseLabel = null;
	public Button exerciseButton = null;
	
	public CardExerciseView(Context context) {
		super(context);
		
		this.exerciseLabel = new TextView(context);
		this.exerciseLabel.setText(this.greeting);
		
		this.exerciseButton = new Button(context);
		this.exerciseButton.setText("Go!");
		this.exerciseButton.setOnClickListener(this);
	}

	public void addToLayout(LinearLayout layout) {
		layout.addView(this.exerciseLabel);
		layout.addView(this.exerciseButton);
	}
	
	public void onClick(View v) {
		ReviewActivity.reviewMode = this.reviewMode;
		
		Intent i = new Intent(v.getContext(), FlashCardActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		v.getContext().startActivity(i);
	}
}