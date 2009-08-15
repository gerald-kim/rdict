package com.amplio.rdict.review;

import android.app.AlertDialog;
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
	Button m_helpButton = null;
	
	public ReviewExerciseFrontButtonsViewWrapper(Context context, FlashCardActivity a) {		
		this.m_context = context;
		this.m_view = View.inflate(m_context, R.layout.review_exercise_buttons_front, null);
		this.m_viewAnswerButton = (Button) this.m_view.findViewById(R.id.view_answer_button);
		this.m_viewAnswerButton.setOnClickListener(this);
		this.m_helpButton = (Button) this.m_view.findViewById(R.id.front_help_button);
		this.m_helpButton.setOnClickListener(this);
		
		this.m_a = a;
	}

	public void onClick(View v) {
		if(v == this.m_viewAnswerButton) {
			FlashCardActivity.m_exerciseMgr.userPressedViewAnswerButton();
			
			v.setPressed(false);
			
		    m_a.drawDisplay();
		}
		else {
			new AlertDialog.Builder(this.m_a)
			.setTitle("Help")
			.setMessage("Can you remember this word?  Think about the definition.\n\nWhen you remember, of if you decide you can't remember, push the 'View Answer' button.")
			.setNeutralButton("Ok", null)
			.show();
		}
    }

	public View getView() {
	    return this.m_view;
    }

}
