package com.amplio.rdict.more;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplio.rdict.R;
import com.amplio.rdict.RDictActivity;
import com.amplio.rdict.review.Card;

public class AddCardActivity extends Activity implements OnClickListener{

	EditText headwordText = null;
	EditText definitionText = null;
	Button cancelButton = null;
	Button saveButton = null;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.add_flashcard);
		
		this.headwordText = (EditText) findViewById(R.id.headword_text);
		this.definitionText = (EditText) findViewById(R.id.definition_text);
		this.cancelButton = (Button) findViewById(R.id.cancel_button);
		this.cancelButton.setOnClickListener(this);
		this.saveButton = (Button) findViewById(R.id.save_button);
		this.saveButton.setOnClickListener(this);
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	public void onClick(View v) {
		if(this.saveButton == v){
			Card c = new Card(this.headwordText.getText().toString(), this.definitionText.getText().toString());

			Card c2 = null;
			try {
				c2 = RDictActivity.c_cardSetManager.loadCardByHeadword(c.question);
			}
			catch(IllegalStateException e){
				e.printStackTrace();
			}
			
			if(null == c2) {
				RDictActivity.c_cardSetManager.save(c);
				Toast.makeText(this, "Card added.", Toast.LENGTH_SHORT).show();
				this.finish();
			}
			else {
				new AlertDialog.Builder(this)
				.setTitle("Delete")
				.setMessage("A card for \'" + c.question +  "\' already exists in the database.  Please edit the existing card, or choose a different title.")
				.setNeutralButton("Ok", new DialogInterface.OnClickListener() { 
					public void onClick(DialogInterface dialog, int whichButton) {}})
				.show();
			}
		}
		else if(this.cancelButton == v){
			this.finish();
		}
	}
	
}
