package com.amplio.rdict.more;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.amplio.rdict.R;
import com.amplio.rdict.RDictActivity;
import com.amplio.rdict.review.Card;
import com.amplio.rdict.review.CardSetManager;
import com.db4o.ObjectSet;

public class ManageActivity extends Activity {
	
	CardSetManager cardMgr = null;
	
	ListView cardList = null;
	
	public static Card targetCard = null;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.manage);
		
		this.cardList = (ListView) findViewById(R.id.card_list_view);
	}
	
	public void onResume() {
		super.onResume();
		this.cardMgr = new CardSetManager(RDictActivity.db);
		
		String prefix = "";
		ObjectSet cards = this.cardMgr.loadCardsByPrefix(prefix);
		
		FlashcardAdapter aa = new FlashcardAdapter(getApplicationContext(), android.R.layout.simple_list_item_1);
		
		for(int i = 0; i < cards.size(); i++) {
			aa.add((Card) cards.get(i));
		}

		this.cardList.setAdapter(aa);
		aa.notifyDataSetChanged();
	}
}
