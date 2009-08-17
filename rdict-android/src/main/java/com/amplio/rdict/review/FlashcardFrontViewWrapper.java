package com.amplio.rdict.review;

import android.content.Context;
import android.view.View;

import com.amplio.rdict.R;

public class FlashcardFrontViewWrapper {
	private View m_view = null;
	
	ScalableTextView m_frontWordLabel = null;
	
	public FlashcardFrontViewWrapper(Context context) {
		this.m_view = View.inflate(context, R.layout.flashcard_front, null);
		this.m_frontWordLabel = (ScalableTextView) this.m_view.findViewById(R.id.front_headword_label);
	}

	public void setWord( String question ) {
		this.m_frontWordLabel.setText(question);
    }

	public View getView() {
	    return this.m_view;
    }
}
