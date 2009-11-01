package com.amplio.vcbl8r.review;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.amplio.vcbl8r.R;

public class ReviewExerciseFrontButtonsViewWrapper implements OnClickListener {
	private FlashCardActivity a = null;
	private View view = null;
	Button viewAnswerButton = null;
	Button helpButton = null;
	
	public ReviewExerciseFrontButtonsViewWrapper(Context context, FlashCardActivity a) {		
		this.view = View.inflate(context, R.layout.review_exercise_buttons_front, null);
		this.viewAnswerButton = (Button) this.view.findViewById(R.id.view_answer_button);
		this.viewAnswerButton.setOnClickListener(this);
		this.helpButton = (Button) this.view.findViewById(R.id.front_help_button);
		this.helpButton.setOnClickListener(this);
		
		this.a = a;
	}

	public void onClick(View v) {
		if(v == this.viewAnswerButton) {
			FlashCardActivity.exerciseMgr.userPressedViewAnswerButton();
			
			v.setPressed(false);
			
		    a.drawDisplay();
		}
		else {
			new AlertDialog.Builder(this.a)
			.setTitle("Help")
			.setMessage("Can you remember this word?  Think about the definition.\n\nWhen you remember, or if you decide you can't remember, push the 'View Answer' button.")
			.setNeutralButton("Ok", null)
			.show();
		}
    }

	public View getView() {
	    return this.view;
    }

}
