package com.amplio.vcbl8r.more;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.amplio.vcbl8r.R;
import com.amplio.vcbl8r.RDictActivity;

public class SettingsActivity extends Activity implements OnClickListener{
	
	Button clearButton = null;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.settings);
		
		this.clearButton = (Button)findViewById(R.id.clear_button);
		this.clearButton.setOnClickListener(this);
	}

	public void onClick( View view ) {
		if (this.clearButton == view) {
			new AlertDialog.Builder(this)
			.setTitle("Clear Search History")
			.setMessage("Are you sure you want to clear your Search History?")
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				        public void onClick( DialogInterface dialog, int whichButton ) {
				        	RDictActivity.historyMgr.clearHistory();
				        }
			        } )
			.setNegativeButton("No", new DialogInterface.OnClickListener() { 
						public void onClick(DialogInterface dialog, int whichButton) {}})
			.show();
		}
    }
}
