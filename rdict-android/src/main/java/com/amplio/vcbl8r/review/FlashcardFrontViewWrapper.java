package com.amplio.vcbl8r.review;

import android.content.Context;
import android.view.View;

import com.amplio.vcbl8r.R;

public class FlashcardFrontViewWrapper {
	private View view = null;
	
	ScalableTextView frontWordLabel = null;
	
	public FlashcardFrontViewWrapper(Context context) {
		this.view = View.inflate(context, R.layout.flashcard_front, null);
		this.frontWordLabel = (ScalableTextView) this.view.findViewById(R.id.front_headword_label);
	}

	public void setWord( String question ) {
		this.frontWordLabel.setText(question);
    }

	public View getView() {
	    return this.view;
    }
}
