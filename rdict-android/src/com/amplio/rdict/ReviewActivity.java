package com.amplio.rdict;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReviewActivity extends Activity{
	
	private ReviewManager _reviewManager = null;
	private TextView scheduledTodayLabel = null;
	private Button scheduledTodayButton = null;
	
	private TextView practiceAnotherSetLabel = null;
	
	private TextView top20HardestLabel = null;
	private Button top20HardestButton = null;
	
	private TextView lookedupTodayLabel = null;
	private Button lookedupTodayButton = null;
	
	@Override
	public void onCreate(Bundle icicle){
		super.onCreate(icicle);
		setContentView(R.layout.review);
		
		this.scheduledTodayLabel = (TextView)findViewById(R.id.welcome_mesg);
		this.scheduledTodayButton = (Button)findViewById(R.id.todays_cards_go);
		
		this.practiceAnotherSetLabel = (TextView)findViewById(R.id.practice_another_label);
		
		this.top20HardestLabel = (TextView)findViewById(R.id.top_20_label);
		this.top20HardestButton = (Button)findViewById(R.id.top_20_go);
		
		this.lookedupTodayLabel = (TextView)findViewById(R.id.lookedup_today_label);
		this.lookedupTodayButton = (Button)findViewById(R.id.lookedup_today_go);
		
		 ObjectContainer db = Db4o.openFile(this.getApplicationContext().getFilesDir() + "/" + "browseandroid.db4o");
		
		_reviewManager = new ReviewManager(db);
		_reviewManager.checkAvailableStudyModes();
		
		this.scheduledTodayLabel.setText(_reviewManager.getReviewHomeGreeting());
		
		if(ReviewManager.PRACTICE_SCHEDULED_TODAY == _reviewManager.studyMode){
			this.scheduledTodayButton.setVisibility(View.VISIBLE);
			
			this.practiceAnotherSetLabel.setVisibility(View.INVISIBLE);
			
			this.top20HardestLabel.setVisibility(View.INVISIBLE);
			this.top20HardestButton.setVisibility(View.INVISIBLE);
			
			this.lookedupTodayLabel.setVisibility(View.INVISIBLE);
			this.lookedupTodayButton.setVisibility(View.INVISIBLE);
			
		}
		
		else {
			this.scheduledTodayButton.setVisibility(View.INVISIBLE);
			
			this.practiceAnotherSetLabel.setVisibility(View.VISIBLE);
			
			this.top20HardestLabel.setVisibility(View.VISIBLE);
			this.top20HardestButton.setVisibility(View.VISIBLE);
			
			this.lookedupTodayLabel.setVisibility(View.VISIBLE);
			this.lookedupTodayButton.setVisibility(View.VISIBLE);
			
		}
	}
}
