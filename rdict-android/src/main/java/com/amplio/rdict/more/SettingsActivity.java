package com.amplio.rdict.more;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.amplio.rdict.R;
import com.amplio.rdict.RDictActivity;
import com.amplio.rdict.setup.DictionaryDownloader;

public class SettingsActivity extends Activity implements OnClickListener{
	
	Button clearButton = null;
	Button deleteButton = null;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.settings);
		
		this.clearButton = (Button)findViewById(R.id.clear_button);
		this.clearButton.setOnClickListener(this);
		
		this.deleteButton = (Button)findViewById(R.id.delete_button);
		this.deleteButton.setOnClickListener(this);
	}

	public void onClick( View view ) {
		if (this.clearButton == view) {
			new AlertDialog.Builder(this)
			.setTitle("Clear Search History")
			.setMessage("Are you sure you want to clear your Search History?")
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				        public void onClick( DialogInterface dialog, int whichButton ) {
//					        Card c = ManageActivity.targetCard;
//					        RDictActivity.c_cardSetManager.deleteCard( c );
//					        RDictActivity.c_statisticsManager.saveOrUpdateCardStackStatistics();
//					        finish();
				        }
			        } )
			.setNegativeButton("No", new DialogInterface.OnClickListener() { 
						public void onClick(DialogInterface dialog, int whichButton) {}})
			.show();
		}
		else if (this.deleteButton == view) {
			new AlertDialog.Builder(this)
			.setTitle("Delete Dictionary")
			.setMessage("Are you sure you want to delete your Dictionary Files?")
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				        public void onClick( DialogInterface dialog, int whichButton ) {
				        	//new File(DictionaryDownloader.WRITE_PATH_DB).delete();
							new File(DictionaryDownloader.WRITE_PATH_INDEX).delete();
							RDictActivity.RDICT_ACTIVITY.finish();
				        }
			        } )
			.setNegativeButton("No", new DialogInterface.OnClickListener() { 
						public void onClick(DialogInterface dialog, int whichButton) {}})
			.show();
		}
    }
}
