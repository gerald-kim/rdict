package com.amplio.rdict.more;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amplio.rdict.R;

public class EditCardActivity extends Activity implements OnClickListener{

	TextView headwordLabel = null;
	EditText definitionText = null;
	Button cancelButton = null;
	Button saveButton = null;
	Button deleteButton = null;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.edit_or_delete_flashcard);
		
		this.headwordLabel = (TextView) findViewById(R.id.headword_label);
		this.definitionText = (EditText) findViewById(R.id.definition_text);
		this.cancelButton = (Button) findViewById(R.id.cancel_button);
		this.cancelButton.setOnClickListener(this);
		this.saveButton = (Button) findViewById(R.id.save_button);
		this.saveButton.setOnClickListener(this);
		this.deleteButton = (Button) findViewById(R.id.delete_button);
		this.deleteButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		if(this.cancelButton == v) {
		
		}
		else if(this.saveButton == v){
			//save
		}
		else {
			//delete
		}
		
	}
	
}
