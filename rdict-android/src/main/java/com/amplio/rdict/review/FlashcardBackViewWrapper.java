package com.amplio.rdict.review;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.amplio.rdict.R;

public class FlashcardBackViewWrapper {
	
	private View m_view = null;
	
	TextView m_backWordLabel = null;
	TextView m_defLabel = null;
	
	public FlashcardBackViewWrapper(Context context) {
		this.m_view = View.inflate(context, R.layout.flashcard_back, null);
		this.m_backWordLabel = (TextView) this.m_view.findViewById(R.id.back_headword_label);
		this.m_defLabel = (TextView) this.m_view.findViewById(R.id.definition_label);
	}

	public View getView() {
	    return m_view;
    }

	public void setWordAndDef( String question, String answer) {
		this.m_backWordLabel.setText(question);
		this.m_defLabel.setText(answer);
    }

}
