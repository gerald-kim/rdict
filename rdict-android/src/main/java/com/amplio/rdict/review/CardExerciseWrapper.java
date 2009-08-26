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
		c.init("Start today's review session.", ReviewManager.EXERCISES_SCHEDULED_TODAY);
		return c;
	}
	
	public static CardExerciseWrapper buildLookedupTodayCardExercise(Context context){
		CardExerciseWrapper c = new CardExerciseWrapper(context);
		c.init("Early Practice: Review cards you created today.", ReviewManager.EXERCISES_CARDS_LOOKEDUP_TODAY);
		return c;
	}
		
	View view = null;
	public TextView exerciseLabel = null;
	public Button exerciseButton = null;
	
	public int reviewMode = -1;
	
	public CardExerciseWrapper(Context context) {
		this.view = View.inflate( context, R.layout.card_exercise, null);
		
		this.exerciseLabel = (TextView) this.view.findViewById(R.id.exercise_label ); 
		this.exerciseButton = (Button) this.view.findViewById(R.id.start_exercise_button); 
		this.exerciseButton.setOnClickListener(this);
	}
	
	public void init(String greeting, int reviewMode) {
		this.exerciseLabel.setText(greeting);
		this.reviewMode = reviewMode;
	}

	public View getView() {
		return this.view;
	}
	
	public void onClick(View v) {
		ReviewActivity.reviewMode = this.reviewMode;
		
		Intent i = new Intent(v.getContext(), FlashCardActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		v.getContext().startActivity(i);
	}
}