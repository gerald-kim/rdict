package com.amplio.rdict.review;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amplio.rdict.R;
import com.amplio.rdict.RDictActivity;

public class ReviewActivity extends Activity {
	public static int reviewMode = ReviewManager.EXERCISES_CARD_DB_IS_EMPTY;
	public static ReviewManager reviewManager = null;
	public static StatisticsManager statManager = null;
	
	private TextView noCardsScheduledTodayLabel = null;
	private TextView finishedScheduledTodayLabel = null;
	private TextView practiceAnotherSetLabel = null;
	
	private CardExerciseView scheduledView = null;
	private CardExerciseView topNView = null;
	private CardExerciseView todayView = null;
	
	private ImageView cardCountGraph = null;
	private ImageView gradeGraph = null;
	
	private LinearLayout exerciseLayout = null;
	
	@Override
	public void onCreate(Bundle icicle){
		super.onCreate(icicle);
		setContentView(R.layout.review);
		
		this.noCardsScheduledTodayLabel = new TextView(this.getApplicationContext());
		this.noCardsScheduledTodayLabel.setText("There are no cards scheduled for today.");
		
		this.finishedScheduledTodayLabel = new TextView(this.getApplicationContext());
		this.finishedScheduledTodayLabel.setText("Good Job! You've finished studying the cards that were scheduled for today.");
		
		this.practiceAnotherSetLabel = new TextView(this.getApplicationContext());
		this.practiceAnotherSetLabel.setText("Practice another card set:");
		
		this.exerciseLayout = (LinearLayout) findViewById(R.id.exercise_layout);
		
		this.scheduledView = CardExerciseView.buildScheduledCardExercise(this.getApplicationContext());
		this.topNView = CardExerciseView.buildTopNCardExercise(this.getApplicationContext());
		this.todayView = CardExerciseView.buildLookedupTodayCardExercise(this.getApplicationContext());
		
		this.cardCountGraph = (ImageView)findViewById(R.id.card_count_graph);
		this.gradeGraph = (ImageView)findViewById(R.id.grade_graph);
	}
	
	public void onResume() {
		super.onResume();
		
		System.out.println("Review - Resumed");
		
		this.exerciseLayout.removeAllViews();
		
		reviewManager = new ReviewManager(RDictActivity.db);
		reviewManager.checkAvailableExercises();
		
		if(reviewManager.isAvailableTodaysScheduledExercise){
			this.exerciseLayout.addView(this.scheduledView);
		}
		else if (reviewManager.isAvailableLookedupTodayExercise || reviewManager.isAvailableTOPNExercise) {
			
			String todaysDate = new SimpleDateFormat().format(new Date());
			
			if(null == new StatisticsManager(RDictActivity.db).loadStatRecordByDate(todaysDate))
				this.exerciseLayout.addView(this.noCardsScheduledTodayLabel);
			else
				this.exerciseLayout.addView(this.finishedScheduledTodayLabel);
			
			this.exerciseLayout.addView(this.practiceAnotherSetLabel);
			
			if(reviewManager.isAvailableLookedupTodayExercise)
				this.todayView.addToLayout(this.exerciseLayout);

			if(reviewManager.isAvailableTOPNExercise)
				this.topNView.addToLayout(this.exerciseLayout);
		}
		else {
			this.exerciseLayout.addView(this.noCardsScheduledTodayLabel);
		}
		
		Calendar c = Calendar.getInstance(); 
		c.add(Calendar.MONTH, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String oneMonthAgo = sdf.format(c.getTime());
		
		ReviewActivity.statManager = new StatisticsManager(RDictActivity.db);
		Number[] cardCountData = ReviewActivity.statManager.fetchCardCountData(oneMonthAgo);
		Number[] gradeData = ReviewActivity.statManager.fetchGradeData(oneMonthAgo);
		
		Bitmap cardCountBitmap = this.prepareSparkline(cardCountData, false);
	    this.cardCountGraph.setImageBitmap(cardCountBitmap);
		this.cardCountGraph.setBackgroundColor(Color.WHITE);
		this.cardCountGraph.refreshDrawableState();
		
		Bitmap gradeBitmap = this.prepareSparkline(gradeData, true);
		this.gradeGraph.setImageBitmap(gradeBitmap);
		this.gradeGraph.setBackgroundColor(Color.WHITE);
		this.gradeGraph.refreshDrawableState();
	}
	
	public Bitmap prepareSparkline(Number[] data, boolean isPercentageGraph) {
		int width = 200;
		int height = 60;
		int spacing = 1;
		
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
	    Canvas canvas = new Canvas(bitmap);
	    Paint paint = new Paint();
	    Sparkline sl = new Sparkline(width, height, data, spacing, isPercentageGraph);
	    sl.setupRectangles();
	    sl.draw(canvas, paint);
	    
		return bitmap;
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
}
