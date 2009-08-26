package com.amplio.rdict.review;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amplio.rdict.R;
import com.amplio.rdict.RDictActivity;

public class ReviewActivity extends Activity implements Runnable{
	public static int reviewMode = ReviewManager.EXERCISES_CARD_DB_IS_EMPTY;
	public static final String MESG_NO_EXERCISES_AVAILABLE = "No Review Exercises are available.";
	public static final String MESG_CARDS_SCHEDULED_FOR_TODAY = "You have cards scheduled for today.";
	public static final String MESG_NO_CARDS_SCHEDULED_FOR_TODAY = "No cards are scheduled for today.";
	public static final String MESG_GOOD_JOB_YOURE_FINISHED = "Good Job! You finished today's review session.";
	
	private TextView reviewMesg = null;
	
	private LinearLayout exerciseLayout = null;
	private CardExerciseWrapper scheduledViewWrapper = null;
	private CardExerciseWrapper todayViewWrapper = null;
	
	private LinearLayout graphLayout = null;
	private ReviewGraphViewWrapper cardCountGraph = null;
	private ReviewGraphViewWrapper gradeGraph = null;
	
	private Handler graphViewHandler = null;
	
	@Override
	public void onCreate(Bundle icicle){
		super.onCreate(icicle);
		setContentView(R.layout.review);
		
		this.reviewMesg = (TextView) findViewById(R.id.review_mesg);
		
		this.exerciseLayout = (LinearLayout) findViewById(R.id.exercise_layout);
		
		this.scheduledViewWrapper = CardExerciseWrapper.buildScheduledCardExercise(this.getApplicationContext());
		this.todayViewWrapper = CardExerciseWrapper.buildLookedupTodayCardExercise(this.getApplicationContext());
		
		this.graphLayout = (LinearLayout) findViewById(R.id.graph_layout);
		
		this.cardCountGraph = new ReviewGraphViewWrapper(this.getApplicationContext(), "Total Cards: ");
		this.graphLayout.addView( this.cardCountGraph.getView());
		
		this.gradeGraph = new ReviewGraphViewWrapper(this.getApplicationContext(), "Grade: ");
		this.graphLayout.addView( this.gradeGraph.getView());
		
		this.graphViewHandler = new Handler();
	}
	
	public void onResume() {
		super.onResume();
		
		RDictActivity.c_reviewManager.checkAvailableExercises();
		
		this.reviewMesg.setText(this.getGreetingMessage());
		
		this.setupExerciseViews();
		this.setupGraphViews();
	}
	
	public String getGreetingMessage() {
		if(RDictActivity.c_reviewManager.isAvailableTodaysScheduledExercise){
			return MESG_CARDS_SCHEDULED_FOR_TODAY;
		}
		else {
			StringBuffer sb = new StringBuffer();

			String todaysDate = new SimpleDateFormat().format(new Date());
			
			if(! existsStatRecordForDate(todaysDate)) {
				if(RDictActivity.c_reviewManager.isAvailableLookedupTodayExercise)
					sb.append(MESG_NO_CARDS_SCHEDULED_FOR_TODAY);
				else
					sb.append(MESG_NO_EXERCISES_AVAILABLE);
			}
			else
				sb.append(MESG_GOOD_JOB_YOURE_FINISHED);
						
			return sb.toString();
		}
	}
	
	private boolean existsStatRecordForDate( String todaysDate ) {
	    return null != RDictActivity.c_statisticsManager.loadStatRecordByDate(todaysDate);
    }
	
	private void setupExerciseViews() {
		this.exerciseLayout.removeAllViews();		
		
		Vector<CardExerciseWrapper> exercises = this.getExercises();
		
		for( CardExerciseWrapper e : exercises){
			this.exerciseLayout.addView(e.getView());
		}	
    }
	
	public Vector<CardExerciseWrapper> getExercises() {
		Vector<CardExerciseWrapper> exercises = new Vector<CardExerciseWrapper>();
		if(RDictActivity.c_reviewManager.isAvailableTodaysScheduledExercise){
			exercises.add(this.scheduledViewWrapper);
			return exercises;
		}
		else {
			if(RDictActivity.c_reviewManager.isAvailableLookedupTodayExercise)
				exercises.add(this.todayViewWrapper);
			return exercises;
		}
	}
	
	public void setupGraphViews() {
		new Thread(this).start();
	}
	
	public void run() {
		Calendar c = Calendar.getInstance(); 
		c.add(Calendar.HOUR, -24 * 30);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String oneMonthAgo = sdf.format(c.getTime());
		
		Number[] cardCountData = RDictActivity.c_statisticsManager.fetchCardCountData(oneMonthAgo);
		String numCardsAddedToday = cardCountData[cardCountData.length - 1].toString();
		
		this.cardCountGraph.setValueAndData( numCardsAddedToday, cardCountData, this.graphViewHandler, this.cardCountGraph.getDrawGraphRunnable());
		this.cardCountGraph.getView().refreshDrawableState();
		
		Number[] gradeData = RDictActivity.c_statisticsManager.fetchGradeData(oneMonthAgo);
		String todaysGrade = gradeData[gradeData.length - 1].toString() + " %";
	
		this.gradeGraph.setValueAndData( todaysGrade, gradeData, this.graphViewHandler, this.gradeGraph.getDrawGraphRunnable());
		this.gradeGraph.getView().refreshDrawableState();		
	}
}
