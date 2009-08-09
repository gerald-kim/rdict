package com.amplio.rdict.review;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.amplio.rdict.R;

public class CardExerciseWrapper implements OnClickListener {
	
	public static final int N = 10;

	public static CardExerciseWrapper buildScheduledCardExercise(Context context){
		CardExerciseWrapper c = new CardExerciseWrapper(context);
		c.init("There are Cards scheduled for practice Today.", ReviewManager.EXERCISES_SCHEDULED_TODAY);
		return c;
	}
	
	public static CardExerciseWrapper buildLookedupTodayCardExercise(Context context){
		CardExerciseWrapper c = new CardExerciseWrapper(context);
		c.init("Practice the cards you've looked up today.", ReviewManager.EXERCISES_CARDS_LOOKEDUP_TODAY);
		return c;
	}
		
	View v = null;
	public TextView exerciseLabel = null;
	public Button exerciseButton = null;
	
	public int reviewMode = -1;
	
	public CardExerciseWrapper(Context context) {
		this.v = View.inflate( context, R.layout.card_exercise, null);
		
		this.exerciseLabel = (TextView) this.v.findViewById(R.id.exercise_label ); 
		this.exerciseButton = (Button) this.v.findViewById(R.id.start_exercise_button); 
		this.exerciseButton.setOnClickListener(this);
	}
	
	public void init(String greeting, int reviewMode) {
		this.exerciseLabel.setText(greeting);
		this.reviewMode = reviewMode;
	}

	public View getView() {
		return this.v;
	}
	
	public void onClick(View v) {
		ReviewActivity.reviewMode = this.reviewMode;
		
		Intent i = new Intent(v.getContext(), FlashCardActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		v.getContext().startActivity(i);
	}
}