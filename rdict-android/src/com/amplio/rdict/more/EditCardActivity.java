package com.amplio.rdict.more;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplio.rdict.R;
import com.amplio.rdict.RDictActivity;
import com.amplio.rdict.review.Card;
import com.amplio.rdict.review.CardSetManager;

public class EditCardActivity extends Activity implements OnClickListener{

	TextView headwordLabel = null;
	EditText definitionText = null;
	Button cancelButton = null;
	Button saveButton = null;
	Button deleteButton = null;
	
	public static CardSetManager cardSetMgr = null;
	
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
		
		EditCardActivity.cardSetMgr = new CardSetManager(RDictActivity.db);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		EditCardActivity.cardSetMgr = new CardSetManager(RDictActivity.db);
		
		this.headwordLabel.setText(ManageActivity.targetCard.question);
		this.definitionText.setText(ManageActivity.targetCard.answer.replace("%20", " "));
	}

	public void onClick(View v) {
		if(this.saveButton == v){
			Card c = ManageActivity.targetCard;
			c.answer = this.definitionText.getText().toString().replace(" ", "%20");
			EditCardActivity.cardSetMgr.save(c);
			Toast.makeText(this, "Changes saved.", Toast.LENGTH_SHORT).show();
			this.finish();
		}
		else if(this.deleteButton == v){
			new AlertDialog.Builder(this)
			.setTitle("Delete")
			.setMessage("Delete this card?")
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
														Card c = ManageActivity.targetCard;
														EditCardActivity.cardSetMgr.deleteCard(c); finish();}})
			.setNegativeButton("No", new DialogInterface.OnClickListener() { 
				public void onClick(DialogInterface dialog, int whichButton) {}})
			.show();
		}
		else if(this.cancelButton == v){
			this.finish();
		}
	}
	
}