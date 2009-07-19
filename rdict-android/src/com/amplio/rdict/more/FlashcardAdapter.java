package com.amplio.rdict.more;

import com.amplio.rdict.R;
import com.amplio.rdict.review.Card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;

public class FlashcardAdapter extends ArrayAdapter<Card> implements OnClickListener {

	Context context = null;
	
	public FlashcardAdapter(Context context, int viewResourceId) {
		super(context, viewResourceId);
		this.context = context;
	}
	
	public View getView(int position, View view, ViewGroup parent) {
		FlashcardViewWrapper fv = new FlashcardViewWrapper(View.inflate(this.context, R.layout.flashcard_ui_for_manage, null));
		fv.view.setTag(fv);
		
		Card c = this.getItem(position);
		
		fv.headwordLabel.setText(c.question);
		fv.definitionLabel.setText(c.answer.replace("%20", " "));
		
		fv.editButton.setOnClickListener(this);
		
		return fv.view;
	}

	public void onClick(View v) {
				
	}
}
