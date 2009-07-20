package com.amplio.rdict.more;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplio.rdict.R;
import com.amplio.rdict.RDictActivity;
import com.amplio.rdict.review.Card;
import com.amplio.rdict.review.CardSetManager;

public class AddCardActivity extends Activity implements OnClickListener{

	EditText headwordText = null;
	EditText definitionText = null;
	Button cancelButton = null;
	Button saveButton = null;
	
	public static CardSetManager cardSetMgr = null;
	
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
		
		AddCardActivity.cardSetMgr = new CardSetManager(RDictActivity.db);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		AddCardActivity.cardSetMgr = new CardSetManager(RDictActivity.db);
	}

	public void onClick(View v) {
		if(this.saveButton == v){
			Card c = new Card(this.headwordText.getText().toString(), this.definitionText.getText().toString().replace(" ", "%20"));

			//AddCardActivity.cardSetMgr.
			
			
			AddCardActivity.cardSetMgr.save(c);
			Toast.makeText(this, "Card added.", Toast.LENGTH_SHORT).show();
			this.finish();
		}
		else if(this.cancelButton == v){
			this.finish();
		}
	}
	
}
