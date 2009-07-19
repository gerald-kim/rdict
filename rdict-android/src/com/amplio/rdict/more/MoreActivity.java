package com.amplio.rdict.more;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.amplio.rdict.R;

public class MoreActivity extends Activity implements OnClickListener {
	
	Button helpButton = null;
	Button aboutButton = null;
	Button manageButton = null;
	Button settingsButton = null;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.more);
		
		this.helpButton = (Button)findViewById(R.id.help_button);
		this.helpButton.setOnClickListener(this);
		
		this.aboutButton = (Button)findViewById(R.id.about_button);
		this.aboutButton.setOnClickListener(this);
		
		this.manageButton = (Button)findViewById(R.id.manage_button);
		this.manageButton.setOnClickListener(this);
		
		this.settingsButton = (Button)findViewById(R.id.settings_button);
		this.settingsButton.setOnClickListener(this);
	}

	public void onClick(View view) {
		if (this.helpButton == view) {
			Intent i = new Intent(view.getContext(), HelpActivity.class);
			view.getContext().startActivity(i);
		}
		else if (this.aboutButton == view) {
			Intent i = new Intent(view.getContext(), AboutActivity.class);
			view.getContext().startActivity(i);
		}
		else if (this.manageButton == view) {
			Intent i = new Intent(view.getContext(), ManageActivity.class);
			view.getContext().startActivity(i);			
		}
		else if (this.settingsButton == view) {
			Intent i = new Intent(view.getContext(), SettingsActivity.class);
			view.getContext().startActivity(i);
		}
	}
}
