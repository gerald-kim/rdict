package com.amplio.rdict.setup;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PromptForDBDownloadView extends SetupView implements OnClickListener {
	TextView mesg = null;
	
	Button laterButton = null;
	Button nowButton = null;
	
	public PromptForDBDownloadView(Context context, SetupActivity setupActivity) {
		super(context, setupActivity);
		
		this.mesg = new TextView(context);
		this.mesg.setText("Do you want to download the db now?");
		
		this.laterButton = new Button(context);
		this.laterButton.setText("Later");
		this.laterButton.setOnClickListener(this);
		
		this.nowButton = new Button(context);
		this.nowButton.setText("Now");
		this.nowButton.setOnClickListener(this);
	}
	
	@Override
	public void addToLayout(LinearLayout l) {
		l.addView(this.mesg);
		l.addView(this.laterButton);
		l.addView(this.nowButton);
	}

	public void onClick(View v) {
		if(v == this.nowButton)
			SetupActivity.setupMgr.userChoseDownloadOption();
		else
			SetupActivity.setupMgr.userChoseToDelayDownlaod();
		
		this.setupActivity.updateLayout();
	}
}
