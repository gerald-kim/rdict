package com.amplio.rdict.review;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplio.rdict.R;
import com.amplio.rdict.RDictActivity;

public class ReviewActivity extends Activity implements OnClickListener{
	public static int reviewMode = ReviewManager.EXERCISES_CARD_DB_IS_EMPTY;
	public static ReviewManager reviewManager = null;
	
	private TextView scheduledTodayLabel = null;
	private Button scheduledTodayButton = null;
	
	private TextView practiceAnotherSetLabel = null;
	
	private TextView top20HardestLabel = null;
	private Button top20HardestButton = null;
	
	private TextView lookedupTodayLabel = null;
	private Button lookedupTodayButton = null;
	
	private ImageView cardCountGraph = null;
	private ImageView gradeGraph = null;
	
	@Override
	public void onCreate(Bundle icicle){
		super.onCreate(icicle);
		setContentView(R.layout.review);
		
		this.scheduledTodayLabel = (TextView)findViewById(R.id.welcome_mesg);
		this.scheduledTodayButton = (Button)findViewById(R.id.todays_cards_go);
		this.scheduledTodayButton.setOnClickListener(this);
		
		this.practiceAnotherSetLabel = (TextView)findViewById(R.id.practice_another_label);
		
		this.top20HardestLabel = (TextView)findViewById(R.id.top_20_label);
		this.top20HardestButton = (Button)findViewById(R.id.top_20_go);
		this.top20HardestButton.setOnClickListener(this);
		
		this.lookedupTodayLabel = (TextView)findViewById(R.id.lookedup_today_label);
		this.lookedupTodayButton = (Button)findViewById(R.id.lookedup_today_go);
		this.lookedupTodayButton.setOnClickListener(this);
		
		this.cardCountGraph = (ImageView)findViewById(R.id.card_count_graph);
		this.gradeGraph = (ImageView)findViewById(R.id.grade_graph);
	}
	
	public void onClick(View view){
		if(view == this.scheduledTodayButton){
			reviewMode = ReviewManager.EXERCISES_SCHEDULED_TODAY;
		}
		else if (view == this.lookedupTodayButton) {
			reviewMode = ReviewManager.EXERCISES_CARDS_LOOKEDUP_TODAY;
		}
		else {
			reviewMode = ReviewManager.EXERCISES_CARDS_TOP_N_HARDEST;
		}
		
		Intent i = new Intent(view.getContext(), FlashCardActivity.class);
		view.getContext().startActivity(i);
	}
	
	public void onResume() {
		System.out.println("Review - Resumed");
		
		final Number[] data = new Integer[] { 5, 22, 16, 8, 30, 9, 12, 27, 19, 22 };

		// width, height, spacing
		final SizeParams params = new SizeParams(50, 12, 1);

		final Bitmap i = AndroidBarGraph.createGraph(data);

		this.cardCountGraph.setImageBitmap(i);
		this.cardCountGraph.setBackgroundColor(Color.BLUE);
		this.cardCountGraph.refreshDrawableState();
		
		this.gradeGraph.setImageBitmap(i);
		this.gradeGraph.setBackgroundColor(Color.RED);
		this.gradeGraph.refreshDrawableState();
		
		super.onResume();
		
		reviewManager = new ReviewManager(RDictActivity.db);
		
		reviewManager.checkAvailableExercises();
		
		this.scheduledTodayLabel.setText(getReviewHomeGreeting());
		
		if(reviewManager.isAvailableTodaysScheduledExercise){
			this.scheduledTodayButton.setVisibility(View.VISIBLE);
			
			this.practiceAnotherSetLabel.setVisibility(View.INVISIBLE);
			
			this.top20HardestLabel.setVisibility(View.INVISIBLE);
			this.top20HardestButton.setVisibility(View.INVISIBLE);
			
			this.lookedupTodayLabel.setVisibility(View.INVISIBLE);
			this.lookedupTodayButton.setVisibility(View.INVISIBLE);
		}
		else if (reviewManager.isAvailableLookedupTodayExercise || reviewManager.isAvailableTOPNExercise) {
			
			this.scheduledTodayButton.setVisibility(View.INVISIBLE);
			this.practiceAnotherSetLabel.setVisibility(View.VISIBLE);
			
			if(reviewManager.isAvailableLookedupTodayExercise){
				this.lookedupTodayLabel.setVisibility(View.VISIBLE);
				this.lookedupTodayButton.setVisibility(View.VISIBLE);
			}
			if(reviewManager.isAvailableTOPNExercise){
				this.top20HardestLabel.setVisibility(View.VISIBLE);
				this.top20HardestButton.setVisibility(View.VISIBLE);
			}
		}
		else {
			this.scheduledTodayLabel.setText("There are no cards to review.");
			
			this.scheduledTodayButton.setVisibility(View.INVISIBLE);
			
			this.practiceAnotherSetLabel.setVisibility(View.INVISIBLE);
			
			this.top20HardestLabel.setVisibility(View.INVISIBLE);
			this.top20HardestButton.setVisibility(View.INVISIBLE);
			
			this.lookedupTodayLabel.setVisibility(View.INVISIBLE);
			this.lookedupTodayButton.setVisibility(View.INVISIBLE);
		}
	}
	
	public void onPause(){
		System.out.println("Review - paused");
		
		super.onPause();
	}
	
	public void onStart(){
		System.out.println("Review - Started");
		super.onStart();
	}
	
	 public void onStop(){
    	super.onStop();
    }
	 
	 public String getReviewHomeGreeting() {
		if(reviewManager.isAvailableTodaysScheduledExercise)
			return "You have cards to practice today.";
		else
			return "You have finished studying for today.";
	}
}
