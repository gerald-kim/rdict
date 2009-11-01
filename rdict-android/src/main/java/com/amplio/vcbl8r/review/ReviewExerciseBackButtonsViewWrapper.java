package com.amplio.vcbl8r.review;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.amplio.vcbl8r.R;
import com.amplio.vcbl8r.RDictActivity;

public class ReviewExerciseBackButtonsViewWrapper implements OnClickListener {
	private FlashCardActivity a = null;
	private View view = null;
	
	Button easyButton = null;
	Button notBadButton = null;
	Button hardButton = null;
	Button iForgotButton = null;
	
	Button helpButton = null;
	
	public ReviewExerciseBackButtonsViewWrapper(Context context, FlashCardActivity a) {		
		this.view = View.inflate(context, R.layout.review_exercise_buttons_back, null);
		
		this.easyButton = (Button) this.view.findViewById(R.id.easy_button);
		this.easyButton.setOnClickListener(this);
		this.notBadButton = (Button) this.view.findViewById(R.id.not_bad_button);
		this.notBadButton.setOnClickListener(this);
		this.hardButton = (Button) this.view.findViewById(R.id.hard_button);
		this.hardButton.setOnClickListener(this);
		this.iForgotButton = (Button) this.view.findViewById(R.id.i_forgot_button);
		this.iForgotButton.setOnClickListener(this);
		
		this.helpButton = (Button) this.view.findViewById(R.id.back_help_button);
		this.helpButton.setOnClickListener(this);
		
		this.a = a;
	}

	public void onClick(View v) {
		v.setPressed(false);
	    
		if(v != this.helpButton) {
			FlashCardActivity.exerciseMgr.userPressedAnEasinessButton(this.getGradeByButton(v));
		    RDictActivity.cardSetManager.save(FlashCardActivity.exerciseMgr.getCard());
		    
		    RDictActivity.RDICT_ACTIVITY.updateReviewTabIndicator();
		    
		    FlashCardActivity.exerciseMgr.next();
			
		    if(ReviewExerciseManager.STATE_USER_FINISHED_EXERCISE != FlashCardActivity.exerciseMgr.getState()) {
		    	this.a.drawDisplay();
			}
			else{
				RDictActivity.statisticsManager.saveOrUpdateCardStackStatistics();
				this.a.finish();
			}
	    }
	    else {
			new AlertDialog.Builder(this.a)
			.setTitle("Help")
			.setMessage("How easy was it to remember the word?\n\nTell Vocabulator by pressing one of the buttons.")
			.setNeutralButton("Ok", null)
			.show();
		}
    }
	
	private int getGradeByButton(View view) {
		if(view.equals(this.easyButton)) return ReviewExerciseManager.GRADE_EASY;
		else if (view.equals(this.notBadButton)) return ReviewExerciseManager.GRADE_NOT_BAD;
		else if (view.equals(this.hardButton)) return ReviewExerciseManager.GRADE_HARD;
		else return ReviewExerciseManager.GRADE_I_FORGOT;
	}

	public View getView() {
	    return this.view;
    }

}
