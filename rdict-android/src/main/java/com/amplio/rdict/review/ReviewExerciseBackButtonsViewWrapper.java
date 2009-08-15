package com.amplio.rdict.review;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.amplio.rdict.R;
import com.amplio.rdict.RDictActivity;

public class ReviewExerciseBackButtonsViewWrapper implements OnClickListener {
	private FlashCardActivity m_a = null;
	private View m_view = null;
	
	Button m_easyButton = null;
	Button m_notBadButton = null;
	Button m_hardButton = null;
	Button m_iForgotButton = null;
	
	public ReviewExerciseBackButtonsViewWrapper(Context context, FlashCardActivity a) {		
		this.m_view = View.inflate(context, R.layout.review_exercise_buttons_back, null);
		
		this.m_easyButton = (Button) this.m_view.findViewById(R.id.easy_button);
		this.m_easyButton.setOnClickListener(this);
		this.m_notBadButton = (Button) this.m_view.findViewById(R.id.not_bad_button);
		this.m_notBadButton.setOnClickListener(this);
		this.m_hardButton = (Button) this.m_view.findViewById(R.id.hard_button);
		this.m_hardButton.setOnClickListener(this);
		this.m_iForgotButton = (Button) this.m_view.findViewById(R.id.i_forgot_button);
		this.m_iForgotButton.setOnClickListener(this);
		
		this.m_a = a;
	}

	public void onClick(View v) {
		v.setPressed(false);
	    
		FlashCardActivity.m_exerciseMgr.userPressedAnEasinessButton(this.getGradeByButton(v));
	    RDictActivity.c_cardSetManager.save(FlashCardActivity.m_exerciseMgr.getCard());
	    
	    RDictActivity.RDICT_ACTIVITY.updateReviewTabIndicator();
	    
	    FlashCardActivity.m_exerciseMgr.next();
		
	    if(ReviewExerciseManager.STATE_USER_FINISHED_EXERCISE != FlashCardActivity.m_exerciseMgr.getState()) {
	    	this.m_a.drawDisplay();
		}
		else{
			RDictActivity.c_statisticsManager.saveOrUpdateCardStackStatistics();
			this.m_a.finish();
		}
    }
	
	private int getGradeByButton(View view) {
		if(view.equals(this.m_easyButton)) return ReviewExerciseManager.GRADE_EASY;
		else if (view.equals(this.m_notBadButton)) return ReviewExerciseManager.GRADE_NOT_BAD;
		else if (view.equals(this.m_hardButton)) return ReviewExerciseManager.GRADE_HARD;
		else return ReviewExerciseManager.GRADE_I_FORGOT;
	}

	public View getView() {
	    return this.m_view;
    }

}
