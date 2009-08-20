package com.amplio.rdict.more;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.amplio.rdict.R;
import com.amplio.rdict.review.Card;

public class FlashcardAdapter extends ArrayAdapter<Card> implements OnClickListener {

	public static final int ABBREVIATED_DEF_LENGTH = 50;
	
	Context context = null;
	
	public FlashcardAdapter(Context context, int viewResourceId) {
		super(context, viewResourceId);
		this.context = context;
	}
	
	public FlashcardAdapter( Context context, int textViewResourceId, List<Card> objects ) {
	    super( context, textViewResourceId, objects );
	    this.context = context;
    }

	public View getView(int position, View view, ViewGroup parent) {
		Card c = this.getItem(position);
		
		View fcView = View.inflate(this.context, R.layout.flashcard_ui_for_manage, null);
		((TextView) fcView.findViewById(R.id.headword_label)).setText(c.question);
		
		Button editButton = (Button) fcView.findViewById(R.id.edit_button);
		editButton.setTag(new Integer(position).toString());
		editButton.setOnClickListener(this);
		
		((TextView) fcView.findViewById(R.id.definition_label)).setText(c.answer);
		
		return fcView;
	}

	public void onClick(View v) {
		Card c = this.getItem( new Integer(v.getTag().toString()));
		
		ManageActivity.targetCard = c;
		
		Intent i = new Intent(context, EditCardActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}
}
