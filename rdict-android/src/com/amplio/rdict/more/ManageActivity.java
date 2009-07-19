package com.amplio.rdict.more;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.amplio.rdict.R;
import com.amplio.rdict.RDictActivity;
import com.amplio.rdict.review.Card;
import com.amplio.rdict.review.CardSetManager;
import com.db4o.ObjectSet;

public class ManageActivity extends Activity implements OnItemClickListener {
	
	CardSetManager cardMgr = null;
	
	ListView cardList = null;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.manage);
		
		this.cardMgr = new CardSetManager(RDictActivity.db);
		
		this.cardList = (ListView) findViewById(R.id.card_list_view);
		this.cardList.setOnItemClickListener(this);
	}
	
	public void onResume() {
		super.onResume();
		String prefix = "";
		ObjectSet cards = this.cardMgr.loadCardsByPrefix(prefix);
		
		ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
		
		System.out.println(cards.size());
		
		for(int i=0; i < cards.size(); i++) {
			//		show a flashcard to user
			Card card = (Card) cards.get(i);
			
			aa.add(card.question + "\n" + card.answer);
		}

		this.cardList.setAdapter(aa);
		aa.notifyDataSetChanged();
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

}
