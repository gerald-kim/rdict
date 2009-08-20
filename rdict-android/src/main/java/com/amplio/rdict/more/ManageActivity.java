package com.amplio.rdict.more;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.amplio.rdict.R;
import com.amplio.rdict.RDictActivity;
import com.amplio.rdict.review.Card;

public class ManageActivity extends Activity implements OnClickListener, TextWatcher {
	
	ListView cardList = null;
	
	Button addButton = null;
	EditText filterText = null;
	
	public static Card targetCard = null;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.manage);
		
		this.cardList = (ListView) findViewById(R.id.card_list_view);
		this.addButton = (Button) findViewById(R.id.add_button);
		this.addButton.setOnClickListener(this);
		this.filterText = (EditText) findViewById(R.id.filter_text);
		this.filterText.addTextChangedListener(this);
	}
	
	public void onResume() {
		super.onResume();
		
		List<Card> cards = RDictActivity.c_cardSetManager.allCards();
		FlashcardAdapter aa = new FlashcardAdapter(getApplicationContext(), R.layout.flashcard_ui_for_manage, cards);
		this.cardList.setAdapter(aa);
		aa.notifyDataSetChanged();
	}

	public void onClick(View v) {
		Intent i = new Intent(this.getApplicationContext(), AddCardActivity.class);
		this.startActivity(i);
	}

	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		
	}

	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
//		aa.notifyDataSetChanged();
		cardList.setSelectionFromTop( RDictActivity.c_cardSetManager.findCardIndex( s.toString() ), 0 );
	}
}
