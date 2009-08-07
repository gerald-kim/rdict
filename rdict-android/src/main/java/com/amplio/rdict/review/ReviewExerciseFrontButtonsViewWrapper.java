package com.amplio.rdict.review;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.amplio.rdict.R;

public class ReviewExerciseFrontButtonsViewWrapper implements OnClickListener {
	private Context m_context = null;
	private FlashCardActivity m_a = null;
	private View m_view = null;
	Button m_viewAnswerButton = null;
	
	public ReviewExerciseFrontButtonsViewWrapper(Context context, FlashCardActivity a) {		
		this.m_context = context;
		this.m_view = View.inflate(m_context, R.layout.review_exercise_buttons_front, null);
		this.m_viewAnswerButton = (Button) this.m_view.findViewById(R.id.view_answer_button);
		this.m_viewAnswerButton.setOnClickListener(this);
		
		this.m_a = a;
	}

	public void onClick(View v) {
		FlashCardActivity.m_exerciseMgr.userPressedViewAnswerButton();
		
		v.setPressed(false);
		
	    m_a.drawDisplay();
    }

	public View getView() {
	    return this.m_view;
    }

}
