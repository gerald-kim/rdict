package com.amplio.rdict.more;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amplio.rdict.R;

public class FlashcardViewWrapper {

	View view = null;
	
	TextView headwordLabel = null;
	Button editButton = null;
	TextView definitionLabel = null;
	
	public FlashcardViewWrapper(View view) {
		this.view = view;
		this.headwordLabel = (TextView) this.view.findViewById(R.id.headword_label);
		this.editButton = (Button) this.view.findViewById(R.id.edit_button);
		this.definitionLabel = (TextView) this.view.findViewById(R.id.definition_label);
	}

}
