package com.amplio.rdict.review;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.amplio.rdict.R;

public class FlashcardBackViewWrapper {
	
	private View view = null;
	
	ScalableTextView backWordLabel = null;
	TextView defLabel = null;
	
	public FlashcardBackViewWrapper(Context context) {
		this.view = View.inflate(context, R.layout.flashcard_back, null);
		this.backWordLabel = (ScalableTextView) this.view.findViewById(R.id.back_headword_label);
		this.defLabel = (TextView) this.view.findViewById(R.id.definition_label);
	}

	public View getView() {
	    return view;
    }

	public void setWordAndDef( String question, String answer) {
		this.backWordLabel.setText(question);
		this.defLabel.setText(answer);
    }

}
